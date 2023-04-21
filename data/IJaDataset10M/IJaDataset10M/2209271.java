package ch.skyguide.tools.requirement.hmi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import ch.skyguide.fdp.common.hmi.framework.IAccessorAdapter;
import ch.skyguide.fdp.common.hmi.framework.form.AbstractComponentWrapper;
import ch.skyguide.fdp.common.hmi.framework.form.DefaultComponentWrapperFactory;
import ch.skyguide.fdp.common.hmi.framework.form.FileComponentWrapper;
import ch.skyguide.fdp.common.hmi.framework.form.FormProxy;
import ch.skyguide.fdp.common.hmi.framework.form.FormProxyChangeEvent;
import ch.skyguide.fdp.common.hmi.framework.form.FormProxyFactory;
import ch.skyguide.fdp.common.hmi.framework.form.IFormProxy;
import ch.skyguide.fdp.common.hmi.framework.form.IFormProxyChangeListener;
import ch.skyguide.fdp.common.hmi.framework.form.PasswordComponentWrapper;
import ch.skyguide.fdp.common.hmi.framework.form.TextComponentWrapper;
import ch.skyguide.fdp.common.hmi.framework.form.URLComponentWrapper;
import ch.skyguide.fdp.common.hmi.input.MaximumCharacterCompletionController;
import ch.skyguide.fdp.common.hmi.util.FileChooserLongTermMemory;
import ch.skyguide.tools.requirement.data.IPreferences;
import ch.skyguide.tools.requirement.data.Preferences;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;
import ch.skyguide.tools.requirement.hmi.model.IssueListModel;
import com.dhb.introspection.AccessorManager;
import com.dhb.introspection.BeanManager;

