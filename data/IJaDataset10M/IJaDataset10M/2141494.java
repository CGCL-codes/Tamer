package com.explosion.datastream.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.datastream.EXQLModuleManager;
import com.explosion.datastream.processes.ConnectProcess;
import com.explosion.expf.Application;
import com.explosion.expf.ExpConstants;
import com.explosion.expf.ExpFrame;
import com.explosion.expf.ExpInternalFrame;
import com.explosion.expfmodules.rdbmsconn.connect.ConnectionManager;
import com.explosion.utilities.exception.ExceptionManagerFactory;
import com.explosion.utilities.preferences.groups.PreferenceGroup;
import com.explosion.utilities.process.threads.Finishable;
import com.explosion.utilities.process.threads.ProcessThread;
import com.explosion.utilities.process.threads.SimpleProcess;
import com.explosion.utilities.process.threads.SimpleProcessThread;

/**
 * This class is a dialog box that provides buttons for a user to cancel a
 * connect. It also kicks off an EXQL Tool if the connection is succesful
 * 
 * @author Stephen Created on May 3, 2004
 */
public class ConnectPanel extends JPanel implements Finishable {

    private static Logger log = LogManager.getLogger(ConnectPanel.class);

    private ConnectStatusBar statusBar;

    private ConnectProcess process;

    private boolean cancelled = false;

    public ConnectPanel() {
        init();
    }

    private void init() {
        statusBar = new ConnectStatusBar(this);
        this.setLayout(new BorderLayout());
        this.add(statusBar, BorderLayout.CENTER);
    }

    /**
     * Starts the connection process
     *  
     */
    public void connect(PreferenceGroup descriptor) {
        try {
            log.debug("connect");
            process = new ConnectProcess(this, descriptor);
            statusBar.setProcessToMonitor(process);
            statusBar.startProcess();
        } catch (Exception e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while initialising SystemMonitoringProcess");
        }
    }

    /**
     * Creates an EXQL Base tool and hands it the connection ey as well as the
     * connection Manager
     * 
     * @see com.explosion.utilities.process.threads.Finishable#finish()
     *  
     */
    public void finish() {
        log.debug("finish");
        try {
            int connectionKey = process.getConnectionIdentifier();
            if (connectionKey >= 0) {
                try {
                    ((ExpFrame) Application.getApplicationFrame()).getFrameWithComponent(this, ExpFrame.PALETTE_LAYER.intValue()).setVisible(false);
                } catch (Exception e1) {
                }
                int lastHeight = ((Integer) EXQLModuleManager.instance().getPreference(ExpConstants.HEIGHT).getValue()).intValue();
                int lastWidth = ((Integer) EXQLModuleManager.instance().getPreference(ExpConstants.WIDTH).getValue()).intValue();
                EXQLBaseTool tool = new EXQLBaseTool(connectionKey, ConnectionManager.getInstance());
                ExpInternalFrame frame = ((ExpFrame) Application.getApplicationFrame()).createDocumentFrame(tool, new Dimension(lastWidth, lastHeight), (connectionKey + 1) + " : " + ConnectionManager.getInstance().getConnectionDescriptor(connectionKey).getIdentifier(), true);
                frame.addSizePersistence(EXQLModuleManager.instance().getPreference(ExpConstants.HEIGHT), EXQLModuleManager.instance().getPreference(ExpConstants.WIDTH));
                frame.addToolbar(tool.getToolBar());
                tool.refreshTool();
            }
        } catch (Exception e) {
            try {
                ((ExpFrame) Application.getApplicationFrame()).getFrameWithComponent(this, ExpFrame.PALETTE_LAYER.intValue()).setVisible(false);
            } catch (Exception e1) {
            }
            ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while createing the EXQL tool.");
        }
        try {
            ((ExpFrame) Application.getApplicationFrame()).closeFrameWithComponent(this, ExpFrame.PALETTE_LAYER);
        } catch (Exception e1) {
        }
    }

    /**
     * @return Returns the cancelled.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @param cancelled The cancelled to set.
     */
    public void cancel() {
        process.setPercentComplete(90);
        process.setStatusText("Cancelling connect.");
        this.setVisible(false);
        this.cancelled = true;
    }
}

