package au.gov.naa.digipres.dpr.model.job;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;
import au.gov.naa.digipres.dpr.core.Messages;

/**
 * Job status represents the status of a transfer job. JobStatus objects
 * are immutable.
 */
public class JobStatus implements Comparable<JobStatus> {

    private static Collection<JobStatus> jobStatuses = new Vector<JobStatus>();

    private static Collection<JobStatus> qfJobStatuses = new TreeSet<JobStatus>();

    private static Collection<JobStatus> pfJobStatuses = new TreeSet<JobStatus>();

    private static Collection<JobStatus> drJobStatuses = new TreeSet<JobStatus>();

    public static final JobStatus NULL_STATUS = new JobStatus("", 999999);

    public static final JobStatus QF_READY_FOR_PROCESSING = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReadyForProcessingInQF"), 0);

    public static final JobStatus QF_CARRIER_LOCATION_SET = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.QFCarrierLocSet"), 0);

    public static final JobStatus QF_TRANSFER_JOB_INITIALISED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.QFTransferJobInitialised"), 10);

    public static final JobStatus QF_INITIALISED_FROM_MANIFEST_PASSED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.InitialisedFromManifest"), 100);

    public static final JobStatus QF_INITIALISED_FROM_MANIFEST_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ManifestInitialisationFailed"), 101);

    public static final JobStatus QF_PRE_QUARANTINE_PASSED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PreQuarantineProcessingPassed"), 200);

    public static final JobStatus QF_PRE_QUARANTINE_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PreQuarantineProcessingFailed"), 201);

    public static final JobStatus QF_INTO_QUARANTINE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PutIntoQuarantine"), 230);

    public static final JobStatus QF_OUT_OF_QUARANTINE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.BroughtOutOfQuarantine"), 240);

    public static final JobStatus QF_POST_QUARANTINE_PASSED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PostQuarantineProcessingComplete"), 400);

    public static final JobStatus QF_POST_QUARANTINE_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PostQuarantineProcessingFailed"), 401);

    public static final JobStatus STOPPED_IN_QF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.StoppedInQF"), 999);

    public static final JobStatus REJECTED_IN_QF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.RejectedInQF"), 1000);

    public static final JobStatus RESTARTED_IN_QF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.RestartedInQF"), 1004);

    public static final JobStatus QF_PROCESSING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.QFProcessingComplete"), 9999);

    public static final JobStatus REPROCESSING_IN_QF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingInQF"), 9998);

    public static final JobStatus PF_IMPORTED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ImportedIntoPF"), 10000);

    public static final JobStatus PF_CARRIER_LOCATION_SET = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PFCarrierLocSet"), 10001);

    public static final JobStatus PF_CHECKSUM_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PFChecksumsPassed"), 10010);

    public static final JobStatus PF_CHECKSUM_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PFChecksumsFailed"), 10011);

    public static final JobStatus PF_PACKING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PackingPFPassed"), 10015);

    public static final JobStatus PF_PACKING_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PackingPFFailed"), 10016);

    public static final JobStatus PF_BINARY_NORMALISING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.BinaryNormalisingComplete"), 10020);

    public static final JobStatus PF_BINARY_NORMALISING_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.BinaryNormalisingFailed"), 10021);

    public static final JobStatus PF_NORMALISING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.NormalisingComplete"), 10030);

    public static final JobStatus PF_NORMALISING_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.NormalisingFailed"), 10031);

    public static final JobStatus PF_AIP_CHECKSUM_GENERATION_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.AIPChecksumGenComplete"), 10040);

    public static final JobStatus PF_AIP_CHECKSUM_GENERATION_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.AIPChecksumGenFailed"), 10041);

    public static final JobStatus PF_BINARY_QA_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.BinaryQAComplete"), 10050);

    public static final JobStatus PF_BINARY_QA_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.BinaryQAFailed"), 10051);

    public static final JobStatus PF_QA_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.QAComplete"), 10060);

    public static final JobStatus PF_QA_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.QAFailed"), 10061);

    public static final JobStatus PF_PROCESSING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.PFProcessingComplete"), 19999);

    public static final JobStatus STOPPED_IN_PF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.StoppedInPF"), 18999);

    public static final JobStatus RESTARTED_IN_PF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.RestartedInPF"), 19000);

    public static final JobStatus RETURNED_TO_QF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReturnedToQF"), 19001);

    public static final JobStatus REPROCESSING_IN_PF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingInPF"), 19002);

