package com.volantis.mcs.cli.asset;

import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.project.BuilderBatchOperation;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.TransactionLevel;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;
import java.io.File;
import java.util.List;

/**
 * A class which creates assets from a predefined structure of files on a
 * filesystem, as per R90/A898.
 * <p>
 * <i>The following is extracted directly from A898:</i>
 * <p>
 * The expected structure is:
 * <pre>
 *  /
 *   componentName1/
 *          .       convertible/
 *          .                   imagefile
 *          .       device/
 *          .              deviceName1/
 *          .                   .      imagefile
 *          .                   .
 *          .              deviceNameN/
 *          .                   .      imagefile
 *          .       generic/
 *          .               imagefile1
 *          .                   .
 *          .               imagefileN
 *   componentNameN/
 *          .       convertible/
 *          .                   imagefile
 *          .       device/
 *          .              deviceName1/
 *          .                   .      imagefile
 *          .                   .
 *          .              deviceNameN/
 *          .                   .      imagefile
 *          .       generic/
 *          .               imagefile1
 *          .                   .
 *          .               imagefileN *
 * </pre>
 * <p>
 * The directory structure contains a single directory for each component name.
 * Within each component name directory there are the 3 optional directories
 * 'convertible', 'device' and 'generic'. If a directory does not exist then no
 * images of the specific type are loaded. If all 3 directories are missing
 * then a component with no Assets is created.
 * <ul>
 * <li>If specified the 'convertible' may only contain a single image file.
 * <li>If specified the 'device' directory may contain multiple directories
 * (each specifying a valid MCS device name e.g. Master or PC). Each device
 * name directory may contain a single image file.
 * <li>If specified the 'generic' directory may contain multiple image files.
 * </ul>
 * <i>The following is paraphrased from A898:</i>
 * <p>
 * <strong>Import Behaviour</strong>
 * <p>
 * This class will create each component as specified by the component
 * directory, if the component already exists then only assets that do not
 * already exist will be imported (unless the -replace option is used).
 * <p>
 * <strong>Error Handling & Reporting</strong>
 * <p>
 * A log of the load process will be produced stating the success or failure
 * of importing each particular component and its assets. This will include the
 * image attributes.
 * <p>
 * If an image exists and -replace has not been specified then a warning
 * will be given stating that the particular image has not been loaded because
 * it already exists.
 * <p>
 * If an invalid device name has been specified for a device then an error
 * will be logged stating the failing component and invalid device name.
 * <p>
 * Not finding the srcdir, repository or devicerepository will be considered
 * fatal errors and result in termination of the load. Any other error with
 * particular components or assets will not terminate the load but will flag an
 * appropriate error stating the component name and as much information about
 * the failure as is useful.
 */
public class AssetLoader implements BuilderBatchOperation {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(AssetLoader.class);

    /**
     * Factory to create image assets from image files.
     */
    private final FileImageAssetFactory assetFactory = new FileImageAssetFactory();

    /**
     * The root directory of the file system structure to load.
     */
    private final File sourceDirectory;

    /**
     * The device repository to validate the device directories against.
     */
    private final DeviceRepository deviceRepository;

    /**
     * The default implicit project for the destination repository.
     */
    private final Project project;

    /**
     * The name of the asset group that is given to all assets created.
     */
    private String assetGroupName;

    /**
     * The prefix to prepend to any component names created. For example, this
     * may take the form of a directory.
     */
    private String componentNamePrefix;

    /**
     * Flag to indicate if we should replace an existing components found with
     * the names which we wish to create.
     */
    private boolean replace;

    /**
     * The width hint that is given to all generic image assets created.
     */
    private int widthHint = 100;

    /**
     * Initialise.
     *
     * @param sourceDirectory the root directory of the file system structure
     *      to import.
     * @param deviceRepository used to check that the device directories in
     * @param project
     */
    public AssetLoader(File sourceDirectory, DeviceRepository deviceRepository, Project project) {
        if (sourceDirectory == null) {
            throw new IllegalArgumentException("sourceDirectory cannot be " + "null");
        }
        if (deviceRepository == null) {
            throw new IllegalArgumentException("deviceRepository cannot be " + "null");
        }
        if (project == null) {
            throw new IllegalArgumentException("project cannot be null");
        }
        this.sourceDirectory = sourceDirectory;
        this.deviceRepository = deviceRepository;
        this.project = project;
    }

