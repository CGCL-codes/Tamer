package org.jecars.support;

import org.jecars.binary.CB_Message;
import org.jecars.binary.ECB_Message;

/**
 *
 * @author weert
 */
public class CARS_Buffer {

    private transient StringBuilder mStringBuilder = null;

    private transient CB_Message mMessage = null;

    public CARS_Buffer() {
        return;
    }

    public CARS_Buffer(final StringBuilder pStringBuilder) {
        mStringBuilder = pStringBuilder;
        return;
    }

    public CB_Message getMessage() {
        return mMessage;
    }

    public StringBuilder getBuffer() {
        return mStringBuilder;
    }

    private CB_Message getOrCreateMessage() {
        if (mMessage == null) {
            mMessage = new CB_Message();
        }
        return mMessage;
    }

    public CARS_Buffer addMessage(final ECB_Message pMessage, final String pParam) {
        final CB_Message m = getOrCreateMessage();
        return this;
    }

    public CARS_Buffer append(final String pS) {
        if (mStringBuilder != null) {
            mStringBuilder.append(pS);
        }
        return this;
    }

    public CARS_Buffer append(final char pL) {
        if (mStringBuilder != null) {
            mStringBuilder.append(pL);
        }
        return this;
    }

    public CARS_Buffer append(final long pL) {
        if (mStringBuilder != null) {
            mStringBuilder.append(pL);
        }
        return this;
    }

    public int length() {
        if (mStringBuilder != null) {
            return mStringBuilder.length();
        }
        return -1;
    }

    @Override
    public String toString() {
        if (mStringBuilder != null) {
            return mStringBuilder.toString();
        }
        return super.toString();
    }
}