    public static final JobStatus REJECTED_IN_PF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.RejectedInPF"), 19003);

    public static final JobStatus IMPORTED_INTO_DR = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ImportedIntoDR"), 20000);

    public static final JobStatus COPY_TO_DR_PASSED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.CopyToDRPassed"), 20020);

    public static final JobStatus COPY_TO_DR_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.CopyToDRFailed"), 20021);

    public static final JobStatus STOPPED_IN_DR = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.StoppedInDR"), 21000);

    public static final JobStatus RETURNED_TO_PF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReturnedToPF"), 21001);

    public static final JobStatus RESTARTED_IN_DR = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.RestartedInDR"), 21002);

    public static final JobStatus DR_PROCESSING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.DRProcessingComplete"), 29999);

    public static final JobStatus EXPORTED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.Exported"), 50000);

    public static final JobStatus IMPORTED_AS_REPROCESSING_JOB = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingJobImported"), 30000);

    public static final JobStatus REPROCESSING_CARRIER_LOCATION_SET = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingCarrierLocSet"), 30002);

    public static final JobStatus REPROCESSING_EXPORT_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingExportComplete"), 30010);

    public static final JobStatus REPROCESSING_EXPORT_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingExportFailed"), 30011);

    public static final JobStatus REPROCESSING_PF_PACKING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingPackingComplete"), 30020);

    public static final JobStatus REPROCESSING_PF_PACKING_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingPackingFailed"), 30021);

    public static final JobStatus REPROCESSING_NORMALISATION_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingNormalisationComplete"), 30030);

    public static final JobStatus REPROCESSING_NORMALISATION_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingNormalisationFailed"), 30031);

    public static final JobStatus REPROCESSING_CHECKSUM_GENERATION_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingChecksumGenerationComplete"), 30040);

    public static final JobStatus REPROCESSING_CHECKSUM_GENERATION_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingChecksumGenerationFailed"), 30041);

    public static final JobStatus REPROCESSING_QA_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingQAComplete"), 30050);

    public static final JobStatus REPROCESSING_QA_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingQAFailed"), 30051);

    public static final JobStatus REPROCESSING_MARKED_COMPLETED_ON_PF = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingMarkedCompleted"), 30055);

    public static final JobStatus REPROCESSING_BREAK = new JobStatus(Messages.getString(JobStatus.class, "------ Reprocessing Job ------"), 29995);

    public static final JobStatus SENT_FOR_REPROCESSING = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingJobSentForReprocessing"), 29998);

    public static final JobStatus IMPORTED_REPROCESSING_JOB_INTO_DR = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ImportedReprocessingJobIntoDR"), 31000);

    public static final JobStatus REPROCESSING_COPY_TO_DR_PASSED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingCopyToDRPassed"), 31100);

    public static final JobStatus REPROCESSING_COPY_TO_DR_FAILED = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.ReprocessingCopyToDRFailed"), 31101);

    public static final JobStatus DR_REPROCESSING_COMPLETE = new JobStatus(Messages.getString(JobStatus.class, "JobStatus.DRReprocessingComplete"), 39999);

