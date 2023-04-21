package com.mindquarry.jcr.xml.source.helper;

import java.io.IOException;
import java.io.InputStream;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import org.apache.excalibur.source.SourceNotFoundException;
import com.mindquarry.jcr.xml.source.JCRConstants;

/**
 * Source helper for nodes that represents a file (nt:file) with binary
 * content.
 * 
 * @author <a href="mailto:lars(dot)trieloff(at)mindquarry(dot)com">Lars
 *         Trieloff</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileSourceHelper {

    /**
     * Delegate for getInputStream.
     */
    public static InputStream getInputStream(Node content) throws IOException, SourceNotFoundException {
        try {
            return content.getProperty(JCRConstants.JCR_DATA).getStream();
        } catch (PathNotFoundException e) {
            throw new SourceNotFoundException("No valid jcr:data property", e);
        } catch (RepositoryException e) {
            throw new IOException("Unable to read from repository: " + e.getLocalizedMessage());
        }
    }
}
