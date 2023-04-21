package ro.codemart.tutorial.v2.operation.registry;

import ro.codemart.installer.core.InstallerException;
import ro.codemart.installer.core.I18nKey;

/**
 * Adds a key with a value to the registry.
 * The key is the version of the product and the value is the installation directory.
 */
public class AddRegistryOperation extends RegistryOperation {

    public static final String ADD_REGISTRY_MESSAGE = "AddRegistryMessage";

    public static final String ADD_REGISTRY_DEFAULT_MESSAGE = "Add the key {0} into the registry";

    private String installDir;

    public AddRegistryOperation(String version, String installDir) {
        super(version);
        this.installDir = installDir;
    }

    /**
     * Executes this operation.
     *
     * @throws InstallerException if any error occurs while performing the operation
     */
    public void execute() throws InstallerException {
        String regKey = INSTALL_DIR_KEY + getVersion();
        setDescription(new I18nKey(ADD_REGISTRY_MESSAGE, ADD_REGISTRY_DEFAULT_MESSAGE), getLanguage(), regKey);
        prefs.put(regKey, installDir);
    }
}
