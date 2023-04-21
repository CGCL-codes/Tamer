package org.jmetis.binding.eclipse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.jmetis.binding.IObservableProperty;

/**
 * {@code ObservableValue}
 * 
 * @author aerlach
 */
public class ObservableValue extends AbstractObservableValue {

    private IObservableProperty baseObservableValue;

    private PropertyChangeListener valueChangeHandler;

    /**
	 * Constructs a new {@code ObservableValue} instance.
	 */
    public ObservableValue(IObservableProperty baseObservableValue) {
        super();
        this.baseObservableValue = baseObservableValue;
    }

    @Override
    protected void fireValueChange(ValueDiff diff) {
        super.fireValueChange(diff);
    }

    protected void forwardValueChanged(PropertyChangeEvent propertyChangeEvent) {
        if (hasListeners()) {
            final ValueDiff diff = Diffs.createValueDiff(propertyChangeEvent.getOldValue(), propertyChangeEvent.getNewValue());
            getRealm().exec(new Runnable() {

                public void run() {
                    ObservableValue.this.fireValueChange(diff);
                }
            });
        }
    }

    @Override
    protected void firstListenerAdded() {
        valueChangeHandler = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                ObservableValue.this.forwardValueChanged(propertyChangeEvent);
            }
        };
        baseObservableValue.addPropertyChangeListener(valueChangeHandler);
    }

    @Override
    protected void lastListenerRemoved() {
        baseObservableValue.removePropertyChangeListener(valueChangeHandler);
    }

    public Object getValueType() {
        return baseObservableValue.getValueType();
    }

    @Override
    protected Object doGetValue() {
        return baseObservableValue.getValue();
    }

    @Override
    protected void doSetValue(Object value) {
        baseObservableValue.setValue(value);
    }

    @Override
    public synchronized void dispose() {
        lastListenerRemoved();
        super.dispose();
    }

    @Override
    public String toString() {
        return baseObservableValue.toString();
    }
}
