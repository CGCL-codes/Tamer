package ru.goldenforests.forum.faces;

import java.security.AccessControlException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.JDBCException;
import net.sf.hibernate.Query;
import org.apache.log4j.Logger;
import ru.goldenforests.forum.ForumPermission;
import ru.goldenforests.forum.ForumPost;
import ru.goldenforests.forum.ForumThread;
import ru.goldenforests.forum.ForumUser;
import ru.goldenforests.forum.ForumUserForumSettings;
import ru.goldenforests.forum.ForumUserThreadSettings;
import ru.goldenforests.forum.User;
import ru.goldenforests.forum.beans.ConstForumInfo;
import ru.goldenforests.forum.beans.ConstForumPost;
import ru.goldenforests.forum.beans.ConstForumThread;
import ru.goldenforests.forum.dao.impl.CachingUserSettingsDAO;
import ru.goldenforests.forum.dao.impl.QueryUserSettingsDAO;
import ru.goldenforests.forum.dao.impl.SimplePostExplicitlySeenDAO;
import ru.goldenforests.forum.dao.impl.SimpleUserSettingsDAO;
import ru.goldenforests.forum.engine.MultipleThreadsFetcher;
import ru.goldenforests.forum.engine.NewStatusChecker;
import ru.goldenforests.forum.engine.NewStatusCheckerImpl;
import ru.goldenforests.forum.engine.TooOldCalculator;
import ru.goldenforests.forum.engine.UsersManager;
import ru.goldenforests.forum.util.SimpleTimer;

public class ReadFace extends AbstractForumFace {

    private static final Logger logger = Logger.getLogger(ReadFace.class);

    public static final long REQUIRED_PERMISSIONS = ForumPermission.READ;

    private static final boolean ENABLE_CATCHUP = true;

    private ForumUser forumUser;

    public void init() {
        try {
            User u = super.getUserSession().getUser();
            if (super.getSession() != null) this.forumUser = UsersManager.getUserByLogin(super.getSession(), u.getLogin());
        } catch (HibernateException th) {
            logger.debug(th.getMessage(), th);
            throw new RuntimeException(th);
        }
    }

    public List getThreads(int first, int total, boolean slow) throws HibernateException {
        MultipleThreadsFetcher fetcher = new MultipleThreadsFetcher();
        fetcher.setSession(super.getSession());
        fetcher.setForumInfo(super.getForumInfo());
        fetcher.setUser(this.forumUser);
        return slow ? fetcher.fetchThreads_slow(first, total) : fetcher.fetchThreads(first, total);
    }

    public ConstForumThread getThread(long tid) throws HibernateException, JDBCException {
        TooOldCalculator tooOldCalculator = new TooOldCalculator(new CachingUserSettingsDAO(new QueryUserSettingsDAO(super.getSession(), this.forumUser)));
        return new ConstForumThread(super.getSession(), this.forumUser, getForumThread(tid), true, tooOldCalculator);
    }

    public ConstForumPost getPost(long pid) throws HibernateException {
        NewStatusChecker newStatusChecker = new NewStatusCheckerImpl(new TooOldCalculator(new QueryUserSettingsDAO(super.getSession(), this.forumUser)), new SimplePostExplicitlySeenDAO(this.forumUser));
        return new ConstForumPost(this.forumUser, getForumPost(pid), newStatusChecker);
    }

    /** TODO rewrite using only queries */
    public void markForumAsSeen() throws JDBCException, HibernateException {
        Timestamp now = new Timestamp(new Date().getTime());
        ForumUserForumSettings userForumSettings = (ForumUserForumSettings) super.getForumInfo().getForumsSettings().get(this.forumUser);
        if (userForumSettings == null) {
            logger.debug("// u=" + this.forumUser.getLogin() + " f=" + super.getForumInfo().getName() + " -- first catch up");
            userForumSettings = new ForumUserForumSettings();
            super.getForumInfo().getForumsSettings().put(this.forumUser, userForumSettings);
            super.getSession().save(userForumSettings);
        }
        if (ENABLE_CATCHUP) userForumSettings.setLastCatchUp(now);
        logger.debug("catch up: " + now);
    }

    public void markPostAsSeen(long pid) throws HibernateException {
        ForumPost post = getForumPost(pid);
        logger.debug("marking post " + pid + " as seen by '" + super.getUserSession().getUser().getLogin() + "'");
        if (post.getSeenByUsers().contains(this.forumUser)) {
            logger.debug(" ... already mentioned in seenByusers.");
            return;
        }
        Map threadSettings = post.getThread().getThreadsSettings();
        if (threadSettings.containsKey(this.forumUser)) {
            ForumUserThreadSettings ts = (ForumUserThreadSettings) threadSettings.get(this.forumUser);
            if (post.getDate().compareTo(ts.getLastCatchUp()) <= 0) {
                logger.debug(" ... already mentioned in threadSettings: post=" + post.getDate() + " < " + ts.getLastCatchUp());
                return;
            }
            logger.debug(" ... newer than threadSettings: post=" + post.getDate() + " > " + ts.getLastCatchUp());
        } else {
            logger.debug(" ... no appropriate threadSettings");
        }
        if (ENABLE_CATCHUP) {
            post.getSeenByUsers().add(this.forumUser);
        }
    }

