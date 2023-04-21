package net.sourceforge.emfutils.ecore2argo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public final class EcoreXmiLoader {

    private EcoreXmiLoader() {
    }

    /**
	 * @todo Validate content
	 * @param uri
	 * @return
	 */
    public static EPackage load(URI uri) {
        EcorePackageImpl.init();
        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(uri, true);
        return (EPackage) resource.getContents().get(0);
    }
}
