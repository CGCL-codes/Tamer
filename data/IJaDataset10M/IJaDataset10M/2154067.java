package de.molimo.session.actions;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import de.molimo.common.EJBHelper;
import de.molimo.common.exceptions.ActionException;
import de.molimo.common.exceptions.SeriousActionException;
import de.molimo.server.AttributeLocal;
import de.molimo.server.ThingLocal;
import de.molimo.server.ThingLocalHome;
import de.molimo.session.BrowserSession;
import de.molimo.session.handler.SessionTierExceptionHandler;

public class ShowThingAttributeAction extends AbstractDirectAction {

    private static final String refThing = "ThingBean";

    private static final String PARAM_THING_TYPE_NAME = "thingTypeName";

    private static final String PARAM_ATTRIBUTE_NAME = "attributeName";

    public Object containerCall() throws ActionException, EJBException, SeriousActionException {
        try {
            Context context = new InitialContext();
            ThingLocalHome thingLocalHome = (ThingLocalHome) context.lookup(EJBHelper.beanEnvironment + refThing);
            ThingLocal thingLocal = thingLocalHome.findByPrimaryKey(getParameter(PARAM_THING_TYPE_NAME));
            AttributeLocal attributeLocal = thingLocal.getAttributeByName(getParameter(PARAM_ATTRIBUTE_NAME));
            Collection collection = attributeLocal.getConstrainedThings(getThing().getName());
            if (collection.size() < 1) {
                throw new SeriousActionException("ACTION.SHOWTHINGATTRIBUTE.NODATA");
            }
            BrowserSession browserSession = getUserSession().createBrowser();
            browserSession.setCollection(collection);
            getActionContainerInfo().setNextApplication(browserSession);
            return null;
        } catch (NamingException e) {
            SessionTierExceptionHandler.handle(e);
            return null;
        } catch (FinderException e) {
            SessionTierExceptionHandler.handle(e);
            return null;
        } catch (CreateException e) {
            SessionTierExceptionHandler.handle(e);
            return null;
        } catch (RemoteException e) {
            SessionTierExceptionHandler.handle(e);
            return null;
        }
    }

    public String getName() throws EJBException {
        return null;
    }
}
