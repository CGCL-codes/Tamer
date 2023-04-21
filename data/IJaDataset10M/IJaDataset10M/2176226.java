package org.xtext.example.linker;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;

/**
 * @author Strocco Fabio
 * 
 */
public class SwrtjLinkingResource extends LazyLinkingResource {

    /**
	 * The uri of the implicit resource containing the implicit object 
	 */
    public static URI implicitSystemUri = URI.createURI("http:///System.swrtj");

    @Override
    protected void doLinking() {
        ensureSystemIsPresent();
        super.doLinking();
    }

    /**
	 * Ensures that in the resource set there is the implicit Object resource
	 */
    private void ensureSystemIsPresent() {
        ResourceSet resourceSet = getResourceSet();
        Resource res = resourceSet.getResource(implicitSystemUri, true);
        if (res != null) {
            return;
        }
    }

    @Override
    public void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
        if (getURI().equals(implicitSystemUri)) return;
        super.doSave(outputStream, options);
    }
}
