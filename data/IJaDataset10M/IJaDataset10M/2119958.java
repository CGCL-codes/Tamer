package com.liferay.kb.knowledgebase.service.base;

import com.liferay.kb.knowledgebase.service.KBArticleLocalService;
import com.liferay.kb.knowledgebase.service.KBArticleResourceLocalService;
import com.liferay.kb.knowledgebase.service.KBArticleService;
import com.liferay.kb.knowledgebase.service.KBFeedbackEntryLocalService;
import com.liferay.kb.knowledgebase.service.KBFeedbackStatsLocalService;
import com.liferay.kb.knowledgebase.service.persistence.KBArticleFinder;
import com.liferay.kb.knowledgebase.service.persistence.KBArticlePersistence;
import com.liferay.kb.knowledgebase.service.persistence.KBArticleResourcePersistence;
import com.liferay.kb.knowledgebase.service.persistence.KBFeedbackEntryPersistence;
import com.liferay.kb.knowledgebase.service.persistence.KBFeedbackStatsPersistence;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.base.PrincipalBean;

/**
 * <a href="KBArticleServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 *
 */
public abstract class KBArticleServiceBaseImpl extends PrincipalBean implements KBArticleService {

    public KBArticleLocalService getKBArticleLocalService() {
        return kbArticleLocalService;
    }

    public void setKBArticleLocalService(KBArticleLocalService kbArticleLocalService) {
        this.kbArticleLocalService = kbArticleLocalService;
    }

    public KBArticleService getKBArticleService() {
        return kbArticleService;
    }

    public void setKBArticleService(KBArticleService kbArticleService) {
        this.kbArticleService = kbArticleService;
    }

    public KBArticlePersistence getKBArticlePersistence() {
        return kbArticlePersistence;
    }

    public void setKBArticlePersistence(KBArticlePersistence kbArticlePersistence) {
        this.kbArticlePersistence = kbArticlePersistence;
    }

    public KBArticleFinder getKBArticleFinder() {
        return kbArticleFinder;
    }

    public void setKBArticleFinder(KBArticleFinder kbArticleFinder) {
        this.kbArticleFinder = kbArticleFinder;
    }

    public KBArticleResourceLocalService getKBArticleResourceLocalService() {
        return kbArticleResourceLocalService;
    }

    public void setKBArticleResourceLocalService(KBArticleResourceLocalService kbArticleResourceLocalService) {
        this.kbArticleResourceLocalService = kbArticleResourceLocalService;
    }

    public KBArticleResourcePersistence getKBArticleResourcePersistence() {
        return kbArticleResourcePersistence;
    }

    public void setKBArticleResourcePersistence(KBArticleResourcePersistence kbArticleResourcePersistence) {
        this.kbArticleResourcePersistence = kbArticleResourcePersistence;
    }

    public KBFeedbackEntryLocalService getKBFeedbackEntryLocalService() {
        return kbFeedbackEntryLocalService;
    }

    public void setKBFeedbackEntryLocalService(KBFeedbackEntryLocalService kbFeedbackEntryLocalService) {
        this.kbFeedbackEntryLocalService = kbFeedbackEntryLocalService;
    }

    public KBFeedbackEntryPersistence getKBFeedbackEntryPersistence() {
        return kbFeedbackEntryPersistence;
    }

    public void setKBFeedbackEntryPersistence(KBFeedbackEntryPersistence kbFeedbackEntryPersistence) {
        this.kbFeedbackEntryPersistence = kbFeedbackEntryPersistence;
    }

    public KBFeedbackStatsLocalService getKBFeedbackStatsLocalService() {
        return kbFeedbackStatsLocalService;
    }

    public void setKBFeedbackStatsLocalService(KBFeedbackStatsLocalService kbFeedbackStatsLocalService) {
        this.kbFeedbackStatsLocalService = kbFeedbackStatsLocalService;
    }

    public KBFeedbackStatsPersistence getKBFeedbackStatsPersistence() {
        return kbFeedbackStatsPersistence;
    }

    public void setKBFeedbackStatsPersistence(KBFeedbackStatsPersistence kbFeedbackStatsPersistence) {
        this.kbFeedbackStatsPersistence = kbFeedbackStatsPersistence;
    }

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.KBArticleLocalService.impl")
    protected KBArticleLocalService kbArticleLocalService;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.KBArticleService.impl")
    protected KBArticleService kbArticleService;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.persistence.KBArticlePersistence.impl")
    protected KBArticlePersistence kbArticlePersistence;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.persistence.KBArticleFinder.impl")
    protected KBArticleFinder kbArticleFinder;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.KBArticleResourceLocalService.impl")
    protected KBArticleResourceLocalService kbArticleResourceLocalService;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.persistence.KBArticleResourcePersistence.impl")
    protected KBArticleResourcePersistence kbArticleResourcePersistence;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.KBFeedbackEntryLocalService.impl")
    protected KBFeedbackEntryLocalService kbFeedbackEntryLocalService;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.persistence.KBFeedbackEntryPersistence.impl")
    protected KBFeedbackEntryPersistence kbFeedbackEntryPersistence;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.KBFeedbackStatsLocalService.impl")
    protected KBFeedbackStatsLocalService kbFeedbackStatsLocalService;

    @BeanReference(name = "com.liferay.kb.knowledgebase.service.persistence.KBFeedbackStatsPersistence.impl")
    protected KBFeedbackStatsPersistence kbFeedbackStatsPersistence;
}
