package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */
public class ForumMessageData extends MessageData {

    private final Forum forum;

    public ForumMessageData(Forum forum) {
        super(-1, -1, -1, forum.getForumId(), -1, forum.getForumName(), forum.getShortForumName(), -1, -1, true, null, false, 0, false);
        this.forum = forum;
    }

    public Forum getForum() {
        return forum;
    }
}
