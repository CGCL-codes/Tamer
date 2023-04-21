package com.gjl.app.view.web.action;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import com.gjl.app.model.business.entity.GuestBookCommentEntity;
import com.gjl.app.model.business.entity.GuestBookItem;
import com.gjl.app.view.web.BaseAction;
import com.gjl.app.view.web.util.WebUtil;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class GuestBookAction extends BaseAction {

    private static final long serialVersionUID = 8047409406799573196L;

    private static final int ITEM_PER_PAGE = 10;

    private String bookTopic;

    private String bookContent;

    private String postName = "匿名用户";

    private String email;

    private String commentId;

    private String comment;

    public String comment() throws Exception {
        try {
            Long guestBookItemId = Long.parseLong(commentId);
            GuestBookCommentEntity guestBookComment = new GuestBookCommentEntity(comment, postName, email);
            EntityManager entityManager = getEntityManager();
            EntityTransaction utx = entityManager.getTransaction();
            utx.begin();
            GuestBookItem item = entityManager.find(GuestBookItem.class, guestBookItemId);
            guestBookComment.setGuestBook(item);
            entityManager.persist(guestBookComment);
            entityManager.flush();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String execute() throws Exception {
        try {
            EntityManager entityManager = getEntityManager();
            if (!StringUtil.isEmptyOrWhitespace(bookTopic) && !StringUtil.isEmptyOrWhitespace(bookContent)) {
                GuestBookItem item = new GuestBookItem(postName, bookTopic, email, bookContent);
                EntityTransaction utx = entityManager.getTransaction();
                utx.begin();
                entityManager.persist(item);
                entityManager.flush();
                utx.commit();
            }
            Query bookQuery = entityManager.createQuery("SELECT item FROM " + GuestBookItem.class.getCanonicalName() + " item ORDER BY item.insertDate DESC");
            @SuppressWarnings("unchecked") List<GuestBookItem> result = (List<GuestBookItem>) bookQuery.getResultList();
            if (result != null && result.size() > 0) {
                WebUtil.paginate("guestbook.action", result, ITEM_PER_PAGE);
            }
            return super.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        if (!StringUtil.isEmptyOrWhitespace(postName)) {
            this.postName = postName;
        }
    }

    public String getBookTopic() {
        return bookTopic;
    }

    public void setBookTopic(String bookTopic) {
        this.bookTopic = bookTopic;
    }

    public String getBookContent() {
        return bookContent;
    }

    public void setBookContent(String bookContent) {
        this.bookContent = bookContent;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
