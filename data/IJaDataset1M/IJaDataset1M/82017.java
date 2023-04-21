package org.scribble.eclipse.model.admin;

import java.util.logging.Logger;
import org.scribble.model.ModelReference;
import org.scribble.model.admin.ModelResource;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;

/**
 * This class implements the ModelResource interface for
 * OSGI based resources.
 */
public class OSGIModelResource implements ModelResource {

    /**
	 * This constructor is initialized with the Eclipse
	 * file resource.
	 * 
	 * @param file The file
	 */
    public OSGIModelResource(IFile file) {
        m_reference = new ModelReference(file.getFileExtension());
        OSGIModelRepository.setFile(m_reference, file);
    }

    /**
	 * This constructor is initialised with the reference
	 * that it represents.
	 * 
	 * @param ref The reference
	 */
    public OSGIModelResource(ModelReference ref) {
        m_reference = ref;
    }

    /**
	 * This method returns the URI of the model resource.
	 * 
	 * @return The URI
	 */
    public java.net.URI getURI() {
        java.net.URI ret = null;
        IResource thisRes = OSGIModelRepository.getFile(m_reference);
        if (thisRes != null) {
            ret = thisRes.getLocationURI();
        }
        return (ret);
    }

    /**
	 * This method returns the contents associated with
	 * the model resource.
	 * 
	 * @return The contents
	 * @exception IOException Failed to obtain content
	 */
    public java.io.InputStream getContents() throws java.io.IOException {
        java.io.InputStream ret = null;
        IFile thisRes = OSGIModelRepository.getFile(m_reference);
        try {
            ret = ((IFile) thisRes).getContents();
        } catch (Exception e) {
            throw new java.io.IOException("Failed to obtain contents: " + e);
        }
        return (ret);
    }

    /**
	 * This method returns the model reference.
	 * 
	 * @return The model reference
	 */
    public ModelReference getReference() {
        return (m_reference);
    }

    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof ModelResource) {
            ModelReference otherRef = ((ModelResource) obj).getReference();
            if (otherRef != null) {
                IResource otherRes = OSGIModelRepository.getFile(otherRef);
                IResource thisRes = OSGIModelRepository.getFile(m_reference);
                logger.finest("EQUAL: " + otherRef + " against " + m_reference);
                logger.finest("EQUAL: " + otherRes + " against " + thisRes);
                if (otherRes != null && thisRes != null) {
                    ret = otherRes.equals(thisRes);
                    logger.finest("EQUAL RES: " + ret);
                } else {
                    ret = otherRef.equals(m_reference);
                    logger.finest("EQUAL REF: " + ret);
                }
            }
        } else if (obj instanceof ModelReference) {
            ModelReference otherRef = (ModelReference) obj;
            ret = otherRef.equals(m_reference);
            logger.info(">>>> CHECK EQUAL: " + otherRef + " against " + m_reference + " ret=" + ret);
        }
        return (ret);
    }

    public String toString() {
        IResource thisRes = OSGIModelRepository.getFile(m_reference);
        return ("[" + m_reference + " resource=" + thisRes + "]");
    }

    public int hashCode() {
        return (m_reference.hashCode());
    }

    private static Logger logger = Logger.getLogger("org.scribble.osgi.model");

    private ModelReference m_reference = null;
}
