package org.openjf.topic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.openjf.exception.BoardException;
import org.openjf.persistence.BoardQuery;
import org.openjf.persistence.Persistence;
import org.openjf.post.Post;
import org.openjf.topic.TopicControl.TopicStatus;
import org.openjf.topic.TopicControl.TopicView;
import org.openjf.userdata.UserTopicData;
import org.openjf.usergroup.User;

public class TopicDao {

    private Persistence persistence;

    public TopicDao(Persistence persistence) {
        this.persistence = persistence;
    }

    public Topic findNE(int topicId) {
        return (Topic) persistence.get(Topic.class, new Integer(topicId));
    }

    public void save(Topic topic) {
        persistence.save(topic);
    }

    public void delete(Topic topic) throws BoardException {
        persistence.delete(topic);
    }

    public void addToDelete(Collection topics) {
        persistence.addObjectsToDelete(topics);
    }

    public void onDelete(UserTopicData userTopicData) {
        persistence.addItemToRemoveFromSet(userTopicData.getId().getTopic().getUsersTopicData(), userTopicData);
    }

    public List getTopics(int forumId, int first, int count) throws BoardException {
        BoardQuery q = persistence.createQuery("from Topic t where t.forum.id = :id order by t.sticky desc, t.lastPost.postTime desc");
        q.setInteger("id", forumId);
        q.setFirstResult(first);
        q.setMaxResults(count);
        return q.list();
    }

    public boolean isUsingUser(User user) {
        BoardQuery q = persistence.createQuery("SELECT count(*) FROM Topic topic WHERE topic.author = :user");
        q.setParameter("user", user);
        Number n = (Number) q.uniqueResult();
        return n.intValue() > 0;
    }

    public TopicStatus calcTopicStats(int topicId) throws BoardException {
        int postCount;
        Timestamp firstPostTime;
        Post lastPost;
        BoardQuery q;
        Object data[];
        q = persistence.createQuery("select count(*), min(p.postTime) from Post p where p.topic.id = :id");
        q.setInteger("id", topicId);
        data = (Object[]) q.uniqueResult();
        postCount = ((Number) data[0]).intValue();
        if (postCount > 0) {
            firstPostTime = new Timestamp(((Date) data[1]).getTime());
        } else {
            firstPostTime = null;
        }
        q = persistence.createQuery("from Post p where p.topic.id = :id ORDER BY p.postTime DESC");
        q.setInteger("id", topicId);
        q.setMaxResults(1);
        List l = q.list();
        if (l.size() > 0) {
            lastPost = (Post) l.get(0);
        } else {
            lastPost = null;
        }
        return new TopicStatus(postCount, firstPostTime, lastPost);
    }

    public List getTopicViews(int forumId, int first, int count) {
        BoardQuery q = persistence.createQuery("SELECT t.id, t.subject, t.viewCount, t.postCount, t.sticky, t.announcement, t.author.nick," + " t.lastPost.id, t.lastPost.author.nick, t.lastPost.postTime" + " FROM Topic t" + " WHERE t.forum.id = :id" + " ORDER BY t.sticky desc, t.lastPost.postTime desc");
        q.setInteger("id", forumId);
        q.setFirstResult(first);
        q.setMaxResults(count);
        List res = new ArrayList();
        List rows = q.list();
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = (Object[]) rows.get(i);
            TopicView view = new TopicView();
            view.setId(((Integer) row[0]).intValue());
            view.setSubject((String) row[1]);
            view.setViewCount(((Integer) row[2]).intValue());
            view.setReplyCount(((Integer) row[3]).intValue() - 1);
            view.setSticky(((Boolean) row[4]).booleanValue());
            view.setAnnouncement(((Boolean) row[5]).booleanValue());
            view.setAuthorNick((String) row[6]);
            view.setLastPostId(((Integer) row[7]).intValue());
            view.setLastPostNick((String) row[8]);
            view.setLastPostTime(new Timestamp(((Timestamp) row[9]).getTime()));
            res.add(view);
        }
        return res;
    }
}
