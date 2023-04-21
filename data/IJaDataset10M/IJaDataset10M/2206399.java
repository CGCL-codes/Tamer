package org.hl7.types.impl;

import org.hl7.types.ANY;
import org.hl7.types.BL;
import org.hl7.types.II;
import org.hl7.types.ST;
import org.hl7.types.UID;

/**
 * Minimal implementation of II as a model we can use for all other type. This one is only used to convey NULL values.
 * Again, we have static values of it, so we don't create so many objects in a running program.
 */
public final class IIimpl extends ANYimpl implements II {

    UID _root;

    ST _extension;

    ST _assigningAuthorityName;

    BL _displayable;

    private IIimpl(UID root) {
        super(null);
        if (root == null) throw new IllegalArgumentException(); else this._root = root;
    }

    public static II valueOf(String rootString, String extensionString) {
        return valueOf(rootString, extensionString, null, "false");
    }

    /**
	 * Here is a static factory method. Now, careful, if we assume that IIimpl is immutable, we can't rely on UID, ST etc.
	 * things handed to us to be immutable. So, we have to copy the data! I'll just leave it at String right now to make
	 * it simpler.
	 */
    public static II valueOf(String rootString, String extensionString, String assigningAuthorityNameString) {
        return valueOf(rootString, extensionString, assigningAuthorityNameString, "false");
    }

    /**
	 * Here is a static factory method. Now, careful, if we assume that IIimpl is immutable, we can't rely on UID, ST etc.
	 * things handed to us to be immutable. So, we have to copy the data! I'll just leave it at String right now to make
	 * it simpler.
	 */
    public static II valueOf(String rootString, String extensionString, String assigningAuthorityNameString, String displayable) {
        UID rootUID = UIDimpl.valueOf(rootString);
        if (rootUID.isNullJ()) {
            return IInull.NI;
        } else {
            IIimpl theII = new IIimpl(rootUID);
            theII._extension = STjlStringAdapter.valueOf(extensionString);
            theII._assigningAuthorityName = STjlStringAdapter.valueOf(assigningAuthorityNameString);
            theII._displayable = BLimpl.valueOf(displayable);
            return theII;
        }
    }

    public static II valueOf(UID root, ST extension) {
        IIimpl theII = new IIimpl(root);
        theII._extension = extension;
        theII._assigningAuthorityName = null;
        theII._displayable = BLimpl.FALSE;
        return theII;
    }

    public BL equal(ANY that) {
        if (this == that) return BLimpl.TRUE; else if (that.isNull().isTrue()) return BLimpl.FALSE; else if (that instanceof II) {
            II thatII = (II) that;
            return this.root().equal(thatII.root()).and(this.extension().isNull().and(thatII.extension().isNull()).or(this.extension().equal(thatII.extension())));
        } else return BLimpl.FALSE;
    }

    /** II interface */
    public ST extension() {
        return this._extension;
    }

    public UID root() {
        return this._root;
    }

    public ST assigningAuthorityName() {
        return this._assigningAuthorityName;
    }

    public BL displayable() {
        return _displayable;
    }

    /** HashCode to allow using IIs in hash maps and sets. */
    public int hashCode() {
        return (root().hashCode() << 16) + extension().hashCode();
    }

    public String toString() {
        return extension().isNull().isTrue() ? root().toString() : extension().toString() + "@" + root().toString();
    }
}

;
