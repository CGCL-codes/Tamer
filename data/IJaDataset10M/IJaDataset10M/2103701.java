package org.apache.commons.vfs;

/**
 * Create a class which is able to determine the content-info for the given content
 * 
 * @author <a href="mailto:imario@apache.org">Mario Ivankovits</a>
 * @version $Revision: 480428 $ $Date: 2006-11-28 22:15:24 -0800 (Tue, 28 Nov 2006) $
 */
public interface FileContentInfoFactory {

    FileContentInfo create(FileContent fileContent) throws FileSystemException;
}
