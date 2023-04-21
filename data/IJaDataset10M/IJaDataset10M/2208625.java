package org.dcm4chex.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.ejb.CreateException;
import org.dcm4che.data.Command;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmDecodeParam;
import org.dcm4che.data.DcmElement;
import org.dcm4che.data.DcmEncodeParam;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileMetaInfo;
import org.dcm4che.dict.Status;
import org.dcm4che.dict.Tags;
import org.dcm4che.net.ActiveAssociation;
import org.dcm4che.net.Association;
import org.dcm4che.net.AssociationFactory;
import org.dcm4che.net.AssociationListener;
import org.dcm4che.net.DcmServiceBase;
import org.dcm4che.net.DcmServiceException;
import org.dcm4che.net.Dimse;
import org.dcm4che.net.PDU;
import org.dcm4cheri.util.StringUtils;
import org.dcm4chex.archive.ejb.interfaces.MoveOrderQueue;
import org.dcm4chex.archive.ejb.interfaces.MoveOrderQueueHome;
import org.dcm4chex.archive.ejb.interfaces.MoveOrderValue;
import org.dcm4chex.archive.ejb.interfaces.Storage;
import org.dcm4chex.archive.ejb.interfaces.StorageHome;
import org.dcm4chex.archive.util.EJBHomeFactory;
import org.dcm4chex.archive.util.HomeFactoryException;
import org.jboss.system.server.ServerConfigLocator;

/**
 * @author Gunter.Zeilinger@tiani.com
 * @version $Revision: 1025 $
 * @since 03.08.2003
 */
public class StoreScp extends DcmServiceBase implements AssociationListener {

    private static final String STORESCP = "org.dcm4chex.service.StoreScp";

    private static final int[] TYPE1_ATTR = { Tags.StudyInstanceUID, Tags.SeriesInstanceUID, Tags.SOPInstanceUID, Tags.SOPClassUID };

    private static final AssociationFactory af = AssociationFactory.getInstance();

    private static final DcmObjectFactory dof = DcmObjectFactory.getInstance();

    private static final DcmParserFactory pf = DcmParserFactory.getInstance();

    private final StoreScpService service;

    private int updateDatabaseMaxRetries = 2;

    private int forwardPriority = 0;

    private ForwardAETs forwardAETs = new ForwardAETs();

    private String storageDirs;

    private File[] storageDirFiles;

    private String maskWarningAsSuccessForCallingAETs = "";

    private HashSet warningAsSuccessSet = new HashSet();

    public StoreScp(StoreScpService service) {
        this.service = service;
    }

    public final String getMaskWarningAsSuccessForCallingAETs() {
        return maskWarningAsSuccessForCallingAETs;
    }

    public final void setMaskWarningAsSuccessForCallingAETs(String aets) {
        maskWarningAsSuccessForCallingAETs = aets;
        warningAsSuccessSet.clear();
        if (aets != null && aets.trim().length() != 0) {
            warningAsSuccessSet.addAll(Arrays.asList(StringUtils.split(aets.trim(), '\\')));
        }
    }

    public final String getForwardAETs() {
        ForwardAETsEditor fce = new ForwardAETsEditor();
        fce.setValue(forwardAETs);
        return fce.getAsText();
    }

    public final void setForwardAETs(String aets) {
        ForwardAETsEditor fce = new ForwardAETsEditor();
        fce.setAsText(aets);
        this.forwardAETs = (ForwardAETs) fce.getValue();
    }

    public final int getForwardPriority() {
        return forwardPriority;
    }

    public final void setForwardPriority(int forwardPriority) {
        this.forwardPriority = forwardPriority;
    }

    public final String getStorageDirs() {
        return storageDirs;
    }

