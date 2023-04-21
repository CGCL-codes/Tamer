package mya_dc.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import mya_dc.client.core.CompilationClient;
import mya_dc.client.gui.TextMessages.EMessageBoxType;
import mya_dc.shared_classes.Message;
import mya_dc.shared_classes.MYA_FileTreeNode.FilesToGet;
import mya_dc.shared_classes.Message.EMessageType;

/**
 * A window that handles the progress of the files transfer/project compilation.
 * 
 * @author      Adam Levi
 * <br>			MYA
 */
public class ProgressWindow extends JFrame {

    private JButton cancelButton;

    private JCheckBox closeWindowWhenCheckBox;

    private static final long serialVersionUID = 4211477451150030404L;

    public enum EActionType {

        FILE_TRANSFER, COMPILE_PROCCESS
    }

    ;

    private JCheckBox phase2CheckBox;

    private JLabel statusLabel;

    private JProgressBar progressBar;

    private JCheckBox phase3CheckBox;

    private JCheckBox phase1CheckBox;

    /**
	 * Create the frame
	 */
    public ProgressWindow(ClientMainWindow parent) {
        super();
        addWindowListener(new WindowAdapter() {

            public void windowClosed(final WindowEvent e) {
                doClose();
            }
        });
        setResizable(false);
        setTitle("MYA Client - Transfer");
        getContentPane().setLayout(null);
        setBounds(100, 100, 410, 338);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        m_Parent = parent;
        progressBar = new JProgressBar();
        getContentPane().add(progressBar);
        progressBar.setBounds(10, 49, 382, 33);
        statusLabel = new JLabel();
        getContentPane().add(statusLabel);
        statusLabel.setBounds(10, 10, 382, 33);
        statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusLabel.setBackground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        final JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setLayout(null);
        panel.setBorder(new TitledBorder(null, "Total Progress", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.inactiveCaption));
        panel.setBounds(10, 88, 382, 113);
        getContentPane().add(panel);
        phase2CheckBox = new JCheckBox();
        phase2CheckBox.setBounds(10, 54, 284, 23);
        panel.add(phase2CheckBox);
        phase2CheckBox.setEnabled(false);
        phase2CheckBox.setText("Phase 2: Verifiying project");
        phase3CheckBox = new JCheckBox();
        phase3CheckBox.setBounds(10, 82, 284, 23);
        panel.add(phase3CheckBox);
        phase3CheckBox.setEnabled(false);
        phase3CheckBox.setText("Phase 3: Compiling sources (Only if requested)");
        phase1CheckBox = new JCheckBox();
        phase1CheckBox.setBounds(10, 25, 284, 23);
        panel.add(phase1CheckBox);
        phase1CheckBox.setSelected(false);
        phase1CheckBox.setEnabled(false);
        phase1CheckBox.setText("Phase 1: Transferring Files");
        cancelButton = new JButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                doCancel();
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.setBounds(293, 271, 101, 25);
        getContentPane().add(cancelButton);
        final JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "Prefrences", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.inactiveCaption));
        panel_1.setLayout(null);
        panel_1.setBounds(10, 207, 382, 58);
        getContentPane().add(panel_1);
        closeWindowWhenCheckBox = new JCheckBox();
        closeWindowWhenCheckBox.setBounds(10, 20, 179, 23);
        panel_1.add(closeWindowWhenCheckBox);
        closeWindowWhenCheckBox.setText("Close this dialog when finished");
        m_DoCompile = false;
    }

    /**
	 * The basic method for sending actions to the server
	 * 
	 * @param action 		- The type of action to take. (either file transfer or project compilation)
	 * @param typeOfFiles   - The type of files to transfer
	 * @param client 	    - The compilation client
	 */
    public void doAction(EActionType action, FilesToGet typeOfFiles, CompilationClient client) {
        m_Client = client;
        if (m_Worker != null && m_Worker.isDone() == false) {
            if (action == EActionType.COMPILE_PROCCESS && m_DoCompile == false) {
                m_DoCompile = true;
            } else {
                TextMessages.showMessageBox(EMessageBoxType.ERROR_ALREADY_IN_PROGRESS, this);
            }
            return;
        }
        m_FilesToTransfer = typeOfFiles;
        if (action == EActionType.COMPILE_PROCCESS) m_DoCompile = true; else m_DoCompile = false;
        phase1CheckBox.setSelected(false);
        phase2CheckBox.setSelected(false);
        phase3CheckBox.setSelected(false);
        m_Parent.setStatusLabel(TextMessages.statusString_FILE_TRANSFER_DONE);
        m_Worker = new DoOnServerWorker();
        m_Worker.execute();
    }

    /**
	 * forces the close of the window, closes the connection
	 * if exists.
	 */
    public void forceClose() {
        if (m_Client != null) {
            m_Client.closeConnection();
        }
        if (m_Worker != null) {
            m_Worker.cancel(true);
        }
        this.setVisible(false);
    }

    ClientMainWindow m_Parent;

    CompilationClient m_Client;

    boolean m_DoCompile;

    DoOnServerWorker m_Worker;

    FilesToGet m_FilesToTransfer;

    /**
	 * hides the window
	 */
    private void doClose() {
        this.setVisible(false);
        cancelButton.setText("Cancel");
    }

    /**
	 * cancels the file transfer or project compilation.
	 */
    private void doCancel() {
        if (m_Worker == null || m_Worker.isDone()) {
            doClose();
        } else {
            m_Client.closeConnection();
            m_Worker.cancel(true);
        }
    }

    /**
	 * A Swing worker class that handles the actions received in the window
	 */
    private class DoOnServerWorker extends SwingWorker<Void, Object[]> {

        private static final int secToWaitBeforeSetIdleStatus = 3;

        private boolean m_SetIdle = true;

        @Override
        protected Void doInBackground() {
            try {
                this.doTransfer();
                if (m_DoCompile) doCompile();
                Thread.sleep(DoOnServerWorker.secToWaitBeforeSetIdleStatus * 1000);
            } catch (InterruptedException e) {
                if (isCancelled()) {
                    m_Client.doCancel();
                }
            }
            return null;
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                statusLabel.setText(TextMessages.statusString_CANCELED);
                m_Parent.setStatusLabel(TextMessages.statusString_CANCELED);
            } else if (m_SetIdle) {
                m_Parent.setStatusLabel(TextMessages.statusString_IDLE);
            }
            m_Parent.updateModels();
            if (closeWindowWhenCheckBox.isSelected()) doClose(); else cancelButton.setText("Close");
        }

        /**
    	 * updates the GUI according to the worker's messages
    	 * <br><b>Notes:</b>
    	 * <br> Object[0] = A string to put in the status bar and the parent's status label.
    	 * <br> Object[1] = The current job that was finished.
    	 * <br>	Object[2] (optional) = A return status from compilationClient.
    	 */
        @Override
        protected void process(List<Object[]> returnedObjectsArray) {
            for (Object[] objArr : returnedObjectsArray) {
                switch((Integer) objArr[1]) {
                    default:
                        {
                            progressBar.setValue(0);
                            progressBar.setMaximum((Integer) objArr[1]);
                            break;
                        }
                    case -3:
                        {
                            phase3CheckBox.setSelected(true);
                            break;
                        }
                    case -4:
                        {
                            phase2CheckBox.setSelected(true);
                            break;
                        }
                    case -2:
                        {
                            phase1CheckBox.setSelected(true);
                            break;
                        }
                    case 0:
                        progressBar.setValue(progressBar.getValue() + 1);
                    case -1:
                }
                statusLabel.setText((String) objArr[0]);
                m_Parent.setStatusLabel((String) objArr[0]);
            }
        }

        /**
    	 * creates an Object[] based on the given input.
    	 * 
    	 * @param objects - the objects that will be inserted in the returned array.
    	 */
        private Object[] makeObjectArr(Object... objects) {
            Object[] arr = new Object[objects.length];
            for (int i = 0; i < objects.length; i++) arr[i] = objects[i];
            return arr;
        }

        /**
    	 * transfers files to the server via the compilation client, and
    	 * sends update messages to the GUI
    	 */
        private Void doTransfer() {
            try {
                int total = m_Client.doTransferStart(m_FilesToTransfer);
                if (total != 0) {
                    Object[] obj = { TextMessages.statusString_FILE_TRANSFER_START, total };
                    publish(obj);
                    while (total > 0) {
                        if (isCancelled()) throw new InterruptedException();
                        publish(makeObjectArr(m_Client.doTransferNext(), 0));
                        total--;
                    }
                }
                m_Client.doTransferEnd();
                publish(makeObjectArr(TextMessages.statusString_FILE_TRANSFER_DONE, -2));
                m_Client.doProjectVerification();
                publish(makeObjectArr(TextMessages.statusString_PROJECT_FERIFICATION_DONE, -4));
            } catch (Exception e) {
                publish(makeObjectArr(TextMessages.statusString_TRANSFER_UNKNOWN_ERROR, -1));
            }
            return null;
        }

        /**
    	 * compile files at the to the server via the compilation client, and
    	 * sends update messages to the GUI
    	 */
        private Void doCompile() {
            try {
                int total = m_Client.doCompileStart();
                switch(total) {
                    case -1:
                        {
                            publish(makeObjectArr(TextMessages.statusString_COMPILATION_UNABLE_TO_COMPILE, -1));
                            return null;
                        }
                    case -2:
                        {
                            publish(makeObjectArr(TextMessages.statusString_COMPILATION_NO_SLAVES, -3, null));
                            return null;
                        }
                }
                publish(makeObjectArr(TextMessages.statusString_COMPILATION_START, total));
                String outOfStr = " out of " + Integer.toString(total);
                boolean compilationFailed = false;
                for (int i = 1; i <= total && !compilationFailed; i++) {
                    if (isCancelled()) throw new InterruptedException();
                    Message tmpMessage = m_Client.doCompileNext();
                    switch(tmpMessage.getType()) {
                        case COMPILATION_SLAVE_DONE:
                            {
                                publish(makeObjectArr("completed " + Integer.toString(i) + outOfStr, 0));
                                break;
                            }
                        case COMPILATION_DONE:
                            {
                                publish(makeObjectArr(TextMessages.statusString_COMPILATION_UNABLE_TO_COMPILE, -1));
                                compilationFailed = true;
                                break;
                            }
                    }
                }
                if (!compilationFailed) {
                    m_Client.doCompileNext();
                }
                EMessageType endMessage = m_Client.doCompileEnd();
                switch(endMessage) {
                    case FILE_TRANSFER:
                    case SUCCESS:
                        {
                            publish(makeObjectArr(TextMessages.statusString_COMPILATION_SUCCESS, -3, endMessage));
                            m_SetIdle = false;
                            break;
                        }
                    case COMPILATION_RESULT_ERRORS:
                        {
                            publish(makeObjectArr(TextMessages.statusString_COMPILATION_RESULT_ERRORS, -3, endMessage));
                            m_SetIdle = false;
                            break;
                        }
                    case COMPILATION_NO_SLAVES:
                        {
                            publish(makeObjectArr(TextMessages.statusString_COMPILATION_NO_SLAVES, -3, endMessage));
                            break;
                        }
                    case COMPILATION_UNABLE_TO_COMPILE:
                        {
                            break;
                        }
                }
            } catch (Exception e) {
                if (isCancelled()) {
                    return null;
                }
                e.printStackTrace();
                publish(makeObjectArr(TextMessages.statusString_COMPILATION_UNABLE_TO_COMPILE, -1));
            }
            return null;
        }
    }
}
