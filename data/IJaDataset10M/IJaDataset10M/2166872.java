package org.oboedit.gui.components;

import org.bbop.framework.AbstractGUIComponent;
import org.bbop.framework.ComponentManager;
import org.bbop.framework.GUIComponent;
import org.bbop.framework.GUIManager;
import org.bbop.framework.dock.LayoutAdapter;
import org.bbop.framework.dock.LayoutListener;
import org.bbop.io.FileUtil;
import org.bbop.io.IOUtil;
import org.bbop.swing.*;
import org.bbop.util.OSUtil;
import org.obo.datamodel.*;
import org.oboedit.gui.*;
import org.oboedit.gui.event.ReconfigEvent;
import org.oboedit.gui.widget.DbxrefListEditor;
import org.oboedit.util.GUIUtil;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import org.apache.log4j.*;

public class ConfigurationManager extends AbstractGUIComponent {

    protected static final Logger logger = Logger.getLogger(ConfigurationManager.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 683746976443349712L;

    ListEditor iconList;

    JTextField selectionBatchField;

    JTextField browserCommandField;

    JCheckBox useDefaultBrowserBox;

    JTextField userField;

    JTextField fullnameField;

    JTextField emailField;

    JCheckBox allowCyclesBox;

    JCheckBox warnBeforeDeleteBox;

    JCheckBox warnBeforeDefinitionLossBox;

    JCheckBox advancedRootDetectionBox;

    JCheckBox onlyOneGlobalOTECheckbox;

    JCheckBox autoCommitCheckBox;

    JCheckBox warnBeforeDiscardingEditsBox;

    JButton commitButton;

    JTabbedPane mainPanel;

    JTextField memoryField;

    JComboBox fontNameList;

    JComboBox fontSizeList;

    JComboBox fontTypeList;

    JTextArea fontPreviewArea;

    JCheckBox showUndefinedTermsBox;

    JCheckBox caseSensitiveSortBox;

    JCheckBox showToolTipsBox;

    JCheckBox excludeObsoletesFromSearchesBox;

    JCheckBox confirmOnExitBox;

    JCheckBox advxpMatrixEditorCheckBox;

    JCheckBox advIntersectionEditorCheckBox;

    JCheckBox autosaveEnabledCheckBox;

    JTextField autosavePathField;

    JTextField autosaveExpirationField;

    JTextField autosaveWaitField;

    DbxrefListEditor dbxrefEditor;

    DbxrefListEditor defDbxrefListEditor;

    ListEditor defDbxrefList;

    JCheckBox allowExtendedCheckbox;

    JTextArea defTextArea = new JTextArea();

    JCheckBox personalDefCheckbox = new JCheckBox("Use personal definition");

    JTextField logFilePath = new JTextField(Preferences.getPreferences().getLogfile());

    private Vector<IconWrapper> icons;

    private class IconWrapper {

        private String type;

        private String url;

        private Color color;

        public IconWrapper(String type, String url, Color color) {
            this.type = type;
            this.url = url;
            this.color = color;
        }

        public String getType() {
            return type;
        }

        public String getURL() {
            return url;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setURL(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return type;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    private class IconEditor extends JPanel implements GenericEditorComponent {

        private static final long serialVersionUID = 4386550408050571020L;

        JLabel typeLabel = new JLabel("Relationship type");

        JLabel urlLabel = new JLabel("Icon URL");

        JTextField typeField = new JTextField(10);

        JTextField urlField = new JTextField(50);

        JLabel previewLabel = new JLabel();

        JButton browseButton = new JButton("Browse files");

        JButton libraryButton = new JButton("Browse built-in icons");

        JButton colorButton = new JButton("Click to modify");

        protected ListEditor editor;

        public void setMasterComponent(Component c) {
            if (c instanceof ListEditor) editor = (ListEditor) c;
        }

        public IconEditor() {
            previewLabel.setVerticalAlignment(SwingConstants.CENTER);
            setMinimumSize(new Dimension(0, 0));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            urlField.setMaximumSize(new Dimension(Integer.MAX_VALUE, urlField.getPreferredSize().height));
            typeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, typeField.getPreferredSize().height));
            FocusListener listener = new FocusListener() {

                public void focusLost(FocusEvent e) {
                    update();
                    iconList.commit();
                }

                public void focusGained(FocusEvent e) {
                }
            };
            urlField.addFocusListener(listener);
            typeField.addFocusListener(listener);
            libraryButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showIconLibrary();
                }
            });
            browseButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showFileChooser();
                }
            });
            colorButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Color c = JColorChooser.showDialog(ConfigurationManager.this, "Select link color", colorButton.getForeground());
                    if (c != null) {
                        colorButton.setForeground(c);
                        colorButton.setText(ColorUtil.getName(c) + " (click to modify)");
                    }
                }
            });
            Box typeBox = new Box(BoxLayout.X_AXIS);
            typeBox.add(typeLabel);
            typeBox.add(Box.createHorizontalStrut(10));
            typeBox.add(Box.createHorizontalGlue());
            typeBox.add(typeField);
            typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, typeBox.getPreferredSize().height));
            Box urlLabelBox = new Box(BoxLayout.X_AXIS);
            urlLabelBox.add(urlLabel);
            urlLabelBox.add(Box.createHorizontalGlue());
            Box buttonBox = new Box(BoxLayout.X_AXIS);
            buttonBox.add(browseButton);
            buttonBox.add(libraryButton);
            buttonBox.add(Box.createHorizontalGlue());
            JPanel previewBox = new JPanel();
            previewBox.setLayout(new GridLayout(1, 1));
            previewBox.add(previewLabel);
            JPanel iconPanel = new JPanel();
            iconPanel.setLayout(new BorderLayout());
            JPanel fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
            fieldsPanel.add(urlLabelBox);
            fieldsPanel.add(urlField);
            fieldsPanel.add(Box.createVerticalStrut(5));
            fieldsPanel.add(buttonBox);
            iconPanel.add(fieldsPanel, "Center");
            iconPanel.add(previewBox, "East");
            iconPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) iconPanel.getPreferredSize().getHeight()));
            JPanel colorPanel = new JPanel();
            colorPanel.setLayout(new BorderLayout());
            Box colorLabelBox = Box.createHorizontalBox();
            colorLabelBox.add(new JLabel("Link color"));
            colorPanel.add(colorLabelBox, "Center");
            colorPanel.add(colorButton, "South");
            add(typeBox);
            add(Box.createVerticalStrut(10));
            add(iconPanel);
            add(colorPanel);
            add(Box.createVerticalGlue());
        }

        private void update() {
            Icon icon = null;
            try {
                if (urlField.getText().startsWith("resource:")) icon = Preferences.loadLibraryIcon(urlField.getText().substring(9)); else icon = Preferences.getIconForURL(new URL(urlField.getText()));
                validate();
            } catch (Exception e) {
                File file = new File(urlField.getText());
                if (file.exists()) try {
                    icon = Preferences.getIconForURL(file.toURL());
                } catch (MalformedURLException e1) {
                }
            }
            if (icon != null) previewLabel.setIcon(new ScaledIcon(icon, 60)); else previewLabel.setIcon(null);
        }

        public void load(Object o) {
            IconWrapper iw = (IconWrapper) o;
            urlField.setText(iw.getURL());
            typeField.setText(iw.getType());
            urlField.setCaretPosition(0);
            typeField.setCaretPosition(0);
            colorButton.setForeground(iw.getColor());
            colorButton.setText(ColorUtil.getName(iw.getColor()) + " (click to modify)");
            update();
        }

        public void store(Object o) {
            IconWrapper iw = (IconWrapper) o;
            iw.setURL(urlField.getText());
            iw.setType(typeField.getText());
            iw.setColor(colorButton.getForeground());
        }

        public Object createNewValue() {
            return new IconWrapper("UNKNOWN", "<new icon path>", Color.black);
        }

        private void showIconLibrary() {
            JPanel panel = new JPanel();
            panel.setBackground(Color.white);
            try {
                List<URL> icons = Preferences.getIconLibrary();
                final JDialog dialog = new JDialog();
                dialog.setTitle("Click an icon to select it");
                dialog.setModal(true);
                panel.setPreferredSize(new Dimension(565, 415));
                for (final URL url : icons) {
                    ActionListener listener = new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            File file = new File(url.getPath());
                            urlField.setText("resource:" + file.getName());
                            update();
                            dialog.dispose();
                        }
                    };
                    JButton button = new JButton(new ScaledIcon(Preferences.getIconForURL(url), 20));
                    button.setOpaque(false);
                    button.addActionListener(listener);
                    panel.add(button);
                }
                dialog.setContentPane(panel);
                dialog.pack();
                dialog.setVisible(true);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        private void showFileChooser() {
            SelectDialog dialog = SelectDialog.getFileSelector(SelectDialog.LOAD, System.getProperty("user.dir"));
            dialog.show();
            File selected = dialog.getSelected();
            if (selected != null) {
                try {
                    urlField.setText(selected.toURI().toURL().toString());
                    update();
                } catch (MalformedURLException e) {
                }
            }
        }
    }

    protected void selectListItem(JComboBox list, Object item) {
        list.removeItem(item);
        list.insertItemAt(item, 0);
        list.setSelectedIndex(0);
    }

    protected Font getChosenFont() {
        return GUIUtil.decodeFont((String) fontNameList.getSelectedItem(), (String) fontSizeList.getSelectedItem(), (String) fontTypeList.getSelectedItem());
    }

    public void buildFontPreview() {
        fontPreviewArea.setFont(getChosenFont());
        revalidate();
        repaint();
    }

    private class FontListener implements ActionListener, ItemListener {

        public void actionPerformed(ActionEvent e) {
            buildFontPreview();
        }

        public void itemStateChanged(ItemEvent e) {
            buildFontPreview();
        }
    }

    protected void formatField(JLabel label, JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) field.getPreferredSize().getHeight()));
        label.setMaximumSize(label.getPreferredSize());
    }

    protected void updatePersonalDefFields(boolean enabled) {
        defTextArea.setEnabled(enabled);
        defDbxrefList.setEnabled(enabled);
    }

    protected LayoutListener layoutListener = new LayoutAdapter() {

        public boolean closing(GUIComponent c) {
            return true;
        }
    };

    @Override
    public void init() {
        ComponentManager.getManager().addLayoutListener(layoutListener);
        removeAll();
        mainPanel = new JTabbedPane();
        setLayout(new BorderLayout());
        JLabel noIconLabel = new JLabel("Click a relationship name to edit its icon");
        icons = getIcons();
        commitButton = new JButton("Save Configuration");
        commitButton.setAlignmentX((float) .5);
        commitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        iconList = new ListEditor(new IconEditor(), noIconLabel, icons, true, true, true, true, false);
        iconList.setBorder(new TitledBorder("Relationship Type Icons"));
        iconList.setMinimumSize(new Dimension(0, 0));
        Box allowBox = new Box(BoxLayout.X_AXIS);
        Box warnDeleteBox = new Box(BoxLayout.X_AXIS);
        Box warnDefinitionBox = new Box(BoxLayout.X_AXIS);
        Box advancedRootBox = new Box(BoxLayout.X_AXIS);
        Box onlyOneGlobalOTEBox = new Box(BoxLayout.X_AXIS);
        Box autoCommitPanel = new Box(BoxLayout.X_AXIS);
        JLabel userLabel = new JLabel("User name", JLabel.TRAILING);
        JLabel fullnameLabel = new JLabel("Full Name", JLabel.TRAILING);
        JLabel emailLabel = new JLabel("Email Address", JLabel.TRAILING);
        JLabel selectionBatchLabel = new JLabel("Selection batch size", JLabel.TRAILING);
        JTextArea noDbxLabel = new JTextArea("Select a dbxref from the list " + "to edit it, or press 'Add' to " + "create a new dbxref");
        noDbxLabel.setMinimumSize(new Dimension(0, 0));
        noDbxLabel.setOpaque(false);
        noDbxLabel.setLineWrap(true);
        noDbxLabel.setWrapStyleWord(true);
        noDbxLabel.setEditable(false);
        noDbxLabel.setEnabled(false);
        final JLabel browserLabel = new JLabel("Browser command", JLabel.TRAILING);
        userField = new JTextField(20);
        fullnameField = new JTextField(20);
        emailField = new JTextField(20);
        selectionBatchField = new JTextField(3);
        browserCommandField = new JTextField(20);
        dbxrefEditor = new DbxrefListEditor(Dbxref.UNKNOWN);
        defDbxrefListEditor = new DbxrefListEditor(Dbxref.DEFINITION);
        defDbxrefList = new ListEditor(defDbxrefListEditor, noDbxLabel, new Vector<Object>(0), true, true, true, true, true);
        allowExtendedCheckbox = new JCheckBox("Allow extended characters (except in dbxrefs)");
        dbxrefEditor.setBorder(new TitledBorder("Personal Dbxref"));
        formatField(userLabel, userField);
        formatField(fullnameLabel, fullnameField);
        formatField(emailLabel, emailField);
        formatField(selectionBatchLabel, selectionBatchField);
        formatField(browserLabel, browserCommandField);
        useDefaultBrowserBox = new JCheckBox("Use default browser");
        useDefaultBrowserBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                browserCommandField.setEnabled(!useDefaultBrowserBox.isSelected());
                browserLabel.setEnabled(!useDefaultBrowserBox.isSelected());
            }
        });
        allowCyclesBox = new JCheckBox("Allow new cycles to be created");
        warnBeforeDeleteBox = new JCheckBox("Warn before final delete");
        warnBeforeDefinitionLossBox = new JCheckBox("Warn before discarding " + "definitions on save");
        autoCommitCheckBox = new JCheckBox("Autocommit text edits");
        autoCommitCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                warnBeforeDiscardingEditsBox.setEnabled(!autoCommitCheckBox.isSelected());
            }
        });
        warnBeforeDiscardingEditsBox = new JCheckBox("Warn before discarding text edits");
        advancedRootDetectionBox = new JCheckBox("Use advanced root " + "detection");
        onlyOneGlobalOTECheckbox = new JCheckBox("Only one Ontology Tree Editor at a time can be in global mode");
        showUndefinedTermsBox = new JCheckBox("Gray out undefined terms");
        caseSensitiveSortBox = new JCheckBox("Case-sensitive term sorting");
        showToolTipsBox = new JCheckBox("Show term IDs as tool tips in " + "term panels");
        excludeObsoletesFromSearchesBox = new JCheckBox("Exclude obsolete terms from search results [requires restart to change behavior]");
        excludeObsoletesFromSearchesBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Preferences.getPreferences().setExcludeObsoletesFromSearches(excludeObsoletesFromSearchesBox.isSelected());
            }
        });
        confirmOnExitBox = new JCheckBox("Confirm on exit, even if there are no changes to be saved");
        advxpMatrixEditorCheckBox = new JCheckBox("Cross-Product Matrix Editor");
        advIntersectionEditorCheckBox = new JCheckBox("Intersection Editor");
        autosaveEnabledCheckBox = new JCheckBox("Enable autosave");
        autosavePathField = new JTextField();
        autosaveExpirationField = new JTextField();
        autosaveWaitField = new JTextField();
        autosaveEnabledCheckBox.setOpaque(false);
        useDefaultBrowserBox.setOpaque(false);
        allowCyclesBox.setOpaque(false);
        warnBeforeDeleteBox.setOpaque(false);
        warnBeforeDefinitionLossBox.setOpaque(false);
        autoCommitCheckBox.setOpaque(false);
        warnBeforeDiscardingEditsBox.setOpaque(false);
        advancedRootDetectionBox.setOpaque(false);
        onlyOneGlobalOTECheckbox.setOpaque(false);
        showUndefinedTermsBox.setOpaque(false);
        caseSensitiveSortBox.setOpaque(false);
        showToolTipsBox.setOpaque(false);
        excludeObsoletesFromSearchesBox.setOpaque(false);
        confirmOnExitBox.setOpaque(false);
        advxpMatrixEditorCheckBox.setOpaque(false);
        advIntersectionEditorCheckBox.setOpaque(false);
        useDefaultBrowserBox.setSelected(Preferences.getPreferences().getBrowserCommand().length() == 0);
        browserCommandField.setEnabled(!useDefaultBrowserBox.isSelected());
        browserLabel.setEnabled(!useDefaultBrowserBox.isSelected());
        autosaveEnabledCheckBox.setSelected(Preferences.getPreferences().getAutosaveEnabled());
        dbxrefEditor.load(Preferences.getPreferences().getPersonalDbxref());
        autosavePathField.setText(Preferences.getPreferences().getAutosavePath().toString());
        autosaveExpirationField.setText(Preferences.getPreferences().getAutosaveExpirationDays() + "");
        autosaveWaitField.setText(Preferences.getPreferences().getAutosaveWaitTime() + "");
        userField.setText(Preferences.getPreferences().getUserName());
        fullnameField.setText(Preferences.getPreferences().getFullName());
        emailField.setText(Preferences.getPreferences().getEmail());
        selectionBatchField.setText(Preferences.getPreferences().getSelectionBatchSize() + "");
        browserCommandField.setText(Preferences.getPreferences().getBrowserCommand() + "");
        allowCyclesBox.setSelected(Preferences.getPreferences().getAllowCycles());
        allowExtendedCheckbox.setSelected(Preferences.getPreferences().getAllowExtendedCharacters());
        personalDefCheckbox.setSelected(Preferences.getPreferences().getUsePersonalDefinition());
        if (Preferences.getPreferences().getUsePersonalDefinition()) {
            defTextArea.setText(Preferences.getPreferences().getPersonalDefinition());
            Vector<Dbxref> v = new Vector<Dbxref>();
            v.addAll(Preferences.getPreferences().getPersonalDbxrefs());
            defDbxrefList.setData(v);
        } else {
            defTextArea.setText("");
            defDbxrefList.setData(new Vector<Object>());
        }
        updatePersonalDefFields(Preferences.getPreferences().getUsePersonalDefinition());
        autoCommitCheckBox.setSelected(Preferences.getPreferences().getAutoCommitTextEdits());
        warnBeforeDiscardingEditsBox.setSelected(Preferences.getPreferences().getWarnBeforeDiscardingEdits());
        warnBeforeDiscardingEditsBox.setEnabled(!autoCommitCheckBox.isSelected());
        warnBeforeDeleteBox.setSelected(Preferences.getPreferences().getWarnBeforeDelete());
        warnBeforeDefinitionLossBox.setSelected(Preferences.getPreferences().getWarnBeforeDefinitionLoss());
        advancedRootDetectionBox.setSelected(!Preferences.getPreferences().getUseBasicRootDetection());
        onlyOneGlobalOTECheckbox.setSelected(Preferences.getPreferences().getOnlyOneGlobalOTE());
        caseSensitiveSortBox.setSelected(Preferences.getPreferences().getCaseSensitiveSort());
        showToolTipsBox.setSelected(Preferences.getPreferences().getShowToolTips());
        excludeObsoletesFromSearchesBox.setSelected(Preferences.getPreferences().getExcludeObsoletesFromSearches());
        confirmOnExitBox.setSelected(Preferences.getPreferences().getConfirmOnExit());
        advxpMatrixEditorCheckBox.setSelected(Preferences.getPreferences().getadvMatrixEditorOptions());
        advIntersectionEditorCheckBox.setSelected(Preferences.getPreferences().getadvIntersectionEditorOptions());
        allowBox.add(allowCyclesBox);
        allowBox.add(Box.createHorizontalGlue());
        warnDeleteBox.add(warnBeforeDeleteBox);
        warnDeleteBox.add(Box.createHorizontalGlue());
        warnDefinitionBox.add(warnBeforeDefinitionLossBox);
        warnDefinitionBox.add(Box.createHorizontalGlue());
        advancedRootBox.add(advancedRootDetectionBox);
        advancedRootBox.add(Box.createHorizontalGlue());
        onlyOneGlobalOTEBox.add(onlyOneGlobalOTECheckbox);
        onlyOneGlobalOTEBox.add(Box.createHorizontalGlue());
        autoCommitPanel.add(autoCommitCheckBox);
        autoCommitPanel.add(Box.createHorizontalStrut(10));
        autoCommitPanel.add(warnBeforeDiscardingEditsBox);
        autoCommitPanel.add(Box.createHorizontalStrut(10));
        autoCommitPanel.add(allowExtendedCheckbox);
        autoCommitPanel.add(Box.createHorizontalGlue());
        Box autosaveEnabledBox = new Box(BoxLayout.X_AXIS);
        autosaveEnabledBox.add(autosaveEnabledCheckBox);
        autosaveEnabledBox.add(Box.createHorizontalGlue());
        JLabel autosavePathLabel = new JLabel("Directory for autosave");
        Box autosavePathBox = new Box(BoxLayout.X_AXIS);
        autosavePathBox.add(Box.createHorizontalStrut(20));
        autosavePathBox.add(autosavePathLabel);
        autosavePathBox.add(Box.createHorizontalStrut(10));
        autosavePathBox.add(Box.createHorizontalGlue());
        autosavePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, autosavePathField.getPreferredSize().height));
        autosavePathBox.add(autosavePathField);
        autosavePathBox.add(Box.createHorizontalStrut(5));
        JLabel autosaveWaitLabel = new JLabel("Do autosave every");
        JLabel minutesLabel = new JLabel("minutes");
        Box autosaveWaitBox = new Box(BoxLayout.X_AXIS);
        autosaveWaitBox.add(Box.createHorizontalStrut(20));
        autosaveWaitBox.add(autosaveWaitLabel);
        autosaveWaitBox.add(Box.createHorizontalStrut(10));
        autosaveWaitField.setMaximumSize(new Dimension(Integer.MAX_VALUE, autosaveWaitField.getPreferredSize().height));
        autosaveWaitBox.add(autosaveWaitField);
        autosaveWaitBox.add(Box.createHorizontalStrut(5));
        autosaveWaitBox.add(minutesLabel);
        autosaveWaitBox.add(Box.createHorizontalStrut(5));
        JLabel autosaveExpirationLabel = new JLabel("Autosave files expire in");
        Box autosaveExpirationBox = new Box(BoxLayout.X_AXIS);
        JLabel daysLabel = new JLabel("days");
        autosaveExpirationBox.add(Box.createHorizontalStrut(20));
        autosaveExpirationBox.add(autosaveExpirationLabel);
        autosaveExpirationBox.add(Box.createHorizontalStrut(10));
        autosaveExpirationBox.add(autosaveExpirationField);
        autosaveExpirationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, autosaveExpirationField.getPreferredSize().height));
        autosaveExpirationBox.add(Box.createHorizontalStrut(5));
        autosaveExpirationBox.add(daysLabel);
        autosaveExpirationBox.add(Box.createHorizontalStrut(5));
        Box configFileLabelBox = new Box(BoxLayout.X_AXIS);
        JLabel configFileLabel = new JLabel("User configuration directory");
        JTextField configFilePath = new JTextField(GUIManager.getPrefsDir().getPath());
        configFilePath.setEditable(false);
        configFilePath.setBackground(getBackground());
        configFilePath.setMaximumSize(new Dimension(Integer.MAX_VALUE, configFilePath.getPreferredSize().height));
        JButton removeConfigFiles = new JButton("Reset all configuration files (requires restart)");
        JButton updateSystemDicts = new JButton("Update system dictionaries");
        JButton backupUserDefDict = new JButton("Backup user-defined dictionary");
        removeConfigFiles.setAlignmentX((float) .5);
        removeConfigFiles.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeConfigFiles();
            }
        });
        updateSystemDicts.setAlignmentX((float) .5);
        updateSystemDicts.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateSystemDicts();
            }
        });
        backupUserDefDict.setAlignmentX((float) .5);
        backupUserDefDict.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    backupUserDefDict();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        configFileLabelBox.add(configFileLabel);
        configFileLabelBox.add(Box.createHorizontalStrut(5));
        configFileLabelBox.add(Box.createHorizontalGlue());
        configFileLabelBox.add(configFilePath);
        configFileLabelBox.add(Box.createHorizontalStrut(5));
        Box logFileBox = new Box(BoxLayout.X_AXIS);
        logFileBox.setOpaque(false);
        JLabel logFileLabel = new JLabel("Log file");
        logFilePath.setMaximumSize(new Dimension(Integer.MAX_VALUE, configFilePath.getPreferredSize().height));
        logFilePath.setEditable(false);
        logFilePath.setBackground(getBackground());
        logFileBox.add(logFileLabel);
        logFileBox.add(Box.createHorizontalStrut(5));
        logFileBox.add(Box.createHorizontalGlue());
        logFileBox.add(logFilePath);
        logFileBox.add(Box.createHorizontalStrut(5));
        Box fieldsPanel = new Box(BoxLayout.X_AXIS);
        fieldsPanel.setOpaque(false);
        fieldsPanel.add(userLabel);
        fieldsPanel.add(Box.createHorizontalStrut(5));
        fieldsPanel.add(Box.createHorizontalGlue());
        fieldsPanel.add(userField);
        fieldsPanel.add(Box.createHorizontalStrut(5));
        memoryField = new JTextField();
        memoryField.setText(Preferences.getPreferences().getMemString());
        JLabel memoryLabel = new JLabel("OBO-Edit Memory allocation ");
        Box memoryBox = new Box(BoxLayout.X_AXIS);
        memoryBox.add(memoryLabel);
        memoryBox.add(Box.createHorizontalStrut(5));
        memoryBox.add(memoryField);
        memoryBox.add(Box.createHorizontalStrut(5));
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(configFileLabelBox);
        userPanel.add(removeConfigFiles);
        userPanel.add(Box.createVerticalStrut(40));
        userPanel.add(updateSystemDicts);
        userPanel.add(Box.createVerticalStrut(40));
        userPanel.add(backupUserDefDict);
        userPanel.add(Box.createVerticalStrut(40));
        userPanel.add(logFileBox);
        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(memoryBox);
        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(fieldsPanel);
        userPanel.add(Box.createVerticalStrut(40));
        String[] sizes = { "6", "8", "10", "12", "14", "16", "18", "20", "24", "30", "36", "42", "48", "56", "64", "72", "80", "90", "100" };
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String[] fontStyles = { "Normal", "Italic", "Bold", "Bold-Italic" };
        fontNameList = new JComboBox(fonts);
        fontSizeList = new JComboBox(sizes);
        fontTypeList = new JComboBox(fontStyles);
        selectListItem(fontNameList, getDefaultFontName());
        fontNameList.setSelectedItem(getDefaultFontName());
        fontSizeList.setSelectedItem(getDefaultFontSize());
        fontTypeList.setSelectedItem(getDefaultFontStyle());
        fontPreviewArea = new JTextArea();
        fontPreviewArea.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" + "abcdefghijklmnopqrstuvwxyz");
        fontPreviewArea.setWrapStyleWord(true);
        fontPreviewArea.setLineWrap(true);
        fontPreviewArea.setEditable(false);
        fontPreviewArea.setOpaque(false);
        memoryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, memoryBox.getPreferredSize().height));
        JPanel autosavePanel = new JPanel();
        autosavePanel.setOpaque(false);
        autosavePanel.setLayout(new BoxLayout(autosavePanel, BoxLayout.Y_AXIS));
        autosavePanel.setBorder(new TitledBorder("Autosave Options"));
        autosavePanel.add(autosaveEnabledBox);
        autosavePanel.add(autosavePathBox);
        autosavePanel.add(autosaveWaitBox);
        autosavePanel.add(autosaveExpirationBox);
        JPanel guiPanel = new JPanel();
        guiPanel.setLayout(new BorderLayout());
        Box fontLine = new Box(BoxLayout.X_AXIS);
        fontLine.add(fontNameList);
        fontLine.add(fontSizeList);
        fontLine.add(fontTypeList);
        JLabel fontPreviewLabel = new JLabel("Font preview:");
        Box fontPreviewLabelLine = new Box(BoxLayout.X_AXIS);
        fontPreviewLabelLine.add(fontPreviewLabel);
        fontPreviewLabelLine.add(Box.createHorizontalGlue());
        JLabel fontLabel = new JLabel("Default font");
        Box fontAndPreviewBox = new Box(BoxLayout.Y_AXIS);
        fontAndPreviewBox.add(fontLine);
        fontAndPreviewBox.add(Box.createVerticalStrut(5));
        fontAndPreviewBox.add(fontPreviewLabelLine);
        fontAndPreviewBox.add(fontPreviewArea);
        Box fontLabelBox = new Box(BoxLayout.Y_AXIS);
        fontLabelBox.add(fontLabel);
        fontLabelBox.add(Box.createVerticalGlue());
        Box fontBox = new Box(BoxLayout.X_AXIS);
        fontBox.add(fontLabelBox);
        fontBox.add(Box.createHorizontalStrut(20));
        fontBox.add(fontAndPreviewBox);
        FontListener fontListener = new FontListener();
        fontNameList.addActionListener(fontListener);
        fontNameList.addItemListener(fontListener);
        fontSizeList.addActionListener(fontListener);
        fontSizeList.addItemListener(fontListener);
        fontTypeList.addActionListener(fontListener);
        fontTypeList.addItemListener(fontListener);
        JPanel moreOptionsPanel = new JPanel();
        moreOptionsPanel.setOpaque(false);
        moreOptionsPanel.setLayout(new BoxLayout(moreOptionsPanel, BoxLayout.Y_AXIS));
        moreOptionsPanel.setBorder(new TitledBorder("Startup Options (changes do not take effect until restart)"));
        moreOptionsPanel.add(Box.createVerticalStrut(10));
        moreOptionsPanel.add(fontBox);
        JPanel runtimeDisplayPanel = new JPanel();
        runtimeDisplayPanel.setOpaque(false);
        runtimeDisplayPanel.setLayout(new BoxLayout(runtimeDisplayPanel, BoxLayout.Y_AXIS));
        JPanel excludeObsoletesFromSearchesPanel = new JPanel();
        excludeObsoletesFromSearchesPanel.setOpaque(false);
        excludeObsoletesFromSearchesPanel.setLayout(new BoxLayout(excludeObsoletesFromSearchesPanel, BoxLayout.X_AXIS));
        excludeObsoletesFromSearchesPanel.add(excludeObsoletesFromSearchesBox);
        excludeObsoletesFromSearchesPanel.add(Box.createHorizontalGlue());
        JPanel caseSensitiveSortPanel = new JPanel();
        caseSensitiveSortPanel.setOpaque(false);
        caseSensitiveSortPanel.setLayout(new BoxLayout(caseSensitiveSortPanel, BoxLayout.X_AXIS));
        caseSensitiveSortPanel.add(caseSensitiveSortBox);
        caseSensitiveSortPanel.add(Box.createHorizontalGlue());
        JPanel showToolTipsPanel = new JPanel();
        showToolTipsPanel.setOpaque(false);
        showToolTipsPanel.setLayout(new BoxLayout(showToolTipsPanel, BoxLayout.X_AXIS));
        showToolTipsPanel.add(showToolTipsBox);
        showToolTipsPanel.add(Box.createHorizontalGlue());
        JPanel showConfirmOnExitPanel = new JPanel();
        showConfirmOnExitPanel.setOpaque(false);
        showConfirmOnExitPanel.setLayout(new BoxLayout(showConfirmOnExitPanel, BoxLayout.X_AXIS));
        showConfirmOnExitPanel.add(confirmOnExitBox);
        showConfirmOnExitPanel.add(Box.createHorizontalGlue());
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new GridLayout(1, 1));
        iconPanel.add(iconList);
        iconPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        guiPanel.add(moreOptionsPanel, "Center");
        JPanel behaviorPanel = new JPanel();
        behaviorPanel.setLayout(new BoxLayout(behaviorPanel, BoxLayout.Y_AXIS));
        behaviorPanel.add(excludeObsoletesFromSearchesPanel);
        behaviorPanel.add(caseSensitiveSortPanel);
        behaviorPanel.add(showToolTipsPanel);
        behaviorPanel.add(allowBox);
        behaviorPanel.add(advancedRootBox);
        behaviorPanel.add(onlyOneGlobalOTEBox);
        behaviorPanel.add(Box.createVerticalStrut(10));
        behaviorPanel.add(warnDeleteBox);
        behaviorPanel.add(warnDefinitionBox);
        behaviorPanel.add(showConfirmOnExitPanel);
        behaviorPanel.add(Box.createVerticalGlue());
        JScrollPane scroller = new JScrollPane(defTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        defTextArea.setLineWrap(true);
        defTextArea.setWrapStyleWord(true);
        personalDefCheckbox.setOpaque(false);
        JPanel textEditTopPanel = new JPanel();
        textEditTopPanel.setLayout(new BoxLayout(textEditTopPanel, BoxLayout.Y_AXIS));
        textEditTopPanel.setOpaque(false);
        textEditTopPanel.add(autoCommitPanel);
        JPanel textEditPanel = new JPanel();
        textEditPanel.setLayout(new BorderLayout());
        textEditPanel.setOpaque(true);
        JPanel personalDefinitionSubPanel = new JPanel();
        personalDefinitionSubPanel.setLayout(new GridLayout(1, 2));
        personalDefinitionSubPanel.add(scroller);
        personalDefinitionSubPanel.add(defDbxrefList);
        JPanel personalDefinitionPanel = new JPanel();
        personalDefinitionPanel.setLayout(new BorderLayout());
        personalDefinitionPanel.add(personalDefinitionSubPanel, "Center");
        personalDefinitionPanel.add(personalDefCheckbox, "North");
        personalDefCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updatePersonalDefFields(personalDefCheckbox.isSelected());
            }
        });
        personalDefinitionPanel.setOpaque(true);
        personalDefinitionSubPanel.setOpaque(true);
        textEditPanel.add(personalDefinitionPanel, "Center");
        textEditPanel.add(textEditTopPanel, "North");
        JPanel advancedPanel = new JPanel();
        advancedPanel.setLayout(new GridLayout(5, 1));
        JLabel advEditorsLabel = new JLabel("Editors");
        advancedPanel.add(advEditorsLabel);
        advancedPanel.add(advxpMatrixEditorCheckBox);
        advancedPanel.add(advIntersectionEditorCheckBox);
        mainPanel.addTab("User Settings", null, userPanel, "Set user options");
        mainPanel.addTab("Font", null, guiPanel, "Set default font");
        mainPanel.addTab("Icons & Colors", null, iconPanel, "Set up default colors and icons for relationship types");
        mainPanel.addTab("Autosave", null, autosavePanel, "Set autosave behavior");
        mainPanel.addTab("Behavior", null, behaviorPanel, "Set behavior options");
        mainPanel.addTab("Text Editing", null, textEditPanel, "Set text editing options");
        mainPanel.addTab("Enable Experimental Components", null, advancedPanel, "Advanced user options");
        mainPanel.setPreferredSize(new Dimension(800, 500));
        add(mainPanel, "Center");
        Box buttonBox = Box.createVerticalBox();
        buttonBox.add(Box.createVerticalStrut(15));
        buttonBox.add(commitButton);
        buttonBox.add(Box.createVerticalStrut(15));
        add(buttonBox, "South");
        buildFontPreview();
        setPreferredSize(new Dimension(800, 500));
    }

    private String getDefaultFontName() {
        return getFont().getFamily();
    }

    private String getDefaultFontSize() {
        return getFont().getSize() + "";
    }

    private String getDefaultFontStyle() {
        Font font = getFont();
        String style;
        if (font.isItalic() && font.isBold()) style = "Bold-Italic"; else if (font.isItalic()) style = "Italic"; else if (font.isBold()) style = "Bold"; else style = "Normal";
        return style;
    }

    private Vector<IconWrapper> getIcons() {
        Vector<IconWrapper> out = new Vector<IconWrapper>();
        for (String id : Preferences.getPreferences().getIconURLIndex().keySet()) {
            IconWrapper iw = new IconWrapper(id, Preferences.getPreferences().getIconURLIndex().get(id), Preferences.getPreferences().getColorForRelationshipType(id));
            out.addElement(iw);
        }
        return out;
    }

    protected void updateSystemDicts() {
        List<String> systemDictFiles = Preferences.getSystemDictFilenames();
        String confDir = Preferences.getOBOEditPrefsDir().toString();
        String files = "";
        for (String f : systemDictFiles) {
            String systemDictFile = confDir + "/" + f;
            files += "\n" + systemDictFile;
        }
        if (JOptionPane.showConfirmDialog(this, "The following standard system dictionary files will be replaced in " + confDir + ":" + files + "\nProceed?", "Update system dictionary files?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        String errors = "";
        for (String f : systemDictFiles) {
            File systemDictFile = new File(confDir + "/" + f);
            try {
                IOUtil.deltree(systemDictFile);
            } catch (Exception e) {
                errors = errors + ("Couldn't delete " + systemDictFile + "\n");
            }
        }
        try {
            FileUtil.ensureExists(Preferences.getStandardDictionaryFile(), "org/oboedit/resources/standard.dict");
            FileUtil.ensureExists(Preferences.getAllowedRepeatsFile(), "org/oboedit/resources/allowedrepeats.dict");
            FileUtil.ensureExists(Preferences.getAlwaysLowercaseFile(), "org/oboedit/resources/alwayslowercase.dict");
            FileUtil.ensureExists(Preferences.getPeriodWordsFile(), "org/oboedit/resources/periodwords.dict");
        } catch (IOException e) {
        }
    }

    protected void backupUserDefDict() throws IOException {
        String confDir = Preferences.getOBOEditPrefsDir().toString();
        File userDictFile = new File(confDir + "/dict/" + "user.dict");
        JFileChooser chooser = new JFileChooser(confDir);
        chooser.setDialogTitle("Save copy of user.dict");
        chooser.setSelectedFile(userDictFile);
        int returnVal = chooser.showSaveDialog(GUIManager.getManager().getFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dictfile = chooser.getSelectedFile();
            InputStream in = null;
            OutputStream out = null;
            in = new FileInputStream(userDictFile);
            out = new FileOutputStream(dictfile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /** Remove all of the user config files that control the look of OBO-Edit */
    protected void removeConfigFiles() {
        List<String> configFiles = Preferences.getPrefsFilenames();
        String confDir = Preferences.getOBOEditPrefsDir().toString();
        String files = "";
        for (String f : configFiles) {
            String configFile = confDir + "/" + f;
            files += "\n" + configFile;
        }
        if (JOptionPane.showConfirmDialog(this, "The following files and subdirectories will be deleted from " + confDir + ":" + files + "\n\nYou will then need to quit and restart OBO-Edit to reset your configuration.\nProceed?", "Delete your config files?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        String errors = "";
        for (String f : configFiles) {
            File configFile = new File(confDir + "/" + f);
            try {
                IOUtil.deltreeOnExit(configFile);
            } catch (Exception e) {
                errors = errors + ("Couldn't delete " + configFile + "\n");
            }
        }
        if (errors.equals("")) JOptionPane.showMessageDialog(this, "Configuration files removed.  You will need to quit and restart OBO-Edit\nto reset your configuration to the default settings."); else {
            JOptionPane.showMessageDialog(this, "Failed to delete some of the configuration files or directories:\n" + errors);
            logger.warn(errors);
        }
    }

    /** Save Configuration button event */
    public void save() {
        iconList.commit();
        defDbxrefList.commit();
        icons = iconList.getData();
        Preferences preferences = Preferences.getPreferences();
        try {
            int batchSize = Integer.parseInt(selectionBatchField.getText());
            preferences.setSelectionBatchSize(batchSize);
        } catch (NumberFormatException ex) {
        }
        if (useDefaultBrowserBox.isSelected()) preferences.setBrowserCommand(""); else preferences.setBrowserCommand(browserCommandField.getText());
        preferences.setUserName(userField.getText());
        preferences.setFullName(fullnameField.getText());
        preferences.setEmail(emailField.getText());
        preferences.setAllowCycles(allowCyclesBox.isSelected());
        preferences.setAutoCommitTextEdits(autoCommitCheckBox.isSelected());
        preferences.setWarnBeforeDiscardingEdits(warnBeforeDiscardingEditsBox.isSelected());
        preferences.setWarnBeforeDelete(warnBeforeDeleteBox.isSelected());
        preferences.setWarnBeforeDefinitionLoss(warnBeforeDefinitionLossBox.isSelected());
        preferences.setUseBasicRootDetection(!advancedRootDetectionBox.isSelected());
        preferences.setOnlyOneGlobalOTE(onlyOneGlobalOTECheckbox.isSelected());
        preferences.setAllowExtendedCharacters(allowExtendedCheckbox.isSelected());
        Dbxref ref = (Dbxref) dbxrefEditor.createNewValue();
        dbxrefEditor.store(ref);
        preferences.setPersonalDbxref(ref);
        preferences.setExcludeObsoletesFromSearches(excludeObsoletesFromSearchesBox.isSelected());
        preferences.setCaseSensitiveSort(caseSensitiveSortBox.isSelected());
        preferences.setShowToolTips(showToolTipsBox.isSelected());
        preferences.setConfirmOnExit(confirmOnExitBox.isSelected());
        preferences.setadvMatrixEditorOptions(advxpMatrixEditorCheckBox.isSelected());
        if (advxpMatrixEditorCheckBox.isSelected()) {
            GUIManager.getManager().setEnabledMenuItem("Editors:Cross-Product Matrix Editor", true);
        } else GUIManager.getManager().setEnabledMenuItem("Editors:Cross-Product Matrix Editor", false);
        preferences.setadvIntersectionEditorOptions(advIntersectionEditorCheckBox.isSelected());
        if (advIntersectionEditorCheckBox.isSelected()) {
            GUIManager.getManager().setEnabledMenuItem("Editors:Intersection Editor", true);
        } else GUIManager.getManager().setEnabledMenuItem("Editors:Intersection Editor", false);
        preferences.setAutosaveEnabled(autosaveEnabledCheckBox.isSelected());
        try {
            int waitTime = Integer.parseInt(autosaveWaitField.getText());
            preferences.setAutosaveWaitTime(waitTime);
        } catch (NumberFormatException ex) {
        }
        try {
            int days = Integer.parseInt(autosaveExpirationField.getText());
            preferences.setAutosaveExpirationDays(days);
        } catch (NumberFormatException ex) {
        }
        preferences.setAutosavePath(new File(autosavePathField.getText()));
        preferences.setFont(GUIUtil.decodeFont((String) fontNameList.getSelectedItem(), (String) fontSizeList.getSelectedItem(), (String) fontTypeList.getSelectedItem()));
        boolean isWindows = OSUtil.isWindows();
        String mem = memoryField.getText();
        if (mem == null || mem.toUpperCase().indexOf("M") < 1) {
            String newMem = isWindows ? "1024M" : "1860M";
            JOptionPane.showMessageDialog(GUIManager.getManager().getFrame(), "Error: illegal memory setting " + ((mem == null) ? "" : mem) + ".\nMemory setting must be some number of megabytes (M).\nSetting to " + newMem + ".");
            mem = newMem;
        }
        String numMem = mem.substring(0, mem.indexOf("M"));
        int intMem = Integer.parseInt(numMem);
        int memLimit = 1860;
        boolean useMaxMemory = is64BitJava();
        if (isWindows) {
            if (useMaxMemory) {
                memLimit = Integer.MAX_VALUE;
            } else {
                memLimit = 1024;
            }
        }
        if (OSUtil.isLinux() && useMaxMemory) {
            memLimit = getAvailableMaxMemoryLinux();
        }
        boolean isMacOSX = OSUtil.isMacOSX();
        boolean is64BitMacOS = false;
        if (isMacOSX) {
            Object[] macOSMemorySettings = getAvailableMaxMemoryMacOS(useMaxMemory);
            memLimit = (Integer) macOSMemorySettings[0];
            useMaxMemory = (Boolean) macOSMemorySettings[1];
            is64BitMacOS = (Boolean) macOSMemorySettings[2];
        }
        if (intMem > memLimit) {
            String message;
            if (useMaxMemory) {
                message = "Error -- you cannot allocate more memory for OBO-Edit than is physically available on your machine (" + memLimit + "M).";
            } else if (isMacOSX && !is64BitMacOS) {
                message = "<html>Error -- You cannot set the memory higher than " + memLimit + "M.<br>This is a precaution as your MacOS does not appear to be in 64-bit mode.<br>If you are <b>sure</b> that your Java supports more memory,you can try increasing<br>the memory allocation by editing the OBO-Edit.vmoptions file (and then<br>restarting OBO-Edit <i>twice</i>), but if you set the memory allocation too high,<br>OBO-Edit won't start.<br>For more details, see the Configuration Manager page in the user guide.</html>";
            } else {
                message = "Error -- Your current JVM (32-bit) does not support more than " + memLimit + "M of memory.";
            }
            JOptionPane.showMessageDialog(GUIManager.getManager().getFrame(), message, "Error in Memory Settings.", JOptionPane.ERROR_MESSAGE);
            mem = memLimit + "M";
        } else if (isWindows && intMem > 2048) {
            String message = "WARNING: You are attempting to set the memory for OBO-Edit to more than 2GByte.\nIf you set the memory allocation that high, OBO-Edit may not be able to launch.\nAre you sure you want to set it to " + mem + "?";
            int result = JOptionPane.showConfirmDialog(GUIManager.getManager().getFrame(), message, "Warning", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                mem = "2048M";
            }
        }
        preferences.setMemoryValue(mem);
        memoryField.setText(mem);
        Map<String, String> iconURLIndex = new HashMap<String, String>();
        Map<String, Color> colorIndex = new HashMap<String, Color>();
        for (int i = 0; i < icons.size(); i++) {
            IconWrapper iw = icons.elementAt(i);
            iconURLIndex.put(iw.getType(), iw.getURL());
            colorIndex.put(iw.getType(), iw.getColor());
        }
        preferences.setIconURLIndex(iconURLIndex);
        preferences.setColorIndex(colorIndex);
        preferences.setUsePersonalDefinition(personalDefCheckbox.isSelected());
        if (personalDefCheckbox.isSelected()) {
            preferences.setPersonalDefinition(defTextArea.getText());
            if (preferences.getPersonalDbxrefs() == null) preferences.setPersonalDbxrefs(new LinkedList<Dbxref>()); else preferences.getPersonalDbxrefs().clear();
            preferences.getPersonalDbxrefs().addAll(defDbxrefList.getData());
        } else {
            preferences.setPersonalDefinition(null);
            preferences.setPersonalDbxrefs(null);
        }
        JOptionPane.showMessageDialog(this, "Configuration saved.\n\nYou may need to quit and restart OBO-Edit\nfor your changes to take effect.");
        reload();
    }

    private static boolean is64BitJava() {
        String arch = OSUtil.getOSArch();
        return (arch != null) && (arch.contains("x86_64") || arch.contains("amd64"));
    }

    private static Object[] getAvailableMaxMemoryMacOS(boolean is64bitJava) {
        int maxMemory = 1860;
        boolean is64bitOS = false;
        try {
            String cmd = "uname -pm";
            Process child = Runtime.getRuntime().exec(cmd);
            java.io.InputStream in = child.getInputStream();
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                sb.append((char) c);
            }
            in.close();
            String uname = sb.toString().toLowerCase();
            is64bitOS = uname.contains("x86_64");
        } catch (java.io.IOException e) {
        }
        boolean useMaxMemory = is64bitOS && is64bitJava;
        if (useMaxMemory) {
            try {
                String cmd = "sysctl hw.memsize";
                Process child = Runtime.getRuntime().exec(cmd);
                java.io.InputStream in = child.getInputStream();
                StringBuilder sb = new StringBuilder();
                int c;
                while ((c = in.read()) != -1) {
                    sb.append((char) c);
                }
                in.close();
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("hw.memsize:\\s+(\\d+)\\s*");
                java.util.regex.Matcher matcher = pattern.matcher(sb);
                if (matcher.find()) {
                    maxMemory = new Long(Long.parseLong(matcher.group(1)) / 1024L / 1024L).intValue();
                }
            } catch (java.io.IOException e) {
            } catch (NumberFormatException e) {
            }
        }
        return new Object[] { Integer.valueOf(maxMemory), Boolean.valueOf(useMaxMemory), Boolean.valueOf(is64bitOS) };
    }

    private static int getAvailableMaxMemoryLinux() {
        int maxMemory = 4096;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/meminfo"));
            String s;
            Pattern pattern = Pattern.compile("MemTotal:\\s+(\\d+)\\s*kB");
            while ((s = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    int maxKB = Integer.parseInt(matcher.group(1));
                    maxMemory = maxKB / 1024;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            logger.info("Could not read max memory: " + e.getMessage());
        } catch (IOException e) {
            logger.info("Could not read max memory: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return maxMemory;
    }

    @Override
    public void cleanup() {
        ComponentManager.getManager().removeLayoutListener(layoutListener);
        super.cleanup();
    }

    public ConfigurationManager(String id) {
        super(id);
    }

    @Override
    public String getName() {
        return "OBO-Edit Configuration Manager";
    }

    public void reload() {
        Preferences.getPreferences().fireReconfigEvent(new ReconfigEvent(this));
    }
}
