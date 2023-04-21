package uk.co.platosys.boox.core;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;

/**
 *
 * @author edward
 */
public class Auditor {

    private Clerk clerk;

    public Auditor(String name, Ledger ledger) {
    }

    public Auditor(String databaseName, String name, String password) throws CredentialsException, BooxException {
        this.clerk = new Clerk(databaseName, name, password);
    }
}
