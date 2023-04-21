package jaxlib.util;

import java.util.ConcurrentModificationException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import jaxlib.array.CharArrays;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.jaxlib_private.CharTools;
import jaxlib.jaxlib_private.util.UnsafeStringConstructor;
import jaxlib.lang.Ints;

/**
 * Provides static utilities for working with instances of {@link String} and {@link CharSequence}.
 *
 * @see StringBuffers
 * @see jaxlib.array.CharArrays
 * @see jaxlib.buffer.CharBuffers
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: Strings.java,v 1.6 2004/10/13 21:06:34 joerg_wassmer Exp $
 */
public class Strings extends CharSequences {

    protected Strings() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final String[] EMPTY_ARRAY = new String[0];

    private static final UnsafeStringConstructor unsafeStringConstructor = UnsafeStringConstructor.getInstance(true);

    private static final boolean UNSAFE = unsafeStringConstructor.unsafe;

    private static String internalToString(char[] a) {
        return Strings.unsafeStringConstructor.newStringUsingArray(a);
    }

    public static String alignCenter(CharSequence s, int len) {
        return alignCenter(s, len, ' ');
    }

    public static String alignCenter(CharSequence s, int len, char pad) {
        if (len < 0) throw new IllegalArgumentException("len (" + len + ") < 0.");
        int slen = s.length();
        if (len <= slen) {
            return s.toString();
        } else {
            char[] buf = new char[len];
            int start = (len - slen) >> 1;
            Strings.toArray(s, 0, slen, buf, start);
            CharArrays.fillFast(buf, 0, start, pad);
            CharArrays.fillFast(buf, start + slen, len, pad);
            return internalToString(buf);
        }
    }

    /**
  * Left-justifies the specified string in a string of the specified length.
  * If the specified length is greater than the length of the string, the method returns a new string of specified length, 
  * starting with the specified string, and ending with <tt>len - s.length()</tt> copies of the space character (' ').<br>
  * If the specified length is equal or less than the length of the specified string, the specified string itself is returned.
  *
  * @return a string with the input string aligned left.
  *
  * @param  s     the input string.
  * @param  len   the length of the new string.
  *
  * @throws IllegalArgumentException if <tt>len &lt; 0</tt>.
  * @throws NullPointerException     if <tt>s == null</tt>.
  *
  * @see #alignLeft(CharSequence,int,char)
  *
  * @since JaXLib 1.0
  */
    public static String alignLeft(CharSequence s, int len) {
        return alignLeft(s, len, ' ');
    }

    /**
  * Left-justifies the specified string in a string of the specified length.
  * If the specified length is greater than the length of the string, the method returns a new string of specified length, 
  * starting with the specified string, and ending with <tt>len - s.length()</tt> copies of specified char.<br>
  * If the specified length is equal or less than the length of the specified string, the specified string itself is returned.
  * <p>
  * Example: <tt>Strings.alignLeft("10", 5, '.')</tt> returns <tt>"10..."</tt>.
  * </p>
  *
  * @return a string with the input string aligned left.
  *
  * @param  s     the input string.
  * @param  len   the length of the new string.
  * @param  pad   the character to use for filling the difference.
  *
  * @throws IllegalArgumentException if <tt>len &lt; 0</tt>.
  * @throws NullPointerException     if <tt>s == null</tt>.
  *
  * @since JaXLib 1.0
  */
    public static String alignLeft(CharSequence s, int len, char pad) {
        if (len < 0) throw new IllegalArgumentException("len (" + len + ") < 0.");
        int slen = s.length();
        if (len <= slen) {
            return s.toString();
        } else {
            char[] buf = new char[len];
            Strings.toArray(s, 0, slen, buf, 0);
            CharArrays.fillFast(buf, slen, len, pad);
            return internalToString(buf);
        }
    }

    /**
  * Right-justifies the specified string in a string of the specified length.
  * If the specified length is greater than the length of the string, the method returns a new string of specified length, 
  * starting with <tt>len - s.length()</tt> copies of the space character (' '), and ending with the specified string.<br>
  * If the specified length is equal or less than the length of the specified string, the specified string itself is returned.
  *
  * @return a string with the input string aligned right.
  *
  * @param  s     the input string.
  * @param  len   the length of the new string.
  *
  * @throws IllegalArgumentException if <tt>len &lt; 0</tt>.
  * @throws NullPointerException     if <tt>s == null</tt>.
  *
  * @see #alignRight(CharSequence,int,char)
  *
  * @since JaXLib 1.0
  */
    public static String alignRight(CharSequence s, int len) {
        return alignRight(s, len, ' ');
    }

