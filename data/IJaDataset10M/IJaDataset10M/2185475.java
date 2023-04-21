package jmemento.web.controller.photo;

import java.util.Collection;
import jmemento.api.domain.pool.IPool;
import jmemento.api.domain.user.IUser;
import jmemento.impl.domain.photo.ImageSize;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author Rusty Wright
 * 
 */
public final class PhotoInfo {

    private String photoId;

    private String size;

    private String displayName;

    private String poolName;

    private String nextId;

    private String prevId;

    private boolean invalidPhoto;

    private ImageSize imageSize;

    private IUser owner;

    private Collection<IPool> poolsMemberOf;

    private Collection<IPool> poolsAvailable;

    private boolean editOk;

    private boolean delete;

    private String poolAddId;

    private String poolId;

    private String poolThumb;

    public PhotoInfo() {
    }

    public PhotoInfo(final boolean _editOk) {
        editOk = _editOk;
    }

    /**
     * @return the photoId
     */
    public String getPhotoId() {
        return (photoId);
    }

    /**
     * @param _photoId
     * the photoId to set
     */
    public void setPhotoId(final String _photoId) {
        photoId = _photoId;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return (size);
    }

    /**
     * @param _size
     * the size to set
     */
    public void setSize(final String _size) {
        size = _size;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return (displayName);
    }

    /**
     * @param _displayName
     * the displayName to set
     */
    public void setDisplayName(final String _displayName) {
        displayName = _displayName;
    }

    /**
     * @return the poolName
     */
    public String getPoolName() {
        return (poolName);
    }

    /**
     * @param _pool
     * the poolName to set
     */
    public void setPoolName(final String _pool) {
        poolName = _pool;
    }

    /**
     * @return the nextId
     */
    public String getNextId() {
        return (nextId);
    }

    /**
     * @param _nextId
     * the nextId to set
     */
    public void setNextId(final String _nextId) {
        nextId = _nextId;
    }

    /**
     * @return the prevId
     */
    public String getPrevId() {
        return (prevId);
    }

    /**
     * @param _prevId
     * the prevId to set
     */
    public void setPrevId(final String _prevId) {
        prevId = _prevId;
    }

    /**
     * @return the invalidPhoto
     */
    public boolean isInvalidPhoto() {
        return (invalidPhoto);
    }

    /**
     * @param _invalidPhoto
     * the invalidPhoto to set
     */
    public void setInvalidPhoto(final boolean _invalidPhoto) {
        invalidPhoto = _invalidPhoto;
    }

    /**
     * @return the imageSize
     */
    public ImageSize getImageSize() {
        return (imageSize);
    }

    /**
     * @param _imageSize
     * the imageSize to set
     */
    public void setImageSize(final ImageSize _imageSize) {
        imageSize = _imageSize;
    }

    /**
     * @return the owner
     */
    public IUser getOwner() {
        return (owner);
    }

    /**
     * @param _user
     * the owner to set
     */
    public void setOwner(final IUser _user) {
        owner = _user;
    }

    /**
     * @return the poolsMemberOf
     */
    public Collection<IPool> getPoolsMemberOf() {
        return (poolsMemberOf);
    }

    /**
     * @param _pools
     * the poolsMemberOf to set
     */
    public void setPoolsMemberOf(final Collection<IPool> _pools) {
        poolsMemberOf = _pools;
    }

    /**
     * @return the poolsAvailable
     */
    public Collection<IPool> getPoolsAvailable() {
        return (poolsAvailable);
    }

    /**
     * @param _poolsAvailable
     * the poolsAvailable to set
     */
    public void setPoolsAvailable(final Collection<IPool> _poolsAvailable) {
        poolsAvailable = _poolsAvailable;
    }

    /**
     * @return the poolId
     */
    public String getPoolId() {
        return (poolId);
    }

    /**
     * @param _poolId
     * the poolId to set
     */
    public void setPoolId(final String _poolId) {
        this.poolId = _poolId;
    }

    /**
     * @return the editOk
     */
    public boolean isEditOk() {
        return (editOk);
    }

    /**
     * @param editOk
     * the editOk to set
     */
    public void setEditOk(final boolean editOk) {
        this.editOk = editOk;
    }

    /**
     * @return the delete
     */
    public boolean isDelete() {
        return (delete);
    }

    /**
     * @param delete
     * the delete to set
     */
    public void setDelete(final boolean delete) {
        this.delete = delete;
    }

    /**
     * @return the poolAddId
     */
    public String getPoolAddId() {
        return (poolAddId);
    }

    /**
     * @param poolAddId
     * the poolAddId to set
     */
    public void setPoolAddId(final String poolAddId) {
        this.poolAddId = poolAddId;
    }

    /**
     * @return the poolThumb
     */
    public String getPoolThumb() {
        return (poolThumb);
    }

    /**
     * @param poolThumb
     * the poolThumb to set
     */
    public void setPoolThumb(final String poolThumb) {
        this.poolThumb = poolThumb;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
