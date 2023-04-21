package mscheme.values;

import mscheme.exceptions.ImmutableException;

/**
 * 
 */
abstract class Compound extends ValueDefaultImplementations {

    /** The CVS id of the file containing this class. */
    public static final String id = "$Id: Compound.java 691 2004-01-16 21:52:46Z sielenk $";

    private final boolean _isConst;

    protected Compound(boolean isConst) {
        _isConst = isConst;
    }

    protected final void modify() throws ImmutableException {
        if (_isConst) {
            throw new ImmutableException(this);
        }
    }

    protected abstract Object getConstCopy();

    public final Object getConst() {
        return _isConst ? this : getConstCopy();
    }
}
