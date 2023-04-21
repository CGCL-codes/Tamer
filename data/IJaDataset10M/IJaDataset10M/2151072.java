package jsbsim.referers;

/**
 * RefAny provides provides generic way to pass referencies to atomic types.
 * Templating requires object types, so the only usable option here is to init
 * such referencies with <Double>, <Integer> and so on.
 * back and th
 * $Id$
 */
public class RefAny<E> {

    public E value;

    public RefAny(E value) {
        this.value = value;
    }

    @Override
    public RefAny<E> clone() {
        RefAny<E> result = new RefAny<E>(value);
        result.value = this.value;
        return result;
    }

    public void delete() {
        value = null;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
