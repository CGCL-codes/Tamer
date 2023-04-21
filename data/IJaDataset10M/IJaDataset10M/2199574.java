package com.intel.gpe.client2.portalclient;

import java.net.URL;
import javax.xml.rpc.Stub;
import org.globus.axis.gsi.GSIConstants;
import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.HostAuthorization;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.ietf.jgss.GSSCredential;
import com.intel.gpe.client2.StandaloneClient;
import com.intel.gpe.client2.defaults.IPreferences;
import com.intel.gpe.client2.providers.MessageProvider;
import com.intel.gpe.client2.security.GPESecurityManager;
import com.intel.gpe.clients.api.GridBeanClient;
import com.intel.gpe.clients.api.RegistryClient;
import com.intel.gpe.clients.api.gpe.GPETargetSystemFactoryClient;
import com.intel.gpe.clients.api.security.gss.GSSSecurityManager;
import com.intel.gpe.clients.api.virtualworkspace.GPEVirtualTargetSystemFactoryClient;
import com.intel.gpe.clients.gpe.Cache;
import com.intel.gpe.clients.gpe.DefaultProperties;
import com.intel.gpe.clients.gpe4gtk.ISecuritySetup;
import com.intel.gpe.clients.gpe4gtk.fts.byteio.RandomByteIOFileTransferImpl;
import com.intel.gpe.clients.gpe4gtk.fts.byteio.StreamableByteIOFileTransferImpl;
import com.intel.gpe.clients.gpe4gtk.fts.gridftp.GridFTPFileTransferImpl;
import com.intel.gpe.clients.gpe4gtk.jms.workflow.GTKGridFTPDataStagingSetup;
import com.intel.gpe.clients.gpe4gtk.jms.workflow.GTKRandomByteIODataStagingSetup;
import com.intel.gpe.clients.gpe4gtk.jms.workflow.GTKStreamableByteIODataStagingSetup;
import com.intel.gpe.clients.gpe4gtk.registry.RegistryClientImpl;
import com.intel.gpe.clients.gpe4gtk.tsf.TargetSystemFactoryClient;
import com.intel.gpe.clients.gtk.gbs.GBSClient;
import com.intel.gpe.globus.security.FakeHostAuthorization;
import com.intel.gui.controls2.configurable.IConfigurable;

/**
 * The portal security manager.
 * 
 * @author Ivan Serduk
 */
public class PortalSecurityManager implements ISecuritySetup, GSSSecurityManager, GPESecurityManager {

    private GSSCredential cred;

    private DefaultProperties properties;

    public PortalSecurityManager(GSSCredential cred) {
        this.cred = cred;
        properties = new DefaultProperties();
        properties.putImportTransferClient("GridFTP", GridFTPFileTransferImpl.class.getName());
        properties.putExportTransferClient("GridFTP", GridFTPFileTransferImpl.class.getName());
        properties.putDataStagingSetup("GridFTP", GTKGridFTPDataStagingSetup.class.getName());
        properties.putImportTransferClient("RandomByteIO", RandomByteIOFileTransferImpl.class.getName());
        properties.putExportTransferClient("RandomByteIO", RandomByteIOFileTransferImpl.class.getName());
        properties.putDataStagingSetup("RandomByteIO", GTKRandomByteIODataStagingSetup.class.getName());
        properties.putImportTransferClient("StreamableByteIO", StreamableByteIOFileTransferImpl.class.getName());
        properties.putExportTransferClient("StreamableByteIO", StreamableByteIOFileTransferImpl.class.getName());
        properties.putDataStagingSetup("StreamableByteIO", GTKStreamableByteIODataStagingSetup.class.getName());
    }

    public void setup(Stub stub) {
        stub._setProperty(GSIConstants.GSI_TRANSPORT, Constants.SIGNATURE);
        stub._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
        if (cred != null) {
            stub._setProperty(GSIConstants.GSI_CREDENTIALS, cred);
        }
    }

    public void setupDelegation(Stub stub) throws Exception {
        System.out.println("Setup delegation. Cred: " + cred);
        stub._setProperty(GSIConstants.GSI_TRANSPORT, Constants.SIGNATURE);
        stub._setProperty(Constants.AUTHORIZATION, HostAuthorization.getInstance());
        stub._setProperty(GSIConstants.GSI_AUTHORIZATION, new FakeHostAuthorization("host"));
        stub._setProperty(Constants.GSI_SEC_CONV, Constants.SIGNATURE);
        stub._setProperty(GSIConstants.GSI_MODE, GSIConstants.GSI_MODE_FULL_DELEG);
        GSSCredential cred = getGSSCredential();
        if (cred != null) {
            stub._setProperty(GSIConstants.GSI_CREDENTIALS, cred);
        }
    }

    public GSSCredential getGSSCredential() {
        return cred;
    }

    public void configure(MessageProvider messageProvider, IPreferences preferences) throws Exception {
    }

    public GridBeanClient getGBSClient(URL gridBeanServiceURL) {
        return new GBSClient(this, gridBeanServiceURL);
    }

    public RegistryClient getRegistryClient(String url) throws Exception {
        return new RegistryClientImpl(this, url, new Cache(), properties);
    }

    public GPETargetSystemFactoryClient getTargetSystemFactoryClient(String url) throws Exception {
        return new TargetSystemFactoryClient(this, new URL(url), new Cache(), properties);
    }

    public void init(MessageProvider messageProvider, IPreferences preferences, StandaloneClient standaloneClient, IConfigurable parent) throws Exception {
    }

    public String getIdentity() {
        return null;
    }

    public GPEVirtualTargetSystemFactoryClient getVirtualTargetSystemFactoryClient(String url) throws Exception {
        return null;
    }
}
