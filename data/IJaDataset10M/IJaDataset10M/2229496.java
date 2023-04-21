package fairVote.agent.registrar;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import fairVote.agent.AgentCore;
import fairVote.core.MyException;
import fairVote.data.Config;
import fairVote.data.RuleData;
import fairVote.data.Session;
import fairVote.mysql.MySql;
import fairVote.util.FairLog;
import fairVote.votazione.Votazione;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import org.apache.commons.codec.binary.Base64;

public class Registrar {

    private static Logger LOGGER = FairLog.getLogger(Registrar.class.getName());

    public static final int SessionTokenSize = 32;

    private static HashMap<String, Session> sessions = new HashMap<String, Session>();

    public static final String C_AUTHUSERPASS = "USERPASS";

    public static final String C_AUTHX509 = "X509";

    public static final String MSG_YETINSERTED = "FAIL::YETINSERTED";

    public static synchronized int addSession(String S1, String user4T1, String IDVotazione, byte[] certBytes, PublicKey pp) {
        LOGGER.debug("S1:" + S1);
        LOGGER.info("Adding session ...");
        if (sessions.containsKey(S1)) {
            LOGGER.info("... session already set");
            return -1;
        }
        Session s = new Session(S1, user4T1, IDVotazione, certBytes, pp);
        sessions.put(S1, s);
        Iterator<String> i = sessions.keySet().iterator();
        Vector<String> ls = new Vector<String>();
        long now = System.currentTimeMillis();
        if (LOGGER.isTraceEnabled()) LOGGER.trace("Now: " + now);
        while (i.hasNext()) {
            String tS1 = (String) i.next();
            Session ts = (Session) sessions.get(tS1);
            if (now - ts.tstart > Session.TimeOutMilliSec) {
                if (LOGGER.isTraceEnabled()) LOGGER.trace("Timeout:" + ts.user + " " + ts.S1 + " " + ts.tstart);
                ls.add(tS1);
            } else {
                if (LOGGER.isTraceEnabled()) LOGGER.trace("OK:" + ts.user + " " + ts.S1 + " " + ts.tstart);
            }
        }
        Iterator<String> ii = ls.iterator();
        while (ii.hasNext()) {
            String tS1 = (String) ii.next();
            sessions.remove(tS1);
        }
        if (LOGGER.isTraceEnabled()) LOGGER.trace("N.Active sessions:" + sessions.size());
        LOGGER.info("... session added");
        return 0;
    }

    public static synchronized int removeSession(String S1) {
        LOGGER.debug("search if session \"" + S1 + "\"is set");
        if (!sessions.containsKey(S1)) {
            LOGGER.warn("Session not set");
            return -1;
        }
        sessions.remove(S1);
        return 0;
    }

    public static synchronized byte[] getSessionBundleAuth(String S1) {
        LOGGER.debug("Check if session \"" + S1 + "\"is set for cert");
        if (!sessions.containsKey(S1)) {
            LOGGER.debug("Session not set");
            return null;
        }
        if (((Session) sessions.get(S1)).bundle_auth == null) {
            LOGGER.debug("Session bundle_auth not set");
            return null;
        }
        LOGGER.debug("Ok");
        return ((Session) sessions.get(S1)).bundle_auth;
    }

    public static synchronized String getSessionUser(String S1) {
        LOGGER.debug("Check if session \"" + S1 + "\"is set for user");
        if (!sessions.containsKey(S1)) {
            LOGGER.debug("Session not set");
            return null;
        }
        if (((Session) sessions.get(S1)).user == null) {
            LOGGER.debug("Session user not set");
            return null;
        }
        String user = ((Session) sessions.get(S1)).user;
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Ok[" + user + "]");
        return user;
    }

    public static synchronized String getSessionIDVotazione(String S1) {
        LOGGER.debug("Check if session \"" + S1 + "\"is set for user");
        if (!sessions.containsKey(S1)) {
            LOGGER.debug("Session not set");
            return null;
        }
        if (((Session) sessions.get(S1)).IDVotazione == null) {
            LOGGER.debug("Session IDVotazione not set");
            return null;
        }
        String IDVotazione = ((Session) sessions.get(S1)).IDVotazione;
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Ok[" + IDVotazione + "]");
        return IDVotazione;
    }

    public static synchronized PublicKey getSessionPublicKey(String S1) {
        LOGGER.debug("Check if session \"" + S1 + "\"is set");
        if (!sessions.containsKey(S1)) {
            LOGGER.debug("Session not set");
            return null;
        }
        if (((Session) sessions.get(S1)).pp == null) {
            LOGGER.debug("Session pp not set");
            return null;
        }
        LOGGER.debug("Ok");
        return ((Session) sessions.get(S1)).pp;
    }

