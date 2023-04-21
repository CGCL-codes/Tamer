package pcgen.gui2.prefs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import pcgen.cdom.base.Constants;
import pcgen.core.Globals;
import pcgen.core.SettingsHandler;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;
import pcgen.gui2.tools.Utility;
import pcgen.gui2.util.JComboBoxEx;
import pcgen.system.LanguageBundle;

/**
 * The Class <code>OutputPanel</code> is responsible for 
 * displaying character output related preferences and allowing the 
 * preferences to be edited by the user.
 * 
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2011-02-24 18:41:00 -0500 (Thu, 24 Feb 2011) $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 14600 $
 */
@SuppressWarnings("serial")
public class OutputPanel extends PCGenPrefsPanel {

    private static String in_output = LanguageBundle.getString("in_Prefs_output");

    private static String in_pcgen = LanguageBundle.getString("in_Prefs_pcgen");

    private static String in_alwaysOverwrite = LanguageBundle.getString("in_Prefs_alwaysOverwrite");

    private static String in_invalidToHitText = LanguageBundle.getString("in_Prefs_invalidToHitText");

    private static String in_invalidDmgText = LanguageBundle.getString("in_Prefs_invalidDmgText");

    private static String in_outputSheetEqSet = LanguageBundle.getString("in_Prefs_templateEqSet");

    private static String in_paperType = LanguageBundle.getString("in_Prefs_paperType");

    private static String in_postExportCommandStandard = LanguageBundle.getString("in_Prefs_postExportCommandStandard");

    private static String in_postExportCommandPDF = LanguageBundle.getString("in_Prefs_postExportCommandPDF");

    private static String in_removeTemp = LanguageBundle.getString("in_Prefs_removeTemp");

    private static String in_saveOutputSheetWithPC = LanguageBundle.getString("in_Prefs_saveOutputSheetWithPC");

    private static String in_showSingleBoxPerBundle = LanguageBundle.getString("in_Prefs_showSingleBoxPerBundle");

    private static String in_weaponProfPrintout = LanguageBundle.getString("in_Prefs_weaponProfPrintout");

    private static String in_skillChoice = LanguageBundle.getString("in_Prefs_skillChoiceLabel");

    private static String in_skillChoiceNone = LanguageBundle.getString("in_Prefs_skillChoiceNone");

    private static String in_skillChoiceUntrained = LanguageBundle.getString("in_Prefs_skillChoiceUntrained");

    private static String in_skillChoiceAll = LanguageBundle.getString("in_Prefs_skillChoiceAll");

    private static String in_skillChoiceAsUI = LanguageBundle.getString("in_Prefs_skillChoiceAsUI");

    private static String in_choose = "...";

    private JCheckBox printSpellsWithPC = new JCheckBox();

    private JCheckBox removeTempFiles;

    private JCheckBox saveOutputSheetWithPC = new JCheckBox();

    private JCheckBox weaponProfPrintout;

    private JButton outputSheetEqSetButton;

    private JButton outputSheetHTMLDefaultButton;

    private JButton outputSheetPDFDefaultButton;

    private JButton outputSheetSpellsDefaultButton;

    private JTextField outputSheetEqSet;

    private JTextField outputSheetHTMLDefault;

    private JTextField outputSheetPDFDefault;

    private JTextField outputSheetSpellsDefault;

    private JComboBoxEx paperType = new JComboBoxEx();

    private JComboBoxEx skillChoice = new JComboBoxEx();

    private JTextField postExportCommandStandard;

    private JTextField postExportCommandPDF;

    private JTextField invalidToHitText;

    private JTextField invalidDmgText;

    private JCheckBox alwaysOverwrite;

    private JCheckBox showSingleBoxPerBundle;

    private String[] paperNames = null;

    private PrefsButtonListener prefsButtonHandler = new PrefsButtonListener();

    private final TextFocusLostListener textFieldListener = new TextFocusLostListener();

