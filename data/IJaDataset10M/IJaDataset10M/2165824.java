package com.liferay.portlet.imagegallery.service.base;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.counter.service.CounterService;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.bean.InitializingBean;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.service.ImageLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.service.LayoutService;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.ImagePersistence;
import com.liferay.portal.service.persistence.LayoutPersistence;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.service.IGFolderLocalService;
import com.liferay.portlet.imagegallery.service.IGImageLocalService;
import com.liferay.portlet.imagegallery.service.IGImageService;
import com.liferay.portlet.imagegallery.service.persistence.IGFolderPersistence;
import com.liferay.portlet.imagegallery.service.persistence.IGImageFinder;
import com.liferay.portlet.imagegallery.service.persistence.IGImagePersistence;
import com.liferay.portlet.tags.service.TagsEntryLocalService;
import com.liferay.portlet.tags.service.TagsEntryService;
import com.liferay.portlet.tags.service.persistence.TagsEntryPersistence;
import java.util.List;

public abstract class IGFolderLocalServiceBaseImpl implements IGFolderLocalService, InitializingBean {

    protected IGFolderPersistence igFolderPersistence;

    protected IGImageLocalService igImageLocalService;

    protected IGImageService igImageService;

    protected IGImagePersistence igImagePersistence;

    protected IGImageFinder igImageFinder;

    protected CounterLocalService counterLocalService;

    protected CounterService counterService;

    protected ImageLocalService imageLocalService;

    protected ImagePersistence imagePersistence;

    protected LayoutLocalService layoutLocalService;

    protected LayoutService layoutService;

    protected LayoutPersistence layoutPersistence;

    protected ResourceLocalService resourceLocalService;

    protected ResourceService resourceService;

    protected ResourcePersistence resourcePersistence;

    protected UserLocalService userLocalService;

    protected UserService userService;

    protected UserPersistence userPersistence;

    protected TagsEntryLocalService tagsEntryLocalService;

    protected TagsEntryService tagsEntryService;

    protected TagsEntryPersistence tagsEntryPersistence;

    public IGFolder addIGFolder(IGFolder igFolder) throws SystemException {
        igFolder.setNew(true);
        return igFolderPersistence.update(igFolder, false);
    }

    public void deleteIGFolder(long folderId) throws PortalException, SystemException {
        igFolderPersistence.remove(folderId);
    }

    public void deleteIGFolder(IGFolder igFolder) throws SystemException {
        igFolderPersistence.remove(igFolder);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery) throws SystemException {
        return igFolderPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start, int end) throws SystemException {
        return igFolderPersistence.findWithDynamicQuery(dynamicQuery, start, end);
    }

    public IGFolder getIGFolder(long folderId) throws PortalException, SystemException {
        return igFolderPersistence.findByPrimaryKey(folderId);
    }

    public List<IGFolder> getIGFolders(int start, int end) throws SystemException {
        return igFolderPersistence.findAll(start, end);
    }

    public int getIGFoldersCount() throws SystemException {
        return igFolderPersistence.countAll();
    }

    public IGFolder updateIGFolder(IGFolder igFolder) throws SystemException {
        igFolder.setNew(false);
        return igFolderPersistence.update(igFolder, true);
    }

    public IGFolderPersistence getIGFolderPersistence() {
        return igFolderPersistence;
    }

    public void setIGFolderPersistence(IGFolderPersistence igFolderPersistence) {
        this.igFolderPersistence = igFolderPersistence;
    }

    public IGImageLocalService getIGImageLocalService() {
        return igImageLocalService;
    }

    public void setIGImageLocalService(IGImageLocalService igImageLocalService) {
        this.igImageLocalService = igImageLocalService;
    }

    public IGImageService getIGImageService() {
        return igImageService;
    }

    public void setIGImageService(IGImageService igImageService) {
        this.igImageService = igImageService;
    }

    public IGImagePersistence getIGImagePersistence() {
        return igImagePersistence;
    }

    public void setIGImagePersistence(IGImagePersistence igImagePersistence) {
        this.igImagePersistence = igImagePersistence;
    }

    public IGImageFinder getIGImageFinder() {
        return igImageFinder;
    }

