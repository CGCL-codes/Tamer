package org.xebra.scp.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DimseRSP;
import org.dcm4che2.net.DimseRSPHandler;
import org.dcm4che2.net.pdu.AAssociateRJ;
import org.dcm4che2.util.UIDUtils;
import org.xebra.dcm.util.DCMUtil;
import org.xebra.dcm.util.DicomWriter;
import org.xebra.scp.InternalSender;
import org.xebra.scp.XebraConstants;
import org.xebra.scp.db.exception.PersistException;
import org.xebra.scp.db.peer.AEConnection;
import org.xebra.scp.db.peer.AEConnectionType;
import org.xebra.scp.db.peer.AEPeer;
import org.xebra.scp.db.peer.ServiceClassProvider;
import org.xebra.scp.util.FileWriter;
import org.xebra.scp.util.OutputStreamResponseHandler;
import org.xebra.scp.util.TransferSyntaxSelector;

/**
 * An execution class used to run c-find, c-move, and c-store operations.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.18 $
 */
public class DCMExec implements XebraConstants {

    private static final Logger _log = Logger.getLogger(DCMExec.class);

    public static enum QueryLevel {

        PATIENT, STUDY, SERIES
    }

    public static final int SUCCESS = 0;

    public static final int PENDING = 0xFF00;

    public static final int GENERIC_RESP = 0x8000;

    public static final OutputStream DEV_NULL = new OutputStream() {

        public void write(int b) throws IOException {
        }

        ;
    };

    private PrintStream out;

    private PrintStream err;

    private String scpHost = "localhost";

    private int scpPort;

    private InternalSender sender;

    /**
	 * Constructor.
	 * 
	 * @param out The output stream.
	 * @param err The error stream.
	 */
    public DCMExec(OutputStream out, OutputStream err) {
        setOutputStream(out);
        setErrorStream(err);
        try {
            this.scpPort = ServiceClassProvider.load().getPort();
            this.sender = new InternalSender();
        } catch (Throwable t) {
            t.printStackTrace(this.err);
        }
    }

    /**
	 * Constructor.
	 * 
	 * @param out The output stream.
	 */
    public DCMExec(OutputStream out) {
        this(out, out);
    }

    /**
	 * Sets the output stream to write to.
	 * 
	 * @param out The output stream to write to.
	 */
    public void setOutputStream(OutputStream out) {
        if (out == null) throw new NullPointerException("Output stream is null");
        if (out instanceof PrintStream) {
            this.out = (PrintStream) out;
        } else {
            this.out = new PrintStream(out);
        }
    }

    /**
	 * Gets the output stream.
	 * 
	 * @return Returns the output stream.
	 */
    public OutputStream getOutputStream() {
        return this.out;
    }

    /**
	 * Sets the error output stream to write to.
	 * 
	 * @param err The error output stream to write to.
	 */
    public void setErrorStream(OutputStream err) {
        if (err == null) throw new NullPointerException("Error stream is null");
        if (err instanceof PrintStream) {
            this.err = (PrintStream) err;
        } else {
            this.err = new PrintStream(err);
        }
    }

    /**
	 * Gets the error output stream.
	 * 
	 * @return Returns the error output stream.
	 */
    public OutputStream getErrorStream() {
        return this.err;
    }

    /**
	 * Sets the host of the SCP Service.
	 * Default is 'localhost'.
	 * 
	 * @param host The host of the scp service.
	 */
    public void setSCPHost(String host) {
        if (host == null) throw new NullPointerException();
        this.scpHost = host;
    }

    /**
	 * Gets the host of the SCP Service.
	 * Default is 'localhost'.
	 * 
	 * @return Returns the host of the SCP Service.
	 */
    public String getSCPHost() {
        return this.scpHost;
    }

    /**
	 * Sets the port of the SCP Service.
	 * 
	 * @param port The SCP Port.
	 */
    public void setSCPPort(int port) {
        this.scpPort = port;
    }

