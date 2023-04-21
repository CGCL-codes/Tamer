package cx.ath.mancel01.dependencyshot.event;

import cx.ath.mancel01.dependencyshot.api.DSInjector;
import javax.inject.Inject;

/**
 *
 * @author Mathieu ANCELIN
 */
public class InjectorStoppedEvent extends AutoEvent {

    @Inject
    private DSInjector injector;

    public DSInjector getInjector() {
        return injector;
    }
}
