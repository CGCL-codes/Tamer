package org.dolmen.swing.application;

import org.dolmen.core.container.Container;
import org.dolmen.swing.GUIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * default {@link Application}
 *
 * @since 0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public abstract class DefaultApplication implements Application {

    /**
	 * the logger
	 */
    private static Logger fLogger = LoggerFactory.getLogger(DefaultApplication.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The configuration
	 */
    private ApplicationConfiguration fConfig;

    /**
	 * the objects container
	 */
    private Container fContainer;

    /**
	 * The Application Name
	 */
    private String fName = "";

    public DefaultApplication() {
        this("");
    }

    public DefaultApplication(String aName) {
        fName = aName;
    }

    public ApplicationConfiguration getConfiguration() {
        return fConfig;
    }

    public Container getContainer() {
        return fContainer;
    }

    public String getName() {
        if (getConfiguration() != null) {
            return "";
        } else if (fName != "") {
            return fName;
        } else {
            return getClass().getName();
        }
    }

    public void loadConfiguration() throws Exception {
        if (fConfig != null) {
            fLogger.debug("configuration was set, trying to load it");
            fConfig.load();
            fLogger.info("configuration readed");
        } else {
            throw new ApplicationException("No configuration was set");
        }
    }

    protected abstract void loadLogging(String aURL) throws Exception;

    public void setConfiguration(ApplicationConfiguration aConfig) {
        assert aConfig != null;
        fConfig = aConfig;
    }

    public void setContainer(Container aContainer) {
        fContainer = aContainer;
    }

    public void start() throws Exception {
        fLogger.info("Trying to start " + getName() + " application");
        loadConfiguration();
        if (getConfiguration().getLookAndFeel() != "") {
            GUIUtils.setLookAndFeel(getConfiguration().getLookAndFeel());
        } else {
            GUIUtils.setNativeLookAndFeel();
        }
        if (getContainer() != null) {
            fLogger.debug("Trying to start the container (" + getContainer().getName() + ")");
            getContainer().start();
        }
    }

    public void stop() throws Exception {
        fLogger.debug("trying to stopping the application");
        if (getContainer() != null) {
            fLogger.debug("Trying to stop the container (" + getContainer().getName() + ")");
            getContainer().stop();
        }
        fLogger.info("application stopped");
        System.exit(0);
    }
}
