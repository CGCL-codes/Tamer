package com.liferay.portlet.journal.service.base;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.counter.service.CounterService;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.service.persistence.ResourceFinder;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portlet.journal.service.JournalArticleImageLocalService;
import com.liferay.portlet.journal.service.JournalArticleLocalService;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalService;
import com.liferay.portlet.journal.service.JournalArticleService;
import com.liferay.portlet.journal.service.JournalContentSearchLocalService;
import com.liferay.portlet.journal.service.JournalFeedLocalService;
import com.liferay.portlet.journal.service.JournalFeedService;
import com.liferay.portlet.journal.service.JournalStructureLocalService;
import com.liferay.portlet.journal.service.JournalStructureService;
import com.liferay.portlet.journal.service.JournalTemplateLocalService;
import com.liferay.portlet.journal.service.JournalTemplateService;
import com.liferay.portlet.journal.service.persistence.JournalArticleFinder;
import com.liferay.portlet.journal.service.persistence.JournalArticleImagePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticlePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticleResourcePersistence;
import com.liferay.portlet.journal.service.persistence.JournalContentSearchPersistence;
import com.liferay.portlet.journal.service.persistence.JournalFeedFinder;
import com.liferay.portlet.journal.service.persistence.JournalFeedPersistence;
import com.liferay.portlet.journal.service.persistence.JournalStructureFinder;
import com.liferay.portlet.journal.service.persistence.JournalStructurePersistence;
import com.liferay.portlet.journal.service.persistence.JournalTemplateFinder;
import com.liferay.portlet.journal.service.persistence.JournalTemplatePersistence;

