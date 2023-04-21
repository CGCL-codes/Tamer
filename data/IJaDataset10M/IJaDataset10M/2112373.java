package frost.gui.preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import frost.*;
import frost.storage.*;
import frost.util.gui.translation.*;

/**
 * Main options frame.
 */
public class OptionsFrame extends JDialog implements ListSelectionListener {

    /**
     * A simple helper class to store JPanels and their name into a JList.
     */
    class ListBoxData {

        String name;

        JPanel panel;

        public ListBoxData(final String n, final JPanel p) {
            panel = p;
            name = n;
        }

        public JPanel getPanel() {
            return panel;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final Logger logger = Logger.getLogger(OptionsFrame.class.getName());

    private final SettingsClass frostSettings;

    private final Language language;

    private JPanel buttonPanel = null;

    private boolean checkBlock;

    private boolean checkBlockBody;

    private boolean checkHideBadMessages;

    private boolean checkHideCheckMessages;

    private boolean checkHideObserveMessages;

    private boolean checkHideJunkMessages;

    private String checkMaxMessageDisplay;

    private String checkMaxMessageDownload;

    private boolean checkSignedOnly;

    private boolean checkRememberSharedFileDownloaded;

    private boolean checkShowDeletedMessages;

    private boolean showColoredRows;

    private boolean checkShowOwnMessagesAsMeDisabled;

    private boolean checkIndicateLowReceivedMessages;

    private JPanel contentAreaPanel = null;

    private DisplayPanel displayPanel = null;

    private DisplayBoardTreePanel displayBoardTreePanel = null;

    private DisplayMessagesPanel displayMessagesPanel = null;

    private DownloadPanel downloadPanel = null;

    private boolean exitState;

    private JPanel mainPanel = null;

    private MiscPanel miscPanel = null;

    private NewsPanel newsPanel = null;

    private News2Panel news2Panel = null;

    private JunkPanel junkPanel = null;

    private ExpirationPanel expirationPanel = null;

    private JList optionsGroupsList = null;

    private JPanel optionsGroupsPanel = null;

    private SearchPanel searchPanel = null;

    private boolean shouldReloadMessages = false;

    private boolean shouldResetLastBackloadUpdateFinishedMillis = false;

    private boolean shouldResetSharedFilesLastDownloaded = false;

    private UploadPanel uploadPanel = null;

    private static int lastSelectedPanelIndex = 0;

    /**
     * Constructor, reads init file and inits the gui.
     * @param parent
     * @param settings
     */
    public OptionsFrame(final Frame parent, final SettingsClass settings) {
        super(parent);
        setModal(true);
        language = Language.getInstance();
        frostSettings = settings;
        setDataElements();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            Init();
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Exception thrown in constructor", e);
        }
        optionsGroupsList.setSelectedIndex(lastSelectedPanelIndex);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Close window and do not save settings
     */
    private void cancel() {
        exitState = false;
        dispose();
    }

    /**
     * cancelButton Action Listener (Cancel)
     * @param e
     */
    private void cancelButton_actionPerformed(final ActionEvent e) {
        cancel();
    }

    /**
     * Computes the maximum width and height of the various options panels.
     * Returns Dimension with max. x and y that is needed.
     * Gets all panels from the ListModel of the option groups list.
     * @param m
     * @return
     */
    protected Dimension computeMaxSize(final ListModel m) {
        if (m == null || m.getSize() == 0) {
            return null;
        }
        int maxX = -1;
        int maxY = -1;
        JDialog dlgdummy = new JDialog();
        for (int x = 0; x < m.getSize(); x++) {
            final ListBoxData lbdata = (ListBoxData) m.getElementAt(x);
            final JPanel aPanel = lbdata.getPanel();
            contentAreaPanel.removeAll();
            contentAreaPanel.add(aPanel, BorderLayout.CENTER);
            dlgdummy.setContentPane(contentAreaPanel);
            dlgdummy.pack();
            final int tmpX = contentAreaPanel.getWidth();
            final int tmpY = contentAreaPanel.getHeight();
            maxX = Math.max(maxX, tmpX);
            maxY = Math.max(maxY, tmpY);
        }
        dlgdummy = null;
        contentAreaPanel.removeAll();
        return new Dimension(maxX, maxY);
    }

    /**
     * Build the button panel.
     * @return
     */
    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            final JButton okButton = new JButton(language.getString("Common.ok"));
            final JButton cancelButton = new JButton(language.getString("Common.cancel"));
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
        }
        return buttonPanel;
    }

