package org.nakedobjects.viewer.swing.test;

import org.nakedobjects.Exploration;
import org.nakedobjects.object.NakedClassList;
import org.nakedobjects.object.NakedObjectStore;
import org.nakedobjects.object.ObjectStoreException;
import org.nakedobjects.object.TransientObjectStore;
import org.nakedobjects.utility.ConfigurationException;

/**
 *
 * @author  beders
 */
public class TestExploration extends Exploration {

    /** Creates a new instance of TestExploration */
    public TestExploration() {
    }

    static String configFile;

    /** registers top-level classes */
    public void classSet(NakedClassList classes) {
        classes.addClass(ValueTest.class);
        classes.addClass(ActionTest.class);
        classes.addClass(PermissionTest.class);
        classes.addClass(AssocTest.class);
        classes.addClass(DisabledClass.class);
    }

    /**
   * template pattern (hook method). <br>name of configuration file to use for log4j parameters; (defaulted if none supplied).
   * <p>Implementation note: if null supplied, will default to
   * exploration.properties if available, or BasicConfigurator failing all else
   */
    protected String configurationFile() {
        return configFile;
    }

    /** template pattern (hook method). installs initial objects (if any) */
    protected void initObjects() {
    }

    /**
   * template pattern (hook method) returns object store to hold classes for duration of run.
   * a valid object store is required.
   */
    protected NakedObjectStore installObjectStore() throws ConfigurationException, ObjectStoreException {
        return new TransientObjectStore();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            configFile = args[0];
            System.out.println("Using config file: " + configFile);
        }
        new TestExploration();
    }
}
