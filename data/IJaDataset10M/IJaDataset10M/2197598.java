package com.objectcarpentry.kaylee;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.osgi.framework.BundleContext;
import com.objectcarpentry.kaylee.gstreamer.KayleeGStreamerPlugin;
import com.objectcarpentry.kaylee.provider.KayleeItemProviderAdapterFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class KayleePlugin extends Plugin {

    public static final String PLUGIN_ID = "com.objectcarpentry.kaylee";

    private static KayleePlugin plugin;

    protected Library library;

    private EditingDomain editingDomain;

    /**
	 * The constructor
	 */
    public KayleePlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        loadLibrary();
    }

    public void stop(BundleContext context) throws Exception {
        getResourceSet().getResources().get(0).save(Collections.EMPTY_MAP);
        plugin = null;
        super.stop(context);
    }

    public URI getMetadataBaseURI() {
        String userHome = System.getProperty("user.home");
        URI home = URI.createFileURI(userHome);
        return home.appendSegment(".kaylee");
    }

    public ResourceSet getResourceSet() {
        return getEditingDomain().getResourceSet();
    }

    private void loadLibrary() {
        ResourceSet rs = getResourceSet();
        URI baseURI = getMetadataBaseURI();
        File f = new File(baseURI.toFileString());
        if (f.exists() || f.mkdir()) {
            Resource resource;
            URI libURI = baseURI.appendSegment("library.xml");
            try {
                if (!(new File(libURI.toFileString()).exists())) {
                    resource = rs.createResource(libURI);
                    library = KayleeFactory.eINSTANCE.createLibrary();
                    resource.getContents().add(library);
                    try {
                        resource.save(Collections.EMPTY_MAP);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    KayleePackage kl = KayleePackage.eINSTANCE;
                    resource = rs.getResource(libURI, true);
                    library = (Library) resource.getContents().get(0);
                }
                try {
                    resource.save(System.out, Collections.EMPTY_MAP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (WrappedException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public Library getLibrary() {
        return library;
    }

    public EditingDomain getEditingDomain() {
        if (editingDomain == null) {
            editingDomain = new AdapterFactoryEditingDomain(new KayleeItemProviderAdapterFactory(), new BasicCommandStack());
        }
        return editingDomain;
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static KayleePlugin getDefault() {
        return plugin;
    }

    public IPlayer getPlayer() {
        return KayleeGStreamerPlugin.getDefault().getPlayer();
    }
}
