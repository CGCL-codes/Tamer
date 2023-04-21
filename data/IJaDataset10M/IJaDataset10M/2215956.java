package spidr.webapp;

import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.xmldb.api.base.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.*;
import vobs.dbaccess.*;
import wdc.settings.*;
import vobs.vocatalog.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import spidr.datamodel.User;
import java.io.IOException;
import vobs.vosearch.*;

public final class MetadataCatalogueAction extends Action {

    private Logger log = Logger.getLogger(MetadataCatalogueAction.class);

    private static String settingsDB = Settings.get("vo_meta.settingsResource");

    private static String userDB = Settings.get("vo_meta.userProfilesResource");

    private static String logsDB = Settings.get("vo_meta.logsResource");

    private static String forumDB = Settings.get("vo_meta.forumResource");

    private static String dbLocation = Settings.get("vo_meta.httpUri");

    private static String spidrServerName = Settings.get("sites.localSite");

    private static String spidrServerUrl = Settings.get("sites." + spidrServerName + ".url");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        DirStructure structure = (DirStructure) session.getServletContext().getAttribute(spidrServerName + "VirtualDir");
        if (null == structure) {
            DirStructure structureTmp = loadStructure("VirtualDir");
            if (null != structureTmp && null != structureTmp.getId()) {
                session.getServletContext().setAttribute(spidrServerName + "VirtualDir", loadStructure("VirtualDir"));
            } else {
                structureTmp = new DirStructure("VirtualDir");
                session.getServletContext().setAttribute(spidrServerName + "VirtualDir", structureTmp);
            }
            structure = (DirStructure) session.getServletContext().getAttribute(spidrServerName + "VirtualDir");
        }
        String dir_action = request.getParameter("dir_action");
        String dir_id = request.getParameter("dir_id");
        if (null != dir_action && dir_action.equalsIgnoreCase("reload")) {
            session.getServletContext().removeAttribute(spidrServerName + "VirtualDir");
            session.getServletContext().setAttribute(spidrServerName + "VirtualDir", loadStructure("VirtualDir"));
            structure = (DirStructure) session.getServletContext().getAttribute(spidrServerName + "VirtualDir");
        }
        if (null != dir_action && dir_action.equalsIgnoreCase("store")) {
            storeStructure(structure);
        }
        if (user.isAdministrator()) {
            if (null != dir_action && null != dir_id) {
                if (dir_action.equalsIgnoreCase("up")) {
                    DirStructure curDir = DirStructure.getSubDirParent(dir_id, structure);
                    if (null != curDir) curDir.upSubDir(dir_id);
                }
                if (dir_action.equalsIgnoreCase("down")) {
                    DirStructure curDir = DirStructure.getSubDirParent(dir_id, structure);
                    if (null != curDir) curDir.downSubDir(dir_id);
                }
                if (dir_action.equalsIgnoreCase("setTitle")) {
                    DirStructure curDir = DirStructure.getGlobalFirstSubDir(dir_id, structure);
                    String title = VOAccess.getRequestUnicodeParameter(request, "new_title");
                    if (null != curDir && null != title) curDir.setTitle(title);
                }
                if (dir_action.equalsIgnoreCase("setDescr")) {
                    DirStructure curDir = DirStructure.getGlobalFirstSubDir(dir_id, structure);
                    String descr = VOAccess.getRequestUnicodeParameter(request, "new_descr");
                    if (null != curDir && null != descr) curDir.setDescr(descr);
                }
                if (dir_action.equalsIgnoreCase("addSubdir")) {
                    DirStructure curDir = DirStructure.getGlobalFirstSubDir(dir_id, structure);
                    if (null != curDir) {
                        String newDirId = vobs.dbaccess.VOAccess.getUniqueId();
                        DirStructure newDir = new DirStructure(newDirId);
                        curDir.addDirInStructure(newDirId, newDir);
                    }
                }
                if (dir_action.equalsIgnoreCase("delete")) {
                    DirStructure curDir = DirStructure.getSubDirParent(dir_id, structure);
                    if (null != curDir) curDir.deleteSubDir(dir_id);
                }
                if (dir_action.equalsIgnoreCase("removeItem")) {
                    DirStructure curDir = DirStructure.getGlobalFirstSubDir(dir_id, structure);
                    String itemId = VOAccess.getRequestUnicodeParameter(request, "item");
                    if (null != curDir && null != itemId) curDir.deleteItemInDir(itemId);
                }
                if (dir_action.equalsIgnoreCase("addItem")) {
                    DirStructure curDir = DirStructure.getGlobalFirstSubDir(dir_id, structure);
                    String itemId = VOAccess.getRequestUnicodeParameter(request, "item");
                    if (null != curDir && null != itemId) curDir.addItemInDir(itemId);
                }
            }
        }
        return (mapping.findForward("success"));
    }

    private DirStructure loadStructure(String structureId) throws XMLDBException, IOException, JDOMException {
        String searchrequest = spidrServerUrl + (spidrServerUrl.endsWith("/") ? "" : "/") + "osproxy.do?specialRequest=document&settingDocPath=" + structureId + "/" + spidrServerName + structureId + "Tree.xml";
        ResultStructure searchresult = new ResultStructure();
        searchresult = new ResultStructure(searchrequest);
        if (null != searchresult.getErrorText()) {
            DirStructure structure = new DirStructure("VirtualDir");
            structure.setDescr(searchresult.getErrorText());
            return structure;
        }
        SAXBuilder parser = new SAXBuilder();
        Element dirElm = parser.build(searchrequest).getRootElement();
        DirStructure structure = new DirStructure(dirElm);
        return structure;
    }

    private void storeStructure(DirStructure structure) throws XMLDBException {
        Element dirElm = DirStructure.genXmlDoc(structure);
        XMLOutputter outXml = new XMLOutputter(Format.getPrettyFormat());
        String structureId = structure.getId();
        Collection col = CollectionsManager.getCollection(settingsDB + "/" + structureId, true);
        XMLResource document = (XMLResource) col.createResource(spidrServerName + structureId + "Tree.xml", XMLResource.RESOURCE_TYPE);
        document.setContent(outXml.outputString(dirElm));
        col.storeResource(document);
    }
}
