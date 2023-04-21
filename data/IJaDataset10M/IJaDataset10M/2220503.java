package org.antdepo.common;

import java.util.jar.Manifest;

/**
 * Interface to the module's manifest file and common attributes
 */
public class CmdModuleArchiveManifest extends ArchiveManifest {

    public static final String ARCHIVE_TYPE = "Module";

    public static final String BUILD_COMMAND = "Build-Command";

    public static final String BUILD_DATE = "Build-Date";

    public static final String BUILT_BY = "Built-By";

    public static final String MODULE_NAME = "Module-Name";

    public static final String MODULE_VERSION = "Module-Version";

    public static final String ANTDEPO_FWK_VERSION = "Antdepo-Framework-Version";

    /**
     * Required attributes. These must be in the manifest
     */
    public static String[] REQUIRED_ATTRIBUTES = { ARCHIVE_TYPE, MODULE_NAME, MODULE_VERSION };

    public CmdModuleArchiveManifest(final Manifest manifest) {
        super(manifest);
    }

    /**
     * Factory method. calls base constructor
     *
     * @param manifest Manifest file
     * @return new instance
     */
    public static CmdModuleArchiveManifest create(final Manifest manifest) {
        return new CmdModuleArchiveManifest(manifest);
    }

    /**
     * Gets the module name attribute
     *
     * @return module name
     */
    public String getModuleName() {
        return getMainAttributes().getValue(MODULE_NAME);
    }

    /**
     * Gets the module version attribute
     *
     * @return version of module
     */
    public String getModuleVersion() {
        return getMainAttributes().getValue(MODULE_VERSION);
    }
}
