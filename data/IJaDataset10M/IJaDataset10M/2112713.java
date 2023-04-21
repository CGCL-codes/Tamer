package com.liferay.portlet.auction.service.base;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.counter.service.CounterService;
import com.liferay.mail.service.MailService;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.CompanyService;
import com.liferay.portal.service.ImageLocalService;
import com.liferay.portal.service.PortletPreferencesLocalService;
import com.liferay.portal.service.PortletPreferencesService;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.CompanyPersistence;
import com.liferay.portal.service.persistence.ImagePersistence;
import com.liferay.portal.service.persistence.PortletPreferencesPersistence;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.auction.model.AuctionEntry;
import com.liferay.portlet.auction.service.AuctionEntryImageLocalService;
import com.liferay.portlet.auction.service.AuctionEntryLocalService;
import com.liferay.portlet.auction.service.AuctionEntryService;
import com.liferay.portlet.auction.service.BidEntryLocalService;
import com.liferay.portlet.auction.service.persistence.AuctionEntryImagePersistence;
import com.liferay.portlet.auction.service.persistence.AuctionEntryPersistence;
import com.liferay.portlet.auction.service.persistence.BidEntryPersistence;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;
import com.liferay.portlet.expando.service.ExpandoValueService;
import com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence;
import java.util.List;

/**
 * <a href="AuctionEntryLocalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bijan Vakili
 *
 */
public abstract class AuctionEntryLocalServiceBaseImpl implements AuctionEntryLocalService {

    public AuctionEntry addAuctionEntry(AuctionEntry auctionEntry) throws SystemException {
        auctionEntry.setNew(true);
        return auctionEntryPersistence.update(auctionEntry, false);
    }

    public AuctionEntry createAuctionEntry(long id) {
        return auctionEntryPersistence.create(id);
    }

    public void deleteAuctionEntry(long id) throws PortalException, SystemException {
        auctionEntryPersistence.remove(id);
    }

    public void deleteAuctionEntry(AuctionEntry auctionEntry) throws SystemException {
        auctionEntryPersistence.remove(auctionEntry);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery) throws SystemException {
        return auctionEntryPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start, int end) throws SystemException {
        return auctionEntryPersistence.findWithDynamicQuery(dynamicQuery, start, end);
    }

    public AuctionEntry getAuctionEntry(long id) throws PortalException, SystemException {
        return auctionEntryPersistence.findByPrimaryKey(id);
    }

    public List<AuctionEntry> getAuctionEntries(int start, int end) throws SystemException {
        return auctionEntryPersistence.findAll(start, end);
    }

    public int getAuctionEntriesCount() throws SystemException {
        return auctionEntryPersistence.countAll();
    }

    public AuctionEntry updateAuctionEntry(AuctionEntry auctionEntry) throws SystemException {
        auctionEntry.setNew(false);
        return auctionEntryPersistence.update(auctionEntry, true);
    }

    public AuctionEntry updateAuctionEntry(AuctionEntry auctionEntry, boolean merge) throws SystemException {
        auctionEntry.setNew(false);
        return auctionEntryPersistence.update(auctionEntry, merge);
    }

    public AuctionEntryLocalService getAuctionEntryLocalService() {
        return auctionEntryLocalService;
    }

    public void setAuctionEntryLocalService(AuctionEntryLocalService auctionEntryLocalService) {
        this.auctionEntryLocalService = auctionEntryLocalService;
    }

    public AuctionEntryService getAuctionEntryService() {
        return auctionEntryService;
    }

    public void setAuctionEntryService(AuctionEntryService auctionEntryService) {
        this.auctionEntryService = auctionEntryService;
    }

    public AuctionEntryPersistence getAuctionEntryPersistence() {
        return auctionEntryPersistence;
    }

    public void setAuctionEntryPersistence(AuctionEntryPersistence auctionEntryPersistence) {
        this.auctionEntryPersistence = auctionEntryPersistence;
    }

    public AuctionEntryImageLocalService getAuctionEntryImageLocalService() {
        return auctionEntryImageLocalService;
    }

