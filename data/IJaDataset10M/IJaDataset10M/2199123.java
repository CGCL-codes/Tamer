package es.devel.opentrats.view.controller;

import es.devel.opentrats.dao.ICustomerDao;
import es.devel.opentrats.dao.IDelegationDao;
import es.devel.opentrats.dao.exception.CustomerDaoException;
import es.devel.opentrats.dao.exception.DelegationDaoException;
import es.devel.opentrats.model.Customer;
import es.devel.opentrats.model.DTO.LastAttentionDTO;
import es.devel.opentrats.model.Delegation;
import es.devel.opentrats.view.command.CustomerCommandBean;
import es.devel.opentrats.view.controller.common.OpenTratsSimpleFormController;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author  Fran Serrano
 */
public class CustomerSimpleFormController extends OpenTratsSimpleFormController {

    private ICustomerDao customerDao;

    private IDelegationDao delegationDao;

    public CustomerSimpleFormController() {
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView mav = new ModelAndView(getFormView());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        CustomerCommandBean customerCommand = (CustomerCommandBean) command;
        Customer customer = new Customer();
        if (customerCommand.getIdCustomer().equals("")) {
            customer.setIdCustomer(null);
        } else {
            customer.setIdCustomer(new Long(customerCommand.getIdCustomer()));
            MultipartFile file = customerCommand.getPicture();
            String path = request.getSession().getServletContext().getRealPath("/");
            String destPath = path + "img/repo/customers/" + customer.getIdCustomer() + ".jpg";
            if (file != null) {
                file.transferTo(new File(destPath));
                customer.setPicture("" + customer.getIdCustomer());
            }
        }
        customer.setAddress(customerCommand.getAddress().trim());
        customer.setDate(df.parse(customerCommand.getDate().trim()));
        customer.setCity(customerCommand.getCity().trim());
        customer.setComments(customerCommand.getComments());
        customer.setEmail(customerCommand.getEmail().trim());
        customer.setMobile(customerCommand.getMobile().trim());
        customer.setName(customerCommand.getName().trim());
        customer.setNif(customerCommand.getNif().trim());
        customer.setPhone(customerCommand.getPhone().trim());
        customer.setPostalCode(customerCommand.getPostalCode().trim());
        customer.setProvince(customerCommand.getProvince().trim());
        if (customerCommand.isMailing() == true) {
            customer.setMailing(1);
        } else {
            customer.setMailing(0);
        }
        customer.setSurname(customerCommand.getSurname().trim());
        customer.setRefDelegation(new Integer(customerCommand.getRefDelegation()));
        customer.setReferences(customerCommand.getReferences());
        customer.setDate(df.parse(customerCommand.getDate().trim()));
        customer.setClubPartner(customerCommand.isClubPartner());
        customer.setClubCredits(customerCommand.getClubCredits());
        customer.setGender(new Integer(customerCommand.getGender()));
        try {
            customerDao.save(customer);
            putOk(request, "Datos guardados con exito.");
        } catch (CustomerDaoException e) {
            e.printStackTrace();
            errors.rejectValue("nif", "", "Hubo algun fallo al guardar.");
            return showForm(request, errors, getFormView());
        }
        mav.setViewName(getSuccessView());
        return mav;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object obj, Errors errors) throws Exception {
        String idCustomer = request.getParameter("idCustomer");
        List<Delegation> delegationList = null;
        List<LastAttentionDTO> lastAttentionList = null;
        if (idCustomer != null && idCustomer.equals("") == false) {
            try {
                lastAttentionList = (List<LastAttentionDTO>) customerDao.getLastServices(new Long(idCustomer));
            } catch (CustomerDaoException e) {
                e.printStackTrace();
            }
        }
        try {
            delegationList = (List<Delegation>) delegationDao.getAllDelegations();
        } catch (DelegationDaoException e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("lastAttentionList", lastAttentionList);
        map.put("delegationList", delegationList);
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String idCustomer = request.getParameter("idCustomer");
        CustomerCommandBean customerCommand = (CustomerCommandBean) createCommand();
        Customer customer = null;
        if (idCustomer != null && idCustomer.equals("") == false) {
            try {
                customer = (Customer) customerDao.load(new Long(idCustomer));
            } catch (CustomerDaoException e) {
                e.printStackTrace();
            }
            customerCommand.setAddress(customer.getAddress());
            Date customerDate = customer.getBirthDate();
            if (customerDate != null) {
                customerCommand.setBirthDate(sdf.format(customerDate));
            } else {
                customerCommand.setBirthDate("");
            }
            customerCommand.setCity(customer.getCity());
            customerCommand.setComments(customer.getComments());
            customerCommand.setDate(sdf.format(customer.getDate()));
            customerCommand.setEmail(customer.getEmail());
            customerCommand.setIdCustomer(String.valueOf(customer.getIdCustomer()));
            boolean mailing = false;
            if (customer.getMailing() == 1) {
                mailing = true;
            }
            customerCommand.setMailing(mailing);
            customerCommand.setMobile(customer.getMobile());
            customerCommand.setName(customer.getName());
            customerCommand.setNif(customer.getNif());
            customerCommand.setPhone(customer.getPhone());
            customerCommand.setPostalCode(customer.getPostalCode());
            customerCommand.setProvince(customer.getProvince());
            customerCommand.setRefDelegation(String.valueOf(customer.getRefDelegation()));
            customerCommand.setReferences(customer.getReferences());
            customerCommand.setSurname(customer.getSurname());
            customerCommand.setClubCredits(customer.getClubCredits());
            customerCommand.setClubPartner(customer.isClubPartner());
            customerCommand.setGender(String.valueOf(customer.getGender()));
        } else {
            customerCommand.setAddress("");
            customerCommand.setDate("");
            customerCommand.setCity("");
            customerCommand.setEmail("");
            customerCommand.setIdCustomer(null);
            customerCommand.setMobile("");
            customerCommand.setName("");
            customerCommand.setNif("");
            customerCommand.setPhone("");
            customerCommand.setPostalCode("");
            customerCommand.setProvince("");
            customerCommand.setSurname("");
            customerCommand.setMailing(false);
            customerCommand.setComments("");
            customerCommand.setReferences("");
            customerCommand.setRefDelegation("");
            customerCommand.setBirthDate("");
            customerCommand.setPictureUrl("nd");
            customerCommand.setClubCredits(0);
            customerCommand.setClubPartner(false);
            customerCommand.setGender("");
        }
        return customerCommand;
    }

    public ICustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public IDelegationDao getDelegationDao() {
        return delegationDao;
    }

    public void setDelegationDao(IDelegationDao delegationDao) {
        this.delegationDao = delegationDao;
    }
}
