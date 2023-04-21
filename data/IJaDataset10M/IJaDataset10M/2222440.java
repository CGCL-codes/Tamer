package org.simfony.field;

import org.simfony.Transformer;
import org.simfony.Validator;
import org.simfony.parse.StringParser;

/**
 * TextField is used to hold text information.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public class TextField extends SingleValueField {

    /**
    * Creates TextField with empty data.
    */
    public TextField() {
        this(null, null);
    }

    /**
    * Creates TextField with a given name and empty string
    * as value.
    *
    * @param name Name of the field.
    */
    public TextField(String name) {
        this(name, null);
    }

    /**
    * Creates TextField with a given name and value.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    */
    public TextField(String name, Object value) {
        this(name, value, null, null);
    }

    /**
    * Creates TextField with a given name, value
    * ant validator.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param validator Validator of the field.
    */
    public TextField(String name, Object value, Validator validator) {
        this(name, value, new Validator[] { validator }, null);
    }

    /**
    * Creates TextField with a given name, value
    * ant validators.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param validators Validators of the field.
    */
    public TextField(String name, Object value, Validator[] validators) {
        this(name, value, validators, null);
    }

    /**
    * Creates TextField with a given name, value
    * ant transformer.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param transformer Transformer of the field.
    */
    public TextField(String name, Object value, Transformer transformer) {
        this(name, value, null, new Transformer[] { transformer });
    }

    /**
    * Creates TextField with a given name, value,
    * list of validators and list of transformers.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param validators Validators of the field.
    * @param transformers Transformers of the field.
    */
    public TextField(String name, Object value, Validator[] validators, Transformer[] transformers) {
        super(name, value, validators, transformers, StringParser.getInstance());
    }

    /**
    * Returns true if the field is empty.
    *
    * @return true if the field value is null or empty string.
    */
    public boolean isEmpty() {
        return super.isEmpty() || ((String) getValue()).length() == 0;
    }
}
