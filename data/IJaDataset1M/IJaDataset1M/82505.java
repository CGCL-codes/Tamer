package neembuuuploader.accounts;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import neembuuuploader.HostsPanel;
import neembuuuploader.NeembuuUploader;
import neembuuuploader.TranslationProvider;
import neembuuuploader.accountgui.AccountsManager;
import neembuuuploader.interfaces.abstractimpl.AbstractAccount;
import neembuuuploader.utils.NULogger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author Dinesh
 */
public class DropBoxAccount extends AbstractAccount {

    private URL u;

    private HttpURLConnection uc;

    private static String tmp;

    private static String gvccookie;

    private BufferedReader br;

    private static String forumjarcookie = "", touchcookie = "";

    private static String forumlidcookie = "", lidcookie = "", jarcookie = "";

    public DropBoxAccount() {
        KEY_USERNAME = "dbusername";
        KEY_PASSWORD = "dbpassword";
        HOSTNAME = "DropBox.com";
    }

    public void disableLogin() {
        loginsuccessful = false;
        HostsPanel.getInstance().dropBoxCheckBox.setEnabled(false);
        HostsPanel.getInstance().dropBoxCheckBox.setSelected(false);
        NeembuuUploader.getInstance().updateSelectedHostsLabel();
        NULogger.getLogger().log(Level.INFO, "{0} account disabled", getHOSTNAME());
    }

    private void initialize() throws Exception {
        NULogger.getLogger().info("Getting startup cookie from dropbox.com");
        u = new URL("http://www.dropbox.com/");
        uc = (HttpURLConnection) u.openConnection();
        Map<String, List<String>> headerFields = uc.getHeaderFields();
        if (headerFields.containsKey("set-cookie")) {
            List<String> header = headerFields.get("set-cookie");
            for (int i = 0; i < header.size(); i++) {
                tmp = header.get(i);
                if (tmp.contains("gvc")) {
                    gvccookie = tmp;
                }
            }
        }
        gvccookie = gvccookie.substring(0, gvccookie.indexOf(";"));
        NULogger.getLogger().log(Level.INFO, "gvccookie: {0}", gvccookie);
        uc.disconnect();
    }

    public void loginDropBox() throws Exception {
        loginsuccessful = false;
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().info("Trying to log in to dropbox.com");
        HttpPost httppost = new HttpPost("https://www.dropbox.com/login");
        httppost.setHeader("Cookie", gvccookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login_email", getUsername()));
        formparams.add(new BasicNameValuePair("login_password", getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        NULogger.getLogger().info("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("forumjar")) {
                forumjarcookie = "forumjar=" + escookie.getValue();
                loginsuccessful = true;
                NULogger.getLogger().info(forumjarcookie);
            }
            if (escookie.getName().equalsIgnoreCase("touch")) {
                touchcookie = "touch=" + escookie.getValue();
                NULogger.getLogger().info(touchcookie);
            }
            if (escookie.getName().equalsIgnoreCase("forumlid")) {
                forumlidcookie = "forumlid=" + escookie.getValue();
                NULogger.getLogger().info(forumlidcookie);
            }
            if (escookie.getName().equalsIgnoreCase("lid")) {
                lidcookie = "lid=" + escookie.getValue();
                NULogger.getLogger().info(lidcookie);
            }
            if (escookie.getName().equalsIgnoreCase("jar")) {
                jarcookie = "jar=" + escookie.getValue();
                NULogger.getLogger().info(jarcookie);
            }
        }
        if (loginsuccessful) {
            loginsuccessful = true;
            HostsPanel.getInstance().dropBoxCheckBox.setEnabled(true);
            username = getUsername();
            password = getPassword();
            NULogger.getLogger().info("DropBox Login success :)");
        } else {
            NULogger.getLogger().log(Level.SEVERE, "DropBox Login failed :(");
            loginsuccessful = false;
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }

    public static String getForumjarcookie() {
        return forumjarcookie;
    }

    public static String getForumlidcookie() {
        return forumlidcookie;
    }

    public static String getGvccookie() {
        return gvccookie;
    }

    public static String getJarcookie() {
        return jarcookie;
    }

    public static String getLidcookie() {
        return lidcookie;
    }

    public static String getTouchcookie() {
        return touchcookie;
    }

    public void login() {
        try {
            initialize();
            loginDropBox();
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: Error in DropBox Login", getClass().getName());
        }
    }
}
