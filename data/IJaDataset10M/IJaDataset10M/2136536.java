package net.sf.jabref;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jabref.labelPattern.LabelPattern;
import net.sf.jabref.labelPattern.LabelPatternUtil;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The Preferences panel for key generation.
 */
public class TabLabelPattern extends JPanel implements PrefsTab {

    private String def = Globals.lang("Default");

    private GridBagLayout gbl = new GridBagLayout();

    private GridBagConstraints con = new GridBagConstraints();

    private HashMap<String, JTextField> textFields = new HashMap<String, JTextField>();

    private JabRefPreferences _prefs;

    private LabelPattern _keypatterns = null;

    private JCheckBox dontOverwrite = new JCheckBox(Globals.lang("Do not overwrite existing keys")), warnBeforeOverwriting = new JCheckBox(Globals.lang("Warn before overwriting existing keys")), generateOnSave = new JCheckBox(Globals.lang("Generate keys before saving (for entries without a key)")), autoGenerateOnImport = new JCheckBox(Globals.lang("Generate keys for imported entries"));

    private JRadioButton letterStartA = new JRadioButton(Globals.lang("Ensure unique keys using letters (a, b, ...)")), letterStartB = new JRadioButton(Globals.lang("Ensure unique keys using letters (b, c, ...)")), alwaysAddLetter = new JRadioButton(Globals.lang("Always add letter (a, b, ...) to generated keys"));

    private JLabel lblEntryType, lblKeyPattern;

    private JTextField defaultPat = new JTextField();

    private JTextField KeyPatternRegex = new JTextField(20);

    private JTextField KeyPatternReplacement = new JTextField(20);

    private JButton btnDefaultAll, btnDefault;

    private HelpAction help;

    /**
	 * The constructor
	 */
    public TabLabelPattern(JabRefPreferences prefs, HelpDialog helpDiag) {
        _prefs = prefs;
        help = new HelpAction(helpDiag, GUIGlobals.labelPatternHelp, "Help on key patterns");
        buildGUI();
    }

    /**
	 * Store changes to table preferences. This method is called when
	 * the user clicks Ok.
	 *
	 */
    public void storeSettings() {
        Globals.prefs.put("defaultLabelPattern", defaultPat.getText());
        Globals.prefs.putBoolean("warnBeforeOverwritingKey", warnBeforeOverwriting.isSelected());
        Globals.prefs.putBoolean("avoidOverwritingKey", dontOverwrite.isSelected());
        Globals.prefs.put("KeyPatternRegex", KeyPatternRegex.getText());
        Globals.prefs.put("KeyPatternReplacement", KeyPatternReplacement.getText());
        Globals.prefs.putBoolean("generateKeysAfterInspection", autoGenerateOnImport.isSelected());
        Globals.prefs.putBoolean("generateKeysBeforeSaving", generateOnSave.isSelected());
        if (alwaysAddLetter.isSelected()) Globals.prefs.putBoolean("keyGenAlwaysAddLetter", true); else if (letterStartA.isSelected()) {
            Globals.prefs.putBoolean("keyGenFirstLetterA", true);
            Globals.prefs.putBoolean("keyGenAlwaysAddLetter", false);
        } else {
            Globals.prefs.putBoolean("keyGenFirstLetterA", false);
            Globals.prefs.putBoolean("keyGenAlwaysAddLetter", false);
        }
        LabelPatternUtil.updateDefaultPattern();
        LabelPattern defKeyPattern = _keypatterns.getParent();
        _keypatterns = new LabelPattern(defKeyPattern);
        Iterator<String> i = textFields.keySet().iterator();
        while (i.hasNext()) {
            String s = i.next(), text = textFields.get(s).getText();
            if (!"".equals(text.trim())) _keypatterns.addLabelPattern(s, text);
        }
        _prefs.putKeyPattern(_keypatterns);
    }

