package neembuuuploader.accounts;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import neembuuuploader.HostsPanel;
import neembuuuploader.NeembuuUploader;
import neembuuuploader.TranslationProvider;
import neembuuuploader.accountgui.AccountsManager;
import neembuuuploader.interfaces.abstractimpl.AbstractAccount;
import neembuuuploader.uploaders.common.CommonUploaderTasks;
import neembuuuploader.utils.NULogger;
import neembuuuploader.utils.NeembuuUploaderProperties;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author dinesh
 */
public class IFileAccount extends AbstractAccount {

    public static final String KEY_API_KEY = "ifile_api_key";

    private String loginresponse;

    private static String apikey;

    public IFileAccount() {
        KEY_USERNAME = "ifusername";
        KEY_PASSWORD = "ifpassword";
        HOSTNAME = "IFile.it";
    }

    public static String getApikey() {
        return apikey;
    }

    @Override
    public void disableLogin() {
        NeembuuUploaderProperties.setEncryptedProperty(KEY_API_KEY, "");
        loginsuccessful = false;
        HostsPanel.getInstance().iFileCheckBox.setEnabled(false);
        HostsPanel.getInstance().iFileCheckBox.setSelected(false);
        NeembuuUploader.getInstance().updateSelectedHostsLabel();
        NULogger.getLogger().log(Level.INFO, "{0} account disabled", getHOSTNAME());
    }

    @Override
    public void login() {
        if (!username.isEmpty()) {
            if (username.equals(getUsername()) && password.equals(getPassword())) {
                return;
            } else {
                loginsuccessful = false;
                NeembuuUploaderProperties.setEncryptedProperty(KEY_API_KEY, "");
            }
        }
        if (!NeembuuUploaderProperties.getEncryptedProperty(KEY_API_KEY).isEmpty()) {
            HostsPanel.getInstance().iFileCheckBox.setEnabled(true);
            loginsuccessful = true;
            username = getUsername();
            password = getPassword();
            return;
        }
        try {
            loginsuccessful = false;
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to ifile.it");
            HttpPost httppost = new HttpPost("https://secure.ifile.it/api-fetch_apikey.api");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("username", getUsername()));
            formparams.add(new BasicNameValuePair("password", getPassword()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            loginresponse = EntityUtils.toString(httpresponse.getEntity());
            NULogger.getLogger().log(Level.INFO, "response : {0}", loginresponse);
            httpclient.getConnectionManager().shutdown();
            if (loginresponse.contains("akey")) {
                apikey = CommonUploaderTasks.parseResponse(loginresponse, "akey\":\"", "\"");
                NULogger.getLogger().info("IFile Login sccuess :)");
                NULogger.getLogger().log(Level.INFO, "API key : {0}", apikey);
                loginsuccessful = true;
                HostsPanel.getInstance().iFileCheckBox.setEnabled(true);
                username = getUsername();
                password = getPassword();
                NULogger.getLogger().info("IFile Login success :)");
                NeembuuUploaderProperties.setEncryptedProperty(KEY_API_KEY, apikey);
            } else {
                NeembuuUploaderProperties.setEncryptedProperty(KEY_API_KEY, "");
                NULogger.getLogger().info("IFile Login failed :(");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: Error in IFile Login", getClass().getName());
        }
    }
}
