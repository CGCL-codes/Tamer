package eu.more.identityproviderservice.internal.usersCredentials;

import org.soda.dpws.DPWSException;
import eu.more.identityproviderservice.internal.hash.Hash;

/**
 * 
 * @author Emilio Salazar
 *
 **/
public class HumanCredential implements UserCredential {

    private String humanPassword = null;

    private String humanID = null;

    /**
	 * Constructor: Creates a new human credential
	 * @param MORE_ID
	 * @param password
	 * @param cipherKey
	 * @throws DPWSException 
	 */
    public HumanCredential(String MORE_ID, String password) throws DPWSException {
        this.humanPassword = Hash.hash(password);
        this.humanID = MORE_ID;
    }

    @Override
    public String toString() {
        try {
            return Hash.hash(this.humanID + this.humanPassword);
        } catch (DPWSException e) {
            e.printStackTrace();
            return null;
        }
    }
}