    private JTextField addEntryType(Container c, String name, int y) {
        JLabel lab = new JLabel(Util.nCase(name));
        name = name.toLowerCase();
        con.gridx = 0;
        con.gridy = y;
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 0;
        con.weighty = 0;
        con.anchor = GridBagConstraints.WEST;
        con.insets = new Insets(0, 5, 0, 5);
        gbl.setConstraints(lab, con);
        c.add(lab);
        JTextField tf = new JTextField();
        tf.setColumns(15);
        con.gridx = 1;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 1;
        con.weighty = 0;
        con.anchor = GridBagConstraints.CENTER;
        con.insets = new Insets(0, 5, 0, 5);
        gbl.setConstraints(tf, con);
        c.add(tf);
        JButton but = new JButton(def);
        con.gridx = 2;
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 0;
        con.weighty = 0;
        con.anchor = GridBagConstraints.CENTER;
        con.insets = new Insets(0, 5, 0, 5);
        gbl.setConstraints(but, con);
        but.setActionCommand(name);
        but.addActionListener(new buttonHandler());
        c.add(but);
        return tf;
    }

    private void setValue(JTextField tf, String fieldName) {
        if (_keypatterns.isDefaultValue(fieldName)) tf.setText(""); else {
            tf.setText(_keypatterns.getValue(fieldName).get(0).toString());
        }
    }

