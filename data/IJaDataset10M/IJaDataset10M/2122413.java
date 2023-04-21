package com.liferay.portlet.journal.service.base;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portlet.journal.service.JournalArticleImageLocalService;
import com.liferay.portlet.journal.service.JournalArticleImageLocalServiceFactory;
import com.liferay.portlet.journal.service.JournalArticleLocalService;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceFactory;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalService;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceFactory;
import com.liferay.portlet.journal.service.JournalArticleService;
import com.liferay.portlet.journal.service.JournalArticleServiceFactory;
import com.liferay.portlet.journal.service.JournalContentSearchLocalService;
import com.liferay.portlet.journal.service.JournalContentSearchLocalServiceFactory;
import com.liferay.portlet.journal.service.JournalStructureLocalService;
import com.liferay.portlet.journal.service.JournalTemplateLocalService;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceFactory;
import com.liferay.portlet.journal.service.JournalTemplateService;
import com.liferay.portlet.journal.service.JournalTemplateServiceFactory;
import com.liferay.portlet.journal.service.persistence.JournalArticleImagePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticleImageUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticlePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticleResourcePersistence;
import com.liferay.portlet.journal.service.persistence.JournalArticleResourceUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticleUtil;
import com.liferay.portlet.journal.service.persistence.JournalContentSearchPersistence;
import com.liferay.portlet.journal.service.persistence.JournalContentSearchUtil;
import com.liferay.portlet.journal.service.persistence.JournalStructurePersistence;
import com.liferay.portlet.journal.service.persistence.JournalStructureUtil;
import com.liferay.portlet.journal.service.persistence.JournalTemplatePersistence;
import com.liferay.portlet.journal.service.persistence.JournalTemplateUtil;
import org.springframework.beans.factory.InitializingBean;
import java.util.List;

/**
 * <a href="JournalStructureLocalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class JournalStructureLocalServiceBaseImpl implements JournalStructureLocalService, InitializingBean {

    public List dynamicQuery(DynamicQueryInitializer queryInitializer) throws SystemException {
        return JournalStructureUtil.findWithDynamicQuery(queryInitializer);
    }

    public List dynamicQuery(DynamicQueryInitializer queryInitializer, int begin, int end) throws SystemException {
        return JournalStructureUtil.findWithDynamicQuery(queryInitializer, begin, end);
    }

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

    public JournalStructurePersistence getJournalStructurePersistence() {
        return journalStructurePersistence;
    }

    public void setJournalStructurePersistence(JournalStructurePersistence journalStructurePersistence) {
        this.journalStructurePersistence = journalStructurePersistence;
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

    public void afterPropertiesSet() {
        if (journalArticleLocalService == null) {
            journalArticleLocalService = JournalArticleLocalServiceFactory.getImpl();
        }
        if (journalArticleService == null) {
            journalArticleService = JournalArticleServiceFactory.getImpl();
        }
        if (journalArticlePersistence == null) {
            journalArticlePersistence = JournalArticleUtil.getPersistence();
        }
        if (journalArticleImageLocalService == null) {
            journalArticleImageLocalService = JournalArticleImageLocalServiceFactory.getImpl();
        }
        if (journalArticleImagePersistence == null) {
            journalArticleImagePersistence = JournalArticleImageUtil.getPersistence();
        }
        if (journalArticleResourceLocalService == null) {
            journalArticleResourceLocalService = JournalArticleResourceLocalServiceFactory.getImpl();
        }
        if (journalArticleResourcePersistence == null) {
            journalArticleResourcePersistence = JournalArticleResourceUtil.getPersistence();
        }
        if (journalContentSearchLocalService == null) {
            journalContentSearchLocalService = JournalContentSearchLocalServiceFactory.getImpl();
        }
        if (journalContentSearchPersistence == null) {
            journalContentSearchPersistence = JournalContentSearchUtil.getPersistence();
        }
        if (journalStructurePersistence == null) {
            journalStructurePersistence = JournalStructureUtil.getPersistence();
        }
        if (journalTemplateLocalService == null) {
            journalTemplateLocalService = JournalTemplateLocalServiceFactory.getImpl();
        }
        if (journalTemplateService == null) {
            journalTemplateService = JournalTemplateServiceFactory.getImpl();
        }
        if (journalTemplatePersistence == null) {
            journalTemplatePersistence = JournalTemplateUtil.getPersistence();
        }
    }

    protected JournalArticleLocalService journalArticleLocalService;

    protected JournalArticleService journalArticleService;

    protected JournalArticlePersistence journalArticlePersistence;

    protected JournalArticleImageLocalService journalArticleImageLocalService;

    protected JournalArticleImagePersistence journalArticleImagePersistence;

    protected JournalArticleResourceLocalService journalArticleResourceLocalService;

    protected JournalArticleResourcePersistence journalArticleResourcePersistence;

    protected JournalContentSearchLocalService journalContentSearchLocalService;

    protected JournalContentSearchPersistence journalContentSearchPersistence;

    protected JournalStructurePersistence journalStructurePersistence;

    protected JournalTemplateLocalService journalTemplateLocalService;

    protected JournalTemplateService journalTemplateService;

    protected JournalTemplatePersistence journalTemplatePersistence;
}
