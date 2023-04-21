package org.dcm4chee.xero.dicom;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.dcm4che2.data.UID;
import org.dcm4che2.net.Device;
import org.dcm4che2.net.ExtQueryTransferCapability;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.NetworkConnection;
import org.dcm4che2.net.TransferCapability;
import org.dcm4chee.xero.search.DicomCFindFilter;
import org.dcm4chee.xero.util.NamedThreadFactory;

/**
 * AE connections wrapper class
 * <p>
 * Moved from {@link DicomCFindFilter}
 * @author smohan
 * @author Andrew Cowan (amidx)
 */
public class AEConnection {

    private static final String[] NATIVE_LE_TS = { UID.ImplicitVRLittleEndian, UID.ExplicitVRLittleEndian };

    private Executor executor = Executors.newCachedThreadPool(new NamedThreadFactory("XERO_QUERY "));

    private Device device = new Device("XERO");

    private NetworkApplicationEntity remoteAE = new NetworkApplicationEntity();

    private NetworkConnection remoteConn = new NetworkConnection();

    private NetworkApplicationEntity ae = new NetworkApplicationEntity();

    private NetworkConnection conn = new NetworkConnection();

    private String remoteHost = null;

    private AEConnection(String[] cuids) {
        remoteAE.setInstalled(true);
        remoteAE.setAssociationAcceptor(true);
        remoteAE.setNetworkConnection(new NetworkConnection[] { remoteConn });
        device.setNetworkApplicationEntity(ae);
        device.setNetworkConnection(conn);
        ae.setNetworkConnection(conn);
        ae.setAssociationInitiator(true);
        ae.setPackPDV(true);
        conn.setTcpNoDelay(true);
        ae.setMaxOpsInvoked(1);
        configureTransferCapability(cuids);
    }

    /**
    * @param aeProps
    */
    public AEConnection(String[] cuids, AESettings settings) {
        this(cuids);
        remoteConn.setHostname(settings.getHostName());
        remoteConn.setPort(settings.getPort());
        remoteAE.setAETitle(settings.getRemoteTitle());
        ae.setAETitle(settings.getLocalTitle());
        remoteHost = settings.getHostName();
    }

    /**
    * @return <tt>Executor</tt>
    */
    public Executor getExecutor() {
        return executor;
    }

    /**
    * @return <tt>NetworkApplicationEntity</tt>
    */
    public NetworkApplicationEntity getAE() {
        return ae;
    }

    /**
    * @return <tt>NetworkConnection</tt>
    */
    public NetworkConnection getConnection() {
        return conn;
    }

    /**
    * @return <tt>NetworkApplicationEntity</tt>
    */
    public NetworkApplicationEntity getRemoteAE() {
        return remoteAE;
    }

    /**
    * @return remote host name
    */
    public String getRemoteHost() {
        return remoteHost;
    }

    protected boolean getSemanticPersonNameMatching() {
        return false;
    }

    protected boolean getRelationQR() {
        return false;
    }

    /**
    * Configure the transfer capabilities to request - based on the type of the object being requested from.
    */
    private void configureTransferCapability(String[] cuids) {
        TransferCapability[] tc = new TransferCapability[cuids.length];
        int i = 0;
        for (String cuid : cuids) {
            tc[i++] = mkFindTC(cuid, NATIVE_LE_TS);
        }
        ae.setTransferCapability(tc);
    }

    /** Make a find transfer capability object for use in requesting the transfer capabilities. */
    private TransferCapability mkFindTC(String cuid, String[] ts) {
        ExtQueryTransferCapability tc = new ExtQueryTransferCapability(cuid, ts, TransferCapability.SCU);
        tc.setExtInfoBoolean(ExtQueryTransferCapability.RELATIONAL_QUERIES, getRelationQR());
        tc.setExtInfoBoolean(ExtQueryTransferCapability.DATE_TIME_MATCHING, false);
        tc.setExtInfoBoolean(ExtQueryTransferCapability.FUZZY_SEMANTIC_PN_MATCHING, getSemanticPersonNameMatching());
        return tc;
    }
}
