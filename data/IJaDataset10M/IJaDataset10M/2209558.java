package de.uhilger.netzpult.server;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.uhilger.netzpult.client.OwnerService;
import de.uhilger.netzpult.server.db.DataStorage;
import de.uhilger.netzpult.shared.Document;
import de.uhilger.netzpult.shared.Folder;
import de.uhilger.netzpult.shared.GroupShare;
import de.uhilger.netzpult.shared.UserGroup;
import de.uhilger.netzpult.shared.UserShare;

/**
 * 
 * @author Copyright (c) Ulrich Hilger, http://uhilger.de
 * @author Published under the terms and conditions of
 * the <a href="http://www.gnu.org/licenses/" target="_blank">GNU General Public License</a>
 */
public class OwnerServiceImpl extends AbstractWebfilesServlet implements OwnerService {

    private static final long serialVersionUID = -6957716160693011354L;

    private String userName;

    @Override
    public Folder createFolder(Folder folder) throws Exception {
        return ds.createFolder(folder, userName);
    }

    @Override
    public List<Folder> getRootFolders(String owner) throws Exception {
        return ds.getRootFolders(userName);
    }

    @Override
    public List<Folder> getChildren(String owner, Folder folder) throws Exception {
        return ds.getChildren(folder, userName);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest(".");
        userName = getUserName(request);
        super.service(request, response);
    }

    @Override
    public Folder updateFolder(Folder folder) throws Exception {
        return ds.updateFolder(folder);
    }

    @Override
    public void addToFolder(Folder folder, int documentId) throws Exception {
        ds.addToFolder(folder, documentId);
    }

    @Override
    public void removeFromFolder(Folder folder, int documentId) throws Exception {
        ds.removeFromFolder(folder, documentId);
    }

    private Document newVersion(Document document) throws Exception {
        try {
            String docOwner = ds.getDocumentOwner(document.getId());
            if (docOwner.equals(userName)) {
                return ds.createNewVersion(document);
            } else {
                throw new Exception("User does not own document");
            }
        } catch (Exception ex) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Document createDocument(Document document) throws Exception {
        if (document.getId() == Document.NEW_DOCUMENT_ID) {
            document.setVersion(Document.DRAFT_VERSION_ID);
            return ds.createDocument(document, userName);
        } else {
            return newVersion(document);
        }
    }

    @Override
    public Document saveDraft(Document document) throws Exception {
        if (document.getId() == Document.NEW_DOCUMENT_ID) {
            document.setVersion(Document.DRAFT_VERSION_ID);
            return ds.createDocument(document, userName);
        } else {
            return ds.saveDraft(document);
        }
    }

    @Override
    public List<Document> getDocuments(String owner, Folder folder) throws Exception {
        return ds.getDocuments(userName, folder, DataStorage.WITHOUT_DRAFT);
    }

    @Override
    public List<Document> getUnfiledDocuments() throws Exception {
        return ds.getUnfiledDocuments(userName, DataStorage.WITHOUT_DRAFT);
    }

    @Override
    public List<Document> getDraftDocuments() throws Exception {
        return ds.getDraftDocuments(userName);
    }

    @Override
    public Document getDocument(String idStr) throws Exception {
        try {
            int id = Integer.parseInt(idStr);
            String docOwner = ds.getDocumentOwner(id);
            if (docOwner.equals(userName)) {
                return ds.getDocument(id, DataStorage.WITH_DRAFT, DataStorage.WITH_CONTENT);
            } else {
                throw new Exception("User does not own document");
            }
        } catch (Exception ex) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Integer upFolder(int folderId) throws Exception {
        return new Integer(ds.upFolder(folderId));
    }

    @Override
    public Integer downFolder(int folderId) throws Exception {
        return new Integer(ds.downFolder(folderId));
    }

    @Override
    public Integer upDocument(int folderId, int documentId) throws Exception {
        return ds.upDocument(folderId, documentId);
    }

    @Override
    public Integer downDocument(int folderId, int documentId) throws Exception {
        return ds.downDocument(folderId, documentId);
    }

    @Override
    public String reindex() throws Exception {
        return ds.reindex();
    }

    @Override
    public List<Document> getSharedDocuments() throws Exception {
        return ds.getSharedDocuments(userName);
    }

    @Override
    public Folder moveFolder(Folder folder, int newParentId) throws Exception {
        Folder returnFolder = folder;
        if (folder.getParentId() == Folder.ROOT_FOLDER_ID) {
            throw new Exception("Root folders can not be moved");
        } else {
            if (!ds.getFolderOwner(folder).equals(userName)) {
                throw new Exception("User does not own folder");
            } else {
                returnFolder = ds.moveFolder(folder, newParentId);
            }
        }
        return returnFolder;
    }

    public List<UserShare> getUserShares(int docId) {
        return ds.getUserShares(docId);
    }

    public UserShare addUserShare(UserShare share) {
        return ds.addUserShare(share);
    }

    public UserShare updateUserShare(UserShare share) {
        return ds.updateUserShare(share);
    }

    public UserShare deleteUserShare(UserShare share) {
        return ds.deleteUserShare(share);
    }

    @Override
    public GroupShare addGroupShare(GroupShare share) {
        return ds.addGroupShare(share);
    }

    @Override
    public GroupShare deleteGroupShare(GroupShare share) {
        return ds.deleteGroupShare(share);
    }

    @Override
    public List<GroupShare> getGroupShares(int docId) {
        return ds.getGroupShares(docId);
    }

    @Override
    public GroupShare updateGroupShare(GroupShare share) {
        return ds.updateGroupShare(share);
    }

    @Override
    public UserGroup addUserGroup(UserGroup group) {
        return ds.addUserGroup(group);
    }

    @Override
    public UserGroup deleteUserGroup(UserGroup group) {
        return ds.deleteUserGroup(group);
    }

    @Override
    public List<UserGroup> getUserGroups(String owner) {
        return ds.getUserGroups(owner);
    }

    @Override
    public UserGroup updateUserGroups(UserGroup group) {
        return ds.updateUserGroups(group);
    }

    @Override
    public void addUserToGroup(int groupId, String userId) {
        ds.addUserToGroup(groupId, userId);
    }

    @Override
    public void removeUserFromGroup(int groupId, String userId) {
        ds.removeUserFromGroup(groupId, userId);
    }

    @Override
    public int deleteDocument(int docId) throws Exception {
        String docOwner = ds.getDocumentOwner(docId);
        if (docOwner.equals(userName)) {
            return ds.deleteDocument(docId);
        } else {
            throw new Exception("User does not own document");
        }
    }
}
