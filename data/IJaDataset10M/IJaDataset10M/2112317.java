package org.ujac.form;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Name: RadioGroupFormField<br>
 * Description: A FormField class representing a group of radio buttons.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2005/03/06 23:34:12  lauerc
 * <br>Log: Replaced RadioButtonFormField by RadioGroupFormField.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 2263 $
 */
public class RadioGroupFormField extends BaseFormField {

    /** The serial version UID. */
    private static final long serialVersionUID = 3689355437547729976L;

    /** The option list (holds instances of class Option). */
    private List options = null;

    /**
   * Constructs a RadioButtonFormField instance with specific attributes.
   * @param name The name of the radio button group. 
   * @param dataType The data type of the input field.
   * @param label The label for the input field.
   * @param options The option list.
   */
    public RadioGroupFormField(String name, int dataType, String label, List options) {
        super(name, dataType, label);
        this.options = options;
    }

    /**
   * Constructs a RadioButtonFormField instance with specific attributes.
   * @param name The name of the radio button group. 
   * @param key Tells whether or not the field defines a key value.
   * @param dataType The data type of the input field.
   * @param label The label for the input field.
   * @param options The option list.
   */
    public RadioGroupFormField(String name, boolean key, int dataType, String label, List options) {
        super(name, key, dataType, label);
        this.options = options;
    }

    /**
   * Gets the type of the form field.
   * @return The field's type.
   */
    public String getType() {
        return "radiogroup";
    }

    /**
   * Formats the value.
   * @return The textual representation of the current field value.
   */
    public String formatValue() {
        Object value = getValue();
        if (options == null) {
            return null;
        }
        int numOptions = options.size();
        if (numOptions == 0) {
            return null;
        }
        if (value == null) {
            return ((Option) options.get(0)).getText();
        }
        for (int i = 0; i < numOptions; i++) {
            Option option = (Option) options.get(i);
            if (value.equals(option.getValue())) {
                return option.getText();
            }
        }
        return ((Option) options.get(0)).getText();
    }

    /**
   * Getter method for the the property options.
   * @return The current value of property options.
   */
    public List getOptions() {
        return options;
    }

    /**
   * Setter method for the the property options.
   * @param options The value to set for the property options.
   */
    public void setOptions(List options) {
        this.options = options;
    }

    /**
   * Clones the form field.
   * @return A clone of the Form instance.
   */
    public Object clone() {
        BaseFormField clonedField = new RadioGroupFormField(this.getName(), isKey(), this.getDataType(), this.getLabel(), this.options);
        clonedField.copyAttributes(this);
        return clonedField;
    }

    /**
   * Writes the object's data to the given stream.
   * @param s The stream to write to
   * @exception IOException In case the data output failed.
   */
    private void writeObject(ObjectOutputStream s) throws IOException {
        writeData(s);
        s.writeObject(options);
    }

    /**
   * Reads the object's data from the given stream.
   * @param s The stream to read from.
   * @exception IOException In case the data reading failed.
   * @exception ClassNotFoundException In case the class to deserialize could not be found
   */
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        readData(s);
        options = (List) s.readObject();
    }
}
