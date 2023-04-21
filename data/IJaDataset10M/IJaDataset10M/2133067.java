package org.strophe.sph;

import java.lang.*;
import org.strophe.sph.*;

public final class SphListUnit implements SphUnit, SphDispatch {

    Integer size;

    SphObjectScope scope;

    public SphListUnit(SphObjectScope scope) {
        this.scope = scope;
        SphParametersScope ps = (SphParametersScope) scope;
        SphParameters params = (SphParameters) ps.params;
        int size = params.units.length;
        this.size = new Integer(size);
    }

    private Object getObject(int index) {
        return scope.sphObjectScope_getObject(5, index);
    }

    public Object sphUnit_compute() {
        return this;
    }

    public Object sphDispatch_getObject(int index) {
        return size;
    }

    public SphUnit sphDispatch_callFunction(int index, SphDispatch params) {
        return new ListAt(params);
    }

    class ListAt implements SphUnit {

        SphDispatch params;

        ListAt(SphDispatch params) {
            this.params = params;
        }

        public Object sphUnit_compute() {
            Integer io = (Integer) params.sphDispatch_getObject(0);
            if (io == null) return null;
            int i = io.intValue();
            if (i < 1) return null;
            if (i > size.intValue()) return null;
            return getObject(i - 1);
        }
    }
}
