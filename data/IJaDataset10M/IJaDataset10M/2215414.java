package slevnik;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Eduard Krak, Martin Penak
 */
public class CategoryManagerImpl implements CategoryManager {

    private final DataSource pool;

    private static final Logger logger = Logger.getLogger(CategoryManagerImpl.class.getName());

    public CategoryManagerImpl(DataSource ds) throws SQLException {
        this.pool = ds;
    }

    public DataSource getPool() {
        return this.pool;
    }

    public Category addCategory(Category category) throws SlevnikException {
        if (category == null) {
            throw new NullPointerException("Category can't be null!");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("INSERT INTO slevnik.categories (category_name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                category.setId(keys.getInt(1));
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Add category failed", se);
            throw new SlevnikException("Add category failed", se);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                logger.log(Level.SEVERE, "Closing connection failed", se2);
                throw new SlevnikException("Closing connection failed", se2);
            }
        }
        return category;
    }

    public Category findCategoryById(int id) throws SlevnikException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id has to be positive integer");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("SELECT * FROM slevnik.categories WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Category category = null;
            if (rs.next()) {
                category = new CategoryImpl();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("category_name"));
            }
            return category;
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding category failed", se);
            throw new SlevnikException("Finding category failed", se);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                logger.log(Level.SEVERE, "Closing connection failed", se2);
                throw new SlevnikException("Closing connection failed", se2);
            }
        }
    }

    public List<Category> findAllCategories() throws SlevnikException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("SELECT * FROM slevnik.categories");
            ResultSet rs = ps.executeQuery();
            List<Category> allCategories = new ArrayList<Category>();
            while (rs.next()) {
                Category category = new CategoryImpl();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("category_name"));
                allCategories.add(category);
            }
            return Collections.unmodifiableList(allCategories);
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding category failed", se);
            throw new SlevnikException("Finding category failed", se);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                logger.log(Level.SEVERE, "Closing connection failed", se2);
                throw new SlevnikException("Closing connection failed", se2);
            }
        }
    }

    public boolean removeCategory(Category category) throws SlevnikException {
        if (category == null) {
            throw new NullPointerException("Category is null");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("DELETE FROM slevnik.categories WHERE id=?");
            ps.setInt(1, category.getId());
            int eu = ps.executeUpdate();
            if (eu == 0) {
                return false;
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Removing category failed", se);
            throw new SlevnikException("Removing category failed", se);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                logger.log(Level.SEVERE, "Closing connection failed", se2);
                throw new SlevnikException("Closing connection failed", se2);
            }
        }
        return true;
    }

    public boolean removeAllCategories() throws SlevnikException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("DELETE FROM slevnik.categories");
            int eu = ps.executeUpdate();
            if (eu == 0) {
                return false;
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Deleting all categories failed", se);
            throw new SlevnikException("Deleting all categories failed", se);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                logger.log(Level.SEVERE, "Closing connection failed", se2);
                throw new SlevnikException("Closing connection failed", se2);
            }
        }
        return true;
    }
}