    public void setAuctionEntryImageLocalService(AuctionEntryImageLocalService auctionEntryImageLocalService) {
        this.auctionEntryImageLocalService = auctionEntryImageLocalService;
    }

    public AuctionEntryImagePersistence getAuctionEntryImagePersistence() {
        return auctionEntryImagePersistence;
    }

    public void setAuctionEntryImagePersistence(AuctionEntryImagePersistence auctionEntryImagePersistence) {
        this.auctionEntryImagePersistence = auctionEntryImagePersistence;
    }

    public BidEntryLocalService getBidEntryLocalService() {
        return bidEntryLocalService;
    }

    public void setBidEntryLocalService(BidEntryLocalService bidEntryLocalService) {
        this.bidEntryLocalService = bidEntryLocalService;
    }

    public BidEntryPersistence getBidEntryPersistence() {
        return bidEntryPersistence;
    }

    public void setBidEntryPersistence(BidEntryPersistence bidEntryPersistence) {
        this.bidEntryPersistence = bidEntryPersistence;
    }

    public CounterLocalService getCounterLocalService() {
        return counterLocalService;
    }

    public void setCounterLocalService(CounterLocalService counterLocalService) {
        this.counterLocalService = counterLocalService;
    }

    public CounterService getCounterService() {
        return counterService;
    }

