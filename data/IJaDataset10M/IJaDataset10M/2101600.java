package de.andreavicentini.mosaicdomain.test2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.magiclabs.mosaic.Mosaic;
import org.magiclabs.mosaic.Mosaic.Mixins;
import org.magiclabs.mosaic.Mosaic.This;

@Mixins(DirtyFeature.Mixin.class)
public interface DirtyFeature {

    void setDirty();

    boolean isDirty();

    class SetDirtySideEffect implements Mosaic.ISideEffect, InvocationHandler {

        @This
        DirtyFeature domo;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            domo.setDirty();
            return null;
        }
    }

    class Mixin implements DirtyFeature {

        private boolean dirty;

        public void setDirty() {
            this.dirty = true;
        }

        public boolean isDirty() {
            return this.dirty;
        }
    }
}
