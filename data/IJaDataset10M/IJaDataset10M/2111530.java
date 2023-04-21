package uk.org.ogsadai.client.toolkit.presentation.cxf;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.common.util.Base64Exception;

public class CXFBase64Mapper {

    public static void decode(char[] id, int o, int l, java.io.OutputStream ostream) throws Base64Exception {
        Base64Utility.decode(id, o, l, ostream);
    }

    public static byte[] decode(String id) throws Base64Exception {
        return Base64Utility.decode(id);
    }

    public static void decode(java.lang.String id, java.io.OutputStream ostream) throws Base64Exception {
        Base64Utility.decode(id, ostream);
    }

    public static byte[] decode(char[] id, int o, int l) throws Base64Exception {
        return Base64Utility.decodeChunk(id, o, l);
    }

    public static String encode(byte[] id) throws Base64Exception {
        return Base64Utility.encode(id);
    }

    public static void encode(byte[] id, int o, int l, java.io.Writer writer) throws Base64Exception {
        Base64Utility.encode(id, o, l, writer);
    }

    public static String encode(byte[] id, int o, int l) throws Base64Exception {
        return String.copyValueOf(Base64Utility.encodeChunk(id, o, l));
    }

    public static void encode(byte[] id, int o, int l, java.io.OutputStream ostream) throws Base64Exception {
        Base64Utility.encodeChunk(id, o, l, ostream);
    }
}
