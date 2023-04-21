package com.luzan.common.processor;

import com.mockobjects.util.AssertMo;

public class CheckExpectedException {

    public static interface Caller<T> {

        T method(Object... params);
    }

    private CheckExpectedException() {
    }

    public static <T> T call(Caller<T> caller, Class<? extends Throwable> expectedException, Object... params) {
        T val;
        try {
            val = caller.method(params);
        } catch (Throwable e) {
            if (expectedException == null || !(e.getClass().isAssignableFrom(expectedException))) AssertMo.fail("unexpected exception: \n" + e);
            return null;
        }
        if (expectedException != null) AssertMo.fail(expectedException.getSimpleName() + " exception expected");
        return val;
    }
}
