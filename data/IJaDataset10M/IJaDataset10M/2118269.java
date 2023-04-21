package org.remus.infomngmnt.connector.twitter.infotype;

import java.io.InputStream;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.remus.core.extension.AbstractInformationRepresentation;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class TwitterInformationRepresentation extends AbstractInformationRepresentation {

    /**
	 * 
	 */
    public TwitterInformationRepresentation() {
    }

    @Override
    public InputStream handleHtmlGeneration(final IProgressMonitor monitor) throws CoreException {
        return null;
    }
}