    /**
     * Set the name of the asset group to use for all the assets created.
     *
     * @param assetGroupName the name of the asset group.
     */
    public void setAssetGroupName(String assetGroupName) {
        String prefix = "/";
        String suffix = "." + FileExtension.ASSET_GROUP.getExtension();
        if (!(assetGroupName.startsWith(prefix) && assetGroupName.endsWith(suffix))) {
            throw new IllegalArgumentException("asset group name must start " + "with '" + prefix + "' and end with '" + suffix + "'");
        }
        this.assetGroupName = assetGroupName;
    }

    /**
     * Set the prefix to prepend to any component names created. For example,
     * this may take the form of a directory.
     *
     * @param componentNamePrefix prepended to any component names created.
     */
    public void setComponentNamePrefix(String componentNamePrefix) {
        this.componentNamePrefix = componentNamePrefix;
    }

    /**
     * Set the flag which to indicate if we should replace an existing
     * components found with the names which we wish to create.
     *
     * @param replace if true, replace existing components.
     */
    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    /**
     * Set the width hint that is given to all generic image assets created.
     *
     * @param widthHint the width hint, as a percentage.
     */
    public void setWidthHint(int widthHint) {
        if (widthHint < 0 || widthHint > 100) {
            throw new IllegalArgumentException("widthhint must be >= 0 and " + "<= 100");
        }
        this.widthHint = widthHint;
    }

