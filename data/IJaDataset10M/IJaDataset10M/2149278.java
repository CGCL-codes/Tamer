package edu.cmu.vlis.wassup.db;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanFactory;
import org.mybeans.factory.BeanFactoryException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.MatchArg;
import org.mybeans.factory.RollbackException;
import org.mybeans.factory.Transaction;
import edu.cmu.vlis.wassup.databean.EventsForUser;
import edu.cmu.vlis.wassup.databean.User;
import edu.cmu.vlis.wassup.databean.UserInterest;

public class UserInterestDAO {

    private BeanFactory<UserInterest> factory;

    public UserInterestDAO() throws DAOException {
        try {
            String jdbcDriverName = "com.mysql.jdbc.Driver";
            String jdbcURL = "jdbc:mysql:///wassup?user=root&password=mysql";
            BeanTable.useJDBC(jdbcDriverName, jdbcURL);
            UserDAO userDAO = new UserDAO();
            BeanTable<UserInterest> interestTable = BeanTable.getInstance(UserInterest.class, "interests", userDAO.getFactory());
            if (!interestTable.exists()) interestTable.create("userId", "tag");
            interestTable.setIdleConnectionCleanup(true);
            factory = interestTable.getFactory();
        } catch (BeanFactoryException e) {
            throw new DAOException(e);
        }
    }

    public UserInterestDAO(String URL) throws DAOException {
        try {
            String jdbcDriverName = "com.mysql.jdbc.Driver";
            String jdbcURL = "jdbc:mysql://" + URL + "/wassup?user=root&password=mysql";
            BeanTable.useJDBC(jdbcDriverName, jdbcURL);
            UserDAO userDAO = new UserDAO();
            BeanTable<UserInterest> interestTable = BeanTable.getInstance(UserInterest.class, "interests", userDAO.getFactory());
            if (!interestTable.exists()) interestTable.create("userId", "tag");
            interestTable.setIdleConnectionCleanup(true);
            factory = interestTable.getFactory();
        } catch (BeanFactoryException e) {
            throw new DAOException(e);
        }
    }

    /** Insert a new tuple of User_interesting mappping into the beanTable
	 * Returns error message if creation fails.
	 * Otherwise return a null upon success.
	 * 
	 * @param User_id
	 * @param tags
	 * @throws RollbackException
	 */
    public void insert(User userId, String[] tags) throws DAOException {
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i];
            try {
                Transaction.begin();
                factory.create(userId, tag);
                Transaction.commit();
            } catch (RollbackException e) {
                e.printStackTrace();
                throw new DAOException(e);
            }
        }
    }

    /** Lookup whether a typical event exists in the table
	 * Return Not Found if the events information is missing
	 * Otherwise return detailed information about event of a user
	 * 
	 * @param event_id
	 * @return
	 * @throws RollbackException
	 */
    public UserInterest lookup(User userId, String tag) {
        UserInterest interest = null;
        try {
            interest = factory.lookup(userId, tag);
        } catch (RollbackException e) {
            e.printStackTrace();
        }
        return interest;
    }

    /** Delete a tuple with primary key as composing of User and Tag
	 * return null if no tuple is affected
	 * 
	 * @param event_id
	 * @return
	 */
    public void delete(User userId, String tag) {
        UserInterest interst = lookup(userId, tag);
        if (interst != null) {
            try {
                Transaction.begin();
                factory.delete(userId, tag);
                Transaction.commit();
            } catch (RollbackException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Get all the messages about a certain User - getTuplesFromUser
	 * @param a
	 * @return
	 * @throws RollbackException
	 */
    public UserInterest[] getUserInterests(User user) {
        UserInterest[] userInterests = null;
        try {
            userInterests = factory.match(MatchArg.equals("userId", user));
        } catch (RollbackException e) {
            e.printStackTrace();
        }
        return userInterests;
    }

    /**
	 * Delete all the messages related to the input user
	 * @param userId
	 * @throws DAOException
	 */
    public void deleteInterests(User userId) throws DAOException {
        UserInterest[] interests = null;
        try {
            interests = factory.match(MatchArg.equals("userId", userId));
            if (interests != null && interests.length > 0) {
                for (UserInterest interest : interests) {
                    factory.delete(userId, interest.getTag());
                }
            }
        } catch (RollbackException e) {
            throw new DAOException(e);
        }
    }

    /**
	 * Get all the users interested in a certain tag - getUsersWithTag
	 * @param a
	 * @return
	 * @throws RollbackException
	 */
    public User[] getUsersInterestedIn(String tag) {
        UserInterest[] userInterests = null;
        User[] users = null;
        try {
            userInterests = factory.match(MatchArg.equals("tag", tag));
        } catch (RollbackException e) {
            e.printStackTrace();
        }
        if (userInterests != null && userInterests.length != 0) {
            users = new User[userInterests.length];
            for (int i = 0; i < userInterests.length; i++) {
                UserInterest userInterest = userInterests[i];
                users[i] = userInterest.getUserId();
            }
        }
        return users;
    }

    /**
	 * Get all the tags a certain user is interested in - getTagFromUser
	 * @param a
	 * @return
	 * @throws RollbackException
	 */
    public String[] getUserTags(User user) {
        UserInterest[] userInterests = null;
        String[] tags = null;
        try {
            userInterests = factory.match(MatchArg.equals("userId", user));
        } catch (RollbackException e) {
            e.printStackTrace();
        }
        if (userInterests != null && userInterests.length != 0) {
            tags = new String[userInterests.length];
            for (int i = 0; i < userInterests.length; i++) {
                UserInterest userInterest = userInterests[i];
                tags[i] = userInterest.getTag();
            }
        }
        return tags;
    }

    private User createUser() {
        User user = new User("radhika@cmu.edu");
        user.setFirstName("Radhika");
        user.setLastName("Karandikar");
        user.setPassword("1234");
        return user;
    }

    public static void main(String[] args) {
        try {
            UserInterestDAO userInterestDAO = new UserInterestDAO();
            User user = userInterestDAO.createUser();
            String[] tags = new String[3];
            tags[0] = "Music";
            tags[1] = "Family";
            tags[2] = "Social";
            userInterestDAO.insert(user, tags);
            System.out.println("inserted successfully");
            System.out.println(" --- Lookup ----");
            UserInterest userInterest = userInterestDAO.lookup(user, "Music");
            System.out.println(userInterest);
            System.out.println(" --- Delete ----");
            System.out.println(" --- getUserInterests ----");
            UserInterest[] userInterests = userInterestDAO.getUserInterests(user);
            for (int i = 0; i < userInterests.length; i++) {
                userInterest = userInterests[i];
                System.out.println(userInterest);
            }
            System.out.println(" --- getUsersInterestedIn Music ----");
            User[] users = userInterestDAO.getUsersInterestedIn("Music");
            for (int i = 0; i < users.length; i++) {
                User user2 = users[i];
                System.out.println(user2);
            }
            System.out.println(" --- getUserTags for user ----" + user);
            String[] inteterts = userInterestDAO.getUserTags(user);
            for (int i = 0; i < inteterts.length; i++) {
                System.out.println(inteterts[i]);
            }
            System.exit(0);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
