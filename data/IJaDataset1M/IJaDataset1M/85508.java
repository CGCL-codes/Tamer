package org.orion.as.kernel.spi.module;

import java.net.URI;
import java.util.jar.Manifest;

/**
 * The Interface ModuleDefinition.
 * 
 * @author <a href="liuguohui@gmail.com">Alex Liu</a>
 * @Created at: 4:03:07 PM May 14, 2009
 * @version
 */
public interface ModuleMetadata {

    /**
     * Gets the name.
     * 
     * @return the name
     */
    String getName();

    /**
     * Gets the public interfaces.
     * 
     * @return the public interfaces
     */
    String[] getPublicInterfaces();

    /**
     * Gets the dependencies.
     * 
     * @return the dependencies
     */
    ModuleDependency[] getDependencies();

    /**
     * Gets the locations.
     * 
     * @return the locations
     */
    URI[] getLocations();

    /**
     * Gets the version.
     * 
     * @return the version
     */
    String getVersion();

    /**
     * Gets the import policy class name.
     * 
     * @return the import policy class name
     */
    String getImportPolicyClassName();

    /**
     * Gets the lifecycle policy class name.
     * 
     * @return the lifecycle policy class name
     */
    String getLifecyclePolicyClassName();

    /**
     * Gets the manifest.
     * 
     * @return the manifest
     */
    Manifest getManifest();

    /**
     * Gets the definition.
     * 
     * @return the definition
     */
    ModuleMetadata getDefinition();
}
