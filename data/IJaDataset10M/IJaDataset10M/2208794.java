package org.spirit.form;

import java.util.Calendar;
import org.spirit.form.base.BotListBaseForm;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListForumGroupForm extends BotListBaseForm {

    private Long cityId;

    private String forumName;

    private Calendar createdOn;

    private String keywords;

    private String forumDescr;

    /**
		 * @return the cityId
		 */
    public Long getCityId() {
        return cityId;
    }

    /**
		 * @param cityId the cityId to set
		 */
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    /**
		 * @return the createdOn
		 */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
		 * @param createdOn the createdOn to set
		 */
    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
		 * @return the forumDescr
		 */
    public String getForumDescr() {
        return forumDescr;
    }

    /**
		 * @param forumDescr the forumDescr to set
		 */
    public void setForumDescr(String forumDescr) {
        this.forumDescr = forumDescr;
    }

    /**
		 * @return the forumName
		 */
    public String getForumName() {
        return forumName;
    }

    /**
		 * @param forumName the forumName to set
		 */
    public void setForumName(String forumName) {
        this.forumName = forumName;
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
}
