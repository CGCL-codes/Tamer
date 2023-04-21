package orcajo.azada.core.model.job;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import orcajo.azada.core.model.QueryModelBase;
import orcajo.azada.core.olap.internal.WhithMembersSupport;
import orcajo.azada.core.util.OlapUtil;
import orcajo.azada.core.util.Util;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;

class QueryModelReader extends QueryModelBase {

    private Element eQuery;

    private String datasourceInfoName = null;

    private String catalogInfoName = null;

    private WhithMembersSupport witMembers;

    private QueryModelReader(String datasourceInfoName, String catalogInfoName, Element eQuery) {
        super(null);
        this.eQuery = eQuery;
        this.datasourceInfoName = datasourceInfoName;
        this.catalogInfoName = catalogInfoName;
        witMembers = new WhithMembersSupport();
    }

    static Query read(String datasourceInfoName, String catalogInfoName, Element eQuery) throws SQLException, JDOMException {
        return new QueryModelReader(datasourceInfoName, catalogInfoName, eQuery).read();
    }

    private Query read() throws SQLException, JDOMException {
        Query query = null;
        if (eQuery != null && datasourceInfoName != null && catalogInfoName != null) {
            Cube cube = getCube();
            if (cube == null) {
                throw new OlapException("Cube no found");
            }
            String name = datasourceInfoName + "_" + catalogInfoName;
            query = new Query(name, cube);
            List<?> list = eQuery.getChildren(AXIS);
            for (Object value : list) {
                if (value instanceof Element) {
                    Element eAxis = (Element) value;
                    populateQuery(query, eAxis);
                }
            }
            query.validate();
        }
        return query;
    }

    private void populateQuery(Query query, Element eAxis) throws JDOMException, OlapException {
        Attribute at = eAxis.getAttribute(ORDINAL);
        if (at == null) {
            throw new JDOMException("Attribute " + ORDINAL + " no found.");
        }
        int ordinal = at.getIntValue();
        QueryAxis axisQuery = query.getAxes().get(Axis.Factory.forOrdinal(ordinal));
        if (axisQuery == null) {
            throw new OlapException("QueryAxis no found.");
        }
        Attribute atNonEmpty = eAxis.getAttribute(NON_EMPTY);
        if (atNonEmpty != null) {
            axisQuery.setNonEmpty(atNonEmpty.getBooleanValue());
        }
        Element eSort = eAxis.getChild(SORT_ORDER);
        if (eSort != null) {
            String sortEvaluationLiteral = eSort.getTextTrim();
            Attribute aSort = eSort.getAttribute(SORT_ORDER);
            if (aSort != null && sortEvaluationLiteral.length() > 0) {
                String sorName = aSort.getValue();
                SortOrder order = SortOrder.valueOf(sorName);
                axisQuery.sort(order, sortEvaluationLiteral);
            }
        }
        List<?> qDims = eAxis.getChildren(QUERY_DIMENSION);
        for (Object value : qDims) {
            if (value instanceof Element) {
                Element eDim = (Element) value;
                populateDimension(query, axisQuery, eDim);
            }
        }
    }

    private void populateDimension(Query query, QueryAxis axisQuery, Element eDim) throws JDOMException, OlapException {
        Cube cube = query.getCube();
        String name = getText(eDim.getChild(NAME), NAME);
        QueryDimension queryDim = null;
        if (name != null) {
            queryDim = query.getDimension(name);
        } else {
            Element eeDim = eDim.getChild(DIMENSION);
            if (eeDim != null) {
                name = getText(eeDim.getChild(NAME), NAME);
                Dimension dim = cube.getDimensions().get(name);
                if (name != null && dim != null && name.equals(dim.getName())) {
                    queryDim = query.getDimension(dim.getUniqueName());
                }
            }
        }
        if (queryDim == null) {
            name = (name == null) ? "" : name;
            throw new OlapException("QueryDimension " + name + " no found");
        }
        if (!axisQuery.getDimensions().contains(queryDim)) {
            axisQuery.getDimensions().add(queryDim);
        }
        Attribute at = eDim.getAttribute(SORT_ORDER);
        if (at != null) {
            name = at.getValue();
            SortOrder order = SortOrder.valueOf(name);
            queryDim.sort(order);
        }
        at = eDim.getAttribute(HIERARCHIZE_MODE);
        if (at != null) {
            name = at.getValue();
            HierarchizeMode hierarchizeMode = HierarchizeMode.valueOf(name);
            queryDim.setHierarchizeMode(hierarchizeMode);
        }
        Element eInclusions = eDim.getChild(INCLUSIONS);
        if (eInclusions != null) {
            List<Selection> sels = getSelection(queryDim, eInclusions.getChildren(SELECTION));
            for (Selection selection : sels) {
                if (!queryDim.getInclusions().contains(selection)) {
                    queryDim.getInclusions().add(selection);
                }
            }
        }
        Element eExclusions = eDim.getChild(EXCLUSIONS);
        if (eExclusions != null) {
            List<Selection> sels = getSelection(queryDim, eExclusions.getChildren(SELECTION));
            for (Selection selection : sels) {
                queryDim.exclude(selection.getOperator(), (Member) selection.getRootElement());
            }
        }
    }

