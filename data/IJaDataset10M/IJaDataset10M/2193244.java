package uk.ac.lkl.migen.system.expresser.model;

import uk.ac.lkl.common.util.value.Value;
import uk.ac.lkl.common.util.event.UpdateEvent;
import uk.ac.lkl.common.util.event.UpdateListener;
import uk.ac.lkl.common.util.expression.Expression;

/**
 * 
 * doesn't allow direct setting of value since value is set by internal
 * mechanism. Now gives out reference to this so is only ctor that knows how to
 * set a value
 * 
 */
public class DrivenValueSource<T extends Value<T>> extends AbstractDrivenValueSource<T> {

    private ValueSource<T> valueSource;

    private UpdateListener<ValueSource<T>> updateListener = new UpdateListener<ValueSource<T>>() {

        @Override
        public void objectUpdated(UpdateEvent<ValueSource<T>> e) {
            fireObjectUpdated();
        }
    };

    public DrivenValueSource(ValueSource<T> valueSource) {
        if (valueSource == null) throw new IllegalArgumentException("valueSource cannot be null");
        this.valueSource = valueSource;
        addChangeListener();
    }

    private void addChangeListener() {
        valueSource.addUpdateListener(updateListener);
    }

    private void removeChangeListener() {
        valueSource.removeUpdateListener(updateListener);
    }

    @Override
    public final T getValue() {
        return valueSource.getValue();
    }

    @Override
    public final Expression<T> getExpression() {
        return valueSource.getExpression();
    }

    @Override
    public boolean setValue(T newValue) {
        return false;
    }

    @Override
    public boolean isValueSettable() {
        return false;
    }

    @Override
    public void removeAll() {
        removeChangeListener();
        valueSource.removeAll();
        super.removeAll();
    }
}
