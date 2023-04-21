package org.dcm4chex.archive.mbean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.dict.Tags;
import org.dcm4chex.archive.common.PrivateTags;
import org.dcm4chex.archive.config.RetryIntervalls;
import org.dcm4chex.archive.ejb.interfaces.CheckStudyPatient;
import org.dcm4chex.archive.ejb.interfaces.CheckStudyPatientHome;
import org.dcm4chex.archive.ejb.interfaces.FileDTO;
import org.dcm4chex.archive.util.Convert;
import org.dcm4chex.archive.util.EJBHomeFactory;
import org.dcm4chex.archive.util.FileUtils;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author franz.willer@gwi-ag.com
 * @version $Revision: 2881 $ $Date: 2006-10-24 14:55:22 -0400 (Tue, 24 Oct 2006) $
 * @since 08.08.2006
 *
 */
public class CheckStudyPatientService extends ServiceMBeanSupport {

    private final SchedulerDelegate scheduler = new SchedulerDelegate(this);

    private long taskInterval = 0L;

    private Integer studyStatus;

    private Integer errorStudyStatus;

    private Integer successStudyStatus;

    private Integer checkedStudyStatus;

    private String sourceAET;

    private int limitNumberOfStudiesPerTask;

    private String issuer;

    private Integer listenerID;

    private static final DcmObjectFactory oFact = DcmObjectFactory.getInstance();

    private static final DcmParserFactory pFact = DcmParserFactory.getInstance();

    private String timerIDCheckStudyPatient;

    private final NotificationListener consistentCheckListener = new NotificationListener() {

        public void handleNotification(Notification notif, Object handback) {
            try {
                check();
            } catch (Exception e) {
                log.error("Study patient check failed!", e);
            }
        }
    };

    public ObjectName getSchedulerServiceName() {
        return scheduler.getSchedulerServiceName();
    }

    public void setSchedulerServiceName(ObjectName schedulerServiceName) {
        scheduler.setSchedulerServiceName(schedulerServiceName);
    }

    public final String getTaskInterval() {
        return RetryIntervalls.formatIntervalZeroAsNever(taskInterval);
    }

    public void setTaskInterval(String interval) throws Exception {
        long oldInterval = taskInterval;
        taskInterval = RetryIntervalls.parseIntervalOrNever(interval);
        if (getState() == STARTED && oldInterval != taskInterval) {
            scheduler.stopScheduler(timerIDCheckStudyPatient, listenerID, consistentCheckListener);
            listenerID = scheduler.startScheduler(timerIDCheckStudyPatient, taskInterval, consistentCheckListener);
        }
    }

    /**
	 * @return Returns the sourceAET.
	 */
    public String getSourceAET() {
        return sourceAET == null ? "*" : sourceAET;
    }

    /**
	 * @param sourceAET The sourceAET to set.
	 */
    public void setSourceAET(String sourceAET) {
        this.sourceAET = "*".equals(sourceAET) ? null : sourceAET;
    }

    /**
	 * @return Returns the studyStatus.
	 */
    public String getStudyStatus() {
        return studyStatus == null ? "*" : String.valueOf(studyStatus.intValue());
    }

    /**
	 * @param studyStatus The studyStatus to set.
	 */
    public void setStudyStatus(String studyStatus) {
        this.studyStatus = "*".equals(studyStatus) ? null : new Integer(studyStatus);
    }

    /**
	 * @return Returns the errorStudyStatus.
	 */
    public String getErrorStudyStatus() {
        return errorStudyStatus == null ? "NONE" : String.valueOf(errorStudyStatus.intValue());
    }

    /**
	 * @param errorStudyStatus The errorStudyStatus to set.
	 */
    public void setErrorStudyStatus(String errorStudyStatus) {
        this.errorStudyStatus = "NONE".equalsIgnoreCase(errorStudyStatus) ? null : new Integer(errorStudyStatus);
    }

    /**
	 * @return Returns the successStudyStatus.
	 */
    public String getSuccessStudyStatus() {
        return successStudyStatus == null ? "NONE" : String.valueOf(successStudyStatus.intValue());
    }

    /**
	 * @param successStudyStatus The successStudyStatus to set.
	 */
    public void setSuccessStudyStatus(String successStudyStatus) {
        this.successStudyStatus = "NONE".equalsIgnoreCase(successStudyStatus) ? null : new Integer(successStudyStatus);
    }

    /**
	 * @return Returns the checkedStudyStatus.
	 */
    public String getCheckedStudyStatus() {
        return checkedStudyStatus == null ? "NONE" : String.valueOf(checkedStudyStatus.intValue());
    }

    /**
	 * @param checkedStudyStatus The checkedStudyStatus to set.
	 */
    public void setCheckedStudyStatus(String checkedStudyStatus) {
        this.checkedStudyStatus = "NONE".equalsIgnoreCase(checkedStudyStatus) ? null : new Integer(checkedStudyStatus);
    }

    /**
	 * @return Returns the issuer.
	 */
    public String getIssuer() {
        return issuer;
    }

    /**
	 * @param issuer The issuer to set.
	 */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public int getLimitNumberOfStudiesPerTask() {
        return limitNumberOfStudiesPerTask;
    }

