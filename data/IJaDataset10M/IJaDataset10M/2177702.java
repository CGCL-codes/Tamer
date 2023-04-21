package org.bejug.javacareers.common.view.jsf.utils;

import javax.faces.event.ActionEvent;
import org.bejug.javacareers.jobs.view.jsf.util.Utils;

/**
 * @author : Peter Symoens (Last modified by $Author: schauwvliege $)
 * @version $Revision: 1.8 $ - $Date: 2005/09/06 13:22:50 $:
 */
public class LoginAction {

    /**
     * @param actionEvent actionEvent
     */
    public void processLogin(ActionEvent actionEvent) {
        Utils.setDefaultTargetUrl("/home.jsf");
        Utils.redirectToLogin();
    }
}