    public final void setStorageDirs(String dirs) throws IOException {
        if (dirs == null) {
            throw new IllegalArgumentException();
        }
        StringTokenizer stok = new StringTokenizer(dirs, "\r\n\t ");
        File[] tmp = new File[stok.countTokens()];
        if (tmp.length == 0) {
            throw new IllegalArgumentException(dirs);
        }
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = checkStorageDir(stok.nextToken());
        }
        this.storageDirs = dirs;
        this.storageDirFiles = tmp;
    }

    private File checkStorageDir(String dir) throws IOException {
        File f = new File(dir);
        if (!f.isAbsolute()) {
            f = new File(ServerConfigLocator.locate().getServerHomeDir(), dir);
        }
        if (!f.exists()) {
            service.getLog().warn("directory " + dir + " does not exist - create new one");
            if (!f.mkdirs()) {
                String prompt = "Failed to create directory " + dir;
                service.getLog().error(prompt);
                throw new IOException(prompt);
            }
        }
        if (!f.isDirectory() || !f.canWrite()) {
            String prompt = dir + " is not a writeable directory";
            service.getLog().error(prompt);
            throw new IOException(prompt);
        }
        return f;
    }

    public final int getUpdateDatabaseMaxRetries() {
        return updateDatabaseMaxRetries;
    }

    public final void setUpdateDatabaseMaxRetries(int updateDatabaseMaxRetries) {
        this.updateDatabaseMaxRetries = updateDatabaseMaxRetries;
    }

    void checkReadyToStart() {
        if (storageDirFiles == null || storageDirFiles.length == 0) {
            throw new IllegalStateException("No Storage Directory configured!");
        }
        if (service.getRetrieveAETs() == null) {
            throw new IllegalStateException("No Retrieve AET configured!");
        }
    }

    protected void doCStore(ActiveAssociation activeAssoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
        Command rqCmd = rq.getCommand();
        InputStream in = rq.getDataAsStream();
        Association assoc = activeAssoc.getAssociation();
        File file = null;
        try {
            DcmDecodeParam decParam = DcmDecodeParam.valueOf(rq.getTransferSyntaxUID());
            Dataset ds = objFact.newDataset();
            DcmParser parser = pf.newDcmParser(in);
            parser.setDcmHandler(ds.getDcmHandler());
            parser.parseDataset(decParam, Tags.PixelData);
            service.logDataset("Dataset:\n", ds);
            ds.setFileMetaInfo(objFact.newFileMetaInfo(rqCmd.getAffectedSOPClassUID(), rqCmd.getAffectedSOPInstanceUID(), rq.getTransferSyntaxUID()));
            checkDataset(ds);
            Calendar today = Calendar.getInstance();
            File dir = storageDirFiles[today.get(Calendar.DAY_OF_MONTH) % storageDirFiles.length];
            file = makeFile(dir, today, ds);
            MessageDigest md = MessageDigest.getInstance("MD5");
            storeToFile(parser, ds, file, (DcmEncodeParam) decParam, md);
            final String dirPath = dir.getCanonicalPath().replace(File.separatorChar, '/');
            final String filePath = file.getCanonicalPath().replace(File.separatorChar, '/').substring(dirPath.length() + 1);
            Dataset coercedElements = updateDB(assoc, ds, dirPath, filePath, (int) file.length(), md.digest());
            if (coercedElements.isEmpty() || warningAsSuccessSet.contains(assoc.getCallingAET())) {
                rspCmd.putUS(Tags.Status, Status.Success);
            } else {
                int[] coercedTags = new int[coercedElements.size()];
                Iterator it = coercedElements.iterator();
                for (int i = 0; i < coercedTags.length; i++) {
                    coercedTags[i] = ((DcmElement) it.next()).tag();
                }
                rspCmd.putAT(Tags.OffendingElement, coercedTags);
                rspCmd.putUS(Tags.Status, Status.CoercionOfDataElements);
                ds.putAll(coercedElements);
            }
            updateStoredStudiesInfo(assoc, ds);
        } catch (DcmServiceException e) {
            service.getLog().warn(e.getMessage(), e);
            deleteFailedStorage(file);
            throw e;
        } catch (Exception e) {
            service.getLog().error(e.getMessage(), e);
            deleteFailedStorage(file);
            throw new DcmServiceException(Status.ProcessingFailure, e);
        } finally {
            in.close();
        }
    }

    private void deleteFailedStorage(File file) {
        if (file == null) return;
        service.getLog().info("M-DELETE file:" + file);
        file.delete();
    }

    private Dataset updateDB(Association assoc, Dataset ds, String dirPath, String filePath, int fileLength, byte[] md5) throws DcmServiceException, RemoteException, CreateException, HomeFactoryException {
        Storage storage = getStorageHome().create();
        try {
            int retry = 0;
            for (; ; ) {
                try {
                    return storage.store(assoc.getCallingAET(), assoc.getCalledAET(), ds, service.getRetrieveAETs(), dirPath, filePath, fileLength, md5);
                } catch (Exception e) {
                    if (retry++ >= updateDatabaseMaxRetries) {
                        service.getLog().error("failed to update DB with entries for received " + dirPath + "/" + filePath, e);
                        throw new DcmServiceException(Status.ProcessingFailure, e);
                    }
                    service.getLog().warn("failed to update DB with entries for received " + dirPath + "/" + filePath + " - retry", e);
                }
            }
        } finally {
            try {
                storage.remove();
            } catch (Exception ignore) {
            }
        }
    }

    private File makeFile(File basedir, Calendar today, Dataset ds) throws IOException {
        File dir = new File(basedir, String.valueOf(today.get(Calendar.YEAR)) + File.separator + toDec(today.get(Calendar.MONTH) + 1) + File.separator + toDec(today.get(Calendar.DAY_OF_MONTH)) + File.separator + toHex(ds.getString(Tags.StudyInstanceUID).hashCode()) + File.separator + toHex(ds.getString(Tags.SeriesInstanceUID).hashCode()));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (int hash = ds.getString(Tags.SOPInstanceUID).hashCode(); ; ++hash) {
            File f = new File(dir, toHex(hash));
            if (f.createNewFile()) {
                return f;
            }
        }
    }

    private File toFile(String basedir, String[] fileIDs) {
        File dir = new File(basedir, fileIDs[0] + File.separatorChar + fileIDs[1] + File.separatorChar + fileIDs[2] + File.separatorChar + fileIDs[3] + File.separatorChar + fileIDs[4]);
        File file;
        while ((file = new File(dir, fileIDs[5])).exists()) {
            fileIDs[5] = toHex((int) Long.parseLong(fileIDs[5], 16) + 1);
        }
        return file;
    }

    private StorageHome getStorageHome() throws HomeFactoryException {
        return (StorageHome) EJBHomeFactory.getFactory().lookup(StorageHome.class, StorageHome.JNDI_NAME);
    }

    private MoveOrderQueueHome getMoveOrderQueueHome() throws HomeFactoryException {
        return (MoveOrderQueueHome) EJBHomeFactory.getFactory().lookup(MoveOrderQueueHome.class, MoveOrderQueueHome.JNDI_NAME);
    }

    private void storeToFile(DcmParser parser, Dataset ds, File file, DcmEncodeParam encParam, MessageDigest md) throws IOException {
        service.getLog().info("M-WRITE file:" + file);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        DigestOutputStream dos = new DigestOutputStream(out, md);
        try {
            ds.writeFile(dos, encParam);
            if (parser.getReadTag() == Tags.PixelData) {
                ds.writeHeader(dos, encParam, parser.getReadTag(), parser.getReadVR(), parser.getReadLength());
                copy(parser.getInputStream(), dos);
            }
        } finally {
            try {
                dos.close();
            } catch (IOException ignore) {
            }
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[512];
        int c;
        while ((c = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, c);
        }
    }

    private void checkDataset(Dataset ds) throws DcmServiceException {
        for (int i = 0; i < TYPE1_ATTR.length; ++i) {
            if (ds.vm(TYPE1_ATTR[i]) <= 0) {
                throw new DcmServiceException(Status.DataSetDoesNotMatchSOPClassError, "Missing Type 1 Attribute " + Tags.toString(TYPE1_ATTR[i]));
            }
            FileMetaInfo fmi = ds.getFileMetaInfo();
            if (!fmi.getMediaStorageSOPInstanceUID().equals(ds.getString(Tags.SOPInstanceUID))) {
                throw new DcmServiceException(Status.DataSetDoesNotMatchSOPClassError, "SOP Instance UID in Dataset differs from Affected SOP Instance UID");
            }
            if (!fmi.getMediaStorageSOPClassUID().equals(ds.getString(Tags.SOPClassUID))) {
                throw new DcmServiceException(Status.DataSetDoesNotMatchSOPClassError, "SOP Class UID in Dataset differs from Affected SOP Class UID");
            }
        }
    }

    private static char[] HEX_DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private String toHex(int val) {
        char[] ch8 = new char[8];
        for (int i = 8; --i >= 0; val >>= 4) {
            ch8[i] = HEX_DIGIT[val & 0xf];
        }
        return String.valueOf(ch8);
    }

    private String toDec(int val) {
        return String.valueOf(new char[] { HEX_DIGIT[val / 10], HEX_DIGIT[val % 10] });
    }

    private void mkdir(File dir) {
        if (dir.mkdir()) {
            service.getLog().info("M-WRITE dir:" + dir);
        }
    }

    public void write(Association src, PDU pdu) {
    }

    public void received(Association src, PDU pdu) {
    }

    public void write(Association src, Dimse dimse) {
    }

    public void received(Association src, Dimse dimse) {
    }

    public void error(Association src, IOException ioe) {
    }

    public void close(Association assoc) {
        Map storedStudiesInfo = (Map) assoc.getProperty(STORESCP);
        if (storedStudiesInfo != null) {
            forward(forwardAETs.get(assoc.getCallingAET()), storedStudiesInfo.values().iterator());
        }
    }

    private void updateStoredStudiesInfo(Association assoc, Dataset ds) {
        Map storedStudiesInfo = (Map) assoc.getProperty(STORESCP);
        if (storedStudiesInfo == null) {
            assoc.putProperty(STORESCP, storedStudiesInfo = new HashMap());
        }
        Dataset refSOP = getRefImageSeq(ds, getRefSeriesSeq(ds, storedStudiesInfo)).addNewItem();
        refSOP.putUI(Tags.RefSOPClassUID, ds.getString(Tags.SOPClassUID));
        refSOP.putUI(Tags.RefSOPInstanceUID, ds.getString(Tags.SOPInstanceUID));
    }

    private DcmElement getRefSeriesSeq(Dataset ds, Map storedStudiesInfo) {
        final String siud = ds.getString(Tags.StudyInstanceUID);
        Dataset info = (Dataset) storedStudiesInfo.get(siud);
        if (info != null) {
            return info.get(Tags.RefSeriesSeq);
        }
        storedStudiesInfo.put(siud, info = dof.newDataset());
        info.putLO(Tags.PatientID, ds.getString(Tags.PatientID));
        info.putPN(Tags.PatientName, ds.getString(Tags.PatientName));
        info.putSH(Tags.StudyID, ds.getString(Tags.StudyID));
        info.putUI(Tags.StudyInstanceUID, siud);
        return info.putSQ(Tags.RefSeriesSeq);
    }

    private DcmElement getRefImageSeq(Dataset ds, DcmElement seriesSq) {
        final String siud = ds.getString(Tags.SeriesInstanceUID);
        Dataset info;
        for (int i = 0, n = seriesSq.vm(); i < n; ++i) {
            info = seriesSq.getItem(i);
            if (siud.equals(info.getString(Tags.SeriesInstanceUID))) {
                return info.get(Tags.RefImageSeq);
            }
        }
        info = seriesSq.addNewItem();
        info.putUI(Tags.SeriesInstanceUID, siud);
        return info.putSQ(Tags.RefImageSeq);
    }

    private static String firstOf(String s, char delim) {
        int delimPos = s.indexOf(delim);
        return delimPos == -1 ? s : s.substring(0, delimPos);
    }

    private void forward(String[] destAETs, Iterator scns) {
        if (destAETs.length == 0) {
            return;
        }
        MoveOrderQueue orderQueue;
        try {
            orderQueue = getMoveOrderQueueHome().create();
        } catch (Exception e) {
            service.getLog().error("Failed to access Move Order Queue", e);
            return;
        }
        final MoveOrderValue order = new MoveOrderValue();
        order.setScheduledTime(new Date());
        order.setRetrieveAET(firstOf(service.getRetrieveAETs(), '\\'));
        order.setPriority(forwardPriority);
        while (scns.hasNext()) {
            Dataset scn = (Dataset) scns.next();
            DcmElement refSeriesSeq = scn.get(Tags.RefSeriesSeq);
            order.setStudyIuids(scn.getString(Tags.StudyInstanceUID));
            for (int i = 0, n = refSeriesSeq.vm(); i < n; ++i) {
                Dataset refSeries = refSeriesSeq.getItem(i);
                DcmElement refSOPSeq = refSeries.get(Tags.RefImageSeq);
                StringBuffer sopIUIDs = new StringBuffer();
                for (int j = 0, m = refSOPSeq.vm(); j < m; ++j) {
                    if (j != 0) {
                        sopIUIDs.append('\\');
                    }
                    sopIUIDs.append(refSOPSeq.getItem(j).getString(Tags.RefSOPInstanceUID));
                }
                order.setSeriesIuids(refSeries.getString(Tags.SeriesInstanceUID));
                order.setSopIuids(sopIUIDs.toString());
                for (int k = 0; k < destAETs.length; ++k) {
                    order.setMoveDestination(destAETs[k]);
                    try {
                        orderQueue.queue(order);
                    } catch (RemoteException e) {
                        service.getLog().error("Failed to queue " + order, e);
                    }
                }
            }
        }
    }
}
