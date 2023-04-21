package frost.gui.preferences;

import java.awt.*;
import javax.swing.*;
import frost.*;
import frost.fcp.*;
import frost.util.gui.*;
import frost.util.gui.translation.*;

class UploadPanel extends JPanel {

    private SettingsClass settings = null;

    private Language language = null;

    private final JLabel htlExplanationLabel = new JLabel();

    private final JLabel htlLabel = new JLabel();

    private final JTextField htlTextField = new JTextField(6);

    private final JLabel splitfileThreadsExplanationLabel = new JLabel();

    private final JLabel splitfileThreadsLabel = new JLabel();

    private final JTextField splitfileThreadsTextField = new JTextField(6);

    private final JLabel priorityLabel = new JLabel();

    private final JTextField priorityTextField = new JTextField(6);

    private final JCheckBox enforceFrostPriorityFileUpload = new JCheckBox();

    private final JLabel maxRetriesLabel = new JLabel();

    private final JTextField maxRetriesTextField = new JTextField(6);

    private final JLabel waitTimeLabel = new JLabel();

    private final JTextField waitTimeTextField = new JTextField(6);

    private final JLabel threadsLabel = new JLabel();

    private final JTextField threadsTextField = new JTextField(6);

    private final JCheckBox logUploadsCheckBox = new JCheckBox();

    /**
     * @param settings the SettingsClass instance that will be used to get and store the settings of the panel
     */
    protected UploadPanel(final SettingsClass settings) {
        super();
        this.language = Language.getInstance();
        this.settings = settings;
        initialize();
        loadSettings();
    }

    private void initialize() {
        setName("UploadPanel");
        setLayout(new GridBagLayout());
        refreshLanguage();
        new TextComponentClipboardMenu(htlTextField, language);
        new TextComponentClipboardMenu(splitfileThreadsTextField, language);
        new TextComponentClipboardMenu(threadsTextField, language);
        new TextComponentClipboardMenu(maxRetriesTextField, language);
        new TextComponentClipboardMenu(waitTimeTextField, language);
        final GridBagConstraints constraints = new GridBagConstraints();
        final Insets insets0555 = new Insets(0, 5, 5, 5);
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.insets = insets0555;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        add(maxRetriesLabel, constraints);
        constraints.gridx = 1;
        add(maxRetriesTextField, constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        add(waitTimeLabel, constraints);
        constraints.gridx = 1;
        add(waitTimeTextField, constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        add(threadsLabel, constraints);
        constraints.gridx = 1;
        add(threadsTextField, constraints);
        if (FcpHandler.isFreenet07()) {
            constraints.gridy++;
            constraints.gridx = 0;
            add(priorityLabel, constraints);
            constraints.gridx = 1;
            add(priorityTextField, constraints);
        } else {
            constraints.gridy++;
            constraints.gridx = 0;
            add(htlLabel, constraints);
            constraints.gridx = 1;
            add(htlTextField, constraints);
            constraints.gridx = 2;
            add(htlExplanationLabel, constraints);
            constraints.gridy++;
            constraints.gridx = 0;
            add(splitfileThreadsLabel, constraints);
            constraints.gridx = 1;
            add(splitfileThreadsTextField, constraints);
            constraints.gridx = 2;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0;
            add(splitfileThreadsExplanationLabel, constraints);
        }
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        add(logUploadsCheckBox, constraints);
        if (FcpHandler.isFreenet07()) {
            constraints.gridy++;
            constraints.gridx = 0;
            constraints.gridwidth = 3;
            add(enforceFrostPriorityFileUpload, constraints);
        }
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        add(new JLabel(""), constraints);
    }

    /**
     * Load the settings of this panel
     */
    private void loadSettings() {
        if (FcpHandler.isFreenet07()) {
            priorityTextField.setText(settings.getValue(SettingsClass.FCP2_DEFAULT_PRIO_FILE_UPLOAD));
            enforceFrostPriorityFileUpload.setSelected(settings.getBoolValue(SettingsClass.FCP2_ENFORCE_FROST_PRIO_FILE_UPLOAD));
        } else {
            htlTextField.setText(settings.getValue(SettingsClass.UPLOAD_FILE_HTL));
            splitfileThreadsTextField.setText(settings.getValue(SettingsClass.UPLOAD_MAX_SPLITFILE_THREADS));
        }
        threadsTextField.setText(settings.getValue(SettingsClass.UPLOAD_MAX_THREADS));
        maxRetriesTextField.setText("" + settings.getIntValue(SettingsClass.UPLOAD_MAX_RETRIES));
        waitTimeTextField.setText("" + settings.getIntValue(SettingsClass.UPLOAD_WAITTIME));
        logUploadsCheckBox.setSelected(settings.getBoolValue(SettingsClass.LOG_UPLOADS_ENABLED));
    }

    public void ok() {
        saveSettings();
    }

    private void refreshLanguage() {
        final String minutes = language.getString("Options.common.minutes");
        waitTimeLabel.setText(language.getString("Options.uploads.waittimeAfterEachTry") + " (" + minutes + ")");
        maxRetriesLabel.setText(language.getString("Options.uploads.maximumNumberOfRetries"));
        threadsLabel.setText(language.getString("Options.uploads.numberOfSimultaneousUploads") + " (3)");
        logUploadsCheckBox.setText(language.getString("Options.uploads.logUploads"));
        if (FcpHandler.isFreenet07()) {
            priorityLabel.setText(language.getString("Options.uploads.uploadPriority") + " (3)");
            enforceFrostPriorityFileUpload.setText(language.getString("Options.uploads.enforceFrostPriorityFileUpload"));
        } else {
            htlLabel.setText(language.getString("Options.uploads.uploadHtl") + " (21)");
            htlExplanationLabel.setText(language.getString("Options.uploads.uploadHtlExplanation"));
            splitfileThreadsLabel.setText(language.getString("Options.uploads.numberOfSplitfileThreads") + " (15)");
            splitfileThreadsExplanationLabel.setText(language.getString("Options.uploads.numberOfSplitfileThreadsExplanation"));
        }
    }

    /**
     * Save the settings of this panel
     */
    private void saveSettings() {
        if (FcpHandler.isFreenet07()) {
            settings.setValue(SettingsClass.FCP2_DEFAULT_PRIO_FILE_UPLOAD, priorityTextField.getText());
            settings.setValue(SettingsClass.FCP2_ENFORCE_FROST_PRIO_FILE_UPLOAD, enforceFrostPriorityFileUpload.isSelected());
        } else {
            settings.setValue(SettingsClass.UPLOAD_FILE_HTL, htlTextField.getText());
            settings.setValue(SettingsClass.UPLOAD_MAX_SPLITFILE_THREADS, splitfileThreadsTextField.getText());
        }
        settings.setValue(SettingsClass.UPLOAD_MAX_THREADS, threadsTextField.getText());
        settings.setValue(SettingsClass.UPLOAD_MAX_RETRIES, maxRetriesTextField.getText());
        settings.setValue(SettingsClass.UPLOAD_WAITTIME, waitTimeTextField.getText());
        settings.setValue(SettingsClass.LOG_UPLOADS_ENABLED, logUploadsCheckBox.isSelected());
    }
}
