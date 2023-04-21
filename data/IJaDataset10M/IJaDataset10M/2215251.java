package org.encuestame.utils.web;

import java.io.Serializable;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Unit Comment bean.
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since August 14, 2011
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentBean implements Serializable {

    /** Serial. **/
    private static final long serialVersionUID = 3553112539868437033L;

    /** **/
    @JsonProperty(value = "id")
    private Long commentId;

    /** **/
    @JsonProperty(value = "comment")
    private String comment;

    /** **/
    @JsonProperty(value = "created_at")
    private Date createdAt;

    /** **/
    @JsonProperty(value = "likeVote")
    private Long likeVote;

    /** **/
    @JsonProperty(value = "dislike_vote")
    private Long dislikeVote;

    /** **/
    @JsonProperty(value = "item_id")
    private Long id;

    /** Type of Comment */
    @JsonProperty(value = "type")
    private String type;

    /** **/
    @JsonIgnore
    @JsonProperty(value = "uid")
    private Long userAccountId;

    /**
     * Comment url.
     */
    @JsonProperty(value = "url")
    private String commentUrl;

    /**
     * Commented By.
     */
    @JsonProperty(value = "commented_by")
    private String commentedBy;

    /**
     * Commented By.
     */
    @JsonProperty(value = "commented_username")
    private String commentedByUsername;

    /** **/
    @JsonProperty(value = "parent_id")
    private Long parentId;

    /**
     * @return the commentId
     */
    @JsonIgnore
    public Long getCommentId() {
        return commentId;
    }

    /**
     * @param commentId the commentId to set
     */
    public void setCommentId(final Long commentId) {
        this.commentId = commentId;
    }

    /**
     * @return the comment
     */
    @JsonIgnore
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * @return the createdAt
     */
    @JsonIgnore
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the likeVote
     */
    @JsonIgnore
    public Long getLikeVote() {
        return likeVote;
    }

    /**
     * @param likeVote the likeVote to set
     */
    public void setLikeVote(final Long likeVote) {
        this.likeVote = likeVote;
    }

    /**
     * @return the unlikeVote
     */
    @JsonIgnore
    public Long getDislikeVote() {
        return dislikeVote;
    }

    /**
     * @param unlikeVote the unlikeVote to set
     */
    public void setDislikeVote(final Long dislikeVote) {
        this.dislikeVote = dislikeVote;
    }

    /**
     * @return the userAccountId
     */
    @JsonIgnore
    public Long getUserAccountId() {
        return userAccountId;
    }

    /**
     * @param userAccountId the userAccountId to set
     */
    public void setUserAccountId(final Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    /**
     * @return the parentId
     */
    @JsonIgnore
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(final Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the id
     */
    @JsonIgnore
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    @JsonIgnore
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return the commentedBy
     */
    @JsonIgnore
    public String getCommentedBy() {
        return commentedBy;
    }

    /**
     * @param commentedBy the commentedBy to set
     */
    public void setCommentedBy(final String commentedBy) {
        this.commentedBy = commentedBy;
    }

    /**
     * @return the commentedByUsername
     */
    @JsonIgnore
    public String getCommentedByUsername() {
        return commentedByUsername;
    }

    /**
     * @return the commentUrl
     */
    @JsonIgnore
    public String getCommentUrl() {
        return commentUrl;
    }

    /**
     * @param commentUrl the commentUrl to set
     */
    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    /**
     * @param commentedByUsername the commentedByUsername to set
     */
    public void setCommentedByUsername(final String commentedByUsername) {
        this.commentedByUsername = commentedByUsername;
    }
}
