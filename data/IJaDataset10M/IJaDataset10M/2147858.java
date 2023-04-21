package net.jforum.util.mail;

import java.text.MessageFormat;
import java.util.List;
import net.jforum.api.integration.mail.pop.MessageId;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.view.forum.common.PostCommon;
import net.jforum.view.forum.common.ViewCommon;
import freemarker.template.SimpleHash;

/**
 * Notify users of replies to existing topics
 * @author Rafael Steil
 * @version $Id: TopicReplySpammer.java,v 1.5 2007/08/20 19:35:51 rafaelsteil Exp $
 */
public class TopicReplySpammer extends Spammer {

    /**
	 * Creates a new instance with a message's contents send
	 * @param topic the topic we are replying to 
	 * @param post the post instance, with the message's contents. If null,
	 * only a notification will be sent
	 * @param users list of users who'll be notified
	 */
    public TopicReplySpammer(Topic topic, Post post, List users) {
        StringBuffer page = new StringBuffer();
        int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
        if (topic.getTotalReplies() >= postsPerPage) {
            page.append(((topic.getTotalReplies() / postsPerPage)) * postsPerPage).append('/');
        }
        String forumLink = ViewCommon.getForumLink();
        String path = this.messageLink(topic, page, forumLink);
        String unwatch = this.unwatchLink(topic, forumLink);
        SimpleHash params = new SimpleHash();
        params.put("topic", topic);
        params.put("path", path);
        params.put("forumLink", forumLink);
        params.put("unwatch", unwatch);
        if (post != null) {
            this.setMessageId(MessageId.buildMessageId(post.getId(), topic.getId(), topic.getForumId()));
            post = PostCommon.preparePostForDisplay(post);
            params.put("message", post.getText());
        }
        this.setUsers(users);
        if (topic.getFirstPostId() != post.getId()) {
            this.setInReplyTo(MessageId.buildInReplyTo(topic));
        }
        this.setTemplateParams(params);
        String subject = SystemGlobals.getValue(ConfigKeys.MAIL_NEW_ANSWER_SUBJECT);
        this.prepareMessage(MessageFormat.format(subject, new Object[] { topic.getTitle() }), SystemGlobals.getValue(ConfigKeys.MAIL_NEW_ANSWER_MESSAGE_FILE));
    }

    /**
	 * Creates the "unwatch" link for the current topic
	 * @param topic the topic
	 * @param forumLink the forum's link
	 * @return the unwath link
	 */
    private String unwatchLink(Topic topic, String forumLink) {
        return new StringBuffer(128).append(forumLink).append("posts/unwatch/").append(topic.getId()).append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)).toString();
    }

    /**
	 * Creates the link to read the message online
	 * @param topic the topic
	 * @param page the current topic's page
	 * @param forumLink the forum's link
	 * @return the link to the message
	 */
    private String messageLink(Topic topic, StringBuffer page, String forumLink) {
        return new StringBuffer(128).append(forumLink).append("posts/list/").append(page.toString()).append(topic.getId()).append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)).append('#').append(topic.getLastPostId()).toString();
    }
}
