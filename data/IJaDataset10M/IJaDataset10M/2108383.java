package net.sf.jtreemap.swing;

/**
 * Default Value <BR>
 * The getLabel() method returns the "" + getValue()
 * 
 * @author Laurent DUTHEIL
 */
public class DefaultValue extends Value {

    private static final long serialVersionUID = 367321198951855282L;

    private double value;

    /**
     * Constructor.
     */
    public DefaultValue() {
    }

    /**
     * Constructor.
     * 
     * @param value
     *            double value
     */
    public DefaultValue(final double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public String getLabel() {
        return "" + this.value;
    }

    @Override
    public void setValue(final double value) {
        this.value = value;
    }

    @Override
    public void setLabel(final String newLabel) {
    }
}
