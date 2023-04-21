package com.yhtl.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 07-31-2006
 * 
 * XDoclet definition:
 * @struts.form name="addUserForm"
 */
public class AddUserForm extends ActionForm {

    /** age property */
    private Integer age;

    /** startPoint property */
    private Integer startPoint;

    /** sex property */
    private Integer sex;

    /** password property */
    private String password;

    /** contact property */
    private String contact;

    /** workYear property */
    private Integer workYear;

    /** recommander property */
    private String recommander;

    /** capability property */
    private String capability;

    /** trueName property */
    private String trueName;

    /** nickName property */
    private String nickName;

    /** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if ((nickName == null) && (nickName.trim().equals(""))) {
            request.setAttribute("info", "[ERROR]添加用户失败：用户名不能为空");
            return new ActionErrors();
        } else {
            return null;
        }
    }

    /** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        sex = 0;
        startPoint = 100;
        password = "";
        contact = "";
        workYear = 1;
        recommander = "";
        capability = "";
        trueName = "";
    }

    /** 
	 * Returns the startPoint.
	 * @return Integer
	 */
    public Integer getStartPoint() {
        return startPoint;
    }

    /** 
	 * Set the startPoint.
	 * @param startPoint The startPoint to set
	 */
    public void setStartPoint(Integer startPoint) {
        this.startPoint = startPoint;
    }

    /** 
	 * Returns the sex.
	 * @return Integer
	 */
    public Integer getSex() {
        return sex;
    }

    /** 
	 * Set the sex.
	 * @param sex The sex to set
	 */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /** 
	 * Returns the password.
	 * @return String
	 */
    public String getPassword() {
        return password;
    }

    /** 
	 * Set the password.
	 * @param password The password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /** 
	 * Returns the contact.
	 * @return String
	 */
    public String getContact() {
        return contact;
    }

    /** 
	 * Set the contact.
	 * @param contact The contact to set
	 */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /** 
	 * Returns the workYear.
	 * @return Integer
	 */
    public Integer getWorkYear() {
        return workYear;
    }

    /** 
	 * Set the workYear.
	 * @param workYear The workYear to set
	 */
    public void setWorkYear(Integer workYear) {
        this.workYear = workYear;
    }

    /** 
	 * Returns the recommander.
	 * @return String
	 */
    public String getRecommander() {
        return recommander;
    }

    /** 
	 * Set the recommander.
	 * @param recommander The recommander to set
	 */
    public void setRecommander(String recommander) {
        this.recommander = recommander;
    }

    /** 
	 * Returns the capability.
	 * @return String
	 */
    public String getCapability() {
        return capability;
    }

    /** 
	 * Set the capability.
	 * @param capability The capability to set
	 */
    public void setCapability(String capability) {
        this.capability = capability;
    }

    /** 
	 * Returns the trueName.
	 * @return String
	 */
    public String getTrueName() {
        return trueName;
    }

    /** 
	 * Set the trueName.
	 * @param trueName The trueName to set
	 */
    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    /** 
	 * Returns the nickName.
	 * @return String
	 */
    public String getNickName() {
        return nickName;
    }

    /** 
	 * Set the nickName.
	 * @param nickName The nickName to set
	 */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
	 * Returns the age.
	 * @return age
	 */
    public Integer getAge() {
        return age;
    }

    /**
	 * Set the age.
	 * @param age the age to set
	 */
    public void setAge(Integer age) {
        this.age = age;
    }
}
