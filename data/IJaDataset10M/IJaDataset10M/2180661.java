package gnu.javax.security.auth;

import gnu.java.security.util.ExpirableObject;
import javax.security.auth.DestroyFailedException;

/**
 * Immutible, though destroyable, password class.
 *
 * <p>Extends {@link ExpirableObject}, implementing {@link doDestroy()}
 * in which encapsulated {@link char[]}, and {@link byte[]} password fields
 * are cleared (elements set to zero) in order to thwart memory heap
 * snooping.
 */
public final class Password extends ExpirableObject {

    /**
   * Password stored in {@link char[]} format.
   */
    private final char[] password;

    /**
   * Password stored in {@link byte[]} format.
   */
    private final byte[] bPassword;

    /**
   * Indicates whether this Password object's {@link doDestroy()} method has
   * been called.  See also, {@link ExpirableObject#Destroy()}.
   */
    private boolean mIsDestroyed = false;

    /**
   * Create a new expirable Password object that will expire after the
   * default timeout {@link ExpirableObject#DEFAULT_TIMEOUT}.
   *
   * @param password The character array password to associate with this
   * Password object.
   */
    public Password(char[] password) {
        this(password, 0, password.length, DEFAULT_TIMEOUT);
    }

    /**
   * Create a new expirable Password object that will expire after the
   * timeout denoted by constructor parameter, <i>delay</i>.
   *
   * @param password The character array password to associate with this
   * Password object.
   * @param delay The number of miliseconds before this Password object
   * will be automatically destroyed.
   */
    public Password(char[] password, long delay) {
        this(password, 0, password.length, delay);
    }

    /**
   * Create a new expirable Password object that will expire after the
   * default timeout {@link ExpirableObject#DEFAULT_TIMEOUT}.
   *
   * @param password The character array password to associate with this
   * Password object.
   * @param offset The <i>password</i> character array parameter element
   * marking the beginning of the contained password string.
   * @param length The number of characters, beginning at <i>offset</i>,
   * to be copied into this object's {@link password} field.
   */
    public Password(char[] password, int offset, int length) {
        this(password, offset, length, DEFAULT_TIMEOUT);
    }

    /**
   * Create a new expirable Password object that will expire after the
   * timeout denoted by constructor parameter, <i>delay</i>.
   *
   * @param password The character array password to associate with this
   * Password object.
   * @param offset The <i>password</i> character array parameter element
   * marking the beginning of the contained password string.
   * @param length The number of characters, beginning at <i>offset</i>,
   * to be copied into this object's {@link password} field.
   * @param delay The number of miliseconds before this Password object
   * will be automatically destroyed.
   */
    public Password(char[] password, int offset, int length, long delay) {
        super(delay);
        if (offset < 0 || length < 0 || offset + length > password.length) throw new ArrayIndexOutOfBoundsException("off=" + offset + " length=" + length + " array.length=" + password.length);
        int i, j;
        this.password = new char[length];
        bPassword = new byte[length];
        for (i = 0, j = offset; i < length; i++, j++) {
            this.password[i] = (char) password[j];
            bPassword[i] = (byte) (password[j] & 0x7F);
        }
    }

    /**
   * Create a new expirable Password object that will expire after the
   * default timeout {@link ExpirableObject#DEFAULT_TIMEOUT}.
   *
   * @param password The byte array password to associate with this
   * Password object.
   */
    public Password(byte[] password) {
        this(password, 0, password.length, DEFAULT_TIMEOUT);
    }

    /**
   * Create a new expirable Password object that will expire after the
   * timeout denoted by constructor parameter, <i>delay</i>.
   *
   * @param password The byte array password to associate with this
   * Password object.
   * @param delay The number of miliseconds before this Password object
   * will be automatically destroyed.
   */
    public Password(byte[] password, long delay) {
        this(password, 0, password.length, delay);
    }

    /**
   * Create a new expirable Password object that will expire after the
   * default timeout {@link ExpirableObject#DEFAULT_TIMEOUT}.
   *
   * @param password The byte array password to associate with this
   * Password object.
   * @param offset The <i>password</i> byte array parameter element
   * marking the beginning of the contained password string.
   * @param length The number of bytes, beginning at <i>offset</i>,
   * to be copied into this object's {@link password} field.
   */
    public Password(byte[] password, int offset, int length) {
        this(password, offset, length, DEFAULT_TIMEOUT);
    }

    /**
   * Create a new expirable Password object that will expire after the
   * timeout denoted by constructor parameter, <i>delay</i>.
   *
   * @param password The byte array password to associate with this
   * Password object.
   * @param offset The <i>password</i> byte array parameter element
   * marking the beginning of the contained password string.
   * @param length The number of bytes, beginning at <i>offset</i>,
   * to be copied into this object's {@link bPassword} field.
   * @param delay The number of miliseconds before this Password object
   * will be automatically destroyed.
   */
    public Password(byte[] password, int offset, int length, long delay) {
        super(delay);
        if (offset < 0 || length < 0 || offset + length > password.length) throw new ArrayIndexOutOfBoundsException("off=" + offset + " length=" + length + " array.length=" + password.length);
        int i, j;
        this.password = new char[length];
        bPassword = new byte[length];
        for (i = 0, j = offset; i < length; i++, j++) {
            this.password[i] = (char) password[j];
            bPassword[i] = password[j];
        }
    }

    /**
   * Returns a reference to the {@link char[]} password storage field,
   * {@link password}.
   */
    public synchronized char[] getPassword() {
        if (mIsDestroyed) throw new IllegalStateException("Attempted destroyed password access.");
        return password;
    }

    /**
   * Returns a reference to the {@link byte[]} password storage field,
   * {@link bPassword}.
   */
    public synchronized byte[] getBytes() {
        if (mIsDestroyed) throw new IllegalStateException("Attempted destroyed password access.");
        return bPassword;
    }

    /**
   * Sets password field char[], and byte[] array elements to zero.
   * This method implements base class {@link ExpirableObject} abstract
   * method, {@link ExpirableObject#doDestroy()}.  See also,
   * {@link ExpirableObject#destroy()}.
   */
    protected synchronized void doDestroy() {
        if (isDestroyed()) return; else {
            for (int i = 0; i < password.length; i++) password[i] = 0;
            for (int i = 0; i < bPassword.length; i++) bPassword[i] = 0;
            mIsDestroyed = true;
        }
    }

    /**
   * Returns true, or false relative to whether, or not this object's
   * {@link doDestroy()} method has been called.  See also,
   * {@ExpirableObject#destroy()}.
   */
    public synchronized boolean isDestroyed() {
        return (mIsDestroyed);
    }
}
