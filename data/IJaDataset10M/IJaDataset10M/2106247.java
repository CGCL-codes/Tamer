package fr.gfi.gfinet.web.struts.business.customerManagement.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

/**
 * 
 * @author esousa  
 *
 */
public class CustomerForm extends ValidatorForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Fields
	 */
    private String idCustomer;

    private String selectedParentId;

    private String customerType;

    private String name;

    private String address1;

    private String address2;

    private String address3;

    private String code;

    private String ville;

    private String nameResponsible;

    private String idResponsible;

    private String businessActivity;

    private String specificity;

    private String comments;

    /**
	 * Getters Setters 
	 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIdResponsible() {
        return idResponsible;
    }

    public void setIdResponsible(String idResponsible) {
        this.idResponsible = idResponsible;
    }

    public String getNameResponsible() {
        return nameResponsible;
    }

    public void setNameResponsible(String nameResponsible) {
        this.nameResponsible = nameResponsible;
    }

    public String getBusinessActivity() {
        return businessActivity;
    }

    public void setBusinessActivity(String businessActivity) {
        this.businessActivity = businessActivity;
    }

    public String getSpecificity() {
        return specificity;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getSelectedParentId() {
        return selectedParentId;
    }

    public void setSelectedParentId(String selectedParentId) {
        this.selectedParentId = selectedParentId;
    }

    /**
	 * Reset function
	 */
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        idCustomer = null;
        selectedParentId = null;
        customerType = new String();
        name = null;
        address1 = null;
        address2 = null;
        address3 = null;
        code = null;
        ville = null;
        businessActivity = null;
        specificity = null;
        comments = null;
        nameResponsible = null;
        idResponsible = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if ((name == null) || (name.length() == 0)) {
            errors.add("error", new ActionMessage("customerManagement.error.name"));
        }
        if ((nameResponsible == null) || (nameResponsible.length() == 0)) {
            errors.add("error", new ActionMessage("customerManagement.error.responsible"));
        }
        return errors;
    }
}
