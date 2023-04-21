package hu.sztaki.lpds.pgportal.portlets.workflow;

import hu.sztaki.lpds.information.com.ServiceType;
import hu.sztaki.lpds.information.local.InformationBase;
import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.pgportal.portlet.GenericWSPgradePortlet;
import hu.sztaki.lpds.pgportal.service.base.PortalCacheService;
import hu.sztaki.lpds.pgportal.service.workflow.Sorter;
import hu.sztaki.lpds.pgportal.service.workflow.WorkflowExportUtils;
import hu.sztaki.lpds.pgportal.util.stream.HttpDownload;
import hu.sztaki.lpds.storage.inf.PortalStorageClient;
import hu.sztaki.lpds.wfs.com.ComDataBean;
import hu.sztaki.lpds.wfs.com.RepositoryWorkflowBean;
import hu.sztaki.lpds.wfs.inf.PortalWfsClient;
import javax.portlet.*;
import java.io.*;
import java.util.*;

/**
 * A felhasznalo absztakt workflowinak kezeleset megvalosito portlet.
 *
 * @author krisztian
 */
public class GraphManagerPortlet extends GenericWSPgradePortlet {

    private PortletContext pContext;

    public GraphManagerPortlet() {
    }

    /**
     * Portlet inicializalasa
     */
    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        pContext = config.getPortletContext();
    }

    /**
     * Portlet UI megjelenitesehez az adatok atadasa
     */
    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType("text/html");
        if (!isInited()) {
            getPortletContext().getRequestDispatcher("/WEB-INF/jsp/error/init.jsp").include(request, response);
            return;
        }
        openRequestAttribute(request);
        try {
            PortletRequestDispatcher dispatcher;
            Enumeration enm = PortalCacheService.getInstance().getUser(request.getRemoteUser()).getAbstactWorkflows().keys();
            String key = "";
            while (enm.hasMoreElements()) {
                key = "" + enm.nextElement();
                PortalCacheService.getInstance().getUser(request.getRemoteUser()).getAbstactWorkflow(key).setTmp("0");
            }
            enm = PortalCacheService.getInstance().getUser(request.getRemoteUser()).getWorkflows().keys();
            while (enm.hasMoreElements()) {
                key = "" + enm.nextElement();
                if (PortalCacheService.getInstance().getUser(request.getRemoteUser()).getWorkflow(key).getRunningStatus() > 0) {
                    PortalCacheService.getInstance().getUser(request.getRemoteUser()).getAbstactWorkflow(PortalCacheService.getInstance().getUser(request.getRemoteUser()).getWorkflow(key).getGraf()).setTmp("1");
                }
            }
            dispatcher = pContext.getRequestDispatcher("/jsp/workflow/grafswrk.jsp");
            request.setAttribute("aWorkflowList", Sorter.getInstance().sortFromValues(PortalCacheService.getInstance().getUser(request.getRemoteUser()).getAbstactWorkflows()));
            request.setAttribute("puser", request.getRemoteUser());
            request.setAttribute("portalID", PropertyLoader.getInstance().getProperty("service.url"));
            dispatcher.include(request, response);
        } catch (IOException e) {
            throw new PortletException("JSPPortlet.doView exception", e);
        }
    }

    /**
     * Graf torlesekor hivodik meg
     */
    public void doDelete(ActionRequest request, ActionResponse response) throws PortletException {
        try {
            String userID = request.getRemoteUser();
            String workflowID = request.getParameter("workflow");
            ComDataBean cmd = new ComDataBean();
            cmd.setWorkflowID(workflowID);
            cmd.setUserID(userID);
            cmd.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
            Enumeration wfenm = PortalCacheService.getInstance().getUser(userID).getWorkflows().keys();
            while (wfenm.hasMoreElements()) {
                String wfkey = "" + wfenm.nextElement();
                if (PortalCacheService.getInstance().getUser(userID).getWorkflow(wfkey).getGraf().equals(workflowID)) {
                    try {
                        String storageID = PortalCacheService.getInstance().getUser(userID).getWorkflow(wfkey).getStorageID();
                        Hashtable hsh = new Hashtable();
                        hsh.put("url", storageID);
                        ServiceType st = InformationBase.getI().getService("storage", "portal", hsh, new Vector());
                        PortalStorageClient ps = (PortalStorageClient) Class.forName(st.getClientObject()).newInstance();
                        ps.setServiceURL(st.getServiceUrl());
                        ps.setServiceID(st.getServiceID());
                        ComDataBean tmp = new ComDataBean();
                        tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
                        tmp.setUserID(userID);
                        tmp.setWorkflowID(wfkey);
                        ps.deleteWorkflow(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            wfenm = PortalCacheService.getInstance().getUser(userID).getTemplateWorkflows().keys();
            while (wfenm.hasMoreElements()) {
                String wfkey = "" + wfenm.nextElement();
                if (PortalCacheService.getInstance().getUser(userID).getTemplateWorkflow(wfkey).getGraf().equals(workflowID)) {
                    try {
                        String storageID = PortalCacheService.getInstance().getUser(userID).getTemplateWorkflow(wfkey).getStorageID();
                        Hashtable hsh = new Hashtable();
                        hsh.put("url", storageID);
                        ServiceType st = InformationBase.getI().getService("storage", "portal", hsh, new Vector());
                        PortalStorageClient ps = (PortalStorageClient) Class.forName(st.getClientObject()).newInstance();
                        ps.setServiceURL(st.getServiceUrl());
                        ps.setServiceID(st.getServiceID());
                        ComDataBean tmp = new ComDataBean();
                        tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
                        tmp.setUserID(userID);
                        tmp.setWorkflowID(wfkey);
                        ps.deleteWorkflow(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            wfenm = PortalCacheService.getInstance().getUser(userID).getWorkflows().keys();
            String key = "";
            while (wfenm.hasMoreElements()) {
                key = "" + wfenm.nextElement();
                if (PortalCacheService.getInstance().getUser(userID).getWorkflow(key).getGraf().equals(workflowID)) {
                    Hashtable hsh = new Hashtable();
                    ServiceType st = InformationBase.getI().getService("wfs", "portal", hsh, new Vector());
                    try {
                        PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
                        pc.setServiceURL(st.getServiceUrl());
                        pc.setServiceID(st.getServiceID());
                        ComDataBean tmp = new ComDataBean();
                        tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
                        tmp.setUserID(userID);
                        tmp.setWorkflowID(key);
                        pc.deleteWorkflow(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PortalCacheService.getInstance().getUser(userID).getWorkflows().remove(key);
                }
            }
            wfenm = PortalCacheService.getInstance().getUser(userID).getTemplateWorkflows().keys();
            key = "";
            while (wfenm.hasMoreElements()) {
                key = "" + wfenm.nextElement();
                if (PortalCacheService.getInstance().getUser(userID).getTemplateWorkflow(key).getGraf().equals(workflowID)) {
                    Hashtable hsh = new Hashtable();
                    ServiceType st = InformationBase.getI().getService("wfs", "portal", hsh, new Vector());
                    try {
                        PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
                        pc.setServiceURL(st.getServiceUrl());
                        pc.setServiceID(st.getServiceID());
                        ComDataBean tmp = new ComDataBean();
                        tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
                        tmp.setUserID(userID);
                        tmp.setWorkflowID(key);
                        pc.deleteWorkflow(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PortalCacheService.getInstance().getUser(userID).getTemplateWorkflows().remove(key);
                }
            }
            try {
                ServiceType st = InformationBase.getI().getService("wfs", "portal", new Hashtable(), new Vector());
                PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
                pc.setServiceURL(PortalCacheService.getInstance().getUser(userID).getAbstactWorkflow(workflowID).getWfsID());
                pc.setServiceID(PortalCacheService.getInstance().getUser(userID).getAbstactWorkflow(workflowID).getWfsIDService());
                pc.deleteWorkflowGraf(cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PortalCacheService.getInstance().getUser(userID).getAbstactWorkflows().remove(workflowID);
            setRequestAttribute(request.getPortletSession(), "msg", "workflowgraf.delete.ok");
        } catch (Exception e) {
            e.printStackTrace();
            setRequestAttribute(request.getPortletSession(), "msg", "workflowgraf.delete.error");
        }
    }

    /**
     * Workflow exportalas a repository-ba (doExport)
     */
    public void doExport(ActionRequest request, ActionResponse response) throws PortletException {
        String msg = new String("");
        try {
            RepositoryWorkflowBean bean = WorkflowExportUtils.getInstance().getBeanFromRequest(request.getParameterMap(), request.getRemoteUser());
            msg = WorkflowExportUtils.getInstance().exportWorkflow(bean);
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getLocalizedMessage();
            setRequestAttribute(request.getPortletSession(), "msg", msg);
        }
        setRequestAttribute(request.getPortletSession(), "msg", msg);
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        if (request.getParameter("wfId") != null) GraphEditorUtil.jnpl(request, response); else {
            response.setContentType("application/zip");
            response.setProperty("Content-Disposition", "inline; filename=\"" + request.getParameter("workflowID") + "_graph.zip\"");
            try {
                HttpDownload.fileDownload("graf", request, response);
            } catch (Exception e) {
                e.printStackTrace();
                throw new PortletException("com error");
            }
        }
    }
}
