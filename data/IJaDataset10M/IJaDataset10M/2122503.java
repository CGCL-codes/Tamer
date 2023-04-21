package test.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import test.hibernate.dao.CustomerDao;
import test.hibernate.pojo.Customer;

/**
 *
 * @author mamat
 */
public class DetilCustomerController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        CustomerDao dao = (CustomerDao) getApplicationContext().getBean("CustomerDao");
        Integer id = Integer.parseInt(arg0.getParameter("id"));
        System.out.println(">>>ID=" + id);
        List<Customer> rows = dao.getCustomerId(id);
        System.out.println(">>>ROW SIZE=" + rows.size());
        ModelAndView mav = new ModelAndView("detil");
        mav.addObject("rows", rows);
        return mav;
    }
}
