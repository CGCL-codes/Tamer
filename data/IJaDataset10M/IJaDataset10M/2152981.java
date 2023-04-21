package mscheme.values;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import mscheme.exceptions.ImmutableException;
import mscheme.exceptions.InvalidStringIndexException;

public final class ScmString {

    public static final String id = "$Id: ScmString.java 699 2004-03-26 10:32:44Z sielenk $";

    public static char[] create(int size, char fill) {
        char[] result = new char[size];
        Arrays.fill(result, fill);
        return result;
    }

    public static char[] create(String javaString) {
        return javaString.toCharArray();
    }

    public static String toString(char[] s) {
        return new String(s);
    }

    public static char[] copy(char[] cs) {
        return (char[]) cs.clone();
    }

    public static int getLength(char[] s) {
        return s.length;
    }

    public static void set(char[] s, int index, char c) throws InvalidStringIndexException, ImmutableException {
        try {
            s[index] = c;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidStringIndexException(s, index);
        }
    }

    public static char get(char[] s, int index) throws InvalidStringIndexException {
        try {
            return s[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidStringIndexException(s, index);
        }
    }

    public static boolean equals(char[] s, Object other) {
        if (other instanceof char[]) {
            char[] otherString = (char[]) other;
            return toString(s).compareTo(toString(otherString)) == 0;
        } else {
            return false;
        }
    }

    public static void outputOn(char[] s, Writer destination, boolean doWrite) throws IOException {
        final String str = toString(s);
        if (doWrite) {
            destination.write('"');
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                switch(c) {
                    case '\n':
                        destination.write("\\n");
                        break;
                    case '"':
                        destination.write("\\\"");
                        break;
                    default:
                        destination.write(c);
                        break;
                }
            }
            destination.write('"');
        } else {
            destination.write(str);
        }
    }
}
