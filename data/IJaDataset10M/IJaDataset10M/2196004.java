package ru.korusconsulting.connector.config;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import org.apache.commons.lang.StringUtils;
import ru.korusconsulting.connector.funambol.CalendarSyncSource;
import ru.korusconsulting.connector.funambol.ZimbraSyncSource;
import com.funambol.admin.AdminException;
import com.funambol.admin.ui.SourceManagementPanel;
import com.funambol.framework.engine.source.ContentType;
import com.funambol.framework.engine.source.SyncSourceInfo;

public class ContactSyncSourceConfigPanel extends SourceManagementPanel implements Serializable {

    public static final String NAME_ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_.";

    private static final String DEFAULT_ZIMBRA_URL = "https://<hostname>/service/soap/";

    private JLabel panelName = new JLabel();

    private TitledBorder titledBorder1;

    private JLabel nameLabel = new JLabel();

    private JTextField nameValue = new JTextField();

    private JLabel sourceURILabel = new JLabel();

    private JTextField sourceURIValue = new JTextField();

    private JLabel zimbraURLLabel = new JLabel();

    private JTextField zimbraURLValue = new JTextField();

    private JButton button = new JButton();

    private ZimbraSyncSource syncSource;

    private JCheckBox taskCheckBox = new JCheckBox();

    /**
     * 
     */
    public ContactSyncSourceConfigPanel() {
        init();
    }

