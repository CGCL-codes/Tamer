package com.bayareasoftware.chartengine.model;

public class Arg implements PropertySerializable {

    public ArgType argType;

    public String argValue;

    private transient ISeries series;

    public static final String PROP_argType = "argType";

    public static final String PROP_argValue = "argValue";

    public boolean deserializeFromProps(SimpleProps p, String prefix) {
        boolean ret = false;
        SimpleProps props = p.subset(prefix);
        String key;
        key = PROP_argType;
        if (props.containsKey(key)) {
            this.argType = ArgType.decode(props.get(key));
            props.remove(key);
            ret = true;
        }
        key = PROP_argValue;
        if (props.containsKey(key)) {
            this.argValue = props.get(key);
            props.remove(key);
            ret = true;
        }
        return ret;
    }

    public SimpleProps serializeToProps(SimpleProps p, String prefix) {
        if (p == null) p = new SimpleProps();
        if (this.argType != null) {
            p.set(prefix, PROP_argType, this.argType.toString());
        }
        if (this.argValue != null) {
            p.set(prefix, PROP_argValue, this.argValue);
        }
        return p;
    }

    public Arg() {
    }

    public Arg(ArgType at, double value) {
        if (at != ArgType.NUMBER) {
            throw new IllegalArgumentException("wrong constructor for " + at);
        }
        this.argType = at;
        this.argValue = "" + value;
    }

    public Arg(ArgType argType, Integer value) {
        this.argType = argType;
        if (value == null) {
            throw new NullPointerException("null value");
        }
        this.argValue = value.toString();
    }

    public Arg(ArgType argType, String argValue) {
        this.argType = argType;
        this.argValue = argValue;
    }

    public ArgType getArgType() {
        return argType;
    }

    public void setArgType(ArgType argType) {
        this.argType = argType;
    }

    public String getArgValue() {
        return argValue;
    }

    public void setArgValue(String argValue) {
        this.argValue = argValue;
    }

    public Double asDouble() {
        Double ret = null;
        try {
            if (argValue != null) {
                ret = Double.parseDouble(argValue);
            }
        } catch (NumberFormatException nfe) {
        }
        return ret;
    }

    public Long asLong() {
        Double d = asDouble();
        return d != null ? d.longValue() : null;
    }

    public Integer asInt() {
        Double d = asDouble();
        return d != null ? d.intValue() : null;
    }

    public boolean asBoolean() {
        return "true".equalsIgnoreCase(argValue) || "yes".equalsIgnoreCase(argValue);
    }

    public ISeries asSeries() {
        return series;
    }

    public void setSeries(ISeries s) {
        series = s;
    }

    public String toString() {
        return this.argType + ": " + this.argValue;
    }

    public int hashCode() {
        int ret = -1;
        if (this.argType != null) {
            ret ^= this.argType.hashCode();
        }
        if (this.argValue != null) {
            ret ^= this.argValue.hashCode();
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Arg)) return false;
        Arg other = (Arg) o;
        if (!objEquals(this.argType, other.argType) || !objEquals(this.argValue, other.argValue)) return false;
        return (this.argValue.equals(other.argValue));
    }

    private boolean objEquals(Object o1, Object o2) {
        if (o1 == null) {
            return (o2 == null);
        }
        return o1.equals(o2);
    }
}
