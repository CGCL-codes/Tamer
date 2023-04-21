package net.taylor.tracker.entity;

import javax.persistence.Entity;
import javax.persistence.PreRemove;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.search.annotations.Indexed;

/**
 * @todo add comment for javadoc
 *
 * @author jgilbert
 * @generated
 */
@Entity
@Indexed(index = "taylor")
public class Topic extends Artifact {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public Topic() {
    }

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public Long getReplies() {
        return replies;
    }

    /** @generated */
    public void setReplies(final Long replies) {
        this.replies = replies;
    }

    /** @generated */
    private Long replies = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public Long getViews() {
        return views;
    }

    /** @generated */
    public void setViews(final Long views) {
        this.views = views;
    }

    /** @generated */
    private Long views = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public Boolean getSticky() {
        return sticky;
    }

    /** @generated */
    public void setSticky(final Boolean sticky) {
        this.sticky = sticky;
    }

    /** @generated */
    private Boolean sticky = null;

    /**
	 * ------------------------------------------------------------------------
	 * @todo add comment for javadoc
	 * ------------------------------------------------------------------------
	 * @generated
	 */
    public Boolean getLastPost() {
        return lastPost;
    }

    /** @generated */
    public void setLastPost(final Boolean lastPost) {
        this.lastPost = lastPost;
    }

    /** @generated */
    private Boolean lastPost = null;

    /** @generated */
    @PreRemove
    public void preRemove() {
        removeAllLinkedTo();
        removeAllTags();
        removeAllWatchers();
    }

    /** @generated */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /** @generated */
    public Topic deepClone() throws Exception {
        Topic clone = (Topic) super.deepClone();
        return clone;
    }
}
