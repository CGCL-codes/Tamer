package com.bird.action;

import java.util.List;
import com.bird.domain.TopicBean;
import com.bird.domain.UserBean;
import com.bird.service.TopicService;
import com.bird.service.UserService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ������italk��talk��Ϣ
 * @author ��־ǿ
 * 2009-12-12
 */
public class RandomBrowse extends ActionSupport {

    private TopicBean topicBean;

    private UserBean userBean;

    private TopicService topicService;

    private UserService userService;

    private List<TopicBean> topicList;

    public String execute() throws Exception {
        topicList = topicService.getRandomBrowseList();
        for (TopicBean topic : topicList) {
            userBean = new UserBean();
            long userId = topic.getUserId();
            userBean.setUserId(userId);
            userBean = userService.getUserById(userBean);
            if (userBean != null) {
                String photoPath = userBean.getPhotoPath();
                topic.setPhotoPath(photoPath);
            }
        }
        userBean = null;
        return SUCCESS;
    }

    public List<TopicBean> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopicBean> topicList) {
        this.topicList = topicList;
    }

    public TopicBean getTopicBean() {
        return topicBean;
    }

    public void setTopicBean(TopicBean topicBean) {
        this.topicBean = topicBean;
    }

    public TopicService getTopicService() {
        return topicService;
    }

    public void setTopicService(TopicService topicService) {
        this.topicService = topicService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
