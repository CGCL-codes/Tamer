package org.dctmvfs.vfs.provider.dctm.client.content;

import java.io.File;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;

public interface ContentCreator {

    /**
	 * Creates a new folder
	 * @param session
	 * @param name
	 * @param location
	 * @return the folder object
	 * @throws DctmContentException
	 */
    public abstract IDfSysObject createFolder(IDfSession session, String name, String location) throws DctmContentException;

    /**
	 * Creates a new content object and sets the file content
	 * @param session
	 * @param name
	 * @param location
	 * @param tmpFile
	 * @return the new content object
	 * @throws DctmContentException
	 */
    public abstract IDfSysObject createContent(IDfSession session, String name, String location, File tmpFile) throws DctmContentException;
}
