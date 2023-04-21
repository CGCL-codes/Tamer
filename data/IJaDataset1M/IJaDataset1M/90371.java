package edu.upmc.opi.caBIG.caTIES.server.dispatcher.dorianUpdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import edu.upmc.opi.caBIG.caTIES.client.ApplicationLauncher;
import edu.upmc.opi.caBIG.caTIES.client.config.PropertyDefaults;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_PropertyLoader;
import edu.upmc.opi.caBIG.caTIES.security.CaTIES_SecurityManager;
import edu.upmc.opi.caBIG.caTIES.server.dispatcher.CaTIES_CommandProcessor;
import edu.upmc.opi.caBIG.common.CaBIG_LobUtilities;
import gov.nih.nci.cagrid.dorian.client.IdPAdministrationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;

/**
 * Changes dorian user information including password. This is required since only
 * the admininstrator can change account info in dorian(for the version we use)
 * in the future, if we upgrade dorian, this will not be required as the latest versions
 * let users change their own information.
 */
public class CaTIES_DorianUpdater extends CaTIES_CommandProcessor {

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_DorianUpdater.class);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        IdPUser user = new IdPUser();
        user.setUserId("dorian");
        user.setPassword("password");
        CaTIES_DorianUpdater updater = new CaTIES_DorianUpdater();
        updater.initDummyProperties();
        try {
            updater.updateDorianUser(user);
        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDummyProperties() {
        this.properties = new Properties();
        properties.setProperty(CaTIES_Constants.USER_NAME_KEY, "dorian");
        properties.setProperty(CaTIES_Constants.USER_PASSWORD_KEY, "password");
        properties.setProperty(CaTIES_Constants.DORIAN_SERVICE_URI_KEY, "https://157.229.220.28:8443/wsrf/services/cagrid/Dorian");
        properties.setProperty(CaTIES_Constants.DORIANUPDATER_PROPERTIESFILEPREFIX + ".0", "C:/sandbox/caties/tomcat55/webapps/wsrf/WEB-INF/etc/secure_Dispatcher/caTIES.properties");
        properties.setProperty(CaTIES_Constants.DORIANUPDATER_PROPERTIESFILEPREFIX + ".1", "C:/sandbox/caties/tomcat55/webapps/wsrf/WEB-INF/etc/secure_test_Dispatcher/caTIES.properties");
    }

    @Override
    public Document processCommand(Document command) {
        if (!isAuthorized()) {
            return unauthorizedReply(command);
        }
        try {
            Element requestElement = (Element) command.getRootElement().getChild("Request");
            IdPUser user = (IdPUser) CaBIG_LobUtilities.decodeLob(requestElement.getText().getBytes());
            updateDorianUser(user);
            clearDocumentContent(command);
            this.responseElement = new Element("Response");
            command.getRootElement().addContent(this.responseElement);
            responseElement.setAttribute("status", "success");
            responseElement.setText("Success");
        } catch (Exception x) {
            this.responseElement = new Element("Response");
            responseElement.setAttribute("status", "failure");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream s = new PrintStream(bos);
            x.printStackTrace(s);
            responseElement.setText(bos.toString());
            command.getRootElement().addContent(this.responseElement);
            x.printStackTrace();
        }
        return command;
    }

    private void updateDorianUser(IdPUser updatedUser) throws MalformedURIException, RemoteException, Exception {
        new CaTIES_PropertyLoader(PropertyDefaults.getDefaultForProperty(CaTIES_Constants.PROPERTY_KEY_APPLICATION_CONFIG_FILE));
        String username = properties.getProperty(CaTIES_Constants.USER_NAME_KEY, null);
        String password = properties.getProperty(CaTIES_Constants.USER_PASSWORD_KEY, null);
        String dorianUri = properties.getProperty(CaTIES_Constants.DORIAN_SERVICE_URI_KEY, null);
        if (username == null || password == null || dorianUri == null) {
            throw new Exception("Config file does not contain login parameters.");
        }
        System.setProperty(CaTIES_Constants.USER_NAME_KEY, username);
        System.setProperty(CaTIES_Constants.USER_PASSWORD_KEY, password);
        System.setProperty(CaTIES_Constants.DORIAN_SERVICE_URI_KEY, dorianUri);
        CaTIES_SecurityManager securityManager = CaTIES_SecurityManager.getInstance();
        securityManager.setUserName(username);
        securityManager.setUserPassword(password);
        securityManager.authenticate();
        if (securityManager.getDistinguishedName() == null) throw new Exception("Login failure for Dorian Updater service. Check account configuration for Dorian Updater Service");
        IdPAdministrationClient client = new IdPAdministrationClient(CaTIES_SecurityManager.getInstance().getDorianServiceUri(), CaTIES_SecurityManager.getInstance().getProxyCredentialFromDorianIdP());
        IdPUserFilter filter = new IdPUserFilter();
        filter.setUserId(updatedUser.getUserId());
        IdPUser[] list = client.findUsers(filter);
        if (list == null || list.length == 0) throw new Exception("No user found with id " + updatedUser.getUserId());
        IdPUser user = list[0];
        syncUserObjects(user, updatedUser);
        client.updateUser(user);
        if (user.getUserId().equals(username) && user.getPassword() != null) {
            System.setProperty(CaTIES_Constants.USER_PASSWORD_KEY, user.getPassword());
            changePasswordInConfiguration(user);
        }
    }

    private void syncUserObjects(IdPUser user, IdPUser updatedUser) {
        if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());
        if (updatedUser.getAddress2() != null) user.setAddress2(updatedUser.getAddress2());
        if (updatedUser.getCity() != null) user.setCity(updatedUser.getCity());
        if (updatedUser.getState() != null) user.setState(updatedUser.getState());
        if (updatedUser.getCountry() != null) user.setCountry(updatedUser.getCountry());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhoneNumber() != null) user.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getFirstName() != null) user.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null) user.setLastName(updatedUser.getLastName());
        if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
        if (updatedUser.getRole() != null) user.setRole(updatedUser.getRole());
    }

    private void changePasswordInConfiguration(IdPUser user) throws IOException {
        List<String> propertyFiles = getPropertyFileList();
        for (String filepath : propertyFiles) {
            File propertiesFile = new File(filepath);
            if (propertiesFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(propertiesFile));
                String line = null;
                List<String> propertyFileLines = new ArrayList<String>();
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(CaTIES_Constants.USER_PASSWORD_KEY)) ; else propertyFileLines.add(line);
                }
                reader.close();
                BufferedWriter writer = new BufferedWriter(new FileWriter(propertiesFile));
                for (Iterator iterator = propertyFileLines.iterator(); iterator.hasNext(); ) {
                    line = (String) iterator.next();
                    logger.debug("Writing " + line);
                    writer.write(line);
                    writer.newLine();
                }
                line = CaTIES_Constants.USER_PASSWORD_KEY + " = " + user.getPassword();
                logger.debug("Writing password line");
                writer.write(line);
                writer.newLine();
                writer.close();
            }
        }
    }

    private List<String> getPropertyFileList() {
        List<String> propertyFiles = new ArrayList<String>();
        int i = 0;
        while (properties.getProperty(CaTIES_Constants.DORIANUPDATER_PROPERTIESFILEPREFIX + "." + i) != null) {
            propertyFiles.add(properties.getProperty(CaTIES_Constants.DORIANUPDATER_PROPERTIESFILEPREFIX + "." + i));
            i++;
        }
        return propertyFiles;
    }

    @Override
    public void dispose() {
    }
}
