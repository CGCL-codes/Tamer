package com.liferay.wol.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;

/**
 * <a href="SVNRevisionLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
@Transactional(rollbackFor = { PortalException.class, SystemException.class })
public interface SVNRevisionLocalService {

    public com.liferay.wol.model.SVNRevision addSVNRevision(com.liferay.wol.model.SVNRevision svnRevision) throws com.liferay.portal.SystemException;

    public com.liferay.wol.model.SVNRevision createSVNRevision(long svnRevisionId);

    public void deleteSVNRevision(long svnRevisionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deleteSVNRevision(com.liferay.wol.model.SVNRevision svnRevision) throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.liferay.wol.model.SVNRevision getSVNRevision(long svnRevisionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.wol.model.SVNRevision> getSVNRevisions(int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getSVNRevisionsCount() throws com.liferay.portal.SystemException;

    public com.liferay.wol.model.SVNRevision updateSVNRevision(com.liferay.wol.model.SVNRevision svnRevision) throws com.liferay.portal.SystemException;

    public com.liferay.wol.model.SVNRevision addSVNRevision(java.lang.String svnUserId, java.util.Date createDate, long svnRepositoryId, long revisionNumber, java.lang.String comments) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.liferay.wol.model.SVNRevision getFirstSVNRevision(java.lang.String svnUserId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.liferay.wol.model.SVNRevision getLastSVNRevision(java.lang.String svnUserId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.wol.model.SVNRevision> getSVNRevisions(java.lang.String svnUserId, int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.wol.model.SVNRevision> getSVNRevisions(long svnRepositoryId, int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.liferay.wol.model.SVNRevision> getSVNRevisions(java.lang.String svnUserId, long svnRepositoryId, int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getSVNRevisionsCount(java.lang.String svnUserId) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getSVNRevisionsCount(long svnRepositoryId) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getSVNRevisionsCount(java.lang.String svnUserId, long svnRepositoryId) throws com.liferay.portal.SystemException;
}