    /**
	 * Instantiates a new levelling up panel.
	 */
    public OutputPanel() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JLabel label;
        Border etched = null;
        TitledBorder title1 = BorderFactory.createTitledBorder(etched, in_output);
        title1.setTitleJustification(TitledBorder.LEFT);
        this.setBorder(title1);
        this.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(2, 2, 2, 2);
        Utility.buildConstraints(c, 0, 0, 1, 1, 0, 0);
        label = new JLabel(LanguageBundle.getString("in_Prefs_outputSheetHTMLDefault") + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 0, 1, 1, 1, 0);
        outputSheetHTMLDefault = new JTextField(String.valueOf(SettingsHandler.getSelectedCharacterHTMLOutputSheet(null)));
        outputSheetHTMLDefault.addFocusListener(textFieldListener);
        gridbag.setConstraints(outputSheetHTMLDefault, c);
        this.add(outputSheetHTMLDefault);
        Utility.buildConstraints(c, 2, 0, 1, 1, 0, 0);
        outputSheetHTMLDefaultButton = new JButton(in_choose);
        gridbag.setConstraints(outputSheetHTMLDefaultButton, c);
        this.add(outputSheetHTMLDefaultButton);
        outputSheetHTMLDefaultButton.addActionListener(prefsButtonHandler);
        Utility.buildConstraints(c, 0, 1, 1, 1, 0, 0);
        label = new JLabel(LanguageBundle.getString("in_Prefs_outputSheetPDFDefault") + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 1, 1, 1, 1, 0);
        outputSheetPDFDefault = new JTextField(String.valueOf(SettingsHandler.getSelectedCharacterPDFOutputSheet(null)));
        outputSheetPDFDefault.addFocusListener(textFieldListener);
        gridbag.setConstraints(outputSheetPDFDefault, c);
        this.add(outputSheetPDFDefault);
        Utility.buildConstraints(c, 2, 1, 1, 1, 0, 0);
        outputSheetPDFDefaultButton = new JButton(in_choose);
        gridbag.setConstraints(outputSheetPDFDefaultButton, c);
        this.add(outputSheetPDFDefaultButton);
        outputSheetPDFDefaultButton.addActionListener(prefsButtonHandler);
        Utility.buildConstraints(c, 0, 2, 1, 1, 0, 0);
        label = new JLabel(in_outputSheetEqSet + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 2, 1, 1, 0, 0);
        outputSheetEqSet = new JTextField(String.valueOf(SettingsHandler.getSelectedEqSetTemplate()));
        outputSheetEqSet.addFocusListener(textFieldListener);
        gridbag.setConstraints(outputSheetEqSet, c);
        this.add(outputSheetEqSet);
        Utility.buildConstraints(c, 2, 2, 1, 1, 0, 0);
        outputSheetEqSetButton = new JButton(in_choose);
        gridbag.setConstraints(outputSheetEqSetButton, c);
        this.add(outputSheetEqSetButton);
        outputSheetEqSetButton.addActionListener(prefsButtonHandler);
        Utility.buildConstraints(c, 0, 3, 1, 1, 0, 0);
        label = new JLabel(in_saveOutputSheetWithPC + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 3, 1, 1, 0, 0);
        gridbag.setConstraints(saveOutputSheetWithPC, c);
        this.add(saveOutputSheetWithPC);
        Utility.buildConstraints(c, 0, 4, 1, 1, 0, 0);
        label = new JLabel(LanguageBundle.getString("in_Prefs_outputSpellSheetDefault") + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 4, 1, 1, 0, 0);
        outputSheetSpellsDefault = new JTextField(String.valueOf(SettingsHandler.getSelectedSpellSheet()));
        outputSheetSpellsDefault.addFocusListener(textFieldListener);
        gridbag.setConstraints(outputSheetSpellsDefault, c);
        this.add(outputSheetSpellsDefault);
        Utility.buildConstraints(c, 2, 4, 1, 1, 0, 0);
        outputSheetSpellsDefaultButton = new JButton(in_choose);
        gridbag.setConstraints(outputSheetSpellsDefaultButton, c);
        this.add(outputSheetSpellsDefaultButton);
        outputSheetSpellsDefaultButton.addActionListener(prefsButtonHandler);
        Utility.buildConstraints(c, 0, 5, 1, 1, 0, 0);
        label = new JLabel(LanguageBundle.getString("in_Prefs_printSpellsWithPC") + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 5, 1, 1, 0, 0);
        gridbag.setConstraints(printSpellsWithPC, c);
        this.add(printSpellsWithPC);
        Utility.buildConstraints(c, 0, 6, 1, 1, 0, 0);
        label = new JLabel(in_paperType + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 6, 1, 1, 0, 0);
        final int paperCount = Globals.getPaperCount();
        paperNames = new String[paperCount];
        for (int i = 0; i < paperCount; ++i) {
            paperNames[i] = Globals.getPaperInfo(i, Constants.PAPERINFO_NAME);
        }
        paperType = new JComboBoxEx(paperNames);
        gridbag.setConstraints(paperType, c);
        this.add(paperType);
        Utility.buildConstraints(c, 0, 7, 3, 1, 0, 0);
        removeTempFiles = new JCheckBox(in_removeTemp, SettingsHandler.getCleanupTempFiles());
        gridbag.setConstraints(removeTempFiles, c);
        this.add(removeTempFiles);
        Utility.buildConstraints(c, 0, 8, 3, 1, 0, 0);
        weaponProfPrintout = new JCheckBox(in_weaponProfPrintout, SettingsHandler.getWeaponProfPrintout());
        gridbag.setConstraints(weaponProfPrintout, c);
        this.add(weaponProfPrintout);
        Utility.buildConstraints(c, 0, 9, 1, 1, 0, 0);
        label = new JLabel(in_postExportCommandStandard + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 9, 1, 1, 0, 0);
        postExportCommandStandard = new JTextField(String.valueOf(SettingsHandler.getPostExportCommandStandard()));
        gridbag.setConstraints(postExportCommandStandard, c);
        this.add(postExportCommandStandard);
        Utility.buildConstraints(c, 0, 10, 1, 1, 0, 0);
        label = new JLabel(in_postExportCommandPDF + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 10, 1, 1, 0, 0);
        postExportCommandPDF = new JTextField(String.valueOf(SettingsHandler.getPostExportCommandPDF()));
        gridbag.setConstraints(postExportCommandPDF, c);
        this.add(postExportCommandPDF);
        Utility.buildConstraints(c, 0, 11, 1, 1, 0, 0);
        label = new JLabel(in_skillChoice + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 11, 1, 1, 0, 0);
        skillChoice.setModel(new DefaultComboBoxModel(new String[] { in_skillChoiceNone, in_skillChoiceUntrained, in_skillChoiceAll, in_skillChoiceAsUI }));
        skillChoice.setSelectedIndex(SettingsHandler.getIncludeSkills());
        gridbag.setConstraints(skillChoice, c);
        this.add(skillChoice);
        Utility.buildConstraints(c, 0, 12, 1, 1, 0, 0);
        label = new JLabel(in_invalidToHitText + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 12, 1, 1, 0, 0);
        invalidToHitText = new JTextField(String.valueOf(SettingsHandler.getInvalidToHitText()));
        gridbag.setConstraints(invalidToHitText, c);
        this.add(invalidToHitText);
        Utility.buildConstraints(c, 0, 13, 1, 1, 0, 0);
        label = new JLabel(in_invalidDmgText + ": ");
        gridbag.setConstraints(label, c);
        this.add(label);
        Utility.buildConstraints(c, 1, 13, 1, 1, 0, 0);
        invalidDmgText = new JTextField(String.valueOf(SettingsHandler.getInvalidDmgText()));
        gridbag.setConstraints(invalidDmgText, c);
        this.add(invalidDmgText);
        Utility.buildConstraints(c, 0, 14, 3, 1, 0, 0);
        alwaysOverwrite = new JCheckBox(in_alwaysOverwrite, SettingsHandler.getAlwaysOverwrite());
        gridbag.setConstraints(alwaysOverwrite, c);
        this.add(alwaysOverwrite);
        Utility.buildConstraints(c, 0, 15, 3, 1, 0, 0);
        showSingleBoxPerBundle = new JCheckBox(in_showSingleBoxPerBundle, SettingsHandler.getShowSingleBoxPerBundle());
        gridbag.setConstraints(showSingleBoxPerBundle, c);
        this.add(showSingleBoxPerBundle);
        Utility.buildConstraints(c, 0, 20, 3, 1, 1, 1);
        c.fill = GridBagConstraints.BOTH;
        label = new JLabel(" ");
        gridbag.setConstraints(label, c);
        this.add(label);
    }

    @Override
    public String getTitle() {
        return in_output;
    }

    @Override
    public void setOptionsBasedOnControls() {
        Globals.selectPaper((String) paperType.getSelectedItem());
        if (SettingsHandler.getCleanupTempFiles() || removeTempFiles.isSelected()) {
            SettingsHandler.setCleanupTempFiles(removeTempFiles.isSelected());
        }
        if (SettingsHandler.getWeaponProfPrintout() != weaponProfPrintout.isSelected()) {
            SettingsHandler.setWeaponProfPrintout(weaponProfPrintout.isSelected());
        }
        if (SettingsHandler.getAlwaysOverwrite() || alwaysOverwrite.isSelected()) {
            SettingsHandler.setAlwaysOverwrite(alwaysOverwrite.isSelected());
        }
        if (SettingsHandler.getShowSingleBoxPerBundle() || showSingleBoxPerBundle.isSelected()) {
            SettingsHandler.setShowSingleBoxPerBundle(showSingleBoxPerBundle.isSelected());
        }
        SettingsHandler.setSelectedCharacterHTMLOutputSheet(outputSheetHTMLDefault.getText(), null);
        SettingsHandler.setSelectedCharacterPDFOutputSheet(outputSheetPDFDefault.getText(), null);
        SettingsHandler.setSelectedEqSetTemplate(outputSheetEqSet.getText());
        SettingsHandler.setSaveOutputSheetWithPC(saveOutputSheetWithPC.isSelected());
        SettingsHandler.setSelectedSpellSheet(outputSheetSpellsDefault.getText());
        SettingsHandler.setPrintSpellsWithPC(printSpellsWithPC.isSelected());
        SettingsHandler.setPostExportCommandStandard(postExportCommandStandard.getText());
        SettingsHandler.setPostExportCommandPDF(postExportCommandPDF.getText());
        SettingsHandler.setInvalidToHitText(invalidToHitText.getText());
        SettingsHandler.setInvalidDmgText(invalidDmgText.getText());
        SettingsHandler.setIncludeSkills(skillChoice.getSelectedIndex());
    }

    @Override
    public void applyOptionValuesToControls() {
        paperType.setSelectedIndex(Globals.getSelectedPaper());
        weaponProfPrintout.setSelected(SettingsHandler.getWeaponProfPrintout());
        saveOutputSheetWithPC.setSelected(SettingsHandler.getSaveOutputSheetWithPC());
        printSpellsWithPC.setSelected(SettingsHandler.getPrintSpellsWithPC());
        skillChoice.setSelectedIndex(SettingsHandler.getIncludeSkills());
    }

    private final class PrefsButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            JButton source = (JButton) actionEvent.getSource();
            if (source == null) {
            } else if (source == outputSheetHTMLDefaultButton) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(LanguageBundle.getString("in_Prefs_outputSheetHTMLDefaultTitle"));
                fc.setCurrentDirectory(new File(SettingsHandler.getHTMLOutputSheetPath()));
                fc.setSelectedFile(new File(SettingsHandler.getSelectedCharacterHTMLOutputSheet(null)));
                if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File newTemplate = fc.getSelectedFile();
                    if (newTemplate.isDirectory() || (!newTemplate.getName().startsWith("csheet") && !newTemplate.getName().startsWith("psheet"))) {
                        ShowMessageDelegate.showMessageDialog(LanguageBundle.getString("in_Prefs_outputSheetDefaultError"), in_pcgen, MessageType.ERROR);
                    } else {
                        if (newTemplate.getName().startsWith("csheet")) {
                            SettingsHandler.setSelectedCharacterHTMLOutputSheet(newTemplate.getAbsolutePath(), null);
                        } else {
                            SettingsHandler.setSelectedPartyHTMLOutputSheet(newTemplate.getAbsolutePath());
                        }
                    }
                }
                outputSheetHTMLDefault.setText(String.valueOf(SettingsHandler.getSelectedCharacterHTMLOutputSheet(null)));
            } else if (source == outputSheetPDFDefaultButton) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(LanguageBundle.getString("in_Prefs_outputSheetPDFDefaultTitle"));
                fc.setCurrentDirectory(new File(SettingsHandler.getPDFOutputSheetPath()));
                fc.setSelectedFile(new File(SettingsHandler.getSelectedCharacterPDFOutputSheet(null)));
                if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File newTemplate = fc.getSelectedFile();
                    if (newTemplate.isDirectory() || (!newTemplate.getName().startsWith("csheet") && !newTemplate.getName().startsWith("psheet"))) {
                        ShowMessageDelegate.showMessageDialog(LanguageBundle.getString("in_Prefs_outputSheetDefaultError"), in_pcgen, MessageType.ERROR);
                    } else {
                        if (newTemplate.getName().startsWith("csheet")) {
                            SettingsHandler.setSelectedCharacterPDFOutputSheet(newTemplate.getAbsolutePath(), null);
                        } else {
                            SettingsHandler.setSelectedPartyPDFOutputSheet(newTemplate.getAbsolutePath());
                        }
                    }
                }
                outputSheetPDFDefault.setText(String.valueOf(SettingsHandler.getSelectedCharacterPDFOutputSheet(null)));
            } else if (source == outputSheetEqSetButton) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(LanguageBundle.getString("in_Prefs_templateEqSetTitle"));
                fc.setCurrentDirectory(SettingsHandler.getPcgenOutputSheetDir());
                fc.setSelectedFile(new File(SettingsHandler.getSelectedEqSetTemplate()));
                if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File newTemplate = fc.getSelectedFile();
                    if (newTemplate.isDirectory() || !newTemplate.getName().startsWith("eqsheet")) {
                        ShowMessageDelegate.showMessageDialog(LanguageBundle.getString("in_Prefs_templateEqSetError"), in_pcgen, MessageType.ERROR);
                    } else {
                        SettingsHandler.setSelectedEqSetTemplate(newTemplate.getAbsolutePath());
                    }
                }
                outputSheetEqSet.setText(String.valueOf(SettingsHandler.getSelectedEqSetTemplate()));
            } else if (source == outputSheetSpellsDefaultButton) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(LanguageBundle.getString("in_Prefs_outputSpellSheetDefault"));
                fc.setCurrentDirectory(SettingsHandler.getPcgenOutputSheetDir());
                fc.setSelectedFile(new File(SettingsHandler.getSelectedSpellSheet()));
                if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File newTemplate = fc.getSelectedFile();
                    if (newTemplate.isDirectory() || !newTemplate.getName().startsWith("csheet")) {
                        ShowMessageDelegate.showMessageDialog(LanguageBundle.getString("in_Prefs_outputSheetDefaultError"), in_pcgen, MessageType.ERROR);
                    } else {
                        SettingsHandler.setSelectedSpellSheet(newTemplate.getAbsolutePath());
                    }
                }
                outputSheetSpellsDefault.setText(String.valueOf(SettingsHandler.getSelectedSpellSheet()));
            }
        }
    }

    private final class TextFocusLostListener implements FocusListener {

        private String initialValue = null;

        private boolean dialogOpened = false;

        /**
		 * @see java.awt.event.FocusListener#focusGained(FocusEvent)
		 */
        public void focusGained(FocusEvent e) {
            dialogOpened = false;
            final Object source = e.getSource();
            if (source instanceof JTextField) {
                initialValue = ((JTextField) source).getText();
            }
        }

        /**
		 * @see java.awt.event.FocusListener#focusLost(FocusEvent)
		 */
        public void focusLost(FocusEvent e) {
            final Object source = e.getSource();
            if (source instanceof JTextField) {
                final String fieldValue = ((JTextField) source).getText();
                final File fieldFile = new File(fieldValue);
                if ((!fieldFile.exists()) && (!fieldValue.equalsIgnoreCase("null")) && (fieldValue.trim().length() > 0) && (!dialogOpened)) {
                    dialogOpened = true;
                    ShowMessageDelegate.showMessageDialog("File does not exist; preferences were not set.", "Invalid Path", MessageType.ERROR);
                    ((JTextField) source).setText(initialValue);
                }
            }
        }
    }
}
