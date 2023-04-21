package com.quikj.application.communicator.applications.webtalk.controller;

import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import com.quikj.application.communicator.applications.webtalk.model.GroupTable;

/**
 * 
 * @author bhm
 */
public class DropCustomerSelectForm extends ActionForm {

    /** Holds value of property submit. */
    private String submit;

    /** Holds value of property domain. */
    private String domain;

    /** Holds value of property domains. */
    private ArrayList domains = new ArrayList();

    /** Creates a new instance of DropCustomerSelectForm */
    public DropCustomerSelectForm() {
    }

    /**
	 * Getter for property domain.
	 * 
	 * @return Value of property domain.
	 * 
	 */
    public String getDomain() {
        return this.domain;
    }

    /**
	 * Getter for property domains.
	 * 
	 * @return Value of property domains.
	 * 
	 */
    public ArrayList getDomains() {
        return this.domains;
    }

    /**
	 * Getter for property submit.
	 * 
	 * @return Value of property submit.
	 * 
	 */
    public String getSubmit() {
        return this.submit;
    }

    /**
	 * Setter for property domain.
	 * 
	 * @param domain
	 *            New value of property domain.
	 * 
	 */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
	 * Setter for property domains.
	 * 
	 * @param domains
	 *            New value of property domains.
	 * 
	 */
    public void setDomains(ArrayList domains) {
        this.domains = domains;
    }

    /**
	 * Setter for property submit.
	 * 
	 * @param submit
	 *            New value of property submit.
	 * 
	 */
    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if (submit.startsWith("Cancel") == true) {
            return null;
        }
        ActionErrors errors = new ActionErrors();
        try {
            if ((domain == null) || (domain.length() == 0)) {
                errors.add("domains", new ActionError("error.customer.select.domain"));
            }
            if (errors.isEmpty() == false) {
                GroupTable groups = new GroupTable();
                Connection c = (Connection) request.getSession().getAttribute("connection");
                groups.setConnection(c);
                ArrayList domain_list = groups.listDomains();
                if (domain_list != null) {
                    ArrayList list = new ArrayList();
                    Iterator iter = domain_list.iterator();
                    while (iter.hasNext() == true) {
                        String domain = (String) iter.next();
                        if (domain.equals("ace") == false) {
                            list.add(new LabelValueBean(domain, URLEncoder.encode(domain, "UTF-8")));
                        }
                    }
                    setDomains(list);
                }
            }
        } catch (Exception e) {
            errors.add("domains", new ActionError("error.internal.error"));
        }
        return errors;
    }
}