    private DisplayPanel getDisplayPanel() {
        if (displayPanel == null) {
            displayPanel = new DisplayPanel(this, frostSettings);
        }
        return displayPanel;
    }

    private DisplayBoardTreePanel getDisplayBoardTreePanel() {
        if (displayBoardTreePanel == null) {
            displayBoardTreePanel = new DisplayBoardTreePanel(this, frostSettings);
        }
        return displayBoardTreePanel;
    }

    private DisplayMessagesPanel getDisplayMessagesPanel() {
        if (displayMessagesPanel == null) {
            displayMessagesPanel = new DisplayMessagesPanel(this, frostSettings);
        }
        return displayMessagesPanel;
    }

    private DownloadPanel getDownloadPanel() {
        if (downloadPanel == null) {
            downloadPanel = new DownloadPanel(this, frostSettings);
        }
        return downloadPanel;
    }

    private MiscPanel getMiscPanel() {
        if (miscPanel == null) {
            miscPanel = new MiscPanel(frostSettings);
        }
        return miscPanel;
    }

    private News2Panel getNews2Panel() {
        if (news2Panel == null) {
            news2Panel = new News2Panel(frostSettings);
        }
        return news2Panel;
    }

    private JunkPanel getJunkPanel() {
        if (junkPanel == null) {
            junkPanel = new JunkPanel(frostSettings);
        }
        return junkPanel;
    }

    private ExpirationPanel getExpirationPanel() {
        if (expirationPanel == null) {
            expirationPanel = new ExpirationPanel(this, frostSettings);
        }
        return expirationPanel;
    }

    private NewsPanel getNewsPanel() {
        if (newsPanel == null) {
            newsPanel = new NewsPanel(frostSettings);
        }
        return newsPanel;
    }

    /**
     * Build the panel containing the list of option groups.
     * @return
     */
    protected JPanel getOptionsGroupsPanel() {
        if (optionsGroupsPanel == null) {
            final Vector<ListBoxData> listData = new Vector<ListBoxData>();
            listData.add(new ListBoxData(" " + language.getString("Options.downloads") + " ", getDownloadPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.uploads") + " ", getUploadPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.news") + " (1) ", getNewsPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.news") + " (2) ", getNews2Panel()));
            listData.add(new ListBoxData(" " + language.getString("Options.junk") + " ", getJunkPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.display") + " ", getDisplayPanel()));
            listData.add(new ListBoxData("    " + language.getString("Options.boardTree") + " ", getDisplayBoardTreePanel()));
            listData.add(new ListBoxData("    " + language.getString("Options.messages") + " ", getDisplayMessagesPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.expiration") + " ", getExpirationPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.search") + " ", getSearchPanel()));
            listData.add(new ListBoxData(" " + language.getString("Options.miscellaneous") + " ", getMiscPanel()));
            optionsGroupsList = new JList(listData);
            optionsGroupsList.setSelectionMode(DefaultListSelectionModel.SINGLE_INTERVAL_SELECTION);
            optionsGroupsList.addListSelectionListener(this);
            optionsGroupsPanel = new JPanel(new GridBagLayout());
            final GridBagConstraints constr = new GridBagConstraints();
            constr.anchor = GridBagConstraints.NORTHWEST;
            constr.fill = GridBagConstraints.BOTH;
            constr.weightx = 0.7;
            constr.weighty = 0.7;
            constr.insets = new Insets(5, 5, 5, 5);
            constr.gridx = 0;
            constr.gridy = 0;
            optionsGroupsPanel.add(optionsGroupsList, constr);
            optionsGroupsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));
        }
        return optionsGroupsPanel;
    }

