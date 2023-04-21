package net.community.chest.jmx;

import javax.management.ObjectName;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright as per GPLv2</P>
 * Compares the domain first and then the parameters list
 * @author Lyor G.
 * @since Feb 15, 2011 9:02:01 AM
 */
public class StructuredObjectNameComparator extends AbstractObjectNameComparator {

    /**
	 * 
	 */
    private static final long serialVersionUID = 396142750904437860L;

    public StructuredObjectNameComparator(boolean ascending) {
        super(ascending);
    }

    @Override
    public int compareValues(ObjectName v1, ObjectName v2) {
        final String d1 = (v1 == null) ? null : v1.getDomain(), d2 = (v2 == null) ? null : v2.getDomain();
        int nRes = StringUtil.compareDataStrings(d1, d2, false);
        if (nRes != 0) return nRes;
        final String p1 = (v1 == null) ? null : v1.getCanonicalKeyPropertyListString(), p2 = (v2 == null) ? null : v2.getCanonicalKeyPropertyListString();
        if ((nRes = StringUtil.compareDataStrings(p1, p2, false)) != 0) return nRes;
        return 0;
    }

    public static final StructuredObjectNameComparator ASCENDING = new StructuredObjectNameComparator(true), DESCENDING = new StructuredObjectNameComparator(false);
}
