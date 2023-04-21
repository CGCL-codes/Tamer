package org.lokee.punchcard.config.converter;

import org.lokee.punchcard.Card;
import org.lokee.punchcard.Deck;
import org.lokee.punchcard.Field;
import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.CardConfig;
import org.lokee.punchcard.config.DeckConfig;
import org.lokee.punchcard.config.FieldConfig;
import org.lokee.punchcard.config.IPunchCardConfig;

/**
 * @author CLaguerre
 * 
 * This is use to lookup values from properties.
 */
public class LookupConverter extends Converter {

    /**
	 * Holds value of property sinkPrefix
	 */
    private String sinkPrefix;

    /**
	 * Holds value of property sourcePrefix
	 */
    private String sourcePrefix;

    public Object convertField(Field field, Card card, Deck deck, FieldConfig fieldConfig, CardConfig cardConfig, DeckConfig deckConfig) throws ConverterException {
        String prefix = "";
        if (sinkPrefix != null) {
            prefix = sinkPrefix;
        }
        String value = PunchCardUtil.translateValue(field.getValue(), card, cardConfig, deck, deckConfig);
        value = PunchCardUtil.findProperty(prefix + value, new IPunchCardConfig[] { fieldConfig, cardConfig, deckConfig });
        if (value != null) {
            return value;
        }
        return PunchCardUtil.translateValue(field.getValue(), card, cardConfig, deck, deckConfig);
    }

    public String[] convertFieldValues(String[] values, Card card, Deck deck, FieldConfig fieldConfig, CardConfig cardConfig, DeckConfig deckConfig) throws ConverterException {
        String[] newValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String result = PunchCardUtil.findProperty(values[i], new IPunchCardConfig[] { fieldConfig, cardConfig, deckConfig });
            if (result == null) {
                result = values[i];
            }
            newValues[i] = result;
        }
        return newValues;
    }

    public String[] convertObject(Object object, Card card, Deck deck, FieldConfig fieldConfig, CardConfig cardConfig, DeckConfig deckConfig) throws ConverterException {
        if (object == null) {
            return null;
        }
        String prefix = "";
        if (sourcePrefix != null) {
            prefix = sourcePrefix;
        }
        String value = PunchCardUtil.findProperty(prefix + object.toString(), new IPunchCardConfig[] { fieldConfig, cardConfig, deckConfig });
        return new String[] { value };
    }

    /**
	 * getSinkPrefix
	 * @return
	 */
    public String getSinkPrefix() {
        return sinkPrefix;
    }

    /**
	 * setSinkPrefix
	 * @param sinkPrefix
	 */
    public void setSinkPrefix(String sinkPrefix) {
        this.sinkPrefix = sinkPrefix;
    }

    /**
	 * getSourcePrefix
	 * @return
	 */
    public String getSourcePrefix() {
        return sourcePrefix;
    }

    /**
	 * setSourcePrefix
	 * @param sourcePrefix
	 */
    public void setSourcePrefix(String sourcePrefix) {
        this.sourcePrefix = sourcePrefix;
    }
}