    public void markThreadAsSeen(long tid) throws HibernateException {
        markThreadAsSeen(tid, new Timestamp(new Date().getTime()));
    }

    public void markThreadAsSeen(long tid, Timestamp ts) throws HibernateException {
        ForumThread thread = getForumThread(tid);
        ForumUserThreadSettings userThreadSettings = (ForumUserThreadSettings) thread.getThreadsSettings().get(this.forumUser);
        if (userThreadSettings == null) {
            userThreadSettings = new ForumUserThreadSettings();
            thread.getThreadsSettings().put(this.forumUser, userThreadSettings);
            super.getSession().save(userThreadSettings);
        }
        if (ENABLE_CATCHUP) {
            userThreadSettings.setLastCatchUp(ts);
        }
        logger.debug("catch up/thread: " + ts);
    }

    public int countThreads() throws HibernateException {
        SimpleTimer t = new SimpleTimer();
        Query query1a = super.getSession().createQuery("select count (thread.id) " + "from ru.goldenforests.forum.ForumThread as thread " + "where thread.forumInfo = :forum");
        query1a.setLong("forum", super.getForumInfo().getId());
        query1a.setCacheable(true);
        int totalThreads = ((Number) query1a.uniqueResult()).intValue();
        logger.debug("  ..... totalThreads=" + totalThreads + " -- " + t.check() + "ms");
        return totalThreads;
    }

    public Object[] countPostsAndThreads() throws HibernateException {
        SimpleTimer t = new SimpleTimer();
        Query query1b = super.getSession().createQuery("select count (post.id), count (distinct thread.id)" + "   from ru.goldenforests.forum.ForumThread as thread " + "       join thread.posts as post " + "   where thread.forumInfo = :forum");
        query1b.setLong("forum", super.getForumInfo().getId());
        query1b.setCacheable(true);
        Object[] pair = (Object[]) query1b.uniqueResult();
        long totalPosts = ((Number) pair[0]).longValue();
        long totalThreads = ((Number) pair[1]).longValue();
        logger.debug("  ..... total: posts=" + totalPosts + " threads=" + totalThreads + " -- " + t.check() + "ms");
        return pair;
    }

    public long countSeenOrOldPosts() throws HibernateException {
        SimpleTimer t = new SimpleTimer();
        Timestamp tooOld = new TooOldCalculator(this.forumUser).getTooOldTime(super.getForumInfo());
        Query query3 = super.getSession().createQuery("select count (distinct post.id) " + "from ru.goldenforests.forum.ForumThread as thread " + "    join thread.posts as post " + "    left outer join thread.threadsSettings as ts " + "where " + "   thread.forumInfo = :forum " + "   and (" + "       post.date < :tooold " + "       or (threadsSettingsUserId = :user.id and post.date < ts.lastCatchUp)" + "       or post.id in (" + "           select P.id " + "               from ru.goldenforests.forum.ForumPost as P" + "               left outer join P.seenByUsers as SB" + "           where SB = :user" + "       )" + "   )");
        query3.setLong("forum", super.getForumInfo().getId());
        query3.setTimestamp("tooold", tooOld);
        query3.setEntity("user", this.forumUser);
        query3.setLong("user.id", this.forumUser.getId());
        query3.setCacheable(true);
        long total = ((Number) query3.uniqueResult()).longValue();
        logger.debug("  ....... seen-or-old posts ::=" + total + " -- " + t.check() + "ms");
        return total;
    }

    /** @deprecated use ConstForumInfo constructor directly */
    public ConstForumInfo getConstForumInfo() throws JDBCException, HibernateException {
        return new ConstForumInfo(super.getUserSession(), super.getForumInfo());
    }

    private final ForumThread getForumThread(long tid) throws JDBCException, HibernateException {
        ForumThread thread = (ForumThread) super.getSession().load(ForumThread.class, new Long(tid));
        if (thread.getForumInfo().getId() != super.getForumInfo().getId()) throw new AccessControlException("Tried to access POST from the wrong forum.");
        return thread;
    }

    public final ForumPost getForumPost(long pid) throws JDBCException, HibernateException {
        ForumPost post = (ForumPost) super.getSession().load(ForumPost.class, new Long(pid));
        if (post.getThread().getForumInfo().getId() != super.getForumInfo().getId()) throw new AccessControlException("Tried to access THREAD from the wrong forum.");
        return post;
    }
}
