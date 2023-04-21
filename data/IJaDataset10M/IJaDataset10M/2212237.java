package com.peralex.utilities.ui.validatedtextfield;

/**
 * 
 * @author Noel Grandin
 */
public interface ValidatedTextModel {

    /** Is the text value valid? */
    boolean isValid(String text);

    /** Convert text to object. Will always be called with a valid text value. */
    Object textToObject(String text);

    /** Convert an object to text. Throws some kind of RuntimeException if the value is invalid. */
    String objectToText(Object obj);

    Object getValue();

    /** will always be called with a valid value */
    void setValue(Object obj);
}
