package org.nakedobjects.object;

import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class NakedObjectTestCase extends TestCase {

    public NakedObjectTestCase(String name) {
        super(name);
    }

    public NakedObjectTestCase() {
    }

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
    }
}
