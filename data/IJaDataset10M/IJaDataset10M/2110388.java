package net.sf.jour.processor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javassist.CannotCompileException;
import javassist.CtClass;

public class InstrumentedEntry implements Entry {

    private Entry orig;

    private CtClass ctClass;

    public InstrumentedEntry(Entry orig, CtClass ctClass) {
        this.orig = orig;
        this.ctClass = ctClass;
    }

    public InputStream getInputStream() throws IOException {
        try {
            return new ByteArrayInputStream(ctClass.toBytecode());
        } catch (CannotCompileException e) {
            throw new CompileIOException(e);
        }
    }

    public Entry getOrigin() {
        return orig;
    }

    public String getName() {
        return this.orig.getName();
    }

    public long getSize() {
        return -1;
    }

    public long getTime() {
        return this.orig.getTime();
    }

    public boolean isClass() {
        return true;
    }

    public boolean isDirectory() {
        return false;
    }
}
