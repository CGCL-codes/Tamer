package com.germinus.merlin.manager.document;

import java.rmi.RemoteException;
import javax.portlet.RenderRequest;
import com.germinus.merlin.model.Document;
import com.germinus.liferay.util.ILiferayUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFolderServiceUtil;

public class MerlinDocumentLibraryManager extends LiferayDocumentLibraryManager implements IMerlinDLManager {

    private ILiferayUtil liferayUtil;

    public MerlinDocumentLibraryManager() {
    }

    public int addFolder(long plid, long parenFolderId, String name, String description) {
        return 0;
    }

    public int deleteDocument(long fileEntryId) {
        return 0;
    }

    public int deleteFolder(long folderId) {
        return 0;
    }

    public int deleteFolder(String name, long parenFolderId) {
        return 0;
    }

    public int initialize(RenderRequest request) {
        return 1;
    }

    public void revertTestMethod(RenderRequest request) {
    }

    public Document searchDocument(String name, long folderId) {
        return null;
    }

    public DLFileEntry searchFile(String name, long folderId) {
        return null;
    }

    public void testMethod(RenderRequest request) {
        initialize(request);
    }

    public int addDocument(Document document, int resourceContainerId) {
        return 0;
    }

    public int deleteDocument(Document document, int resourceContainerId) {
        return 0;
    }

    public int updateDocument(long documentId, Document document) {
        return 0;
    }

    /**
	 * @return the liferayUtil
	 */
    public ILiferayUtil getLiferayUtil() {
        return liferayUtil;
    }

    /**
	 * @param liferayUtil the liferayUtil to set
	 */
    public void setLiferayUtil(ILiferayUtil liferayUtil) {
        this.liferayUtil = liferayUtil;
    }
}
