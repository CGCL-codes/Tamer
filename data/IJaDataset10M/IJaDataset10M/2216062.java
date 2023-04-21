package DB;

import Logik.Blog;
import Logik.BlogComment;
import Logik.Skills;
import Logik.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 */
public class DBBlog {

    public static ArrayList<Blog> SearchBlogs(String search) {
        Verbindung verb = new Verbindung();
        try {
            ArrayList<Blog> ret = new ArrayList();
            Statement state = verb.verbMySQL.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM tbl_Blog WHERE Name LIKE '%" + search + "%' OR Contents LIKE '%" + search + "%'");
            while (result.next()) {
                ArrayList<BlogComment> comments = new ArrayList();
                state = verb.verbMySQL.createStatement();
                ResultSet resultBlog = state.executeQuery("SELECT * FROM tbl_BlogComments WHERE BlogID='" + result.getString(1) + "'");
                while (resultBlog.next()) {
                    comments.add(new BlogComment(resultBlog.getInt(3), resultBlog.getString(4), resultBlog.getString(5)));
                }
                ret.add(new Blog(result.getInt(1), result.getInt(2), result.getString(3), comments, result.getString(5), result.getString(4)));
            }
            return ret;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static ArrayList<Blog> GetAllBlogsOfUser(int userid) {
        Verbindung verb = new Verbindung();
        try {
            ArrayList<Blog> ret = new ArrayList();
            Statement state = verb.verbMySQL.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM tbl_Blog WHERE UserID='" + userid + "'");
            while (result.next()) {
                ArrayList<BlogComment> comments = new ArrayList();
                state = verb.verbMySQL.createStatement();
                ResultSet resultBlog = state.executeQuery("SELECT * FROM tbl_BlogComments WHERE BlogID='" + result.getString(1) + "'");
                while (resultBlog.next()) {
                    comments.add(new BlogComment(resultBlog.getInt(3), resultBlog.getString(4), resultBlog.getString(5)));
                }
                ret.add(new Blog(result.getInt(1), result.getInt(2), result.getString(3), comments, result.getString(5), result.getString(4)));
            }
            return ret;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static Boolean SaveBlog(int userid, String Name) {
        Verbindung verb = new Verbindung();
        try {
            ArrayList<Blog> ret = new ArrayList();
            Statement state = verb.verbMySQL.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM tbl_Blog WHERE UserID='" + userid + "' AND Name='" + Name + "'");
            if (result.next()) {
                return false;
            }
            state.executeUpdate("INSERT INTO tbl_Blog (UserID,Name) VALUES('" + userid + "','" + Name + "')");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean DeleteBlog(int userid, int blogid) {
        Verbindung verb = new Verbindung();
        try {
            Statement state = verb.verbMySQL.createStatement();
            state.executeUpdate("DELETE FROM tbl_Blog WHERE BlogID='" + blogid + "' AND UserID='" + userid + "'");
            state = verb.verbMySQL.createStatement();
            state.executeUpdate("DELETE FROM tbl_BlogComments WHERE BlogID='" + blogid + "'");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean UpdateBlog(int userid, int blogid, String contents) {
        Verbindung verb = new Verbindung();
        try {
            Statement state = verb.verbMySQL.createStatement();
            state.executeUpdate("UPDATE tbl_Blog SET Contents='" + contents + "' WHERE BlogID='" + blogid + "' AND UserID='" + userid + "'");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean SaveComment(int userid, int blogid, String comment) {
        Verbindung verb = new Verbindung();
        try {
            Statement state = verb.verbMySQL.createStatement();
            state.executeUpdate("INSERT INTO tbl_BlogComments (BlogID,UserID,Comment) VALUES('" + blogid + "','" + userid + "','" + comment + "')");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean ActivateNotification(int userid, int blogid) {
        Verbindung verb = new Verbindung();
        try {
            Statement state = verb.verbMySQL.createStatement();
            state.executeUpdate("INSERT INTO tbl_BlogNotification (BlogID,UserID) VALUES('" + blogid + "','" + userid + "')");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean DeleteNotification(int userid, int blogid) {
        Verbindung verb = new Verbindung();
        try {
            Statement state = verb.verbMySQL.createStatement();
            state.executeUpdate("DELETE FROM tbl_BlogNotification WHERE BlogID='" + blogid + "' AND UserID='" + userid + "'");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean checkNotification(int userid, int blogid) {
        Verbindung verb = new Verbindung();
        try {
            Statement state = verb.verbMySQL.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM tbl_BlogNotification WHERE BlogID='" + blogid + "' AND UserID='" + userid + "'");
            return result.next();
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static ArrayList<Integer> getNotifications(int blogid) {
        Verbindung verb = new Verbindung();
        try {
            ArrayList<Integer> res = new ArrayList<Integer>();
            Statement state = verb.verbMySQL.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM tbl_BlogNotification WHERE BlogID='" + blogid + "'");
            while (result.next()) {
                res.add(result.getInt(2));
            }
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                verb.verbMySQL.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
