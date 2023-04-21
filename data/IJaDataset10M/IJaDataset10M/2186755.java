package com.intel.gpe.gridbeans.plugins;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.namespace.QName;
import com.intel.gpe.client2.Client;
import com.intel.gpe.client2.defaults.IPreferences;
import com.intel.gpe.client2.defaults.preferences.INode;
import com.intel.gpe.gridbeans.ErrorSet;
import com.intel.gpe.gridbeans.IComplexParameterValue;
import com.intel.gpe.gridbeans.IGridBeanModel;
import com.intel.gpe.gridbeans.IGridBeanParameterValue;
import com.intel.gpe.gridbeans.JobError;
import com.intel.gpe.gridbeans.OutputFileParameterValue;
import com.intel.gpe.gridbeans.plugins.controls.ButtonDataControl;
import com.intel.gpe.gridbeans.plugins.controls.CheckBoxControl;
import com.intel.gpe.gridbeans.plugins.controls.ComboBoxDataControl;
import com.intel.gpe.gridbeans.plugins.controls.GroupControl;
import com.intel.gpe.gridbeans.plugins.controls.LabelDataControl;
import com.intel.gpe.gridbeans.plugins.controls.ListDataControl;
import com.intel.gpe.gridbeans.plugins.controls.TextEditorControl;
import com.intel.gpe.gridbeans.plugins.controls.TextFieldDataControl;
import com.intel.gpe.gridbeans.plugins.translators.EmptyValueTranslator;
import com.intel.gpe.gridbeans.plugins.translators.StringValueTranslator;
import com.intel.gpe.gridbeans.plugins.validators.EmptyValueValidator;
import com.intel.gpe.gridbeans.plugins.validators.NCNameValueValidator;
import com.intel.gui.controls2.configurable.IConfigurable;
import com.intel.gui.editors.text.TextEditor;
import com.intel.util.xml.QNameUtil;

/**
 * The base implementation of an input or output GridBean panel.
 * 
 * @author Alexander Lukichev
 * @version $Id: GridBeanPanel.java,v 1.18 2007/02/22 14:40:10 dizhigul Exp $
 */
public class GridBeanPanel extends JPanel implements IGridBeanPanel, IConfigurable {

    protected Client parent2;

    private String name;

    private INode node;

    private Map<QName, IDataControl> controls = new HashMap<QName, IDataControl>();

    private List<IConfigurable> children = new ArrayList<IConfigurable>();

    private IConfigurable parent;

    public GridBeanPanel(com.intel.gpe.client2.Client client, String name) {
        this.parent2 = client;
        this.name = name;
    }

    public GridBeanPanel(Client client, String name, INode node) {
        this.parent2 = client;
        this.name = name;
        this.node = node;
    }

    /**
     * Get the control by its name
     * 
     * @param key
     *            The name of the control
     */
    public IDataControl getControl(QName key) throws DataSetException {
        IDataControl control = controls.get(key);
        if (control == null) {
            throw new DataSetException("No control with key " + key + " defined");
        }
        return control;
    }

    public boolean getEnabled(QName key) throws DataSetException {
        return getControl(key).getEnabled();
    }

    public void setEnabled(QName key, boolean enabled) throws DataSetException {
        getControl(key).setEnabled(enabled);
    }

    public Set<QName> keySet() {
        return controls.keySet();
    }

    public Object getValue(QName key) throws DataSetException, TranslationException {
        return getControl(key).getValue();
    }

    public void setValue(QName key, Object value) throws DataSetException, TranslationException {
        if (value instanceof OutputFileParameterValue) {
            getControl(key).setValue(((OutputFileParameterValue) value).getGpeFile());
            return;
        }
        if (value instanceof IComplexParameterValue) {
            getControl(key).setValue(((IComplexParameterValue) value).getValue());
            return;
        }
        getControl(key).setValue(value);
    }

    public void setValueTranslator(QName key, IValueTranslator translator) throws DataSetException {
        getControl(key).setValueTranslator(translator);
    }

    public void setValueValidator(QName key, IValueValidator validator) throws DataSetException {
        getControl(key).setValueValidator(validator);
    }

    public void setDescription(QName key, String descr) throws DataSetException {
        getControl(key).setDescription(descr);
    }

    public void setPossibleValues(QName key, List values) throws DataSetException, TranslationException {
        getControl(key).setPossibleValues(values);
    }

    /**
     * Bind the specifed control to the specified name
     * 
     * @param key
     *            The name
     * @param control
     *            The control
     */
    public void linkDataControl(QName key, IDataControl control) {
        controls.put(key, control);
    }

    /**
     * Helper method to bind a text field containing the job identifier (name)
     * 
     * @param key
     *            The name of the control (usually <code>"name"</code>)
     * @param textField
     *            The text field
     */
    public void linkJobNameTextField(QName key, JTextField textField) throws DataSetException {
        IDataControl control = new TextFieldDataControl(textField);
        control.setDescription("Job Name");
        control.setValueValidator(NCNameValueValidator.getInstance());
        control.setValueTranslator(StringValueTranslator.getInstance());
        linkDataControl(key, control);
    }

    /**
     * Helper method to bind a list
     * 
     * @param key
     *            The name of the control
     * @param list
     *            The list
     */
    public void linkList(QName key, JList list) {
        IDataControl control = new ListDataControl(list);
        linkDataControl(key, control);
    }

    /**
     * Helper method to bind a text field
     * 
     * @param key
     *            The name of the control
     * @param textField
     *            The text field
     */
    public void linkTextField(QName key, JTextField textField) {
        IDataControl control = new TextFieldDataControl(textField);
        linkDataControl(key, control);
    }

