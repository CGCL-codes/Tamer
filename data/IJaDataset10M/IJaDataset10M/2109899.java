package cz.muni.fi.sindb2xhtml.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import cz.muni.fi.sindb2xhtml.Sindb2xhtmlException;

public class FileUtils {

    public static void copyfile(InputStream srcStream, String destFilePath) throws Sindb2xhtmlException {
        if (srcStream == null) {
            throw new IllegalArgumentException("InputStream is null.");
        }
        if (destFilePath == null) {
            throw new IllegalArgumentException("destFilePath is null.");
        }
        try {
            File f2 = new File(destFilePath);
            OutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = srcStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            srcStream.close();
            out.close();
        } catch (FileNotFoundException e) {
            throw new Sindb2xhtmlException(e);
        } catch (IOException e) {
            throw new Sindb2xhtmlException(e);
        }
    }
}
