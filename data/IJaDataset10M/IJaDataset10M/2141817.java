package com.google.gwt.maeglin89273.game.mengine.service;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * @author Maeglin Liao
 *
 */
public class LoginInfo implements IsSerializable {

    public enum Status implements IsSerializable {

        LOGGED_IN, LOGGED_OUT, SERVER_ERROR, OTHER_ERROR, OFFLINE;

        private Status() {
        }
    }

    ;

    private Status status;

    private String url;

    private GoogleAccount account;

    public LoginInfo(Status status, String url, GoogleAccount account) {
        this.status = status;
        this.url = url;
        this.account = account;
    }

    public LoginInfo(Status status, String url) {
        this(status, url, null);
    }

    public LoginInfo(Status status) {
        this(status, null);
    }

    private LoginInfo() {
    }

    /**
	 * @return the status
	 */
    public Status getStatus() {
        return status;
    }

    /**
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @return the user
	 */
    public GoogleAccount getAccount() {
        return account;
    }

    public boolean isConnectionSuccess() {
        return status == Status.LOGGED_IN || status == Status.LOGGED_OUT;
    }

    public static LoginInfo getExceptionalLoginInfo(Throwable caught) {
        if (caught instanceof StatusCodeException) {
            StatusCodeException e = (StatusCodeException) caught;
            switch(e.getStatusCode()) {
                case 500:
                    return new LoginInfo(Status.SERVER_ERROR);
                case 0:
                    return new LoginInfo(Status.OFFLINE);
                default:
                    return new LoginInfo(Status.OTHER_ERROR);
            }
        } else {
            return new LoginInfo(Status.OTHER_ERROR);
        }
    }
}
