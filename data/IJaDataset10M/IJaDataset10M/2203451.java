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
public class UserManagementForm extends ActionForm {

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

    /** Holds value of property belongsToGroups. */
    private Object[] belongsToGroups;

    /** Holds value of property ownsGroups. */
    private Object[] ownsGroups;

    /** Holds value of property fullName. */
    private String fullName;

    /** Holds value of property address. */
    private String address;

    /** Holds value of property unavailXferTo. */
    private String unavailXferTo;

    private static int MIN_PASSWORD_LENGTH = 4;

    /** Holds value of property userGroups. */
    private ArrayList userGroups = new ArrayList();

    /** Holds value of property gatekeeper. */
    private String gatekeeper;

    /** Holds value of property domain. */
    private String domain;

    /** Creates a new instance of AccountManagementForm */
    public UserManagementForm() {
        reset();
    }

    public static void validatePassword(String password, ActionErrors errors) {
        password = password.trim();
        if (password.length() == 0) {
            errors.add("password", new ActionError("error.user.password.hasblanks"));
        } else {
            int len = password.length();
            if (len < MIN_PASSWORD_LENGTH) {
                errors.add("password", new ActionError("error.user.password.tooshort", new Integer(MIN_PASSWORD_LENGTH)));
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
                    errors.add("password", new ActionError("error.user.password.hasblanks"));
                }
            }
            if ((letter == false) || (digit == false)) {
                errors.add("password", new ActionError("error.user.password.content"));
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
	 * Getter for property address.
	 * 
	 * @return Value of property address.
	 * 
	 */
    public String getAddress() {
        return this.address;
    }

    /**
	 * Getter for property belongsToGroups.
	 * 
	 * @return Value of property belongsToGroups.
	 * 
	 */
    public Object[] getBelongsToGroups() {
        return this.belongsToGroups;
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
	 * Getter for property fullName.
	 * 
	 * @return Value of property fullName.
	 * 
	 */
    public String getFullName() {
        return this.fullName;
    }

    /**
	 * Getter for property gatekeeper.
	 * 
	 * @return Value of property gatekeeper.
	 * 
	 */
    public String getGatekeeper() {
        return this.gatekeeper;
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
	 * Getter for property ownsGroups.
	 * 
	 * @return Value of property ownsGroups.
	 * 
	 */
    public Object[] getOwnsGroups() {
        return this.ownsGroups;
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
	 * Getter for property unavailXferTo.
	 * 
	 * @return Value of property unavailXferTo.
	 * 
	 */
    public String getUnavailXferTo() {
        return this.unavailXferTo;
    }

    /**
	 * Getter for property usrGroups.
	 * 
	 * @return Value of property usrGroups.
	 * 
	 */
    public ArrayList getUserGroups() {
        return this.userGroups;
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
        belongsToGroups = null;
        ownsGroups = null;
        name = null;
        password = null;
        verifyPassword = null;
        submit = "Find";
        additionalInfo = null;
        fullName = null;
        address = null;
        unavailXferTo = null;
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
	 * Setter for property address.
	 * 
	 * @param address
	 *            New value of property address.
	 * 
	 */
    public void setAddress(String address) {
        this.address = address.trim();
    }

    /**
	 * Setter for property belongsToGroups.
	 * 
	 * @param belongsToGroups
	 *            New value of property belongsToGroups.
	 * 
	 */
    public void setBelongsToGroups(Object[] belongsToGroups) {
        this.belongsToGroups = belongsToGroups;
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
	 * Setter for property fullName.
	 * 
	 * @param fullName
	 *            New value of property fullName.
	 * 
	 */
    public void setFullName(String fullName) {
        this.fullName = fullName.trim();
    }

    /**
	 * Setter for property gatekeeper.
	 * 
	 * @param gatekeeper
	 *            New value of property gatekeeper.
	 * 
	 */
    public void setGatekeeper(String gatekeeper) {
        this.gatekeeper = gatekeeper;
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
	 * Setter for property ownsGroups.
	 * 
	 * @param ownsGroups
	 *            New value of property ownsGroups.
	 * 
	 */
    public void setOwnsGroups(Object[] ownsGroups) {
        this.ownsGroups = ownsGroups;
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
	 * Setter for property unavailXferTo.
	 * 
	 * @param unavailXferTo
	 *            New value of property unavailXferTo.
	 * 
	 */
    public void setUnavailXferTo(String unavailXferTo) {
        this.unavailXferTo = unavailXferTo.trim();
    }

    /**
	 * Setter for property usrGroups.
	 * 
	 * @param usrGroups
	 *            New value of property usrGroups.
	 * 
	 */
    public void setUserGroups(ArrayList userGroups) {
        this.userGroups = userGroups;
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
        if ((submit.startsWith("Finished") == true) || (submit.startsWith("Cancel") == true)) {
            return null;
        }
        ActionErrors errors = new ActionErrors();
        try {
            if ((name == null) || (name.length() == 0)) {
                errors.add("name", new ActionError("error.user.no.name"));
            }
            if (DataCheckUtility.followsTableIdRules(name) == false) {
                errors.add("name", new ActionError("error.user.invalid.id"));
            }
            if (submit.startsWith("Create") == true) {
                if ((password == null) || (password.length() == 0)) {
                    errors.add("password", new ActionError("error.user.no.password"));
                } else {
                    validatePassword(password, errors);
                    if (verifyPassword != null) {
                        if (password.equals(verifyPassword) == false) {
                            errors.add("password", new ActionError("error.user.password.mismatch"));
                        }
                    } else {
                        errors.add("password", new ActionError("error.user.password.mismatch"));
                    }
                }
            }
            if (submit.equals("Modify") == true) {
                if ((password != null) && (password.length() > 0)) {
                    validatePassword(password, errors);
                    if (verifyPassword != null) {
                        if (password.equals(verifyPassword) == false) {
                            errors.add("password", new ActionError("error.user.password.mismatch"));
                        }
                    } else {
                        errors.add("password", new ActionError("error.user.password.mismatch"));
                    }
                }
            }
            if ((submit.startsWith("Create") == true) || (submit.equals("Modify") == true)) {
                if (ownsGroups != null) {
                    if (belongsToGroups != null) {
                        for (int i = 0; i < ownsGroups.length; i++) {
                            String name = ownsGroups[i].toString();
                            for (int j = 0; j < belongsToGroups.length; j++) {
                                if (belongsToGroups[j].toString().equals(name) == true) {
                                    errors.add("belongsToGroups", new ActionError("error.user.groups.illegal"));
                                    break;
                                }
                            }
                        }
                    }
                }
                if (unavailXferTo != null) {
                    if (unavailXferTo.equals(name) == true) {
                        errors.add("unavailXferTo", new ActionError("error.user.unavail.invalid"));
                    }
                }
            }
            if (errors.isEmpty() == false) {
                GroupTable groups = new GroupTable();
                Connection c = (Connection) request.getSession().getAttribute("connection");
                groups.setConnection(c);
                ArrayList group_list = groups.list(domain);
                if (group_list != null) {
                    ArrayList list = new ArrayList();
                    Iterator iter = group_list.iterator();
                    while (iter.hasNext() == true) {
                        String group = (String) iter.next();
                        if (group.equals(domain) == false) {
                            list.add(new LabelValueBean(group, URLEncoder.encode(group, "UTF-8")));
                        }
                    }
                    setUserGroups(list);
                }
            }
        } catch (Exception e) {
            errors.add("unavailXferTo", new ActionError("error.internal.error"));
        }
        return errors;
    }
}
