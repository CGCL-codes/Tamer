package com.amazon.carbonado.repo.sleepycat;

import java.io.File;
import com.amazon.carbonado.capability.Capability;

/**
 * Capability to provide direct access to the underlying BDB environment.
 *
 * @author Brian S O'Neill
 */
public interface EnvironmentCapability extends Capability {

    /**
     * Returns the BDB environment object, which must be cast to the expected
     * type, depending on the BDB product and version being used.
     */
    Object getEnvironment();

    BDBProduct getBDBProduct();

    /**
     * Returns the major, minor, and patch version numbers.
     */
    int[] getVersion();

    /**
     * Returns the home directory for the BDB environment.
     */
    File getHome();

    /**
     * Returns the directory where data files are stored, which is the same as
     * the home directory by default.
     */
    File getDataHome();
}
