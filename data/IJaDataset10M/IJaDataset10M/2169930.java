package ordo;

import java.io.*;
import Freenet.crypt.Util;
import Freenet.crypt.CryptoKey;

public class CryptoKeyWirer {

    public static void write(Object v, OutputStream out) throws IOException {
        ((CryptoKey) v).write(out);
    }

    public static CryptoKey read(InputStream in) throws IOException {
        return CryptoKey.read(in);
    }
}
