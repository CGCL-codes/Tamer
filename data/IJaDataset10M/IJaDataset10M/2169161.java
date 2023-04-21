package de.mogwai.common.usercontext;

/**
 * Authentifizierungsobjekt mit Rolleninformationen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 15:06:45 $
 */
public interface AuthenticatableWithRole extends Authenticatable {

    /**
     * Ermittlung der Rollen.
     * 
     * @return die Rollen
     */
    Role[] getRoles();
}
