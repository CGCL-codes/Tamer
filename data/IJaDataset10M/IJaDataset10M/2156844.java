package org.awelements.table.web;

/**
 * Checks a property of the row object and strikes the element through if it is true.
 *
 * @author Guido Spahn
 */
public class StrikeThroughColumnDecorator extends BooleanPropertyColumnDecorator {

    public StrikeThroughColumnDecorator() {
        setStartDecoration("<del>");
        setEndDecoration("</del>");
    }
}
