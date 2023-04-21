package org.jledger.ui.accounts;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jledger.ui.internal.JLedgerWorkbenchPlugin;
import org.jlense.uiworks.dialogs.IDialogSettings;
import org.jlense.uiworks.forms.FormPage;
import org.jlense.uiworks.preference.IPreferenceStore;
import org.jlense.uiworks.widget.WidgetUtils;
import org.jlense.uiworks.workbench.IWorkbench;
import org.jlense.uiworks.workbench.IWorkbenchPreferencePage;

/**
 * Preference page for Accounts.
 * 
 * @author ted stockwell [emorning@sourceforge.net]
 */
public class AccountsPreferencePage extends FormPage implements IWorkbenchPreferencePage {

    public static final String PRE_CHECK1 = JLedgerWorkbenchPlugin.PLUGIN_ID + ".check1";

    public static final String PRE_CHECK2 = JLedgerWorkbenchPlugin.PLUGIN_ID + ".check2";

    public static final String PRE_CHECK3 = JLedgerWorkbenchPlugin.PLUGIN_ID + ".check3";

    public static final String PRE_TEXT = JLedgerWorkbenchPlugin.PLUGIN_ID + ".text";

    private JCheckBox checkBox1;

    private JCheckBox checkBox2;

    private JCheckBox checkBox3;

    private JTextField textField;

    private JButton pushButton_textField;

    /** (non-Javadoc)
     * Method declared on FormPage
     */
    protected JComponent createContents() {
        JPanel contents = new JPanel(new GridBagLayout());
        JPanel composite_textField = new JPanel(new GridBagLayout());
        JLabel label_textField = new JLabel("Text Field: ");
        composite_textField.add(label_textField, WidgetUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST));
        textField = new JTextField(10);
        composite_textField.add(textField, WidgetUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST));
        JPanel composite_checkBox = new JPanel(new GridBagLayout());
        checkBox1 = new JCheckBox("Check box 1");
        composite_checkBox.add(checkBox1, WidgetUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST));
        checkBox2 = new JCheckBox("Check box 2");
        composite_checkBox.add(checkBox2, WidgetUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST));
        checkBox3 = new JCheckBox("Check box 3");
        composite_checkBox.add(checkBox3, WidgetUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST));
        contents.add(composite_textField, WidgetUtils.createGridBagConstraints(0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE));
        contents.add(composite_checkBox, WidgetUtils.createGridBagConstraints(0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE));
        contents.add(new JPanel(), WidgetUtils.createGridBagConstraints(1, 2, GridBagConstraints.BOTH));
        initializeValues();
        return contents;
    }

    /** 
     * The <code>ReadmePreferencePage</code> implementation of this
     * <code>FormPage</code> method 
     * returns preference store that belongs to the our plugin.
     * This is important because we want to store
     * our preferences separately from the desktop.
     */
    protected IDialogSettings doGetDialogSettings() {
        return JLedgerWorkbenchPlugin.getDefault().getPreferenceStore();
    }

    /**
     * Initializes states of the controls using default values
     * in the preference store.
     */
    private void initializeDefaults() {
        IPreferenceStore store = (IPreferenceStore) getDialogSettings();
        checkBox1.setSelected(store.getDefaultBoolean(PRE_CHECK1).booleanValue());
        checkBox2.setSelected(store.getDefaultBoolean(PRE_CHECK2).booleanValue());
        checkBox3.setSelected(store.getDefaultBoolean(PRE_CHECK3).booleanValue());
        textField.setText(store.getDefaultString(PRE_TEXT));
    }

    /**
     * Initializes states of the controls from the preference store.
     */
    private void initializeValues() {
        IPreferenceStore store = (IPreferenceStore) getDialogSettings();
        checkBox1.setSelected(store.getBoolean(PRE_CHECK1).booleanValue());
        checkBox2.setSelected(store.getBoolean(PRE_CHECK2).booleanValue());
        checkBox3.setSelected(store.getBoolean(PRE_CHECK3).booleanValue());
        textField.setText(store.getString(PRE_TEXT));
    }

    protected void performDefaults() {
        super.performDefaults();
        initializeDefaults();
    }

    public boolean performOk() {
        storeValues();
        return true;
    }

    /**
     * Stores the values of the controls back to the preference store.
     */
    private void storeValues() {
        IPreferenceStore store = (IPreferenceStore) getDialogSettings();
        store.put(PRE_CHECK1, checkBox1.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        store.put(PRE_CHECK2, checkBox2.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        store.put(PRE_CHECK3, checkBox3.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        store.put(PRE_TEXT, textField.getText());
    }

    /**
     * Initializes this preference page for the given workbench.
     * <p>
     * This method is called automatically as the preference page is being created
     * and initialized. Clients must not call this method.
     * </p>
     *
     * @param workbench the workbench
     */
    public void init(IWorkbench workbench) {
    }
}
