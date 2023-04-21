package au.gov.naa.digipres.dpr.ui.core;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import au.gov.naa.digipres.dpr.core.DPRClient;
import au.gov.naa.digipres.dpr.core.importexport.ImportExportListener;
import au.gov.naa.digipres.dpr.core.importexport.ReprocessingJobImporter;
import au.gov.naa.digipres.dpr.core.importexport.TransferJobImporter;
import au.gov.naa.digipres.dpr.core.importexport.TransferJobImporter.ImportException;
import au.gov.naa.digipres.dpr.model.facility.Facility;
import au.gov.naa.digipres.dpr.model.job.JobNumber;
import au.gov.naa.digipres.dpr.model.job.JobStatus;
import au.gov.naa.digipres.dpr.model.reprocessingjob.ReprocessingJob;
import au.gov.naa.digipres.dpr.model.transferjob.TransferJob;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.JobProcessingTask;
import au.gov.naa.digipres.dpr.task.ReprocessingJobProcessingTask;
import au.gov.naa.digipres.dpr.task.RetrieveTransferJobSummariesTask;
import au.gov.naa.digipres.dpr.task.StopJobTask;
import au.gov.naa.digipres.dpr.task.TransferJobProcessingTask;
import au.gov.naa.digipres.dpr.task.Tasks.AccessRestrictedException;
import au.gov.naa.digipres.dpr.ui.task.StopJobDialog;
import au.gov.naa.digipres.dpr.util.ActionProvider;
import au.gov.naa.digipres.dpr.util.AppActions;
import au.gov.naa.digipres.dpr.util.AppDefaults;
import au.gov.naa.digipres.dpr.util.Command;
import au.gov.naa.digipres.dpr.util.SwingWorker;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 */
public class JobSelectorActions extends AppActions {

    private enum Db4oJobType {

        TRANSFER_JOB, REPROCESSING_JOB
    }

    public static final String CREATE_NEW_TRANSFER_JOB_COMMAND = "createnewtransferjob";

    public static final String PROCESS_JOB_COMMAND = "processjob";

    public static final String STOP_JOB_COMMAND = "stopjob";

    public static final String IMPORT_JOB_COMMAND = "importjob";

    public static final String SHOW_DR_TASKS_COMMAND = "showdrtasks";

    public static final String ADMINISTER_TRANSFER_JOB_COMMAND = "admintransferjob";

    public static final String VIEW_DATA = "viewdata";

    private AppDefaults appDefaults;

    private final JobSelectorPanel selector;

    private Facility currentFacility;

    private User loggedInUser;

    private boolean isReprocessingJob;

    private DPRClient dprClient;

    private RetrieveTransferJobSummariesTask jobSummariesTask;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
	 * Create and initialise the actions for the job jobSelector.
	 * @param jobSelector
	 * @param transferJobSummaries
	 * @param transferJobNumber
	 */
    public JobSelectorActions(JobSelectorPanel selector, DPRClient dprClient, User user, Facility currentFacility) {
        appDefaults = new AppDefaults();
        appDefaults.addResourceBundle("actions.JobSelector");
        this.selector = selector;
        try {
            jobSummariesTask = (RetrieveTransferJobSummariesTask) dprClient.getTaskByName(user, RetrieveTransferJobSummariesTask.TASK_NAME);
        } catch (AccessRestrictedException e) {
            ExceptionDialog.showAccessRestrictedDialog(selector, e.getRequiredPermission());
            return;
        }
        this.currentFacility = currentFacility;
        this.dprClient = dprClient;
        loggedInUser = user;
        boolean canProcessJobs = true;
        boolean canAdministerJobs = true;
        Command newTransferJobCommand = addCommand(CREATE_NEW_TRANSFER_JOB_COMMAND, new Command(CREATE_NEW_TRANSFER_JOB_COMMAND, new CreateNewTransferJobActionProvider(selector), appDefaults));
        newTransferJobCommand.setEnabled(canProcessJobs);
        Command processJobCommand = addCommand(PROCESS_JOB_COMMAND, new Command(PROCESS_JOB_COMMAND, new ProcessJobActionProvider(selector), appDefaults));
        processJobCommand.setEnabled(canProcessJobs);
        Command importJobCommand = addCommand(IMPORT_JOB_COMMAND, new Command(IMPORT_JOB_COMMAND, new ImportJobActionProvider(selector), appDefaults));
        importJobCommand.setEnabled(canProcessJobs);
        Command adminJobCommand = addCommand(ADMINISTER_TRANSFER_JOB_COMMAND, new Command(ADMINISTER_TRANSFER_JOB_COMMAND, new AdminTranfserJobActionProvider(selector), appDefaults));
        adminJobCommand.setEnabled(canAdministerJobs);
        Command viewJobCommand = addCommand(VIEW_DATA, new Command(VIEW_DATA, new ViewJobActionProvider(selector), appDefaults));
        viewJobCommand.setEnabled(canProcessJobs);
        Command drTasksCommand = addCommand(SHOW_DR_TASKS_COMMAND, new Command(SHOW_DR_TASKS_COMMAND, new ShowDRTasksActionProvider(selector.getMainFrame()), appDefaults));
        drTasksCommand.setEnabled(canAdministerJobs);
        Command stopJobCommand = addCommand(STOP_JOB_COMMAND, new Command(STOP_JOB_COMMAND, new StopJobActionProvider(selector), appDefaults));
        stopJobCommand.setEnabled(canProcessJobs);
    }

