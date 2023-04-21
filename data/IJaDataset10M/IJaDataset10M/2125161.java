package ces.platform.bbs;

public interface ForumMessageContent {

    public String getContent();

    public void setContent(String content);

    public ces.platform.bbs.ForumMessage getParent();

    public void setParent(ces.platform.bbs.ForumMessage parent);
}
