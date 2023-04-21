package net.spamcomplaint.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.AddressException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import net.spamcomplaint.App;
import net.spamcomplaint.dao.ConnectionDAO;
import net.spamcomplaint.dao.vo.MailAccountVO;
import net.spamcomplaint.dao.vo.TemplateVO;
import net.spamcomplaint.mail.MessageListener;
import net.spamcomplaint.mail.ProcessEventListener;
import net.spamcomplaint.mail.SendMail;
import net.spamcomplaint.util.LocaleResource;

/**
 * User can bind a mail account to a template, set a few options and 
 * start / stop sending Spam Complaints. 
 * 
 * @author jcalfee
 */
public class SendMailPanel extends JPanel implements ProcessEventListener, ActionListener, TransportListener, FocusListener {

    public JComboBox templateCombo = new JComboBox();

    public JComboBox mailAccountCombo = new JComboBox();

    private static final long serialVersionUID = 1;

    private ConnectionDAO dao;

    private JComponentHelper jHelper;

    private JButton startButton, stopButton;

    private LocaleResource locale = LocaleResource.inst;

    private Component parentComponent = null;

    private Thread sendThread;

    private SendMail sendMail;

    private Thread stopThread = null;

    private JTextField status = JComponentHelper.createStatusBar();

    private JTextField ccEmailField;

    private JTextField bccEmailField;

    private JTextField msgDays, msgPurgeDays;

    private JCheckBox msgReadOnly, shutdownAfterSend;

    private MessageListener messageListener;

    /**
     * May display several things at once
     */
    private List statList = new LinkedList();

    private final int STAT_ALL = -1;

    private final int STAT_START_STOP = 0;

    private final int STAT_MSG_TRANSMIT = 1;

    private final int STAT_MSG_CONFIG = 2;

    private final int STAT_SWEEP = 3;

    private final int STAT_PURGE = 4;

    private final String MAIL_ACCOUNT_INDEX_KEY = "mail.account.index";

    private final String TEMPLATE_INDEX_KEY = "template.index";

    private final String EMAIL_CC = "email.cc";

    private final String EMAIL_BCC = "email.bcc";

    private final String MESSAGE_DAYS_OLD = "msg.days.old";

    private final String MESSAGE_PURGE_DAYS_OLD = "msg.purge.days.old";

    private final String MESSAGE_SEND_READ_ONLY = "msg.send.read.only";

    private final String SHUTDOWN_AFTER_SEND = "msg.shutdown.after.send";

    private ApplicationPanel appPanel;

    public SendMailPanel(ConnectionDAO dao, ApplicationPanel appPanel) throws SQLException, IOException {
        super(new BorderLayout());
        this.dao = dao;
        jHelper = new JComponentHelper(this);
        super.add(createToolbarPanel(), BorderLayout.NORTH);
        super.add(createCenterPanel(), BorderLayout.CENTER);
        super.add(status, BorderLayout.SOUTH);
        this.appPanel = appPanel;
        registerKeyEvents();
    }