    /**
	 * Gets the port of the SCP Service.
	 * 
	 * @return Returns the SCP Port.
	 */
    public int getSCPPort() {
        return this.scpPort;
    }

    /**
	 * Writes out every known DICOM Tag and its name.
	 */
    public void dcmDictionary() {
        try {
            Field[] fields = Tag.class.getDeclaredFields();
            for (Field field : fields) {
                Object value = field.get(null);
                if (value != null) {
                    Integer tagVal = (Integer) value;
                    this.out.print(DCMUtil.getTag(tagVal));
                    this.out.print("=");
                    this.out.println(field.getName());
                }
            }
        } catch (Throwable t) {
            t.printStackTrace(this.err);
        }
    }

    /**
	 * Dumps the contents of a DCM File.
	 * 
	 * @param dcmFile The file to dump.
	 */
    public void dcmDump(File dcmFile) throws FileNotFoundException {
        if (dcmFile == null || !dcmFile.exists() || !dcmFile.isFile()) {
            throw new FileNotFoundException();
        }
        try {
            DicomInputStream dis = null;
            if (dcmFile.getName().endsWith(".gz")) {
                dis = new DicomInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(dcmFile))));
            } else {
                dis = new DicomInputStream(dcmFile);
            }
            DicomObject obj = dis.readDicomObject();
            DicomWriter.write(obj, this.getOutputStream());
        } catch (IOException exc) {
            exc.printStackTrace(this.err);
        }
    }

    /**
	 * Sends a C-FIND request.
	 * 
	 * @param aeTitle The AE Title of the requesting application.
	 * @param level The query level.
	 * @param queryParams The query parameters.
	 * 
	 * @return Returns an array of the data found.
	 */
    public DicomObject[] cfind(String aeTitle, QueryLevel level, HashMap<Integer, String> queryParams) throws DCMException {
        return cfind(aeTitle, level, buildQuery(level, queryParams));
    }

    /**
	 * Sends a C-FIND request.
	 * 
	 * @param aeTitle The AE Title of the requesting application.
	 * @param level The query level.
	 * @param queryParams The query parameters.
	 * 
	 * @return Returns an array of the data found.
	 */
    public DicomObject[] cfind(String aeTitle, QueryLevel level, DicomObject queryParams) throws DCMException {
        AEPeer peer = null;
        try {
            peer = AEPeer.loadByAETitle(aeTitle);
        } catch (Throwable t) {
        }
        if (peer == null) {
            try {
                peer = AEPeer.loadByLocalName(aeTitle);
            } catch (Throwable t) {
                t.printStackTrace(this.err);
                throw new DCMException("Could not bind to AE Peer: " + aeTitle, t);
            }
        }
        if (peer == null) {
            throw new DCMException("Could not bind to AE Peer: " + aeTitle);
        }
        String cuid = null;
        if (level == QueryLevel.PATIENT) {
            cuid = UID.PatientRootQueryRetrieveInformationModelFIND;
        } else {
            cuid = UID.StudyRootQueryRetrieveInformationModelFIND;
        }
        try {
            this.out.println("Requesting Association");
            AEConnection conn = peer.getDefaultAEConnection(AEConnectionType.C_FIND);
            if (conn == null) {
                throw new DCMException("No connection for function C-FIND");
            }
            this.out.println("Calling AE Title: " + Connector.getSystemAETitle());
            this.out.println("Called AE Title:  " + conn.getAETitle());
            this.out.println("Called Host:      " + conn.getHost());
            this.out.println("Called Port:      " + conn.getPort());
            Association init = Connector.openQueryConnection(peer);
            this.out.println("Association Accepted");
            String tsuid = TransferSyntaxSelector.selectTransferSyntax(init, cuid);
            this.out.println();
            this.out.println("Sending Query");
            this.out.println("Request:");
            this.out.println();
            this.out.println("Used Transfer Syntax: " + DCMUtil.nameOfUID(tsuid));
            DicomWriter.write(queryParams, this.out);
            this.out.println();
            OutputStreamResponseHandler handler = new OutputStreamResponseHandler(this.out, this.err);
            init.cfind(cuid, 0, queryParams, tsuid, handler);
            init.release(true);
            return handler.getData();
        } catch (DCMException exc) {
            throw exc;
        } catch (Throwable t) {
            throw new DCMException(t.getMessage(), t);
        }
    }

    /**
	 * Runs a C-Echo against the supplied AE-Title.
	 *
	 * @param aeTitle The AE Title to echo against.
	 * @param verbose Determines the verbosity of the output.
	 * @param fullTest Determines whether to test all ae titles
	 *        associated to this peer, not just the one provided.
	 *        
	 * @return Returns the dicom status.
	 */
    public int cecho(String aeTitle, boolean verbose, boolean fullTest) throws DCMException {
        AEPeer peer = null;
        try {
            peer = AEPeer.loadByAETitle(aeTitle);
        } catch (Throwable t) {
        }
        if (peer == null) {
            try {
                peer = AEPeer.loadByLocalName(aeTitle);
                aeTitle = peer.getDefaultAEConnection(AEConnectionType.C_ECHO).getAETitle();
            } catch (Throwable t) {
                throw new DCMException("Could not load the AEPeer: " + aeTitle, t);
            }
        }
        if (peer == null) {
            throw new DCMException("Could not bind to AE Peer: " + aeTitle);
        }
        String[] aeTitles = fullTest ? new String[peer.getAEConnections().size()] : new String[] { aeTitle };
        if (fullTest) {
            List<AEConnection> titles = peer.getAEConnections();
            for (int i = 0; i < aeTitles.length; i++) {
                aeTitles[i] = titles.get(i).getAETitle();
            }
        }
        int status = 0;
        for (String ae : aeTitles) {
            try {
                this.out.println("Requesting Association");
                AEConnection conn = peer.getAEConnection(ae);
                if (conn == null) {
                    throw new DCMException("No connection for AE Title: " + ae);
                }
                this.out.println("Calling AE Title: " + Connector.getSystemAETitle());
                this.out.println("Called AE Title:  " + conn.getAETitle());
                this.out.println("Called Host:      " + conn.getHost());
                this.out.println("Called Port:      " + conn.getPort());
                Association init = Connector.openVerificationConnection(peer, ae);
                this.out.println("Association Accepted");
                this.out.println();
                DimseRSP response = init.cecho(UID.VerificationSOPClass);
                init.waitForDimseRSP();
                do {
                    if (response.getCommand() != null && !response.getCommand().isEmpty()) {
                        DicomObject cmd = response.getCommand();
                        if (cmd.containsValue(Tag.Status)) {
                            status |= cmd.getInt(Tag.Status);
                        }
                        DicomWriter.write(cmd, this.out);
                    }
                    if (response.getDataset() != null && !response.getDataset().isEmpty()) {
                        DicomObject data = response.getDataset();
                        if (data.containsValue(Tag.Status)) {
                            status |= data.getInt(Tag.Status);
                        }
                        DicomWriter.write(data, this.out);
                    }
                } while (response.next());
                init.release(true);
            } catch (IOException exc) {
                _log.trace(exc.getMessage(), exc);
                if (exc.getCause() != null && exc.getCause() instanceof AAssociateRJ) {
                    return ((AAssociateRJ) exc.getCause()).getReason();
                } else if (exc.getCause() != null && exc.getCause() instanceof ConnectException) {
                    return -1;
                }
                throw new DCMException(exc.getMessage(), exc);
            } catch (DCMException exc) {
                throw exc;
            } catch (Throwable t) {
                throw new DCMException(t.getMessage(), t);
            }
        }
        return status;
    }

    /**
	 * Stores the provided image files in the given AE Title.
	 * 
	 * @param aeTitle The AE Title to move the images to.
	 * @param images The images to move.
	 * @param anonMap A map of tags and replacement values for image anonymization.
	 *        This value may be null.
	 *        
	 * @return Returns the DICOM status of the request.
	 */
    public int cstore(String aeTitle, File[] images, Map<Integer, String> anonMap) throws DCMException {
        AEPeer peer = null;
        try {
            peer = AEPeer.loadByAETitle(aeTitle);
        } catch (Throwable t) {
        }
        if (peer == null) {
            try {
                peer = AEPeer.loadByLocalName(aeTitle);
            } catch (Throwable t) {
                throw new DCMException("Could not bind to AE Peer: " + aeTitle, t);
            }
        }
        if (peer == null) {
            throw new DCMException("Could not bind to AE Peer: " + aeTitle);
        }
        try {
            this.out.println("Requesting Association");
            AEConnection conn = peer.getDefaultAEConnection(AEConnectionType.C_STORE);
            if (conn == null) {
                throw new DCMException("No connection for function C-STORE");
            }
            this.out.println("Calling AE Title: " + Connector.getSystemAETitle());
            this.out.println("Called AE Title:  " + conn.getAETitle());
            this.out.println("Called Host:      " + conn.getHost());
            this.out.println("Called Port:      " + conn.getPort());
            Association init = Connector.openStoreConnection(peer);
            this.out.println("Association Accepted");
            this.out.println();
            this.out.println("Sending Data");
            OutputStreamResponseHandler handler = new OutputStreamResponseHandler(this.out, this.err);
            for (File file : images) {
                if (!file.exists()) {
                    this.err.println("File not found: " + file.getAbsolutePath());
                }
                if (file.isDirectory()) {
                    File[] list = file.listFiles(new FilenameFilter() {

                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".dcm");
                        }
                    });
                    for (File f : list) {
                        FileWriter fw = new FileWriter(f, anonMap);
                        String tsuid = TransferSyntaxSelector.selectTransferSyntax(init, fw.getCUID(), fw.getTUID());
                        store(fw, tsuid, init, handler);
                    }
                } else {
                    FileWriter fw = new FileWriter(file, anonMap);
                    String tsuid = TransferSyntaxSelector.selectTransferSyntax(init, fw.getCUID(), fw.getTUID());
                    store(fw, tsuid, init, handler);
                }
            }
            init.release(true);
            return handler.getStatus();
        } catch (DCMException exc) {
            throw exc;
        } catch (Throwable t) {
            throw new DCMException(t.getMessage(), t);
        }
    }

    /**
	 * Moves a Study from ONE AE Peer to another.
	 * 
	 * @param fromAETitle The AE Title to push from.
	 * @param toAETitle The AE Title to push to.
	 * @param studyUID The study UID to push.
	 * @param anonMap A map of tags and replacement values for image anonymization.
	 *        This value may be null.
	 *        
	 * @return Returns the DICOM status of the request.
	 */
    public int cmove(String fromAETitle, String toAETitle, String studyUID, Map<Integer, String> anonMap) throws DCMException {
        Random rand = new Random();
        int v = rand.nextInt(0xFFFF);
        String tuid = UIDUtils.createUID() + "." + v;
        if (tuid.length() > 64) tuid = tuid.substring(0, 64);
        if (tuid.endsWith(".")) tuid = tuid.substring(0, 63);
        return cmove(fromAETitle, toAETitle, studyUID, tuid, anonMap);
    }

    /**
	 * Moves a Study from ONE AE Peer to another.
	 * 
	 * @param fromAETitle The AE Title to push from.
	 * @param toAETitle The AE Title to push to.
	 * @param studyUID The study UID to push.
	 * @param transactionUID The Transaction UID to use for this request.
	 * @param anonMap A map of tags and replacement values for image anonymization.
	 *        This value may be null.
	 *        
	 * @return Returns the DICOM status of the request.
	 */
    public int cmove(String fromAETitle, String toAETitle, String studyUID, String transactionUID, Map<Integer, String> anonMap) throws DCMException {
        if (anonMap != null) {
            sendAnonymizationRequest(studyUID, anonMap);
        }
        AEPeer peer = null;
        try {
            peer = AEPeer.loadByAETitle(fromAETitle);
        } catch (PersistException exc) {
        }
        if (peer == null) {
            try {
                peer = AEPeer.loadByLocalName(fromAETitle);
            } catch (Throwable t) {
                throw new DCMException("Could not bind to AE Peer: " + fromAETitle, t);
            }
        }
        try {
            this.out.println("Requesting Association");
            AEConnection conn = peer.getDefaultAEConnection(AEConnectionType.C_MOVE);
            if (conn == null) {
                throw new DCMException("No connection for function C-MOVE");
            }
            this.out.println("Calling AE Title: " + Connector.getSystemAETitle());
            this.out.println("Called AE Title:  " + conn.getAETitle());
            this.out.println("Move AE Title:    " + toAETitle);
            this.out.println("Called Host:      " + conn.getHost());
            this.out.println("Called Port:      " + conn.getPort());
            Association init = Connector.openMoveConnection(peer);
            String tsuid = TransferSyntaxSelector.selectTransferSyntax(init, UID.StudyRootQueryRetrieveInformationModelMOVE);
            this.out.println("Association Accepted");
            this.out.println();
            this.out.println("Sending Query");
            this.out.println("Request:");
            this.out.println();
            this.out.println("Used Transfer Syntax: " + DCMUtil.nameOfUID(tsuid));
            DicomObject data = new BasicDicomObject();
            data.putString(Tag.QueryRetrieveLevel, VR.CS, "STUDY");
            data.putString(Tag.StudyInstanceUID, VR.UI, studyUID);
            data.putString(Tag.TransactionUID, VR.UI, transactionUID);
            this.out.print("  ");
            this.out.print(DicomWriter.getElementNameString(data, Tag.StudyInstanceUID));
            this.out.println(studyUID);
            this.out.println();
            OutputStreamResponseHandler handler = new OutputStreamResponseHandler(this.out, this.err);
            init.cmove(UID.StudyRootQueryRetrieveInformationModelMOVE, 0, data, tsuid, toAETitle, handler);
            init.release(true);
            return handler.getStatus();
        } catch (Throwable t) {
            sendRemoveAnonymizationRequest(studyUID);
            throw new DCMException(t.getMessage(), t);
        }
    }

    /**
     * Sends an anonymization request.
     * @param studyUID The study UID to anonymize.
     * @param anon The anonymization attributes.
     * @return Returns true if successful.
     */
    public boolean sendAnonymizationRequest(String studyUID, Map<Integer, String> anon) {
        return this.sender.sendAddStudyAnon(studyUID, anon);
    }

    /**
     * Sends a stop anonymization request.
     * @param studyUID The study UID to stop anonymizing.
     * @return Returns true if successful.
     */
    public boolean sendRemoveAnonymizationRequest(String studyUID) {
        return this.sender.sendRemoveStudyAnon(studyUID);
    }

    private static final void store(FileWriter file, String tsuid, Association assoc, DimseRSPHandler handler) throws IOException, InterruptedException {
        assoc.cstore(file.getCUID(), file.getIUID(), 0, file, tsuid, handler);
    }

    private static DicomObject buildQuery(QueryLevel level, HashMap<Integer, String> params) {
        DicomObject data = new BasicDicomObject();
        data.putString(Tag.QueryRetrieveLevel, VR.CS, level.name());
        for (Integer key : params.keySet()) {
            String value = params.get(key);
            if (value != null) {
                data.putString(key, null, value.trim());
            } else {
                data.putNull(key, null);
            }
        }
        return data;
    }
}
