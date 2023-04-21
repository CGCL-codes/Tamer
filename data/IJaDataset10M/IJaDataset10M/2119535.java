package de.mogwai.common.web;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import de.mogwai.common.logging.Logger;

public class MogwaiApplicationFactory extends ApplicationFactory {

    private static final Logger LOGGER = new Logger(MogwaiApplicationFactory.class);

    private Application application;

    public MogwaiApplicationFactory() {
        LOGGER.logDebug("Initing ApplicationFactory");
        application = new MogwaiApplicationImpl();
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void setApplication(Application aApplication) {
        application = aApplication;
    }
}
