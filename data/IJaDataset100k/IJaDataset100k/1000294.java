package org.appspy.perf.servlet.skip;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public interface SkipManager {

    public static final int UNDEFINED = 0;

    public static final int SKIP = 1;

    public static final int COLLECT = 2;

    public int skipRequest(ServletRequest req, ServletResponse res, ServletContext sc);
}
