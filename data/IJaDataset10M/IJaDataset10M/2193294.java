package net.sourceforge.agsyslib.util.javac;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;

public class JavaSourceFromString extends SimpleJavaFileObject {

    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
