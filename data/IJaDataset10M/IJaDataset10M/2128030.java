package org.torweg.pulse.vfs.filebrowser;

import java.net.URI;
import org.jdom.Element;
import org.torweg.pulse.bundle.JDOMable;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.util.INamed;

/**
 * @author Thomas Weber
 * @version $Revision: 1430 $
 */
public interface FileDescriptor extends INamed, JDOMable, Comparable<FileDescriptor> {

    /**
	 * returns the {@code URI} identifying the file.
	 * 
	 * @return the {@code URI} identifying the file
	 */
    URI getURI();

    /**
	 * sets the current {@code ServiceRequest} for the descriptor.
	 * 
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
    void setServiceRequest(ServiceRequest request);

    /**
	 * deserializes the {@code FileDescriptor} to JDOM.
	 * 
	 * @param request
	 *            the current requests
	 * @return a JDOM representation of the {@code FileDescriptor}
	 */
    Element deserializeToJDOM(ServiceRequest request);
}
