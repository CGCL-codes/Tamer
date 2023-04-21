package cn.com.believer.songyuanframework.openapi.storage.box.objects;

import java.util.List;

/**
 * @author Jimmy
 * 
 */
public interface BoxAbstractFile {

    /**
     * @return the id
     */
    public String getId();

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id);

    /**
     * @return the name
     */
    public String getName();

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name);

    /**
     * @return the keyword
     */
    public String getKeyword();

    /**
     * @param keyword
     *            the keyword to set
     */
    public void setKeyword(String keyword);

    /**
     * @return the shared
     */
    public boolean isShared();

    /**
     * @param shared
     *            the shared to set
     */
    public void setShared(boolean shared);

    /**
     * @return the size
     */
    public long getSize();

    /**
     * @param size
     *            the size to set
     */
    public void setSize(long size);

    /**
     * @return the isFolder
     */
    public boolean isFolder();

    /**
     * @param isFolder
     *            the isFolder to set
     */
    public void setFolder(boolean isFolder);

    /**
     * @return the created
     */
    public long getCreated();

    /**
     * @param created
     *            the created to set
     */
    public void setCreated(long created);

    /**
     * @return the updated
     */
    public long getUpdated();

    /**
     * @param updated
     *            the updated to set
     */
    public void setUpdated(long updated);

    /**
     * @return the tags
     */
    public List getTags();

    /**
     * @param tags
     *            the tags to set
     */
    public void setTags(List tags);
}
