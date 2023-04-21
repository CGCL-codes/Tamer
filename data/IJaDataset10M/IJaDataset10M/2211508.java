package org.knowhowlab.osgi.tips.activation.ds;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.tips.activation.core.Echo;
import org.osgi.service.prefs.PreferencesService;

/**
 * @author dmytro.pishchukhin
 */
@Component(name = "Echo", immediate = true)
@Service(value = Echo.class)
@Property(name = Echo.ECHO_TYPE_PROP, value = "Declarative Services")
public class EchoComponent implements Echo {

    @Reference(name = "preferencesService", referenceInterface = PreferencesService.class, cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private PreferencesService preferencesService;

    public String echo(String str) {
        return str;
    }

    public void bindPreferencesService(PreferencesService preferencesService) {
        System.out.println("PreferencesService is linked");
        this.preferencesService = preferencesService;
    }

    public void unbindPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = null;
        System.out.println("PreferencesService is unlinked");
    }
}
