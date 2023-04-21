package vobs.webapp;

import java.util.*;
import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.exist.xmldb.*;
import org.exist.xmldb.XQueryService;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import vobs.datamodel.*;
import vobs.dbaccess.*;
import wdc.settings.*;

public final class RefreshAction extends Action {

    private static Logger log = Logger.getLogger(RefreshAction.class);

    private DefaultJDOMFactory factory = new DefaultJDOMFactory();

    private DOMBuilder builder = new DOMBuilder();

    private XMLOutputter outXml = new XMLOutputter(Format.getPrettyFormat());

    private static String URI = Settings.get("vo_meta.uri");

    private static String userDB = Settings.get("vo_meta.userProfilesResource");

    private static String logsDB = Settings.get("vo_meta.logsResource");

    private static String settingsDB = Settings.get("vo_meta.settingsResource");

    private static String timeZone = Settings.get("vo_meta.timeZone");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = getLocale(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User voUser = (User) session.getAttribute("voUser");
        VO virtObs = (VO) session.getAttribute("vobean");
        if (null == virtObs && null == voUser) {
            return (mapping.findForward("logon"));
        }
        if (request.getParameter("source") != null && virtObs != null) {
            virtObs.setAction(request.getParameter("source"));
            session.setAttribute("vobean", virtObs);
        }
        Vector userMenuItems = new Vector();
        if (!voUser.isAnonymous()) {
            moveUserLogRecords(voUser.getProfileName());
            Element refreshElm = factory.element("USER_REFRESH");
            userMenuItems = VOAccess.getUserMenuItems(voUser.getProfileName(), refreshElm);
            writeUserRefresh(voUser.getProfileName(), refreshElm);
        }
        VOAccess.setUserActivityTime(URI, userDB, voUser.getProfileName(), VOAccess.getCurrentTime(timeZone));
        virtObs.setUserMenuItems(userMenuItems);
        virtObs.setFontSize(getProfileFontSize(voUser.getProfileName()));
        virtObs.setAppColor(getProfileAppColor(voUser.getProfileName()));
        session.setAttribute("vobean", virtObs);
        if (null != request.getSession().getAttribute("currentPage")) {
            return new ActionForward((String) request.getSession().getAttribute("currentPage"));
        }
        return (mapping.findForward("home"));
    }

    private int getProfileFontSize(String profileName) throws XMLDBException, XMLDBException {
        int fontSize = 9;
        if (null == profileName) return fontSize;
        String fontSizeQuery = "document('" + profileName + "')/USER_PROFILE/USER_DESIGN/SIZE/text()";
        XQueryService fontSizeService = (XQueryService) CollectionsManager.getCollection(userDB, true).getService("XQueryService", "1.0");
        ResourceSet fontSizeResult = fontSizeService.query(fontSizeQuery);
        if (fontSizeResult.getSize() > 0) {
            XMLResource fontSizeResource = (XMLResource) fontSizeResult.getResource(0);
            try {
                fontSize = Integer.parseInt(fontSizeResource.getContent().toString());
            } catch (NumberFormatException ex) {
                log.error("Error parsing font size value. Response is: " + fontSizeResource.getContent());
            }
        } else {
            log.error("Error parsing font size value : returned no resluts.");
        }
        return fontSize;
    }

    private String getProfileAppColor(String profileName) throws XMLDBException, XMLDBException {
        String color = "Black";
        if (null == profileName) return color;
        String appColorQuery = "document('" + profileName + "')/USER_PROFILE/USER_DESIGN/COLOR/text()";
        XQueryService appColorService = (XQueryService) CollectionsManager.getCollection(userDB, true).getService("XQueryService", "1.0");
        ResourceSet Result = appColorService.query(appColorQuery);
        if (Result.getSize() > 0) {
            XMLResource appColorResource = (XMLResource) Result.getResource(0);
            try {
                color = appColorResource.getContent().toString();
            } catch (NumberFormatException ex) {
                log.error("Error parsing app color value. Response is: " + appColorResource.getContent());
            }
        } else {
            log.error("Error parsing app color value : returned no results.");
        }
        return color;
    }

    public void moveUserLogRecords(String profileName) throws XMLDBException {
        String xquery = "xquery version \"1.0\";" + "declare namespace xmldb=\"http://exist-db.org/xquery/xmldb\";" + "let $userProfile := document(\"" + userDB + "/" + profileName + "\")," + "$userLog := document(\"" + logsDB + "/" + profileName + ".log\")," + "$userFilters := $userProfile/USER_PROFILE/USER_FILTER/PART," + "$copy_items_count:=count(document(\"" + logsDB + "/vo_log.xml\")//EVENT[EVENT_PC_TIME gt $userProfile/USER_PROFILE/USER_TIME_PC_ACTIVITY][EVENT_NODE = document(\"" + userDB + "/" + profileName + "\")/USER_PROFILE/USER_FILTER/PART/EVENT_NODE])" + "for $num in 1 to $copy_items_count " + "let $update_query :=  " + " <xupdate:modifications version=\"1.0\" xmlns:xupdate=\"http://www.xmldb.org/xupdate\"> " + "  <xupdate:variable name=\"prof\" select=\"document('" + userDB + "/" + profileName + "')\"/>  " + "  <xupdate:append select=\"document('" + logsDB + "/" + profileName + ".log')/LOG\" child=\"2\">  " + "      <xupdate:element name=\"EVENT\"> " + "              <xupdate:value-of select=\"document('" + logsDB + "/vo_log.xml')//EVENT[EVENT_PC_TIME gt {$userProfile/USER_PROFILE/USER_TIME_PC_ACTIVITY}][EVENT_NODE = $prof/USER_PROFILE/USER_FILTER/PART/EVENT_NODE][{$copy_items_count - $num + 1}]/*\"> " + "              </xupdate:value-of> " + "      </xupdate:element> " + "  </xupdate:append>   " + " </xupdate:modifications> " + "let $collection := xmldb:collection(\"" + logsDB + "\", \"" + Settings.get("vo_meta.userName") + "\", \"" + Settings.get("vo_meta.password") + "\") " + "let $result := xmldb:update($collection, $update_query) " + "return $result ";
        XQueryService service = (XQueryService) CollectionsManager.getService(Settings.get("vo_meta.rootCollection"), true, "XQueryService");
        ResourceSet set = service.query(xquery);
        return;
    }

    private void writeUserRefresh(String profileName, Element refreshTree) {
        String xquery = "<xupdate:modifications version='1.0' xmlns:xupdate='http://www.xmldb.org/xupdate'>";
        try {
            String query = "  <xupdate:update select=\"document('" + userDB + "/" + profileName + "')//USER_PROFILE/USER_REFRESH\">" + outXml.outputString(refreshTree) + "  </xupdate:update>";
            xquery = xquery + query;
            xquery = xquery + "</xupdate:modifications>";
            XUpdateQueryService service = (XUpdateQueryService) CollectionsManager.getService(userDB, true, "XUpdateQueryService");
            long res = service.update(xquery);
        } catch (Exception e) {
            log.debug("Error updating user refresh section: " + e);
            e.printStackTrace();
        }
    }
}
