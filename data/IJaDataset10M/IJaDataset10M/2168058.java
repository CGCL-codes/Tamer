package ro.codemart.tutorial.v3.cdmanager;

import ro.codemart.tutorial.CDManager;
import ro.codemart.tutorial.v2.operation.registry.RegistryOperation;
import ro.codemart.tutorial.v2.operation.registry.AddRegistryOperation;
import ro.codemart.tutorial.v2.operation.registry.DeleteRegistryOperation;
import ro.codemart.tutorial.v2.cdmanager.CDManager_V2;
import ro.codemart.tutorial.v3.cdmanager.operation.CreateTablesDBAction;
import ro.codemart.tutorial.v3.cdmanager.operation.CleanDBAction;
import ro.codemart.tutorial.v3.cdmanager.operation.DataMigration_V2_V3;
import java.util.List;
import java.io.File;
import ro.codemart.installer.core.InstallerContext;
import ro.codemart.installer.core.ProductVersion;
import ro.codemart.installer.core.utils.file.DeleteFolderOperation;
import ro.codemart.installer.core.operation.OperationBatch;

/**
 * Manager for the third version of application.
 * Creates the list of audio CDs, the plays and defines the install, update and uninstall actions.
 */
public class CDManager_V3 extends CDManager {

    /**
     * Creates the third version for this manager
     *
     * @param version the string representing the version for this manager. Would be something like "V3", "Version3", "3.0" etc.
     */
    public CDManager_V3(String version) {
        super(version);
    }

    public ProductVersion upgradeProductVersionInstance(ProductVersion productVersion) {
        return this;
    }

    /**
     * Creates the install actions for this manager application.
     *
     * @param currentProduct the previously installed product. Can be {@code null}
     * @param context        installer context
     * @return the list of actions for installing this product
     */
    public OperationBatch buildUninstallActions(ProductVersion currentProduct, InstallerContext context) {
        OperationBatch uninstallBatch = new OperationBatch();
        if (context.getUserPropertyAsBoolean("delete.db.tables")) {
            uninstallBatch.addOperation(new CleanDBAction("CleanDBOp", currentProduct, context));
        }
        uninstallBatch.addOperation(new DeleteFolderOperation("DeleteFolderOp", getInstallDir(context)));
        uninstallBatch.addOperation(new DeleteRegistryOperation(getVersion()));
        return uninstallBatch;
    }

    /**
     * Creates the actions necessary for updating this product
     *
     * @param currentProduct  the previously installed product. Never {@code null}
     * @param upgradedProduct the product after the update (created by {@link #upgradeProductVersionInstance(ProductVersion)})
     * @param context         installer context
     * @return the list of actions for updating this product
     */
    public OperationBatch buildIncrementalUpdateActions(ProductVersion currentProduct, ProductVersion upgradedProduct, InstallerContext context) {
        OperationBatch updateBatch = new OperationBatch();
        CDManager_V2 cdManager_v2 = (CDManager_V2) currentProduct;
        CDManager_V3 cdManager_v3 = (CDManager_V3) upgradedProduct;
        updateBatch.addOperation(cdManager_v3.buildInstallActions(cdManager_v2, context));
        updateBatch.addOperation(new DataMigration_V2_V3(cdManager_v2, cdManager_v3, context));
        String installDir = context.getUserProperty("installDir");
        updateBatch.addOperation(new AddRegistryOperation(getVersion(), installDir));
        updateBatch.addOperation(cdManager_v2.buildUninstallActions(cdManager_v2, context));
        return updateBatch;
    }

    /**
     * Creates the actions necessary for uninstalling this product
     *
     * @param currentProduct the previously installed product. Cannot {@code null}
     * @param context        installer context
     * @return the list of actions for uninstalling this product
     */
    public OperationBatch buildInstallActions(ProductVersion currentProduct, InstallerContext context) {
        OperationBatch installBatch = new OperationBatch();
        installBatch.addOperation(new CreateTablesDBAction("CDBA", context, currentProduct));
        String installDir = context.getUserProperty("installDir");
        installBatch.addOperation(new AddRegistryOperation(getVersion(), installDir));
        return installBatch;
    }

    /**
     * This method is responsible in checking if this product version is already installed.
     * For this version, check that registry key for this version has a value
     *
     * @param context the installer context to get additional user input (if needed)
     * @return {@code true} if the product is installed.
     */
    public boolean isInstalled(InstallerContext context) {
        return RegistryOperation.isInstalled(getVersion());
    }

    public File getInstallDir(InstallerContext context) {
        if (RegistryOperation.isInstalled(getVersion())) {
            return new File(RegistryOperation.getInstallDir(getVersion()));
        }
        return null;
    }

    /**
     * Creates the list of audio CDs.
     *
     * @return the list of audio CDs for this version
     */
    protected List createAudioCDs() {
        return null;
    }
}
