package com.example.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 08-26-2008
 * 
 * XDoclet definition:
 * 
 * @struts.form name="updateGene2Form"
 */
public class UpdateGene2Form extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String[] selectedItems = new String[0];

    public String[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(String[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    /**
	 * Method validate
	 * 
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
	 * 
	 * @param mapping
	 * @param request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
}
