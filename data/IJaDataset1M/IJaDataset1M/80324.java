package net.sourceforge.freejava.util.exception;

public class WrappedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WrappedException(Throwable orig) {
        super(orig);
    }

    @SuppressWarnings("unchecked")
    public <T extends Throwable> T unwrap() {
        return (T) getCause();
    }

    @SuppressWarnings("unchecked")
    public <E1 extends Throwable> void rethrow(Class<E1> majorEx) throws E1 {
        Throwable orig = unwrap();
        if (majorEx.isInstance(orig)) throw (E1) orig;
        if (orig instanceof RuntimeException) throw (RuntimeException) orig;
        throw new RuntimeException(orig.getMessage(), orig);
    }

    @SuppressWarnings("unchecked")
    public <E1 extends Throwable, E2 extends Throwable> void rethrow(Class<E1> e1, Class<E2> e2) throws E1, E2 {
        Throwable orig = unwrap();
        if (e1.isInstance(orig)) throw (E1) orig;
        if (e2.isInstance(orig)) throw (E2) orig;
        if (orig instanceof RuntimeException) throw (RuntimeException) orig;
        throw new RuntimeException(orig.getMessage(), orig);
    }

    @SuppressWarnings("unchecked")
    public <E1 extends Throwable, E2 extends Throwable, E3 extends Throwable> void rethrow(Class<E1> e1, Class<E2> e2, Class<E3> e3) throws E1, E2, E3 {
        Throwable orig = unwrap();
        if (e1.isInstance(orig)) throw (E1) orig;
        if (e2.isInstance(orig)) throw (E2) orig;
        if (e3.isInstance(orig)) throw (E3) orig;
        if (orig instanceof RuntimeException) throw (RuntimeException) orig;
        throw new RuntimeException(orig.getMessage(), orig);
    }
}
