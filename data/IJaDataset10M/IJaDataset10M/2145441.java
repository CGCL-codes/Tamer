package sibilant.object;

public class Slot<E extends Atom> extends Atom {

    public E data;

    public final Dir<? super E, ? super E> scope;

    public Slot(E data, Dir<? super E, ? super E> scope) {
        type = Type.SLOT;
        this.data = data;
        this.scope = scope;
    }
}