    /**
	 * Method to build GUI
	 *
	 */
    private void buildGUI() {
        ButtonGroup bg = new ButtonGroup();
        bg.add(letterStartA);
        bg.add(letterStartB);
        bg.add(alwaysAddLetter);
        JPanel pan = new JPanel();
        JScrollPane sp = new JScrollPane(pan);
        sp.setPreferredSize(new Dimension(100, 100));
        sp.setBorder(BorderFactory.createEmptyBorder());
        pan.setLayout(gbl);
        setLayout(gbl);
        lblEntryType = new JLabel(Globals.lang("Entry type"));
        Font f = new Font("plain", Font.BOLD, 12);
        lblEntryType.setFont(f);
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        con.gridheight = 1;
        con.fill = GridBagConstraints.VERTICAL;
        con.anchor = GridBagConstraints.WEST;
        con.insets = new Insets(5, 5, 10, 0);
        gbl.setConstraints(lblEntryType, con);
        pan.add(lblEntryType);
        lblKeyPattern = new JLabel(Globals.lang("Key pattern"));
        lblKeyPattern.setFont(f);
        con.gridx = 1;
        con.gridy = 0;
        con.gridheight = 1;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.anchor = GridBagConstraints.WEST;
        con.insets = new Insets(5, 5, 10, 5);
        gbl.setConstraints(lblKeyPattern, con);
        pan.add(lblKeyPattern);
        con.gridy = 1;
        con.gridx = 0;
        JLabel lab = new JLabel(Globals.lang("Default pattern"));
        gbl.setConstraints(lab, con);
        pan.add(lab);
        con.gridx = 1;
        gbl.setConstraints(defaultPat, con);
        pan.add(defaultPat);
        con.insets = new Insets(5, 5, 10, 5);
        btnDefault = new JButton(Globals.lang("Default"));
        btnDefault.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                defaultPat.setText((String) Globals.prefs.defaults.get("defaultLabelPattern"));
            }
        });
        con.gridx = 2;
        int y = 2;
        gbl.setConstraints(btnDefault, con);
        pan.add(btnDefault);
        for (String s : BibtexEntryType.ALL_TYPES.keySet()) {
            textFields.put(s, addEntryType(pan, s, y));
            y++;
        }
        con.fill = GridBagConstraints.BOTH;
        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 3;
        con.weightx = 1;
        con.weighty = 1;
        gbl.setConstraints(sp, con);
        add(sp);
        con.gridwidth = 1;
        con.gridx = 1;
        con.gridy = 2;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0;
        con.weighty = 0;
        con.anchor = GridBagConstraints.SOUTHEAST;
        con.insets = new Insets(0, 5, 0, 5);
        JButton hlb = new JButton(GUIGlobals.getImage("helpSmall"));
        hlb.setToolTipText(Globals.lang("Help on key patterns"));
        gbl.setConstraints(hlb, con);
        add(hlb);
        hlb.addActionListener(help);
        btnDefaultAll = new JButton(Globals.lang("Reset all"));
        con.gridx = 2;
        con.gridy = 2;
        con.weightx = 1;
        con.weighty = 0;
        con.anchor = GridBagConstraints.SOUTHEAST;
        con.insets = new Insets(20, 5, 0, 5);
        gbl.setConstraints(btnDefaultAll, con);
        btnDefaultAll.addActionListener(new buttonHandler());
        add(btnDefaultAll);
        FormLayout layout = new FormLayout("1dlu, 8dlu, left:pref, 8dlu, left:pref", "");
        pan = new JPanel();
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.appendSeparator(Globals.lang("Key generator settings"));
        builder.nextLine();
        builder.append(pan);
        builder.append(autoGenerateOnImport);
        builder.append(letterStartA);
        builder.nextLine();
        builder.append(pan);
        builder.append(warnBeforeOverwriting);
        builder.append(letterStartB);
        builder.nextLine();
        builder.append(pan);
        builder.append(dontOverwrite);
        builder.append(alwaysAddLetter);
        builder.nextLine();
        builder.append(pan);
        builder.append(generateOnSave);
        builder.nextLine();
        builder.append(pan);
        builder.append(Globals.lang("Replace (regular expression)") + ":");
        builder.append(Globals.lang("by") + ":");
        builder.nextLine();
        builder.append(pan);
        builder.append(KeyPatternRegex);
        builder.append(KeyPatternReplacement);
        builder.getPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        con.gridx = 1;
        con.gridy = 3;
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 1;
        con.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(builder.getPanel(), con);
        add(builder.getPanel());
        dontOverwrite.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                warnBeforeOverwriting.setEnabled(!dontOverwrite.isSelected());
            }
        });
    }

    /**
	 * An inner class to handle button actions
	 * @author Ulrik Stervbo (ulriks AT ruc.dk)
	 */
    class buttonHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == btnDefaultAll) {
                Iterator<String> i = textFields.keySet().iterator();
                while (i.hasNext()) {
                    String s = i.next();
                    JTextField tf = textFields.get(s);
                    tf.setText("");
                }
                return;
            }
            JTextField tf = textFields.get(evt.getActionCommand());
            tf.setText("");
        }
    }

    public boolean readyToClose() {
        return true;
    }

    public void setValues() {
        _keypatterns = _prefs.getKeyPattern();
        defaultPat.setText(Globals.prefs.get("defaultLabelPattern"));
        dontOverwrite.setSelected(Globals.prefs.getBoolean("avoidOverwritingKey"));
        generateOnSave.setSelected(Globals.prefs.getBoolean("generateKeysBeforeSaving"));
        autoGenerateOnImport.setSelected(Globals.prefs.getBoolean("generateKeysAfterInspection"));
        warnBeforeOverwriting.setSelected(Globals.prefs.getBoolean("warnBeforeOverwritingKey"));
        boolean alwaysAddLetter = Globals.prefs.getBoolean("keyGenAlwaysAddLetter"), firstLetterA = Globals.prefs.getBoolean("keyGenFirstLetterA");
        if (alwaysAddLetter) this.alwaysAddLetter.setSelected(true); else if (firstLetterA) this.letterStartA.setSelected(true); else this.letterStartB.setSelected(true);
        warnBeforeOverwriting.setEnabled(!dontOverwrite.isSelected());
        for (Iterator<String> i = textFields.keySet().iterator(); i.hasNext(); ) {
            String name = i.next();
            JTextField tf = textFields.get(name);
            setValue(tf, name);
        }
        KeyPatternRegex.setText(Globals.prefs.get("KeyPatternRegex"));
        KeyPatternReplacement.setText(Globals.prefs.get("KeyPatternReplacement"));
    }

    public String getTabName() {
        return Globals.lang("BibTeX key generator");
    }
}
