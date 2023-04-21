package org.docflower.resources;

import java.io.*;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import org.docflower.model.*;
import org.docflower.rcp.Activator;
import org.docflower.resources.storages.*;
import org.docflower.util.*;
import org.docflower.xml.ISchemaInputStreamFactory;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.*;

/**
 * Global resource hub. Work with all resources through this class. It is
 * singelton.
 * 
 */
public class ResourceManager implements ISchemaInputStreamFactory {

    private static final String RESOURCE_LOC_PREFIX = "resources/";

    private static ResourceManager singleton = new ResourceManager();

    private Transformer nodeListGenerator;

    private Map<String, BundleInfo> resourceStorages = new HashMap<String, BundleInfo>();

    private Map<QName, EntityAttributes> entitiesRegistry = new HashMap<QName, EntityAttributes>();

    private Map<String, IConfigurationElement> storageEntries = new HashMap<String, IConfigurationElement>();

    private EntityAttributesTreeItem entitiesRegistryTree = new EntityAttributesCategory();

    private ResourcePool<Image, String> imagePool = new ResourcePool<Image, String>(new ImagePoolAccess());

    private ResourcePool<Font, FontDescriptor> fontPool = new ResourcePool<Font, FontDescriptor>(new FontPoolAccess());

    private ResourcePool<Color, String> colorPool = new ResourcePool<Color, String>(new ColorPoolAccess());

    /**
	 * Restrict access to the instances of this class. It is singleton and must
	 * be accessed throght the getInstance() static method.
	 */
    private ResourceManager() {
        super();
        initResourceStorages();
        initEntitiesRegistry();
        initStorageEntry();
    }

    public static ResourceManager getInstance() {
        return singleton;
    }

    public static void tryDispose() {
        if (singleton != null) {
            singleton.dispose();
        }
    }

    public void dispose() {
        getImagePool().dispose();
        getFontPool().dispose();
        getColorPool().dispose();
    }

    @Override
    public InputStream getSchemaStream(String schemaLocation, String locationPrefix) throws IOException {
        return getResourceInputStream(schemaLocation, locationPrefix);
    }

    public InputStream getResourceInputStream(String resourceLocationUri) throws IOException {
        return getResourceInputStream(resourceLocationUri, "");
    }

    public InputStream getResourceInputStream(String resourceLocationUri, String pathPrefix) throws IOException {
        StorageEntry se = StorageFactory.getSingleton().getStorageEntry(pathPrefix, resourceLocationUri);
        return se.openStream();
    }

    public Transformer getNodeListGenerator() {
        if (nodeListGenerator == null) {
            initNodeListGenerator();
        }
        return nodeListGenerator;
    }

