package cn.ac.ntarl.umt.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 07-02-2007
 * 
 * XDoclet definition:
 * @struts.form name="deleteGroupForm"
 */
public class ListUserApplyGroupForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** groupname property */
    private int offset = 0;

    private String act = "load";

    private int voID = -1;

    private int groupID;

    /**
	 * @return the voID
	 */
    public int getVoID() {
        return voID;
    }

    /**
	 * @param voID the voID to set
	 */
    public void setVoID(int voID) {
        this.voID = voID;
    }

    /** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.offset = 0;
        act = "load";
        voID = -1;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    /**
	 * @return the groupID
	 */
    public int getGroupID() {
        return groupID;
    }

    /**
	 * @param groupID the groupID to set
	 */
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