    /**
  * Right-justifies the specified string in a string of the specified length.
  * If the specified length is greater than the length of the string, the method returns a buffer
  * starting with <tt>len - s.length()</tt> copies of specified char, and ending with the specified string.<br>
  * If the specified length is equal or less than the length of the specified string, the specified string itself is returned
  * <p>
  * Example: <tt>Strings.alignRight("10", 5, '.')</tt> returns <tt>"...10"</tt>.
  * </p>
  *
  * @return a string with the input string aligned right.
  *
  * @param  s     the input string.
  * @param  len   the length of the new string.
  * @param  pad   the character to use for filling the difference.
  *
  * @throws IllegalArgumentException if <tt>len &lt; 0</tt>.
  * @throws NullPointerException     if <tt>s == null</tt>.
  *
  * @since JaXLib 1.0
  */
    public static String alignRight(CharSequence s, int len, char pad) {
        if (len < 0) throw new IllegalArgumentException("len (" + len + ") < 0.");
        int slen = s.length();
        if (len <= slen) {
            return s.toString();
        } else {
            char[] buf = new char[len];
            Strings.toArray(s, 0, slen, buf, len - slen);
            CharArrays.fillFast(buf, 0, len - slen, pad);
            return internalToString(buf);
        }
    }

    /**
   * Efficiently concats the string representations of the specified objects.
   *
   * @since JaXLib 1.0
   */
    public static String concat(Object a, Object b) {
        CharSequence sa = toCharSequence(a);
        CharSequence sb = toCharSequence(b);
        int la = sa.length();
        int lb = sb.length();
        int len = la + lb;
        if (len == 0) return ""; else {
            char[] array = new char[len];
            toArray(sa, 0, la, array, 0);
            toArray(sb, 0, lb, array, la);
            return internalToString(array);
        }
    }

    /**
   * Efficiently concats the string representations of the specified objects.
   *
   * @since JaXLib 1.0
   */
    public static String concat(Object a, Object b, Object c) {
        CharSequence sa = toCharSequence(a);
        CharSequence sb = toCharSequence(b);
        CharSequence sc = toCharSequence(c);
        int la = sa.length();
        int lb = sb.length();
        int lc = sc.length();
        int len = la + lb + lc;
        if (len == 0) return ""; else {
            char[] array = new char[len];
            toArray(sa, 0, la, array, 0);
            toArray(sb, 0, lb, array, la);
            toArray(sc, 0, lc, array, la + lb);
            return internalToString(array);
        }
    }

    /**
   * Efficiently concats the string representations of the specified objects.
   *
   * @since JaXLib 1.0
   */
    public static String concat(Object... objects) {
        if (objects instanceof String[]) return concat((String[]) objects);
        if (objects.length == 0) return "";
        if (objects.length == 1) return String.valueOf(objects[0]);
        CharSequence[] a = new CharSequence[objects.length];
        int len = 0;
        for (int i = a.length; --i >= 0; ) {
            CharSequence s = toCharSequence(objects[i]);
            len += s.length();
            a[i] = s;
        }
        if (len == 0) {
            return "";
        } else {
            char[] array = new char[len];
            int offs = 0;
            for (CharSequence s : a) {
                int slen = s.length();
                toArray(s, 0, slen, array, offs);
                offs += slen;
            }
            if (offs != len) throw new ConcurrentModificationException("one of the sequence has been modified concurrently");
            return internalToString(array);
        }
    }

    /**
   * Efficiently concats the string representations of the specified objects.
   *
   * @since JaXLib 1.0
   */
    public static String concat(String... objects) {
        if (objects.length == 0) return ""; else if (objects.length == 1) return String.valueOf(objects[0]);
        int len = 0;
        for (int i = objects.length; --i >= 0; ) {
            String s = objects[i];
            len += (s == null) ? 4 : s.length();
        }
        if (len == 0) {
            return "";
        } else {
            char[] array = new char[len];
            int offs = 0;
            for (String s : objects) {
                if (s == null) s = "null";
                int slen = s.length();
                s.getChars(0, slen, array, offs);
                offs += slen;
            }
            return internalToString(array);
        }
    }

