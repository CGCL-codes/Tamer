package ee.webAppToolkit.storage.db4o;

import javax.inject.Inject;
import com.db4o.ObjectServer;
import com.db4o.events.Event4;
import com.db4o.events.EventListener4;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.events.ObjectInfoEventArgs;
import com.google.inject.Injector;

/**
 * Calls <code>injector.injectMembers</code> on all created instances
 *
 */
public class Db4oInstantiationMemberInjector implements EventListener4<ObjectInfoEventArgs> {

    private Injector _injector;

    @Inject
    public Db4oInstantiationMemberInjector(Injector injector, ObjectServer objectServer) {
        _injector = injector;
        EventRegistry serverEvents = EventRegistryFactory.forObjectContainer(objectServer.ext().objectContainer());
        serverEvents.instantiated().addListener(this);
    }

    @Override
    public void onEvent(Event4<ObjectInfoEventArgs> e, ObjectInfoEventArgs args) {
        _injector.injectMembers(args.object());
    }
}
