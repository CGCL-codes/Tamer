package de.g18.gruppe3.common.ldap;

import java.util.List;
import de.g18.gruppe3.common.exception.ServiceException;
import de.g18.gruppe3.common.model.User;

/**
 * LDAP-Schnittstelle um Benutzer und Organisationseinheiten im LDAP mit einer Liste
 * abzugleichen und zu Synchronizieren
 *
 * @author <a href="mailto:kevinhuber.kh@gmail.com">Kevin Huber</a>
 */
public interface LDAPSynchronizationService {

    /**
     * Führt eine "harte"-Synchronisation mit dem LDAP durch.<br/>
     * Bei der Synchronisation wird wie folgt vorgegangen:
     * <ul>
     *   <li>Organisationseinheiten &amp; Benutzer die nicht in der Liste stehen werden aus dem LDAP gelöscht</li>
     *   <li>Organisationseinheiten &amp; Benutzer die noch nicht im LDAP sind werden neu angelegt</li>
     * </ul>
     *
     * @param aUserList Liste mit Benutzers die im LDAP eingetragen sein sollen
     *
     * @throws ServiceException
     */
    void synchronize(List<User> aUserList) throws ServiceException;
}
