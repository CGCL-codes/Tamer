package org.openscience.nmrshiftdb.util;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import org.apache.ecs.ClearElement;
import org.apache.jetspeed.util.SimpleTransform;
import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;
import org.apache.turbine.om.peer.BasePeer;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.pull.ApplicationTool;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.Log;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.ServletUtils;
import org.apache.turbine.util.db.Criteria;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.openscience.nmrshiftdb.om.DBLabGroupPeer;
import org.openscience.nmrshiftdb.om.DBMoleculePeer;
import org.openscience.nmrshiftdb.om.NmrshiftdbUser;
import org.openscience.nmrshiftdb.om.NmrshiftdbUserPeer;
import com.workingdogs.village.Record;

/**
 *  This is class building the http(s) url from request url.
 *
 * @author     shk3.
 * @created    April 29, 2003.
 */
public class UrlTool implements ApplicationTool {

    private RunData data = null;

    private ServletConfig servcon = null;

    private static HashMap users = new HashMap();

    /**
   *  Sets the servlet configuration value (this is only needed for getServerOverview).
   *
   * @param  servcon  The servletconfiguration.
   */
    public void setServcon(ServletConfig servcon) {
        this.servcon = servcon;
    }

    /**
   *  Gets the logon wiht(out) link.
   *
   * @return    The link.
   */
    public String getAppropriateURL() {
        String url = javax.servlet.http.HttpUtils.getRequestURL(data.getRequest()).toString();
        if (url.indexOf("https") != -1) {
            url = "<a href=\"" + GeneralUtils.replace(url, "https", "http") + "\">Logon without SSL</a>";
        } else {
            url = "<a href=\"" + GeneralUtils.replace(url, "http", "https") + "\">Logon with SSL</a>";
        }
        return (url);
    }

    /**
   *  Tells if the current connection is by https or not.
   *
   * @return    The isHttps value.
   */
    public String getIsHttps() {
        if (javax.servlet.http.HttpUtils.getRequestURL(data.getRequest()).toString().indexOf("https") != -1) {
            return ("true");
        } else {
            return ("false");
        }
    }

    /**
   *  Gets an overview of nmrshiftdb servers by rendering servers.xml.
   *
   * @return                                The serverOverview value.
   */
    public String getServerOverview() {
        StringWriter w = new StringWriter();
        try {
            ClearElement ce = new ClearElement(SimpleTransform.transform("http://nmrshiftdb.sf.net/nmrshiftdbservers.xml", "http://nmrshiftdb.sf.net/nmrshiftdbservers.xsl", new HashMap()));
            File outputFile = new File(ServletUtils.expandRelative(servcon, "/WEB-INF/templates/vm/homeprov.vm"));
            FileWriter out = new FileWriter(outputFile);
            out.write(ce + "");
            out.close();
            VelocityContext context = new VelocityContext();
            context.put("urltool", new UrlTool());
            Velocity.mergeTemplate("homeprov.vm", "ISO-8859-1", context, w);
        } catch (Exception ex) {
            GeneralUtils.logError(ex, "ServerOverview", null, false);
            w.write("Server overview currently not available!");
        }
        return (w.toString());
    }

    /**
   *  Gets the number of users on a certain server.
   *
   * @param  servername  The name of the server.
   * @return             The users value.
   */
    public String getUsers(String servername) {
        Response r = null;
        Call c = new Call();
        Vector parameters = new Vector();
        c.setTargetObjectURI("urn:xml-soap-greeter-demo");
        c.setMethodName("getUserNumber");
        c.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
        c.setParams(parameters);
        try {
            r = c.invoke(new URL("http://" + servername + "/soap/servlet/rpcrouter"), "");
        } catch (Exception e) {
            Log.error("Error in calling webservice");
        }
        if (r == null) {
            return ("Unavailable");
        }
        if (r.generatedFault()) {
            Fault f = r.getFault();
            Log.error("Error Occurred: ");
            Log.error("  Fault Code   = " + f.getFaultCode());
            Log.error("  Fault String = " + f.getFaultString());
            return ("Unavailable");
        } else {
            Parameter greeting = r.getReturnValue();
            return ((String) greeting.getValue());
        }
    }

