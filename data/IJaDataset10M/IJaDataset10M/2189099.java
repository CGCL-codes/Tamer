package jsbsim.simgear.props;

/**
Base class for all reference counted SimGear objects
Classes derived from this one are meant to be managed with
the SGSharedPtr class.
For more info see @SGSharedPtr. */
public class SGReferenced {

    private int _refcount;

    public SGReferenced() {
        _refcount = 0;
    }

    @Override
    public SGReferenced clone() {
        return new SGReferenced();
    }

    public static int get(final SGReferenced ref) {
        if (null != ref) {
            return ++(ref._refcount);
        } else {
            return 0;
        }
    }

    public static int put(final SGReferenced ref) {
        if (null != ref) {
            return --(ref._refcount);
        } else {
            return 0;
        }
    }

    public static int count(final SGReferenced ref) {
        if (null != ref) {
            return ref._refcount;
        } else {
            return 0;
        }
    }

    public static boolean shared(final SGReferenced ref) {
        return (null != ref) && 1 < ref._refcount;
    }
}
