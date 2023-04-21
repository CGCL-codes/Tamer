package com.pallas.unicore.requests;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.unicore.upl.ConsignJobReply;
import org.unicore.upl.UnicoreResponse;
import com.pallas.unicore.client.trees.ContainerNode;
import com.pallas.unicore.container.ActionContainer;
import com.pallas.unicore.container.JobContainer;
import com.pallas.unicore.container.TaskContainer;
import com.pallas.unicore.extensions.FileImport;
import com.pallas.unicore.resourcemanager.ResourceManager;
import com.pallas.unicore.utility.UserMessages;

/**
 * Submit a job from the job preparation area
 * 
 * @author Thomas Kentemich
 * @author Ralf Ratering
 * @version $Id: SubmitJobNode.java,v 1.3 2006/01/04 09:14:01 bschuller Exp $
 */
public class SubmitJobNode extends ObservableNodeThread {

    private JobContainer jobContainer;

    private long start, total;

    /**
	 * Constructor
	 * 
	 * @param node
	 *            Job Node in Job Preparation Area to be submitted
	 */
    public SubmitJobNode(ContainerNode node) {
        super(node);
        jobContainer = (JobContainer) node.getActionContainer();
    }

    /**
	 * Start the Thread
	 */
    public void run() {
        try {
            start = System.currentTimeMillis();
            if (!jobContainer.isComplete()) {
                throw new Exception("Cannot submit incorrect job: " + jobContainer);
            }
            ProgressDialog progressDialog = new ProgressDialog(ResourceManager.getCurrentInstance(), false, jobContainer);
            progressDialog.showInitialScreen();
            jobContainer.run();
            if (jobContainer.getState() == JobContainer.STATE_SUBMITTED) {
                total = System.currentTimeMillis() - start;
                progressDialog.showEndScreen(total);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Thread interrupted", e);
                }
            }
            progressDialog.dispose();
        } catch (Exception e) {
            UserMessages.warning("Could not submit job " + getNode());
            logger.log(Level.SEVERE, "Could not submit job " + getNode(), e);
        }
        notifyObservers(this, getNode());
    }

    /**
	 * Dialog to show file import progress. Currently unused
	 */
    private class ProgressDialog extends JDialog {

        private class ProgressThread extends Thread {

            public void run() {
                Vector importTasks = container.getNspaceImportTasks();
                if (!importTasks.isEmpty()) {
                    buildImportDialog();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                }
                while (container.getState() != ActionContainer.STATE_SUBMITTED) {
                    try {
                        if (!importTasks.isEmpty()) {
                            showImportProgress();
                        }
                        sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        private JProgressBar bar = new JProgressBar(0, 100);

        private JobContainer container;

        private FileImport currentImport;

        private JLabel filenameLabel = new JLabel("");

        private int kbSize = 0;

        private JLabel sizeLabel = new JLabel("");

        private JLabel speedLabel = new JLabel("");

        private long start = 0;

        private JLabel transferredLabel = new JLabel("");

        public ProgressDialog(Frame owner, boolean modal, JobContainer container) {
            super(owner, modal);
            this.container = container;
        }

        private void buildImportDialog() {
            bar.setBorderPainted(true);
            bar.setStringPainted(true);
            JPanel namePanel = new JPanel();
            namePanel.add(new JLabel("Transferring file: "));
            namePanel.add(filenameLabel);
            JPanel sizePanel = new JPanel();
            sizePanel.add(new JLabel("Size:"));
            sizePanel.add(sizeLabel);
            sizePanel.add(new JLabel(" kBytes"));
            sizePanel.add(new JLabel(" Transferred:"));
            sizePanel.add(transferredLabel);
            sizePanel.add(new JLabel(" kBytes"));
            sizePanel.add(new JLabel(" Speed:"));
            sizePanel.add(speedLabel);
            sizePanel.add(new JLabel(" kBytes/sec"));
            removeAll();
            getContentPane().add(namePanel, BorderLayout.NORTH);
            getContentPane().add(sizePanel, BorderLayout.CENTER);
            getContentPane().add(bar, BorderLayout.SOUTH);
        }

        private String genMessage(int code, String reason, int otherSubmissionErrors) {
            if (code == 0 && otherSubmissionErrors == 0) {
                return ResourceManager.getCompleteFormatter().format(new Date()) + ": Job successfully submitted.";
            }
            logger.severe("The submission encountered an error:" + reason);
            return "The submission encountered an error.\n" + reason;
        }

        public void showEndScreen(long totalTime) {
            ConsignJobReply reply = container.getSubmissionResult();
            String response = "<None>";
            int code = 0;
            if (reply != null) {
                UnicoreResponse[] allResponses = reply.getTrace();
                code = allResponses[allResponses.length - 1].getReturnCode();
                response = allResponses[allResponses.length - 1].getComment();
            }
            String message = genMessage(code, response, container.getOtherSubmissionErrors());
            getContentPane().removeAll();
            getContentPane().add(new JLabel(message), BorderLayout.CENTER);
            long tx = getTotalBytes();
            long tm = totalTime;
            String spd = "n/a";
            try {
                spd = tx / tm + " kB/s";
            } catch (Exception ex) {
            }
            String msg = "Transmitted " + tx / 1000 + " kB in " + tm + " milliseconds, speed " + spd;
            getContentPane().add(new JLabel(msg), BorderLayout.SOUTH);
            ResourceManager.getCurrentInstance().setStatusMessage("Job submitted. " + msg);
            pack();
            setLocationRelativeTo(getParent());
            show();
        }

        private void showImportProgress() {
            Vector importTasks = container.getNspaceImportTasks();
            for (int i = 0; i < importTasks.size(); i++) {
                TaskContainer container = (TaskContainer) importTasks.elementAt(i);
                FileImport[] fileImports = container.getFileImports();
                for (int j = 0; j < fileImports.length; j++) {
                    if (fileImports[j].isTransferring()) {
                        if (fileImports[j] != currentImport) {
                            currentImport = fileImports[j];
                            start = System.currentTimeMillis();
                            filenameLabel.setText(currentImport.getSourceName());
                            kbSize = (int) (currentImport.getTotalBytes() / 1024);
                            sizeLabel.setText("" + kbSize);
                        }
                        int percentage = (int) (100 * currentImport.getTransferredBytes() / currentImport.getTotalBytes());
                        int kbProgress = (int) (currentImport.getTransferredBytes() / 1024);
                        long diff = (System.currentTimeMillis() - start) / 1000;
                        long speed = (diff == 0 ? 0 : kbProgress / diff);
                        transferredLabel.setText("" + kbProgress);
                        speedLabel.setText("" + speed);
                        bar.setValue(percentage);
                        bar.setString(percentage + "%");
                        pack();
                        setLocationRelativeTo(getParent());
                    }
                }
            }
        }

        public void showInitialScreen() {
            setTitle("Submitting " + container + " to " + container.getVsite());
            String message = "Submission of " + container + " to site " + container.getVsite() + " started...";
            getContentPane().add(new JLabel(message), BorderLayout.CENTER);
            pack();
            setLocationRelativeTo(getParent());
            show();
        }

        public void start() {
            new ProgressThread().start();
        }

        private long getTotalBytes() {
            long totalBytes = 0;
            Vector importTasks = container.getNspaceImportTasks();
            for (int i = 0; i < importTasks.size(); i++) {
                TaskContainer container = (TaskContainer) importTasks.elementAt(i);
                FileImport[] fileImports = container.getFileImports();
                for (int j = 0; j < fileImports.length; j++) {
                    totalBytes += fileImports[j].getTotalBytes();
                }
            }
            return totalBytes;
        }
    }
}
