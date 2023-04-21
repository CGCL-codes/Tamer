package com.nexirius.framework.datamodel;

import com.nexirius.util.CopyPairs;
import com.nexirius.util.TextToken;
import com.nexirius.util.simpletype.SimpleType_boolean;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/**
 * This class represents a boolean information which is displayed as two different strings or as a toggle button.
 *
 * @author Marcel Baumann
 */
public class BooleanModel extends SimpleModel implements ChangeListener {

    public static final String TRUE_FALSE[] = { "false", "true" };

    SimpleArrayModel trueFalseLabelModel = null;

    ToggleButtonModel toggleButtonModel = null;

    LabelModelListener labelModelListener = null;

    /**
     * Creates a instance and initializes the value to false
     */
    public BooleanModel() {
        this(false);
    }

    /**
     * Creates a instance and initializes the value
     *
     * @param value The initial value
     */
    public BooleanModel(boolean value) {
        super(new SimpleType_boolean(value));
    }

    /**
     * Creates a instance and initializes the value and the field name
     *
     * @param value     The initial value
     * @param fieldName The initial field name
     */
    public BooleanModel(boolean value, String fieldName) {
        super(new SimpleType_boolean(value), fieldName);
    }

    /**
     * Read the persistent data from an input stream. A boolean is coded as the letter 'T' or 'F'.
     *
     * @param in The input stream
     * @throws Exception If the stream cannot be read or the data is corrupt
     */
    public void readDataFrom(PushbackInputStream in) throws Exception {
        TextToken token = TextToken.nextToken(in);
        if (token.isIdentifier("T") || token.isIdentifier("F")) {
            if (token.isIdentifier("T")) {
                setBoolean(true);
            } else {
                setBoolean(false);
            }
        } else {
            throw new Exception("Expecting T of F (field value) but have:" + token);
        }
    }

    /**
     * Write the persistent data to an output stream. A boolean is coded as the letter 'T' or 'F'.
     *
     * @param out The output stream
     * @throws Exception If the stream cannot be written
     */
    public void writeDataTo(OutputStream out) throws Exception {
        TextToken value = new TextToken((getBoolean() ? "T" : "F"), TextToken.IDENTIFIER);
        value.writeTo(out);
    }

    /**
     * Set a string pair which represents the true and false labels as a string literal
     *
     * @param labelModel An array of two strings where the first represents the false value.
     */
    public void setTrueFalseLabelModel(SimpleArrayModel labelModel) {
        if (this.labelModelListener != null) {
            this.trueFalseLabelModel.removeDataModelListener(labelModelListener);
        } else {
            this.labelModelListener = new LabelModelListener();
        }
        this.trueFalseLabelModel = labelModel;
        if (this.trueFalseLabelModel != null) {
            this.trueFalseLabelModel.addDataModelListener(labelModelListener);
        } else {
            this.labelModelListener = null;
        }
    }

    /**
     * Get the model which represents the labels for the true and false values
     *
     * @return null (default) or the model which was introduced with setTrueFalseLabelModel()
     */
    public SimpleArrayModel getTrueFalseLabelModel() {
        return trueFalseLabelModel;
    }

    /**
     * Get the current value as boolean
     */
    public boolean getBoolean() {
        return getSimpleType().getBoolean();
    }

    /**
     * Set the new value as boolean. Value changed events are only fired if the actual
     * value differs from the new value.
     *
     * @param value The new boolean value which is represented by this model.
     */
    public void setBoolean(boolean value) {
        setValue(new SimpleType_boolean(value));
    }

    /**
     * Translate the given text into a boolean and set the new value.
     *
     * @param text The value as text (true or false)
     * @throws Exception If the specified String is not interpreted as a legal boolean
     */
    public void setText(String text) throws Exception {
        setValue(new SimpleType_boolean(text));
    }

    /**
     * Get the actual value as string
     *
     * @return The appropriate string from the given label model of "true" or "false"
     *         according to the current state of the represented boolean
     */
    public String getText() {
        int index = (getBoolean() ? 1 : 0);
        String text = null;
        if (trueFalseLabelModel != null && trueFalseLabelModel.getLength() > index) {
            text = trueFalseLabelModel.getItem(index).toString();
        } else {
            text = TRUE_FALSE[index];
        }
        return text;
    }

    /**
     * Create a copy instance (the label model is shared ot copied)
     */
    public DataModel duplicate(DataModel instance, CopyPairs copyPairs) {
        if (instance == null) {
            instance = new BooleanModel(getBoolean());
            ((BooleanModel) instance).setTrueFalseLabelModel(getTrueFalseLabelModel());
        }
        return super.duplicate(instance, copyPairs);
    }

    /**
     * Get the associated swing toggle button model
     *
     * @return A new (only first call) ToggleButtonModel
     */
    public ToggleButtonModel getToggleButtonModel() {
        if (this.toggleButtonModel == null) {
            this.toggleButtonModel = new ToggleButtonModel();
            this.toggleButtonModel.addChangeListener(this);
        }
        return this.toggleButtonModel;
    }

    /**
     * The toggle button change listener interface implementation
     */
    public void stateChanged(ChangeEvent ev) {
        setBoolean(this.toggleButtonModel.isSelected());
    }

    /**
     * Updates the associated toggle button model when the model changes state.
     */
    public void updateToggleButtonModel() {
        getToggleButtonModel().setSelected(getBoolean());
    }

    /**
     * A class to observe the associated label model.
     */
    public class LabelModelListener implements DataModelListener {

        public void dataModelChangeValue(DataModelEvent event) {
            fireValueChange(BooleanModel.this);
        }

        public void dataModelChangeStructure(DataModelEvent event) {
        }

        public void dataModelGrabFocus(DataModelEvent event) {
        }
    }
}
