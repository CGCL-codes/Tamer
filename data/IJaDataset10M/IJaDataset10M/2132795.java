package org.dcm4chee.xero.dicom;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Map;
import org.dcm4che2.data.UID;
import org.dcm4che2.net.Device;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.NetworkConnection;
import org.dcm4che2.net.TransferCapability;
import org.dcm4chee.xero.metadata.filter.FilterUtil;
import org.dcm4chee.xero.search.AEProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory that abstracts the logic of loading Application Entities (AE) for the
 * client application.
 * <P>
 * TODO: Consider using an identifier formatted as <i>aeTitle@hostName</i> to uniquely identify stations.
 * @author Andrew Cowan (amidx)
 */
public class ApplicationEntityProvider {

    private static final Logger log = LoggerFactory.getLogger(ApplicationEntityProvider.class);

    private static final String[] NATIVE_LE_TS = { UID.ImplicitVRLittleEndian, UID.ExplicitVRLittleEndian };

    private static final String DEFAULT_LOCAL_AE_TITLE = "XERO";

    private final KeyStoreLoader keyStoreLoader;

    public ApplicationEntityProvider(KeyStoreLoader keyStoreLoader) {
        this.keyStoreLoader = keyStoreLoader;
    }

    public ApplicationEntityProvider() {
        this(new SimpleKeyStoreLoader());
    }

    /**
    * Create a new AE and configure the supported sop class uids.
    * @param aePath <aeTitle>[@hostName][:port]
    * @param sopClassUIDs SOP class UIDs for this AEs capabilities.
    * @throws IllegalArgumentException if the AE is not configured
    * @return Valid AE instance.
    */
    public NetworkApplicationEntity getAE(String aePath, String... sopClassUIDs) throws IOException {
        Map<String, Object> settings = AEProperties.getInstance().getAE(aePath);
        if (settings == null) throw new IllegalArgumentException("Unknown AE path: " + aePath);
        log.debug("Creating a new AE for {}", aePath);
        NetworkApplicationEntity ae = createAE(settings);
        NetworkConnection connection = createNetworkConnection(settings);
        if (connection.getPort() == 0) log.warn("The port for AE {} is not set and will be set automatically by the system.");
        Device device = createDevice(settings);
        ae.setNetworkConnection(connection);
        device.setNetworkApplicationEntity(ae);
        device.setNetworkConnection(connection);
        configureTransferCapabilities(ae, sopClassUIDs);
        return ae;
    }

    /**
    * Configure the TransferCapabilities of the indicated AE.
    */
    private void configureTransferCapabilities(NetworkApplicationEntity ae, String[] sopClassUIDs) {
        int idx = 0;
        TransferCapability[] transferCapabilities = new TransferCapability[sopClassUIDs.length];
        for (String cuid : sopClassUIDs) {
            TransferCapability tc = new TransferCapability(cuid, NATIVE_LE_TS, TransferCapability.SCU);
            transferCapabilities[idx++] = tc;
        }
        ae.setTransferCapability(transferCapabilities);
    }

    /**
    * Configure the network connection for the AE from the properties file.
    * <p>
    * The configured values are
    * <ul>
    * <li>AE host
    * <li>AE port
    * <li>TcpNoDelay = true (default)
    * <li>tls
    * </ul>
    */
    private NetworkConnection createNetworkConnection(Map<String, Object> settings) {
        NetworkConnection connection = new NetworkConnection();
        connection.setHostname((String) settings.get(AEProperties.AE_HOST_KEY));
        connection.setPort((Integer) settings.get(AEProperties.AE_PORT_KEY));
        String tls = (String) settings.get("tls");
        if (tls != null) {
            if ("AES".equalsIgnoreCase(tls)) connection.setTlsAES_128_CBC(); else if ("3DES".equalsIgnoreCase(tls)) connection.setTls3DES_EDE_CBC(); else if ("NULL".equalsIgnoreCase(tls)) connection.setTlsWithoutEncyrption(); else throw new IllegalArgumentException("Unable to configure TLS encryption of type " + tls);
        }
        connection.setTcpNoDelay(true);
        return connection;
    }

