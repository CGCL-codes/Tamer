package pcgen.persistence.lst;

import pcgen.core.SizeAdjustment;

/**
 * <code>SizeAdjustmentLstToken</code>
 *
 * @author  Devon Jones <soulcatcher@evilsoft.org>
 */
public interface SizeAdjustmentLstToken extends LstToken {

    /**
	 * @param sa
	 * @param value
	 * @return true if parse OK
	 */
    public abstract boolean parse(SizeAdjustment sa, String value);
}
