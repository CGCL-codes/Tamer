package org.asmatron.messengine.observ;

public class ObservedProperty<S, V> {

    private final S source;

    private Observable<ObservePropertyChanged<S, V>> ob = new Observable<ObservePropertyChanged<S, V>>();

    private V value;

    private ObservePropertyChanged<S, V> lastEvent;

    public ObservedProperty(S s, V value) {
        this.source = s;
        this.value = value;
    }

    public ObservedProperty(S s) {
        this.source = s;
    }

    public void setValue(V v) {
        if (v == this.value || (this.value != null && this.value.equals(v))) {
            return;
        }
        setValueAndRaiseEvent(v);
    }

    public V getValue() {
        return value;
    }

    public ObserverCollection<ObservePropertyChanged<S, V>> on() {
        return ob;
    }

    public void addAndLaunch(Observer<ObservePropertyChanged<S, V>> observer) {
        ObservePropertyChanged<S, V> event = lastEvent;
        if (event == null) {
            event = new ObservePropertyChanged<S, V>(source, value, value);
        }
        observer.observe(event);
        on().add(observer);
    }

    public void setValueAndRaiseEvent(V newValue) {
        V oldValue = this.value;
        this.value = newValue;
        lastEvent = new ObservePropertyChanged<S, V>(source, newValue, oldValue);
        ob.fire(lastEvent);
    }

    public static <S, V> ObservedProperty<S, V> newProp(S s) {
        return new ObservedProperty<S, V>(s);
    }
}
