package org.nakedobjects.example.simple;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.value.Password;

public class SecureContact extends AbstractDomainObject {

    public static String[] namesDoSomething(String name) {
        return new String[] { "Name" };
    }

    public void doSomething(@Named("sdasdas") @Optional String name) {
    }

    public void created() {
        password = new Password("password");
    }

    private Password password;

    public Password getPassword() {
        resolve(password);
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
        objectChanged();
    }

    public String getClearTextPassword() {
        return password == null ? "" : password.getPassword();
    }
}