    public void refresh() {
        refresh(null, null, false);
    }

    public List<User> getUserList() {
        return jobSummariesTask.getUserList();
    }

    /**
	 * Refresh the job jobSelector screen.
	 * @param filterOutExported A flag used to filter out exported jobs.
	 */
    public void refresh(final JobStatus status, final User user, final boolean filterOutExported) {
        selector.tableModel.setSummaries(jobSummariesTask.retrieveSummariesByStatusAndUser(status, user, filterOutExported));
    }

    /**
	 * Create a new transferjob, on Quarantine, from scratch.
	 * 
	 * This Action basically just displays the "create new transfer job task"
	 */
    public class CreateNewTransferJobActionProvider implements ActionProvider {

        private JobSelectorPanel newTJselector;

        public CreateNewTransferJobActionProvider(JobSelectorPanel selector) {
            newTJselector = selector;
        }

        public void doAction(ActionEvent ae) {
            JobProcessingTask jobTask;
            try {
                jobTask = (JobProcessingTask) dprClient.getTaskByName(loggedInUser, TransferJobProcessingTask.TASK_NAME);
            } catch (AccessRestrictedException e1) {
                ExceptionDialog.showAccessRestrictedDialog(newTJselector.getMainFrame(), e1.getRequiredPermission());
                return;
            }
            try {
                newTJselector.getMainFrame().showJobProcessingTask(jobTask, null, null);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An exception occurred when attempting to create a new transfer job.", e);
                ExceptionDialog.showExceptionDialog(newTJselector.getMainFrame(), e, "Problem Creating New Transfer Job", "An exception occurred when attempting to create a new transfer job.");
            }
            newTJselector.getMainFrame().setProcessingState(true);
        }
    }

    /**
	 * Process the selected job in the transfer job table.
	 * 
	 * This will display the appropriate task, based on what type of
	 * job is currently selected - process tranfser job for transfer jobs, 
	 * process reprocessing job for reprocessing jobs, etc.
	 */
    public class ProcessJobActionProvider implements ActionProvider {

        JobSelectorPanel jobSelector;

        public ProcessJobActionProvider(JobSelectorPanel jobSelector) {
            this.jobSelector = jobSelector;
        }

