package edu.rice.cs.cunit.util;

/**
 * Mutable reference.
 * @author Mathias Ricken
 */
public class Ref<O> {

    private O _o;

    public Ref() {
    }

    public Ref(O o) {
        _o = o;
    }

    public O get() {
        return _o;
    }

    public void set(O o) {
        _o = o;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ref ref = (Ref) o;
        if (_o != null ? !_o.equals(ref._o) : ref._o != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (_o != null ? _o.hashCode() : 0);
    }
}
