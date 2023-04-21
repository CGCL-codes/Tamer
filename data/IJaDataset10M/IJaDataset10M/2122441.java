package com.bluesky.my4gl.internalClass.lang;

import com.bluesky.my4gl.core.Object;
import com.bluesky.my4gl.core.ObjectImpl;

public class NativeObject<T> extends ObjectImpl implements Object {

    private T nativeValue;

    public T getNativeValue() {
        return nativeValue;
    }

    public void setNativeValue(T nativeObject) {
        this.nativeValue = nativeObject;
    }
}
