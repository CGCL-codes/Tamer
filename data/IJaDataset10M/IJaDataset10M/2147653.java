package de.molimo.session.security;

import java.security.Principal;
import java.util.Collection;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import de.molimo.common.EJBHelper;
import de.molimo.server.AttributeLocalHome;
import de.molimo.server.ThingLocal;
import de.molimo.session.handler.SessionTierExceptionHandler;

/**
 * @author Marcus Schiesser
 */
public class ThingHasAttributesChecker implements ISecurityChecker {

    public boolean canCall(Principal principal, ThingLocal thing) {
        try {
            AttributeLocalHome attributeLocalHome = EJBHelper.getAttributeHome();
            Collection attributes = attributeLocalHome.findByThing(thing);
            return attributes.size() != 0;
        } catch (NamingException e) {
            SessionTierExceptionHandler.handle(e);
            return false;
        } catch (FinderException e) {
            SessionTierExceptionHandler.handle(e);
            return false;
        }
    }

    public void setParameter(String key, Object value) {
    }
}
