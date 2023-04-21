package com.centraview.report.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportVO;

/**
 * <code>DuplicateAdHocReportHandler</code> Handler to copy and save data to database for
 * the Ad-Hoc reports.
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */
public class DuplicateAdHocReportHandler extends ReportAction {

    /**
     * This is overridden method  from Action class
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException
     * @throws ServletException
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String nextURL = "addadhocreport";
        try {
            HttpSession session = request.getSession();
            DynaActionForm reportForm = null;
            ReportVO reportVO = null;
            UserObject userObject = (UserObject) session.getAttribute("userobject");
            int individualId = userObject.getIndividualID();
            String rowId[] = null;
            if (request.getParameterValues("rowId") != null) {
                rowId = request.getParameterValues("rowId");
            }
            ReportFacade remote = getReportFacade();
            reportVO = remote.getAdHocReport(individualId, Integer.parseInt(rowId[0]));
            reportForm = (DynaActionForm) getAdHocReportFormFromReportVO(reportVO, form, request);
            reportForm.set("copy", new Boolean(true));
            request.setAttribute("adhocreportform", reportForm);
            request.setAttribute("pagedata", reportVO);
            request.setAttribute("reportType", String.valueOf(ReportConstants.ADHOC_REPORT_CODE));
            request.setAttribute("moduleId", String.valueOf(reportVO.getModuleId()));
        } catch (Exception e) {
            System.out.println("[Exception][DuplicateAdHocReportHandler.execute] Exception Thrown: " + e);
            throw new ServletException(e);
        }
        return mapping.findForward(nextURL);
    }
}