    static {
        qfJobStatuses.add(QF_READY_FOR_PROCESSING);
        qfJobStatuses.add(QF_INITIALISED_FROM_MANIFEST_PASSED);
        qfJobStatuses.add(QF_PRE_QUARANTINE_PASSED);
        qfJobStatuses.add(QF_PRE_QUARANTINE_FAILED);
        qfJobStatuses.add(QF_INTO_QUARANTINE);
        qfJobStatuses.add(QF_OUT_OF_QUARANTINE);
        qfJobStatuses.add(QF_POST_QUARANTINE_PASSED);
        qfJobStatuses.add(QF_POST_QUARANTINE_FAILED);
        qfJobStatuses.add(STOPPED_IN_QF);
        qfJobStatuses.add(REJECTED_IN_QF);
        qfJobStatuses.add(RESTARTED_IN_QF);
        qfJobStatuses.add(QF_PROCESSING_COMPLETE);
        qfJobStatuses.add(REPROCESSING_IN_QF);
        qfJobStatuses.add(EXPORTED);
        pfJobStatuses.add(PF_IMPORTED);
        pfJobStatuses.add(PF_PACKING_COMPLETE);
        pfJobStatuses.add(PF_PACKING_FAILED);
        pfJobStatuses.add(PF_CHECKSUM_COMPLETE);
        pfJobStatuses.add(PF_CHECKSUM_FAILED);
        pfJobStatuses.add(PF_BINARY_NORMALISING_COMPLETE);
        pfJobStatuses.add(PF_BINARY_NORMALISING_FAILED);
        pfJobStatuses.add(PF_NORMALISING_COMPLETE);
        pfJobStatuses.add(PF_NORMALISING_FAILED);
        pfJobStatuses.add(PF_BINARY_QA_COMPLETE);
        pfJobStatuses.add(PF_BINARY_QA_FAILED);
        pfJobStatuses.add(PF_QA_COMPLETE);
        pfJobStatuses.add(PF_QA_FAILED);
        pfJobStatuses.add(PF_AIP_CHECKSUM_GENERATION_COMPLETE);
        pfJobStatuses.add(PF_AIP_CHECKSUM_GENERATION_FAILED);
        pfJobStatuses.add(PF_PROCESSING_COMPLETE);
        pfJobStatuses.add(STOPPED_IN_PF);
        pfJobStatuses.add(RETURNED_TO_QF);
        pfJobStatuses.add(RESTARTED_IN_PF);
        pfJobStatuses.add(REPROCESSING_IN_PF);
        pfJobStatuses.add(EXPORTED);
        pfJobStatuses.add(REPROCESSING_BREAK);
        pfJobStatuses.add(IMPORTED_AS_REPROCESSING_JOB);
        pfJobStatuses.add(REPROCESSING_EXPORT_COMPLETE);
        pfJobStatuses.add(REPROCESSING_EXPORT_FAILED);
        pfJobStatuses.add(REPROCESSING_PF_PACKING_COMPLETE);
        pfJobStatuses.add(REPROCESSING_PF_PACKING_FAILED);
        pfJobStatuses.add(REPROCESSING_NORMALISATION_COMPLETE);
        pfJobStatuses.add(REPROCESSING_NORMALISATION_FAILED);
        pfJobStatuses.add(REPROCESSING_CHECKSUM_GENERATION_COMPLETE);
        pfJobStatuses.add(REPROCESSING_CHECKSUM_GENERATION_FAILED);
        pfJobStatuses.add(REPROCESSING_QA_COMPLETE);
        pfJobStatuses.add(REPROCESSING_QA_FAILED);
        drJobStatuses.add(IMPORTED_INTO_DR);
        drJobStatuses.add(COPY_TO_DR_PASSED);
        drJobStatuses.add(COPY_TO_DR_FAILED);
        drJobStatuses.add(STOPPED_IN_DR);
        drJobStatuses.add(RETURNED_TO_PF);
        drJobStatuses.add(RESTARTED_IN_DR);
        drJobStatuses.add(DR_PROCESSING_COMPLETE);
    }

    /**
	 * @return the drJobStatuses
	 */
    public static Collection<JobStatus> getDrJobStatuses() {
        return drJobStatuses;
    }

    /**
	 * @return the pfJobStatuses
	 */
    public static Collection<JobStatus> getPfJobStatuses() {
        return pfJobStatuses;
    }

    /**
	 * @return the qfJobStatuses
	 */
    public static Collection<JobStatus> getQfJobStatuses() {
        return qfJobStatuses;
    }

    private final String description;

    private final int order;

    public static JobStatus getJobStatusByName(String name) {
        for (Object element2 : jobStatuses) {
            JobStatus element = (JobStatus) element2;
            if (element.getDescription().equals(name)) {
                return element;
            }
        }
        return null;
    }

    public static JobStatus valueOf(String name) {
        return getJobStatusByName(name);
    }

    private static void addToJobStatuses(JobStatus jobStatus) {
        jobStatuses.add(jobStatus);
    }

    /**
	 * 
	 */
    protected JobStatus(String description, int order) {
        this.description = description;
        this.order = order;
        addToJobStatuses(this);
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return description;
    }

    public int compareTo(JobStatus compObj) {
        Integer myOrder = new Integer(order);
        return myOrder.compareTo(new Integer(compObj.getOrder()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobStatus)) {
            return false;
        }
        JobStatus otherJobStatus = (JobStatus) obj;
        return description.equals(otherJobStatus.getDescription()) && order == otherJobStatus.getOrder();
    }

    @Override
    public int hashCode() {
        String hashString = description + order;
        return hashString.hashCode();
    }
}
