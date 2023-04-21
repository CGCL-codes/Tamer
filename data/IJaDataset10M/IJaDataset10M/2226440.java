package com.teliose.service.administrator;

import com.teliose.entity.persistence.inventory.TblSite;
import com.teliose.entity.persistence.inventory.TblUserSite;
import com.teliose.entity.result.ParseResult;
import com.teliose.entity.result.administrator.SiteResult;
import com.teliose.entity.result.administrator.UserSiteResult;
import com.teliose.service.AbstractService;

/**
 * 
 * @author Prabath Ariyarathna
 */
public interface SiteService extends AbstractService {

    public ParseResult<TblSite> saveSite(TblSite siteDetail);

    public UserSiteResult saveUserSite(TblUserSite userSite);

    public UserSiteResult getAddedUsers(int params);

    public SiteResult findById(int siteId);

    public SiteResult updateSite(TblSite siteDetail);

    public void truncTable(int siteId);

    public void deleteSite(int siteId);

    public SiteResult getSiteListFirstPage(int pageSize);

    public SiteResult getSiteListLastPage(int pageSize);

    public SiteResult getSiteListNextPage(int pageSize);

    public SiteResult getSiterListPreviousePage(int pageSize);
}
