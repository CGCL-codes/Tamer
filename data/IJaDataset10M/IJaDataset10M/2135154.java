package org.gzigzag.test;

import junit.framework.*;
import org.gzigzag.*;

/** Some utility routines for tests.
 */
public class TestUtil {

    public static final String rcsid = "$Id: TestUtil.java,v 1.1 2001/05/09 13:49:44 tjl Exp $";

    public static void append(TextScrollBlock sb, String s) {
        try {
            for (int i = 0; i < s.length(); i++) sb.append(s.charAt(i));
        } catch (ImmutableException e) {
            throw new Error("Immutable! " + e);
        }
    }
}
