package ru.newton.pokertrainer.web.gwt.pokertrainermodule.client.ui.pages;

import ru.newton.pokertrainer.web.gwt.pokertrainermodule.client.ui.windows.LoginWindow;
import ru.newton.pokertrainer.web.gwt.pokertrainermodule.client.ui.pages.Page;

/**
 * Login page opens a login window allowing to perform authentication.
 *
 * @author echo
 */
public class LoginPage extends Page {

    public LoginPage(final boolean error) {
        LoginWindow loginWindow = new LoginWindow(error);
        loginWindow.setShowModalMask(true);
        loginWindow.show();
    }
}