class ConnectStatusBar extends JPanel implements SimpleProcess {

    private JButton cancelButton = new JButton("Cancel");

    private JPanel buttonPanel = new JPanel();

    private JPanel progressPanel = new JPanel();

    private JLabel statusLabel = new JLabel();

    private ProcessThread processThread;

    private ProcessThread workerThread;

    private ConnectPanel connectPanel;

    private boolean isUserProcess = false;

    public ConnectStatusBar(ConnectPanel connectPanel) {
        try {
            this.connectPanel = connectPanel;
            init();
        } catch (Exception e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, null);
        }
    }

    public void init() throws Exception {
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        progressPanel.setLayout(new GridBagLayout());
        progressPanel.add(statusLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Status"));
        this.add(progressPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        this.add(buttonPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void setProcessToMonitor(com.explosion.utilities.process.threads.Process processToMonitor) throws Exception {
        if (this.workerThread != null && workerThread.getRunThread() != null && workerThread.getRunThread().isAlive()) throw new Exception("Worker thread is currently busy.  Cannot start another process yet.");
        this.workerThread = processToMonitor.getProcessControl();
    }

    public void startProcess() throws Exception {
        if (this.workerThread == null) throw new Exception("Worker thread is null.  Cannot initiaite processing.");
        processThread = new SimpleProcessThread();
        processThread.setInterval(75);
        processThread.setProcess(this);
        this.workerThread.start();
        this.processThread.start();
    }

    /**
     * Stops the process but asks the user to confirm, first
     *  
     */
    public void stopProcess() {
        cancelButton_actionPerformed(null);
    }

    /**
     * Stops the process without asking for confirmation
     *  
     */
    public void stopNoConfirm() throws InterruptedException {
        workerThread.stop();
    }

    void cancelButton_actionPerformed(ActionEvent ed) {
        try {
            connectPanel.cancel();
            workerThread.stop();
        } catch (Exception ex) {
            ExceptionManagerFactory.getExceptionManager().manageException(ex, null);
        }
    }

    /**
     * @see com.explosion.utilities.process.threads.SimpleProcess#process()
     */
    public void process() throws Exception {
        Runnable updateProgress = new Runnable() {

            public void run() {
                statusLabel.setText(workerThread.getProcess().getStatusText());
            }
        };
        SwingUtilities.invokeLater(updateProgress);
    }

    public void kill() {
    }

    /**
     * @see com.explosion.utilities.process.threads.SimpleProcess#finalise()
     * @throws Exception
     */
    public void finalise() throws Exception {
    }

    /**
     * @see com.explosion.utilities.process.threads.SimpleProcess#finalise(java.lang.Exception)
     * @param e
     */
    public void finalise(Exception e) {
    }

    /**
     * @see com.explosion.utilities.process.threads.SimpleProcess#initialise()
     * @throws Exception
     */
    public void initialise() throws Exception {
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#getPercentComplete()
     * @return
     */
    public int getPercentComplete() {
        return 0;
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#getProcessControl()
     * @return
     */
    public ProcessThread getProcessControl() {
        return null;
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#getStatusText()
     * @return
     */
    public String getStatusText() {
        return null;
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#isStopped()
     * @return
     */
    public boolean isStopped() {
        return false;
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#log(java.lang.Exception, java.lang.String)
     * @param exception
     * @param message
     */
    public void log(Exception exception, String message) {
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#log(java.lang.String)
     * @param string
     */
    public void log(String string) {
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#setPercentComplete(int)
     * @param percentComplete
     */
    public void setPercentComplete(int percentComplete) {
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#setProcessControl(com.explosion.Utils.Processing.Threading.ProcessThread)
     * @param processThread
     */
    public void setProcessControl(ProcessThread processThread) {
    }

    /**
     * @see com.explosion.utilities.process.threads.Process#setStatusText(java.lang.String)
     * @param statusText
     */
    public void setStatusText(String statusText) {
    }

    /**
     * Returns a boolean value indicating whther this is a user process or not
     * @return
     */
    public boolean isUserProcess() {
        return this.isUserProcess;
    }

    /**
     * Sets whtether or not this is a user process.  True means that it is
     * @param truth
     */
    public void setIsUserProcess(boolean truth) {
        this.isUserProcess = true;
    }
}
