package net.sf.jlue.example;

import org.apache.struts.action.ActionForm;

/**
 * @author Sun Yat-ton (Mail:PubTlk@Hotmail.com)
 * @version 1.00 2008-1-23
 * 
 */
public class HelloWorldForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