    /**
   * Efficiently concats the string representations of the specified objects.
   *
   * @since JaXLib 1.0
   */
    public static String concat(Collection<?> objects) {
        int size = objects.size();
        if (size == 0) {
            return "";
        } else if (size == 1) {
            return String.valueOf((objects instanceof List) ? ((List) objects).get(0) : objects.iterator().next());
        }
        Object[] a = objects.toArray(new Object[size]);
        int len = 0;
        for (int i = a.length; --i >= 0; ) {
            CharSequence s = toCharSequence(a[i]);
            len += s.length();
            a[i] = s;
        }
        if (len == 0) {
            return "";
        } else {
            char[] array = new char[len];
            int offs = 0;
            for (Object e : a) {
                CharSequence s = (CharSequence) e;
                int slen = s.length();
                toArray(s, 0, slen, array, offs);
                offs += slen;
            }
            if (offs != len) throw new ConcurrentModificationException("one of the sequence has been modified concurrently");
            return internalToString(array);
        }
    }

    public static String nCopies(char c, int count) {
        if (count == 0) return ""; else if (count == 1) return Character.toString(c); else return internalToString(CharArrays.nCopies(c, count));
    }

    /**
   * Returns a string with each specified old character in specified sequence replaced by specified new character.
   *
   * @param s       the sequence.
   * @param oldChar the character to replace.
   * @param newChar the character to replace the old one by.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @see String#replace(char,char)
   *
   * @since JaXLib 1.0
   */
    public static String replaceEach(CharSequence s, char oldChar, char newChar) {
        return s.toString().replace(oldChar, newChar);
    }

    /**
   * Returns a string with each specified old character in specified subsequence replaced by specified new character.
   * The returned string has the same length as the specified charsequence.
   *
   * @param s         the sequence.
   * @param fromIndex the index of the first character of the subsequence.
   * @param toIndex   the index after the first character of the subsequence.
   * @param oldChar   the character to replace.
   * @param newChar   the character to replace the old one by.
   *
   * @throws IndexOutOfBoundsException  for an illegal endpoint index value <tt>(fromIndex &lt; 0 || toIndex &gt; s.length() || fromIndex &gt; toIndex)</tt>.
   * @throws NullPointerException       if <tt>s == null</tt>.
   *
   * @see String#replace(char,char)
   *
   * @since JaXLib 1.0
   */
    public static String replaceEach(CharSequence s, int fromIndex, int toIndex, char oldChar, char newChar) {
        int len = s.length();
        CheckArg.range(len, fromIndex, toIndex);
        if ((oldChar == newChar) || (fromIndex == toIndex)) {
            return s.toString();
        } else if ((fromIndex == 0) && (toIndex == len) && (s instanceof String)) {
            return s.toString().replace(oldChar, newChar);
        } else {
            fromIndex = indexOf(s, fromIndex, toIndex, oldChar);
            if (fromIndex < 0) {
                return s.toString();
            } else {
                char[] buf = toArray(s);
                buf[fromIndex] = newChar;
                CharArrays.replaceEach(buf, fromIndex + 1, toIndex, oldChar, newChar);
                return internalToString(buf);
            }
        }
    }

