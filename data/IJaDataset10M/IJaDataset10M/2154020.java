package org.dishevelled.commandline.argument;

import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 * A file set argument.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class FileSetArgument extends AbstractArgument<Set<File>> {

    /**
     * Create a new file set argument.
     *
     * @param shortName short argument name
     * @param longName long argument name
     * @param description argument description
     * @param required <code>true</code> if this argument is required
     */
    public FileSetArgument(final String shortName, final String longName, final String description, final boolean required) {
        super(shortName, longName, description, required);
    }

    /** {@inheritDoc} */
    protected Set<File> convert(final String s) throws Exception {
        Set<File> set = new HashSet<File>();
        StringTokenizer st = new StringTokenizer(s, ",");
        while (st.hasMoreTokens()) {
            String token = StringUtils.stripToEmpty(st.nextToken());
            File f = new File(token);
            set.add(f);
        }
        return set;
    }
}
