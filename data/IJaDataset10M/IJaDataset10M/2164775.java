package org.zxframework.doc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zxframework.Tuple;
import org.zxframework.ZXException;
import org.zxframework.zXType;
import org.zxframework.datasources.DSHRdbms;
import org.zxframework.datasources.DSHandler;
import org.zxframework.datasources.DSRS;
import org.zxframework.exception.NestableRuntimeException;
import org.zxframework.util.CloneUtil;
import org.zxframework.util.DomElementUtil;
import org.zxframework.util.StringUtil;
import org.zxframework.util.ToStringBuilder;
import org.zxframework.util.XMLGen;
import org.zxframework.logging.Log;
import org.zxframework.logging.LogFactory;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTableCursor;

/**
 * The implementation of the grid merge doc builder action.
 * 
 * <pre>
 * 
 * TODO : Bug fixes.... Duplicate refobject unused menmGridType
 * 
 * Who    : Bertus Dispa
 * When   : 28 November 2003
 * 
 * Change    : BD10JUL04
 * Why       : Fixed problem with multi-line fields and Word engine
 *
 * Change    : BD5APR05 - V1.5:1
 * Why       : Added support for data-sources
 * </pre>
 *  
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * 
 * @version 0.0.1
 */
public class DBGridMerge extends DBAction {

    private static Log log = LogFactory.getLog(DBGridMerge.class);

    private DBObject refobject;

    private List values;

    private String queryname;

    private String fixedrows;

    private String fixedcols;

    private int maxrows;

    private boolean showNumRows;

    private zXType.docBuilderGridType gridType;

    private String active;

    private static final String DBGRIDMERGE_REFOBJECT = "refobject";

    private static final String DBGRIDMERGE_VALUES = "values";

    private static final String DBGRIDMERGE_QUERYNAME = "queryname";

    private static final String DBGRIDMERGE_FIXEDROWS = "fixedrows";

    private static final String DBGRIDMERGE_FIXEDCOLS = "fixedcols";

    private static final String DBGRIDMERGE_ACTIVE = "active";

    private static final String DBGRIDMERGE_MAXROWS = "maxrows";

    private static final String DBGRIDMERGE_SHOWNUMROWS = "shownumrows";

    private static final String DBGRIDMERGE_GRIDTYPE = "gridtype";

    /**
	 * Default constructor.
	 */
    public DBGridMerge() {
        super();
    }

    /**
	 * @return Returns the active.
	 */
    public String getActive() {
        return active;
    }

    /**
	 * @param active The active to set.
	 */
    public void setActive(String active) {
        this.active = active;
    }

    /**
	 * @return Returns the fixedcols.
	 */
    public String getFixedcols() {
        return fixedcols;
    }

    /**
	 * @param fixedcols The fixedcols to set.
	 */
    public void setFixedcols(String fixedcols) {
        this.fixedcols = fixedcols;
    }

    /**
	 * @return Returns the fixedrows.
	 */
    public String getFixedrows() {
        return fixedrows;
    }

    /**
	 * @param fixedrows The fixedrows to set.
	 */
    public void setFixedrows(String fixedrows) {
        this.fixedrows = fixedrows;
    }

    /**
	 * @return Returns the gridType.
	 */
    public zXType.docBuilderGridType getGridType() {
        return gridType;
    }

    /**
	 * @param gridType The gridType to set.
	 */
    public void setGridType(zXType.docBuilderGridType gridType) {
        this.gridType = gridType;
    }

    /**
	 * @return Returns the maxrows.
	 */
    public int getMaxrows() {
        return maxrows;
    }

    /**
	 * @param maxrows The maxrows to set.
	 */
    public void setMaxrows(int maxrows) {
        this.maxrows = maxrows;
    }

    /**
	 * @return Returns the queryname.
	 */
    public String getQueryname() {
        return queryname;
    }

    /**
	 * @param queryname The queryname to set.
	 */
    public void setQueryname(String queryname) {
        this.queryname = queryname;
    }

    /**
	 * @return Returns the refobject.
	 */
    public DBObject getRefobject() {
        return refobject;
    }

    /**
	 * @param refobject The refobject to set.
	 */
    public void setRefobject(DBObject refobject) {
        this.refobject = refobject;
    }