    public void setIGImageFinder(IGImageFinder igImageFinder) {
        this.igImageFinder = igImageFinder;
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

    public LayoutLocalService getLayoutLocalService() {
        return layoutLocalService;
    }

    public void setLayoutLocalService(LayoutLocalService layoutLocalService) {
        this.layoutLocalService = layoutLocalService;
    }

    public LayoutService getLayoutService() {
        return layoutService;
    }

    public void setLayoutService(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    public LayoutPersistence getLayoutPersistence() {
        return layoutPersistence;
    }

    public void setLayoutPersistence(LayoutPersistence layoutPersistence) {
        this.layoutPersistence = layoutPersistence;
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

    public void afterPropertiesSet() {
        if (igFolderPersistence == null) {
            igFolderPersistence = (IGFolderPersistence) PortalBeanLocatorUtil.locate(IGFolderPersistence.class.getName() + ".impl");
        }
        if (igImageLocalService == null) {
            igImageLocalService = (IGImageLocalService) PortalBeanLocatorUtil.locate(IGImageLocalService.class.getName() + ".impl");
        }
        if (igImageService == null) {
            igImageService = (IGImageService) PortalBeanLocatorUtil.locate(IGImageService.class.getName() + ".impl");
        }
        if (igImagePersistence == null) {
            igImagePersistence = (IGImagePersistence) PortalBeanLocatorUtil.locate(IGImagePersistence.class.getName() + ".impl");
        }
        if (igImageFinder == null) {
            igImageFinder = (IGImageFinder) PortalBeanLocatorUtil.locate(IGImageFinder.class.getName() + ".impl");
        }
        if (counterLocalService == null) {
            counterLocalService = (CounterLocalService) PortalBeanLocatorUtil.locate(CounterLocalService.class.getName() + ".impl");
        }
        if (counterService == null) {
            counterService = (CounterService) PortalBeanLocatorUtil.locate(CounterService.class.getName() + ".impl");
        }
        if (imageLocalService == null) {
            imageLocalService = (ImageLocalService) PortalBeanLocatorUtil.locate(ImageLocalService.class.getName() + ".impl");
        }
        if (imagePersistence == null) {
            imagePersistence = (ImagePersistence) PortalBeanLocatorUtil.locate(ImagePersistence.class.getName() + ".impl");
        }
        if (layoutLocalService == null) {
            layoutLocalService = (LayoutLocalService) PortalBeanLocatorUtil.locate(LayoutLocalService.class.getName() + ".impl");
        }
        if (layoutService == null) {
            layoutService = (LayoutService) PortalBeanLocatorUtil.locate(LayoutService.class.getName() + ".impl");
        }
        if (layoutPersistence == null) {
            layoutPersistence = (LayoutPersistence) PortalBeanLocatorUtil.locate(LayoutPersistence.class.getName() + ".impl");
        }
        if (resourceLocalService == null) {
            resourceLocalService = (ResourceLocalService) PortalBeanLocatorUtil.locate(ResourceLocalService.class.getName() + ".impl");
        }
        if (resourceService == null) {
            resourceService = (ResourceService) PortalBeanLocatorUtil.locate(ResourceService.class.getName() + ".impl");
        }
        if (resourcePersistence == null) {
            resourcePersistence = (ResourcePersistence) PortalBeanLocatorUtil.locate(ResourcePersistence.class.getName() + ".impl");
        }
        if (userLocalService == null) {
            userLocalService = (UserLocalService) PortalBeanLocatorUtil.locate(UserLocalService.class.getName() + ".impl");
        }
        if (userService == null) {
            userService = (UserService) PortalBeanLocatorUtil.locate(UserService.class.getName() + ".impl");
        }
        if (userPersistence == null) {
            userPersistence = (UserPersistence) PortalBeanLocatorUtil.locate(UserPersistence.class.getName() + ".impl");
        }
        if (tagsEntryLocalService == null) {
            tagsEntryLocalService = (TagsEntryLocalService) PortalBeanLocatorUtil.locate(TagsEntryLocalService.class.getName() + ".impl");
        }
        if (tagsEntryService == null) {
            tagsEntryService = (TagsEntryService) PortalBeanLocatorUtil.locate(TagsEntryService.class.getName() + ".impl");
        }
        if (tagsEntryPersistence == null) {
            tagsEntryPersistence = (TagsEntryPersistence) PortalBeanLocatorUtil.locate(TagsEntryPersistence.class.getName() + ".impl");
        }
    }
}
