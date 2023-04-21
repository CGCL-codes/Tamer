package org.zxframework.doc;

import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zxframework.datasources.DSHRdbms;
import org.zxframework.datasources.DSHandler;
import org.zxframework.datasources.DSRS;
import org.zxframework.exception.NestableRuntimeException;
import org.zxframework.util.DomElementUtil;
import org.zxframework.util.StringUtil;
import org.zxframework.util.ToStringBuilder;
import org.zxframework.util.XMLGen;
import org.zxframework.ZXException;
import org.zxframework.zXType;
import org.zxframework.logging.Log;
import org.zxframework.logging.LogFactory;

/**
 * The implementation of the doc-builder loop-over action
 * 
 * <pre>
 * 
 * Who    : Bertus Dispa
 * When   : 19 May 2003
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
public class DBLoopOver extends DBAction {

    private static Log log = LogFactory.getLog(DBLoopOver.class);

    private String queryname;

    private String action;

    private String active;

    private static final String DBLOOPOVER_QUERYNAME = "queryname";

    private static final String DBLOOPOVER_ACTION = "action";

    private static final String DBLOOPOVER_ACTIVE = "active";

    /**
	 * Default constructor.
	 */
    public DBLoopOver() {
        super();
    }

    /**
	 * @return Returns the action.
	 */
    public String getAction() {
        return action;
    }

    /**
	 * @param action The action to set.
	 */
    public void setAction(String action) {
        this.action = action;
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
     * @see org.zxframework.doc.DBAction#dumpAsXML(XMLGen)
     */
    public void dumpAsXML(XMLGen objXMLGen) {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        try {
            super.dumpAsXML(objXMLGen);
            objXMLGen.taggedCData(DBLOOPOVER_QUERYNAME, getQueryname(), false);
            objXMLGen.taggedCData(DBLOOPOVER_ACTION, getAction(), false);
            objXMLGen.taggedCData(DBLOOPOVER_ACTIVE, getActive(), false);
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
                    if (DBLOOPOVER_QUERYNAME.equalsIgnoreCase(nodeName)) {
                        setQueryname(element.getText());
                    } else if (DBLOOPOVER_ACTION.equalsIgnoreCase(nodeName)) {
                        setAction(element.getText());
                    } else if (DBLOOPOVER_ACTIVE.equalsIgnoreCase(nodeName)) {
                        setActive(element.getText());
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
            DBEntity objEntity;
            Iterator iter = colEntities.values().iterator();
            while (iter.hasNext()) {
                objEntity = (DBEntity) iter.next();
                objEntity.setResolvedLoadGroup(getZx().resolveDirector(objEntity.getLoadgroup()));
            }
            if (objDSHandler.getDsType().pos == zXType.dsType.dstChannel.pos) {
                objRS = objDSHandler.boRS(objTheEntity.getBo(), objTheEntity.getResolvedLoadGroup(), strWhereClause, true, strOrderByClause, blnReverse, 0, 0);
            } else {
                objRS = ((DSHRdbms) objDSHandler).sqlRS(strQry);
            }
            while (!objRS.eof()) {
                colEntities = pobjDocBuilder.resolveEntities(this);
                if (colEntities == null) {
                    throw new ZXException("Failed to get entities");
                }
                iter = colEntities.values().iterator();
                while (iter.hasNext()) {
                    objEntity = (DBEntity) iter.next();
                    objRS.rs2obj(objEntity.getBo(), objEntity.getResolvedLoadGroup());
                }
                pobjDocBuilder.handleQS(this);
                if (pobjDocBuilder.isActive(this.active)) {
                    int intRC = pobjDocBuilder.processActions(this.action).pos;
                    if (intRC == zXType.rc.rcOK.pos) {
                    } else if (intRC == zXType.rc.rcWarning.pos) {
                    } else {
                        throw new ZXException("Failed to execute action", this.action);
                    }
                }
                objRS.moveNext();
            }
            return go;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Process DBLoopOver action.", e);
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
        DBLoopOver objDBAction = null;
        try {
            objDBAction = (DBLoopOver) super.clone();
            objDBAction.setQueryname(getQueryname());
            objDBAction.setAction(getAction());
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
        toString.append(DBLOOPOVER_ACTION, getAction());
        toString.append(DBLOOPOVER_ACTIVE, getActive());
        toString.append(DBLOOPOVER_QUERYNAME, getQueryname());
        return toString.toString();
    }
}
