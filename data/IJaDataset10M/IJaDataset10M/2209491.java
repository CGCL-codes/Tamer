package lichen.internal.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import lichen.entities.forum.Forum;
import lichen.entities.forum.ForumImpl;
import lichen.entities.post.AbstractPost;
import lichen.entities.post.Comment;
import lichen.entities.post.Post;
import lichen.entities.post.Topic;
import lichen.services.PostService;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对贴子进行处理的服务类
 * @author <a href="mailto:jun.tsai@gmail.com">Jun Tsai</a>
 * @version $Revision: 222 $
 * @since 0.0.1
 */
@Transactional
public class PostServiceImpl implements PostService {

    public static final String UPDATE_FORUM_TOPIC_NUM_HQL = "update " + ForumImpl.class.getName() + " f set f." + ForumImpl.TOPIC_NUM_PRO_NAME + "=f." + ForumImpl.TOPIC_NUM_PRO_NAME + "+1 where f." + ForumImpl.LEFT_PRO_NAME + "<=? and f." + ForumImpl.RIGHT_PRO_NAME + ">=?";

    private static final String UPDATE_FORUM_COMMENT_NUM_HQL = "update " + ForumImpl.class.getName() + " f set f." + ForumImpl.COMMENT_NUM_PRO_NAME + "=f." + ForumImpl.COMMENT_NUM_PRO_NAME + "+1 where f." + ForumImpl.LEFT_PRO_NAME + "<=? and f." + ForumImpl.RIGHT_PRO_NAME + ">=?";

    private static final String UPDATE_TOPIC_COMMENT_NUMN_HQL = "update " + Topic.class.getName() + " t set t." + Topic.COMMENT_NUM_PRO_NAME + "=t." + Topic.COMMENT_NUM_PRO_NAME + "+1 where t.id=?";

    private static final String QUERY_PARENT_COUNT_HQL = "select count(*) from " + AbstractPost.class.getName() + " p where p." + AbstractPost.PARENT_NODE_PRO_NAME + "=? ";

    private static final String QUERY_FORUM_TOPIC_HQL = "from " + Topic.class.getName() + " p where p." + Topic.FORUM_PRO_NAME + "=? order by p." + Topic.CREATE_DATE_PRO_NAME + " desc";

    private static final String QUERY_FORUM_TOPIC_COUNT_HQL = "select count(id) from " + Topic.class.getName() + " p where p." + Topic.FORUM_PRO_NAME + "=?";

    private static final String QUERY_CHILD_FORUMS_HQL = "from " + ForumImpl.class.getName() + " f where f." + ForumImpl.LEFT_PRO_NAME + ">? and f." + ForumImpl.RIGHT_PRO_NAME + "<? and f." + ForumImpl.DEPTH_PRO_NAME + "=?";

    private HibernateTemplate _template;

    /**
	 * 通过给定的HibernateSession来构造Service.
	 * @param template hibernate template
	 */
    public PostServiceImpl(HibernateTemplate template) {
        _template = template;
    }

    /**
	 * @see lichen.services.PostService#saveComment(lichen.entities.post.Topic, lichen.entities.post.Post, lichen.entities.post.Comment)
	 */
    @Transactional
    public void saveComment(Topic topic, Post parentPost, Comment comment) {
        Forum forum = parentPost.getForum();
        List list = _template.find(QUERY_PARENT_COUNT_HQL, new Object[] { parentPost });
        long num = (Long) list.iterator().next();
        StringBuffer sb = new StringBuffer();
        sb.append(parentPost.getThread());
        sb.setLength(sb.length() - 1);
        sb.append(".");
        sb.append(int2vancode(num + 1));
        sb.append("/");
        comment.setThread(sb.toString());
        comment.setParentTopic(topic);
        comment.setParentNode(parentPost);
        comment.setForum(forum);
        comment.setCreateDate(new Date().getTime());
        _template.saveOrUpdate(comment);
        _template.bulkUpdate(UPDATE_TOPIC_COMMENT_NUMN_HQL, topic.getId());
        this.updateForumPostNumByHQL(UPDATE_FORUM_COMMENT_NUM_HQL, forum);
    }

    private void updateForumPostNumByHQL(String hql, Forum forum) {
        _template.bulkUpdate(hql, new Object[] { forum.getLeft(), forum.getRight() });
    }

    /**
	 * @see lichen.services.PostService#increamViewNum(lichen.entities.post.Topic)
	 */
    public void increamViewNum(Topic topic) {
        topic.setViewNum(topic.getViewNum() + 1);
        _template.saveOrUpdate(topic);
    }

    /**
	 * Generate vancode.
	 *
	 * Consists of a leading character indicating length, followed by N digits
	 * with a numerical value in base 36. Vancodes can be sorted as strings
	 * without messing up numerical order.
	 *
	 * It goes:
	 * 10, 11, 12, ..., 1y, 1z,
	 * 210, 211, ... , 2zy, 2zz,
	 * 3100, 3101, ..., 3zzy, 3zzz,
	 * 41000, 41001, ...
	 */
    public static String int2vancode(long i) {
        String num = Long.toString(i, 36);
        int length = num.length();
        return Integer.toString(length, 36) + num;
    }

    /**
	 * Decode vancode back to an integer.
	 */
    public static long vancode2int(String thread) {
        return Long.parseLong(thread.substring(1), 36);
    }

    /**
	 * 得到当前论坛的线程集合. 
	 * @return 当前论坛的线程集合.
	 */
    public List getTopics(final Forum forum, final int nFirst, final int nPageSize) {
        return (List) _template.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(QUERY_FORUM_TOPIC_HQL);
                query.setParameter(0, forum);
                query.setFirstResult(nFirst);
                query.setMaxResults(nPageSize);
                return query.list();
            }
        });
    }

    /**
	 * 得到当前论坛的Topics的数量
	 * @param forum
	 * @return
	 * @since 0.0.3
	 */
    public long getTopicCount(Forum forum) {
        List list = _template.find(QUERY_FORUM_TOPIC_COUNT_HQL, forum);
        long rowCount = (Long) list.get(0);
        return rowCount;
    }

    public List fetchChildForums(Object[] objs) {
        return _template.find(QUERY_CHILD_FORUMS_HQL, objs);
    }
}
