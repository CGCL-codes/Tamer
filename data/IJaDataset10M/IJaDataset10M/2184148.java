package com.hba.web.lib.client.internationalization;

import com.google.gwt.i18n.client.Constants;

public interface ApplicationConstants extends Constants {

    @DefaultStringValue("Welcome")
    String hello();

    @DefaultStringValue("Sign out")
    String signOut();

    /**
	   * ***************************** APPLICATION MENU ******************************
	   */
    @DefaultStringValue("Home")
    String homeMenuItem();

    @DefaultStringValue("User")
    String userMenuItem();
}
