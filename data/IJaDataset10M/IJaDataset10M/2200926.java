package com.liferay.portlet.tags.service.base;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portlet.tags.service.TagsAssetLocalService;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceFactory;
import com.liferay.portlet.tags.service.TagsAssetService;
import com.liferay.portlet.tags.service.TagsAssetServiceFactory;
import com.liferay.portlet.tags.service.TagsEntryLocalService;
import com.liferay.portlet.tags.service.TagsEntryLocalServiceFactory;
import com.liferay.portlet.tags.service.TagsEntryService;
import com.liferay.portlet.tags.service.TagsEntryServiceFactory;
import com.liferay.portlet.tags.service.TagsPropertyLocalService;
import com.liferay.portlet.tags.service.TagsPropertyLocalServiceFactory;
import com.liferay.portlet.tags.service.TagsPropertyService;
import com.liferay.portlet.tags.service.TagsPropertyServiceFactory;
import com.liferay.portlet.tags.service.TagsSourceLocalService;
import com.liferay.portlet.tags.service.persistence.TagsAssetPersistence;
import com.liferay.portlet.tags.service.persistence.TagsAssetUtil;
import com.liferay.portlet.tags.service.persistence.TagsEntryPersistence;
import com.liferay.portlet.tags.service.persistence.TagsEntryUtil;
import com.liferay.portlet.tags.service.persistence.TagsPropertyPersistence;
import com.liferay.portlet.tags.service.persistence.TagsPropertyUtil;
import com.liferay.portlet.tags.service.persistence.TagsSourcePersistence;
import com.liferay.portlet.tags.service.persistence.TagsSourceUtil;
import org.springframework.beans.factory.InitializingBean;
import java.util.List;

/**
 * <a href="TagsSourceLocalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class TagsSourceLocalServiceBaseImpl implements TagsSourceLocalService, InitializingBean {

    public List dynamicQuery(DynamicQueryInitializer queryInitializer) throws SystemException {
        return TagsSourceUtil.findWithDynamicQuery(queryInitializer);
    }

    public List dynamicQuery(DynamicQueryInitializer queryInitializer, int begin, int end) throws SystemException {
        return TagsSourceUtil.findWithDynamicQuery(queryInitializer, begin, end);
    }

    public TagsAssetLocalService getTagsAssetLocalService() {
        return tagsAssetLocalService;
    }

    public void setTagsAssetLocalService(TagsAssetLocalService tagsAssetLocalService) {
        this.tagsAssetLocalService = tagsAssetLocalService;
    }

    public TagsAssetService getTagsAssetService() {
        return tagsAssetService;
    }

    public void setTagsAssetService(TagsAssetService tagsAssetService) {
        this.tagsAssetService = tagsAssetService;
    }

    public TagsAssetPersistence getTagsAssetPersistence() {
        return tagsAssetPersistence;
    }

    public void setTagsAssetPersistence(TagsAssetPersistence tagsAssetPersistence) {
        this.tagsAssetPersistence = tagsAssetPersistence;
    }

    public TagsEntryLocalService getTagsEntryLocalService() {
        return tagsEntryLocalService;
    }

    public void setTagsEntryLocalService(TagsEntryLocalService tagsEntryLocalService) {
        this.tagsEntryLocalService = tagsEntryLocalService;
    }

    public TagsEntryService getTagsEntryService() {
        return tagsEntryService;
    }

    public void setTagsEntryService(TagsEntryService tagsEntryService) {
        this.tagsEntryService = tagsEntryService;
    }

    public TagsEntryPersistence getTagsEntryPersistence() {
        return tagsEntryPersistence;
    }

    public void setTagsEntryPersistence(TagsEntryPersistence tagsEntryPersistence) {
        this.tagsEntryPersistence = tagsEntryPersistence;
    }

    public TagsPropertyLocalService getTagsPropertyLocalService() {
        return tagsPropertyLocalService;
    }

    public void setTagsPropertyLocalService(TagsPropertyLocalService tagsPropertyLocalService) {
        this.tagsPropertyLocalService = tagsPropertyLocalService;
    }

    public TagsPropertyService getTagsPropertyService() {
        return tagsPropertyService;
    }

    public void setTagsPropertyService(TagsPropertyService tagsPropertyService) {
        this.tagsPropertyService = tagsPropertyService;
    }

    public TagsPropertyPersistence getTagsPropertyPersistence() {
        return tagsPropertyPersistence;
    }

    public void setTagsPropertyPersistence(TagsPropertyPersistence tagsPropertyPersistence) {
        this.tagsPropertyPersistence = tagsPropertyPersistence;
    }

    public TagsSourcePersistence getTagsSourcePersistence() {
        return tagsSourcePersistence;
    }

    public void setTagsSourcePersistence(TagsSourcePersistence tagsSourcePersistence) {
        this.tagsSourcePersistence = tagsSourcePersistence;
    }

    public void afterPropertiesSet() {
        if (tagsAssetLocalService == null) {
            tagsAssetLocalService = TagsAssetLocalServiceFactory.getImpl();
        }
        if (tagsAssetService == null) {
            tagsAssetService = TagsAssetServiceFactory.getImpl();
        }
        if (tagsAssetPersistence == null) {
            tagsAssetPersistence = TagsAssetUtil.getPersistence();
        }
        if (tagsEntryLocalService == null) {
            tagsEntryLocalService = TagsEntryLocalServiceFactory.getImpl();
        }
        if (tagsEntryService == null) {
            tagsEntryService = TagsEntryServiceFactory.getImpl();
        }
        if (tagsEntryPersistence == null) {
            tagsEntryPersistence = TagsEntryUtil.getPersistence();
        }
        if (tagsPropertyLocalService == null) {
            tagsPropertyLocalService = TagsPropertyLocalServiceFactory.getImpl();
        }
        if (tagsPropertyService == null) {
            tagsPropertyService = TagsPropertyServiceFactory.getImpl();
        }
        if (tagsPropertyPersistence == null) {
            tagsPropertyPersistence = TagsPropertyUtil.getPersistence();
        }
        if (tagsSourcePersistence == null) {
            tagsSourcePersistence = TagsSourceUtil.getPersistence();
        }
    }

    protected TagsAssetLocalService tagsAssetLocalService;

    protected TagsAssetService tagsAssetService;

    protected TagsAssetPersistence tagsAssetPersistence;

    protected TagsEntryLocalService tagsEntryLocalService;

    protected TagsEntryService tagsEntryService;

    protected TagsEntryPersistence tagsEntryPersistence;

    protected TagsPropertyLocalService tagsPropertyLocalService;

    protected TagsPropertyService tagsPropertyService;

    protected TagsPropertyPersistence tagsPropertyPersistence;

    protected TagsSourcePersistence tagsSourcePersistence;
}
