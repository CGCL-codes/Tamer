package vobs.webapp;

import java.util.*;
import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import vobs.datamodel.*;
import vobs.dbaccess.*;
import wdc.settings.*;
import javax.xml.parsers.*;
import java.net.*;
import java.io.*;
import org.exist.xmldb.*;
import org.exist.xmldb.XQueryService;
import org.apache.struts.upload.*;
import java.lang.reflect.*;

public final class UpdateUserDataAction extends Action {

    private Logger log = Logger.getLogger(UpdateUserDataAction.class);

    private DefaultJDOMFactory factory = new DefaultJDOMFactory();

    private XMLOutputter outXml = new XMLOutputter(Format.getPrettyFormat());

    private static String userDB = Settings.get("vo_meta.userProfilesResource");

    private static String URI = Settings.get("vo_meta.uri");

    private static String logsDB = Settings.get("vo_meta.logsResource");

    private static String settingsDB = Settings.get("vo_meta.settingsResource");

    private static String forumDB = Settings.get("vo_meta.forumResource");

    private static String timeZone = Settings.get("vo_meta.timeZone");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = getLocale(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User voUser = (User) session.getAttribute("voUser");
        session.removeAttribute("currentPage");
        VO virtObs = (VO) session.getAttribute("vobean");
        if (null == virtObs) {
            return (mapping.findForward("logon"));
        }
        if (request.getParameter("source") != null && virtObs != null) {
            virtObs.setAction(request.getParameter("source"));
            session.setAttribute("vobean", virtObs);
        }
        if (voUser == null) {
            log.error("Session is missing or has expired for client from " + request.getRemoteAddr());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.session.nouser"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        if (!voUser.isAnonymous()) {
            String sectionName = request.getParameter("source");
            String templateName = VOAccess.getElementByName(settingsDB, "TEMPLATE", sectionName);
            String schemaName = VOAccess.getElementByName(settingsDB, "SCHEMA", sectionName);
            String fastIndexFlag = VOAccess.getElementByName(settingsDB, "IS_FAST_INDEX", sectionName);
            String editorMode = VOAccess.getElementByName(settingsDB, "EDITOR_MODE", sectionName);
            if (null == editorMode || editorMode.equalsIgnoreCase("")) {
                editorMode = "form";
            }
            Vector dataKeys = vobs.dbaccess.VOAccess.existRequest(settingsDB, schemaName, "/SCHEMA//FIELD/KEY/text()");
            Vector dataValues = new Vector();
            for (int i = 0; i < dataKeys.size(); i++) {
                String[] paramVals = request.getParameterValues(dataKeys.get(i).toString());
                if (null != paramVals && paramVals.length > 0) {
                    Vector values = new Vector();
                    for (int j = 0; j < paramVals.length; j++) {
                        String value = new String(paramVals[j].getBytes(), "UTF8");
                        value = value.replaceAll("&", "&amp;");
                        value = value.replaceAll("<", "&lt;");
                        value = value.replaceAll(">", "&gt;");
                        values.add(value);
                    }
                    dataValues.add(values);
                } else {
                    dataValues.add(null);
                }
            }
            String destinationDB = request.getParameter("path");
            String objId = request.getParameter("docname");
            if (null == objId || objId.length() == 0) {
                log.error("Error data added: docname n/a");
                return (mapping.getInputForward());
            }
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DOMBuilder builder = new DOMBuilder();
            String xquery = " xquery version \"1.0\"; " + " let $exist_doc := if (exists(document('" + forumDB + "/" + objId + ".xml'))) " + " 	then \"true\" " + "       else \"false\" " + " return <result><doc1>{$exist_doc}</doc1></result>";
            XMLResource checkResource = (XMLResource) (((XQueryService) CollectionsManager.getService(settingsDB, true, "XQueryService")).query(xquery)).getResource(0);
            Element resultElm = builder.build((org.w3c.dom.Document) (checkResource.getContentAsDOM())).getRootElement();
            if (!resultElm.getChildText("doc1").equals("false")) {
                log.error("Error data added: bad docname");
                return (mapping.findForward("logon"));
            }
            if ((null != request.getParameter("fileKey")) && (request.getParameter("fileKey").length() > 0) && (null != request.getParameter("fileAction")) && (request.getParameter("fileAction").length() > 0)) {
                String fileKey = request.getParameter("fileKey");
                String fileAction = request.getParameter("fileAction");
                int fileIndInKeys = dataKeys.indexOf(fileKey);
                if (fileIndInKeys >= 0) {
                    Vector values = new Vector();
                    values.add(new String(Settings.get("vo_store." + fileAction + ".returner") + objId));
                    dataValues.setElementAt(values, fileIndInKeys);
                }
                String filePreviewKey = request.getParameter("filePreviewKey");
                int filePreviewIndInKeys = dataKeys.indexOf(filePreviewKey);
                if (filePreviewIndInKeys >= 0) {
                    Vector values = new Vector();
                    values.add(new String(Settings.get("vo_store." + fileAction + ".returnerPreview") + objId));
                    dataValues.setElementAt(values, filePreviewIndInKeys);
                }
                FileForm myUploadForm = (FileForm) form;
                FormFile inpFile = myUploadForm.getTheFile();
                Class fileProcessor = Class.forName(Settings.get("vo_store." + fileAction + ".storeClass"));
                Method storeFileMethod = fileProcessor.getMethod("storeFile", new Class[] { InputStream.class, String.class, String.class, String.class, String.class, String.class });
                storeFileMethod.invoke(fileProcessor.newInstance(), new Object[] { inpFile.getInputStream(), inpFile.getFileName(), inpFile.getContentType(), String.valueOf(inpFile.getFileSize()), objId, fileAction });
            }
            String additionRestriction = request.getParameter("additionRestriction");
            if (editorMode.equalsIgnoreCase("form")) {
                String eNode = "";
                String title = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayTitle"));
                String description = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayDescription"));
                if (null == description || description.length() == 0) {
                    description = "";
                }
                String onlink = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayLink"));
                if (null == onlink || onlink.length() == 0) {
                    onlink = "";
                }
                String ncower = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayNCover"));
                if (null == ncower || ncower.length() == 0) {
                    ncower = "";
                }
                String ecower = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayECover"));
                if (null == ecower || ecower.length() == 0) {
                    ecower = "";
                }
                String scower = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplaySCover"));
                if (null == scower || scower.length() == 0) {
                    scower = "";
                }
                String wcower = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayWCover"));
                if (null == wcower || wcower.length() == 0) {
                    wcower = "";
                }
                String datefrom = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayDateFrom"));
                if (null == datefrom || datefrom.length() == 0) {
                    datefrom = "";
                }
                String dateto = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayDateTo"));
                if (null == dateto || dateto.length() == 0) {
                    dateto = "";
                }
                String docindex = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayIndex"));
                if (null == docindex || docindex.length() == 0) {
                    docindex = "";
                }
                String previewimg = VOAccess.getMultipartRequestUnicodeParameter(request, request.getParameter("keyDisplayPreviewImg"));
                if (null == previewimg || previewimg.length() == 0) {
                    previewimg = "";
                    if ((null != request.getParameter("fileKey")) && (request.getParameter("fileKey").length() > 0) && (null != request.getParameter("fileAction")) && (request.getParameter("fileAction").length() > 0) && request.getParameter("fileAction").equalsIgnoreCase("videoAction")) {
                        previewimg = "videoretrieve?type=preview&objId=" + objId;
                    }
                }
                String discRestr = request.getParameter("discussRestriction");
                if (null == discRestr || discRestr.length() == 0) {
                    discRestr = "true";
                }
                String votingRestr = request.getParameter("votingRestriction");
                if (null == votingRestr || votingRestr.length() == 0) {
                    votingRestr = "true";
                }
                for (int i = 0; i < destinationDB.length(); i++) {
                    StringTokenizer st = new StringTokenizer(destinationDB);
                    while (st.hasMoreElements()) {
                        String token = st.nextToken("/");
                        eNode = token;
                    }
                }
                if (additionRestriction.equalsIgnoreCase("moderate")) destinationDB = "/waitingForApproval" + destinationDB;
                vobs.dbaccess.VOAccess.addUserData(URI, settingsDB, destinationDB, templateName, dataKeys, dataValues, objId);
                if (!additionRestriction.equalsIgnoreCase("moderate")) vobs.dbaccess.VOAccess.updateLog(logsDB, vobs.dbaccess.VOAccess.getCurrentTime(timeZone), System.currentTimeMillis(), objId, sectionName, eNode, title, description, onlink, voUser.getProfileName());
                vobs.dbaccess.VOAccess.updateUserAddedDataProfile(voUser.getProfileName(), objId, sectionName);
                if (!additionRestriction.equalsIgnoreCase("moderate")) vobs.dbaccess.VOAccess.updateLastItem(objId, sectionName);
                datefrom = VOAccess.parseDate(datefrom, "yyyy-MM-dd'T'HH:mm:ss", VO.defaultTimeFormat.toPattern());
                dateto = VOAccess.parseDate(datefrom, "yyyy-MM-dd'T'HH:mm:ss", VO.defaultTimeFormat.toPattern());
                Hashtable discussionFields = new Hashtable();
                discussionFields.put("OBJECT_ID", objId);
                discussionFields.put("AUTHOR_ID", voUser.getProfileName());
                discussionFields.put("AUTHOR_NAME", voUser.getName());
                discussionFields.put("OBJECT_SECTION", sectionName);
                discussionFields.put("OBJECT_PATH", destinationDB);
                discussionFields.put("FILE_PATH", "");
                discussionFields.put("TITLE", title);
                discussionFields.put("DESCRIPTION", description);
                discussionFields.put("ONLINK", onlink);
                discussionFields.put("NCOVER", ncower);
                discussionFields.put("ECOVER", ecower);
                discussionFields.put("SCOVER", scower);
                discussionFields.put("WCOVER", wcower);
                discussionFields.put("PERIOD_START", datefrom);
                discussionFields.put("PERIOD_END", dateto);
                discussionFields.put("DOC_INDEX", docindex);
                discussionFields.put("PREVIEW_IMG", previewimg);
                discussionFields.put("DISCUSSRESTRICTION", discRestr);
                discussionFields.put("VOTINGRESTRICTION", votingRestr);
                VOAccess.createDiscussionFile(discussionFields);
                if (!additionRestriction.equalsIgnoreCase("moderate") && fastIndexFlag == "true") Indexer.index(objId);
            }
            if (editorMode.equalsIgnoreCase("text")) {
            }
            return (mapping.findForward("success"));
        } else {
            log.error("User from " + request.getRemoteAddr() + " is not authorize to use this page.");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.voUser.authorize"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
    }
}
