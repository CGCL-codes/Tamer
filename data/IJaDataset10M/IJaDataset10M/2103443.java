package org.openuss.discussion;

import java.util.List;

/**
 * @author Ingo Dueppe
 * @author Sebastian Roekens
 */
public interface DiscussionService {

    public void createTopic(PostInfo post, ForumInfo forum);

    public void deleteTopic(TopicInfo topic);

    public void addPost(PostInfo post, TopicInfo topic);

    public void deletePost(PostInfo post);

    public void updatePost(PostInfo post);

    public PostInfo getPost(PostInfo post);

    public List getPosts(TopicInfo topic);

    public TopicInfo getTopic(TopicInfo topic);

    public List getTopics(ForumInfo forum);

    public void addTopicWatch(TopicInfo topic);

    public void addForumWatch(ForumInfo forum);

    public void removeTopicWatch(TopicInfo topic);

    public void removeForumWatch(ForumInfo forum);

    public void addAttachment(PostInfo post, org.openuss.documents.FileInfo file);

    public void removeAttachment(PostInfo post, org.openuss.documents.FileInfo file);

    public void addForum(ForumInfo forum);

    public ForumInfo getForum(org.openuss.foundation.NamedDomainObject domainObject);

    public void changeEditState(ForumInfo forum);

    public void changeEditState(TopicInfo topic);

    public void addHit(TopicInfo topic);

    public List getAttachments(PostInfo post);

    public boolean watchesForum(ForumInfo forum);

    public boolean watchesTopic(TopicInfo topic);

    public void removeUserFromDiscussions(org.openuss.security.User user);
}
