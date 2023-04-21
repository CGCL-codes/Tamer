package com.croftsoft.app.odyssey;

import java.io.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/***********************************************************************
    * Odyssey Store.
    * 
    * @version
    *   $Date$
    *   $Rev$
    *   $Author$
    * @since
    *   2011-12-07
    * @author
    *   David Wallace Croft 
    ***********************************************************************/
@Named
@RequestScoped
public class Store implements Serializable {

    private static final long serialVersionUID = 0L;

    @Inject
    private User user;

    public String getItem() {
        return "";
    }

    public void setItem(final String item) {
        if (item == null || item.equals("")) {
            return;
        }
        final int credits = user.getCredits();
        if (credits > 0) {
            user.setCredits(credits - 1);
        }
    }

    public String leave() {
        return "start";
    }
}
