package org.isi.monet.applications.console.core;

import org.isi.monet.core.model.Account;
import org.isi.monet.core.model.User;

public interface IKernel {

    public User login(String sUsername, String sPassword);

    public Boolean logout();

    public Account loadAccount();

    public Boolean isLogged();

    public User loadUserLogged();

    public String getUserLanguage();
}
