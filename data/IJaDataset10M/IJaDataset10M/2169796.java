package tyRuBa.modes;

public abstract class BindingMode {

    public abstract boolean isBound();

    public abstract boolean isFree();

    public abstract boolean satisfyBinding(BindingMode mode);
}
