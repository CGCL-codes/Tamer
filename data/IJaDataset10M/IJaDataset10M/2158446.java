package powermock.examples.mockpolicy;

import powermock.examples.mockpolicy.nontest.Dependency;
import powermock.examples.mockpolicy.nontest.domain.DataObject;

/**
 * Very simple example of a class that uses a dependency.
 */
public class DependencyUser {

    public DataObject getDependencyData() {
        return new Dependency("some data").getData();
    }
}
