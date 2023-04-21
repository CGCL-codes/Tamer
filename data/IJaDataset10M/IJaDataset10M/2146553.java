package domlet.kbsample.util;

import domlet.kbsample.data.Article;
import domlet.kbsample.data.Category;
import domlet.kbsample.data.User;
import domlet.kbsample.managers.constants.UserTypes;
import net.sf.persist.Persist;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Dan Howard
 * Date: Sep 26, 2006
 * Time: 8:11:23 AM
 */
public class DbUtil {

    private static final Logger log = LogManager.getLogger(DbUtil.class);

    public static boolean isTableInDatabase(String table, Connection con) {
        boolean result = false;
        DatabaseMetaData dma = null;
        ResultSet r = null;
        try {
            dma = con.getMetaData();
            String types[] = { "TABLE" };
            r = dma.getTables(null, null, null, types);
            while (r.next()) {
                if (table.equalsIgnoreCase(r.getString("TABLE_NAME"))) {
                    result = true;
                    break;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * Used to create tables if they don't exist. Note we 'manually' create the tables and
     * don't let hibernate do it cause it doesn't seem to want to create 'CACHED' tables.
     *
     * @param con
     * @param insertDefaults
     * @throws SQLException
     */
    public static void createTables(Connection con, boolean insertDefaults) throws SQLException {
        StringBuffer sql = new StringBuffer("");
        Persist persist = new Persist(con);
        if (!isTableInDatabase("KB_USERS", con)) {
            sql.append("CREATE TABLE KB_USERS (").append("ID IDENTITY PRIMARY KEY, ").append("Username varchar(10), ").append("Password varchar(10), ").append("Name varchar(60), ").append("Email varchar(100), ").append("AllowVisibleEmail bit, ").append("TypeOfUser varchar(12) )");
            Statement st = null;
            try {
                st = con.createStatement();
                st.execute("SET IGNORECASE TRUE");
                st.execute(sql.toString());
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                    }
                }
            }
            if (insertDefaults) {
                User user = new User();
                user.setAllowVisibleEmail(false);
                user.setEmail("");
                user.setName("Built in admin account");
                user.setPassword("admin");
                user.setTypeOfUser(UserTypes.Admin.toString());
                user.setUsername("Admin");
                persist.insert(user);
            }
        }
        if (!isTableInDatabase("KB_ARTICLES", con)) {
            sql = new StringBuffer("");
            sql.append("CREATE TABLE KB_ARTICLES (").append("ID IDENTITY PRIMARY KEY, ").append("Category_ID int ,").append("Subject varchar(60), ").append("Article LONGVARCHAR, ").append("Created datetime, ").append("Creator_ID int, ").append("Modified datetime, ").append("Modifier_ID int, ").append("Security varchar(50), ").append("Users varchar(254), ").append("Active bit )");
            Statement st = null;
            try {
                st = con.createStatement();
                st.execute("SET IGNORECASE TRUE");
                st.execute(sql.toString());
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                    }
                }
            }
            if (insertDefaults) {
                Article article = new Article();
                article.setActive(false);
                article.setArticle("Sample article.");
                article.setCategoryId(1);
                article.setUsers("");
                article.setCreated(new Date());
                article.setCreatorId(1);
                article.setModified(new Date());
                article.setModifierId(1);
                article.setSecurity("Public");
                article.setSubject("Sample");
                persist.insert(article);
            }
        }
        if (!isTableInDatabase("KB_CATEGORIES", con)) {
            sql = new StringBuffer("");
            sql.append("CREATE TABLE KB_CATEGORIES (").append("ID IDENTITY PRIMARY KEY, ").append("Category varchar(40) )");
            Statement st = null;
            try {
                st = con.createStatement();
                st.execute("SET IGNORECASE TRUE");
                st.execute(sql.toString());
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                    }
                }
            }
            if (insertDefaults) {
                Category category = new Category();
                category.setCategory("Sample");
                persist.insert(category);
            }
        }
        con.commit();
    }
}