    /**
   * Returns a string with each specified old character in specified sequence replaced by specified new character, optionally ignoring case.
   *
   * @param s       the sequence.
   * @param oldChar the character to replace.
   * @param newChar the character to replace the old one by.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static String replaceEach(CharSequence s, char oldChar, char newChar, boolean ignoreCase) {
        return replaceEach(s, 0, s.length(), oldChar, newChar, ignoreCase);
    }

    /**
   * Returns a string with each specified old character in specified subsequence replaced by specified new character, optionally ignoring case.
   * The returned string has the same length as the specified charsequence.
   *
   * @param s         the sequence.
   * @param fromIndex the index of the first character of the subsequence.
   * @param toIndex   the index after the first character of the subsequence.
   * @param oldChar   the character to replace.
   * @param newChar   the character to replace the old one by.
   *
   * @throws IndexOutOfBoundsException  for an illegal endpoint index value <tt>(fromIndex &lt; 0 || toIndex &gt; s.length() || fromIndex &gt; toIndex)</tt>.
   * @throws NullPointerException       if <tt>s == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static String replaceEach(CharSequence s, int fromIndex, int toIndex, char oldChar, char newChar, boolean ignoreCase) {
        if (!ignoreCase) {
            return replaceEach(s, fromIndex, toIndex, oldChar, newChar);
        } else {
            char l = Character.toLowerCase(oldChar);
            char u = Character.toUpperCase(oldChar);
            if (l == u) {
                return replaceEach(s, fromIndex, toIndex, oldChar, newChar);
            } else {
                int len = s.length();
                CheckArg.range(len, fromIndex, toIndex);
                fromIndex = indexOf(s, fromIndex, toIndex, oldChar, true);
                if (fromIndex < 0) {
                    return s.toString();
                } else {
                    char[] buf = toArray(s);
                    buf[fromIndex] = newChar;
                    CharArrays.replaceEach(buf, fromIndex + 1, toIndex, oldChar, newChar, true);
                    return internalToString(buf);
                }
            }
        }
    }

    public static String[] split(String s, char delim) {
        int r = count(s, delim) + 1;
        String[] a = new String[r];
        for (int i = 0, ai = 0; r > 0; ai++, r--) {
            int j = s.indexOf(delim, i);
            if (j < 0) {
                a[ai] = s.substring(i);
                break;
            } else {
                a[ai] = s.substring(i, j);
                i = j + 1;
            }
        }
        assert (r == 0) : r;
        return a;
    }

    public static String swap(CharSequence s, int index1, int index2) {
        char c1 = s.charAt(index1);
        char c2 = s.charAt(index2);
        if (c1 == c2) {
            return s.toString();
        } else {
            char[] a = toArray(s);
            a[index1] = c2;
            a[index2] = c1;
            return internalToString(a);
        }
    }

    /**
   * Converts all characters in specified sequence to lower case using the current default <tt>Locale</tt>. 
   * This is equivalent to calling <tt>toLowerCase(s, Locale.getDefault()</tt>.
   *
   * @return the converted sequence.
   *
   * @param s the sequence to convert.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @see #toLowerCase(CharSequence,Locale)
   * @see String#toLowerCase()
   *
   * @since JaXLib 1.0
   */
    public static String toLowerCase(CharSequence s) {
        if (s.length() == 0) return ""; else return s.toString().toLowerCase();
    }

    /**
   * Converts all characters in specified sequence to lower case using the rules of specified <tt>Locale</tt>. 
   *
   * @return the converted sequence.
   *
   * @param s       the sequence to convert.
   * @param locale  the locale to retrieve case conversion rules from.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @see String#toLowerCase(Locale)
   *
   * @since JaXLib 1.0
   */
    public static String toLowerCase(CharSequence s, Locale locale) {
        if (s.length() == 0) return ""; else return s.toString().toLowerCase(locale);
    }

    /**
   * Converts all characters in specified sequence to upper case using the current default <tt>Locale</tt>. 
   * This is equivalent to calling <tt>toUpperCase(s, Locale.getDefault()</tt>.
   *
   * @return the converted sequence.
   *
   * @param s the sequence to convert.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @see #toUpperCase(CharSequence,Locale)
   * @see String#toUpperCase()
   *
   * @since JaXLib 1.0
   */
    public static String toUpperCase(CharSequence s) {
        if (s.length() == 0) return ""; else return s.toString().toUpperCase();
    }

    /**
   * Converts all characters in specified sequence to upper case using the rules of specified <tt>Locale</tt>. 
   *
   * @return the converted sequence.
   *
   * @param s       the sequence to convert.
   * @param locale  the locale to retrieve case conversion rules from.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @see String#toUpperCase(Locale)
   *
   * @since JaXLib 1.0
   */
    public static String toUpperCase(CharSequence s, Locale locale) {
        if (s.length() == 0) return ""; else return s.toString().toUpperCase(locale);
    }

    /**
   * Returns a string with leading and trailing whitespace omitted from specified sequence.
   *
   * @return A string with leading and trailing white space removed from specified sequence.
   *
   * @param s the sequence to trim.
   *
   * @throws NullPointerException if <tt>s == null</tt>.
   *
   * @see String#trim()
   *
   * @since JaXLib 1.0
   */
    public static String trim(CharSequence s) {
        return s.toString().trim();
    }
}
