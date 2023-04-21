package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Transfer Credit Template view of the application.
 */
public class EditTransferCreditTemplateViewController extends PyramusViewController implements Breadcrumbable {

    /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
    public void process(PageRequestContext pageRequestContext) {
        TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
        SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
        SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
        EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
        Long transferCreditTemplateId = pageRequestContext.getLong("transferCreditTemplate");
        TransferCreditTemplate transferCreditTemplate = transferCreditTemplateDAO.findById(transferCreditTemplateId);
        List<Subject> subjects = subjectDAO.listUnarchived();
        Collections.sort(subjects, new StringAttributeComparator("getName"));
        List<EducationalTimeUnit> timeUnits = educationalTimeUnitDAO.listUnarchived();
        Collections.sort(timeUnits, new StringAttributeComparator("getName"));
        List<School> schools = schoolDAO.listUnarchived();
        Collections.sort(schools, new StringAttributeComparator("getName"));
        pageRequestContext.getRequest().setAttribute("transferCreditTemplate", transferCreditTemplate);
        pageRequestContext.getRequest().setAttribute("subjects", subjects);
        pageRequestContext.getRequest().setAttribute("timeUnits", timeUnits);
        pageRequestContext.getRequest().setAttribute("schools", schools);
        pageRequestContext.setIncludeJSP("/templates/settings/edittransfercredittemplate.jsp");
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
        return Messages.getInstance().getText(locale, "settings.editTransferCreditTemplate.pageTitle");
    }
}
