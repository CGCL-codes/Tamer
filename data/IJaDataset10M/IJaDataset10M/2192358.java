package org.ji18n.core.test;

import static org.ji18n.core.util.Locales.en_US;
import java.util.Locale;
import org.ji18n.core.injection.Injection;
import org.ji18n.core.registry.Registry;

/**
 * @version $Id: AbstractCoreTests.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public abstract class AbstractCoreTests {

    private static final Locale ORIGINAL_DEFAULT_LOCALE = Locale.getDefault();

    public void startRegistry() {
        Registry.instance().start();
    }

    public void startRegistry(String configResource) {
        Registry.instance().start(configResource);
    }

    public void startRegistry(String[] configResources) {
        Registry.instance().start(configResources);
    }

    public void stopRegistry() {
        Registry.instance().stop();
    }

    public void inject() {
        Injection.inject(this);
    }

    public void disinject() {
        Injection.disinject(this);
    }

    public void setAssumedDefaultLocale() {
        Locale.setDefault(en_US);
    }

    public void setOriginalDefaultLocale() {
        Locale.setDefault(ORIGINAL_DEFAULT_LOCALE);
    }
}
