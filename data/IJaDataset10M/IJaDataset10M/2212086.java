package org.fao.waicent.kids.giews.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import javax.help.HelpSet;
import javax.help.ServletHelpBroker;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.fao.waicent.db.dbConnectionManager;
import org.fao.waicent.db.dbConnectionManagerPool;
import org.fao.waicent.kids.giews.GIEWSConfiguration;
import org.fao.waicent.kids.giews.GIEWSGeonetworkConfiguration;
import org.fao.waicent.kids.giews.security.GIEWSSecurity;
import org.fao.waicent.kids.giews.security.GIEWSUser;
import org.fao.waicent.kids.giews.security.GIEWSUserProfile;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.kids.server.security.LoginException;
import org.fao.waicent.kids.server.security.XMLSecurity;
import org.fao.waicent.util.Debug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class GiewsLoadConfiguration extends kidsService {

    /********************************************************************/
    public GiewsLoadConfiguration() {
    }

    public GiewsLoadConfiguration(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    /********************************************************************/
    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        Debug.println("***** GiewsLoadConfiguration START *****");
        kidsSession session = request.getSession();
        GIEWSConfiguration configuration = new GIEWSConfiguration(request.setting);
        configuration.setGlobalPath(session.getGlobalHome());
        session.setConfiguration(configuration);
        Debug.println("session.getGlobalHome(): " + session.getGlobalHome());
        configuration.setGlobalPath(session.getGlobalHome());
        String database_ini = session.getGlobalHome() + "WEB-INF" + File.separator + "giews.ini";
        configuration.setDBIni(database_ini);
        String geonetwork_ini = session.getGlobalHome() + "WEB-INF" + File.separator + "geonetwork.ini";
        GIEWSGeonetworkConfiguration geonetwork = new GIEWSGeonetworkConfiguration();
        try {
            Properties prop = new Properties();
            FileInputStream file = new FileInputStream(geonetwork_ini);
            prop.load(file);
            if (prop.getProperty("ContextPath") != null) {
                geonetwork.setContextPath(prop.getProperty("ContextPath"));
            } else {
                geonetwork.setContextPath("/geonetwork");
            }
            if (prop.getProperty("PortNumber") != null) {
                geonetwork.setServerPort(Integer.parseInt(prop.getProperty("PortNumber")));
            } else {
                geonetwork.setServerPort(8080);
            }
            if (prop.getProperty("SRV") != null) {
                geonetwork.setService(prop.getProperty("SRV"));
            } else {
                geonetwork.setService("/srv");
            }
            Debug.println("GIEWSLoadConfiguration: Geonetwork initialized");
        } catch (Exception ee) {
            Debug.println("GIEWSLoadConfiguration: Initialization GeonetworkConfig failed");
            Debug.println("GIEWSLoadConfiguration: load default GeonetworkConfig");
            geonetwork.setContextPath("/geonetwork");
            geonetwork.setService("/srv");
            geonetwork.setServerPort(8080);
        }
        configuration.setGeonetworkConfiguration(geonetwork);
        configuration.setTextApprovalParameters(loadTextApproval(database_ini));
        Document project_tree_document = null;
        try {
            String project_tree_path = session.getGlobalHome() + "WEB-INF" + File.separator + "projects" + File.separator + "project_tree.xml";
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            project_tree_document = docBuilder.parse(new File(project_tree_path));
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        }
        configuration.setProjectTreeXML(project_tree_document);
        session.setConfiguration(configuration);
        XMLSecurity security = GIEWSSecurity.getDefaultXMLSecurity(database_ini);
        String userId = "guest";
        String password = "guest";
        NameCallback nc = new NameCallback("non important", "non important");
        nc.setName(userId);
        PasswordCallback pc = new PasswordCallback("non important", false);
        pc.setPassword(password.toCharArray());
        GIEWSUserProfile user_profile = null;
        try {
            Subject subject = security.validateLogin(new Callback[] { nc, pc });
            if (subject != null) {
                user_profile = new GIEWSUserProfile(subject);
                HashMap hm_users = ((GIEWSSecurity) security).getUsersHashMap();
                GIEWSUser giews_user = (GIEWSUser) hm_users.get(userId);
                user_profile.setGIEWSUser(giews_user);
            }
        } catch (LoginException le) {
            Debug.println("LoginEception :" + le.getMessage());
        }
        session.setXMLSecurity(security);
        session.setUserProfile(user_profile);
        session.setMetadataEditorIndex(0);
        Debug.println("***** GiewsLoadConfiguration END *****");
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }

    private Vector loadTextApproval(String db_ini) {
        Debug.println("GIEWSLoadConfiguration loadTextApproval START");
        Vector hash = new Vector();
        hash = getApprovingOfficers(db_ini, hash);
        hash = getClerks(db_ini, hash);
        hash = getSecretaries(db_ini, hash);
        hash = getApprovingOfficersByRole(db_ini, hash);
        Debug.println("GIEWSLoadConfiguration loadTextApproval END");
        return hash;
    }

    private Vector getApprovingOfficers(String db_ini, Vector aprv_officers) {
        String role = "country approving officer";
        String query = "select a.userprofile_firstname, a.userprofile_lastname, " + "a.userprofile_email, b.proj_code " + "from user_profile a, text_approval b " + "where a.userprofile_id = b.userprofile_id and b.role_id=5";
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = popConnection(db_ini);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                GIEWSUser g_user = new GIEWSUser(rs.getString(1), rs.getString(2), rs.getString(3), role, rs.getString(4));
                aprv_officers.add(g_user);
            }
        } catch (Exception e) {
            Debug.println("getApprovingOfficers exception: " + e.getMessage());
        } finally {
            pushConnection(con, db_ini);
        }
        return aprv_officers;
    }

    private Vector getApprovingOfficersByRole(String db_ini, Vector aprv_officers) {
        String role = "approving officer";
        String query = "select a.UserProfile_FirstName, a.UserProfile_LastName, " + "a.UserProfile_Email, c.Role_Name " + "from user_profile a, user_role b, role c " + "where a.UserProfile_ID = b.UserProfile_ID and b.Role_ID =5 " + "and b.Role_ID = c.Role_ID";
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = popConnection(db_ini);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                GIEWSUser g_user = new GIEWSUser(rs.getString(1), rs.getString(2), rs.getString(3), role, "");
                aprv_officers.add(g_user);
            }
        } catch (Exception e) {
            Debug.println("getApprovingOfficers exception: " + e.getMessage());
        } finally {
            pushConnection(con, db_ini);
        }
        return aprv_officers;
    }

    private Vector getClerks(String db_ini, Vector clerks) {
        String role = "clerk";
        String query = "select a.userprofile_firstname, a.userprofile_lastname, " + "a.userprofile_email, b.proj_code " + "from user_profile a, text_approval b " + "where a.userprofile_id = b.userprofile_id and b.role_id=4";
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = popConnection(db_ini);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                GIEWSUser g_user = new GIEWSUser(rs.getString(1), rs.getString(2), rs.getString(3), role, rs.getString(4));
                clerks.add(g_user);
            }
        } catch (Exception e) {
            Debug.println("getClerks exception: " + e.getMessage());
        } finally {
            pushConnection(con, db_ini);
        }
        return clerks;
    }

    private Vector getSecretaries(String db_ini, Vector secretaries) {
        String role = "";
        String query = "select a.UserProfile_FirstName, a.UserProfile_LastName, " + "a.UserProfile_Email, c.Role_Name " + "from user_profile a, user_role b, role c " + "where a.UserProfile_ID = b.UserProfile_ID and b.Role_ID in (6,7) " + "and b.Role_ID = c.Role_ID";
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = popConnection(db_ini);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                role = rs.getString(4);
                GIEWSUser g_user = new GIEWSUser(rs.getString(1), rs.getString(2), rs.getString(3), role, "");
                secretaries.add(g_user);
            }
        } catch (Exception e) {
            Debug.println("getApprovingOfficers exception: " + e.getMessage());
        } finally {
            pushConnection(con, db_ini);
        }
        return secretaries;
    }

    private Connection popConnection(String database_ini) {
        Debug.println("GiewsLoadConfiguration.popConnection BEGIN");
        Connection con = null;
        dbConnectionManager manager = dbConnectionManagerPool.getConnectionManager(database_ini);
        con = manager.popConnection();
        if (con == null) {
            Debug.println("GiewsLoadConfiguration.popConnection Con is NULL!");
        }
        Debug.println("ImageCropper.popConnection END");
        return con;
    }

    private void pushConnection(Connection con, String database_ini) {
        Debug.println("GiewsLoadConfiguration.pushConnection BEGIN");
        if (database_ini != null && con != null) {
            dbConnectionManagerPool.getConnectionManager(database_ini).pushConnection(con);
        } else {
            Debug.println("GiewsLoadConfiguration.pushConnection : null values!");
        }
        Debug.println("GiewsLoadConfiguration.pushConnection END");
    }
}
