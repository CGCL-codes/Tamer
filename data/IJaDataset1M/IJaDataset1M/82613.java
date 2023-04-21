package edu.ucla.mbi.imex.imexcentral.persistence.orm;

import edu.ucla.mbi.imex.imexcentral.persistence.facade.IMExCentralSecurityException;
import edu.ucla.mbi.imex.imexcentral.persistence.facade.NoSuchObjectException;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Sep 10, 2006
 * Time: 2:48:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractSecurePersonWorker {

    Person getPerson(String requestorLoginId, String loginId) throws IMExCentralSecurityException, NoSuchObjectException;

    void deactivatePerson(String requestorLoginId, String loginId, Boolean removeFromPublications) throws IMExCentralSecurityException, NoSuchObjectException;

    void reactivatePerson(String requestorLoginId, String loginId) throws IMExCentralSecurityException, NoSuchObjectException;

    void resetPassword(String requestorLoginId, String loginId, String newPassword, Boolean sendEmail) throws IMExCentralSecurityException, NoSuchObjectException;
}