    private SearchPanel getSearchPanel() {
        if (searchPanel == null) {
            searchPanel = new SearchPanel(frostSettings);
        }
        return searchPanel;
    }

    private UploadPanel getUploadPanel() {
        if (uploadPanel == null) {
            uploadPanel = new UploadPanel(frostSettings);
        }
        return uploadPanel;
    }

    /**
     * Build up the whole GUI.
     * @throws Exception
     */
    private void Init() throws Exception {
        this.setTitle(language.getString("Options.title"));
        this.setResizable(true);
        mainPanel = new JPanel(new BorderLayout());
        this.getContentPane().add(mainPanel, null);
        contentAreaPanel = new JPanel(new BorderLayout());
        contentAreaPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        contentAreaPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5), contentAreaPanel.getBorder()));
        mainPanel.add(getButtonPanel(), BorderLayout.SOUTH);
        mainPanel.add(getOptionsGroupsPanel(), BorderLayout.WEST);
        final Dimension neededSize = computeMaxSize(optionsGroupsList.getModel());
        contentAreaPanel.setMinimumSize(neededSize);
        contentAreaPanel.setPreferredSize(neededSize);
        mainPanel.add(contentAreaPanel, BorderLayout.CENTER);
    }

    /**
     * Close window and save settings
     */
    private void ok() {
        exitState = true;
        if (displayPanel != null) {
            displayPanel.ok();
        }
        if (displayBoardTreePanel != null) {
            displayBoardTreePanel.ok();
        }
        if (displayMessagesPanel != null) {
            displayMessagesPanel.ok();
        }
        if (downloadPanel != null) {
            downloadPanel.ok();
        }
        if (searchPanel != null) {
            searchPanel.ok();
        }
        if (uploadPanel != null) {
            uploadPanel.ok();
        }
        if (miscPanel != null) {
            miscPanel.ok();
        }
        if (newsPanel != null) {
            newsPanel.ok();
        }
        if (news2Panel != null) {
            news2Panel.ok();
        }
        if (junkPanel != null) {
            junkPanel.ok();
        }
        if (expirationPanel != null) {
            expirationPanel.ok();
        }
        saveSettings();
        dispose();
    }

    /**
     * okButton Action Listener (OK)
     */
    private void okButton_actionPerformed(final ActionEvent e) {
        ok();
    }

    /**
     * When window is about to close, do same as if CANCEL was pressed.
     * @see java.awt.Window#processWindowEvent(java.awt.event.WindowEvent)
     */
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    /**
     * Can be called to run dialog and get its answer (true=OK, false=CANCEL)
     * @return
     */
    public boolean runDialog() {
        exitState = false;
        setVisible(true);
        return exitState;
    }

    /**
     * Save settings
     */
    private void saveSettings() {
        try {
            frostSettings.exitSave();
        } catch (final StorageException se) {
            logger.log(Level.SEVERE, "Error while saving the settings.", se);
        }
        if (!checkMaxMessageDisplay.equals(frostSettings.getValue(SettingsClass.MAX_MESSAGE_DISPLAY)) || checkSignedOnly != frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_UNSIGNED) || checkHideBadMessages != frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_BAD) || checkHideCheckMessages != frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_CHECK) || checkHideObserveMessages != frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_OBSERVE) || checkHideJunkMessages != frostSettings.getBoolValue(SettingsClass.JUNK_HIDE_JUNK_MESSAGES) || checkBlock != frostSettings.getBoolValue(SettingsClass.MESSAGE_BLOCK_SUBJECT_ENABLED) || checkBlockBody != frostSettings.getBoolValue(SettingsClass.MESSAGE_BLOCK_BODY_ENABLED) || checkShowDeletedMessages != frostSettings.getBoolValue(SettingsClass.SHOW_DELETED_MESSAGES) || showColoredRows != frostSettings.getBoolValue(SettingsClass.SHOW_COLORED_ROWS) || checkShowOwnMessagesAsMeDisabled != frostSettings.getBoolValue(SettingsClass.SHOW_OWN_MESSAGES_AS_ME_DISABLED) || checkIndicateLowReceivedMessages != frostSettings.getBoolValue(SettingsClass.INDICATE_LOW_RECEIVED_MESSAGES)) {
            shouldReloadMessages = true;
        }
        if (!checkMaxMessageDownload.equals(frostSettings.getValue(SettingsClass.MAX_MESSAGE_DOWNLOAD))) {
            shouldResetLastBackloadUpdateFinishedMillis = true;
        }
        if (checkRememberSharedFileDownloaded == true && frostSettings.getBoolValue(SettingsClass.REMEMBER_SHAREDFILE_DOWNLOADED) == false) {
            shouldResetSharedFilesLastDownloaded = true;
        }
    }

    /**
     * Load settings
     */
    private void setDataElements() {
        checkMaxMessageDisplay = frostSettings.getValue(SettingsClass.MAX_MESSAGE_DISPLAY);
        checkMaxMessageDownload = frostSettings.getValue(SettingsClass.MAX_MESSAGE_DOWNLOAD);
        checkSignedOnly = frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_UNSIGNED);
        checkHideBadMessages = frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_BAD);
        checkHideCheckMessages = frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_CHECK);
        checkHideObserveMessages = frostSettings.getBoolValue(SettingsClass.MESSAGE_HIDE_OBSERVE);
        checkHideJunkMessages = frostSettings.getBoolValue(SettingsClass.JUNK_HIDE_JUNK_MESSAGES);
        checkBlock = frostSettings.getBoolValue(SettingsClass.MESSAGE_BLOCK_SUBJECT_ENABLED);
        checkBlockBody = frostSettings.getBoolValue(SettingsClass.MESSAGE_BLOCK_BODY_ENABLED);
        checkShowDeletedMessages = frostSettings.getBoolValue(SettingsClass.SHOW_DELETED_MESSAGES);
        checkShowOwnMessagesAsMeDisabled = frostSettings.getBoolValue(SettingsClass.SHOW_OWN_MESSAGES_AS_ME_DISABLED);
        checkIndicateLowReceivedMessages = frostSettings.getBoolValue(SettingsClass.INDICATE_LOW_RECEIVED_MESSAGES);
        checkRememberSharedFileDownloaded = frostSettings.getBoolValue(SettingsClass.REMEMBER_SHAREDFILE_DOWNLOADED);
        showColoredRows = frostSettings.getBoolValue(SettingsClass.SHOW_COLORED_ROWS);
    }

    /**
     * Is called after the dialog is hidden.
     * This method should return true if:
     *  - signedOnly, hideCheck or hideBad where changed by user
     *  - a block settings was changed by user
     * If it returns true, the messages table should be reloaded.
     * @return
     */
    public boolean shouldReloadMessages() {
        return shouldReloadMessages;
    }

    /**
     * @return  true if the LastBackloadUpdateFinishedMillis for all boards should be resetted (user changed maxDownloadDays)
     */
    public boolean shouldResetLastBackloadUpdateFinishedMillis() {
        return shouldResetLastBackloadUpdateFinishedMillis;
    }

    public boolean shouldResetSharedFilesLastDownloaded() {
        return shouldResetSharedFilesLastDownloaded;
    }

    /**
     * Implementing the ListSelectionListener.
     * Must change the content of contentAreaPanel to the selected
     * panel.
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        final JList theList = (JList) e.getSource();
        final Object Olbdata = theList.getSelectedValue();
        lastSelectedPanelIndex = theList.getSelectedIndex();
        contentAreaPanel.removeAll();
        if (Olbdata instanceof ListBoxData) {
            final ListBoxData lbdata = (ListBoxData) Olbdata;
            final JPanel newPanel = lbdata.getPanel();
            contentAreaPanel.add(newPanel, BorderLayout.CENTER);
            newPanel.revalidate();
            newPanel.repaint();
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                contentAreaPanel.revalidate();
            }
        });
    }
}
