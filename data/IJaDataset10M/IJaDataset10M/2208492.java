package cn.netjava.jojo;

/**
 * ������
 * @author Administrator
 *
 */
public class ReplayMsg {

    private int id;

    private String replayMsgContent;

    private String replayMsgTime;

    private UserInfo user;

    private BlogInfo blog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReplayMsgContent() {
        return replayMsgContent;
    }

    public void setReplayMsgContent(String replayMsgContent) {
        this.replayMsgContent = replayMsgContent;
    }

    public String getReplayMsgTime() {
        return replayMsgTime;
    }

    public void setReplayMsgTime(String replatMsgTime) {
        this.replayMsgTime = replatMsgTime;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public BlogInfo getBlog() {
        return blog;
    }

    public void setBlog(BlogInfo blog) {
        this.blog = blog;
    }
}
