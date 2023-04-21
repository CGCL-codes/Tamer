package net.community.chest.io.url;

import java.net.URL;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Feb 3, 2011 12:59:02 PM
 *
 */
public class ByExternalFormURLComparator extends AbstractURLComparator {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5982121871293848888L;

    public ByExternalFormURLComparator(boolean ascending) throws IllegalArgumentException {
        super(ascending);
    }

    @Override
    public int compareValues(URL v1, URL v2) {
        final String f1 = adjustPathValue((v1 == null) ? null : v1.toExternalForm()), f2 = adjustPathValue((v2 == null) ? null : v2.toExternalForm());
        return StringUtil.compareDataStrings(f1, f2, false);
    }

    public static final ByExternalFormURLComparator BY_EXTFORM_ASCENDING = new ByExternalFormURLComparator(true), BY_EXTFORM_DESCENDING = new ByExternalFormURLComparator(false);
}
