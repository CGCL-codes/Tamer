package openfuture.bugbase.actions;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import openfuture.bugbase.forms.ViewReportsForm;
import openfuture.bugbase.model.BugReportDateComparator;
import openfuture.bugbase.model.BugReportDoctorComparator;
import openfuture.bugbase.model.BugReportIdComparator;
import openfuture.bugbase.model.BugReportLevelComparator;
import openfuture.bugbase.model.BugReportPackageComparator;
import openfuture.bugbase.model.BugReportReporterComparator;
import openfuture.bugbase.model.BugReportStatusComparator;
import openfuture.bugbase.model.BugReportTitleComparator;
import openfuture.bugbase.servlet.BugBaseJspServlet;
import openfuture.util.error.I18NException;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action bean for viewing bug reports.
 * <p>
 *
 *
 * Created: Thu Jan 11 08:14:45 2001
 *
 * @author <a href="mailto:wolfgang@openfuture.de">Wolfgang Reissenberger</a>
 * @version $Revision: 1.7 $
 */
public class ViewReportsAction extends BugBaseAction {

    private static final String SORT_ID = "id";

    private static final String SORT_PACKAGE = "package";

    private static final String SORT_DATE = "date";

    private static final String SORT_TITLE = "title";

    private static final String SORT_ERRORLEVEL = "errorLevel";

    private static final String SORT_STATUS = "status";

    private static final String SORT_AUTHOR = "author";

    private static final String SORT_DOCTOR = "doctor";

    /**
     * View bug reports. The reports are sorted due to the attribute 
     * values:
     * <ul>
     *   <li><strong>sort</strong>: The sorting criteria. Recognized values:
     *   <ul>
     *     <li> {@link openfuture.bugbase.domain.BugReport#getId() id}
     *     <li> {@link openfuture.bugbase.domain.BugReport#getPackageName() package}
     *     <li> {@link openfuture.bugbase.domain.BugReport#getDateReported() date}
     *     <li> {@link openfuture.bugbase.domain.BugReport#getTitle() title}
     *     <li> {@link openfuture.bugbase.domain.BugReport#getErrorLevel() errorLevel}
     *     <li> {@link openfuture.bugbase.domain.BugReport#getReporter() author}
     *     <li> {@link openfuture.bugbase.domain.BugReport#getDoctor() doctor}
     *     <li> status
     *   </ul>
     *   <li><strong>direction</strong>: direction in which the list 
     *       should be sorted. Recognized values: <em>up</em> and 
     *       <em>down</em>.</li>
     * </ul>
     *  The following attributes are set
     * in the servlet's context:
     * <ul>
     *   <li> {@link openfuture.bugbase.servlet.BugBaseJspServlet#REPORTS
     openfuture.bugbase.reports} contains a list of 
     *        {@link openfuture.bugbase.domain.BugReport bug reports}.
     * </ul> <p>
     *
     * The following forwards are generated:
     * <ul>
     *   <li><strong>success</strong>: the login was successful.
     *   <li><strong>error</strong>: the login failed or
     *       an error occured.
     * </ul>
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form an The form bean containing the form data
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @return an <code>ActionForward</code> value
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionErrors errors = initialize(mapping, form, request, response);
        if (!errors.empty()) return (mapping.findForward("error"));
        String sort = request.getParameter("sort");
        String direction = request.getParameter("direction");
        if (form instanceof ViewReportsForm) {
            ViewReportsForm myForm = (ViewReportsForm) form;
            try {
                servlet.log("Starting to retrieve bug reports...");
                LinkedList reports = getClient(request).getBugReportList(getProject(request), getYesNoDefault(myForm.getReported()), getYesNoDefault(myForm.getStarted()), getYesNoDefault(myForm.getFixed()), getYesNoDefault(myForm.getRejected()), myForm.getQueryString(), myForm.getAuthor(), myForm.getDoctor());
                servlet.log("Retrieving bug reports finished.");
                if (sort != null) {
                    if (direction == null) direction = "up";
                    if (sort.equals(SORT_ID)) {
                        Collections.sort(reports, new BugReportIdComparator());
                    } else if (sort.equals(SORT_PACKAGE)) {
                        Collections.sort(reports, new BugReportPackageComparator());
                    } else if (sort.equals(SORT_DATE)) {
                        Collections.sort(reports, new BugReportDateComparator());
                    } else if (sort.equals(SORT_TITLE)) {
                        Collections.sort(reports, new BugReportTitleComparator());
                    } else if (sort.equals(SORT_ERRORLEVEL)) {
                        Collections.sort(reports, new BugReportLevelComparator());
                    } else if (sort.equals(SORT_STATUS)) {
                        Collections.sort(reports, new BugReportStatusComparator());
                    } else if (sort.equals(SORT_AUTHOR)) {
                        Collections.sort(reports, new BugReportReporterComparator());
                    } else if (sort.equals(SORT_DOCTOR)) {
                        Collections.sort(reports, new BugReportDoctorComparator());
                    } else {
                        servlet.log("unexpected sorting parameter: " + sort);
                    }
                    if (direction != null && direction.equalsIgnoreCase("down")) {
                        Collections.reverse(reports);
                    }
                }
                request.getSession().setAttribute(BugBaseJspServlet.REPORTS, reports);
                return (mapping.findForward("success"));
            } catch (I18NException e) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.internal.exception"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
        }
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.internal.wrong.form"));
        saveErrors(request, errors);
        return (mapping.findForward("error"));
    }

    private Boolean getYesNoDefault(String s) {
        if (s == null || s.equals("")) return null;
        if (s.equals("no")) return new Boolean(false);
        return new Boolean(true);
    }
}