    private List<Selection> getSelection(QueryDimension queryDim, List<?> eSels) throws JDOMException, OlapException {
        Cube cube = queryDim.getQuery().getCube();
        List<Selection> sels = new ArrayList<Selection>();
        if (eSels != null) {
            for (Object value : eSels) {
                if (value instanceof Element) {
                    Element e = (Element) value;
                    if (SELECTION.equals(e.getName())) {
                        Element eLevel = e.getChild(LEVEL);
                        if (eLevel != null) {
                            Selection sLevel = getSelectionLevel(cube, queryDim, e);
                            sels.add(sLevel);
                        } else {
                            Selection sMember = getSelectionMember(cube, queryDim, e);
                            sels.add(sMember);
                        }
                    }
                }
            }
        }
        return sels;
    }

    private Selection getSelectionMember(Cube cube, QueryDimension queryDim, Element eSelection) throws JDOMException, OlapException {
        Element eMember = eSelection.getChild(MEMBER);
        Selection.Operator operator = null;
        Member member = null;
        if (eMember != null) {
            Attribute atOperator = eSelection.getAttribute(OPERATOR);
            if (atOperator != null) {
                String opName = atOperator.getValue();
                operator = Selection.Operator.valueOf(opName);
            }
            Element eFullName = eMember.getChild(FULL_QUALIFIEDAME);
            String[] parts = null;
            if (eFullName != null) {
                List<?> lParts = eFullName.getChildren(PART_NAME);
                if (lParts != null && !lParts.isEmpty()) {
                    parts = new String[lParts.size()];
                    for (int i = 0; i < lParts.size(); i++) {
                        parts[i] = ((Element) lParts.get(i)).getTextTrim();
                    }
                }
            }
            String uname = eMember.getTextTrim();
            Attribute isWihtMembe = eMember.getAttribute(IS_WITH_MEMBER);
            if (isWihtMembe != null && isWihtMembe.getBooleanValue()) {
                Element eExpression = eMember.getChild(MEMBER_EXPRESSION);
                String withMemberExpression = eExpression.getText();
                try {
                    List<Member> members = witMembers.createWhithMember(queryDim.getQuery().getWhithList(), withMemberExpression, datasourceInfoName, catalogInfoName, queryDim.getQuery().getCube().getUniqueName());
                    if (!members.isEmpty()) {
                        member = members.get(0);
                    }
                } catch (Exception e1) {
                    throw new JDOMException(Util.getRootLocalizedMessage(e1), e1);
                }
            } else {
                member = OlapUtil.getMember(cube, parts, uname);
            }
            if (member == null) {
                throw new JDOMException("Member no found: " + uname);
            }
        }
        if (operator == null) {
            throw new JDOMException("Selection.Operator no found");
        }
        Selection s = queryDim.createSelection(operator, member);
        if (s == null) {
            QueryDimension qd = queryDim.getQuery().getDimension(member.getDimension().getName());
            if (qd != null) {
                s = qd.createSelection(operator, member);
            }
        }
        if (s != null) {
            Element eContext = eSelection.getChild(SELECTION_CONTEXT);
            if (eContext != null && eContext.getChildren(SELECTION) != null) {
                List<Selection> lContext = getSelection(queryDim, eContext.getChildren(SELECTION));
                for (Selection selection : lContext) {
                    s.addContext(selection);
                }
            }
            return s;
        }
        return null;
    }

    private Selection getSelectionLevel(Cube cube, QueryDimension queryDim, Element eSelection) throws JDOMException, OlapException {
        Level level = null;
        Element eLevel = eSelection.getChild(LEVEL);
        Element eDim = eLevel.getChild(DIMENSION);
        if (eDim == null) {
            throw new JDOMException("Dimension no found");
        }
        Element eHier = eLevel.getChild(HIERARCHY);
        if (eHier == null) {
            throw new JDOMException("Hierarchy no found");
        }
        String dimension = eDim.getText();
        String hierarchy = eHier.getText();
        String name = eLevel.getTextNormalize();
        level = OlapUtil.getLevel(cube, dimension, hierarchy, name);
        if (level == null) {
            throw new JDOMException("Level no found");
        }
        Selection s = queryDim.createSelection(level);
        if (s == null) {
            QueryDimension qd = queryDim.getQuery().getDimension(level.getDimension().getName());
            if (qd != null) {
                s = qd.createSelection(level);
            }
        }
        if (s != null) {
            Element eContext = eSelection.getChild(SELECTION_CONTEXT);
            if (eContext != null && eContext.getChildren(SELECTION) != null) {
                List<Selection> lContext = getSelection(queryDim, eContext.getChildren(SELECTION));
                for (Selection selection : lContext) {
                    s.addContext(selection);
                }
            }
            return s;
        }
        return null;
    }

    private Cube getCube() throws SQLException, JDOMException {
        String schema = getText(eQuery.getChild(SCHEME_NAME), SCHEME_NAME);
        String cubeName = getText(eQuery.getChild(CUBE), CUBE);
        return OlapUtil.getCube(datasourceInfoName, catalogInfoName, schema, cubeName);
    }

    private String getText(Element e, String name) throws JDOMException {
        if (e == null) {
            throw new JDOMException("Element " + name + " no found.");
        }
        return e.getTextTrim();
    }
}
