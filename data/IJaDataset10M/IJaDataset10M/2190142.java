package com.bugfree4j.per.test.hibernatetest.struts.form;

import org.apache.struts.action.ActionForm;

/** 
 * Userinfo.java created by EasyStruts - XsltGen.
 * http://easystruts.sf.net
 * created on 12-24-2004
 * 
 * XDoclet definition:
 * @struts:form name="userInfo"
 */
public class UserinfoBean extends ActionForm {

    /** name property */
    private String name;

    /** 
	 * Returns the name.
	 * @return String
	 */
    public String getName() {
        return name;
    }

    /** 
	 * Set the name.
	 * @param name The name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}
