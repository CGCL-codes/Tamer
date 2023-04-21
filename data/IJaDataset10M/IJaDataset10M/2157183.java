package de.fhg.igd.util;

/**
 * This class stores attributes of {@link Manifest Manifest}
 * sections. Since a manifest section does not have many
 * attributes in general, this class is backed by an array
 * list. This class preserves the order in which attributes
 * are added. Although key comparisons are done case
 * independent, this class preserves the case of the key and
 * the value. This class should be used only if the number
 * of attributes is very small. Adding new attributes is
 * getting very costly, if the capacity is exceeded and
 * the number of stored attributes is high. For small number,
 * it is very efficient, though, in particular, if the
 * number of required slots is known or can be estimated
 * accurately.
 *
 * @author Volker Roth
 * @version "$Id: Attributes.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class Attributes extends Object {

    /**
      * The number of mappings.
      */
    protected int size_;

    /**
      * The array of keys.
      */
    protected String[] keys_;

    /**
      * The array of values.
      */
    protected String[] values_;

    /**
      * Creates an empty instance which the given capacity.
      *
      * @param capacity The initial capacity (number of
      *   key/value pair slots) of this instance.
      */
    public Attributes(int capacity) {
        size_ = 0;
        keys_ = new String[capacity];
        values_ = new String[capacity];
    }

    /**
      * Adds a key and value to this instance. If a mapping
      * already exists for the given key then it is replaced
      * with the new one. Otherwise, the new attribute is
      * added.
      *
      * @param key The key.
      * @param value The value.
      */
    public void put(String key, String value) {
        int i;
        for (i = 0; i < size_; i++) {
            if (key.equalsIgnoreCase(keys_[i])) {
                keys_[i] = key;
                values_[i] = value;
                return;
            }
        }
        growTo(size_ + 1);
        keys_[i] = key;
        values_[i] = value;
        size_++;
    }

    /**
      * Removes the entry with the given key and returns the
      * value stored under that key if such a key exists. If
      * no such key exists then <code>null</code> is returned.
      *
      * @param key The key.
      * @return The value or <code>null</code>.
      */
    public String remove(String key) {
        String s;
        int i;
        int n;
        for (i = 0; i < size_; i++) {
            if (key.equalsIgnoreCase(keys_[i])) {
                n = size_ - i - 1;
                s = values_[i];
                if (n == 0) {
                    keys_[i] = null;
                    values_[i] = null;
                } else {
                    System.arraycopy(keys_, i + 1, keys_, i, n);
                    System.arraycopy(values_, i + 1, values_, i, n);
                }
                size_--;
                return s;
            }
        }
        return null;
    }

    /**
      * Returns the value for the given key if such a
      * mapping exists, and <code>null</code> otherwise.
      * The search key is compared case-independent.
      *
      * @param key The key.
      * @return The mapped value.
      */
    public String get(String key) {
        int i;
        for (i = 0; i < size_; i++) {
            if (key.equalsIgnoreCase(keys_[i])) return values_[i];
        }
        return null;
    }

    /**
      * This method enlarges the capacity such that this
      * instance may hold the given number of mappings.
      * If the capacity is already big enough then nothing
      * is done.
      *
      * @param capacity The new minimum capacity.
      */
    public void growTo(int capacity) {
        if (capacity > keys_.length) {
            String[] a;
            String[] b;
            a = new String[capacity];
            b = new String[capacity];
            System.arraycopy(keys_, 0, a, 0, size_);
            System.arraycopy(values_, 0, b, 0, size_);
            keys_ = a;
            values_ = b;
        }
    }

    /**
      * This method trims the capacity of this instance to
      * the size.
      *
      * @param capacity The new minimum capacity.
      */
    public void trimToSize() {
        if (keys_.length > size_) {
            String[] a;
            String[] b;
            a = new String[size_];
            b = new String[size_];
            System.arraycopy(keys_, 0, a, 0, size_);
            System.arraycopy(values_, 0, b, 0, size_);
            keys_ = a;
            values_ = b;
        }
    }

    /**
      * This method returns an array containing the keys
      * of this instance.
      */
    public String[] getKeys() {
        String[] a;
        a = new String[size_];
        System.arraycopy(keys_, 0, a, 0, size_);
        return a;
    }

    /**
      * Writes the attributes to the given output stream.
      * The attributes are written according to RFC822
      * with trailing <code>0x0d 0x0a</code> at the end
      * of the line and an additional <code>0xd 0xa
      * </code> after the attributes. The character
      * encoding is UTF8.
      */
    public byte[] getBytes(String name) {
        int i;
        int n;
        int len;
        byte[] b;
        StringBuffer buf;
        n = 72;
        buf = new StringBuffer();
        if (name != null) append(buf, "Name", name);
        for (i = 0; i < size_; i++) append(buf, keys_[i], values_[i]);
        buf.append("\r\n");
        n = buf.length();
        b = new byte[n];
        for (i = 0; i < n; i++) b[i] = (byte) buf.charAt(i);
        return b;
    }

    /**
      * Appends the given key and value to the given
      * StringBuffer, formatted as RFC822 attribute
      * value pair, terminated by 0x0d 0x0a. This
      * method also inserts breaks such that no line
      * is longer than 72 characters.
      *
      */
    protected void append(StringBuffer buf, String key, String value) {
        int idx;
        int len;
        idx = buf.length();
        buf.append(key);
        buf.append(": ");
        buf.append(value);
        len = buf.length() - idx;
        if (len > 70) {
            idx = idx + 70;
            while (idx < buf.length()) {
                buf.insert(idx - 1, "\r\n ");
                idx += 72;
            }
        }
        buf.append("\r\n");
    }

    /**
      * Returns a string with the space separated names of
      * the digest algorithms defined in this attribute section.
      * If a <code>Digest-Algorithms</code> attribute is defined
      * then the value of this attribute is returned. Else, the
      * digests are determined by looking for attributes of the
      * form <i>digest</i><code>-Digest</code>.
      */
    public String getDigestAlgorithms() {
        int i;
        String s;
        String key;
        s = get("Digest-Algorithms");
        if (s != null) return s;
        s = "";
        for (i = 0; i < size_; i++) {
            key = keys_[i];
            if (key.toUpperCase().endsWith("-DIGEST")) s = s + " " + key.substring(0, key.length() - 7);
        }
        return s.trim();
    }
}