        public void doAction(ActionEvent ae) {
            int selectedIndex = jobSelector.jobsTable.getSelectedRow();
            if (selectedIndex != -1) {
                final JobNumber selectedJobNumber = (JobNumber) jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.JOB_NUMBER_COLUMN);
                isReprocessingJob = false;
                if (currentFacility.getId() != Facility.QUARANTINE_FACILITY_ID) {
                    isReprocessingJob = Boolean.TRUE.equals(jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.IS_REPROCESSINGJOB_COLUMN));
                }
                if (isReprocessingJob) {
                    if (JobStatus.SENT_FOR_REPROCESSING.equals(jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.STATUS_COLUMN))) {
                        String[] messageArr = { "This reprocessing job has been sent to PF, and cannot currently be processed on DR.", "To process this job you must import the job once it has finished on PF.", "The job may also be deleted using the Admin Job Task." };
                        JOptionPane.showMessageDialog(jobSelector, messageArr, "Can Not Process This Job", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    JobProcessingTask jobTask;
                    try {
                        jobTask = (JobProcessingTask) dprClient.getTaskByName(loggedInUser, ReprocessingJobProcessingTask.TASK_NAME);
                    } catch (AccessRestrictedException e1) {
                        ExceptionDialog.showAccessRestrictedDialog(jobSelector.getMainFrame(), e1.getRequiredPermission());
                        return;
                    }
                    try {
                        jobSelector.getMainFrame().showJobProcessingTask(jobTask, selectedJobNumber, null);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "An exception occurred when attempting to process a reprocessing job.", e);
                        ExceptionDialog.showExceptionDialog(jobSelector.getMainFrame(), e, "Problem Processing Reprocessing Job", "An exception occurred when attempting to process a reprocessing job.");
                    }
                } else {
                    JobProcessingTask jobTask;
                    try {
                        jobTask = (JobProcessingTask) dprClient.getTaskByName(loggedInUser, TransferJobProcessingTask.TASK_NAME);
                    } catch (AccessRestrictedException e1) {
                        ExceptionDialog.showAccessRestrictedDialog(jobSelector.getMainFrame(), e1.getRequiredPermission());
                        return;
                    }
                    try {
                        jobSelector.getMainFrame().showJobProcessingTask(jobTask, selectedJobNumber, null);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "An exception occurred when attempting to process a transfer job.", e);
                        ExceptionDialog.showExceptionDialog(jobSelector.getMainFrame(), e, "Problem Processing Transfer Job", "An exception occurred when attempting to process a transfer job.");
                    }
                }
                jobSelector.getMainFrame().setProcessingState(true);
            }
        }
    }

    /**
	 * This will attempt to import a job, from a db4o file generated on a different facility.
	 * For instance, PF importing from QF, or DR importing from PF.
	 * 
	 * It is possible that a job has been sent 'back for reprocessing', for example if a job arrives
	 * at PF and it is missing entire data objects, the job could be sent back to QF, reprocessed,
	 * then imported again to on PF. For this reason, it is not always the case that the job does
	 * not exist on this facility.
	 * 
	 */
    public class ImportJobActionProvider implements ActionProvider {

        JobSelectorPanel jobSelector;

        public ImportJobActionProvider(JobSelectorPanel jobSelector) {
            this.jobSelector = jobSelector;
        }

        public void doAction(ActionEvent ae) {
            MemoryFileChooser fileChooser = new MemoryFileChooser();
            int result = fileChooser.showOpenDialog(jobSelector, JobSelectorActions.class, "import-" + currentFacility.getDescription());
            if (result == JFileChooser.APPROVE_OPTION) {
                Db4oJobType jobType;
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    jobType = getJobType(selectedFile);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "An exception occurred when attempting to determine the import file type.", ex);
                    ExceptionDialog.showExceptionDialog(jobSelector.getMainFrame(), ex, "Problem Importing Job", "An exception occurred when attempting to determine the import file type.");
                    return;
                }
                JobProcessingTask jobTask;
                try {
                    switch(jobType) {
                        case REPROCESSING_JOB:
                            jobTask = (JobProcessingTask) dprClient.getTaskByName(loggedInUser, ReprocessingJobProcessingTask.TASK_NAME);
                            break;
                        case TRANSFER_JOB:
                            jobTask = (JobProcessingTask) dprClient.getTaskByName(loggedInUser, TransferJobProcessingTask.TASK_NAME);
                            break;
                        default:
                            throw new IllegalStateException("job type is not a transfer job or a reprocessing job!");
                    }
                } catch (AccessRestrictedException e) {
                    ExceptionDialog.showAccessRestrictedDialog(jobSelector.getMainFrame(), e.getRequiredPermission());
                    return;
                }
                new ImportThread(selectedFile, jobSelector, jobTask).start();
            }
        }

        /**
		 * Analyse the given Db4o file to determine the job type. The job type should either be a transfer job or a reprocessing job
		 * 
		 * @param selectedFile
		 * @return
		 * @throws IOException 
		 */
        private Db4oJobType getJobType(File selectedFile) throws IOException {
            ObjectContainer db = Db4o.openFile(selectedFile.getAbsolutePath());
            try {
                ObjectSet transferJobSet = db.query(TransferJob.class);
                if (transferJobSet.hasNext()) {
                    return Db4oJobType.TRANSFER_JOB;
                }
                ObjectSet reprocessingJobSet = db.query(ReprocessingJob.class);
                if (reprocessingJobSet.hasNext()) {
                    return Db4oJobType.REPROCESSING_JOB;
                }
                throw new IOException("The selected file does not appear to be a transfer job or a reprocessing job.");
            } finally {
                db.close();
            }
        }
    }

    private class ImportThread extends SwingWorker {

        private ImportExportListener listener;

        private File importFile;

        private JobProcessingTask task;

        public ImportThread(File importFile, ImportExportListener listener, JobProcessingTask task) {
            super();
            this.importFile = importFile;
            this.listener = listener;
            this.task = task;
        }

        @Override
        public Object construct() {
            if (task instanceof TransferJobProcessingTask) {
                TransferJobImporter importer = new TransferJobImporter(importFile, loggedInUser, task);
                importer.addImportListener(listener);
                try {
                    importer.doImport();
                } catch (ImportException e) {
                    return null;
                }
            } else if (task instanceof ReprocessingJobProcessingTask) {
                ReprocessingJobImporter importer = new ReprocessingJobImporter(importFile, loggedInUser, task);
                importer.addImportListener(listener);
                try {
                    importer.doImport();
                } catch (au.gov.naa.digipres.dpr.core.importexport.ReprocessingJobImporter.ImportException e) {
                    return null;
                }
            }
            return Boolean.TRUE;
        }
    }

    public class AdminTranfserJobActionProvider implements ActionProvider {

        JobSelectorPanel jobSelector;

        public AdminTranfserJobActionProvider(JobSelectorPanel jobSelector) {
            this.jobSelector = jobSelector;
        }

        public void doAction(ActionEvent ae) {
            int selectedIndex = jobSelector.jobsTable.getSelectedRow();
            if (selectedIndex != -1) {
                final JobNumber selectedJobNumber = (JobNumber) jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.JOB_NUMBER_COLUMN);
                isReprocessingJob = false;
                if (currentFacility.getId() != Facility.QUARANTINE_FACILITY_ID) {
                    isReprocessingJob = Boolean.TRUE.equals(jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.IS_REPROCESSINGJOB_COLUMN));
                }
                MainFrame mainFrame = jobSelector.getMainFrame();
                if (!isReprocessingJob) {
                    mainFrame.showAdministerTransferJobPanel(selectedJobNumber);
                } else {
                    mainFrame.showAdministerReprocessingJobPanel(selectedJobNumber);
                }
            }
        }
    }

    public class ViewJobActionProvider implements ActionProvider {

        JobSelectorPanel jobSelector;

        public ViewJobActionProvider(JobSelectorPanel jobSelector) {
            this.jobSelector = jobSelector;
        }

        public void doAction(ActionEvent ae) {
            int selectedIndex = jobSelector.jobsTable.getSelectedRow();
            if (selectedIndex != -1) {
                final JobNumber selectedJobNumber = (JobNumber) jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.JOB_NUMBER_COLUMN);
                isReprocessingJob = false;
                if (currentFacility.getId() != Facility.QUARANTINE_FACILITY_ID) {
                    isReprocessingJob = Boolean.TRUE.equals(jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.IS_REPROCESSINGJOB_COLUMN));
                }
                MainFrame mainFrame = jobSelector.getMainFrame();
                if (!isReprocessingJob) {
                    mainFrame.showTransferJobInformationPanel(selectedJobNumber);
                } else {
                    mainFrame.showReprocessingJobInformationPanel(selectedJobNumber);
                }
            }
        }
    }

    public class StopJobActionProvider implements ActionProvider {

        private JobSelectorPanel jobSelector;

        private MainFrame mainFrame;

        public StopJobActionProvider(JobSelectorPanel selector) {
            jobSelector = selector;
            mainFrame = selector.getMainFrame();
        }

        public void doAction(ActionEvent ae) {
            int selectedIndex = jobSelector.jobsTable.getSelectedRow();
            if (selectedIndex != -1) {
                try {
                    StopJobTask stopTask = (StopJobTask) dprClient.getTaskByName(mainFrame.getLoggedInUser(), StopJobTask.TASK_NAME);
                    final JobNumber selectedJobNumber = (JobNumber) jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.JOB_NUMBER_COLUMN);
                    isReprocessingJob = false;
                    if (currentFacility.getId() != Facility.QUARANTINE_FACILITY_ID) {
                        isReprocessingJob = Boolean.TRUE.equals(jobSelector.jobsTable.getValueAt(selectedIndex, JobSelectorPanel.JobSelectorTableModel.IS_REPROCESSINGJOB_COLUMN));
                    }
                    if (isReprocessingJob) {
                        ExceptionDialog.showExceptionDialog(jobSelector.getMainFrame(), "Not implemented yet", "You Can't Do That.", "I am sorry " + loggedInUser.getUserName() + " but I can't let you do that.");
                    } else {
                        try {
                            new StopJobDialog(mainFrame, stopTask, stopTask.getJobEncapsulator(selectedJobNumber), currentFacility);
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "An exception occurred when attempting to process a transfer job.", e);
                            ExceptionDialog.showExceptionDialog(jobSelector.getMainFrame(), e, "Problem Processing Transfer Job", "An exception occurred when attempting to process a transfer job.");
                        }
                    }
                } catch (AccessRestrictedException e) {
                    ExceptionDialog.showAccessRestrictedDialog(mainFrame, e.getRequiredPermission());
                }
            }
        }
    }

    public class ShowDRTasksActionProvider implements ActionProvider {

        MainFrame mainFrame;

        /**
		 * @param mainFrame
		 */
        public ShowDRTasksActionProvider(MainFrame mainFrame) {
            super();
            this.mainFrame = mainFrame;
        }

        public void doAction(ActionEvent ae) {
            mainFrame.showDRTasks();
        }
    }
}
