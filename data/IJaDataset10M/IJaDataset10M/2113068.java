package org.regadou.nalasys.system.handlers;

import org.regadou.nalasys.system.*;
import java.io.*;

public class TextHandler implements DataHandler {

    private LanguageHandler handler;

    private String[] mimetypes;

    public TextHandler(LanguageHandler handler) {
        mimetypes = new String[] { handler.getMimetype() };
        this.handler = handler;
    }

    public Object input(InputStream in) throws IOException {
        return handler.parse(new String(Stream.getBytes(in)));
    }

    public boolean output(OutputStream out, Object obj, String mimetype) throws IOException {
        out.write(handler.print(obj).getBytes());
        return true;
    }

    public Class getObjectClass() {
        return null;
    }

    public String[] getMimetypes() {
        return mimetypes;
    }
}
