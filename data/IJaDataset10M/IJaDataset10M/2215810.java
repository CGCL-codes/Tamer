package de.molimo.session.actions;

import javax.ejb.EJBLocalObject;
import de.molimo.server.ThingLocal;
import de.molimo.session.IApplication;

/**
 @author Marcus Schiesser
 */
public interface ActionContainerInfoLocal extends EJBLocalObject {

    void setNextApplication(IApplication cancelApp);

    IApplication getNextApplication();

    void setThing(ThingLocal thing);

    ThingLocal getThing();
}
