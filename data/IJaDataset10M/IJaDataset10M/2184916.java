package org.objectstyle.cayenne.conf;

import java.io.InputStream;
import org.apache.commons.lang.NotImplementedException;
import org.objectstyle.cayenne.conf.Configuration;
import org.objectstyle.cayenne.util.ResourceLocator;

/**
 * @author Andrei Adamchik
 */
public class MockConfiguration extends Configuration {

    public MockConfiguration() {
        super();
    }

    public boolean canInitialize() {
        return true;
    }

    public void didInitialize() {
    }

    protected InputStream getDomainConfiguration() {
        throw new NotImplementedException("this is an in-memory mockup...'getDomainConfiguration' is not implemented.");
    }

    protected InputStream getMapConfiguration(String name) {
        return null;
    }

    protected ResourceLocator getResourceLocator() {
        return null;
    }

    protected InputStream getViewConfiguration(String location) {
        return null;
    }

    public void initialize() throws Exception {
    }
}