/**
 * <a href="JournalFeedServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class JournalFeedServiceBaseImpl extends PrincipalBean implements JournalFeedService {

    public JournalArticleLocalService getJournalArticleLocalService() {
        return journalArticleLocalService;
    }

    public void setJournalArticleLocalService(JournalArticleLocalService journalArticleLocalService) {
        this.journalArticleLocalService = journalArticleLocalService;
    }

    public JournalArticleService getJournalArticleService() {
        return journalArticleService;
    }

    public void setJournalArticleService(JournalArticleService journalArticleService) {
        this.journalArticleService = journalArticleService;
    }

    public JournalArticlePersistence getJournalArticlePersistence() {
        return journalArticlePersistence;
    }

    public void setJournalArticlePersistence(JournalArticlePersistence journalArticlePersistence) {
        this.journalArticlePersistence = journalArticlePersistence;
    }

    public JournalArticleFinder getJournalArticleFinder() {
        return journalArticleFinder;
    }

    public void setJournalArticleFinder(JournalArticleFinder journalArticleFinder) {
        this.journalArticleFinder = journalArticleFinder;
    }

    public JournalArticleImageLocalService getJournalArticleImageLocalService() {
        return journalArticleImageLocalService;
    }

    public void setJournalArticleImageLocalService(JournalArticleImageLocalService journalArticleImageLocalService) {
        this.journalArticleImageLocalService = journalArticleImageLocalService;
    }

    public JournalArticleImagePersistence getJournalArticleImagePersistence() {
        return journalArticleImagePersistence;
    }

    public void setJournalArticleImagePersistence(JournalArticleImagePersistence journalArticleImagePersistence) {
        this.journalArticleImagePersistence = journalArticleImagePersistence;
    }

    public JournalArticleResourceLocalService getJournalArticleResourceLocalService() {
        return journalArticleResourceLocalService;
    }

    public void setJournalArticleResourceLocalService(JournalArticleResourceLocalService journalArticleResourceLocalService) {
        this.journalArticleResourceLocalService = journalArticleResourceLocalService;
    }

    public JournalArticleResourcePersistence getJournalArticleResourcePersistence() {
        return journalArticleResourcePersistence;
    }

    public void setJournalArticleResourcePersistence(JournalArticleResourcePersistence journalArticleResourcePersistence) {
        this.journalArticleResourcePersistence = journalArticleResourcePersistence;
    }

    public JournalContentSearchLocalService getJournalContentSearchLocalService() {
        return journalContentSearchLocalService;
    }

    public void setJournalContentSearchLocalService(JournalContentSearchLocalService journalContentSearchLocalService) {
        this.journalContentSearchLocalService = journalContentSearchLocalService;
    }

    public JournalContentSearchPersistence getJournalContentSearchPersistence() {
        return journalContentSearchPersistence;
    }

    public void setJournalContentSearchPersistence(JournalContentSearchPersistence journalContentSearchPersistence) {
        this.journalContentSearchPersistence = journalContentSearchPersistence;
    }

    public JournalFeedLocalService getJournalFeedLocalService() {
        return journalFeedLocalService;
    }

    public void setJournalFeedLocalService(JournalFeedLocalService journalFeedLocalService) {
        this.journalFeedLocalService = journalFeedLocalService;
    }

    public JournalFeedService getJournalFeedService() {
        return journalFeedService;
    }

    public void setJournalFeedService(JournalFeedService journalFeedService) {
        this.journalFeedService = journalFeedService;
    }

    public JournalFeedPersistence getJournalFeedPersistence() {
        return journalFeedPersistence;
    }

    public void setJournalFeedPersistence(JournalFeedPersistence journalFeedPersistence) {
        this.journalFeedPersistence = journalFeedPersistence;
    }

    public JournalFeedFinder getJournalFeedFinder() {
        return journalFeedFinder;
    }

    public void setJournalFeedFinder(JournalFeedFinder journalFeedFinder) {
        this.journalFeedFinder = journalFeedFinder;
    }

    public JournalStructureLocalService getJournalStructureLocalService() {
        return journalStructureLocalService;
    }

    public void setJournalStructureLocalService(JournalStructureLocalService journalStructureLocalService) {
        this.journalStructureLocalService = journalStructureLocalService;
    }

    public JournalStructureService getJournalStructureService() {
        return journalStructureService;
    }

    public void setJournalStructureService(JournalStructureService journalStructureService) {
        this.journalStructureService = journalStructureService;
    }

    public JournalStructurePersistence getJournalStructurePersistence() {
        return journalStructurePersistence;
    }

    public void setJournalStructurePersistence(JournalStructurePersistence journalStructurePersistence) {
        this.journalStructurePersistence = journalStructurePersistence;
    }

    public JournalStructureFinder getJournalStructureFinder() {
        return journalStructureFinder;
    }

    public void setJournalStructureFinder(JournalStructureFinder journalStructureFinder) {
        this.journalStructureFinder = journalStructureFinder;
    }

    public JournalTemplateLocalService getJournalTemplateLocalService() {
        return journalTemplateLocalService;
    }

    public void setJournalTemplateLocalService(JournalTemplateLocalService journalTemplateLocalService) {
        this.journalTemplateLocalService = journalTemplateLocalService;
    }

    public JournalTemplateService getJournalTemplateService() {
        return journalTemplateService;
    }

    public void setJournalTemplateService(JournalTemplateService journalTemplateService) {
        this.journalTemplateService = journalTemplateService;
    }

    public JournalTemplatePersistence getJournalTemplatePersistence() {
        return journalTemplatePersistence;
    }

    public void setJournalTemplatePersistence(JournalTemplatePersistence journalTemplatePersistence) {
        this.journalTemplatePersistence = journalTemplatePersistence;
    }

    public JournalTemplateFinder getJournalTemplateFinder() {
        return journalTemplateFinder;
    }

    public void setJournalTemplateFinder(JournalTemplateFinder journalTemplateFinder) {
        this.journalTemplateFinder = journalTemplateFinder;
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

    public ResourceFinder getResourceFinder() {
        return resourceFinder;
    }

    public void setResourceFinder(ResourceFinder resourceFinder) {
        this.resourceFinder = resourceFinder;
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

    public UserFinder getUserFinder() {
        return userFinder;
    }

    public void setUserFinder(UserFinder userFinder) {
        this.userFinder = userFinder;
    }

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalArticleLocalService.impl")
    protected JournalArticleLocalService journalArticleLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalArticleService.impl")
    protected JournalArticleService journalArticleService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalArticlePersistence.impl")
    protected JournalArticlePersistence journalArticlePersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalArticleFinder.impl")
    protected JournalArticleFinder journalArticleFinder;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalArticleImageLocalService.impl")
    protected JournalArticleImageLocalService journalArticleImageLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalArticleImagePersistence.impl")
    protected JournalArticleImagePersistence journalArticleImagePersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalArticleResourceLocalService.impl")
    protected JournalArticleResourceLocalService journalArticleResourceLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalArticleResourcePersistence.impl")
    protected JournalArticleResourcePersistence journalArticleResourcePersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalContentSearchLocalService.impl")
    protected JournalContentSearchLocalService journalContentSearchLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalContentSearchPersistence.impl")
    protected JournalContentSearchPersistence journalContentSearchPersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalFeedLocalService.impl")
    protected JournalFeedLocalService journalFeedLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalFeedService.impl")
    protected JournalFeedService journalFeedService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalFeedPersistence.impl")
    protected JournalFeedPersistence journalFeedPersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalFeedFinder.impl")
    protected JournalFeedFinder journalFeedFinder;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalStructureLocalService.impl")
    protected JournalStructureLocalService journalStructureLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalStructureService.impl")
    protected JournalStructureService journalStructureService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalStructurePersistence.impl")
    protected JournalStructurePersistence journalStructurePersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalStructureFinder.impl")
    protected JournalStructureFinder journalStructureFinder;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalTemplateLocalService.impl")
    protected JournalTemplateLocalService journalTemplateLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.JournalTemplateService.impl")
    protected JournalTemplateService journalTemplateService;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalTemplatePersistence.impl")
    protected JournalTemplatePersistence journalTemplatePersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.journal.service.persistence.JournalTemplateFinder.impl")
    protected JournalTemplateFinder journalTemplateFinder;

    @javax.annotation.Resource(name = "com.liferay.counter.service.CounterLocalService.impl")
    protected CounterLocalService counterLocalService;

    @javax.annotation.Resource(name = "com.liferay.counter.service.CounterService.impl")
    protected CounterService counterService;

    @javax.annotation.Resource(name = "com.liferay.portal.service.ResourceLocalService.impl")
    protected ResourceLocalService resourceLocalService;

    @javax.annotation.Resource(name = "com.liferay.portal.service.ResourceService.impl")
    protected ResourceService resourceService;

    @javax.annotation.Resource(name = "com.liferay.portal.service.persistence.ResourcePersistence.impl")
    protected ResourcePersistence resourcePersistence;

    @javax.annotation.Resource(name = "com.liferay.portal.service.persistence.ResourceFinder.impl")
    protected ResourceFinder resourceFinder;

    @javax.annotation.Resource(name = "com.liferay.portal.service.UserLocalService.impl")
    protected UserLocalService userLocalService;

    @javax.annotation.Resource(name = "com.liferay.portal.service.UserService.impl")
    protected UserService userService;

    @javax.annotation.Resource(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected UserPersistence userPersistence;

    @javax.annotation.Resource(name = "com.liferay.portal.service.persistence.UserFinder.impl")
    protected UserFinder userFinder;
}
