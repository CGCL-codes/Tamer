package org.matsim.utils.objectattributes.attributeconverters;

import org.matsim.utils.objectattributes.AttributeConverter;

/**
 * @author mrieser
 */
public class BooleanConverter implements AttributeConverter<Boolean> {

    @Override
    public Boolean convert(String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public String convertToObject(Boolean b) {
        return b.toString();
    }
}