    private void registerKeyEvents() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent event) {
                KeyEvent ke = (KeyEvent) event;
                if (ke.getID() != KeyEvent.KEY_PRESSED) return;
                try {
                    int keyCode = ke.getKeyCode();
                    int keyMod = ke.getModifiers();
                    if ((keyMod & KeyEvent.CTRL_MASK) != 0 && keyCode == KeyEvent.VK_S) {
                        appPanel.showSendMailPanel();
                        start();
                    }
                    if ((keyMod & KeyEvent.CTRL_MASK) != 0 && keyCode == KeyEvent.VK_Q) {
                        appPanel.showSendMailPanel();
                        stop();
                    } else return;
                    ke.consume();
                } catch (SQLException e) {
                    App.log.log(Level.SEVERE, e.getMessage(), e);
                } catch (AddressException e) {
                    App.log.log(Level.SEVERE, e.getMessage(), e);
                } catch (MessagingException e) {
                    App.log.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    private JToolBar createToolbarPanel() {
        JToolBar bar = new JToolBar();
        bar.setRollover(true);
        startButton = new JButton();
        jHelper.setupButton(startButton, "email.action.start");
        bar.add(startButton);
        stopButton = new JButton();
        jHelper.setupButton(stopButton, "email.action.stop");
        bar.add(stopButton);
        return bar;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gcLabel = new GridBagConstraints();
        gcLabel.weightx = 0;
        gcLabel.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints gcComponent = new GridBagConstraints();
        gcComponent.fill = GridBagConstraints.HORIZONTAL;
        gcComponent.weightx = 0;
        {
            gcLabel.gridx = 0;
            gcLabel.gridy = 0;
            gcLabel.insets = new Insets(30, 30, 2, 2);
            JLabel label = new JLabel(locale.getProperty("email.template"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcLabel.insets = new Insets(2, 30, 2, 2);
            gcComponent.gridx = 1;
            gcComponent.gridy = 0;
            gcComponent.insets = new Insets(30, 2, 2, 30);
            label.setLabelFor(templateCombo);
            panel.add(templateCombo, gcComponent);
            gcComponent.insets = new Insets(2, 2, 2, 30);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.mailAccount"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            label.setLabelFor(mailAccountCombo);
            panel.add(mailAccountCombo, gcComponent);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.cc"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            ccEmailField = new JTextField();
            label.setLabelFor(ccEmailField);
            panel.add(ccEmailField, gcComponent);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.bcc"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            bccEmailField = new JTextField();
            label.setLabelFor(bccEmailField);
            panel.add(bccEmailField, gcComponent);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.msgDelete"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            msgPurgeDays = new JTextField();
            label.setLabelFor(msgPurgeDays);
            panel.add(msgPurgeDays, gcComponent);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.msgDays"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            msgDays = new JTextField();
            label.setLabelFor(msgDays);
            panel.add(msgDays, gcComponent);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.msgRead"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            msgReadOnly = new JCheckBox();
            label.setLabelFor(msgReadOnly);
            panel.add(msgReadOnly, gcComponent);
        }
        {
            gcLabel.gridx = 0;
            gcLabel.gridy += 1;
            JLabel label = new JLabel(locale.getProperty("email.shutdownAfterSend"), JLabel.TRAILING);
            panel.add(label, gcLabel);
            gcComponent.gridx = 1;
            gcComponent.gridy += 1;
            shutdownAfterSend = new JCheckBox();
            label.setLabelFor(shutdownAfterSend);
            panel.add(shutdownAfterSend, gcComponent);
        }
        return panel;
    }

    private String ccEmailString = "";

    private String bccEmailString = "";

    private String msgDaysString = "";

    private String msgPurgeDaysString = "";

    private String msgReadString = "";

    private String shutdownAfterSendString = "";

    public void populate() throws SQLException {
        String indexStr = dao.preference.getPreference(MAIL_ACCOUNT_INDEX_KEY);
        if (indexStr != null) {
            int index = Integer.parseInt(indexStr);
            int itemCount = mailAccountCombo.getItemCount();
            if (index < itemCount) mailAccountCombo.setSelectedIndex(index);
        }
        indexStr = dao.preference.getPreference(TEMPLATE_INDEX_KEY);
        if (indexStr != null) {
            int index = Integer.parseInt(indexStr);
            int itemCount = templateCombo.getItemCount();
            if (index < itemCount) templateCombo.setSelectedIndex(index);
        }
        templateCombo.addActionListener(this);
        mailAccountCombo.addActionListener(this);
        {
            ccEmailString = dao.preference.getPreference(EMAIL_CC);
            if (ccEmailString == null) ccEmailString = "";
            ccEmailField.setText(ccEmailString);
            ccEmailField.addFocusListener(this);
        }
        {
            bccEmailString = dao.preference.getPreference(EMAIL_BCC);
            if (bccEmailString == null) bccEmailString = "";
            bccEmailField.setText(bccEmailString);
            bccEmailField.addFocusListener(this);
        }
        {
            msgPurgeDaysString = dao.preference.getPreference(MESSAGE_PURGE_DAYS_OLD);
            if (msgPurgeDaysString == null) msgPurgeDaysString = "-1";
            msgPurgeDays.setText(msgPurgeDaysString);
            msgPurgeDays.addFocusListener(this);
        }
        {
            msgDaysString = dao.preference.getPreference(MESSAGE_DAYS_OLD);
            if (msgDaysString == null) msgDaysString = "-1";
            msgDays.setText(msgDaysString);
            msgDays.addFocusListener(this);
        }
        {
            msgReadString = dao.preference.getPreference(MESSAGE_SEND_READ_ONLY);
            if (msgReadString == null) msgReadString = "Y";
            msgReadOnly.setSelected("Y".equals(msgReadString));
            msgReadOnly.addFocusListener(this);
        }
        {
            shutdownAfterSendString = dao.preference.getPreference(SHUTDOWN_AFTER_SEND);
            if (shutdownAfterSendString == null) shutdownAfterSendString = "N";
            shutdownAfterSend.setSelected("Y".equals(shutdownAfterSendString));
            shutdownAfterSend.addFocusListener(this);
        }
    }

    public void focusLost(FocusEvent e) {
        saveChanges();
    }

    private void saveChanges() {
        String text = ccEmailField.getText();
        if (!text.equals(ccEmailString)) {
            try {
                dao.preference.setPreference(EMAIL_CC, text);
            } catch (SQLException ee) {
                App.log.log(Level.SEVERE, ee.getMessage(), ee);
            }
            ccEmailString = text;
        }
        text = bccEmailField.getText();
        if (!text.equals(bccEmailString)) {
            try {
                dao.preference.setPreference(EMAIL_BCC, text);
            } catch (SQLException ee) {
                App.log.log(Level.SEVERE, ee.getMessage(), ee);
            }
            bccEmailString = text;
        }
        text = msgPurgeDays.getText();
        if (!text.equals(msgPurgeDaysString)) {
            try {
                dao.preference.setPreference(MESSAGE_PURGE_DAYS_OLD, text);
            } catch (SQLException ee) {
                App.log.log(Level.SEVERE, ee.getMessage(), ee);
            }
            msgPurgeDaysString = text;
        }
        text = msgDays.getText();
        if (!text.equals(msgDaysString)) {
            try {
                dao.preference.setPreference(MESSAGE_DAYS_OLD, text);
            } catch (SQLException ee) {
                App.log.log(Level.SEVERE, ee.getMessage(), ee);
            }
            msgDaysString = text;
        }
        text = msgReadOnly.isSelected() ? "Y" : "N";
        if (!text.equals(msgReadString)) {
            try {
                dao.preference.setPreference(MESSAGE_SEND_READ_ONLY, text);
            } catch (SQLException ee) {
                App.log.log(Level.SEVERE, ee.getMessage(), ee);
            }
            msgReadString = text;
        }
        text = shutdownAfterSend.isSelected() ? "Y" : "N";
        if (!text.equals(shutdownAfterSendString)) {
            try {
                dao.preference.setPreference(SHUTDOWN_AFTER_SEND, text);
            } catch (SQLException ee) {
                App.log.log(Level.SEVERE, ee.getMessage(), ee);
            }
            shutdownAfterSendString = text;
        }
    }

    public void focusGained(FocusEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == templateCombo) {
                dao.preference.setPreference(TEMPLATE_INDEX_KEY, String.valueOf(templateCombo.getSelectedIndex()));
                return;
            }
            if (source == mailAccountCombo) {
                dao.preference.setPreference(MAIL_ACCOUNT_INDEX_KEY, String.valueOf(mailAccountCombo.getSelectedIndex()));
                return;
            }
        } catch (SQLException ex) {
            App.log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * @return parent for use in creating pop-up dialogs
     */
    private Component parent() {
        if (parentComponent == null) parentComponent = getParent().getParent();
        return parentComponent;
    }

    /**
     * Called by JComponentHelper upon action event
     * @param value
     * @throws SQLException
     */
    public void start() throws SQLException, AddressException, MessagingException {
        if (running()) return;
        updateStatus(STAT_ALL, "");
        TemplateVO template = (TemplateVO) templateCombo.getSelectedItem();
        if (template == null) {
            JOptionPane.showConfirmDialog(parent(), locale.getProperty("email.no.template"), locale.getProperty("email.dialog.title"), JOptionPane.OK_OPTION);
            return;
        }
        MailAccountVO mailAccount = (MailAccountVO) mailAccountCombo.getSelectedItem();
        if (mailAccount == null) {
            JOptionPane.showConfirmDialog(parent(), locale.getProperty("email.no.sendMailAccount"), locale.getProperty("email.dialog.title"), JOptionPane.OK_OPTION);
            return;
        }
        String daysStr = msgDays.getText();
        int daysOld;
        try {
            daysOld = Integer.parseInt(daysStr);
        } catch (NumberFormatException e) {
            daysOld = -2;
        }
        if (daysOld == -2) {
            updateStatus(STAT_ALL, locale.getProperty("msg.mindays.needed"));
            return;
        }
        List junkIdList = dao.junkMail.getJunkIds(daysOld, msgReadOnly.isSelected());
        if (junkIdList.size() == 0) {
            updateStatus(STAT_ALL, locale.getProperty("msg.nothing.to.send"));
            return;
        }
        SMTPGuiAuthenticator auth = null;
        if (mailAccount.isUseAuth() && mailAccount.getPassword().length() == 0) {
            auth = new SMTPGuiAuthenticator(parent(), mailAccount.getUserName());
            if (!auth.promptPassword()) return;
        }
        updateStatus(STAT_ALL, locale.getProperty("email.start"));
        try {
            sendMail = new SendMail(junkIdList, dao, this, this, template, mailAccount, auth);
        } catch (AddressException e) {
            JOptionPane.showConfirmDialog(parent(), e.getMessage(), locale.getProperty("exception.title"), JOptionPane.CLOSED_OPTION);
            throw e;
        }
        sendMail.setMessageListener(messageListener);
        sendMail.setCarbonCopyEmail(ccEmailField.getText());
        sendMail.setBlindCarbonCopyEmail(bccEmailField.getText());
        sendThread = new Thread(sendMail);
        sendThread.start();
        new Thread() {

            public void run() {
                try {
                    sendThread.join();
                    if (shutdownAfterSend.isSelected()) appPanel.shutdownEvent();
                } catch (InterruptedException e) {
                    App.log.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }.start();
    }

    private boolean running() {
        return sendThread != null && !sendThread.isInterrupted() && sendThread.isAlive();
    }

    /**
     * Smart status bar, keeps track of more than one item at a time, an update
     * on any type re-prints all status types until all are cleared.
     * 
     * @param statusType
     * @param msg
     */
    private void updateStatus(int statusType, String msg) {
        if (statusType == STAT_ALL) {
            statList.clear();
            status.setText(msg);
            return;
        }
        while (statList.size() <= statusType) statList.add("");
        statList.set(statusType, msg);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < statList.size(); i++) {
            String s = (String) statList.get(i);
            if (s != null && !s.equals("")) {
                sb.append(statList.get(i));
                sb.append(", ");
            }
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        status.setText(sb.toString());
    }

    public void stop() {
        if (sendThread != null && sendThread.isAlive() && (stopThread == null || !stopThread.isAlive())) {
            updateStatus(STAT_START_STOP, locale.getProperty("email.stopping"));
            stopThread = new Thread() {

                public void run() {
                    sendMail.stop();
                    sendThread.interrupt();
                    try {
                        sendThread.join();
                        updateStatus(STAT_START_STOP, "");
                        stopThread = null;
                        sendThread = null;
                    } catch (InterruptedException e) {
                        App.log.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            };
            stopThread.start();
        }
    }

    /**
     * Re-throws exception to cancel message processing
     * 
     * @return <b>true</b> to mark message as processed
     */
    public boolean exceptionWhileProcessing(Exception exception) throws IOException {
        int result = JOptionPane.showConfirmDialog(parent(), locale.format("email.exception.processing", new Object[] { exception }), locale.getProperty("exception.title"), JOptionPane.YES_NO_CANCEL_OPTION);
        String logMsg = locale.format("email.exception.processing", new Object[] { exception }) + " => ";
        switch(result) {
            case JOptionPane.YES_OPTION:
                log(logMsg + "YES");
                break;
            case JOptionPane.NO_OPTION:
                log(logMsg + "NO");
                break;
            case JOptionPane.CANCEL_OPTION:
                log(logMsg + "CANCEL");
                break;
        }
        if (result == JOptionPane.CANCEL_OPTION) {
            if (exception instanceof IOException) throw (IOException) exception;
            throw new IOException(exception.toString());
        }
        return result == JOptionPane.YES_OPTION;
    }

    private static final void log(String s) {
        App.log.info("SendMailPanel: " + s);
    }

    public void exceptionStatus(Exception exception) {
        updateStatus(STAT_MSG_CONFIG, exception.getMessage());
    }

    public void exception(Exception exception) {
        JOptionPane.showMessageDialog(parent(), locale.format("email.exception", new Object[] { exception }), locale.getProperty("exception.title"), JOptionPane.CANCEL_OPTION);
    }

    private int sendListSize = 0;

    private int junkMsgCount = 0;

    private int sendMsgCount = 0;

    private int lastJunkId = -1;

    public void sendingMessage(int junkId, String emailAddresses) {
        if (lastJunkId != junkId) {
            junkMsgCount++;
            lastJunkId = junkId;
        }
        updateStatus(STAT_MSG_TRANSMIT, locale.format("email.sendingMessage", new Object[] { new Integer(junkMsgCount), new Integer(sendListSize), new Integer(junkId), new Integer(sendMsgCount), emailAddresses }));
    }

    public void sendingMessages(int listSize) {
        sendListSize = listSize;
        junkMsgCount = 0;
        sendMsgCount = 0;
    }

    public void sendingMessagesDone(int successMsgsSent) {
        updateStatus(STAT_START_STOP, "");
        updateStatus(STAT_MSG_TRANSMIT, locale.format("email.sendingMessageDone", new Object[] { new Integer(junkMsgCount), new Integer(sendListSize), new Integer(successMsgsSent) }));
        sendListSize = 0;
        junkMsgCount = 0;
        sendMsgCount = 0;
        purge();
        sweep();
    }

    /**
     * Purge old messages from the database
     *
     */
    private void purge() {
        String daysPurgeStr = msgPurgeDays.getText();
        try {
            final int purgeAfterDays = Integer.parseInt(daysPurgeStr);
            if (purgeAfterDays < 0) return;
            updateStatus(STAT_PURGE, locale.getProperty("purging"));
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        int purged = dao.junkMail.deleteJunkAfter(purgeAfterDays);
                        updateStatus(STAT_PURGE, locale.format("purged", String.valueOf(purged)));
                    } catch (SQLException e) {
                        App.log.log(Level.SEVERE, e.getMessage(), e);
                        exception(e);
                    }
                }
            });
        } catch (NumberFormatException e) {
        }
    }

    private void sweep() {
        updateStatus(STAT_SWEEP, locale.getProperty("sweeping"));
        try {
            dao.sweepDatabase();
        } catch (SQLException e) {
            App.log.log(Level.SEVERE, e.getMessage(), e);
            exception(e);
        }
        SwingUtilities.invokeLater(new Thread() {

            public void run() {
                updateStatus(STAT_SWEEP, "");
            }
        });
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * TransportListener (event from JavaMail)
     */
    public void messageDelivered(TransportEvent e) {
        sendMsgCount++;
    }

    public void messageNotDelivered(TransportEvent e) {
    }

    public void messagePartiallyDelivered(TransportEvent e) {
    }

    /**
     * @return <b>true</b> to close the application
     * @throws SQLException
     */
    public boolean isClosing() throws SQLException {
        saveChanges();
        if (sendThread == null || !sendThread.isAlive()) return true;
        int result = JOptionPane.showConfirmDialog(parent(), locale.getProperty("email.running.app.closing"), locale.getProperty("app.closing"), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) return true; else if (result == JOptionPane.CANCEL_OPTION) return false;
        return true;
    }
}
