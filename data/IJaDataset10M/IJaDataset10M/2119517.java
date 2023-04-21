package au.gov.naa.digipres.dpr.ui.task.step;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import au.gov.naa.digipres.dpr.core.DPRClient;
import au.gov.naa.digipres.dpr.model.job.JobEncapsulator;
import au.gov.naa.digipres.dpr.task.JobProcessingTask;
import au.gov.naa.digipres.dpr.task.step.NormalisationStep;
import au.gov.naa.digipres.dpr.task.step.StepResults;
import au.gov.naa.digipres.dpr.task.step.NormalisationStep.NormalisationStepHandler;
import au.gov.naa.digipres.dpr.ui.core.MainFrame;
import au.gov.naa.digipres.dpr.ui.task.StepErrorDetailsModel;
import au.gov.naa.digipres.dpr.ui.task.StepErrorDisplayPanel;
import au.gov.naa.digipres.xena.core.Xena;

/**
 * @author Justin Waddell
 *
 */
public class NormalisationStepHandlerGUI implements NormalisationStepHandler {

    private DPRClient dprClient;

    private MainFrame mainFrame;

    private JobProcessingTask task;

    private JTable errorDetailsTable;

    private StepErrorDetailsModel errorDetailsModel;

    private JDialog errorPanelDialog;

    public NormalisationStepHandlerGUI(MainFrame mainFrame, DPRClient dprClient, JobProcessingTask task) {
        this.mainFrame = mainFrame;
        this.dprClient = dprClient;
        this.task = task;
    }

    public NormalisationActionType getNormalisationAction(StepResults results) {
        NormalisationActionDialog dialog = new NormalisationActionDialog(mainFrame, results);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
        return dialog.getReturnType();
    }

    public StepResults performAdvancedNormalisation(Xena xena, JobEncapsulator jobEncapsulator, File inputDataPath, File destinationDir, NormalisationStep normalisationStep) {
        AdvancedNormalisationDialog dialog;
        if (jobEncapsulator.getJobType() == JobEncapsulator.TRANSFER_JOB_TYPE) {
            dialog = new TransferJobAdvancedNormalisationDialog(mainFrame, dprClient, task, xena, jobEncapsulator, inputDataPath, destinationDir, normalisationStep);
        } else if (jobEncapsulator.getJobType() == JobEncapsulator.REPROCESSING_JOB_TYPE) {
            dialog = new ReprocessingJobAdvancedNormalisationDialog(mainFrame, dprClient, task, xena, jobEncapsulator, inputDataPath, destinationDir, normalisationStep);
        } else {
            throw new IllegalArgumentException("JobEncapsulator does not contain a Transfer Job or a Reprocessing Job");
        }
        dialog.setVisible(true);
        return dialog.getStepResults();
    }

    private class NormalisationActionDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        private NormalisationActionType returnType;

        public NormalisationActionDialog(MainFrame mainFrame, StepResults results) {
            super(mainFrame, "Normalisation Complete", true);
            initGUI(results);
        }

        /**
		 * @param results
		 */
        private void initGUI(StepResults results) {
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton advancedButton = new JButton("Advanced Normalisation");
            advancedButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    returnType = NormalisationActionType.ADVANCED_NORMALISATION;
                    NormalisationActionDialog.this.setVisible(false);
                }
            });
            buttonPanel.add(advancedButton);
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    returnType = NormalisationActionType.SAVE;
                    NormalisationActionDialog.this.setVisible(false);
                }
            });
            buttonPanel.add(saveButton);
            JTextArea headerTextArea = new JTextArea();
            headerTextArea.setEditable(false);
            headerTextArea.setBackground(new JPanel().getBackground());
            if (results.isErrorOccurred()) {
                String headerText = "The table below contains the list of errors that occurred during the normalisation process.\n" + "You can:\n" + "- Perform Advanced Normalisation\n" + "- Save the results\n" + "- Stop processing the job\n" + "- Reset the step";
                headerTextArea.setRows(6);
                headerTextArea.setColumns(40);
                headerTextArea.setText(headerText);
                errorDetailsModel = new StepErrorDetailsModel(results.getErrorData());
                errorDetailsTable = new JTable(errorDetailsModel);
                errorDetailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                errorDetailsTable.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int selectedRow = errorDetailsTable.getSelectedRow();
                            Map<String, Object> errorDetails = errorDetailsModel.getDataForRow(selectedRow);
                            showErrorDetails(errorDetails);
                        }
                    }
                });
                this.add(new JScrollPane(errorDetailsTable), BorderLayout.CENTER);
                JButton stopButton = new JButton("Stop Processing");
                stopButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        returnType = NormalisationActionType.STOP_PROCESSING;
                        NormalisationActionDialog.this.setVisible(false);
                    }
                });
                buttonPanel.add(stopButton);
                JButton resetButton = new JButton("Reset Step");
                resetButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        returnType = NormalisationActionType.RESET_STEP;
                        NormalisationActionDialog.this.setVisible(false);
                    }
                });
                buttonPanel.add(resetButton);
            } else {
                String headerText = "The normalisation process finished successfully.\n" + "You can perform Advanced Normalisation, or save the results.";
                headerTextArea.setRows(2);
                headerTextArea.setColumns(40);
                headerTextArea.setText(headerText);
            }
            this.add(headerTextArea, BorderLayout.NORTH);
            this.add(buttonPanel, BorderLayout.SOUTH);
            pack();
        }

        public NormalisationActionType getReturnType() {
            return returnType;
        }
    }

    /**
	 * @param errorDetails
	 */
    protected void showErrorDetails(Map<String, Object> errorDetails) {
        StepErrorDisplayPanel panel = new StepErrorDisplayPanel(errorDetails);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                errorPanelDialog.setVisible(false);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        errorPanelDialog = new JDialog(mainFrame, "Step Error Details", true);
        errorPanelDialog.setLayout(new BorderLayout());
        errorPanelDialog.add(panel, BorderLayout.CENTER);
        errorPanelDialog.add(buttonPanel, BorderLayout.SOUTH);
        errorPanelDialog.pack();
        errorPanelDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                errorPanelDialog.setVisible(false);
            }
        });
        errorPanelDialog.setLocationRelativeTo(mainFrame);
        errorPanelDialog.setVisible(true);
    }
}
