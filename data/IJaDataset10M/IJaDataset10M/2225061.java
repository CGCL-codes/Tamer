package com.android.im.imps;

import com.android.internal.util.HexDump;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Log;

public class ImpsLog {

    public static final String TAG = "IMPS";

    public static final String PACKET_TAG = "IMPS/Packet";

    public static final boolean DEBUG = true;

    private static PrimitiveSerializer mSerialzier;

    private ImpsLog() {
    }

    static {
        mSerialzier = new XmlPrimitiveSerializer("", "");
    }

    public static void dumpRawPacket(byte[] bytes) {
        Log.d(PACKET_TAG, HexDump.dumpHexString(bytes));
    }

    public static void dumpPrimitive(Primitive p) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            mSerialzier.serialize(p, out);
        } catch (IOException e) {
            Log.e(PACKET_TAG, "Bad Primitive");
        } catch (SerializerException e) {
            Log.e(PACKET_TAG, "Bad Primitive");
        }
        Log.d(PACKET_TAG, out.toString());
    }

    public static void log(Primitive primitive) {
        if (DEBUG) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                mSerialzier.serialize(primitive, out);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SerializerException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            Log.i(TAG, out.toString());
        }
    }

    public static void log(String info) {
        if (DEBUG) {
            Log.d(TAG, info);
        }
    }

    public static void logError(Throwable t) {
        Log.e(TAG, "", t);
    }

    public static void logError(String info, Throwable t) {
        Log.e(TAG, info, t);
    }

    public static void logError(String info) {
        Log.e(TAG, info);
    }
}
