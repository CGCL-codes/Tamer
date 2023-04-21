package com.devunion.salon.web.action;

import com.devunion.salon.persistence.Customer;
import com.devunion.salon.persistence.dao.CustomerDao;
import com.devunion.salon.web.form.TransactionForm;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Collection;

/**
 * @author Timoshenko Alexander
 */
public class CustomerAction extends CoreAction {

    private final String start_UL = "<ul>";

    private final String end_UL = "</ul>";

    private final String start_LI = "<li id=\"{0}\">{1}";

    private final String end_LI = "</li>";

    private CustomerDao customerDao;

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public ActionForward welcome(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        TransactionForm form = (TransactionForm) actionForm;
        String submittedValue = form.getSubmittedValue();
        if (!StringUtils.isBlank(submittedValue)) {
            if (submittedValue.equalsIgnoreCase(resources.getMessage("modal.customer.button.add"))) {
                Customer customer = new Customer();
                customer.setFirstName(form.getCustomerBean().getFirstName());
                customer.setLastName(form.getCustomerBean().getLastName());
                customer.setPhone(form.getCustomerBean().getPhone());
                customer.setCellPhone(form.getCustomerBean().getCellPhone());
                customer.setEmail(form.getCustomerBean().getEmail());
                customerDao.save(customer);
                form.getCustomerBean().setId(customer.getId());
            } else if (submittedValue.equalsIgnoreCase(resources.getMessage("modal.customer.button.confirm"))) {
                if (form.getCustomerBean().getId() == null) {
                    return actionMapping.findForward("checkout");
                }
            }
            return actionMapping.findForward("transaction");
        }
        return actionMapping.getInputForward();
    }

    public ActionForward autocomplete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String firstName = httpServletRequest.getParameter("customerBean.firstName");
        String lastName = httpServletRequest.getParameter("customerBean.lastName");
        String email = httpServletRequest.getParameter("customerBean.email");
        String phone = httpServletRequest.getParameter("customerBean.phone");
        String cellPhone = httpServletRequest.getParameter("customerBean.cellPhone");
        Collection<Customer> customers = null;
        String result = "";
        if (!StringUtils.isBlank(firstName)) {
            customers = customerDao.getCustomerByCriteria(firstName, 1);
            result = prepareResults(customers, 1);
        } else if (!StringUtils.isBlank(lastName)) {
            customers = customerDao.getCustomerByCriteria(lastName, 2);
            result = prepareResults(customers, 2);
        } else if (!StringUtils.isBlank(email)) {
            customers = customerDao.getCustomerByCriteria(email, 3);
            result = prepareResults(customers, 3);
        } else if (!StringUtils.isBlank(phone)) {
            customers = customerDao.getCustomerByCriteria(phone, 4);
            result = prepareResults(customers, 4);
        } else if (!StringUtils.isBlank(cellPhone)) {
            customers = customerDao.getCustomerByCriteria(cellPhone, 5);
            result = prepareResults(customers, 1);
        }
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(result);
        writer.close();
        return null;
    }

    private String prepareResults(Collection<Customer> customers, int type) {
        StringBuilder result = new StringBuilder(start_UL);
        for (Customer customer : customers) {
            result.append(MessageFormat.format(start_LI, customer.getId().toString(), StringUtils.join(new String[] { customer.getFirstName(), customer.getLastName(), customer.getPhone(), customer.getCellPhone(), customer.getEmail() }, " ")) + end_LI);
        }
        result.append(end_UL);
        return result.toString();
    }
}
