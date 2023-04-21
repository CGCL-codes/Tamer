package com.faceye.components.navigation.dao.model;

import java.util.Date;
import com.faceye.core.dao.hibernate.model.BaseObject;

/**
 * 
 * @author：宋海鹏
 * @Connection:E_mail:ecsun@sohu.com/myecsun@hotmail.com QQ:82676683
 * @Copy Right:www.faceye.com
 * @System:www.faceye.com网络支持系统
 * @Create Time:2007-9-22
 * @Package com.faceye.components.navigation.dao.model.FeedSubscribe.java
 * @Description:feed订阅
 */
public class FeedSubscribe extends BaseObject {

    private UserResourceCategory userResourceCategory;

    private Feed feed;

    private Date subscribeTime;

    private Integer feedOrder = new Integer(0);

    public Integer getFeedOrder() {
        return feedOrder;
    }

    public void setFeedOrder(Integer feedOrder) {
        this.feedOrder = feedOrder;
    }

    public UserResourceCategory getUserResourceCategory() {
        return userResourceCategory;
    }

    public void setUserResourceCategory(UserResourceCategory userResourceCategory) {
        this.userResourceCategory = userResourceCategory;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
}
