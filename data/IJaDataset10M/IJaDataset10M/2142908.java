package de.laures.cewolf.taglib;

import de.laures.cewolf.ChartValidationException;

/**
 * @author glaures
 */
public class AttributeValidationException extends ChartValidationException {

    static final long serialVersionUID = -3287442008010604578L;

    public AttributeValidationException(String attribute, String val) {
        super("value " + val + " is not valid for attribute " + attribute);
    }
}
