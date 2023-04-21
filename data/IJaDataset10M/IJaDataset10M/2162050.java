package com.velocityme.utility;

import com.velocityme.interfaces.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;
import javax.ejb.FinderException;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author  Robert
 */
public class ServerConfiguration {

    private static ServerConfiguration m_configInstance = null;

    private String m_datasource = "java:/VelocitymeDS";

    private boolean m_valid = false;

    private PublicKey m_licenceVerificationKey;

    private SystemPropertyLocal m_propertyClientGuidLocal;

    private SystemPropertyLocal m_propertyEmailFromAddressLocal;

    private SystemPropertyLocal m_propertyEmailInternetUrlLocal;

    private SystemPropertyLocal m_propertyEmailIntranetUrlLocal;

    private SystemPropertyLocal m_propertyEmailAccountLocal;

    private SystemPropertyLocal m_propertyEmailAccountUsernameLocal;

    private SystemPropertyLocal m_propertyEmailAccountPasswordLocal;

    private SystemPropertyLocal m_propertyFileAttachmentHomeLocal;

    private SystemPropertyLocal m_propertyOwnerOrganisationLocal;

    private SystemPropertyLocal m_propertyOwnerOrganisationUrlLocal;

    private static final Logger m_log = Logger.getLogger(ServerConfiguration.class);

    /** Creates a new instance of ServerConfiguration */
    private ServerConfiguration() {
        try {
            SystemPropertyLocalHome propertyLocalHome = SystemPropertyUtil.getLocalHome();
            m_propertyClientGuidLocal = propertyLocalHome.findByName("client.guid");
            m_propertyEmailFromAddressLocal = propertyLocalHome.findByName("email.from.address");
            m_propertyEmailInternetUrlLocal = propertyLocalHome.findByName("email.internet.url");
            m_propertyEmailIntranetUrlLocal = propertyLocalHome.findByName("email.intranet.url");
            m_propertyEmailAccountLocal = propertyLocalHome.findByName("email.account");
            m_propertyEmailAccountUsernameLocal = propertyLocalHome.findByName("email.account.username");
            m_propertyEmailAccountPasswordLocal = propertyLocalHome.findByName("email.account.password");
            m_propertyFileAttachmentHomeLocal = propertyLocalHome.findByName("file.attachment.home");
            m_propertyOwnerOrganisationLocal = propertyLocalHome.findByName("owner.organisation");
            m_propertyOwnerOrganisationUrlLocal = propertyLocalHome.findByName("owner.organisation.url");
            m_valid = true;
        } catch (FinderException e) {
            m_log.error(e.getMessage());
        } catch (NamingException e) {
            m_log.error(e.getMessage());
        }
        try {
            InputStream is = getClass().getResourceAsStream("/com/velocityme/utility/licenceVerification.key");
            byte[] encKey = new byte[is.available()];
            is.read(encKey);
            is.close();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            m_licenceVerificationKey = keyFactory.generatePublic(pubKeySpec);
        } catch (IOException e) {
            m_log.error(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            m_log.error(e.getMessage());
        } catch (InvalidKeySpecException e) {
            m_log.error(e.getMessage());
        }
    }

    public static ServerConfiguration getInstance() {
        if (m_configInstance == null || m_configInstance.m_valid == false) m_configInstance = new ServerConfiguration();
        return m_configInstance;
    }

    public URL getInternetServerURL(Integer p_taskId) {
        try {
            URL internetURL = new URL(m_propertyEmailInternetUrlLocal.getValue());
            return new URL(internetURL.getProtocol(), internetURL.getHost(), internetURL.getPort(), internetURL.getPath() + "/showNode.do?nodeId=" + p_taskId);
        } catch (MalformedURLException e) {
            m_log.error("Error in Internet Server URL");
            m_log.error(e.getMessage());
            return null;
        }
    }

    public URL getIntranetServerURL(Integer p_taskId) {
        try {
            URL intranetURL = new URL(m_propertyEmailIntranetUrlLocal.getValue());
            return new URL(intranetURL.getProtocol(), intranetURL.getHost(), intranetURL.getPort(), intranetURL.getPath() + "/showNode.do?nodeId=" + p_taskId);
        } catch (MalformedURLException e) {
            m_log.error("Error in Internet Server URL");
            m_log.error(e.getMessage());
            return null;
        }
    }

    public URLName getMailURLName() {
        if (m_propertyEmailAccountLocal.getValue().equals("")) return null;
        URLName account = new URLName(m_propertyEmailAccountLocal.getValue());
        URLName emailURLName = new URLName(account.getProtocol(), account.getHost(), account.getPort(), null, m_propertyEmailAccountUsernameLocal.getValue(), m_propertyEmailAccountPasswordLocal.getValue());
        return emailURLName;
    }

    public InternetAddress getMailFromAddress() throws AddressException {
        return new InternetAddress(m_propertyEmailFromAddressLocal.getValue());
    }

    public String getDataSource() {
        return m_datasource;
    }

    public File getFileAttachmentHome() {
        return new File(m_propertyFileAttachmentHomeLocal.getValue());
    }

    public String getOwnerOrganisation() {
        if (m_propertyOwnerOrganisationLocal != null) return m_propertyOwnerOrganisationLocal.getValue(); else return "Uninitialised";
    }

    public URL getOwnerOrganisationURL() {
        try {
            if (m_propertyOwnerOrganisationUrlLocal != null) return new URL(m_propertyOwnerOrganisationUrlLocal.getValue()); else return null;
        } catch (MalformedURLException e) {
            m_log.error("Error in Owner Organisation URL");
            m_log.error(e.getMessage());
            return null;
        }
    }

    public int getUserLoginCountLimit() {
        return 3;
    }

    public PublicKey getLicenceVerificationKey() {
        return m_licenceVerificationKey;
    }

    public String getClientGUID() {
        return m_propertyClientGuidLocal.getValue();
    }
}
