package org.informaticisenzafrontiere.openstaff.controller;

import org.informaticisenzafrontiere.openstaff.model.User;
import org.informaticisenzafrontiere.openstaff.security.AuthenticationException;
import org.informaticisenzafrontiere.openstaff.security.AuthenticationModule;

public class Login {

    private static User utente = null;

    private static int errorCode;

    public static boolean controlla(User user) {
        AuthenticationModule sas = new AuthenticationModule(user.getUsername(), user.getPassword());
        try {
            utente = sas.login();
            return true;
        } catch (AuthenticationException ae) {
            if (ae.getErrorCode() == 1) {
                System.out.println("password errata");
                errorCode = 1;
            } else {
                System.out.println("utente inesistente");
                errorCode = 0;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUser() {
        return utente;
    }

    public static int getErrorCode() {
        return errorCode;
    }
}
