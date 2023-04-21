package net.sourceforge.freejava.loader;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

public class CustomClassLoader extends ClassLoader {

    public Class<?> _defineClass(String name, ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
        return defineClass(name, b, protectionDomain);
    }

    public Class<?> _defineClass(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClass(name, b, off, len);
    }

    public Class<?> _defineClass(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        return defineClass(name, b, off, len, protectionDomain);
    }
}
