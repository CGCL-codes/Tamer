package gnu.testlet.java2.util.logging.Logger;

import java.util.logging.Logger;

/**
 * A Logger whose constructor is public.
 *
 * @author <a href="mailto:brawer@dandelis.ch">Sascha Brawer</a>
 */
class TestLogger extends Logger {

    public TestLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }
}