    /**
    * Configure the AE settings based on the properties file values.  
    * <p>
    * The current default settings are:
    * <ul>
    * <li>PackPDV = true
    * <li>MaxOpsInvoked = 1
    * </ul>
    */
    private NetworkApplicationEntity createAE(Map<String, Object> settings) {
        String aeTitle = FilterUtil.getString(settings, AEProperties.AE_TITLE_KEY);
        NetworkApplicationEntity ae = new NetworkApplicationEntity();
        ae.setAETitle(aeTitle);
        ae.setPackPDV(true);
        ae.setMaxOpsInvoked(1);
        return ae;
    }

    private NetworkApplicationEntity createDefaultLocalAE(String aeTitle, String... sopClassUIDs) {
        Device device = new Device("XERO");
        NetworkConnection connection = new NetworkConnection();
        device.setNetworkConnection(connection);
        NetworkApplicationEntity ae = new NetworkApplicationEntity();
        ae.setAETitle(aeTitle);
        ae.setPackPDV(true);
        ae.setMaxOpsInvoked(1);
        ae.setNetworkConnection(connection);
        configureTransferCapabilities(ae, sopClassUIDs);
        return ae;
    }

    /**
    * Create a new network device that is configured from the client 
    * settings.
    */
    private Device createDevice(Map<String, Object> settings) throws IOException {
        String name = FilterUtil.getString(settings, "name", "XERO");
        Device device = new Device(name);
        String keyStoreFile = FilterUtil.getString(settings, "keystore");
        String keyStorePassword = FilterUtil.getString(settings, "keystorepw");
        String trustStoreFile = FilterUtil.getString(settings, "truststore");
        String trustStorePassword = FilterUtil.getString(settings, "truststorepw");
        String keyPassword = FilterUtil.getString(settings, "keypw");
        try {
            KeyStore keyStore = keyStoreLoader.loadKeyStore(keyStoreFile, keyStorePassword);
            KeyStore trustStore = keyStoreLoader.loadKeyStore(trustStoreFile, trustStorePassword);
            if (keyStore != null && trustStore != null) {
                log.debug("Configuring device {} with truststore and keystore", name);
                String keyOrStorePassword = keyPassword != null ? keyPassword : keyStorePassword;
                if (keyOrStorePassword == null) throw new IllegalArgumentException("Key password is undefined");
                device.initTLS(keyStore, keyOrStorePassword.toCharArray(), trustStore);
            }
        } catch (GeneralSecurityException gse) {
            throw new IOException("Unable to configure TLS for device " + name, gse);
        }
        return device;
    }

    /**
    * Parse the indicated URL and return the associated AE.
    * <p>
    * The aeTitle will be used to lookup a locally configured 
    * @param url
    * @return
    */
    public NetworkApplicationEntity getAE(URL dicomURL, String... sopClassUIDs) throws IOException {
        String aeTitle = DicomURLHandler.parseAETitle(dicomURL);
        return getAE(aeTitle, sopClassUIDs);
    }

    /**
    * Get the local AE that has been configured to connect to the indicated 
    * remote AE title.  
    * <p>
    * Local AEs 
    */
    public NetworkApplicationEntity getLocalAE(String remoteAE, String... sopClassUIDs) throws IOException {
        String localTitle = DEFAULT_LOCAL_AE_TITLE;
        Map<String, Object> p = AEProperties.getInstance().getAE(remoteAE);
        if (p != null) localTitle = FilterUtil.getString(p, AEProperties.LOCAL_TITLE);
        NetworkApplicationEntity localAE = null;
        try {
            localAE = getAE(localTitle, sopClassUIDs);
        } catch (IllegalArgumentException ex) {
            log.debug("Unable to find local AE {}, default values will be used.");
            localAE = createDefaultLocalAE(localTitle, sopClassUIDs);
        }
        localAE.setAssociationInitiator(true);
        return localAE;
    }
}
