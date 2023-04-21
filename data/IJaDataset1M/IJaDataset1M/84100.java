package fr.megiste.interloc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utilities {

    public static void copy(InputStream in, OutputStream out) {
        try {
            int byteCount = 0;
            byte buffer[] = new byte[4096];
            for (int bytesRead = -1; (bytesRead = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            throw new InterlocException(e);
        }
    }
}
