package prc.bubulina.forum.data_access;

import java.sql.Connection;
import java.sql.DriverManager;

public class ForumDAOFactory {

    public static final String DRIVER = "com.mysql.jdbc.Driver";

    public static final String DBURL = "jdbc:mysql://localhost:3306/forum";

    public static Connection createConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(DBURL, "horia", "barcelona");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserDAO getUserDAO() {
        return new UserDAO();
    }

    public static ForumThreadDAO getThreadDAO() {
        return new ForumThreadDAO();
    }

    public static TopicDAO getTopicDAO() {
        return new TopicDAO();
    }

    public static PostDAO getPostDAO() {
        return new PostDAO();
    }
}
