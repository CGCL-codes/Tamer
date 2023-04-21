package com.makeabyte.jhosting.server.ejb;

import static com.makeabyte.jhosting.common.StringUtils.getRandomString;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.ejb.Stateless;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.apache.axis2.transport.http.HTTPConstants;
import com.makeabyte.jhosting.common.Constants;
import com.makeabyte.jhosting.server.Configuration;
import com.makeabyte.jhosting.server.persistence.entity.ApplicationEntity;
import com.makeabyte.appstore.webservice.AppstoreAPIStub;
import com.makeabyte.appstore.webservice.AppstoreAPIStub.AppWS;
import com.makeabyte.appstore.webservice.AppstoreAPIStub.Download;
import com.makeabyte.appstore.webservice.AppstoreAPIStub.GetAppsByPlatformId;
import com.makeabyte.appstore.webservice.AppstoreAPIStub.Login;

@Stateless
@Name("AppstoreAPIService")
public class AppstoreAPIService implements AppstoreAPIServiceLocal {

    private String username;

    private String password;

    private String apiKey;

    private String downloadDir;

    private long platformId;

    private AppstoreAPIStub stub;

    @In(create = true)
    private ApplicationServiceLocal ApplicationService;

    @DataModel
    private List<AppWS> appList;

    @Logger
    private Log log;

    /**
	    * AppstoreAPIService constructor
	    */
    public AppstoreAPIService() {
        try {
            Properties props = Configuration.getInstance().getProperties();
            username = props.getProperty("com.makeabyte.appstore.username").toString();
            password = props.getProperty("com.makeabyte.appstore.password").toString();
            apiKey = props.getProperty("com.makeabyte.appstore.apikey").toString();
            downloadDir = props.getProperty("com.makeabyte.jhosting.downloads");
            platformId = Long.parseLong(props.getProperty("com.makeabyte.appstore.platformId").toString());
            stub = new AppstoreAPIStub();
            stub._getServiceClient().getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            FacesMessages.instance().add(ioe.getMessage());
        }
    }

    /**
	    * Returns the list of applications available to jhosting
	    * 
	    * @return A list of AppWS objects representing applications available for installation
	    */
    public List<AppWS> getAppList() {
        return appList;
    }

    /**
	    * Logs into the web service
	    */
    public void login() {
        try {
            Login login = new AppstoreAPIStub.Login();
            login.setUsername(username);
            login.setPassword(password);
            login.setApiKey(apiKey);
            AppstoreAPIStub.LoginE loginImpl = new AppstoreAPIStub.LoginE();
            loginImpl.setLogin(login);
            boolean result = stub.login(loginImpl).getLoginResponse().get_return();
            if (!result) {
                log.error("Error logging into appstore using username '#0' password '#1' apiKey '#2' platformId '#3' endpoint '#4'", username, password, apiKey, platformId, stub._getServiceClient().getTargetEPR().getAddress());
                FacesMessages.instance().add("Error logging into appstore");
            }
        } catch (RemoteException re) {
            re.printStackTrace();
            FacesMessages.instance().add(re.getMessage());
        }
    }

    /**
	    * Sets the appList with a list of all the applications available for jhosting
	    */
    public void prepareAppList() {
        try {
            GetAppsByPlatformId getAppsByPlatformId;
            getAppsByPlatformId = new AppstoreAPIStub.GetAppsByPlatformId();
            getAppsByPlatformId.setId(platformId);
            AppstoreAPIStub.GetAppsByPlatformIdE getAppsByPlatformIdImpl = new AppstoreAPIStub.GetAppsByPlatformIdE();
            getAppsByPlatformIdImpl.setGetAppsByPlatformId(getAppsByPlatformId);
            AppstoreAPIStub.AppWS[] appstoreApps = stub.getAppsByPlatformId(getAppsByPlatformIdImpl).getGetAppsByPlatformIdResponse().get_return();
            List<AppWS> apps = new ArrayList<AppWS>(0);
            for (AppstoreAPIStub.AppWS appstoreApp : appstoreApps) apps.add(appstoreApp);
            appList = apps;
        } catch (RemoteException re) {
            re.printStackTrace();
            FacesMessages.instance().add(re.getMessage());
        }
    }

    /**
	    * Downloads an application given its id using a random file path
	    * 
	    * @return The path to the downloaded file
	    */
    public String download(long id) {
        String dir = new StringBuilder(downloadDir).append(Constants.FILE_SEPARATOR).append("JH-").append(getRandomString(10)).toString();
        String app = new StringBuilder(dir).append(Constants.FILE_SEPARATOR).append(id).append(".jhp").toString();
        File d = new File(dir);
        if (d.exists()) download(id);
        if (!d.mkdirs()) {
            log.error("Error creating temp download directory: #0", dir);
            throw new RuntimeException("Could not create temp download directory: " + dir);
        }
        try {
            Download download = new AppstoreAPIStub.Download();
            download.setId(id);
            AppstoreAPIStub.DownloadE downloadImpl = new AppstoreAPIStub.DownloadE();
            downloadImpl.setDownload(download);
            DataHandler data = stub.download(downloadImpl).getDownloadResponse().get_return();
            InputStream inputStream = data.getInputStream();
            File f = new File(app);
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
            log.info("Done downloading application to #0", d.getAbsolutePath());
            return app;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            FacesMessages.instance().add(ioe.getMessage());
        }
        return null;
    }

    /**
	    * Removes applications from the appList which are already installed.
	    * 
	    * TODO: Encapsulate LdapLookupException and throw generic DataAccessException instead
	    */
    public void filterAppList() {
        try {
            for (ApplicationEntity app : ApplicationService.getApps()) {
                int index = listContains(app.getAppId());
                if (index != -1) appList.remove(index);
            }
        } catch (com.makeabyte.jhosting.server.persistence.ldap.LdapLookupException lle) {
            lle.printStackTrace();
        }
    }

    /**
	    * Check to see if the appList of AppWS objects contains the specified appId.
	    * 
	    * @param appId The appId to check
	    * @return The index of the AppWS object which contains the appId
	    */
    private int listContains(String appId) {
        for (int i = 0; i < appList.size(); i++) if (appList.get(i).getAppId().equals(appId)) return i;
        return -1;
    }
}
