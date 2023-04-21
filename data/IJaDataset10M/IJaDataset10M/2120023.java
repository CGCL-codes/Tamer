package com.liferay.portlet.tasks.service.base;

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
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.service.MBMessageService;
import com.liferay.portlet.messageboards.service.persistence.MBMessageFinder;
import com.liferay.portlet.messageboards.service.persistence.MBMessagePersistence;
import com.liferay.portlet.social.service.SocialActivityLocalService;
import com.liferay.portlet.social.service.persistence.SocialActivityFinder;
import com.liferay.portlet.social.service.persistence.SocialActivityPersistence;
import com.liferay.portlet.tasks.service.TasksProposalLocalService;
import com.liferay.portlet.tasks.service.TasksProposalService;
import com.liferay.portlet.tasks.service.TasksReviewLocalService;
import com.liferay.portlet.tasks.service.TasksReviewService;
import com.liferay.portlet.tasks.service.persistence.TasksProposalFinder;
import com.liferay.portlet.tasks.service.persistence.TasksProposalPersistence;
import com.liferay.portlet.tasks.service.persistence.TasksReviewPersistence;

/**
 * <a href="TasksProposalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class TasksProposalServiceBaseImpl extends PrincipalBean implements TasksProposalService {

    public TasksReviewLocalService getTasksReviewLocalService() {
        return tasksReviewLocalService;
    }

    public void setTasksReviewLocalService(TasksReviewLocalService tasksReviewLocalService) {
        this.tasksReviewLocalService = tasksReviewLocalService;
    }

    public TasksReviewService getTasksReviewService() {
        return tasksReviewService;
    }

    public void setTasksReviewService(TasksReviewService tasksReviewService) {
        this.tasksReviewService = tasksReviewService;
    }

    public TasksReviewPersistence getTasksReviewPersistence() {
        return tasksReviewPersistence;
    }

    public void setTasksReviewPersistence(TasksReviewPersistence tasksReviewPersistence) {
        this.tasksReviewPersistence = tasksReviewPersistence;
    }

    public TasksProposalLocalService getTasksProposalLocalService() {
        return tasksProposalLocalService;
    }

    public void setTasksProposalLocalService(TasksProposalLocalService tasksProposalLocalService) {
        this.tasksProposalLocalService = tasksProposalLocalService;
    }

    public TasksProposalService getTasksProposalService() {
        return tasksProposalService;
    }

    public void setTasksProposalService(TasksProposalService tasksProposalService) {
        this.tasksProposalService = tasksProposalService;
    }

    public TasksProposalPersistence getTasksProposalPersistence() {
        return tasksProposalPersistence;
    }

    public void setTasksProposalPersistence(TasksProposalPersistence tasksProposalPersistence) {
        this.tasksProposalPersistence = tasksProposalPersistence;
    }

    public TasksProposalFinder getTasksProposalFinder() {
        return tasksProposalFinder;
    }

    public void setTasksProposalFinder(TasksProposalFinder tasksProposalFinder) {
        this.tasksProposalFinder = tasksProposalFinder;
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

    public MBMessageLocalService getMBMessageLocalService() {
        return mbMessageLocalService;
    }

    public void setMBMessageLocalService(MBMessageLocalService mbMessageLocalService) {
        this.mbMessageLocalService = mbMessageLocalService;
    }

    public MBMessageService getMBMessageService() {
        return mbMessageService;
    }

    public void setMBMessageService(MBMessageService mbMessageService) {
        this.mbMessageService = mbMessageService;
    }

    public MBMessagePersistence getMBMessagePersistence() {
        return mbMessagePersistence;
    }

    public void setMBMessagePersistence(MBMessagePersistence mbMessagePersistence) {
        this.mbMessagePersistence = mbMessagePersistence;
    }

    public MBMessageFinder getMBMessageFinder() {
        return mbMessageFinder;
    }

    public void setMBMessageFinder(MBMessageFinder mbMessageFinder) {
        this.mbMessageFinder = mbMessageFinder;
    }

    public SocialActivityLocalService getSocialActivityLocalService() {
        return socialActivityLocalService;
    }

    public void setSocialActivityLocalService(SocialActivityLocalService socialActivityLocalService) {
        this.socialActivityLocalService = socialActivityLocalService;
    }

    public SocialActivityPersistence getSocialActivityPersistence() {
        return socialActivityPersistence;
    }

    public void setSocialActivityPersistence(SocialActivityPersistence socialActivityPersistence) {
        this.socialActivityPersistence = socialActivityPersistence;
    }

    public SocialActivityFinder getSocialActivityFinder() {
        return socialActivityFinder;
    }

    public void setSocialActivityFinder(SocialActivityFinder socialActivityFinder) {
        this.socialActivityFinder = socialActivityFinder;
    }

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.TasksReviewLocalService.impl")
    protected TasksReviewLocalService tasksReviewLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.TasksReviewService.impl")
    protected TasksReviewService tasksReviewService;

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.persistence.TasksReviewPersistence.impl")
    protected TasksReviewPersistence tasksReviewPersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.TasksProposalLocalService.impl")
    protected TasksProposalLocalService tasksProposalLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.TasksProposalService.impl")
    protected TasksProposalService tasksProposalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.persistence.TasksProposalPersistence.impl")
    protected TasksProposalPersistence tasksProposalPersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.tasks.service.persistence.TasksProposalFinder.impl")
    protected TasksProposalFinder tasksProposalFinder;

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

    @javax.annotation.Resource(name = "com.liferay.portlet.messageboards.service.MBMessageLocalService.impl")
    protected MBMessageLocalService mbMessageLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.messageboards.service.MBMessageService.impl")
    protected MBMessageService mbMessageService;

    @javax.annotation.Resource(name = "com.liferay.portlet.messageboards.service.persistence.MBMessagePersistence.impl")
    protected MBMessagePersistence mbMessagePersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.messageboards.service.persistence.MBMessageFinder.impl")
    protected MBMessageFinder mbMessageFinder;

    @javax.annotation.Resource(name = "com.liferay.portlet.social.service.SocialActivityLocalService.impl")
    protected SocialActivityLocalService socialActivityLocalService;

    @javax.annotation.Resource(name = "com.liferay.portlet.social.service.persistence.SocialActivityPersistence.impl")
    protected SocialActivityPersistence socialActivityPersistence;

    @javax.annotation.Resource(name = "com.liferay.portlet.social.service.persistence.SocialActivityFinder.impl")
    protected SocialActivityFinder socialActivityFinder;
}
