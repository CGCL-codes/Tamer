package uk.co.lakesidetech.springframework.web.servlet.mvc.multiaction;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Stuart Eccles
 */
public class JamesWExampleMultiActionFormController extends MultiActionController {

    private IDepartmentBusiness businessLogic;

    public ModelAndView deleteDepartment(HttpServletRequest request, Object command, BindException errors) throws Exception {
        String departmentId = request.getParameter("departmentId");
        businessLogic.deleteDepartment(Integer.parseInt(departmentId));
        return null;
    }

    public ModelAndView addDepartment(HttpServletRequest request, Object command, BindException errors) throws Exception {
        String departmentId = request.getParameter("departmentId");
        businessLogic.addDepartment();
        return null;
    }

    public ModelAndView editDepartment(HttpServletRequest request, Object command, BindException errors) throws Exception {
        String departmentId = request.getParameter("departmentId");
        Map model = new HashMap();
        model.put("departmentId", departmentId);
        return new ModelAndView(new RedirectView("editDepartment.htm"), model);
    }
}
