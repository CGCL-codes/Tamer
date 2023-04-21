package org.neblinux.nebliserver.scripts;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Hashtable;
import org.neblinux.nebliserver.Response;

public abstract class ProcesadorScript {

    protected Charset chars;

    protected File raiz;

    public ProcesadorScript(File raiz, Charset chs) {
        chars = chs;
        this.raiz = raiz;
    }

    public ProcesadorScript(File tmp, String chs) {
        this(tmp, Charset.forName(chs));
    }

    public abstract void addClasspath(String path);

    public abstract boolean isMiExtension(String extension);

    public abstract String mimeType();

    public abstract Response procesarScript(File webroot, String uri, Hashtable<String, String> header, Hashtable<String, String> get, Hashtable<String, String> post);

    public abstract void registrarExtension(String extension);
}
