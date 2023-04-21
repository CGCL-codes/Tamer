package com.google.gwt.typedarrays.client;

import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Int8Array;
import com.google.gwt.typedarrays.shared.Int8ArrayTest;
import com.google.gwt.typedarrays.shared.TypedArrays;

/**
 * Test client {@link Int8Array} implementations.
 */
public class GwtInt8ArrayTest extends Int8ArrayTest {

    private static native JsArrayInteger getJsoArray();

    @Override
    public String getModuleName() {
        return "com.google.gwt.typedarrays.TypedArraysTest";
    }

    public void testCreateJsArray() {
        if (!TypedArrays.isSupported()) {
            return;
        }
        JsArrayInteger src = getJsoArray();
        Int8Array array = JsUtils.createInt8Array(src);
        validateArrayContents(array, 0);
    }

    public void testSetJsArray() {
        if (!TypedArrays.isSupported()) {
            return;
        }
        ArrayBuffer buf = TypedArrays.createArrayBuffer(12);
        Int8Array array = TypedArrays.createInt8Array(buf);
        setFromJsArray(array, 0);
        validateArrayContents(array, 0);
        buf = TypedArrays.createArrayBuffer(12);
        array = TypedArrays.createInt8Array(buf);
        setFromJsArray(array, 1);
        validateArrayContents(array, 1);
    }

    /**
   * Initialize from a JSO rather than a Java array
   */
    protected void setFromJsArray(Int8Array array, int offset) {
        JsUtils.set(array, getJsoArray(), offset);
    }
}
