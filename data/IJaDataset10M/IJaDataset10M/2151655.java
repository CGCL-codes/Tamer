package idv.nightpig.lab01.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.sitebricks.SitebricksModule;

public class Lab01GuiceCreator extends GuiceServletContextListener {

    protected Injector getInjector() {
        return Guice.createInjector(new SitebricksModule() {

            protected void configureSitebricks() {
                scan(Hello.class.getPackage());
            }
        });
    }
}