    private void initNodeListGenerator() {
        String nodeListGeneratorFileName = "this/common/nodeListGenerator.xsl";
        try {
            InputStream nodeListGeneratorInputStream = getResourceInputStream(nodeListGeneratorFileName);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                nodeListGenerator = transformerFactory.newTransformer((new StreamSource(nodeListGeneratorInputStream)));
            } finally {
                nodeListGeneratorInputStream.close();
            }
        } catch (Exception e) {
            throw new LowLevelException(Messages.ResourceManager_UnableCreateNodeListGenerator, e);
        }
    }

    public ImageDescriptor createImageDescriptor(String imageLocationUri) {
        StorageEntry se = StorageFactory.getSingleton().getStorageEntry("", imageLocationUri);
        try {
            ImageData imageData = new ImageData(se.openStream());
            return ImageDescriptor.createFromImageData(imageData);
        } catch (IOException e) {
            throw new LowLevelException(Messages.ResourceManager_UnableLoadImage + imageLocationUri);
        }
    }

    private void initResourceStorages() {
        getResourceStorages().put("this", new BundleInfo(Activator.getDefault().getBundle(), RESOURCE_LOC_PREFIX));
        WorkbenchUtils.handleExtensionPoint("resources", new IExtensionPointHandler() {

            @Override
            public void handle(IConfigurationElement configElement) {
                if (configElement.getName().equals("resourceInfo")) {
                    getResourceStorages().put(configElement.getContributor().getName(), new BundleInfo(Platform.getBundle(configElement.getContributor().getName()), StringUtils.ensureTrailingSlash(configElement.getAttribute("resourcePath"))));
                }
            }
        });
    }

    private void initEntitiesRegistry() {
        WorkbenchUtils.handleExtensionPoint("entitiesregistry", new IExtensionPointHandler() {

            @Override
            public void handle(IConfigurationElement configElement) {
                if (configElement.getName().equals("entity")) {
                    EntityAttributes ea = createEntityAttributes(configElement);
                    getEntitiesRegistry().put(new QName(configElement.getAttribute("namespace"), configElement.getAttribute("name")), ea);
                    addChildToNode(getEntitiesRegistryTree(), ea);
                } else {
                    EntityAttributesCategory eac = createEntityAttributesCategory(configElement);
                    addChildToNode(getEntitiesRegistryTree(), eac);
                    IConfigurationElement[] children = configElement.getChildren();
                    for (IConfigurationElement ce : children) {
                        handleTreeItem(ce, eac);
                    }
                }
            }
        });
    }

    private void initStorageEntry() {
        WorkbenchUtils.handleExtensionPoint("storageentries", new IExtensionPointHandler() {

            @Override
            public void handle(IConfigurationElement configElement) {
                if (configElement.getName().equals("storageEntry")) {
                    getStorageEntries().put(configElement.getAttribute("protocol"), configElement);
                }
            }
        });
    }

    protected EntityAttributesCategory createEntityAttributesCategory(IConfigurationElement configElement) {
        EntityAttributesCategory result = new EntityAttributesCategory();
        result.setId(configElement.getAttribute("entitytreeitemid"));
        result.setDescription(configElement.getAttribute("description"));
        result.setLabel(configElement.getAttribute("label"));
        return result;
    }

    protected void handleTreeItem(IConfigurationElement configElement, EntityAttributesTreeItem curTreeItem) {
        if (configElement.getName().equals("entity")) {
            EntityAttributes ea = createEntityAttributes(configElement);
            getEntitiesRegistry().put(new QName(configElement.getAttribute("namespace"), configElement.getAttribute("name")), ea);
            addChildToNode(curTreeItem, ea);
        } else {
            EntityAttributesCategory eac = createEntityAttributesCategory(configElement);
            addChildToNode(curTreeItem, eac);
            IConfigurationElement[] children = configElement.getChildren();
            if (children != null) {
                for (IConfigurationElement ce : children) {
                    handleTreeItem(ce, eac);
                }
            }
        }
    }

    protected void addChildToNode(EntityAttributesTreeItem curNode, EntityAttributesTreeItem newNode) {
        ArrayList<EntityAttributesTreeItem> al = new ArrayList<EntityAttributesTreeItem>();
        EntityAttributesTreeItem eac = (EntityAttributesTreeItem) curNode;
        if (eac.hasChildren()) {
            EntityAttributesTreeItem[] children = eac.getChildren();
            for (EntityAttributesTreeItem ti : children) {
                al.add(ti);
            }
        }
        al.add(newNode);
        newNode.setParent(curNode);
        eac.setChildren((EntityAttributesTreeItem[]) al.toArray(new EntityAttributesTreeItem[al.size()]));
    }

    protected EntityAttributes createEntityAttributes(IConfigurationElement configElement) {
        EntityAttributes result = new EntityAttributes();
        result.setDescriptor(configElement.getAttribute("descriptorFile"));
        result.setName(configElement.getAttribute("name"));
        result.setNamespace(configElement.getAttribute("namespace"));
        result.setActionsFile(configElement.getAttribute("actionsFile"));
        result.setCreateActionName(configElement.getAttribute("createActionName"));
        result.setDescription(configElement.getAttribute("description"));
        result.setImageLocation(configElement.getAttribute("imageLocation"));
        result.setLabel(configElement.getAttribute("label"));
        result.setTitleXPath(configElement.getAttribute("titleXPath"));
        result.setTooltipXPath(configElement.getAttribute("tooltipXPath"));
        result.setSaveActionName(configElement.getAttribute("saveActionName"));
        result.setSchemaFileName(configElement.getAttribute("schemaFileName"));
        result.setSchemaType(configElement.getAttribute("schemaType"));
        result.setStructureExtractor(configElement.getAttribute("structureExtractor"));
        result.setBusinessRules(configElement.getAttribute("businessRules"));
        result.setInitialTitle(configElement.getAttribute("initialTitle"));
        getNestedData(result, configElement);
        return result;
    }

    private void getNestedData(EntityAttributes result, IConfigurationElement configElement) {
        IConfigurationElement[] children = configElement.getChildren();
        for (IConfigurationElement userDefData : children) {
            if (userDefData.getName().equals("userdefineddataentry")) {
                result.getUserDefinedData().put(userDefData.getAttribute("entryname"), userDefData.getAttribute("entryvalue"));
            } else if (userDefData.getName().equals("messages")) {
                getMessageBundles(result, userDefData);
            }
        }
    }

    private void getMessageBundles(EntityAttributes result, IConfigurationElement configElement) {
        ArrayList<String> list = new ArrayList<String>();
        String mainMessageBundle = configElement.getAttribute("messageBundle");
        if ((mainMessageBundle != null) && mainMessageBundle.length() > 0) {
            list.add(mainMessageBundle);
        }
        IConfigurationElement[] children = configElement.getChildren();
        for (IConfigurationElement messageBundle : children) {
            list.add(messageBundle.getAttribute("messageBundle"));
        }
        if (list.size() > 0) {
            String[] messageBundlesArray = new String[list.size()];
            result.setMessages(list.toArray(messageBundlesArray));
        }
    }

    public Map<String, BundleInfo> getResourceStorages() {
        return resourceStorages;
    }

    public Map<QName, EntityAttributes> getEntitiesRegistry() {
        return entitiesRegistry;
    }

    public Map<String, IConfigurationElement> getStorageEntries() {
        return storageEntries;
    }

    public ResourcePool<Image, String> getImagePool() {
        return imagePool;
    }

    public ResourcePool<Font, FontDescriptor> getFontPool() {
        return fontPool;
    }

    public ResourcePool<Color, String> getColorPool() {
        return colorPool;
    }

    public EntityAttributesTreeItem getEntitiesRegistryTree() {
        return entitiesRegistryTree;
    }
}
