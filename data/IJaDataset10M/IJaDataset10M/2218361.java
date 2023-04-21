package springclient.bind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class Value<T> {

    private static Logger logger = Logger.getLogger(Value.class);

    private String name;

    private T value;

    private List<ValueChangedListener<T>> listeners;

    public Value(Class<T> clazz) {
        listeners = new ArrayList<ValueChangedListener<T>>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        logger.debug(name + " [" + this.toString() + "] Returning value: " + value);
        return value;
    }

    public void setValue(T value) {
        logger.debug(name + " [" + this.toString() + "] New value: " + value);
        this.value = value;
        Iterator<ValueChangedListener<T>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            ValueChangedListener<T> listener = iterator.next();
            logger.debug(name + " [" + this.toString() + "] Notifying listener " + listener);
            listener.valueChanged(value);
        }
    }

    public void setValue(ValueChangedListener<T> setter, T value) {
        removeValueChangedListener(setter);
        this.setValue(value);
        addValueChangedListener(setter);
    }

    public void addValueChangedListener(ValueChangedListener<T> listener) {
        listeners.add(listener);
    }

    public void removeValueChangedListener(ValueChangedListener<T> listener) {
        listeners.remove(listener);
    }
}
