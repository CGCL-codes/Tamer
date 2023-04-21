package net.sourceforge.freejava.jdk6compat.jdk7emul;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;

public class Jdk7ZipFile {

    public static ZipFile newInstance(File file, int mode, Charset charset) throws IOException {
        return new ZipFile(file, mode);
    }
}
