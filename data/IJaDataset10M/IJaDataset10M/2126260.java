package org.openscience.nmrshiftdb.webservices;

import org.apache.soap.Constants;
import org.apache.soap.SOAPException;
import org.openscience.nmrshiftdb.util.UrlTool;

/**
 *  This is a web service class informing about the number of logged in users
 *
 * @author     shk3
 * @created    January 21, 2003
 */
public class UserService {

    /**
   *  Gets the userNumber attribute of the UserService object
   *
   * @return                The userNumber value
   * @exception  Exception  Description of Exception
   */
    public String getUserNumber() throws SOAPException {
        try {
            return (new String(UrlTool.getUsersMap().size() + ""));
        } catch (Exception ex) {
            throw new SOAPException(Constants.FAULT_CODE_SERVER, "Database problems", ex);
        }
    }
}