    /**
	 * @return Returns the shownumrows.
	 */
    public boolean isShowNumRows() {
        return showNumRows;
    }

    /**
	 * @param shownumrows The shownumrows to set.
	 */
    public void setShowNumRows(boolean shownumrows) {
        this.showNumRows = shownumrows;
    }

    /**
	 * @param shownumrows The shownumrows to set.
	 */
    public void setShownumrows(String shownumrows) {
        this.showNumRows = StringUtil.booleanValue(shownumrows);
    }

    /**
	 * A collection (ArrayList)(Tuple) of values
	 * @return Returns the values.
	 */
    public List getValues() {
        if (this.values == null) {
            this.values = new ArrayList();
        }
        return values;
    }

    /**
	 * @param values The values to set.
	 */
    public void setValues(List values) {
        this.values = values;
    }

    /**
	 * @param gridType The gridType to set.
	 */
    public void setGridtype(String gridType) {
        this.gridType = zXType.docBuilderGridType.getEnum(gridType);
    }

    /**
     * @see org.zxframework.doc.DBAction#dumpAsXML(XMLGen)
     */
    public void dumpAsXML(XMLGen objXMLGen) {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        try {
            super.dumpAsXML(objXMLGen);
            if (getRefobject() != null) {
                objXMLGen.openTag(DBGRIDMERGE_REFOBJECT);
                getRefobject().dumpAsXML(objXMLGen);
                objXMLGen.closeTag(DBGRIDMERGE_REFOBJECT);
            }
            objXMLGen.taggedCData(DBGRIDMERGE_FIXEDROWS, getFixedrows(), false);
            objXMLGen.taggedCData(DBGRIDMERGE_FIXEDCOLS, getFixedcols(), false);
            objXMLGen.taggedValue(DBGRIDMERGE_MAXROWS, String.valueOf(getMaxrows()));
            objXMLGen.taggedValue(DBGRIDMERGE_SHOWNUMROWS, isShowNumRows());
            objXMLGen.taggedCData(DBGRIDMERGE_ACTIVE, getActive(), false);
            if (getValues() != null) {
                objXMLGen.openTag(DBGRIDMERGE_VALUES);
                Tuple objTuple;
                int intValues = getValues().size();
                for (int i = 0; i < intValues; i++) {
                    objTuple = (Tuple) getValues().get(i);
                    objXMLGen.openTag("value");
                    objXMLGen.taggedCData(DBAction.DBACTION_TUPLE_NAME, objTuple.getName());
                    objXMLGen.taggedCData(DBAction.DBACTION_TUPLE_VALUE, objTuple.getValue());
                    objXMLGen.closeTag("value");
                }
                objXMLGen.closeTag(DBGRIDMERGE_VALUES);
            }
            objXMLGen.taggedCData(DBGRIDMERGE_QUERYNAME, getQueryname());
            objXMLGen.taggedValue(DBGRIDMERGE_GRIDTYPE, zXType.valueOf(getGridType()));
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Dump xml", e);
            throw new NestableRuntimeException("Failed to : Dump xml", e);
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * @see org.zxframework.doc.DBAction#parse(Element)
     */
    public zXType.rc parse(Element pobjXMLNode) {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        zXType.rc parse = zXType.rc.rcOK;
        try {
            DomElementUtil element;
            String nodeName;
            Node node;
            NodeList nodeList = pobjXMLNode.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                if (node instanceof Element) {
                    element = new DomElementUtil((Element) node);
                    nodeName = node.getNodeName();
                    if (DBGRIDMERGE_QUERYNAME.equalsIgnoreCase(nodeName)) {
                        setQueryname(element.getText());
                    } else if (DBGRIDMERGE_VALUES.equalsIgnoreCase(nodeName)) {
                        setValues(DBAction.parseTuples((Element) node));
                    } else if (DBGRIDMERGE_REFOBJECT.equalsIgnoreCase(nodeName)) {
                        this.refobject = new DBObject();
                        this.refobject.parse((Element) node);
                    } else if (DBGRIDMERGE_FIXEDROWS.equalsIgnoreCase(nodeName)) {
                        setFixedrows(element.getText());
                    } else if (DBGRIDMERGE_FIXEDCOLS.equalsIgnoreCase(nodeName)) {
                        setFixedcols(element.getText());
                    } else if (DBGRIDMERGE_ACTIVE.equalsIgnoreCase(nodeName)) {
                        setActive(element.getText());
                    } else if (DBGRIDMERGE_MAXROWS.equalsIgnoreCase(nodeName)) {
                        setMaxrows(Integer.parseInt(element.getText()));
                    } else if (DBGRIDMERGE_SHOWNUMROWS.equalsIgnoreCase(nodeName)) {
                        setShownumrows(element.getText());
                    } else if (DBGRIDMERGE_GRIDTYPE.equalsIgnoreCase(nodeName)) {
                        setGridtype(element.getText());
                    }
                }
            }
            return parse;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Parse Action", e);
            throw new NestableRuntimeException("Failed to : Parse Action", e);
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(parse);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
	 * @see org.zxframework.doc.DBAction#go(org.zxframework.doc.DocBuilder)
	 */
    public zXType.rc go(DocBuilder pobjDocBuilder) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        zXType.rc go = zXType.rc.rcOK;
        DSRS objRS = null;
        try {
            Map colEntities = pobjDocBuilder.resolveEntities(this);
            if (colEntities == null) {
                throw new ZXException("Failed to get entities");
            }
            if (pobjDocBuilder.validDataSourceEntities(colEntities)) {
                throw new ZXException("Unsupported combination of data-source handlers");
            }
            DBEntity objTheEntity = (DBEntity) colEntities.values().iterator().next();
            DSHandler objDSHandler = objTheEntity.getDsHandler();
            DBEntity objEntity;
            Iterator iter = colEntities.values().iterator();
            while (iter.hasNext()) {
                objEntity = (DBEntity) iter.next();
                objEntity.setResolvedLoadGroup(getZx().resolveDirector(objEntity.getLoadgroup()));
            }
            if (pobjDocBuilder.getObject(this.refobject, false).pos != zXType.rc.rcOK.pos) {
                throw new ZXException("Failed to getObject for refObject");
            }
            String strWhereClause = "";
            String strOrderByClause = "";
            String strQry = "";
            boolean blnReverse = false;
            if (objDSHandler.getDsType().pos == zXType.dsType.dstChannel.pos) {
                String strTmp = getZx().resolveDirector(this.queryname);
                strWhereClause = pobjDocBuilder.getFromContext(strTmp + DocBuilder.QRYWHERECLAUSE);
                strOrderByClause = pobjDocBuilder.getFromContext(strTmp + DocBuilder.QRYORDERBYCLAUSE);
                if (StringUtil.len(strWhereClause) > 0) {
                    strWhereClause = ":" + strWhereClause;
                }
                if (StringUtil.len(strOrderByClause) > 1 && strOrderByClause.charAt(0) == '-') {
                    strOrderByClause = strOrderByClause.substring(1);
                    blnReverse = true;
                }
            } else {
                strQry = pobjDocBuilder.constructQuery(getZx().resolveDirector(this.queryname));
                if (StringUtil.len(strQry) == 0) {
                    throw new ZXException("Failed to get query");
                }
            }
            int intFixedRows = 0;
            String strTmp = getZx().resolveDirector(this.fixedrows);
            if (StringUtil.isNumeric(strTmp)) intFixedRows = Integer.parseInt(strTmp);
            int intFixedCols = 0;
            strTmp = getZx().resolveDirector(this.fixedcols);
            if (StringUtil.isNumeric(strTmp)) intFixedCols = Integer.parseInt(strTmp);
            if (objDSHandler.getDsType().pos == zXType.dsType.dstChannel.pos) {
                objRS = objDSHandler.boRS(objTheEntity.getBo(), objTheEntity.getResolvedLoadGroup(), strWhereClause, true, strOrderByClause, blnReverse, 0, 0);
            } else {
                objRS = ((DSHRdbms) objDSHandler).sqlRS(strQry);
            }
            int intRow = intFixedRows;
            doneRS: while (!objRS.eof()) {
                iter = colEntities.values().iterator();
                while (iter.hasNext()) {
                    objEntity = (DBEntity) iter.next();
                    objRS.rs2obj(objEntity.getBo(), objEntity.getResolvedLoadGroup());
                }
                if (pobjDocBuilder.isActive(this.active)) {
                    intRow = intRow + 1;
                    if (this.maxrows > 0 && (intRow - intFixedRows) > this.maxrows) {
                        break doneRS;
                    }
                    if (intRow > 1 + intFixedRows) {
                        pobjDocBuilder.addRow(this.refobject);
                    }
                    Tuple objValue;
                    for (int i = 0; i < this.values.size(); i++) {
                        objValue = (Tuple) this.values.get(i);
                        strTmp = getZx().resolveDirector(objValue.getValue());
                        if (StringUtil.isNumeric(strTmp)) {
                            int intCol = intFixedCols + Integer.parseInt(strTmp);
                            strTmp = getZx().resolveDirector(objValue.getName());
                            pobjDocBuilder.setCell(this.refobject, intRow, intCol, strTmp);
                        }
                    }
                }
                objRS.moveNext();
            }
            if (isShowNumRows()) {
                strTmp = ((intRow - intFixedRows)) + " row" + (intRow - intFixedRows == 1 ? "" : "s") + " displayed";
                if (!objRS.eof()) {
                    strTmp = strTmp + " (result-set truncated)";
                }
                if (pobjDocBuilder.getDescriptor().getEnGine().equals(zXType.docBuilderEngineType.dbetWord9)) {
                    XTextTable xTextTable = (XTextTable) getRefobject().getWordobject();
                    XTableRows xRows = xTextTable.getRows();
                    xRows.insertByIndex(xRows.getCount(), 1);
                    Object aRow = xRows.getByIndex(xRows.getCount());
                    XTextTableCursor xTableCursor = QI.XTextTableCursor(aRow);
                    xTableCursor.mergeRange();
                    OpenOfficeDocument.setCell(getRefobject().getWordobject(), xRows.getCount(), 0, strTmp);
                }
            }
            if (getRefobject().isReCalc()) {
                if (pobjDocBuilder.getDescriptor().getEnGine().equals(zXType.docBuilderEngineType.dbetWord9)) {
                    OpenOfficeDocument.updateFields(((Document) pobjDocBuilder.getWordNewDoc()).wordDoc());
                }
            }
            objRS.RSClose();
            objRS = null;
            return go;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Process DBGridMerge action.", e);
            if (objRS != null) {
                objRS.RSClose();
            }
            if (getZx().throwException) throw new ZXException(e);
            go = zXType.rc.rcError;
            return go;
        } finally {
            if (objRS != null) {
                objRS.RSClose();
                objRS = null;
            }
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(go);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
	 * @see java.lang.Object#clone()
	 */
    public Object clone() {
        DBGridMerge objDBAction = null;
        try {
            objDBAction = (DBGridMerge) super.clone();
            if (getRefobject() != null) {
                objDBAction.setRefobject((DBObject) getRefobject().clone());
            }
            if (this.values != null && this.values.size() > 0) {
                objDBAction.setValues(CloneUtil.clone((ArrayList) this.values));
            }
            objDBAction.setQueryname(getQueryname());
            objDBAction.setFixedrows(getFixedrows());
            objDBAction.setFixedcols(getFixedcols());
            objDBAction.setMaxrows(getMaxrows());
            objDBAction.setShowNumRows(isShowNumRows());
            objDBAction.setGridType(getGridType());
            objDBAction.setActive(getActive());
        } catch (Exception e) {
            log.error("Failed to clone object", e);
        }
        return objDBAction;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.appendSuper(super.toString());
        toString.append(DBGRIDMERGE_ACTIVE, getActive());
        toString.append(DBGRIDMERGE_FIXEDCOLS, getFixedcols());
        toString.append(DBGRIDMERGE_FIXEDROWS, getFixedrows());
        toString.append(DBGRIDMERGE_GRIDTYPE, zXType.valueOf(getGridType()));
        toString.append(DBGRIDMERGE_MAXROWS, getMaxrows());
        toString.append(DBGRIDMERGE_QUERYNAME, getQueryname());
        toString.append(DBGRIDMERGE_REFOBJECT, getRefobject());
        toString.append(DBGRIDMERGE_SHOWNUMROWS, isShowNumRows());
        toString.append(DBGRIDMERGE_VALUES, getValues());
        return toString.toString();
    }
}
