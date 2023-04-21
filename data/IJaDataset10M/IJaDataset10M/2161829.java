package ru.nsu.ccfit.pm.econ.modules;

import static com.google.common.base.Preconditions.checkNotNull;
import ru.nsu.ccfit.pm.econ.view.shared.localization.Formatter;
import ru.nsu.ccfit.pm.econ.view.shared.localization.IFormatter;
import ru.nsu.ccfit.pm.econ.view.shared.localization.Localization;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Binds <tt>Localization</tt> to supplied instance.
 * @see Localization
 * @author dragonfly
 */
public class LocalizationModule extends AbstractModule {

    private Localization localization;

    public LocalizationModule(Localization localization) {
        this.localization = checkNotNull(localization, "No Localization object to inject");
    }

    @Override
    protected void configure() {
        bind(Localization.class).toInstance(localization);
        bind(IFormatter.class).to(Formatter.class);
        singletoniateEmUp();
    }

    private void singletoniateEmUp() {
        bind(Formatter.class).in(Scopes.SINGLETON);
    }
}
