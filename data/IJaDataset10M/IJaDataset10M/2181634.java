package org.dishevelled.commandline.argument;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 * A float list argument.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class FloatListArgument extends AbstractArgument<List<Float>> {

    /**
     * Create a new float list argument.
     *
     * @param shortName short argument name
     * @param longName long argument name
     * @param description argument description
     * @param required <code>true</code> if this argument is required
     */
    public FloatListArgument(final String shortName, final String longName, final String description, final boolean required) {
        super(shortName, longName, description, required);
    }

    /** {@inheritDoc} */
    protected List<Float> convert(final String s) throws Exception {
        List<Float> list = new ArrayList<Float>();
        StringTokenizer st = new StringTokenizer(s, ",");
        while (st.hasMoreTokens()) {
            String token = StringUtils.stripToEmpty(st.nextToken());
            Float f = Float.valueOf(token);
            list.add(f);
        }
        return list;
    }
}
