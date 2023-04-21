package org.mili.core.text;

/**
 * This class defiens a default implementation of interface {@link Row}.
 *
 * @author Michael Lieshoff
 *
 */
public class DefaultRow implements Row {

    private Object[] values = null;

    /**
     * Instantiates a new row.
     *
     * @param values the values
     */
    public DefaultRow(Object[] values) {
        super();
        this.values = values;
    }

    @Override
    public Object getValue(int index) {
        return this.values[index];
    }

    @Override
    public Object[] getValues() {
        return this.values;
    }
}
