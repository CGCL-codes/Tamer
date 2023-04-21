package netgest.bo.xwc.components.classic.attributeHtmlEditor;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import netgest.bo.def.boDefHandler;
import netgest.bo.runtime.EboContext;
import netgest.bo.runtime.boObject;
import netgest.bo.runtime.boObjectList;
import netgest.bo.runtime.boRuntimeException;
import netgest.bo.system.Logger;
import netgest.bo.system.boApplication;
import netgest.bo.xwc.components.classic.GridPanel;
import netgest.bo.xwc.components.connectors.DataRecordConnector;
import netgest.bo.xwc.components.connectors.XEOObjectListConnector;
import netgest.bo.xwc.framework.XUIRequestContext;
import netgest.bo.xwc.framework.XUIScriptContext;
import netgest.bo.xwc.framework.components.XUIViewRoot;
import netgest.bo.xwc.xeo.beans.XEOBaseLookupList;
import netgest.bo.xwc.xeo.beans.XEOEditBean;
import netgest.bo.xwc.xeo.beans.XEOViewerResolver;

public class X3OCM_util_lookup_Bean extends XEOBaseLookupList {

    Logger log = Logger.getLogger(X3OCM_util_lookup_Bean.class);

    private String cmpIdOrigin;

    private String winIdOrigin;

    private String obj2BeCalled;

    private String currentPlugin;

    private String currentObject;

    private Map<String, String> mapObjs;

    public X3OCM_util_lookup_Bean() throws boRuntimeException {
        XUIRequestContext oRequestContext;
        oRequestContext = XUIRequestContext.getCurrentContext();
        HttpServletRequest request = (HttpServletRequest) oRequestContext.getFacesContext().getExternalContext().getRequest();
        currentPlugin = request.getParameter("pluginToLoad");
        cmpIdOrigin = request.getParameter("cmpId");
        winIdOrigin = request.getParameter("winId");
        obj2BeCalled = request.getParameter("obj");
        String objsReq = request.getParameter("objsToList");
        objsReq = (objsReq != null && !"".equals(objsReq)) ? objsReq.replaceAll(",", ";") : "";
        objsReq = (!"".equals(objsReq)) ? objsReq : "Ebo_ClsReg";
        String objsToList[] = objsReq.split(";");
        mapObjs = new HashMap<String, String>();
        for (int i = 0; i < objsToList.length; i++) {
            String objName = objsToList[i];
            boDefHandler objDef = boDefHandler.getBoDefinition(objName.trim());
            if (objDef != null) {
                mapObjs.put(objDef.getName(), objDef.getLabel());
                objsToList[i] = objDef.getName();
            }
        }
        currentObject = objsToList[0];
        setSelectedObject(currentObject);
        setLookupObjects(mapObjs);
    }

    @Override
    public void setSelectedObject(String selectedObject) {
        try {
            super.setSelectedObject(selectedObject);
        } catch (Exception e) {
        }
    }

    @Override
    public void confirm() {
        boObject selectedObj = null;
        EboContext oEboContext = boApplication.currentContext().getEboContext();
        XUIRequestContext oRequestContext = XUIRequestContext.getCurrentContext();
        GridPanel oGridComp = (GridPanel) oRequestContext.getViewRoot().findComponent(GridPanel.class);
        DataRecordConnector currDataRec = oGridComp.getActiveRow();
        long currBoui = 0;
        if (currDataRec != null) {
            currBoui = Long.parseLong(oGridComp.getActiveRow().getAttribute("BOUI").getDisplayValue());
            try {
                selectedObj = boObject.getBoManager().loadObject(oEboContext, currBoui);
            } catch (boRuntimeException e) {
                log.severe(e);
            }
        }
        oRequestContext.getScriptContext().add(XUIScriptContext.POSITION_FOOTER, "function_ret", "try{parent.handleXeoRetPopupVal('" + currBoui + "', '" + cmpIdOrigin + "', '" + winIdOrigin + "', '" + obj2BeCalled + "', '" + selectedObj.getName() + "');}catch(err){alert(err.desctiption);}");
    }

    @Override
    public void addNew() throws Exception {
        String sObjectName = getCurrentObj();
        XUIRequestContext oRequestContext;
        oRequestContext = XUIRequestContext.getCurrentContext();
        XUIViewRoot oEditViewRoot = oRequestContext.getSessionContext().createChildView(getViewerResolver().getViewer(sObjectName, XEOViewerResolver.ViewerType.EDIT));
        XEOEditBean oEditBean = (XEOEditBean) oEditViewRoot.getBean("viewBean");
        oEditBean.createNew(sObjectName);
        oRequestContext.setViewRoot(oEditViewRoot);
        oEditViewRoot.processInitComponents();
        oRequestContext.renderResponse();
    }

    @Override
    public XEOObjectListConnector getDataList() {
        String currObj = getCurrentObj();
        String boql = "SELECT " + currObj + " ORDER BY SYS_DTSAVE DESC";
        EboContext oEboContext = boApplication.currentContext().getEboContext();
        boObjectList currentObjectList = boObjectList.list(oEboContext, boql, false, false);
        currentObjectList.beforeFirst();
        if (currentObjectList.getEboContext() != oEboContext) {
            currentObjectList.setEboContext(oEboContext);
        }
        return new XEOObjectListConnector(currentObjectList);
    }

    @Override
    public String getTitle() {
        String headerTitle = "X3OCM_Plugin [ " + currentPlugin + " ]";
        return headerTitle;
    }

    public String getCurrentObj() {
        String currentObj = (getSelectedObject() == null ? currentObject : getSelectedObject());
        return currentObj;
    }

    public String getCurrentObjName() {
        String currentObjName = mapObjs.get(getCurrentObj());
        return currentObjName;
    }

    public String getCurrentObjNameNew() {
        String menuText = netgest.bo.xwc.xeo.localization.X3OCM_Mensagens.V_X3OCM_util_lookup_addNew_sPar.toString(getCurrentObjName());
        return menuText;
    }
}
