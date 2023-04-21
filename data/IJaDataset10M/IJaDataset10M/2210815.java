package org.simfony.field;

import org.simfony.Field;
import org.simfony.Formatter;
import org.simfony.Transformer;
import org.simfony.Validator;
import org.simfony.element.BasicElement;
import org.simfony.format.StringFormatter;

/**
 * BasicField implements basic Field functions.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public abstract class BasicField extends BasicElement implements Field {

    protected Formatter _formatter;

    /**
    * Creates field with specified name.
    *
    * @param name Name of the field.
    */
    public BasicField(String name) {
        this(name, null, null);
    }

    /**
    * Creates field with specified name, validators and transformers.
    *
    * @param name Name of the field.
    * @param validators Array of validators.
    * @param transformers Array of transformers.
    */
    public BasicField(String name, Validator[] validators, Transformer[] transformers) {
        super(name, validators, transformers);
    }

    /**
    * Returns true if the field is empty.
    *
    * @return true if the field value is null.
    */
    public boolean isEmpty() {
        return getValue() == null;
    }

    /**
    * Sets the format of the field.
    * {@link #toString toString} method should use this format to represent the value.
    *
    * @param format Format to be set.
    */
    public void setFormatter(Formatter formatter) {
        _formatter = formatter;
    }

    /**
    * Returns string representation of the value. Formats value if formatter is set.
    *
    * @return string representation of the value.
    */
    public String toString() {
        return StringFormatter.getInstance().format(_formatter == null ? getValue() : _formatter.format(getValue()));
    }
}
