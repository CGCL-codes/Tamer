package gallery.web.support.pages;

import gallery.model.beans.Pages;
import gallery.web.controller.pages.Config;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Utils {

    public static Pages getCurrentPage(HttpServletRequest request) {
        return (Pages) request.getAttribute(Config.CURRENT_PAGE_ATTRIBUTE);
    }

    public static List<Pages> getNavigation(HttpServletRequest request, Config conf) {
        return (List<Pages>) request.getAttribute(conf.getNavigationDataAttribute());
    }

    private Utils() {
    }
}
