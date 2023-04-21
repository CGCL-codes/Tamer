package ru.goldenforests.forum.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import org.apache.log4j.Logger;
import ru.goldenforests.forum.ForumPost;
import ru.goldenforests.forum.ForumThread;
import ru.goldenforests.forum.ForumUser;
import ru.goldenforests.forum.beans.ConstForumPost;
import ru.goldenforests.forum.engine.NewStatusChecker;

/**
 * @author svv
 *
 */
public class ThreadPostsLoader {

    private static final Logger logger = Logger.getLogger(ThreadPostsLoader.class);

    public ConstForumPost loadPosts(Session session, ForumThread thread, ForumUser forumUser, NewStatusChecker newStatusChecker) throws HibernateException {
        List allPosts = loadRawPosts(session, thread);
        if (thread.getPosts().size() != allPosts.size()) logger.debug("YYY \"" + forumUser.getLogin() + "\" thread[" + thread.getId() + "] getPosts().size()=" + thread.getPosts().size() + " stats.totalPosts=" + allPosts.size());
        ForumPost root = findPostWithId(allPosts, thread.getId());
        return toConst(root, forumUser, allPosts, newStatusChecker);
    }

    private List loadRawPosts(Session session, ForumThread thread) throws HibernateException {
        logger.debug("loading raw posts::");
        SimpleTimer t = new SimpleTimer();
        List list = session.createCriteria(ForumPost.class).add(Expression.eq("thread", thread)).setFetchMode("author", FetchMode.EAGER).setCacheable(true).list();
        logger.debug("   loaded raw posts -- " + t.check() + "ms");
        return new ArrayList(new HashSet(list));
    }

    private ForumPost findPostWithId(Collection posts, long id) {
        for (Iterator i = posts.iterator(); i.hasNext(); ) {
            ForumPost post = (ForumPost) i.next();
            if (post.getId() == id) return post;
        }
        return null;
    }

    private List findPostsWithParentId(List posts, long parentId) {
        Set followUps = new TreeSet();
        for (Iterator i = posts.iterator(); i.hasNext(); ) {
            ForumPost post = (ForumPost) i.next();
            if (post.getParent() != null && post.getParent().getId() == parentId) followUps.add(post);
        }
        return new ArrayList(followUps);
    }

    private ConstForumPost toConst(ForumPost forumPost, ForumUser forumUser, List allPosts, NewStatusChecker newStatusChecker) throws HibernateException {
        List followUps = findPostsWithParentId(allPosts, forumPost.getId());
        List constFollowUps = new ArrayList();
        for (Iterator i = followUps.iterator(); i.hasNext(); ) {
            ForumPost followUp = (ForumPost) i.next();
            constFollowUps.add(toConst(followUp, forumUser, allPosts, newStatusChecker));
        }
        ConstForumPost constPost = new ConstForumPost(forumUser, forumPost, newStatusChecker);
        constPost.setConstFollowUps(constFollowUps);
        return constPost;
    }
}
