package net.frontlinesms.plugins;

import java.util.Locale;
import org.apache.log4j.Logger;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Base implementation of the {@link PluginSettingsController} annotation.
 * 
 * Implementers of this class *must* carry the {@link PluginControllerProperties} annotation.
 * 
 * This class includes default implementation of the text resource loading methods.  These attempt to load text resources
 * in the following way:
 * TODO properly document how this is done from the methods {@link #getDefaultTextResource()} and {@link #getTextResource(Locale)}.
 * 
 * @author Alex
 */
public abstract class BasePluginSettingsController implements PluginController {

    /** Logging object for this class */
    protected final Logger log = FrontlineUtils.getLogger(this.getClass());

    public String getTitle() {
        return this.getName(InternationalisationUtils.getCurrentLocale());
    }
}
