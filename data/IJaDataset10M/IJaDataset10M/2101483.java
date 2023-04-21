package org.gwt.beansbinding.ui.client.impl;

import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.Property;

/**
 * @author Shannon Hickey
 */
public abstract class AbstractColumnBinding extends Binding {

    private int column;

    public AbstractColumnBinding(int column, Property columnSource, Property columnTarget, String name) {
        super(null, columnSource, null, columnTarget, name);
        this.column = column;
        setManaged(true);
    }

    public final int getColumn() {
        return column;
    }

    protected final void setColumn(int column) {
        this.column = column;
    }

    public void bindImpl() {
    }

    public void unbindImpl() {
    }
}
