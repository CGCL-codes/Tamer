package org.simfony.field;

import org.simfony.Transformer;
import org.simfony.Validator;
import org.simfony.parse.KeywordParser;

/**
 * KeywordField has list of Keywords. One of them can be field value.
 * KeywordField can also obtain other values (non-Keywords) but you
 * can use KeywordValidator to restrict that.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public class KeywordField extends SingleValueField {

    private Keyword[] _keywords;

    /**
    * Creates field with specified name, no value and empty list of keywords.
    *
    * @param name Name of the field.
    */
    public KeywordField(String name) {
        this(name, null, null, null, null);
    }

    /**
    * Creates field with specified name, value and keywords.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param keywords List of keywords.
    */
    public KeywordField(String name, Object value, Keyword[] keywords) {
        this(name, value, keywords, null, null);
    }

    /**
    * Creates field with specified name, value, keywords and validator.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param keywords List of keywords.
    * @param validator Validator.
    */
    public KeywordField(String name, Object value, Keyword[] keywords, Validator validator) {
        this(name, value, keywords, new Validator[] { validator }, null);
    }

    /**
    * Creates field with specified name, value, keywords, validators and transformers.
    *
    * @param name Name of the field.
    * @param value Value of the field.
    * @param keywords List of keywords.
    * @param validators Array of validators.
    * @param transformers Array of transformers.
    */
    public KeywordField(String name, Object value, Keyword[] keywords, Validator[] validators, Transformer[] transformers) {
        super(name, value, validators, transformers, keywords == null ? null : new KeywordParser(keywords));
        if (keywords != null) setKeywords(keywords);
    }

    /**
    * Returns the value of the field. This method simple returns <code>super.getValue()</code>.
    *
    * @return the value of the field.
    */
    public Object getActualValue() {
        return super.getValue();
    }

    /**
    * Returns currently selected keyword.
    *
    * @return Currently selected keyword or null if non-keyword value is set.
    */
    public Keyword getKeyword() {
        Object value = super.getValue();
        if (value instanceof Keyword) {
            return (Keyword) value;
        } else {
            return null;
        }
    }

    /**
    * Returns the list of keywords.
    *
    * @return the list of keywords.
    */
    public Keyword[] getKeywords() {
        return _keywords;
    }

    /**
    * Returns the value of the field. KeywordField value can be Keyword or other object.
    * Real value is extracted in case the Keyword is field's actual value.
    *
    * @return the value of the field.
    */
    public Object getValue() {
        Object value = super.getValue();
        if (value instanceof Keyword) {
            return ((Keyword) value).getValue();
        } else {
            return value;
        }
    }

    /**
    * Sets keywords. This method stores the value, sets new array of
    * keywords and then sets the stored value.
    *
    * @param keywords array of keywords to set.
    *
    */
    public void setKeywords(Keyword[] keywords) {
        Object value = getValue();
        _keywords = keywords;
        setValue(value);
    }
}
