package eu.more.cryptographicservicecore.responses;

import java.io.Serializable;
import eu.more.cryptographicservicecore.ciphers.PasswordsGenerator;
import eu.more.cryptographicservicecore.commons.SecurityException;

public class BooleanResponse implements Serializable {

    private static final long serialVersionUID = -6012835101655682959L;

    public boolean isAuth = false;

    @SuppressWarnings("unused")
    private String alea = null;

    public BooleanResponse(boolean response) throws SecurityException {
        this.isAuth = response;
        this.alea = PasswordsGenerator.generateRandomPassword();
    }
}
