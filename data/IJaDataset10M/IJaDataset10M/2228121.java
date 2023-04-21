package info.jtrac.wicket.devmode;

import java.io.IOException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.protocol.http.WicketFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this is a different approach to reloading instead of using the reloading wicket
 * filter that comes along with wicket.  See this wicket mail discussion thread:
 * 
 * http://www.nabble.com/Reloading-on-demand-%28a-different-approach-to-reloading%29-tt15582878.html
 */
public class ReloadingWicketFilter extends WicketFilter {

    private static final Logger logger = LoggerFactory.getLogger(ReloadingWicketFilter.class);

    private static final String banner = "\n***********************************************\n" + "*** WARNING: Reloading Wicket Filter in use ***\n" + "***    This is wrong if production mode.    ***\n" + "***********************************************";

    private ReloadingClassLoader reloadingClassLoader;

    private FilterConfig filterConfig;

    public ReloadingWicketFilter() {
        reloadingClassLoader = new ReloadingClassLoader(getClass().getClassLoader());
        reloadingClassLoader.watch("info.jtrac.wicket.*");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        super.init(filterConfig);
        logger.warn(banner);
    }

    @Override
    public boolean doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (reloadingClassLoader.hasChanges()) {
            logger.debug("changes to reloadable classes detected, reloading...");
            reloadingClassLoader = reloadingClassLoader.clone();
            super.init(filterConfig);
        }
        return super.doGet(request, response);
    }

    @Override
    protected ClassLoader getClassLoader() {
        return reloadingClassLoader;
    }
}
