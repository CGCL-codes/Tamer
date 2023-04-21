package de.easyswt.builder.model;

import java.util.Iterator;
import java.util.logging.Logger;
import de.easyswt.builder.model.internal.TrackedValue;

public abstract class MvcValue<T, LT extends MvcValueListener<T>> extends MvcProperty<LT> {

    public static final Logger LOGGER = Logger.getLogger(MvcValue.class.getName());

    private final TrackedValue<T> value;

    public MvcValue(MvcModel owner, T defaultValue, T initialValue) {
        super(owner);
        this.value = new TrackedValue<T>(defaultValue, initialValue);
    }

    public T getValue() {
        return value.getCurrentValue();
    }

    public T getDefaultValue() {
        return value.getDefaultValue();
    }

    public T getPreviousValue() {
        return value.getCheckpointedValue();
    }

    @Override
    public boolean isModified() {
        return isValueModified() || super.isModified();
    }

    public boolean isValueModified() {
        return value.isModified();
    }

    public boolean setValue(T newValue) {
        return value.setCurrentValue(newValue);
    }

    public boolean setValueAndNotify(T newValue) {
        boolean changed = setValue(newValue);
        getOwner().sendNotifications();
        return changed;
    }

    public void rollbackValue() {
        value.rollback();
    }

    public void rollbackValueAndNotify() {
        rollbackValue();
        getOwner().sendNotifications();
    }

    public abstract MvcCommand getChangeValueCommand();

    @Override
    public void reset() {
        value.reset();
        super.reset();
    }

    @Override
    protected void checkpoint() {
        value.checkpoint();
        super.checkpoint();
    }

    @Override
    protected void notifyListeners() {
        if (value.isModified()) {
            Iterator<LT> iterator = getListenersIterator();
            while (iterator.hasNext()) {
                LT listener = (LT) iterator.next();
                try {
                    listener.valueChanged(value.getCheckpointedValue(), value.getCurrentValue());
                } catch (Throwable t) {
                    MvcUtils.logListener(LOGGER, t);
                }
            }
        }
        super.notifyListeners();
    }
}