    /**
     * Loads all the images in the filesystem structure as components and
     * assets in a repository, using the values already supplied.
     */
    public void load() {
        debugConfiguration();
        try {
            final PolicyBuilderManager manager = project.createPolicyBuilderManager();
            manager.performBatchOperation(this, TransactionLevel.NONE);
        } catch (Exception e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    public boolean perform(final PolicyBuilderManager manager) {
        final PolicyFactory factory = PolicyFactory.getDefaultInstance();
        FileEx sourceDirEx = new FileEx(sourceDirectory);
        sourceDirEx.forEachDirectory(new FileExOperation() {

            public void perform(File policyDir) {
                try {
                    logger.info("processing-policy", policyDir.getName());
                    String policyName = policyDir.getName() + "." + FileExtension.IMAGE_COMPONENT.getExtension();
                    if (componentNamePrefix != null) {
                        policyName = componentNamePrefix + policyName;
                    }
                    VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder) manager.getPolicyBuilder(policyName);
                    boolean update = (policyBuilder != null && replace);
                    if (policyBuilder != null) {
                        if (replace) {
                            logger.info("removing-existing", policyName);
                            manager.removePolicyBuilder(policyName);
                        } else {
                            logger.info("found-existing-ignoring-new", new Object[] { policyName });
                        }
                    }
                    if (policyBuilder == null) {
                        policyBuilder = factory.createVariablePolicyBuilder(PolicyType.IMAGE);
                        policyBuilder.setName(policyName);
                        update = false;
                    } else {
                        update = true;
                    }
                    createConvertibleImageAsset(policyBuilder, policyDir);
                    createDeviceImageAssets(policyBuilder, policyDir);
                    createGenericAsset(policyBuilder, policyDir);
                    if (update) {
                        manager.updatePolicyBuilder(policyBuilder);
                    } else {
                        manager.addPolicyBuilder(policyBuilder);
                    }
                } catch (Exception e) {
                    logger.error("processing-component", policyDir.getName(), e);
                }
            }
        });
        return false;
    }

    /**
     * Log the configuration information supplied to this class.
     */
    private void debugConfiguration() {
        if (logger.isDebugEnabled()) {
            logger.debug("Source directory: " + sourceDirectory);
            logger.debug("Device repository: " + deviceRepository);
            logger.debug("Asset group name: " + assetGroupName);
            logger.debug("Component name prefix: " + componentNamePrefix);
            logger.debug("Replace:" + replace);
            logger.debug("Width Hint: " + widthHint);
        }
    }

    /**
     * Create a convertible image asset for a component, if the requisite
     * component sub-directory exists.
     * <p>
     * This method checks for the "convertible" directory under the component
     * directory supplied, and creates a single convertible image asset using
     * the contained image file.
     *
     * @param policyBuilder the name of the component to add as asset to.
     * @param componentDir the directory that is the root of the component
     */
    private void createConvertibleImageAsset(final VariablePolicyBuilder policyBuilder, File componentDir) {
        File typeDir = new File(componentDir, "convertible");
        if (typeDir.exists() && typeDir.isDirectory()) {
            logger.info("processing-convertable-asset-component", policyBuilder);
            FileEx typeDirEx = new FileEx(typeDir);
            typeDirEx.forSingleFile(new FileExOperation() {

                public void perform(File assetFile) {
                    logger.info("convertable-asset-file-added-for-component", new Object[] { assetFile, policyBuilder });
                    try {
                        VariantBuilder variantBuilder = assetFactory.createConvertibleImageVariant(assetFile, assetGroupName);
                        addAssetSafely(policyBuilder, variantBuilder);
                    } catch (Exception e) {
                        logger.error("convertable-asset-file-added-for-component-error", new Object[] { assetFile, policyBuilder }, e);
                    }
                }
            });
        }
    }

    /**
     * Create a set of device assets for a component, if the requisite
     * component sub-directory exists.
     * <p>
     * This method checks for the "device" directory under the component
     * directory supplied, and iterates over all the contained sub-directories,
     * creating a single device image asset using the directory name as the
     * device name and the contained image file as the asset.
     *
     * @param policyBuilder the name of the component to add assets to.
     * @param componentDir the directory that is the root of the component
     */
    private void createDeviceImageAssets(final VariablePolicyBuilder policyBuilder, File componentDir) {
        File typeDir = new File(componentDir, "device");
        if (typeDir.exists() && typeDir.isDirectory()) {
            logger.info("device-asset-for-component-processed", policyBuilder);
            FileEx typeDirEx = new FileEx(typeDir);
            typeDirEx.forEachDirectory(new FileExOperation() {

                public void perform(File deviceDir) {
                    final String deviceName = deviceDir.getName();
                    try {
                        logger.info("device-directory-for-component-processed", new Object[] { deviceName, policyBuilder });
                        Device device = deviceRepository.getDevice(deviceName);
                        if (device != null) {
                            createDeviceImageAsset(policyBuilder, deviceDir);
                        } else {
                            logger.warn("device-directory-invalid-for-component", new Object[] { deviceName, policyBuilder });
                        }
                    } catch (Exception e) {
                        logger.error("processing-device-for-component-error", new Object[] { deviceName, policyBuilder }, e);
                    }
                }
            });
        }
    }

    /**
     * Create a single device image asset for a component.
     * <p>
     * This method creates a device image asset using a directory containing
     * an image file where the directory name is used as the device name.
     *
     * @param policyBuilder the name of the component to add assets to.
     * @param deviceDir a directory who's name is a valid device, and which
     */
    private void createDeviceImageAsset(final VariablePolicyBuilder policyBuilder, File deviceDir) {
        final String deviceName = deviceDir.getName();
        logger.info("processing-device-asset", new Object[] { deviceName, policyBuilder });
        FileEx deviceDirEx = new FileEx(deviceDir);
        deviceDirEx.forSingleFile(new FileExOperation() {

            public void perform(File assetFile) {
                logger.info("device-asset-file-added", new Object[] { assetFile, deviceName, policyBuilder });
                try {
                    VariantBuilder variantBuilder = assetFactory.createDeviceImageVariant(assetFile, assetGroupName, deviceName);
                    addAssetSafely(policyBuilder, variantBuilder);
                } catch (Exception e) {
                    logger.error("device-asset-file-adding-error", new Object[] { assetFile, deviceName, policyBuilder }, e);
                }
            }
        });
    }

    /**
     * Create a generic image asset for a component, if the requisite component
     * sub-directory exists.
     * <p>
     * This method checks for the "generic" directory under the component
     * directory supplied, and creates a generic image asset using each of the
     * contained image files.
     *
     * @param policyBuilder the name of the component to add as asset to.
     * @param componentDir the directory that is the root of the component
     */
    private void createGenericAsset(final VariablePolicyBuilder policyBuilder, File componentDir) {
        File typeDir = new File(componentDir, "generic");
        if (typeDir.exists() && typeDir.isDirectory()) {
            logger.info("generic-asset-processed", policyBuilder);
            FileEx typeDirEx = new FileEx(typeDir);
            typeDirEx.forEachFile(new FileExOperation() {

                public void perform(File assetFile) {
                    try {
                        logger.info("generic-asset-file-added-for-component", new Object[] { assetFile, policyBuilder });
                        VariantBuilder variantBuilder = assetFactory.createGenericImageVariant(assetFile, assetGroupName, widthHint);
                        addAssetSafely(policyBuilder, variantBuilder);
                    } catch (Exception e) {
                        logger.info("generic-asset-file-error-adding-for-" + "component", new Object[] { assetFile, policyBuilder });
                    }
                }
            });
        }
    }

    /**
     * Add a repository variantBuilder, respecting the setting of the replace flag.
     *
     * @param policyBuilder the policyBuilder into which the variantBuilder should be added.
     * @param variantBuilder the variantBuilder to add.
     */
    private void addAssetSafely(VariablePolicyBuilder policyBuilder, VariantBuilder variantBuilder) {
        SelectionBuilder selectionBuilder = variantBuilder.getSelectionBuilder();
        List variants = policyBuilder.getVariantBuilders();
        for (int i = 0; i < variants.size(); i++) {
            VariantBuilder existing = (VariantBuilder) variants.get(i);
            if (existing.getSelectionBuilder().equals(selectionBuilder)) {
                if (replace) {
                    logger.info("removing-existing", existing);
                    variants.remove(i);
                    break;
                } else {
                    logger.info("found-existing-ignoring-new", new Object[] { existing, variantBuilder });
                    return;
                }
            }
        }
        logger.info("creating-new", variantBuilder);
        policyBuilder.addVariantBuilder(variantBuilder);
    }

    /**
     * A simple class which is used to decorate File to add useful features,
     * namely internal iteration using the command pattern.
     * <p>
     * We must do this via decoration as File cannot be extended.
     */
    private static class FileEx {

        /**
         * The file we are decorating.
         */
        private final File file;

        /**
         * Initialise.
         *
         * @param file the file we are decorating.
         */
        FileEx(File file) {
            this.file = file;
        }

        /**
         * Internal iterator method to iterate over each of the children of
         * this file which is itself a file.
         *
         * @param operation called for each child which is a file.
         */
        void forEachFile(FileExOperation operation) {
            if (logger.isDebugEnabled()) {
                logger.debug("forEachFile: " + file);
            }
            File[] files = file.listFiles();
            if (files == null) {
                throw new IllegalStateException("Cannot iterate over " + "children of file");
            }
            for (int i = 0; i < files.length; i++) {
                final File file = files[i];
                if (file.isFile()) {
                    File child = files[i];
                    if (logger.isDebugEnabled()) {
                        logger.debug("file: " + child);
                    }
                    operation.perform(child);
                }
            }
        }

        /**
         * Internal iterator method to iterate over <strong>just the first
         * </strong> child of this file which is itself a file.
         *
         * @param operation called for the first child which is a file.
         */
        void forSingleFile(FileExOperation operation) {
            if (logger.isDebugEnabled()) {
                logger.debug("forSingleFile: " + file);
            }
            File[] files = file.listFiles();
            if (files == null) {
                throw new IllegalStateException("Cannot iterate over " + "children of file");
            }
            int fileCount = 0;
            for (int i = 0; i < files.length; i++) {
                File child = files[i];
                if (child.isFile()) {
                    if (fileCount == 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("single: " + child);
                        }
                        operation.perform(child);
                        fileCount++;
                    } else {
                        logger.warn("ignoring-extra-file", child);
                    }
                }
            }
        }

        /**
         * Internal iterator method to iterate over each of the children of
         * this file which is itself a directory.
         *
         * @param operation called for each child which is a directory.
         */
        void forEachDirectory(FileExOperation operation) {
            if (logger.isDebugEnabled()) {
                logger.debug("forEachDirectory: " + file);
            }
            File[] files = file.listFiles();
            if (files == null) {
                throw new IllegalStateException("Cannot iterate over " + "children of file");
            }
            for (int i = 0; i < files.length; i++) {
                File child = files[i];
                if (child.isDirectory()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("directory: " + child);
                    }
                    operation.perform(child);
                }
            }
        }
    }

    /**
     * Operation for use with the internal iterator methods of {@link FileEx}.
     */
    private interface FileExOperation {

        /**
         * Perform an operation on the file supplied.
         *
         * @param file the file to perform an operation on.
         */
        void perform(File file);
    }
}