    public static synchronized int setSessionT1(String S1, byte[] T1) {
        LOGGER.debug("Check if session \"" + S1 + "\"is set");
        if (!sessions.containsKey(S1)) {
            LOGGER.debug("Session not set");
            return -1;
        }
        LOGGER.debug("Ok");
        ((Session) sessions.get(S1)).T1 = T1;
        return 0;
    }

    public static final int SETSESSION_OK = 0;

    public static final int SETSESSION_MYSQLERROR = -1;

    public static final int SETSESSION_DIFFERS = -2;

    public static final int SETSESSION_GENERIC = -3;

    public static final int SETSESSION_SESSIONNOTFOUND = -4;

    public static final int SETSESSION_T1NOTSET = -5;

    public static synchronized int setSessionT2(String S1, byte[] T2, String user, String IDVotazione, Config config) {
        LOGGER.debug("Check if session \"" + S1 + "\"is set");
        if (!sessions.containsKey(S1)) {
            LOGGER.debug("Session not set");
            return SETSESSION_SESSIONNOTFOUND;
        }
        if (((Session) sessions.get(S1)).T1 == null) {
            LOGGER.debug("Session T1 not set");
            return SETSESSION_T1NOTSET;
        }
        byte[] T1 = ((Session) sessions.get(S1)).T1;
        try {
            String sT1 = new String(Base64.encodeBase64(T1), "utf-8");
            LOGGER.debug("T1:" + sT1);
            String sT2 = new String(Base64.encodeBase64(T2), "utf-8");
            LOGGER.debug("T2:" + sT2);
            Class.forName("com.mysql.jdbc.Driver");
            String sconn = config.getSconn();
            Connection conn = DriverManager.getConnection(sconn);
            LOGGER.info("Check T2 on DB");
            String query = "" + " SELECT T2 FROM T1T2 " + " WHERE T1=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, sT1);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String s2T2 = rs.getString(1);
                LOGGER.debug("Found:" + s2T2);
                stmt.close();
                if (s2T2.equals(sT2)) return SETSESSION_OK;
                LOGGER.debug("Differs");
                return SETSESSION_DIFFERS;
            } else {
                LOGGER.debug("Free");
                query = "" + " INSERT INTO T1T2 (T1,T2,user,IDVotazione) " + " VALUES           (? ,? ,?   ,?          ) ";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, sT1);
                stmt.setString(2, sT2);
                stmt.setString(3, user);
                stmt.setString(4, IDVotazione);
                int i = stmt.executeUpdate();
                stmt.close();
                LOGGER.debug("i=" + i);
                return SETSESSION_OK;
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
            return SETSESSION_MYSQLERROR;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("ClassNotFoundException", ex);
            return SETSESSION_MYSQLERROR;
        } catch (Exception e) {
            LOGGER.error("Unknown error", e);
            return SETSESSION_GENERIC;
        }
    }

    public static byte[] getT2FromT1(byte[] T1, Config config) {
        try {
            String sT1 = new String(Base64.encodeBase64(T1), "utf-8");
            LOGGER.trace("segue T1");
            LOGGER.trace(sT1);
            Class.forName("com.mysql.jdbc.Driver");
            String sconn = config.getSconn();
            Connection conn = DriverManager.getConnection(sconn);
            String query = "" + " SELECT T2 FROM T1T2 " + " WHERE T1=? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, sT1);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String sT2 = rs.getString(1);
                return Base64.decodeBase64(sT2.getBytes("utf-8"));
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
            return null;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("ClassNotFoundException", ex);
            return null;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Encoding error", ex);
            return null;
        }
    }

    public static Document askForCredential(Config config, String credential_xml, String IDVotazione) {
        String surl = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sconn = config.getSconn();
            Connection conn = DriverManager.getConnection(sconn);
            String query = "" + " SELECT authserver FROM votazioni " + " WHERE ID=? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, IDVotazione);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                surl = rs.getString(1);
                LOGGER.debug("authserver by db " + surl);
            } else return null;
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
            return null;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("ClassNotFoundException", ex);
            return null;
        }
        String response = "";
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (java.security.KeyManagementException e) {
            LOGGER.error("Error opening ssl connection to authserver", e);
            return null;
        } catch (java.security.NoSuchAlgorithmException e) {
            LOGGER.error("Error opening ssl connection to authserver", e);
            return null;
        }
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
            URL url = new URL(surl);
            String test = new String(Base64.encodeBase64(credential_xml.getBytes()));
            LOGGER.debug(test);
            String data = "data=" + URLEncoder.encode(test, "UTF-8");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            wr.close();
            rd.close();
        } catch (MalformedURLException e) {
            LOGGER.error("Malformed authserver url", e);
            return null;
        } catch (java.io.IOException e) {
            LOGGER.error("IO Error while connected to authserver", e);
            return null;
        }
        LOGGER.info("Received response from authserver at " + surl);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("response:" + response);
        SAXReader reader = new SAXReader();
        Document credential = null;
        try {
            ByteArrayInputStream iv = new ByteArrayInputStream(response.getBytes());
            credential = reader.read(iv);
        } catch (DocumentException e) {
            LOGGER.error("Error reading authserver response", e);
            return null;
        }
        if (LOGGER.isTraceEnabled()) LOGGER.trace(credential.asXML());
        boolean hasChildren = false;
        for (Iterator i = credential.getRootElement().elementIterator(); i.hasNext(); ) {
            hasChildren = true;
            break;
        }
        if (!hasChildren) {
            LOGGER.warn("user not found");
            return null;
        }
        return credential;
    }

    public static String askForIdentity(HashMap<String, String> hauth, String domain, byte[] certBytes, String IDVotazione) {
        LOGGER.debug("Domain: " + domain);
        String surl = (String) hauth.get(domain);
        if (surl == null) {
            LOGGER.error("Auth server for " + domain + " not found!");
            return null;
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("credential");
        Element certificate = root.addElement("certificate").addAttribute("type", "X509");
        String response = "";
        try {
            certificate.addElement("certificate").setText(new String(Base64.encodeBase64(certBytes), "utf-8"));
            response = AgentCore.sendRawData(surl + "/" + IDVotazione, new String(Base64.encodeBase64(document.asXML().getBytes()), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encoding error", e);
            return null;
        }
        if (LOGGER.isDebugEnabled()) LOGGER.debug("response: " + response);
        SAXReader reader = new SAXReader();
        Document credential = null;
        try {
            ByteArrayInputStream iv = new ByteArrayInputStream(response.getBytes());
            credential = reader.read(iv);
        } catch (DocumentException e) {
            LOGGER.error("DocumentException", e);
            return null;
        }
        Element userNode = (Element) credential.selectSingleNode("//credential/user");
        if (userNode == null) {
            LOGGER.warn("user not found");
            return null;
        }
        String userResponse = userNode.attributeValue("value");
        return userResponse;
    }

    public static boolean checkEligibility(Document credential, String IDVotazione, Config config) throws MyException {
        RuleData[] rules = MySql.getRulesFromDB(IDVotazione, config);
        String ruleoptions = MySql.getRuleOptionsFromDB(IDVotazione, config);
        String defaultAction = Votazione.getDefaultAction(ruleoptions);
        String status = defaultAction;
        String username = Votazione.getUsername(credential);
        for (int i = 0; i < rules.length; i++) {
            String targettype = rules[i].targettype;
            String target = rules[i].target;
            String action = rules[i].action;
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Check " + targettype + "::" + target + "=" + action);
            if (targettype.equals("group")) {
                if (Votazione.checkGroup(credential, target)) {
                    status = action;
                    break;
                }
            } else if (targettype.equals("user")) {
                if (target.equals(username)) {
                    status = action;
                    break;
                }
            } else if (targettype.startsWith("xpath:")) {
                String xpath = targettype.substring("xpath:".length());
                List nodeList = credential.selectNodes(xpath);
                boolean found = false;
                Iterator iter = nodeList.iterator();
                while (iter.hasNext()) {
                    Element element = (Element) iter.next();
                    if (element.getText().equals(target)) {
                        status = action;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            } else {
                LOGGER.warn("targettype " + targettype + " unknown!");
            }
        }
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Eligibility Action=" + status);
        return (status.equals("allow"));
    }

    public static byte[] createT1(byte[] keyRegistrar, byte[] cert, String IDVotazione) {
        byte[] T1 = null;
        try {
            byte[] IDVotazioneBytes = IDVotazione.getBytes("utf-8");
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(keyRegistrar);
            algorithm.update(IDVotazioneBytes);
            byte saltVotazione[] = algorithm.digest();
            algorithm.reset();
            algorithm.update(cert);
            algorithm.update(saltVotazione);
            byte K1[] = algorithm.digest();
            T1 = new byte[K1.length + IDVotazioneBytes.length];
            for (int i = 0; i < K1.length; i++) T1[i] = K1[i];
            for (int i = 0; i < IDVotazioneBytes.length; i++) T1[i + K1.length] = IDVotazioneBytes[i];
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encoding error", e);
            T1 = null;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Errore while generating T1", e);
            T1 = null;
        }
        return T1;
    }

    public static byte[] createT2(byte[] T1, byte[] key) {
        byte T2[] = null;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(T1);
            algorithm.update(key);
            T2 = algorithm.digest();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error while generating T2", e);
            T2 = null;
        }
        return T2;
    }
}
