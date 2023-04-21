package org.sepp.utils.format;

import org.sepp.exceptions.FormatException;
import org.sepp.utils.Constants;
import org.sepp.utils.format.interfaces.FormatUtils;

/**
 * @author <a href="mailto:stefan.kraxberger@iaik.tugraz.at">Stefan
 *         Kraxberger</a>
 * 
 */
public class FormatUtilsFactory {

    public static FormatUtils instance;

    public static void init() {
        try {
            if (Constants.JAVA_PLATFORM.equalsIgnoreCase("ME")) {
                instance = (FormatUtils) Class.forName("org.sepp.utils.format.implementation.IAIKMEUtils").newInstance();
            } else {
                instance = (FormatUtils) Class.forName("org.sepp.utils.format.implementation.IAIKSEUtils").newInstance();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String toBase64String(byte[] data) {
        if (!(instance instanceof FormatUtilsFactory)) {
            init();
        }
        return instance.toBase64String(data);
    }

    public static byte[] toBase64Bytes(String data) {
        if (!(instance instanceof FormatUtilsFactory)) {
            init();
        }
        return instance.toBase64Bytes(data);
    }

    public static String fromBase64Bytes(byte[] data) throws FormatException {
        if (!(instance instanceof FormatUtilsFactory)) {
            init();
        }
        return instance.fromBase64Bytes(data);
    }

    public static byte[] fromBase64String(String data) throws FormatException {
        if (!(instance instanceof FormatUtilsFactory)) {
            init();
        }
        return instance.fromBase64String(data);
    }

    public static String toASCIIString(byte[] data) {
        if (!(instance instanceof FormatUtilsFactory)) {
            init();
        }
        return instance.toASCIIString(data);
    }

    public static byte[] toASCIIBytes(String data) {
        if (!(instance instanceof FormatUtilsFactory)) {
            init();
        }
        return instance.toASCIIBytes(data);
    }
}
