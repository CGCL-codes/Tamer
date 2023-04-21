package org.openremote.controller.deployer;

import org.openremote.controller.exception.InitializationException;
import org.jdom.Namespace;

/**
 * Model builders are sequences of actions which construct the controller's object model (a.k.a.
 * strategy pattern). Different model builders may operate on differently structured XML document
 * instances. <p>
 *
 * This interface exposes the {@link org.openremote.controller.service.Deployer} side of the API
 * to construct the controller's object model. It is fairly opaque, one-way API to construct the
 * object model instances.  <p>
 *
 * The implementation of a model builder is expected not only to create the Java object instances
 * representing the object model, but also initialize, register and start all the created
 * resources as necessary. On returning from the {@link #buildModel()} method, the controller's
 * object model is expected to be running and fully functional.
 *
 * @see org.openremote.controller.deployer.ModelBuilder.SchemaVersion
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public interface ModelBuilder {

    /**
   * Indicates a controller schema version which the deployer attempts to map to its object model.
   */
    public enum SchemaVersion {

        /**
     * Version 2.0 : this is the schema for controller.xml file
     */
        VERSION_2_0("2.0"), /**
     * Version 3.0 : this is the schema for openremote.xml file
     */
        VERSION_3_0("3.0");

        /**
     * XML namespace definition for OpenRemote XML elements.
     */
        public static final Namespace OPENREMOTE_NAMESPACE = Namespace.getNamespace("or", "http://www.openremote.org");

        /**
     * Maps a string values to type-safe enum instances. <p>
     *
     * The string values should only be used in user interface (which may include user editable
     * configuration) to isolate users from actual enum names used in the codebase. The string
     * representations should not be used anywhere else in the codebase (always use the enums
     * instead).
     *
     * @param   value   string value to map to an enum instance
     *
     * @return  the enum instance corresponding to a given string value
     *
     * @throws  InitializationException if the given string could not be mapped to an enum
     */
        public static SchemaVersion toSchemaVersion(String value) throws InitializationException {
            if (value.equals(VERSION_2_0.toString())) {
                return VERSION_2_0;
            } else if (value.equals(VERSION_3_0.toString())) {
                return VERSION_3_0;
            } else {
                throw new InitializationException("Unrecognized schema version value ''{0}''", value);
            }
        }

        /**
     * Stores the string representation of this enum. This string value should only be used on
     * user interfaces to isolate the user from the enum definitions used within the codebase.
     */
        private String versionString;

        /**
     * Constructs an enum with a given string representation.
     *
     * @param version   version string for user interfaces
     */
        private SchemaVersion(String version) {
            this.versionString = version;
        }

        /**
     * Returns the version string intended for user interfaces. These string values should not
     * be used for other purposes in the codebase.
     *
     * @return  string representation of this enum instance for user interfaces
     */
        @Override
        public String toString() {
            return versionString;
        }
    }

    /**
   * Responsible for constructing the controller's object model. Implementation details
   * vary depending on the schema and source of defining artifacts.
   */
    void buildModel();

    /**
   * Model builder (schema) specific implementation to determine whether the controller
   * definition artifacts have changed in such a way that should result in redeploying the
   * object model.
   *
   * @see org.openremote.controller.service.Deployer.ControllerDefinitionWatch
   *
   * @return  true if the object model should be reloaded, false otherwise
   */
    boolean hasControllerDefinitionChanged();
}
