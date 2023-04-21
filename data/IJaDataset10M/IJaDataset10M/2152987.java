package fitlibraryGeneric.specify.unbound;

import fitlibrary.object.DomainFixtured;

public class ClassHasUnboundTypeVariable<T> implements DomainFixtured {

    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
