package jfraglets;

public class Symbol {

    public Symbol(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }

    public boolean isTrans() {
        return (((value > 0) && (value <= 10)) || ((value > FragletApp.SYM_STAR) && (value <= FragletApp.LAST_TRANSFORMATION)));
    }

    public boolean isMatch() {
        return ((value == FragletApp.SYM_MATCH) || (value == FragletApp.SYM_MATCHP));
    }

    public boolean equals(Object s) {
        return (value == ((Symbol) s).getValue());
    }

    public int hashCode() {
        return value;
    }

    private int value;
}
