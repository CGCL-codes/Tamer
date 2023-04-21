package net.sf.rcpforms.modeladapter.configuration;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;

public class IndexedPropertyValue extends AbstractObservableValue implements IObservableValue {

    /**
     * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
     */
    public Object getValueType() {
        throw new Error("IndexedPropertyValue.getValueType(): NYI (not yet implemented)");
    }

    /**
     * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
     */
    @Override
    protected Object doGetValue() {
        throw new Error("IndexedPropertyValue.doGetValue(): NYI (not yet implemented)");
    }
}
