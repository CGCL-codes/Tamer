package com.hack23.cia.service.user.impl;

import org.hisrc.hifaces20.testing.webappenvironment.WebAppEnvironment;
import org.hisrc.hifaces20.testing.webappenvironment.annotations.PropertiesWebAppEnvironmentConfig;
import org.hisrc.hifaces20.testing.webappenvironment.testing.junit4.AbstractRunWebApp;

public class RunSampleUserService extends AbstractRunWebApp {

    @PropertiesWebAppEnvironmentConfig("src/test/resources/test-web.properties")
    public void setWebAppEnvironment(WebAppEnvironment webAppEnvironment) {
        super.setWebAppEnvironment(webAppEnvironment);
    }
}
