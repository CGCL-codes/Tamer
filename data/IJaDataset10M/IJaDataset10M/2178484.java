package com.jeantessier.classreader;

import java.util.*;
import org.apache.log4j.*;

public final class SignatureHelper {

    private static Map<String, String> conversion = new HashMap<String, String>();

    static {
        conversion.put("B", "byte");
        conversion.put("C", "char");
        conversion.put("D", "double");
        conversion.put("F", "float");
        conversion.put("I", "int");
        conversion.put("J", "long");
        conversion.put("S", "short");
        conversion.put("V", "void");
        conversion.put("Z", "boolean");
    }

    private SignatureHelper() {
    }

    public static String convert(String type) {
        String result = null;
        Logger.getLogger(SignatureHelper.class).debug("Begin Convert(\"" + type + "\")");
        if (type.length() == 1) {
            result = conversion.get(type);
        } else if (type.charAt(0) == 'L') {
            result = ClassNameHelper.path2ClassName(type.substring(1, type.indexOf(';')));
        } else if (type.charAt(0) == 'T') {
            result = ClassNameHelper.path2ClassName(type.substring(1, type.indexOf(';')));
        } else if (type.charAt(0) == '[') {
            result = convert(type.substring(1)) + "[]";
        }
        Logger.getLogger(SignatureHelper.class).debug("End   Convert(\"" + type + "\"): \"" + result + "\"");
        return result;
    }

    public static String getSignature(String descriptor) {
        StringBuffer result = new StringBuffer();
        Logger.getLogger(SignatureHelper.class).debug("Begin Signature(\"" + descriptor + "\")");
        result.append("(");
        int start = descriptor.indexOf("(") + 1;
        int end = descriptor.indexOf(")");
        Iterator i = new SignatureIterator(descriptor.substring(start, end));
        while (i.hasNext()) {
            result.append(i.next());
            if (i.hasNext()) {
                result.append(", ");
            }
        }
        result.append(")");
        Logger.getLogger(SignatureHelper.class).debug("End   Signature(\"" + descriptor + "\"): \"" + result + "\"");
        return result.toString();
    }

    public static int getParameterCount(String descriptor) {
        int result = 0;
        Logger.getLogger(SignatureHelper.class).debug("Begin ParameterCount(\"" + descriptor + "\")");
        int start = descriptor.indexOf("(") + 1;
        int end = descriptor.indexOf(")");
        Iterator i = new SignatureIterator(descriptor.substring(start, end));
        while (i.hasNext()) {
            i.next();
            result++;
        }
        Logger.getLogger(SignatureHelper.class).debug("End   ParameterCount(\"" + descriptor + "\"): \"" + result + "\"");
        return result;
    }

    public static String getReturnType(String descriptor) {
        return convert(descriptor.substring(descriptor.lastIndexOf(")") + 1));
    }

    public static String getType(String descriptor) {
        return convert(descriptor);
    }
}

class SignatureIterator implements Iterator {

    private String descriptor;

    private int currentPos = 0;

    public SignatureIterator(String descriptor) {
        this.descriptor = descriptor;
    }

    public boolean hasNext() {
        return currentPos < descriptor.length();
    }

    public Object next() {
        String result;
        if (hasNext()) {
            int nextPos = currentPos;
            while (descriptor.charAt(nextPos) == '[') {
                nextPos++;
            }
            if (descriptor.charAt(nextPos) == 'L') {
                nextPos = descriptor.indexOf(";", nextPos);
            }
            if (descriptor.charAt(nextPos) == 'T') {
                nextPos = descriptor.indexOf(";", nextPos);
            }
            result = SignatureHelper.convert(descriptor.substring(currentPos, nextPos + 1));
            currentPos = nextPos + 1;
        } else {
            throw new NoSuchElementException();
        }
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
