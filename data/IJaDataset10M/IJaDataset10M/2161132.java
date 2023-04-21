package au.gov.naa.digipres.dpr.ui.task;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import au.gov.naa.digipres.dpr.core.Messages;
import au.gov.naa.digipres.dpr.model.facility.Facility;
import au.gov.naa.digipres.dpr.model.job.JobNumber;
import au.gov.naa.digipres.dpr.model.transferjob.TransferJob;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.AdministerTransferJobTask;
import au.gov.naa.digipres.dpr.ui.core.MainFrame;
import au.gov.naa.digipres.dpr.ui.viewdata.TransferJobDetailsPanel;
import au.gov.naa.digipres.dpr.util.DPRIcons;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class AdminTransferJobPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private TransferJob transferJob;

    private User loggedInUser;

    private JLabel titleLabel;

    private JPanel transferJobInformationPanel;

    private JButton deleteButton;

    private JButton changeJobNumberButton;

    private JButton rejectButton;

    private JButton restartButton;

    private JButton reprocessButton;

    private JButton exportButton;

    private AdminTransferJobActions actions;

    private Facility currentFacility;

    public AdminTransferJobPanel(MainFrame mainFrame, AdministerTransferJobTask task, TransferJob transferJob, User user) {
        this.transferJob = transferJob;
        loggedInUser = user;
        actions = new AdminTransferJobActions(mainFrame, task, transferJob, loggedInUser);
        currentFacility = mainFrame.getCurrentFacility();
        JobNumber jobNumber = transferJob.getTransferJobNumber();
        initComponents(jobNumber);
        initLayout();
    }

    private void initComponents(JobNumber jobNumber) {
        String title = Messages.getString(this.getClass(), "AdminTransferJobPanel.title") + " " + jobNumber.toString();
        titleLabel = new JLabel(title, DPRIcons.getIconByName("images/report48.png"), SwingConstants.LEFT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24.0f));
        transferJobInformationPanel = new TransferJobDetailsPanel(transferJob, false);
        changeJobNumberButton = new JButton(actions.getChangeJobNumberCommand());
        deleteButton = new JButton(actions.getDeleteTransferJobCommand());
        rejectButton = new JButton(actions.getRejectTransferJobCommand());
        restartButton = new JButton(actions.getRestartTransferJobCommand());
        reprocessButton = new JButton(actions.getReprocessTransferJobCommand());
        exportButton = new JButton(actions.getExportTransferJobCommand());
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        FormLayout layout = new FormLayout("max(30dlu;p), 4dlu, " + "max(40dlu;p), 0dlu:grow", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.append(transferJobInformationPanel, layout.getColumnCount());
        builder.append(deleteButton);
        builder.nextLine();
        if (currentFacility.equals(Facility.DIGITAL_REPOSITORY)) {
            builder.append(changeJobNumberButton);
            builder.nextLine();
        }
        if (currentFacility.equals(Facility.QUARANTINE_FACILITY)) {
            builder.append(rejectButton);
            builder.nextLine();
        }
        builder.append(restartButton);
        builder.nextLine();
        if (currentFacility.equals(Facility.QUARANTINE_FACILITY) || currentFacility.equals(Facility.PRESERVATION_FACILITY)) {
            builder.append(reprocessButton);
            builder.nextLine();
            builder.append(exportButton);
            builder.nextLine();
        }
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(builder.getPanel(), BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        add(titleLabel, BorderLayout.NORTH);
    }
}
