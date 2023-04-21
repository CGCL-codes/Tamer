package org.geometerplus.zlibrary.core.html;

import java.nio.charset.CharsetDecoder;

public final class ZLHtmlAttributeMap {

    private ZLByteBuffer[] myKeys;

    private ZLByteBuffer[] myValues;

    private int mySize;

    public ZLHtmlAttributeMap() {
        myKeys = new ZLByteBuffer[8];
        myValues = new ZLByteBuffer[8];
    }

    public void put(ZLByteBuffer key, ZLByteBuffer value) {
        final int size = mySize++;
        ZLByteBuffer[] keys = myKeys;
        if (keys.length == size) {
            keys = new ZLByteBuffer[size << 1];
            System.arraycopy(myKeys, 0, keys, 0, size);
            myKeys = keys;
            final ZLByteBuffer[] values = new ZLByteBuffer[size << 1];
            System.arraycopy(myValues, 0, values, 0, size);
            myValues = values;
        }
        keys[size] = key;
        myValues[size] = value;
    }

    public ZLByteBuffer getValue(String lcPattern) {
        int index = mySize;
        if (index > 0) {
            final ZLByteBuffer[] keys = myKeys;
            while (--index >= 0) {
                if (keys[index].equalsToLCString(lcPattern)) {
                    return myValues[index];
                }
            }
        }
        return null;
    }

    public String getStringValue(String lcPattern, CharsetDecoder decoder) {
        final ZLByteBuffer buffer = getValue(lcPattern);
        return (buffer != null) ? buffer.toString(decoder) : null;
    }

    public int getSize() {
        return mySize;
    }

    public ZLByteBuffer getKey(int index) {
        return myKeys[index];
    }

    public void clear() {
        mySize = 0;
    }
}
