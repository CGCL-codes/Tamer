package ru.goldenforests.forum.cl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import ru.goldenforests.forum.ForumInfo;
import ru.goldenforests.forum.beans.ConstForumInfoStats;
import ru.goldenforests.forum.beans.ConstForumThread;
import ru.goldenforests.forum.beans.DummyUser;
import ru.goldenforests.forum.components.HibernateSessionFactoryBean;
import ru.goldenforests.forum.components.UserSession;
import ru.goldenforests.forum.engine.UsersManager;
import ru.goldenforests.forum.faces.ForumsManager;
import ru.goldenforests.forum.faces.ReadFace;
import ru.goldenforests.forum.util.DateUtils;
import ru.goldenforests.forum.util.SimpleTimer;

/**
 * @author svv
 *
 */
public class CommandLine {

    private static final void thread(Session session) throws Exception {
        ReadFace readFace = new ReadFace();
        readFace.setSession(session);
        readFace.setUserSession(new UserSession(new DummyUser("zowie", false)));
        readFace.setForumInfo((ForumInfo) session.load(ForumInfo.class, new Long(35)));
        for (int i = 0; i < 200; ++i) {
            try {
                ConstForumThread th = readFace.getThread(15587);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private static final void recent(Session session) throws Exception {
        Set forumNames = new HashSet(Arrays.asList(new String[] { "gf.games.2005.vedmak.tech" }));
        UserSession userSession = new UserSession(UsersManager.getUserByLogin(session, "adrian"));
        ForumsManager.getRecentAccessibleThreads(session, userSession, forumNames, DateUtils.getShortMemoryLimit());
        ForumsManager.getRecentAccessibleThreads(session, userSession, forumNames, DateUtils.getShortMemoryLimit());
    }

    public static final void main(String args[]) throws Exception {
        SessionFactory sessionFactory;
        try {
            sessionFactory = new HibernateSessionFactoryBean().getSessionFactory();
            Session session = null;
            try {
                session = sessionFactory.openSession();
                if (args[0].equals("stat")) ; else if (args[0].equals("thread")) thread(session); else if (args[0].equals("recent")) recent(session);
                session.connection().rollback();
            } catch (Exception ex) {
            } finally {
                session.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
