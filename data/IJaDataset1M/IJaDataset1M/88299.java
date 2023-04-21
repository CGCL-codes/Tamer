package type;

import symbol.Symbol;

public class EmptyRecord extends Record {

    public EmptyRecord() {
        super(null, null, null);
    }

    public boolean fits(Type t) {
        return this == t.actual();
    }

    public boolean isEmpty() {
        return true;
    }

    public java.lang.String toString() {
        return "EMPTY_RECORD";
    }

    public Type findField(Symbol fieldName) {
        return null;
    }
}
