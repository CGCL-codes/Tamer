package ro.fortech.peaa.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import ro.fortech.peaa.domain.Employee;
import ro.fortech.peaa.service.api.IEmployeeManager;

/**
 * Controller for employees page.
 * @author robert
 *
 */
public class EmployeesController extends AbstractController {

    /** The employee manager. */
    private IEmployeeManager employeeManager;

    public void setEmployeeManager(IEmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        String xx = (String) arg0.getParameter("xx");
        String yy = (String) arg0.getParameter("yy");
        logger.info("XX% = " + xx + "\nYY% = " + yy);
        Map<String, Object> myModel = new HashMap<String, Object>();
        logger.info("Calling from employees controller");
        List<Employee> employees = this.employeeManager.findAll();
        logger.info("Found " + employees.size() + " employees.");
        myModel.put("employees", employees);
        myModel.put("average_salary", employeeManager.getAverageSalary());
        myModel.put("xx", xx != null && !"".equals(xx) ? Double.valueOf(xx) : null);
        myModel.put("yy", yy != null && !"".equals(yy) ? Double.valueOf(yy) : null);
        return new ModelAndView("employees", myModel);
    }
}