    /**
     * 
     */
    private void init() {
        this.setLayout(null);
        titledBorder1 = new TitledBorder("");
        panelName.setFont(titlePanelFont);
        panelName.setText("Edit Zimbra SyncSource");
        panelName.setBounds(new Rectangle(14, 5, 316, 28));
        panelName.setAlignmentX(SwingConstants.CENTER);
        panelName.setBorder(titledBorder1);
        int y = 60;
        int dy = 30;
        sourceURILabel.setText("Source URI: ");
        sourceURILabel.setFont(defaultFont);
        sourceURILabel.setBounds(new Rectangle(14, y, 150, 18));
        sourceURIValue.setFont(defaultFont);
        sourceURIValue.setBounds(new Rectangle(170, y, 350, 18));
        y += dy;
        nameLabel.setText("Name: ");
        nameLabel.setFont(defaultFont);
        nameLabel.setBounds(new Rectangle(14, y, 150, 18));
        nameValue.setFont(defaultFont);
        nameValue.setBounds(new Rectangle(170, y, 350, 18));
        y += dy;
        zimbraURLLabel.setText("Zimbra URL: ");
        zimbraURLLabel.setFont(defaultFont);
        zimbraURLLabel.setBounds(new Rectangle(14, y, 150, 18));
        zimbraURLValue.setFont(defaultFont);
        zimbraURLValue.setBounds(new Rectangle(170, y, 350, 18));
        y += dy;
        taskCheckBox.setText("is Task? ");
        taskCheckBox.setFont(defaultFont);
        taskCheckBox.setBounds(new Rectangle(170, y, 150, 18));
        taskCheckBox.setEnabled(false);
        y += dy;
        button.setFont(defaultFont);
        button.setText("Add");
        button.setBounds(170, y, 70, 25);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    validateValues();
                    getValues();
                    if (getState() == STATE_INSERT) {
                        ContactSyncSourceConfigPanel.this.actionPerformed(new ActionEvent(ContactSyncSourceConfigPanel.this, ACTION_EVENT_INSERT, event.getActionCommand()));
                    } else {
                        ContactSyncSourceConfigPanel.this.actionPerformed(new ActionEvent(ContactSyncSourceConfigPanel.this, ACTION_EVENT_UPDATE, event.getActionCommand()));
                    }
                } catch (Throwable e) {
                    notifyError(new AdminException(e.getMessage()));
                }
            }
        });
        this.add(panelName, null);
        this.add(nameLabel, null);
        this.add(nameValue, null);
        this.add(sourceURILabel, null);
        this.add(sourceURIValue, null);
        this.add(zimbraURLLabel);
        this.add(zimbraURLValue);
        this.add(taskCheckBox);
        this.add(button, null);
    }

    /**
     * 
     */
    public void updateForm() {
        if (getState() == STATE_INSERT) {
            button.setText("Add");
        } else if (getState() == STATE_UPDATE) {
            button.setText("Save");
        }
        this.syncSource = (ZimbraSyncSource) getSyncSource();
        sourceURIValue.setText(syncSource.getSourceURI());
        nameValue.setText(syncSource.getName());
        zimbraURLValue.setText(syncSource.getZimbraUrl() == null ? DEFAULT_ZIMBRA_URL : syncSource.getZimbraUrl().toString());
        if (syncSource instanceof CalendarSyncSource) {
            taskCheckBox.setEnabled(true);
            taskCheckBox.setSelected(((CalendarSyncSource) syncSource).isTask());
        }
    }

    /**
     * Validate all values
     * @throws IllegalArgumentException
     */
    private void validateValues() throws IllegalArgumentException {
        String value = null;
        value = nameValue.getText();
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Field 'Name' cannot be empty. Please provide a SyncSource name.");
        }
        if (!StringUtils.containsOnly(value, NAME_ALLOWED_CHARS.toCharArray())) {
            throw new IllegalArgumentException("Only the following characters are allowed for field 'Name':\n" + NAME_ALLOWED_CHARS);
        }
        value = sourceURIValue.getText();
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Field 'Source URI' cannot be empty. Please provide a SyncSource URI.");
        }
        value = zimbraURLValue.getText();
        try {
            new URL(value);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Zimbra URL(" + value + "), message:" + e.getLocalizedMessage());
        }
    }

    /**
     * Fill ZimbraSyncSource value s
     */
    private void getValues() {
        ContentType[] contentTypes = null;
        ArrayList<ContentType> ctypeList = new ArrayList<ContentType>();
        syncSource.setSourceURI(sourceURIValue.getText().trim());
        syncSource.setName(nameValue.getText().trim());
        SyncSourceInfo info = syncSource.getInfo();
        if (info != null && info.getSupportedTypes().length > 0) {
            for (int i = 0; i < info.getSupportedTypes().length; i++) {
                ctypeList.add((info.getSupportedTypes())[i]);
            }
        }
        if (syncSource instanceof CalendarSyncSource) {
            if (!ctypeList.contains(new ContentType("text/x-vcalendar", "1.0"))) ctypeList.add(new ContentType("text/x-vcalendar", "1.0"));
            if (!ctypeList.contains(new ContentType("text/x-vcalendar", "2.0"))) ctypeList.add(new ContentType("text/x-vcalendar", "2.0"));
            if (!ctypeList.contains(new ContentType("text/calendar", "1.0"))) ctypeList.add(new ContentType("text/calendar", "1.0"));
            if (!ctypeList.contains(new ContentType("text/calendar", "2.0"))) ctypeList.add(new ContentType("text/calendar", "2.0"));
        } else {
            if (!ctypeList.contains(new ContentType("text/x-vcard", "2.1"))) ctypeList.add(new ContentType("text/x-vcard", "2.1"));
            if (!ctypeList.contains(new ContentType("text/vcard", "3.0"))) ctypeList.add(new ContentType("text/vcard", "3.0"));
            if (!ctypeList.contains(new ContentType("text/x-s4j-sifc", "1.0"))) ctypeList.add(new ContentType("text/x-s4j-sifc", "1.0"));
        }
        contentTypes = ctypeList.toArray(contentTypes);
        syncSource.setZimbraUrl(zimbraURLValue.getText());
        syncSource.setInfo(new SyncSourceInfo(contentTypes, 0));
        if (syncSource instanceof CalendarSyncSource) {
            ((CalendarSyncSource) syncSource).setTask(taskCheckBox.isSelected());
        }
    }
}
