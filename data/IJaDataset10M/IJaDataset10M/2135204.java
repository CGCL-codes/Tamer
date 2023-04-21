package org.pachyderm.foundation;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import org.pachyderm.apollo.core.CXDefaults;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotificationCenter;
import er.extensions.foundation.ERXStringUtilities;

public class PXComponentRegistry {

    private static Logger LOG = Logger.getLogger(PXComponentRegistry.class.getName());

    private static PXComponentRegistry _sharedRegistry;

    private NSMutableDictionary<String, Object> _descriptionsByIdentifier;

    static void awakeSharedRegistry() {
        _sharedRegistry = new PXComponentRegistry();
        registerComponentsInResources();
        registerComponentsInSupportDirectory();
    }

    private static void registerComponentsInResources() {
        NSBundle thisBundle = NSBundle.bundleForClass(PXComponentRegistry.class);
        LOG.info("get PXComponentRegistry from Resources in: " + thisBundle.name());
        NSArray<String> paths = thisBundle.resourcePathsForResources("xml", null);
        for (String rezPath : paths) {
            if (rezPath.endsWith(".component/Definition.xml")) {
                InputStream is = thisBundle.inputStreamForResourcePath(rezPath);
                try {
                    PXComponentDescription description = PXComponentDescription.descriptionWithInputStream(is);
                    _sharedRegistry.registerComponentDescription(description);
                } catch (Exception x) {
                    LOG.error("XXXXX", x);
                }
            }
        }
    }

    private static void registerComponentsInSupportDirectory() {
        String componentsPath = CXDefaults.sharedDefaults().getString("ComponentDirectory");
        if (!componentsPath.startsWith(".")) {
            LOG.info("get PXComponentRegistry from: " + componentsPath);
            registerComponentsInDirectory(new File(componentsPath));
        }
    }

    private static void registerComponentsInDirectory(File dir) {
        NSArray<File> componentFiles = PXFoundation.bundleFilesWithExtensionAtFile("component", dir, true);
        for (File componentFile : componentFiles) {
            try {
                PXComponentDescription description = PXComponentDescription.descriptionWithBundleURL(componentFile.toURI().toURL());
                _sharedRegistry.registerComponentDescription(description);
            } catch (MalformedURLException murle) {
                murle.printStackTrace();
            }
        }
        LOG.trace("registerComponentsInDirectory (done) --> " + _sharedRegistry.prettyString());
    }

    private PXComponentRegistry() {
        super();
        _descriptionsByIdentifier = new NSMutableDictionary<String, Object>(128);
    }

    public static PXComponentRegistry sharedRegistry() {
        return _sharedRegistry;
    }

    public void registerComponentDescription(PXComponentDescription description) {
        if (description != null) {
            if (componentDescriptionForIdentifier(description.identifier()) != null) {
                LOG.warn("Note: Registering component description with identifier '" + description.identifier() + "' will replace an existing description with the same identifier.");
            }
            if (description.identifier().length() == 0) {
                throw new IllegalArgumentException("Attempting to register a component description with an empty identifier.");
            }
            _descriptionsByIdentifier.setObjectForKey(description, description.identifier());
        }
    }

    public void unregisterComponentDescription(PXComponentDescription description) {
        if (description != null) {
            _descriptionsByIdentifier.removeObjectForKey(description.identifier());
        }
    }

    public NSArray<Object> registeredComponentDescriptions() {
        return _descriptionsByIdentifier.allValues();
    }

    public NSArray<String> registeredComponentIdentifiers() {
        return _descriptionsByIdentifier.allKeys();
    }

    public PXComponentDescription componentDescriptionForIdentifier(String identifier) {
        PXComponentDescription desc = (PXComponentDescription) _descriptionsByIdentifier.objectForKey(identifier);
        if (desc == null) {
            NSNotificationCenter.defaultCenter().postNotification("ComponentDescriptionNeededForIdentifier", identifier);
            desc = (PXComponentDescription) _descriptionsByIdentifier.objectForKey(identifier);
        }
        return desc;
    }

    public String prettyString() {
        String nl = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer("PXComponentRegistry [shared] {" + nl);
        for (String key : _descriptionsByIdentifier.allKeys()) {
            sb.append(nl + ERXStringUtilities.leftPad(key, ' ', 48) + " = " + ((PXComponentXMLDesc) _descriptionsByIdentifier.valueForKey(key)).prettyString());
        }
        return sb.append("}").toString();
    }
}
