package ca.ucalgary.cpsc.agilePlanner.persister.auth;

public abstract class AbstractAuthenticator extends ca.ucalgary.cpsc.agilePlanner.util.Object {

    public abstract boolean performAuth(String username, String password);

    public boolean isValidUser(String username, String password) {
        return performAuth(username, password);
    }
}
