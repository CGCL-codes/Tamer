package com.marslei.form;

import java.util.Date;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class TopicForm {

    /**
	 * 主题编号
	 */
    private long topicId;

    /**
	 * 版块编号
	 */
    private long boardId;

    /**
	 * 版块名称
	 */
    private String boardName;

    /**
	 * 主题
	 */
    private String title;

    /**
	 * 内容
	 */
    private Text content;

    /**
	 * 作者
	 */
    private User author;

    /**
	 * 发布日期
	 */
    private Date publishTime;

    /**
	 * 修改日期
	 */
    private Date modifyTime;

    /**
	 * 可编辑
	 */
    private boolean readOnly;

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
