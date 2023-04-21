package javag.data;

public interface ValueChangedListener<type> {

    public void valueChanged(Value<type> value, type oldValue);
}
