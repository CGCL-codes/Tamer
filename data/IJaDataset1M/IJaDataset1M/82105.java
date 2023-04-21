package ru.nsu.ccfit.pm.econ.view.shared;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtkx.WTKXSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.ccfit.pm.econ.view.shared.guice.InjectionVisitable;
import ru.nsu.ccfit.pm.econ.view.shared.localization.Localization;

/**
 * Base abstract class for game applications. 
 * @author dragonfly
 */
public abstract class BaseApp implements Application {

    static final Logger logger = LoggerFactory.getLogger(BaseApp.class);

    public static final String LANGUAGE_KEY = "language";

    protected Window window = null;

    @Override
    public void resume() throws Exception {
        logger.debug("resume()");
    }

    @Override
    public boolean shutdown(boolean b) throws Exception {
        logger.debug("shutdown()");
        if (window != null) window.close();
        return false;
    }

    @Override
    public void startup(Display display, Map<String, String> properties) throws Exception {
        logger.debug("startup()");
        String language = properties.get(LANGUAGE_KEY);
        Localization localization = new Localization(language);
        WTKXSerializer wtkxSerializer = new WTKXSerializer(localization.getResources());
        window = (Window) wtkxSerializer.readObject(this, getWindowWTKX());
        DesktopApplicationContext.sizeToFit(window);
        initInjection((InjectionVisitable) window, localization);
        window.open(display);
    }

    @Override
    public void suspend() throws Exception {
        logger.debug("suspend()");
    }

    protected abstract String getWindowWTKX();

    protected abstract void initInjection(InjectionVisitable root, Localization localization);
}
