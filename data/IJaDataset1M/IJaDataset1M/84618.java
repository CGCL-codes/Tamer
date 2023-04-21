package com.incendiaryblue.cmslite.search;

import com.incendiaryblue.cmslite.Category;
import com.incendiaryblue.cmslite.ConcreteCategoryLink;
import com.incendiaryblue.cmslite.Node;
import com.incendiaryblue.database.Database;
import com.incendiaryblue.database.SQLDialect;
import Zql.*;
import java.util.*;
import java.sql.*;
import com.incendiaryblue.appframework.AppConfig;

/**
 * This concrete subclass of SearchSelectQuery provides functionality for
 * building a content query on the incendiaryblue Java database.
 */
public class ContentSearchSelectQuery extends SearchSelectQuery {

    private String where, orderBy;

    private boolean recurse = false;

    private static SQLDialect dialect = ((Database) AppConfig.getDefaultComponent(Database.class)).getSQLDialect();

    /**
	 * Construct a new search query based on the given criteria object.
	 *
	 * @param criteria The criteria for the search
	 */
    public ContentSearchSelectQuery(ContentSearchCriteria criteria) {
        super(criteria.getNode(), criteria.getStructure());
        this.where = criteria.getWhere();
        this.orderBy = criteria.getOrderBy();
        this.recurse = criteria.getRecursive();
        String contentTableName = getContentTableName();
        String contentPrefix = getContentPrefix(true);
        Integer categoryId = criteria.getCategoryId();
        addTableAlias(contentTableName + " CONTENT");
        addTableAlias("CATEGORY_CONTENT CC");
        addTableAlias("CATEGORY CAT");
        if (criteria.getStructure() != null || criteria.getHandleType() != null) {
            addTableAlias("CATEGORY_CONTENT HOME_CC");
            addTableAlias("CATEGORY HOME_CAT");
            addTableAlias("STRUCTURE");
            addJoinExpression("HOME_CC.CONTENT_ID = CONTENT." + contentPrefix + "CONTENT_ID");
            addJoinExpression("HOME_CC.TYPE = " + Node.CONCRETE_CATEGORY);
            addJoinExpression("HOME_CC.CATEGORY_ID = HOME_CAT.CATEGORY_ID");
            addJoinExpression("HOME_CAT.STRUCTURE_ID = STRUCTURE.STRUCTURE_ID");
            if (criteria.getHandleType() != null) {
                addTableAlias("SYZHANDLE_TYPE HT");
                addJoinExpression("STRUCTURE.HANDLE_TYPE_ID = HT.HANDLE_TYPE_ID");
                addJoinExpression("HT.HANDLE_TYPE_ID = " + criteria.getHandleType().getPrimaryKey());
            } else {
                addJoinExpression("STRUCTURE.STRUCTURE_ID = " + criteria.getStructure().getPrimaryKey());
            }
        }
        addSelectField("CC.CONTENT_ID");
        addSelectField("CAT.TYPE");
        String currentDate = dialect.getCurrentDate();
        addJoinExpression("(CONTENT.FOREVER = 1 OR (CONTENT.VALID_DATE <= " + currentDate + " AND CONTENT.INVALID_DATE >= " + currentDate + "))");
        if (criteria.getRecursive()) {
            addTableAlias("CATEGORY_SUB CS");
            addJoinExpression("CS.CATEGORY_ID = " + categoryId);
            addJoinExpression("CS.SUB_CATEGORY_ID = CC.CATEGORY_ID");
            addSelectField("CS.SUB_CATEGORY_ID AS CATEGORY_ID");
            addSelectField("CS.ROOT_ID");
            addSelectField("CS.LINK_ID");
            Node n = criteria.getNode();
            if (n.getCategoryStub() instanceof Category) {
                addJoinExpression("CS.ROOT_ID = CS.CATEGORY_ID");
            }
        } else {
            addJoinExpression("CC.CATEGORY_ID = " + categoryId);
            addSelectField(categoryId + " AS CATEGORY_ID");
            addSelectField("-1 AS ROOT_ID");
            addSelectField("-1 AS LINK_ID");
        }
        addJoinExpression("CONTENT." + contentPrefix + "CONTENT_ID = CC.CONTENT_ID");
        addJoinExpression("CC.CATEGORY_ID = CAT.CATEGORY_ID");
        parseOrderBy();
        parseWhere();
    }

    private void parseOrderBy() {
        if (orderBy == null) {
            addSelectField("CONTENT.NAME");
            addOrderBy("CONTENT.NAME");
            return;
        }
        Vector orderByExpressions = SearchSyntax.parseOrderBy(orderBy);
        StringBuffer newOrderBy = new StringBuffer();
        if (orderByExpressions != null) {
            ZOrderBy zob;
            for (int i = 0; i < orderByExpressions.size(); i++) {
                zob = (ZOrderBy) orderByExpressions.elementAt(i);
                String fieldName = zob.getExpression().toString();
                String asc = zob.getAscOrder() ? "" : " DESC";
                String dbField = convertField(fieldName);
                addOrderBy(dbField + asc);
                addSelectField(dbField);
            }
        }
    }

    private void parseWhere() {
        this.setWhereClause(parseWhere(where));
    }

    public boolean getRecursive() {
        return recurse;
    }

    public List getResults(ResultSet rs) throws SQLException {
        List l = new ArrayList();
        int contentId, catId, rootId, type, linkId;
        String nodeDescriptor, prefix = "";
        if (this.getNode().isLinked()) {
            prefix = "/" + this.getNode().getNodeDescriptor() + ",";
        }
        while (rs.next()) {
            contentId = rs.getInt("CONTENT_ID");
            type = rs.getInt("TYPE");
            catId = rs.getInt("CATEGORY_ID");
            rootId = rs.getInt("ROOT_ID");
            linkId = rs.getInt("LINK_ID");
            String catRef = type + "|" + catId;
            if (this.getRecursive()) {
                nodeDescriptor = prefix;
                if ((type == Node.LOGICAL_CATEGORY) || (rootId == linkId)) {
                    nodeDescriptor += catRef;
                } else {
                    ConcreteCategoryLink link = ConcreteCategoryLink.getLink(new Integer(rootId), new Integer(linkId));
                    if (link == null) {
                        continue;
                    }
                    if (catId == linkId) {
                        nodeDescriptor += link.getNodeDescriptor();
                    } else {
                        nodeDescriptor += "/" + link.getNodeDescriptor() + "," + catRef;
                    }
                }
            } else {
                nodeDescriptor = this.getNode().getNodeDescriptor();
            }
            l.add(nodeDescriptor + "," + contentId);
        }
        return l;
    }
}