@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog implements ActionListener, IFormProxyChangeListener {

    private static final String OPEN_OFFICE_RESOURCES = "label.OpenOfficeResources";

    private static final String MISCELLANEOUS = "label.Miscellaneous";

    private static final String OPEN_OFFICE_FOLDER = "label.OpenOfficeFolder";

    private static final String CLEAN_RECENT_FILES = "label.CleanRecentFiles";

    private static final String COLLAPSE_HISTORY_BY_VERSIONS = "label.CollapseHistoryByVersion";

    private static final String OPEN_OFFICE_DOCUMENT_TEMPLATE = "label.OpenOfficeDocumentTemplate";

    private static final String OPEN_OFFICE_REQUIREMENT_TEMPLATE = "label.OpenOfficeRequirementTemplate";

    private static final String OPEN_USER_REGISTRATION = "label.UserRegistration";

    private static final String JIRA_USER_REGISTRATION = "label.JiraRegistration";

    private static final String DISPLAY = "label.Display";

    private static final String DISPLAY_CODE = "label.DisplayCode";

    private static final String DISPLAY_HEADING = "label.DisplayHeading";

    private static final String CODE_DELIMITER = "label.CodeDelimiter";

    private static final String AUTOSAVE_DELAY = "label.AutosaveDelay";

    private static final String AUTOSAVE_UNIT = "label.AutosaveUnit";

    private static final String MESSAGE_ERROR = "error.message.console";

    private static final String EXPORT = "Export...";

    private static final String IMPORT = "Import...";

    private static final String ACCEPT = "OK";

    private static final String CLOSE = "Cancel";

    private static final GridBagConstraints CONSTRAINT_LABEL = new GridBagConstraints();

    private static final GridBagConstraints CONSTRAINT_FIELD = new GridBagConstraints();

    private static final GridBagConstraints CONSTRAINT_PLUGIN = new GridBagConstraints();

    private static final GridBagConstraints CONSTRAINT_RESOURCES = new GridBagConstraints();

    private static final GridBagConstraints CONSTRAINT_DISPLAY = new GridBagConstraints();

    private static final GridBagConstraints CONSTRAINT_SECTION = new GridBagConstraints();

    static {
        CONSTRAINT_LABEL.anchor = GridBagConstraints.WEST;
        CONSTRAINT_LABEL.insets.left = 3;
        CONSTRAINT_LABEL.insets.right = 5;
        CONSTRAINT_FIELD.fill = GridBagConstraints.HORIZONTAL;
        CONSTRAINT_FIELD.insets.right = 3;
        CONSTRAINT_FIELD.weightx = 1;
        CONSTRAINT_FIELD.gridwidth = GridBagConstraints.REMAINDER;
        CONSTRAINT_PLUGIN.fill = GridBagConstraints.BOTH;
        CONSTRAINT_PLUGIN.weightx = 1;
        CONSTRAINT_PLUGIN.weighty = 1;
        CONSTRAINT_PLUGIN.gridwidth = GridBagConstraints.REMAINDER;
        CONSTRAINT_RESOURCES.fill = GridBagConstraints.BOTH;
        CONSTRAINT_RESOURCES.weightx = 1;
        CONSTRAINT_SECTION.fill = GridBagConstraints.HORIZONTAL;
        CONSTRAINT_SECTION.weightx = 1;
        CONSTRAINT_SECTION.gridwidth = GridBagConstraints.REMAINDER;
        CONSTRAINT_DISPLAY.gridwidth = GridBagConstraints.REMAINDER;
        CONSTRAINT_DISPLAY.fill = GridBagConstraints.VERTICAL;
    }

    private static class ComponentFactory extends DefaultComponentWrapperFactory {

        private final JFileChooser templateChooser;

        public ComponentFactory() {
            templateChooser = new JFileChooser();
            new FileChooserLongTermMemory(getClass(), templateChooser);
            ExtensionFileFilter templateFileFilter = new ExtensionFileFilter("OpenOffice Template files", ".ott");
            templateChooser.setFileFilter(templateFileFilter);
        }

        @Override
        public <T> AbstractComponentWrapper<T> createComponentWrapper(FormProxy<T> proxy, IAccessorAdapter<T> adapter) {
            final AccessorManager<T> accessor = adapter.getAccessor();
            if ("OpenOfficeFolder".equals(adapter.getName())) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                return new FileComponentWrapper<T>(proxy, accessor, fileChooser);
            } else if ("DocumentTemplate".equals(adapter.getName())) {
                return new FileComponentWrapper<T>(proxy, accessor, templateChooser);
            } else if ("RequirementTemplate".equals(adapter.getName())) {
                return new FileComponentWrapper<T>(proxy, accessor, templateChooser);
            } else if ("DataDefinitionClassPath".equals(adapter.getName())) {
                return new URLComponentWrapper<T>(proxy, accessor);
            } else if (Preferences.JIRA_PASSWORD.equals(adapter.getName())) {
                return new PasswordComponentWrapper<T>(proxy, accessor);
            }
            return super.createComponentWrapper(proxy, adapter);
        }
    }

    private static class JiraConnectionChangeListener implements IFormProxyChangeListener {

        private boolean connectionChanged;

        boolean hasConnectionChanged() {
            return connectionChanged;
        }

        void reset() {
            connectionChanged = false;
        }

        public void propertyChange(FormProxyChangeEvent event) {
            connectionChanged |= Preferences.JIRA_URL.equals(event.getAccessorName()) || Preferences.JIRA_USERNAME.equals(event.getAccessorName()) || Preferences.JIRA_PASSWORD.equals(event.getAccessorName());
        }
    }

    private static final FormProxyFactory<Preferences> factory = new FormProxyFactory<Preferences>(new ComponentFactory(), new BeanManager<Preferences>(Preferences.class));

    private IFormProxy<Preferences> proxy;

    private JButton exportButton;

    private final RecentFileManager recentFileManager;

    private final JiraConnectionChangeListener jiraConnectionChangeListener = new JiraConnectionChangeListener();

    private final JButton saveButton = createSaveButton();

    public PreferencesDialog(RequirementTool tool) {
        super(tool, true);
        recentFileManager = tool.getRecentFileManager();
        try {
            proxy = factory.createProxy(Preferences.instance);
            proxy.addPropertyChangeListener(this);
            proxy.addPropertyChangeListener(jiraConnectionChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e, getTranslatedText(MESSAGE_ERROR), JOptionPane.ERROR_MESSAGE);
        }
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(createForm(), BorderLayout.CENTER);
        getContentPane().add(createButtonBar(), BorderLayout.SOUTH);
        pack();
        setTitle("Preferences");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final Rectangle ownerBounds = tool.getBounds();
        final int widthDelta = ownerBounds.width / 4;
        final int heightDelta = (ownerBounds.height - getHeight()) / 2;
        ownerBounds.x += widthDelta / 2;
        ownerBounds.y += heightDelta / 2;
        ownerBounds.width -= widthDelta;
        ownerBounds.height = getHeight();
        setBounds(ownerBounds);
        getRootPane().setDefaultButton(saveButton);
    }

    private String getTranslatedText(final String label) {
        return BeanManagerAndTableModelFactory.getInstance().getTranslatedText(label);
    }

    private JComponent createForm() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.add(createOpenOfficeResourcesPanel(), CONSTRAINT_RESOURCES);
        panel.add(createDisplayPanel(), CONSTRAINT_DISPLAY);
        panel.add(createUserPanel(), CONSTRAINT_RESOURCES);
        panel.add(createMiscellaneousPanel(), CONSTRAINT_DISPLAY);
        if (IssueListModel.hasIssueTrackerServices()) {
            panel.add(createJiraUserPanel(), CONSTRAINT_SECTION);
        }
        return panel;
    }

    private JComponent createMiscellaneousPanel() {
        final JPanel panel = new JPanel(new GridLayout(0, 1));
        setTitle(panel, MISCELLANEOUS);
        panel.add(createAutosaveLine());
        final JCheckBox recentFilesBox = (JCheckBox) proxy.getComponentAt("CleanRecentFiles");
        recentFilesBox.setText(getTranslatedText(CLEAN_RECENT_FILES));
        panel.add(recentFilesBox);
        final JCheckBox collapseByVersionBox = (JCheckBox) proxy.getComponentAt("CollapseHistoryByVersion");
        collapseByVersionBox.setText(getTranslatedText(COLLAPSE_HISTORY_BY_VERSIONS));
        panel.add(collapseByVersionBox);
        return panel;
    }

    private JPanel createAutosaveLine() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(getTranslatedText(AUTOSAVE_DELAY)), BorderLayout.WEST);
        final JTextField delayField = (JTextField) proxy.getComponentAt(Preferences.AUTOSAVE_DELAY);
        delayField.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("hint.Autosave"));
        TextComponentWrapper.setTextWidth(delayField, "MM");
        panel.add(delayField, BorderLayout.CENTER);
        panel.add(new JLabel(getTranslatedText(AUTOSAVE_UNIT)), BorderLayout.EAST);
        return panel;
    }

    private JComponent createOpenOfficeResourcesPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        setTitle(panel, OPEN_OFFICE_RESOURCES);
        panel.add(new JLabel(getTranslatedText(OPEN_OFFICE_FOLDER)), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt("OpenOfficeFolder"), CONSTRAINT_FIELD);
        panel.add(new JLabel(getTranslatedText(OPEN_OFFICE_DOCUMENT_TEMPLATE)), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt("DocumentTemplate"), CONSTRAINT_FIELD);
        panel.add(new JLabel(getTranslatedText(OPEN_OFFICE_REQUIREMENT_TEMPLATE)), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt("RequirementTemplate"), CONSTRAINT_FIELD);
        return panel;
    }

    private JComponent createDisplayPanel() {
        final JPanel panel = new JPanel(new GridLayout(0, 1));
        setTitle(panel, DISPLAY);
        JCheckBox box = (JCheckBox) proxy.getComponentAt("DisplayCode");
        box.setText(getTranslatedText(DISPLAY_CODE));
        panel.add(box);
        box = (JCheckBox) proxy.getComponentAt("DisplayHeading");
        box.setText(getTranslatedText(DISPLAY_HEADING));
        panel.add(box);
        box = (JCheckBox) proxy.getComponentAt(Preferences.NUMBERED_INDICES);
        box.setText(getTranslatedText(Preferences.NUMBERED_INDICES));
        panel.add(box);
        panel.add(createCodeDelimiterLine());
        return panel;
    }

    private JPanel createCodeDelimiterLine() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(getTranslatedText(CODE_DELIMITER)), BorderLayout.WEST);
        final JTextField delimiterField = (JTextField) proxy.getComponentAt(Preferences.CODE_DELIMITER);
        TextComponentWrapper.setTextWidth(delimiterField, "MM");
        new MaximumCharacterCompletionController(delimiterField, 1);
        panel.add(delimiterField, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createUserPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        setTitle(panel, OPEN_USER_REGISTRATION);
        panel.add(new JLabel(getTranslatedText(Preferences.FIRST_NAME)), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt(Preferences.FIRST_NAME), CONSTRAINT_FIELD);
        panel.add(new JLabel(getTranslatedText(Preferences.LAST_NAME)), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt(Preferences.LAST_NAME), CONSTRAINT_FIELD);
        panel.add(new JLabel(getTranslatedText(Preferences.DEPARTMENT)), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt(Preferences.DEPARTMENT), CONSTRAINT_FIELD);
        return panel;
    }

    private JComponent createJiraUserPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        setTitle(panel, JIRA_USER_REGISTRATION);
        panel.add(new JLabel(getTranslatedText("label.JIRA_URL")), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt(Preferences.JIRA_URL), CONSTRAINT_FIELD);
        panel.add(new JLabel(getTranslatedText("label.JIRA_USERNAME")), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt(Preferences.JIRA_USERNAME), CONSTRAINT_FIELD);
        panel.add(new JLabel(getTranslatedText("label.JIRA_PASSWORD")), CONSTRAINT_LABEL);
        panel.add(proxy.getComponentAt(Preferences.JIRA_PASSWORD), CONSTRAINT_FIELD);
        return panel;
    }

    private void setTitle(final JComponent panel, final String label) {
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0), BorderFactory.createTitledBorder(BorderFactory.createLineBorder(getForeground()), getTranslatedText(label))));
    }

    private Component createButtonBar() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(saveButton);
        panel.add(createCloseButton());
        return panel;
    }

    private JButton createSaveButton() {
        final JButton button = new JButton(UIManager.getString("OptionPane.okButtonText"));
        button.setActionCommand(ACCEPT);
        button.addActionListener(this);
        return button;
    }

    private JButton createCloseButton() {
        final JButton button = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
        RequirementTool.assignEscapeKey(button);
        button.setActionCommand(CLOSE);
        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof AbstractButton) {
            final AbstractButton button = (AbstractButton) event.getSource();
            if (ACCEPT.equals(button.getActionCommand())) {
                try {
                    proxy.storeObjectValues();
                    if (jiraConnectionChangeListener.hasConnectionChanged()) {
                        final RequirementTool tool = (RequirementTool) getOwner();
                        tool.connectToJira();
                    }
                    if (Preferences.instance.getCleanRecentFiles()) {
                        recentFileManager.reset();
                        Preferences.instance.setCleanRecentFiles(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, e, getTranslatedText(MESSAGE_ERROR), JOptionPane.ERROR_MESSAGE);
                }
            } else if (IMPORT.equals(button.getActionCommand())) {
                final JFileChooser chooser = new JFileChooser();
                final int answer = chooser.showOpenDialog(this);
                if (answer == JFileChooser.APPROVE_OPTION) {
                    Preferences.importPreferences(chooser.getSelectedFile());
                }
            } else if (EXPORT.equals(button.getActionCommand())) {
                final JFileChooser chooser = new JFileChooser();
                final int answer = chooser.showSaveDialog(this);
                if (answer == JFileChooser.APPROVE_OPTION) {
                    Preferences.exportPreferences(chooser.getSelectedFile());
                }
            }
            setVisible(false);
        }
    }

    public void propertyChange(FormProxyChangeEvent event) {
        final String accessorName = event.getAccessorName();
        if ("DisplayCode".equals(accessorName)) {
            final IPreferences preferences = (IPreferences) proxy;
            if (!(preferences.getDisplayCode() || preferences.getDisplayHeading())) {
                preferences.setDisplayHeading(true);
            }
        } else if ("DisplayHeading".equals(accessorName)) {
            final IPreferences preferences = (IPreferences) proxy;
            if (!(preferences.getDisplayCode() || preferences.getDisplayHeading())) {
                preferences.setDisplayCode(true);
            }
        } else if ("CodeDelimiter".equals(accessorName)) {
            final IPreferences preferences = (IPreferences) proxy;
            if (preferences.getCodeDelimiter() == null) {
                proxy.setError(accessorName);
            } else {
                proxy.clearError(accessorName);
            }
        }
        saveButton.setEnabled(!proxy.hasError());
        if (exportButton != null) {
            exportButton.setEnabled(false);
        }
    }
}
