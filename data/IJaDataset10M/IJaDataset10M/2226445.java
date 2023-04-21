package junitx.tool;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.2 $ $Date: 2003/01/14 08:58:44 $
 * @author <a href="mailto:vbossica@users.sourceforge.net">Vladimir Bossicard</a>
 */
public class NonTestReturnSuite extends TestCase {

    public NonTestReturnSuite(String name) {
        super(name);
    }

    public static boolean suite() {
        return true;
    }
}
