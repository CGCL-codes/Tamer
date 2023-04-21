package com.genia.toolbox.web.gwt.form.server.spring.form;

/**
 * form to retrieve the data identifier before passing it to the form.jsp
 * through an iframe.
 */
public interface IdentifierForm {

    /**
   * returns the identifier of the data of the form to edit.
   * 
   * @return the identifier of the data of the form to edit
   */
    public abstract String getIdentifier();

    /**
   * returns the identifier of the form to display.
   * 
   * @return the identifier of the form to display
   */
    public abstract String getFormIdentifier();

    /**
   * getter for the backUrl property.
   * 
   * @return the backUrl
   */
    public String getBackUrl();
}
