package com.sitescape.team.portlet.sample;

import com.sitescape.team.module.sample.Employee;
import com.sitescape.team.web.portlet.SSimpleFormController;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;

public class EmployeeEditController extends SSimpleFormController {

    public void onSubmitAction(ActionRequest request, ActionResponse response, Object command, BindException errors) throws Exception {
        Employee employee = (Employee) command;
        Integer key;
        try {
            key = new Integer(request.getParameter("employee"));
        } catch (NumberFormatException ex) {
            key = null;
        }
        if (key == null) {
            getEmployeeModule().addEmployee(employee);
        } else {
            getEmployeeModule().updateEmployee(employee);
        }
        response.setRenderParameter("action", "employees");
    }

    protected Object formBackingObject(PortletRequest request) throws Exception {
        Employee employee;
        try {
            Integer key = new Integer(request.getParameter("employee"));
            employee = getEmployeeModule().getEmployee(key);
        } catch (NumberFormatException ex) {
            employee = new Employee();
        }
        return employee;
    }

    protected void initBinder(PortletRequest request, PortletRequestDataBinder binder) throws Exception {
        binder.setRequiredFields(new String[] { "firstName", "lastName" });
        binder.setAllowedFields(new String[] { "firstName", "lastName", "salary" });
    }

    protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response) throws Exception {
        BindException errors = getErrorsForNewForm(request);
        errors.reject("duplicateFormSubmission", "Duplicate form submission");
        return showForm(request, response, errors);
    }

    protected void handleInvalidSubmit(ActionRequest request, ActionResponse response) throws Exception {
    }
}