    /**
   *  Resets the user inteface of the currently logged in user and returns an appropriate message
   *
   * @return                The resetMessage value
   * @exception  Exception  Description of Exception
   */
    public String getResetMessage() throws Exception {
        try {
            if (((NmrshiftdbUser) data.getUser()).isLabgroupUser(data)) {
                Vector rs = NmrshiftdbUserPeer.executeQuery("select DISTINCT PROFILE from JETSPEED_USER_PROFILE where MEDIA_TYPE ='html' and PAGE='default.psml' and  USER_NAME='testuser'");
                NmrshiftdbUserPeer.executeStatement("update JETSPEED_USER_PROFILE set PROFILE ='" + ((Record) rs.get(0)).getValue(1) + "' where USER_NAME ='" + data.getUser().getUserName() + "' and MEDIA_TYPE='html';");
            } else if (((NmrshiftdbUser) data.getUser()).isLabgroupUser(data)) {
                Vector rs = NmrshiftdbUserPeer.executeQuery("select DISTINCT PROFILE from JETSPEED_USER_PROFILE where MEDIA_TYPE ='html' and PAGE='default.psml' and  USER_NAME='testworker'");
                NmrshiftdbUserPeer.executeStatement("update JETSPEED_USER_PROFILE set PROFILE ='" + ((Record) rs.get(0)).getValue(1) + "' where USER_NAME ='" + data.getUser().getUserName() + "' and MEDIA_TYPE='html';");
            } else {
                NmrshiftdbUserPeer.executeStatement("update JETSPEED_USER_PROFILE set PROFILE =(select DISTINCT PROFILE from JETSPEED_ANON_PROFILE where MEDIA_TYPE ='html' and LANGUAGE ='en' and PAGE='default.psml' limit 1) where USER_NAME ='" + data.getUser().getUserName() + "' and MEDIA_TYPE='html';");
            }
            return ("The user interface has been reset to initial configuration!");
        } catch (Exception ex) {
            return (GeneralUtils.logError(ex, "reset user interface user " + data.getUser().getUserName(), data, true));
        }
    }

    /**
   *  Gets the baseUrl attribute of the UrlTool object
   *
   * @return    The baseUrl value
   */
    public String getBaseUrl() {
        StringTokenizer st = new StringTokenizer(javax.servlet.http.HttpUtils.getRequestURL(data.getRequest()).toString(), "/");
        StringBuffer url = new StringBuffer(st.nextToken());
        url.append("//");
        url.append(st.nextToken());
        return (url.toString());
    }

    /**
   *  Init from ApplicationTool interface.
   *
   * @param  data  RunData.
   */
    public void init(Object data) {
        this.data = (RunData) data;
        try {
            Cookie[] cookies = this.data.getRequest().getCookies();
            if (cookies != null) {
                String username = "";
                String password = "";
                for (int k = 0; k < cookies.length; k++) {
                    Cookie CTemp = cookies[k];
                    if (!CTemp.getValue().equals("invalidnmrshiftdb")) {
                        CTemp.setMaxAge(3000000);
                        this.data.getResponse().addCookie(CTemp);
                        if (CTemp.getName().equals("NmrshiftdbUser")) {
                            username = CTemp.getValue();
                        }
                        if (CTemp.getName().equals("NmrshiftdbPassword")) {
                            password = CTemp.getValue();
                        }
                    }
                }
                if (!username.equals("")) {
                    User user = TurbineSecurity.getAuthenticatedUser(username, password);
                    this.data.setUser(user);
                    user.setHasLoggedIn(new Boolean(true));
                    user.updateLastLogin();
                    this.data.save();
                }
                users.put(((RunData) data).getSession().getId(), new Long(System.currentTimeMillis()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   *  Refresh from ApplicationTool interface.
   */
    public void refresh() {
    }

    public static HashMap getUsersMap() {
        return (users);
    }

    public Vector getLabGroups() throws Exception {
        Criteria crit = new Criteria();
        return (DBLabGroupPeer.doSelect(crit));
    }

    public int getRandomId() throws Exception {
        String sql = "select MOLECULE_ID from MOLECULE ORDER BY rand() LIMIT 1;";
        List l = DBMoleculePeer.executeQuery(sql);
        return ((Record) l.get(0)).getValue(1).asInt();
    }

    public Vector getAff1() throws Exception {
        Vector v = BasePeer.executeQuery("select distinct AFFILIATION_1 from TURBINE_USER where LABGROUP>0");
        Vector v2 = new Vector();
        v2.add("");
        for (int i = 0; i < v.size(); i++) {
            v2.add(((Record) v.get(i)).getValue(1).asString());
        }
        return v2;
    }

    public Vector getAff2() throws Exception {
        Vector v = BasePeer.executeQuery("select distinct AFFILIATION_2 from TURBINE_USER where LABGROUP>0");
        Vector v2 = new Vector();
        v2.add("");
        for (int i = 0; i < v.size(); i++) {
            v2.add(((Record) v.get(i)).getValue(1).asString());
        }
        return v2;
    }
}
