package com.centraview.projects.project;

import java.io.IOException;
import java.util.Vector;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.projects.helper.CustomFieldVO;
import com.centraview.projects.helper.ProjectVO;
import com.centraview.projects.helper.ProjectVOX;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;

/**
 * This handler is used when Editing a Project
 *
 */
public final class EditProjectHandler extends Action {

    String currentTZ = "";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        String saveandclose = (String) request.getParameter("saveandclose");
        String saveandnew = (String) request.getParameter("saveandnew");
        String returnStatus = "failure";
        HttpSession session = request.getSession(true);
        UserObject userobject = (UserObject) session.getAttribute("userobject");
        int individualID = userobject.getIndividualID();
        currentTZ = userobject.getUserPref().getTimeZone();
        ProjectVOX projectVOX = new ProjectVOX(currentTZ, form);
        ProjectVO projectVO = projectVOX.getVO();
        Vector customFieldVec = getCustomFieldVO(request, response);
        projectVO.setCustomFieldVOs(customFieldVec);
        ProjectFacadeHome aa = (ProjectFacadeHome) CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        try {
            ProjectFacade remote = (ProjectFacade) aa.create();
            remote.setDataSource(dataSource);
            remote.updateProject(individualID, projectVO);
        } catch (Exception e) {
            System.out.println("[Exception] EditProjectHandler.execute: " + e.toString());
        }
        if ((saveandclose != null)) {
            returnStatus = ".forward.close_window";
        } else if ((saveandnew != null)) {
            clearForm((ProjectForm) form, mapping);
            request.setAttribute("refreshWindow", "true");
            returnStatus = ".view.projects.new.project";
        } else {
            returnStatus = ".view.projects.edit.project";
        }
        request.setAttribute("projectid", "" + ((ProjectForm) form).getProjectid());
        return (mapping.findForward(returnStatus));
    }

    public void clearForm(ProjectForm form, ActionMapping mapping) {
        form.setEntityid(0);
        form.setTitle("");
        form.setDescription("");
        form.setProjectid(0);
        form.setEntity("");
        form.setContact("");
        form.setContactID(0);
        form.setManagerID(0);
        form.setManager("");
        form.setTeam("");
        form.setTeamID(0);
        form.setStartday("");
        form.setStartmonth("");
        form.setStartyear("");
        form.setEndday("");
        form.setEndmonth("");
        form.setEndyear("");
        form.setStatus("");
        form.setStatusID(0);
        form.setBudhr("0");
        form.setText1("");
        form.setText2("");
        form.setText3("");
    }

    public Vector getCustomFieldVO(HttpServletRequest request, HttpServletResponse response) {
        Vector vec = new Vector();
        for (int i = 1; i < 4; i++) {
            String fieldid = request.getParameter("fieldid" + i);
            String fieldType = request.getParameter("fieldType" + i);
            String textValue = request.getParameter("text" + i);
            if (fieldid == null) {
                fieldid = "0";
            }
            int intfieldId = Integer.parseInt(fieldid);
            CustomFieldVO cfvo = new CustomFieldVO();
            cfvo.setFieldID(intfieldId);
            cfvo.setFieldType(fieldType);
            cfvo.setValue(textValue);
            vec.add(cfvo);
        }
        return vec;
    }
}
