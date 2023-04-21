package com.example.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * MyEclipse Struts Creation date: 01-26-2009
 * 
 * XDoclet definition:
 * 
 * @struts.form name="fileUpload3Form"
 */
public class FileUpload3Form extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private FormFile theFile;

    public FileUpload3Form() {
        System.out.println("create UploadFileForm object");
    }

    /**
	 * @return Returns the theFile.
	 */
    public FormFile getTheFile() {
        return theFile;
    }

    /**
	 * @param theFile
	 *            The FormFile to set.
	 */
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
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
