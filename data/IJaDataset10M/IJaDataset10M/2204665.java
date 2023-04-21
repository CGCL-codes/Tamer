package isg3.data;

import isg3.article.*;
import isg3.user.*;
import isg3.utils.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class JDBCArticleDAO implements IArticleDAO {

    private Connection con;

    private ICategoryDAO cat_dao;

    private IUserDAO user_dao;

    private IRateDAO rate_dao;

    public JDBCArticleDAO() {
        con = ConnectionManager.getInstance().checkOut();
        this.user_dao = new JDBCUserDAO(con);
        this.rate_dao = new JDBCRateDAO(con);
        this.cat_dao = new JDBCCategoryDAO(con);
    }

    public JDBCArticleDAO(Connection c) {
        con = c;
        this.user_dao = new JDBCUserDAO(con);
        this.rate_dao = new JDBCRateDAO(con);
    }

    public void finalize() {
        ConnectionManager.getInstance().checkIn(con);
    }

    @Override
    public boolean insert(Article a) {
        boolean b = false;
        User u = (User) a.getUsersEditors().iterator().next();
        String user_oid = this.getOidOfUser(u.getNick());
        String cat_oid = this.getOidOfCategory(a.getCat().getName());
        String art_oid = UIDGenerator.getInstance().getKey();
        String user_art_oid = UIDGenerator.getInstance().getKey();
        PreparedStatement stmt = null;
        String query1 = "INSERT INTO Article(oid, title, content, lastRevision, visits, categoryOID, underDiscussion) VALUES(?, ?, ?, NOW(), ?, ?, false)";
        String query2 = "INSERT INTO UserArticle(oid, userOID, articleOID) VALUES(?, ?, ?)";
        try {
            stmt = this.con.prepareStatement(query1);
            stmt.setString(1, art_oid);
            stmt.setString(2, a.getTitle());
            stmt.setString(3, a.getContent());
            stmt.setInt(4, 0);
            stmt.setString(5, cat_oid);
            int aux1 = stmt.executeUpdate();
            stmt = this.con.prepareStatement(query2);
            stmt.setString(1, user_art_oid);
            stmt.setString(2, user_oid);
            stmt.setString(3, art_oid);
            int aux2 = stmt.executeUpdate();
            b = ((aux1 == 1) && (aux2 == 1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return b;
    }

    private String getOidOfUser(String nick) {
        String s = null;
        ResultSet results = null;
        PreparedStatement stmt = null;
        String query = "SELECT oid FROM User WHERE (nick = ?)";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, nick);
            results = stmt.executeQuery();
            if (results.next()) {
                s = results.getString("oid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return s;
    }

    private String getOidOfCategory(String name) {
        String s = null;
        ResultSet results = null;
        PreparedStatement stmt = null;
        String query = "SELECT oid FROM Category WHERE (name = ?)";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, name);
            results = stmt.executeQuery();
            if (results.next()) {
                s = results.getString("oid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return s;
    }

    private String getOidOfArticle(String title) {
        String s = null;
        ResultSet results = null;
        PreparedStatement stmt = null;
        String query = "SELECT oid FROM Article WHERE (title = ?)";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, title);
            results = stmt.executeQuery();
            if (results.next()) {
                s = results.getString("oid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return s;
    }

    @Override
    public boolean remove(String title) {
        boolean b = false;
        PreparedStatement stmt = null;
        String query = "DELETE FROM Articulo WHERE (title = ?)";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, title);
            int aux = stmt.executeUpdate();
            b = (aux >= 1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        String art_oid = this.getOidOfArticle(title);
        this.rate_dao.removeAll(art_oid);
        return b;
    }

    @Override
    public Article select(String title) {
        Article art = null;
        String cat_oid = null;
        String art_oid = null;
        ResultSet s1 = null;
        PreparedStatement stmt = null;
        String query = "SELECT * FROM Article WHERE (title = ?)";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, title);
            s1 = stmt.executeQuery();
            if (s1.next()) {
                art = new Article();
                art_oid = s1.getString("oid");
                cat_oid = s1.getString("categoryOID");
                art.setTitle(s1.getString("title"));
                art.setContent(s1.getString("content"));
                art.setVisits(s1.getInt("visits"));
                art.setUnderDiscussion(s1.getBoolean("underDiscussion"));
                java.sql.Date sDate = s1.getDate("lastRevision");
                art.setLastRevision(new java.util.Date(sDate.getTime()));
                Category cat = this.cat_dao.selectByOID(cat_oid);
                art.setCat(cat);
                Collection editors = this.user_dao.selectAllEditors(art_oid);
                art.setUSersEditors(editors);
                Collection c1 = this.rate_dao.selectAllByOID(art_oid);
                RatesCollection rates = new RatesCollection();
                rates.setRates(c1);
                art.setRatesCollection(rates);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return art;
    }

    @Override
    public Collection selectAll() {
        return null;
    }

    @Override
    public Collection selectMostRatedArticles(int n) {
        return null;
    }

    @Override
    public boolean update(Article a, String nick) {
        boolean b = false;
        String user_art_oid = UIDGenerator.getInstance().getKey();
        String user_oid = this.getOidOfUser(nick);
        String cat_oid = this.getOidOfCategory(a.getCat().getName());
        String art_oid = this.getOidOfArticle(a.getTitle());
        PreparedStatement stmt = null;
        String query1 = "UPDATE Article SET content = ?, " + "lastRevision = NOW(), visits = ?, categoryOID = ?, underDiscussion = ? " + "WHERE (title = ?)";
        String query2 = "INSERT INTO UserArticle(oid, userOID, articleOID) VALUES(?, ?, ?)";
        try {
            stmt = this.con.prepareStatement(query1);
            stmt.setString(1, a.getContent());
            stmt.setLong(2, a.getVisits());
            stmt.setString(3, cat_oid);
            stmt.setBoolean(4, a.getUnderDiscussion());
            stmt.setString(5, a.getTitle());
            int aux1 = stmt.executeUpdate();
            stmt = this.con.prepareStatement(query2);
            stmt.setString(1, user_art_oid);
            stmt.setString(2, user_oid);
            stmt.setString(3, art_oid);
            int aux2 = stmt.executeUpdate();
            b = (aux1 == 1) && (aux2 == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public Collection selectAllArticles(Category cat) {
        Collection c = null;
        ResultSet s1 = null;
        PreparedStatement stmt = null;
        String query = "SELECT * FROM Article WHERE (categoryOID = ?)";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, this.getOidOfCategory(cat.getName()));
            s1 = stmt.executeQuery();
            c = new LinkedList();
            while (s1.next()) {
                Article art = new Article();
                String art_oid = s1.getString("oid");
                art.setTitle(s1.getString("title"));
                art.setContent(s1.getString("content"));
                art.setVisits(s1.getInt("visits"));
                java.sql.Date sDate = s1.getDate("lastRevision");
                art.setUnderDiscussion(s1.getBoolean("underDiscussion"));
                art.setLastRevision(new java.util.Date(sDate.getTime()));
                art.setCat(cat);
                Collection editors = this.user_dao.selectAllEditors(art_oid);
                art.setUSersEditors(editors);
                Collection c1 = this.rate_dao.selectAllByOID(art_oid);
                RatesCollection rates = new RatesCollection();
                rates.setRates(c1);
                art.setRatesCollection(rates);
                c.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public Collection selectLastArticles(int n) {
        Collection c = null;
        ResultSet s1 = null;
        PreparedStatement stmt = null;
        String query = "SELECT * FROM Article ORDER BY lastRevision DESC LIMIT ?";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setInt(1, n);
            s1 = stmt.executeQuery();
            c = new LinkedList();
            while (s1.next()) {
                Article art = new Article();
                String art_oid = s1.getString("oid");
                String cat_oid = s1.getString("categoryOID");
                art.setTitle(s1.getString("title"));
                art.setContent(s1.getString("content"));
                art.setVisits(s1.getInt("visits"));
                java.sql.Date sDate = s1.getDate("lastRevision");
                art.setUnderDiscussion(s1.getBoolean("underDiscussion"));
                art.setLastRevision(new java.util.Date(sDate.getTime()));
                Category cat = this.cat_dao.selectByOID(cat_oid);
                art.setCat(cat);
                Collection editors = this.user_dao.selectAllEditors(art_oid);
                art.setUSersEditors(editors);
                Collection c1 = this.rate_dao.selectAllByOID(art_oid);
                RatesCollection rates = new RatesCollection();
                rates.setRates(c1);
                art.setRatesCollection(rates);
                c.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public Collection search(String article) {
        Collection c = new LinkedList();
        ResultSet rs;
        PreparedStatement stmt = null;
        String query = "SELECT * FROM Article WHERE title LIKE ?";
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, "%" + article + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                Article a = this.select(title);
                c.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
