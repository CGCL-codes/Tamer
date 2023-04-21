package Parser;

/**
 * Variablen werden in {@link StackFrame}s gespeichert. Sie bestehen aus Name, Wert, Typ und Zugriffsrechten.
 * @author Peter Frentrup
 */
public class Variable {

    public enum AccessRight {

        PUBLIC, PRIVATE
    }

    public boolean changed;

    private String _name;

    private DataType _type;

    private Data _value;

    private boolean _constant;

    private AccessRight _access;

    /** 
	 * Do not modify! For internal use only.
	 * To <u>read</u>, use stack() instead.
	 */
    public StackFrame ___stack;

    public String name() {
        return _name;
    }

    public DataType type() {
        return _type;
    }

    public Data value() {
        return _value;
    }

    public boolean constant() {
        return _constant;
    }

    public AccessRight access() {
        return _access;
    }

    public StackFrame stack() {
        return ___stack;
    }

    public void value(Data v) throws CastException {
        if (_type != null && v != null) _value = _type.cast(v); else _value = v;
        changed = true;
    }

    public Variable(String name, DataType type, Data value) {
        this(name, AccessRight.PUBLIC, false, type, value);
    }

    public Variable(String name, AccessRight access, DataType type, Data value) {
        this(name, access, false, type, value);
    }

    public Variable(String name, boolean constant, DataType type, Data value) {
        this(name, AccessRight.PUBLIC, constant, type, value);
    }

    public Variable(String name, AccessRight access, boolean constant, DataType type, Data value) {
        ___stack = null;
        changed = true;
        _name = name;
        _type = type;
        if (value == null) {
            if (type == null) _value = null; else _value = type.defaultValue();
        } else {
            try {
                this.value(value);
            } catch (CastException e) {
                if (type == null) _value = null; else _value = type.defaultValue();
            }
        }
        _constant = constant;
        _access = access;
    }

    /**
	 * Everything but not nessessarily the value equals?
	 */
    public boolean samePattern(Variable var) {
        return var != null && (name() == var.name() || name() != null && name().equals(var.name())) && (type() == var.type() || type() != null && type().equals(var.type())) && constant() == var.constant() && access() == var.access() && (stack() == null || var.stack() == null || stack() == var.stack());
    }

    @Override
    public String toString() {
        return name();
    }
}