    public void setCounterService(CounterService counterService) {
        this.counterService = counterService;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public CompanyLocalService getCompanyLocalService() {
        return companyLocalService;
    }

    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyLocalService = companyLocalService;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public CompanyPersistence getCompanyPersistence() {
        return companyPersistence;
    }

    public void setCompanyPersistence(CompanyPersistence companyPersistence) {
        this.companyPersistence = companyPersistence;
    }

    public ImageLocalService getImageLocalService() {
        return imageLocalService;
    }

    public void setImageLocalService(ImageLocalService imageLocalService) {
        this.imageLocalService = imageLocalService;
    }

    public ImagePersistence getImagePersistence() {
        return imagePersistence;
    }

    public void setImagePersistence(ImagePersistence imagePersistence) {
        this.imagePersistence = imagePersistence;
    }

    public PortletPreferencesLocalService getPortletPreferencesLocalService() {
        return portletPreferencesLocalService;
    }

    public void setPortletPreferencesLocalService(PortletPreferencesLocalService portletPreferencesLocalService) {
        this.portletPreferencesLocalService = portletPreferencesLocalService;
    }

    public PortletPreferencesService getPortletPreferencesService() {
        return portletPreferencesService;
    }

    public void setPortletPreferencesService(PortletPreferencesService portletPreferencesService) {
        this.portletPreferencesService = portletPreferencesService;
    }

    public PortletPreferencesPersistence getPortletPreferencesPersistence() {
        return portletPreferencesPersistence;
    }

    public void setPortletPreferencesPersistence(PortletPreferencesPersistence portletPreferencesPersistence) {
        this.portletPreferencesPersistence = portletPreferencesPersistence;
    }

    public ResourceLocalService getResourceLocalService() {
        return resourceLocalService;
    }

    public void setResourceLocalService(ResourceLocalService resourceLocalService) {
        this.resourceLocalService = resourceLocalService;
    }

    public ResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public ResourcePersistence getResourcePersistence() {
        return resourcePersistence;
    }

    public void setResourcePersistence(ResourcePersistence resourcePersistence) {
        this.resourcePersistence = resourcePersistence;
    }

    public UserLocalService getUserLocalService() {
        return userLocalService;
    }

    public void setUserLocalService(UserLocalService userLocalService) {
        this.userLocalService = userLocalService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserPersistence getUserPersistence() {
        return userPersistence;
    }

    public void setUserPersistence(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public ExpandoValueLocalService getExpandoValueLocalService() {
        return expandoValueLocalService;
    }

    public void setExpandoValueLocalService(ExpandoValueLocalService expandoValueLocalService) {
        this.expandoValueLocalService = expandoValueLocalService;
    }

    public ExpandoValueService getExpandoValueService() {
        return expandoValueService;
    }

    public void setExpandoValueService(ExpandoValueService expandoValueService) {
        this.expandoValueService = expandoValueService;
    }

    public ExpandoValuePersistence getExpandoValuePersistence() {
        return expandoValuePersistence;
    }

    public void setExpandoValuePersistence(ExpandoValuePersistence expandoValuePersistence) {
        this.expandoValuePersistence = expandoValuePersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @BeanReference(name = "com.liferay.portlet.auction.service.AuctionEntryLocalService.impl")
    protected AuctionEntryLocalService auctionEntryLocalService;

    @BeanReference(name = "com.liferay.portlet.auction.service.AuctionEntryService.impl")
    protected AuctionEntryService auctionEntryService;

    @BeanReference(name = "com.liferay.portlet.auction.service.persistence.AuctionEntryPersistence.impl")
    protected AuctionEntryPersistence auctionEntryPersistence;

    @BeanReference(name = "com.liferay.portlet.auction.service.AuctionEntryImageLocalService.impl")
    protected AuctionEntryImageLocalService auctionEntryImageLocalService;

    @BeanReference(name = "com.liferay.portlet.auction.service.persistence.AuctionEntryImagePersistence.impl")
    protected AuctionEntryImagePersistence auctionEntryImagePersistence;

    @BeanReference(name = "com.liferay.portlet.auction.service.BidEntryLocalService.impl")
    protected BidEntryLocalService bidEntryLocalService;

    @BeanReference(name = "com.liferay.portlet.auction.service.persistence.BidEntryPersistence.impl")
    protected BidEntryPersistence bidEntryPersistence;

    @BeanReference(name = "com.liferay.counter.service.CounterLocalService.impl")
    protected CounterLocalService counterLocalService;

    @BeanReference(name = "com.liferay.counter.service.CounterService.impl")
    protected CounterService counterService;

    @BeanReference(name = "com.liferay.mail.service.MailService.impl")
    protected MailService mailService;

    @BeanReference(name = "com.liferay.portal.service.CompanyLocalService.impl")
    protected CompanyLocalService companyLocalService;

    @BeanReference(name = "com.liferay.portal.service.CompanyService.impl")
    protected CompanyService companyService;

    @BeanReference(name = "com.liferay.portal.service.persistence.CompanyPersistence.impl")
    protected CompanyPersistence companyPersistence;

    @BeanReference(name = "com.liferay.portal.service.ImageLocalService.impl")
    protected ImageLocalService imageLocalService;

    @BeanReference(name = "com.liferay.portal.service.persistence.ImagePersistence.impl")
    protected ImagePersistence imagePersistence;

    @BeanReference(name = "com.liferay.portal.service.PortletPreferencesLocalService.impl")
    protected PortletPreferencesLocalService portletPreferencesLocalService;

    @BeanReference(name = "com.liferay.portal.service.PortletPreferencesService.impl")
    protected PortletPreferencesService portletPreferencesService;

    @BeanReference(name = "com.liferay.portal.service.persistence.PortletPreferencesPersistence.impl")
    protected PortletPreferencesPersistence portletPreferencesPersistence;

    @BeanReference(name = "com.liferay.portal.service.ResourceLocalService.impl")
    protected ResourceLocalService resourceLocalService;

    @BeanReference(name = "com.liferay.portal.service.ResourceService.impl")
    protected ResourceService resourceService;

    @BeanReference(name = "com.liferay.portal.service.persistence.ResourcePersistence.impl")
    protected ResourcePersistence resourcePersistence;

    @BeanReference(name = "com.liferay.portal.service.UserLocalService.impl")
    protected UserLocalService userLocalService;

    @BeanReference(name = "com.liferay.portal.service.UserService.impl")
    protected UserService userService;

    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected UserPersistence userPersistence;

    @BeanReference(name = "com.liferay.portlet.expando.service.ExpandoValueLocalService.impl")
    protected ExpandoValueLocalService expandoValueLocalService;

    @BeanReference(name = "com.liferay.portlet.expando.service.ExpandoValueService.impl")
    protected ExpandoValueService expandoValueService;

    @BeanReference(name = "com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence.impl")
    protected ExpandoValuePersistence expandoValuePersistence;
}
