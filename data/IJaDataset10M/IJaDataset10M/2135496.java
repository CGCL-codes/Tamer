package org.nakedobjects.runtime.testsystem.dataclasses;

import org.nakedobjects.applib.AbstractDomainObject;

/**
 * @author Kevin
 *
 */
public class SimpleClassTwo extends AbstractDomainObject {

    public String title() {
        return text;
    }

    private String text;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    private Integer integer;

    public Integer getValue() {
        return integer;
    }

    public void setValue(final Integer integer) {
        this.integer = integer;
    }
}
