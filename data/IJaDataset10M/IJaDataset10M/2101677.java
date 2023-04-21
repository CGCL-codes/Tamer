package br.com.felix.bmt.web;

import br.com.felix.fwt.ui.HtmlWriter;
import br.com.felix.fwt.ui.exception.UserInterfaceInitializationException;

/**
 * I haven't tested it, to see if it works.
 * */
public class ErrorPage extends BlogManagementForm {

    private static final long serialVersionUID = 8923585638364845492L;

    private String userMessage;

    private Exception e;

    public ErrorPage(String userMessage, Exception e) {
        this.userMessage = userMessage;
        this.e = e;
    }

    @Override
    protected void initializeUserInterface() {
        HtmlWriter htmlw = new HtmlWriter();
        htmlw.header1("An Error has ocurred!");
        htmlw.paragraphStart();
        htmlw.text(userMessage);
        htmlw.paragraphEndStart();
        htmlw.text(e.getMessage());
        htmlw.paragraphEnd();
        add(htmlw);
    }
}
