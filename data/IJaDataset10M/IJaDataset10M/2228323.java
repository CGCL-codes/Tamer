package cn.com.believer.songyuanframework.openapi.storage.box.objects;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jimmy
 *
 */
public interface BoxUpdate extends Serializable {

    /**
     * @return the updateId
     */
    public String getUpdateId();

    /**
     * @param updateId
     *            the updateId to set
     */
    public void setUpdateId(String updateId);

    /**
     * @return the userId
     */
    public String getUserId();

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId);

    /**
     * @return the userName
     */
    public String getUserName();

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName);

    /**
     * @return the userEmail
     */
    public String getUserEmail();

    /**
     * @param userEmail
     *            the userEmail to set
     */
    public void setUserEmail(String userEmail);

    /**
     * @return the updated
     */
    public String getUpdated();

    /**
     * @param updated
     *            the updated to set
     */
    public void setUpdated(String updated);

    /**
     * @return the updateType
     */
    public String getUpdateType();

    /**
     * @param updateType
     *            the updateType to set
     */
    public void setUpdateType(String updateType);

    /**
     * @return the folderId
     */
    public String getFolderId();

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(String folderId);

    /**
     * @return the folderName
     */
    public String getFolderName();

    /**
     * @param folderName
     *            the folderName to set
     */
    public void setFolderName(String folderName);

    /**
     * @return the shared
     */
    public String getShared();

    /**
     * @param shared
     *            the shared to set
     */
    public void setShared(String shared);

    /**
     * @return the sharedName
     */
    public String getSharedName();

    /**
     * @param sharedName
     *            the sharedName to set
     */
    public void setSharedName(String sharedName);

    /**
     * @return the ownerId
     */
    public String getOwnerId();

    /**
     * @param ownerId
     *            the ownerId to set
     */
    public void setOwnerId(String ownerId);

    /**
     * @return the folderPath
     */
    public String getFolderPath();

    /**
     * @param folderPath
     *            the folderPath to set
     */
    public void setFolderPath(String folderPath);

    /**
     * @return the collabAccess
     */
    public String getCollabAccess();

    /**
     * @param collabAccess
     *            the collabAccess to set
     */
    public void setCollabAccess(String collabAccess);

    /**
     * @return the commentCount
     */
    public String getCommentCount();

    /**
     * @param commentCount
     *            the commentCount to set
     */
    public void setCommentCount(String commentCount);

    /**
     * @return the files
     */
    public List getFiles();

    /**
     * @param files
     *            the files to set
     */
    public void setFiles(List files);

    /**
     * @return the discussions
     */
    public List getDiscussions();

    /**
     * @param discussions
     *            the discussions to set
     */
    public void setDiscussions(List discussions);

    /**
     * @return the folders
     */
    public List getFolders();

    /**
     * @param folders
     *            the folders to set
     */
    public void setFolders(List folders);
}
