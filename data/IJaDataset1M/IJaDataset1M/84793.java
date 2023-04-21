package configurablefoldhandler;

import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.Mode;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import java.text.MessageFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

/**
 * @author    C.J.Kent
 * @created   15 May 2002
 */
@SuppressWarnings("serial")
public class ConfigurableFoldHandlerOptionsPane extends AbstractOptionPane {

    private JTextField defStartField;

    private JTextField defEndField;

    private JTextField modeStartField;

    private JTextField modeEndField;

    private JComboBox modeBox;

    private JCheckBox defUseRegex;

    private JCheckBox modeUseDefault;

    private JCheckBox modeUseRegex;

    private ConfigurableFoldHandlerPlugin plugin;

    private String[] modeNames;

    private HashMap<String, FoldStrings> modeStrings = new HashMap<String, FoldStrings>();

    private String lastModeName;

    private boolean initialising;

    public ConfigurableFoldHandlerOptionsPane() {
        super("configurablefoldhandler");
        plugin = ConfigurableFoldHandlerPlugin.getInstance();
        defUseRegex = new JCheckBox(jEdit.getProperty("options.configurablefoldhandler.use-regex"), jEdit.getBooleanProperty("configurablefoldhandler.use-regex", false));
        defStartField = new JTextField(jEdit.getProperty("configurablefoldhandler.startfold", ConfigurableFoldHandlerPlugin.DEFAULT_FOLD_STRINGS.getStartString()));
        defEndField = new JTextField(jEdit.getProperty("configurablefoldhandler.endfold", ConfigurableFoldHandlerPlugin.DEFAULT_FOLD_STRINGS.getEndString()));
        Mode[] modes = jEdit.getModes();
        modeNames = new String[modes.length];
        for (int i = 0; i < modes.length; i++) modeNames[i] = modes[i].getName();
        modeBox = new JComboBox(modeNames);
        modeUseDefault = new JCheckBox(jEdit.getProperty("options.configurablefoldhandler.use-default"));
        modeUseRegex = new JCheckBox(jEdit.getProperty("options.configurablefoldhandler.use-regex"));
        modeStartField = new JTextField();
        modeEndField = new JTextField();
        addSeparator("options.configurablefoldhandler.separator.default");
        addComponent(defUseRegex);
        addComponent(jEdit.getProperty("options.configurablefoldhandler.foldstart"), defStartField);
        addComponent(jEdit.getProperty("options.configurablefoldhandler.foldend"), defEndField);
        addSeparator("options.configurablefoldhandler.separator.mode-specific");
        addComponent(jEdit.getProperty("options.configurablefoldhandler.mode"), modeBox);
        addComponent(modeUseDefault);
        addComponent(modeUseRegex);
        addComponent(jEdit.getProperty("options.configurablefoldhandler.foldstart"), modeStartField);
        addComponent(jEdit.getProperty("options.configurablefoldhandler.foldend"), modeEndField);
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == modeBox) modeChanged(); else if (e.getSource() == modeUseDefault) useDefClicked();
            }
        };
        modeBox.addActionListener(actionListener);
        modeUseDefault.addActionListener(actionListener);
        String startProp;
        String endProp;
        String regexProp;
        String modeFoldStart;
        String modeFoldEnd;
        boolean isRegex;
        for (int i = 0; i < modeNames.length; i++) {
            startProp = "configurablefoldhandler.mode." + modeNames[i] + ".startfold";
            endProp = "configurablefoldhandler.mode." + modeNames[i] + ".endfold";
            regexProp = "configurablefoldhandler.mode." + modeNames[i] + ".use-regex";
            modeFoldStart = jEdit.getProperty(startProp);
            modeFoldEnd = jEdit.getProperty(endProp);
            isRegex = jEdit.getBooleanProperty(regexProp, false);
            if (modeFoldStart != null && modeFoldEnd != null) {
                modeStrings.put(modeNames[i], new FoldStrings(modeFoldStart, modeFoldEnd, isRegex));
            }
        }
    }

    protected void _init() {
        initialising = true;
        int lastSelIndex = jEdit.getIntegerProperty("options." + "configurablefoldhandler.last-mode-selected", 0);
        if (lastSelIndex < modeBox.getItemCount()) modeBox.setSelectedIndex(lastSelIndex);
        lastModeName = modeNames[modeBox.getSelectedIndex()];
        checkMode();
        initialising = false;
    }

    /**
	 * Called when the options dialog's `OK' button is pressed.
	 * This should save any properties saved in this option pane.
	 */
    protected void _save() {
        modeChanged();
        jEdit.setBooleanProperty("configurablefoldhandler.use-regex", defUseRegex.isSelected());
        jEdit.setProperty("configurablefoldhandler.startfold", defStartField.getText());
        jEdit.setProperty("configurablefoldhandler.endfold", defEndField.getText());
        String startProp;
        String endProp;
        String regexProp;
        FoldStrings foldStrings;
        for (int i = 0; i < modeNames.length; i++) {
            foldStrings = modeStrings.get(modeNames[i]);
            startProp = "configurablefoldhandler.mode." + modeNames[i] + ".startfold";
            endProp = "configurablefoldhandler.mode." + modeNames[i] + ".endfold";
            regexProp = "configurablefoldhandler.mode." + modeNames[i] + ".use-regex";
            if (foldStrings != null) {
                jEdit.setProperty(startProp, foldStrings.getStartString());
                jEdit.setProperty(endProp, foldStrings.getEndString());
                jEdit.setBooleanProperty(regexProp, foldStrings.useRegex());
            } else {
                jEdit.resetProperty(startProp);
                jEdit.resetProperty(endProp);
                jEdit.resetProperty(regexProp);
            }
        }
        plugin.foldStringsChanged();
        jEdit.setIntegerProperty("options.configurablefoldhandler." + "last-mode-selected", modeBox.getSelectedIndex());
    }

    /**
	 * saves the fold settings for the last selected mode when the user selects
	 * a new mode from the combo box.
	 */
    private void modeChanged() {
        if (initialising) return;
        if (modeUseDefault.isSelected()) {
            modeStrings.remove(lastModeName);
        } else {
            FoldStrings newStr = new FoldStrings(modeStartField.getText(), modeEndField.getText(), modeUseRegex.isSelected());
            FoldStrings oldStr = modeStrings.get(lastModeName);
            if (oldStr == null || !newStr.equals(oldStr)) {
                modeStrings.put(lastModeName, newStr);
                if (!newStr.doFolding()) {
                    modeBox.setPopupVisible(false);
                    JOptionPane.showMessageDialog(this, MessageFormat.format(jEdit.getProperty("configurablefoldhandler.optionpaneerror"), new Object[] { newStr.getErrorMessage(), lastModeName }), jEdit.getProperty("configurablefoldhandler.errortitle"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        checkMode();
        lastModeName = modeNames[modeBox.getSelectedIndex()];
    }

    private void useDefClicked() {
        modeStartField.setEnabled(!modeUseDefault.isSelected());
        modeEndField.setEnabled(!modeUseDefault.isSelected());
        modeUseRegex.setEnabled(!modeUseDefault.isSelected());
    }

    /**
	 * checks if there are any fold strings for the mode currently selected in
	 * the mode combo box. if there are then they are put into the text fields
	 * and they're enabled, otherwise the controls are disabled and the default
	 * settings are used. default settings for the mode are used if they're
	 * available. if not the default fold strings are used. 
	 */
    private void checkMode() {
        String modeName = modeNames[modeBox.getSelectedIndex()];
        FoldStrings foldStrings = modeStrings.get(modeName);
        if (foldStrings != null) {
            modeUseDefault.setSelected(false);
            modeStartField.setEnabled(true);
            modeEndField.setEnabled(true);
            modeUseRegex.setEnabled(true);
            modeUseRegex.setSelected(foldStrings.useRegex());
            modeStartField.setText(foldStrings.getStartString());
            modeEndField.setText(foldStrings.getEndString());
        } else {
            foldStrings = plugin.getDefaultModeFoldStrings(modeName);
            if (foldStrings == null) foldStrings = plugin.getDefaultFoldStrings();
            modeUseDefault.setSelected(true);
            modeUseRegex.setSelected(foldStrings.useRegex());
            modeUseRegex.setEnabled(false);
            modeStartField.setEnabled(false);
            modeEndField.setEnabled(false);
            modeStartField.setText(foldStrings.getStartString());
            modeEndField.setText(foldStrings.getEndString());
        }
    }
}
