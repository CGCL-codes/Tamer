package fi.pyramus.views.settings;

import java.util.Locale;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the List Grading Scales view of the application. 
 */
public class ListGradingScalesViewController extends PyramusViewController implements Breadcrumbable {

    /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * In order for the JSP page to build the view, all gradingScale objects are loaded in to "gradingScales" attribute
   * 
   * @param pageRequestContext Page request context
   */
    public void process(PageRequestContext pageRequestContext) {
        GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
        pageRequestContext.getRequest().setAttribute("gradingScales", gradingScaleDAO.listUnarchived());
        pageRequestContext.setIncludeJSP("/templates/settings/listgradingscales.jsp");
    }

    /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
    public UserRole[] getAllowedRoles() {
        return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
    }

    /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
    public String getName(Locale locale) {
        return Messages.getInstance().getText(locale, "settings.listGradingScales.pageTitle");
    }
}
