package org.eclipse.bpel.common;

import java.util.Iterator;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Aug 3, 2007
 *
 */
@SuppressWarnings("nls")
public class BPELResourceSet extends ResourceSetImpl {

    /**
	 * Used to force loading using the right resource loaders.
	 */
    public static final String SLIGHTLY_HACKED_KEY = "slightly.hacked.resource.set";

    /**
	 * Load the resource from the resource set, assuming that it is the kind
	 * indicated by the last argument. The "kind" parameter is the extension 
	 * without the . of the resource.
	 * 
	 * This forces the right resource to be loaded even if the URI of the resource
	 * is "wrong".
	 * 
	 * @param uri the URI of the resource.
	 * @param loadOnDemand load on demand
	 * @param kind the resource kind. It has to be of the form "*.wsdl", or "*.xsd", or "*.bpel"
	 * @return the loaded resource. 
	 */
    @SuppressWarnings("nls")
    public Resource getResource(URI uri, boolean loadOnDemand, String kind) {
        Map<URI, Resource> map = getURIResourceMap();
        if (map != null) {
            Resource resource = map.get(uri);
            if (resource != null) {
                if (loadOnDemand && !resource.isLoaded()) {
                    demandLoadHelper(resource);
                }
                return resource;
            }
        }
        URIConverter theURIConverter = getURIConverter();
        URI normalizedURI = theURIConverter.normalize(uri);
        Iterator<Resource> it = getResources().iterator();
        while (it.hasNext()) {
            Resource resource = it.next();
            if (theURIConverter.normalize(resource.getURI()).equals(normalizedURI)) {
                if (loadOnDemand && !resource.isLoaded()) {
                    demandLoadHelper(resource);
                }
                if (map != null) {
                    map.put(uri, resource);
                }
                return resource;
            }
        }
        if (loadOnDemand) {
            Resource resource = demandCreateResource(uri, kind);
            if (resource == null) {
                throw new RuntimeException("Cannot create a resource for '" + uri + "'; a registered resource factory is needed");
            }
            demandLoadHelper(resource);
            if (map != null) {
                map.put(uri, resource);
            }
            return resource;
        }
        return null;
    }

    protected Resource demandCreateResource(URI uri, String kind) {
        return createResource(uri, kind);
    }

    /**
	 * Create the resource based on the kind.
	 * @param uri
	 * @param kind
	 * @return the created resource
	 */
    @SuppressWarnings("nls")
    public Resource createResource(URI uri, String kind) {
        if (kind == null) {
            return super.createResource(uri);
        }
        Resource resource = createResource(URI.createURI("*." + kind));
        resource.setURI(uri);
        return resource;
    }
}
