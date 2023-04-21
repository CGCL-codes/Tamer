package org.spirit.bean.impl;

import java.io.Serializable;
import java.util.List;
import org.spirit.bean.impl.base.BotListBeanBase;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListGroupLinks extends BotListBeanBase implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1477332735655043652L;

    private String urlDescription;

    private String mainUrl;

    private Long views;

    private String keywords;

    private Long groupId;

    private String urlTitle;

    private String fullName;

    /**
	 * @return the groupId
	 */
    public Long getGroupId() {
        return groupId;
    }

    /**
	 * @param groupId the groupId to set
	 */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
	 * @return the keywords
	 */
    public String getKeywords() {
        return keywords;
    }

    /**
	 * @param keywords the keywords to set
	 */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
	 * @return the mainUrl
	 */
    public String getMainUrl() {
        return mainUrl;
    }

    /**
	 * @param mainUrl the mainUrl to set
	 */
    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    /**
	 * @return the urlDescription
	 */
    public String getUrlDescription() {
        return urlDescription;
    }

    /**
	 * @param urlDescription the urlDescription to set
	 */
    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    /**
	 * @return the urlTitle
	 */
    public String getUrlTitle() {
        return urlTitle;
    }

    /**
	 * @param urlTitle the urlTitle to set
	 */
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    /**
	 * @return the views
	 */
    public Long getViews() {
        return views;
    }

    /**
	 * @param views the views to set
	 */
    public void setViews(Long views) {
        this.views = views;
    }

    /**
	 * @return the fullName
	 */
    public String getFullName() {
        return fullName;
    }

    /**
	 * @param fullName the fullName to set
	 */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
