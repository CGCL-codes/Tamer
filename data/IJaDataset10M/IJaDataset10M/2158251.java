package com.quikj.application.communicator.admin.controller;

import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.quikj.application.communicator.admin.model.AccountElement;

/**
 * 
 * @author bhm
 */
public class AccountManagementForm extends ActionForm {

    /** Holds value of property name. */
    private String name;

    /** Holds value of property password. */
    private String password;

    /** Holds value of property verifyPassword. */
    private String verifyPassword;

    /** Holds value of property submit. */
    private String submit;

    /** Holds value of property additionalInfo. */
    private String additionalInfo;

    /** Holds value of property level. */
    private int level;

    /** Holds value of property featureList. */
    private String[] featureList;

    /** Holds value of property assignedFeatures. */
    private Object[] assignedFeatures;

    private static int MIN_PASSWORD_LENGTH = 4;

    /** Holds value of property domain. */
    private String domain;

    /** Creates a new instance of AccountManagementForm */
    public AccountManagementForm() {
        ArrayList configured_features = AdminConfig.getInstance().getApplications();
        featureList = new String[configured_features.size()];
        int count = 0;
        for (Iterator i = configured_features.iterator(); i.hasNext(); count++) {
            featureList[count] = ((ApplicationElement) i.next()).getDisplayName();
        }
        reset();
    }

    public static void validatePassword(String password, ActionErrors errors, String fieldname) {
        password = password.trim();
        if (password.length() == 0) {
            errors.add(fieldname, new ActionError("error.account.password.hasblanks"));
        } else {
            int len = password.length();
            if (len < MIN_PASSWORD_LENGTH) {
                errors.add(fieldname, new ActionError("error.account.password.tooshort", new Integer(MIN_PASSWORD_LENGTH)));
            }
            boolean letter = false;
            boolean digit = false;
            for (int i = 0; i < len; i++) {
                char c = password.charAt(i);
                if (Character.isDigit(c) == true) {
                    digit = true;
                } else if (Character.isLetter(c) == true) {
                    letter = true;
                } else if (Character.isSpaceChar(c) == true) {
                    errors.add(fieldname, new ActionError("error.account.password.hasblanks"));
                }
            }
            if ((letter == false) || (digit == false)) {
                errors.add(fieldname, new ActionError("error.account.password.content"));
            }
        }
    }

    /**
	 * Getter for property additionalInfo.
	 * 
	 * @return Value of property additionalInfo.
	 * 
	 */
    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    /**
	 * Getter for property assignedFeatures.
	 * 
	 * @return Value of property assignedFeatures.
	 * 
	 */
    public Object[] getAssignedFeatures() {
        return this.assignedFeatures;
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
	 * Getter for property featureList.
	 * 
	 * @return Value of property featureList.
	 * 
	 */
    public String[] getFeatureList() {
        return this.featureList;
    }

    /**
	 * Getter for property level.
	 * 
	 * @return Value of property level.
	 * 
	 */
    public int getLevel() {
        return this.level;
    }

    /**
	 * Getter for property name.
	 * 
	 * @return Value of property name.
	 * 
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Getter for property password.
	 * 
	 * @return Value of property password.
	 * 
	 */
    public String getPassword() {
        return this.password;
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
	 * Getter for property verifyPassword.
	 * 
	 * @return Value of property verifyPassword.
	 * 
	 */
    public String getVerifyPassword() {
        return this.verifyPassword;
    }

    public void reset() {
        password = null;
        verifyPassword = null;
        submit = "Find";
        additionalInfo = null;
        level = AccountElement.LEVEL_CUSTOMER;
        assignedFeatures = new Object[featureList.length];
        domain = null;
    }

    /**
	 * Setter for property additionalInfo.
	 * 
	 * @param additionalInfo
	 *            New value of property additionalInfo.
	 * 
	 */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo.trim();
    }

    /**
	 * Setter for property assignedFeatures.
	 * 
	 * @param assignedFeatures
	 *            New value of property assignedFeatures.
	 * 
	 */
    public void setAssignedFeatures(Object[] assignedFeatures) {
        this.assignedFeatures = assignedFeatures;
    }

    /**
	 * Setter for property domain.
	 * 
	 * @param domain
	 *            New value of property domain.
	 * 
	 */
    public void setDomain(String domain) {
        this.domain = domain.trim();
    }

    /**
	 * Setter for property level.
	 * 
	 * @param level
	 *            New value of property level.
	 * 
	 */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
	 * Setter for property name.
	 * 
	 * @param name
	 *            New value of property name.
	 * 
	 */
    public void setName(String name) {
        this.name = name.trim();
    }

    /**
	 * Setter for property password.
	 * 
	 * @param password
	 *            New value of property password.
	 * 
	 */
    public void setPassword(String password) {
        this.password = password;
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

    /**
	 * Setter for property verifyPassword.
	 * 
	 * @param verifyPassword
	 *            New value of property verifyPassword.
	 * 
	 */
    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if ((name == null) || (name.length() == 0)) {
            errors.add("name", new ActionError("error.account.no.name"));
        }
        if (submit.equals("Create") == true) {
            if ((password == null) || (password.length() == 0)) {
                errors.add("password", new ActionError("error.account.no.password"));
            } else {
                validatePassword(password, errors, "password");
                if (verifyPassword != null) {
                    if (password.equals(verifyPassword) == false) {
                        errors.add("password", new ActionError("error.account.password.mismatch"));
                    }
                } else {
                    errors.add("password", new ActionError("error.account.password.mismatch"));
                }
            }
        }
        if (submit.equals("Modify") == true) {
            if ((password != null) && (password.length() > 0)) {
                validatePassword(password, errors, "password");
                if (verifyPassword != null) {
                    if (password.equals(verifyPassword) == false) {
                        errors.add("password", new ActionError("error.account.password.mismatch"));
                    }
                } else {
                    errors.add("password", new ActionError("error.account.password.mismatch"));
                }
            }
        }
        if ((submit.equals("Modify") == true) || (submit.equals("Create") == true)) {
            if (level == AccountElement.LEVEL_CUSTOMER) {
                if ((domain == null) || (domain.length() == 0)) {
                    errors.add("domain", new ActionError("error.account.no.domain"));
                }
            }
        }
        return errors;
    }
}
