package com.liferay.ams.service.persistence;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;

/**
 * <a href="AssetEntryPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
@Transactional(rollbackFor = { PortalException.class, SystemException.class })
public interface AssetEntryPersistence {

    public com.liferay.ams.model.AssetEntry create(long assetEntryId);

    public com.liferay.ams.model.AssetEntry remove(long assetEntryId) throws com.liferay.ams.NoSuchAssetEntryException, com.liferay.portal.SystemException;

    public com.liferay.ams.model.AssetEntry remove(com.liferay.ams.model.AssetEntry assetEntry) throws com.liferay.portal.SystemException;

    public com.liferay.ams.model.AssetEntry update(com.liferay.ams.model.AssetEntry assetEntry) throws com.liferay.portal.SystemException;

    public com.liferay.ams.model.AssetEntry update(com.liferay.ams.model.AssetEntry assetEntry, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.ams.model.AssetEntry updateImpl(com.liferay.ams.model.AssetEntry assetEntry, boolean merge) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.liferay.ams.model.AssetEntry findByPrimaryKey(long assetEntryId) throws com.liferay.ams.NoSuchAssetEntryException, com.liferay.portal.SystemException;

    public com.liferay.ams.model.AssetEntry fetchByPrimaryKey(long assetEntryId) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<Object> findWithDynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<Object> findWithDynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.ams.model.AssetEntry> findAll() throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.ams.model.AssetEntry> findAll(int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.ams.model.AssetEntry> findAll(int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int countAll() throws com.liferay.portal.SystemException;

    public void registerListener(com.liferay.portal.model.ModelListener listener);

    public void unregisterListener(com.liferay.portal.model.ModelListener listener);
}
