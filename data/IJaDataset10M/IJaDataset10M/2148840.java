package uk.co.wilson.ng.runtime.metaclass;

import ng.runtime.metaclass.Misc;

public class MiscImpl implements Misc {

    private final Class<?> theClass;

    /**
   * @param theClass
   */
    public MiscImpl(final Class<?> theClass) {
        this.theClass = theClass;
    }

    public Class<?> getTheClass(final Object instance) {
        return this.theClass;
    }
}
