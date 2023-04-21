package es.devel.opentrats.view.controller;

import es.devel.opentrats.dao.IEmployeeDao;
import es.devel.opentrats.dao.exception.EmployeeDaoException;
import es.devel.opentrats.model.Employee;
import es.devel.opentrats.view.controller.common.OpenTratsAbstractController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author pau
 */
public class EmployeeSearchAbstractController extends OpenTratsAbstractController {

    private IEmployeeDao employeeDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("redirect:employees.htm");
        String searchText = request.getParameter("searchEmployee");
        List<Employee> employeeList = null;
        try {
            employeeList = (List<Employee>) employeeDao.findByNameOrSurname(searchText, 100);
        } catch (EmployeeDaoException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("searchEmployee", searchText);
        request.getSession().setAttribute("searchEmployeesList", employeeList);
        int listSize = employeeList.size();
        if (listSize > 0 && listSize < 100) {
        } else if (listSize > 100) {
        } else {
        }
        return mav;
    }

    public IEmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(IEmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
}
