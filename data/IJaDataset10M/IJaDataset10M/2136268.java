package com.centropresse.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 12-27-2007
 * 
 * XDoclet definition:
 * @struts.form name="inserisciCategoriaForm"
 */
public class InserisciCategoriaForm extends ActionForm {

    /** categoria property */
    private String categoria;

    /** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    /** 
	 * Returns the categoria.
	 * @return String
	 */
    public String getCategoria() {
        return categoria;
    }

    /** 
	 * Set the categoria.
	 * @param categoria The categoria to set
	 */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
