package net.sourceforge.jpotpourri.jpotspect.memo;

import java.util.Arrays;
import net.sourceforge.jpotpourri.util.PtClassUtil;

/**
 * Used to get a proper hash value for arrays.
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class PtArgumentWrapper {

    public final Object[] args;

    private final int hashValue;

    public PtArgumentWrapper(final Object[] args) {
        this.args = args;
        int tmpHash = 0;
        for (Object object : args) {
            if (object == null) {
                tmpHash += 7;
            } else if (object.getClass().isArray()) {
                tmpHash += PtClassUtil.hashCodeOfArray(object);
            } else {
                tmpHash += object.hashCode();
            }
        }
        this.hashValue = tmpHash;
    }

    @Override
    public String toString() {
        return "PtArgumentWrapper[hashValue=" + this.hashValue + "; " + "args=" + Arrays.toString(this.args) + "]";
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(final Object other) {
        if ((other instanceof PtArgumentWrapper) == false) {
            return false;
        }
        final PtArgumentWrapper that = (PtArgumentWrapper) other;
        if (this.args.length != that.args.length) {
            return false;
        }
        for (int i = 0; i < this.args.length; i++) {
            final Object thisArg = this.args[i];
            final Object thatArg = that.args[i];
            if (thisArg == null && thatArg != null || thisArg != null && thatArg == null) {
                return false;
            } else if (thisArg == null && thatArg == null) {
            } else if (thisArg.getClass() != thatArg.getClass()) {
                return false;
            } else if (thisArg.getClass().isArray()) {
                if (PtClassUtil.areArraysEquals(thisArg, thatArg) == false) {
                    return false;
                }
            } else {
                if (thisArg.equals(thatArg) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.hashValue;
    }
}
