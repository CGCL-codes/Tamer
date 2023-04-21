package net.community.chest.apache.ant.helpers;

import java.io.Serializable;
import net.community.chest.CoVariantReturn;
import net.community.chest.lang.PubliclyCloneable;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 9, 2009 10:26:30 AM
 */
public class InMemoryProcessingResult implements Serializable, PubliclyCloneable<InMemoryProcessingResult> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1227293121486406110L;

    private int _numProcessed, _numChanged;

    /**
	 * @return number of files updated/created
	 */
    public int getNumChanged() {
        return _numChanged;
    }

    public void setNumChanged(int v) {
        _numChanged = v;
    }

    /**
	 * @return total number of processed files
	 */
    public int getNumProcessed() {
        return _numProcessed;
    }

    public void setNumProcessed(int v) {
        _numProcessed = v;
    }

    public InMemoryProcessingResult(final int numProcessed, final int numChanged) {
        _numProcessed = numProcessed;
        _numChanged = numChanged;
    }

    public InMemoryProcessingResult() {
        super();
    }

    @Override
    @CoVariantReturn
    public InMemoryProcessingResult clone() throws CloneNotSupportedException {
        return getClass().cast(super.clone());
    }

    @Override
    public boolean equals(Object obj) {
        final Class<?> oc = (obj == null) ? null : obj.getClass();
        if (oc != getClass()) return false;
        if (this == obj) return true;
        final InMemoryProcessingResult oRes = (InMemoryProcessingResult) obj;
        final int[] va = { oRes.getNumChanged(), getNumChanged(), oRes.getNumProcessed(), getNumProcessed() };
        for (int vIndex = 0; vIndex < va.length; vIndex += 2) {
            final int ov = va[vIndex], tv = va[vIndex + 1];
            if (ov != tv) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getNumChanged() + getNumProcessed();
    }

    @Override
    public String toString() {
        return "#changed=" + getNumChanged() + ";#proc.=" + getNumProcessed();
    }
}
