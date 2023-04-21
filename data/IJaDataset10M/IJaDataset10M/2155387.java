package com.j2biz.compote.plugins.news.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class CommentForm extends ActionForm {

    private String id;

    private String messageId;

    private String title;

    private String comment;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (StringUtils.isEmpty(title)) errors.add("title", new ActionError("error.title.required"));
        if (StringUtils.isEmpty(messageId)) errors.add("messageId", new ActionError("error.messageId.required"));
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setId(null);
        setMessageId(null);
        setTitle(null);
        setComment(null);
    }

    /**
     * @return Returns the comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     *            The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the messageId.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     *            The messageId to set.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
