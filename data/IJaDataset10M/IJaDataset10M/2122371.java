package com.xiaxueqi.entity.book;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CodeGen --powered by Sean
 *
 */
public class BookComment implements Serializable {

    private Integer id;

    private String publisher;

    private Integer bookId;

    private String comment;

    private Date createTime;

    private String deleted;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
