package cn.ac.ntarl.umt.form;

import org.apache.struts.action.ActionForm;

/** 
 * MyEclipse Struts
 * Creation date: 06-29-2007
 * 
 * XDoclet definition:
 * @struts.form name="createGroupForm"
 */
public class AddProfileForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** description property */
    private String initObj;

    private String type;

    private String editProfileName;

    private String editProfileDescription;

    private int editProfileID;

    private String newProfileName;

    private String newProfileDescription;

    private String editProfileType;

    public AddProfileForm() {
    }

    public String getEditProfileType() {
        return this.editProfileType;
    }

    public void setEditProfileType(String editProfileType) {
        this.editProfileType = editProfileType;
    }

    public String getNewProfileName() {
        return this.newProfileName;
    }

    public void setNewProfileName(String newProfileName) {
        this.newProfileName = newProfileName;
    }

    public String getEditProfileDescription() {
        return this.editProfileDescription;
    }

    public void setEditProfileDescription(String editProfileDescription) {
        this.editProfileDescription = editProfileDescription;
    }

    public String getNewProfileDescription() {
        return this.newProfileDescription;
    }

    public void setNewProfileDescription(String newProfileDescription) {
        this.newProfileDescription = newProfileDescription;
    }

    public String getEditProfileName() {
        return this.editProfileName;
    }

    public void setEditProfileName(String editProfileName) {
        this.editProfileName = editProfileName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String action) {
        this.type = action;
    }

    public int getEditProfileID() {
        return this.editProfileID;
    }

    public void setEditProfileID(int editProfileID) {
        this.editProfileID = editProfileID;
    }

    public String getInitObj() {
        return this.initObj;
    }

    public void setInitObj(String initObj) {
        this.initObj = initObj;
    }

    public boolean isInit() {
        return "true".equalsIgnoreCase(initObj);
    }
}
