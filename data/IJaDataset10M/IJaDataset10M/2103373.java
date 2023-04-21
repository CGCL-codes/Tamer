package com.liferay.portlet.softwarecatalog.service.persistence;

/**
 * <a href="SCLicensePersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface SCLicensePersistence {

    public com.liferay.portlet.softwarecatalog.model.SCLicense create(long licenseId);

    public com.liferay.portlet.softwarecatalog.model.SCLicense remove(long licenseId) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense remove(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense update(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense update(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense updateImpl(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense findByPrimaryKey(long licenseId) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense fetchByPrimaryKey(long licenseId) throws com.liferay.portal.SystemException;

    public java.util.List findByActive(boolean active) throws com.liferay.portal.SystemException;

    public java.util.List findByActive(boolean active, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByActive(boolean active, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense findByActive_First(boolean active, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense findByActive_Last(boolean active, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense[] findByActive_PrevAndNext(long licenseId, boolean active, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public java.util.List findByA_R(boolean active, boolean recommended) throws com.liferay.portal.SystemException;

    public java.util.List findByA_R(boolean active, boolean recommended, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByA_R(boolean active, boolean recommended, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense findByA_R_First(boolean active, boolean recommended, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense findByA_R_Last(boolean active, boolean recommended, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public com.liferay.portlet.softwarecatalog.model.SCLicense[] findByA_R_PrevAndNext(long licenseId, boolean active, boolean recommended, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.softwarecatalog.NoSuchLicenseException;

    public java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException;

    public java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findAll() throws com.liferay.portal.SystemException;

    public java.util.List findAll(int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findAll(int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public void removeByActive(boolean active) throws com.liferay.portal.SystemException;

    public void removeByA_R(boolean active, boolean recommended) throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByActive(boolean active) throws com.liferay.portal.SystemException;

    public int countByA_R(boolean active, boolean recommended) throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