    public void setLimitNumberOfStudiesPerTask(int limit) {
        this.limitNumberOfStudiesPerTask = limit;
    }

    public int findStudiesWithPatientCoercion() throws RemoteException, FinderException, CreateException {
        return check(false)[1];
    }

    public String check() throws RemoteException, FinderException, CreateException {
        int[] ia = check(true);
        if (ia[0] == 0) return "Nothing to do!";
        return ia[0] + "studies checked!" + ia[1] + " studies moved to new patient(s)!";
    }

    private int[] check(boolean update) throws RemoteException, FinderException, CreateException {
        int updated = 0;
        long l = System.currentTimeMillis();
        CheckStudyPatient checker = newConsistencyCheck();
        Collection col = checker.findStudiesForTest(studyStatus, sourceAET, limitNumberOfStudiesPerTask);
        if (col.isEmpty()) return new int[] { 0, 0 };
        Object[] oa;
        int i = 0;
        Dataset ds, dsDB;
        Dataset[] dsa;
        long studyPk;
        for (Iterator iter = col.iterator(); iter.hasNext(); ) {
            oa = (Object[]) iter.next();
            dsa = checkPatInfo(dsDB = (Dataset) oa[0], (FileDTO) oa[1]);
            ByteBuffer bb = dsDB.getByteBuffer(PrivateTags.StudyPk);
            studyPk = Convert.toLong(bb.array());
            if (dsa != null) {
                if (dsa[1] != null) {
                    ds = dsa[1];
                    i++;
                    ds.putLO(Tags.IssuerOfPatientID, issuer);
                    ds = checker.moveStudyToNewPatient(ds, studyPk);
                    if (successStudyStatus != null) checker.updateStudyStatus(studyPk, successStudyStatus);
                    log.info("PatientInfo of file and DB differs! A new patient was created and study (pk=" + studyPk + ") moved! new patient:");
                    log.info(ds);
                } else {
                    if (checkedStudyStatus != null) checker.updateStudyStatus(studyPk, checkedStudyStatus);
                }
            } else {
                if (errorStudyStatus != null) checker.updateStudyStatus(studyPk, errorStudyStatus);
            }
        }
        return new int[] { col.size(), i };
    }

    /**
	 * @param dataset
	 * @param fileDTO
	 */
    private Dataset[] checkPatInfo(Dataset dsDB, FileDTO fileDTO) {
        log.info("check Patientinfo for " + dsDB.getString(Tags.PatientID) + " with file " + fileDTO);
        String fsPath = fileDTO.getDirectoryPath();
        String filePath = fileDTO.getFilePath();
        File f = FileUtils.toFile(fsPath, filePath);
        InputStream in = null;
        DcmParser parser = null;
        Dataset dsFile = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            parser = pFact.newDcmParser(in);
            FileFormat format = parser.detectFileFormat();
            if (format != null) {
                dsFile = oFact.newDataset();
                parser.setDcmHandler(dsFile.getDcmHandler());
                parser.parseDcmFile(format, Tags.PixelData);
            }
            return comparePatInfo(dsDB, dsFile) ? new Dataset[] { dsDB, null } : new Dataset[] { dsDB, dsFile };
        } catch (IOException x) {
            log.error("Check patient Info with data file failed! ", x);
            return null;
        }
    }

    /**
	 * @param dsDB
	 * @param ds
	 * @return
	 */
    private boolean comparePatInfo(Dataset dsDB, Dataset dsFile) {
        if (!compareTag(dsDB, dsFile, Tags.PatientID)) return false;
        if (!compareTag(dsDB, dsFile, Tags.IssuerOfPatientID)) {
            if (!issuer.equals(dsDB.getString(Tags.IssuerOfPatientID))) return false;
        }
        if (!compareTag(dsDB, dsFile, Tags.PatientName)) return false;
        if (!compareTag(dsDB, dsFile, Tags.PatientSex)) return false;
        if (!compareTag(dsDB, dsFile, Tags.PatientBirthDate)) return false;
        return true;
    }

    private boolean compareTag(Dataset ds, Dataset ds1, int tag) {
        String s = ds.getString(tag);
        if (s != null) {
            return s.equals(ds1.getString(tag));
        } else {
            return ds1.getString(tag) == null;
        }
    }

    protected void startService() throws Exception {
        listenerID = scheduler.startScheduler(timerIDCheckStudyPatient, taskInterval, consistentCheckListener);
    }

    protected void stopService() throws Exception {
        scheduler.stopScheduler(timerIDCheckStudyPatient, listenerID, consistentCheckListener);
        super.stopService();
    }

    private CheckStudyPatient newConsistencyCheck() {
        try {
            CheckStudyPatientHome home = (CheckStudyPatientHome) EJBHomeFactory.getFactory().lookup(CheckStudyPatientHome.class, CheckStudyPatientHome.JNDI_NAME);
            return home.create();
        } catch (Exception e) {
            throw new RuntimeException("Failed to access CheckStudyPatient EJB:", e);
        }
    }

    public String getTimerIDCheckStudyPatient() {
        return timerIDCheckStudyPatient;
    }

    public void setTimerIDCheckStudyPatient(String timerIDCheckStudyPatient) {
        this.timerIDCheckStudyPatient = timerIDCheckStudyPatient;
    }
}