    /**
     * Helper method to bind a label
     * 
     * @param key
     *            The name of the control
     * @param label
     *            The label
     */
    public void linkLabel(QName key, JLabel label) throws DataSetException {
        IDataControl control = new LabelDataControl(label);
        linkDataControl(key, control);
        control.setDescription("Label");
        control.setValueTranslator(EmptyValueTranslator.getInstance());
        control.setValueValidator(EmptyValueValidator.getInstance());
    }

    /**
     * Helper method to bind a button
     * 
     * @param key
     *            The name of the control
     * @param button
     *            The button
     */
    public void linkButton(QName key, JButton button) throws DataSetException {
        IDataControl control = new ButtonDataControl(button);
        linkDataControl(key, control);
        control.setDescription("Button");
        control.setValueTranslator(EmptyValueTranslator.getInstance());
        control.setValueValidator(EmptyValueValidator.getInstance());
    }

    /**
     * Helper method to bind a text editor
     * 
     * @param key
     *            The name of the control
     * @param editor
     *            The text editor
     */
    public void linkTextEditor(QName key, TextEditor editor) {
        IDataControl control = new TextEditorControl(editor);
        linkDataControl(key, control);
    }

    /**
     * Helper method to bind a check box
     * 
     * @param key
     *            The name of the control
     * @param checkBox
     *            The checkbox
     */
    public void linkCheckBox(QName key, JCheckBox checkBox) {
        IDataControl control = new CheckBoxControl(checkBox);
        linkDataControl(key, control);
    }

    /**
     * Helper method to bind a text field with a label
     * 
     * @param key
     *            The name of the control group
     * @param textField
     *            The text field
     * @param label
     *            The label
     */
    public void linkTextFieldWithLabel(QName key, JTextField textField, JLabel label) throws DataSetException {
        QName textFieldName = QNameUtil.derive(key, "textField");
        linkTextField(textFieldName, textField);
        QName labelName = QNameUtil.derive(key, "label");
        linkLabel(labelName, label);
        IDataControl group = new GroupControl(this, new QName[] { textFieldName, labelName }, textFieldName);
        linkDataControl(key, group);
    }

    /**
     * Helper method to bind a combo box
     * 
     * @param key
     *            The name of the control
     * @param comboBox
     *            The combo box
     */
    public void linkComboBox(QName key, JComboBox comboBox) {
        IDataControl control = new ComboBoxDataControl(comboBox);
        linkDataControl(key, control);
    }

    /**
     * Implements {@link IGridBeanPanel#getName()}
     */
    @Override
    public String getName() {
        return name;
    }

    public Component getComponent() {
        return this;
    }

    public void load(IGridBeanModel model, com.intel.gpe.client2.Client client) throws DataSetException, TranslationException {
        updateValues(client);
        Iterator<QName> keyit = model.keySet().iterator();
        Set<QName> panelKeySet = keySet();
        while (keyit.hasNext()) {
            QName key = keyit.next();
            if (panelKeySet.contains(key)) {
                Object value = model.get(key);
                setValue(key, value);
            }
        }
    }

    public void store(IGridBeanModel model) throws DataSetException, TranslationException {
        Iterator<QName> keyit = keySet().iterator();
        Set<QName> modelKeySet = model.keySet();
        while (keyit.hasNext()) {
            QName key = keyit.next();
            if (modelKeySet.contains(key) && getEnabled(key)) {
                Object value = getValue(key);
                Object oldValue = model.get(key);
                if (oldValue instanceof IComplexParameterValue) {
                    ((IComplexParameterValue) oldValue).setValue((IGridBeanParameterValue) value);
                } else {
                    model.set(key, value);
                }
            }
        }
    }

    /**
     * This method is called whenever {@link #load(IGridBeanModel, Client)} is called. Override it if you want to update
     * the values of the panel depending on the state of the client object.
     * 
     * @param client
     *            The client
     */
    public void updateValues(com.intel.gpe.client2.Client client) {
    }

    public void validate(ErrorSet errors) {
        Iterator<Entry<QName, IDataControl>> entryIt = controls.entrySet().iterator();
        while (entryIt.hasNext()) {
            Entry<QName, IDataControl> entry = entryIt.next();
            StringBuffer buf = new StringBuffer();
            boolean valid = true;
            try {
                valid = entry.getValue().isValid(buf);
            } catch (DataSetException e) {
                try {
                    errors.addError(new JobError("Field " + entry.getValue().getDescription() + " is not valid: cannot get value"));
                } catch (DataSetException e1) {
                    errors.addError(new JobError("Field " + QNameUtil.getRepresentation(entry.getKey()) + " is not valid: cannot get value"));
                }
            }
            if (!valid) {
                try {
                    errors.addError(new JobError("Field " + entry.getValue().getDescription() + " is not valid: " + buf.toString()));
                } catch (DataSetException e) {
                    errors.addError(new JobError("Field " + QNameUtil.getRepresentation(entry.getKey()) + " is not valid: " + buf.toString()));
                }
            }
        }
    }

    public boolean isValid(QName key, StringBuffer buf) throws DataSetException {
        return getControl(key).isValid(buf);
    }

    public String getDescription(QName key) throws DataSetException {
        return getControl(key).getDescription();
    }

    public void load(IPreferences preferences) {
        for (IConfigurable child : children) {
            child.load(preferences);
        }
    }

    public void store(IPreferences preferences) {
        for (IConfigurable child : children) {
            child.store(preferences);
        }
    }

    public void addChild(IConfigurable child) {
        children.add(child);
    }

    public INode getNode() {
        return node;
    }
}
