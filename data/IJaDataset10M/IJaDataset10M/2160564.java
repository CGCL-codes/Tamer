package com.dyuproject.protostuff.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

/**
 * Utility for loading protos from various input.
 *
 * @author David Yu
 * @created Dec 24, 2009
 */
public final class ProtoUtil {

    private ProtoUtil() {
    }

    public static void loadFrom(InputStream in, Proto target) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(in);
        ProtoLexer lexer = new ProtoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProtoParser parser = new ProtoParser(tokens);
        parser.parse(target);
    }

    public static Proto parseProto(File file) {
        Proto proto = new Proto(file);
        try {
            loadFrom(file, proto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return proto;
    }

    public static void loadFrom(File file, Proto target) throws Exception {
        FileInputStream in = new FileInputStream(file);
        try {
            loadFrom(in, target);
        } finally {
            in.close();
        }
    }

    public static void loadFrom(URL resource, Proto target) throws Exception {
        InputStream in = resource.openStream();
        try {
            loadFrom(in, target);
        } finally {
            in.close();
        }
    }

    public static StringBuilder toCamelCase(String name) {
        StringBuilder buffer = new StringBuilder();
        int toUpper = 0;
        char c;
        for (int i = 0, len = name.length(); i < len; ) {
            c = name.charAt(i++);
            if (c == '_') {
                if (i == len) break;
                if (buffer.length() != 0) toUpper++;
                continue;
            } else if (toUpper != 0) {
                if (c > 96 && c < 123) {
                    buffer.append((char) (c - 32));
                    toUpper = 0;
                } else if (c > 64 && c < 91) {
                    buffer.append(c);
                    toUpper = 0;
                } else {
                    while (toUpper > 0) {
                        buffer.append('_');
                        toUpper--;
                    }
                    buffer.append(c);
                }
            } else {
                if (buffer.length() == 0 && c > 64 && c < 91) buffer.append((char) (c + 32)); else buffer.append(c);
            }
        }
        return buffer;
    }

    public static StringBuilder toPascalCase(String name) {
        StringBuilder buffer = toCamelCase(name);
        char c = buffer.charAt(0);
        if (c > 96 && c < 123) buffer.setCharAt(0, (char) (c - 32));
        return buffer;
    }

    public static StringBuilder toUnderscoreCase(String name) {
        StringBuilder buffer = new StringBuilder();
        boolean toLower = false, appendUnderscore = false;
        for (int i = 0, len = name.length(); i < len; ) {
            char c = name.charAt(i++);
            if (c == '_') {
                if (i == len) break;
                if (buffer.length() != 0) appendUnderscore = true;
                continue;
            }
            if (appendUnderscore) buffer.append('_');
            if (c > 96 && c < 123) {
                buffer.append(c);
                toLower = true;
            } else if (c > 64 && c < 91) {
                if (toLower) {
                    if (!appendUnderscore) buffer.append('_');
                    toLower = false;
                }
                buffer.append((char) (c + 32));
            } else {
                buffer.append(c);
                toLower = false;
            }
            appendUnderscore = false;
        }
        return buffer;
    }

    public static void main(String[] args) {
        String[] gg = { "foo_bar_baz", "fooBarBaz", "FooBarBaz", "foo_bar_baz", "____Foo____Bar___Baz____" };
        for (String g : gg) {
            System.err.println(toCamelCase(g));
            System.err.println(toPascalCase(g));
            System.err.println(toUnderscoreCase(g));
            System.err.println(toUnderscoreCase(g).toString().toUpperCase());
        }
    }
}
