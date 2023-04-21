package org.uithin.tools.constraints;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyVeto;
import zz.utils.properties.IRWProperty;
import EDU.Washington.grad.gjb.cassowary.ISimpleVariable;

public abstract class PropertyWrapper<T extends Number> implements ISimpleVariable, IPropertyVeto<T> {

    private IRWProperty<T> itsProperty;

    private IConstraintVeto<T> itsVetoDelegate;

    /**
	 * This flag indicates if the {@link org.uithin.tools.constraints.ConstraintSystem}
	 * is currently editing this variable.
	 */
    private boolean itsEditing = false;

    public PropertyWrapper(IRWProperty<T> aProperty) {
        itsProperty = aProperty;
    }

    protected IRWProperty<T> getProperty() {
        return itsProperty;
    }

    public void setVetoDelegate(IConstraintVeto<T> aDelegate) {
        if (aDelegate != null) {
            assert itsVetoDelegate == null;
            itsVetoDelegate = aDelegate;
            itsProperty.addHardListener(this);
        } else {
            assert itsVetoDelegate != null;
            itsProperty.removeListener(this);
            itsVetoDelegate = null;
        }
    }

    public void setEditing(boolean aEditing) {
        itsEditing = aEditing;
    }

    public boolean canChangeProperty(IProperty<T> aProperty, T aOldValue, T aNewValue) {
        if (itsEditing) return true;
        return itsVetoDelegate.canChangeProperty(this, aOldValue.doubleValue(), aNewValue.doubleValue());
    }

    public void propertyChanged(IProperty<T> aProperty, T aOldValue, T aNewValue) {
        itsVetoDelegate.propertyChanged(this, aOldValue.doubleValue(), aNewValue.doubleValue());
    }

    public void propertyValueChanged(IProperty<T> aProperty) {
    }

    public final void change_value(double aValue) {
        set(aValue);
    }

    /**
	 * Subclasses must implement this method to convert the double to the 
	 * appropriate {@link Number} subclass.
	 */
    protected abstract void set(double aValue);

    public double value() {
        return itsProperty.get().doubleValue();
    }

    public final boolean isDummy() {
        return false;
    }

    public final boolean isExternal() {
        return true;
    }

    public final boolean isPivotable() {
        return false;
    }

    public final boolean isRestricted() {
        return false;
    }

    /**
	 * For in properties
	 * @author gpothier
	 *
	 */
    public static class IntWrapper extends PropertyWrapper<Integer> {

        public IntWrapper(IRWProperty<Integer> aProperty) {
            super(aProperty);
        }

        @Override
        protected void set(double aValue) {
            getProperty().set((int) aValue);
        }
    }

    public static class DoubleWrapper extends PropertyWrapper<Double> {

        public DoubleWrapper(IRWProperty<Double> aProperty) {
            super(aProperty);
        }

        @Override
        protected void set(double aValue) {
            getProperty().set(aValue);
        }
    }
}
