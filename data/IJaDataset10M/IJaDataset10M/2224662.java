package com.potix.util;

import com.potix.lang.CommonException;
import com.potix.lang.Exceptions;
import com.potix.lang.Expectable;

/**
 * Denotes a modification fails. It is considered as
 * a program bug, while {@link InvalidValueException} is considered as
 * an operational error of user.
 *
 * <p>The create, remove and setter method of peristent beans (i3pb) must
 * declare this exception. Besides this exception and,
 * {@link InvalidValueException},
 * com.potix.i3.pb.RemoveException and com.potix.i3.pb.CreateException,
 * who derive from this class, might be thrown
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class ModificationException extends CommonException {

    /** Utilities.
	 *
	 * <p>The reason to use a class to hold static utilities is we can
	 * override the method's return type later.
	 */
    public static class Aide {

        /** Converts an exception to ModificationException or InvalidValueException
		 * depending on whether t implements {@link Expectable}.
		 * @see Exceptions#wrap
		 */
        public static ModificationException wrap(Throwable t) {
            t = Exceptions.unwrap(t);
            if (t instanceof Expectable) return (InvalidValueException) Exceptions.wrap(t, InvalidValueException.class);
            return (ModificationException) Exceptions.wrap(t, ModificationException.class);
        }

        /** Converts an exception to ModificationException or InvalidValueException
		 * depending on whether t implements {@link Expectable}.
		 * @see Exceptions#wrap
		 */
        public static ModificationException wrap(Throwable t, String msg) {
            t = Exceptions.unwrap(t);
            if (t instanceof Expectable) return (InvalidValueException) Exceptions.wrap(t, InvalidValueException.class, msg);
            return (ModificationException) Exceptions.wrap(t, ModificationException.class, msg);
        }

        /** Converts an exception to ModificationException or InvalidValueException
		 * depending on whether t implements {@link Expectable}.
		 * @see Exceptions#wrap
		 */
        public static ModificationException wrap(Throwable t, int code, Object[] fmtArgs) {
            t = Exceptions.unwrap(t);
            if (t instanceof Expectable) return (InvalidValueException) Exceptions.wrap(t, InvalidValueException.class, code, fmtArgs);
            return (ModificationException) Exceptions.wrap(t, ModificationException.class, code, fmtArgs);
        }

        /** Converts an exception to ModificationException or InvalidValueException
		 * depending on whether t implements {@link Expectable}.
		 * @see Exceptions#wrap
		 */
        public static ModificationException wrap(Throwable t, int code, Object fmtArg) {
            t = Exceptions.unwrap(t);
            if (t instanceof Expectable) return (InvalidValueException) Exceptions.wrap(t, InvalidValueException.class, code, fmtArg);
            return (ModificationException) Exceptions.wrap(t, ModificationException.class, code, fmtArg);
        }

        /** Converts an exception to ModificationException or InvalidValueException
		 * depending on whether t implements {@link Expectable}.
		 * @see Exceptions#wrap
		 */
        public static ModificationException wrap(Throwable t, int code) {
            t = Exceptions.unwrap(t);
            if (t instanceof Expectable) return (InvalidValueException) Exceptions.wrap(t, InvalidValueException.class, code);
            return (ModificationException) Exceptions.wrap(t, ModificationException.class, code);
        }
    }

    public ModificationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ModificationException(String s) {
        super(s);
    }

    public ModificationException(Throwable cause) {
        super(cause);
    }

    public ModificationException() {
    }

    public ModificationException(int code, Object[] fmtArgs, Throwable cause) {
        super(code, fmtArgs, cause);
    }

    public ModificationException(int code, Object fmtArg, Throwable cause) {
        super(code, fmtArg, cause);
    }

    public ModificationException(int code, Object[] fmtArgs) {
        super(code, fmtArgs);
    }

    public ModificationException(int code, Object fmtArg) {
        super(code, fmtArg);
    }

    public ModificationException(int code, Throwable cause) {
        super(code, cause);
    }

    public ModificationException(int code) {
        super(code);
    }
}
