package slevnik;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * This class represents manager of items - coupons
 * @author Eduard Krak, Martin Penak
 */
public class ItemManagerImpl implements ItemManager {

    private final DataSource pool;

    private static final Logger logger = Logger.getLogger(ItemManagerImpl.class.getName());

    public ItemManagerImpl(DataSource ds) throws SQLException {
        this.pool = ds;
    }

    public DataSource getPool() {
        return this.pool;
    }

    @Override
    public Item addItem(Item item) throws SlevnikException {
        if (item == null) {
            throw new NullPointerException("Sleva can't be null!");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("INSERT INTO slevnik.items (title, url, city, final_price, original_price, discount, customers, dealstart, dealend, category_id) VALUES (?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, item.getTitle());
            ps.setString(2, item.getUrl());
            ps.setString(3, item.getCity());
            ps.setDouble(4, item.getFinalPrice());
            ps.setDouble(5, item.getOriginalPrice());
            ps.setDouble(6, item.getDiscount());
            ps.setInt(7, item.getCustomers());
            if (item.getDealStart() != null) {
                ps.setDate(8, new java.sql.Date(item.getDealStart().getTime()));
            } else {
                ps.setDate(8, null);
            }
            if (item.getDealEnd() != null) {
                ps.setDate(9, new java.sql.Date(item.getDealEnd().getTime()));
            } else {
                ps.setDate(9, null);
            }
            ps.setInt(10, item.getCategoryId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                item.setId(keys.getInt(1));
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Add item failed", se);
            throw new SlevnikException("Add item failed", se);
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
        return item;
    }

    @Override
    public List<Item> addItems(List<Item> items) throws SlevnikException {
        if (items == null) {
            throw new NullPointerException("Set of items cant be null ");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("INSERT INTO slevnik.items (title, url, city, final_price, original_price, discount, customers, dealstart, dealend, category_id) VALUES (?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            for (Item item : items) {
                ps.setString(1, item.getTitle());
                ps.setString(2, item.getUrl());
                ps.setString(3, item.getCity());
                ps.setDouble(4, item.getFinalPrice());
                ps.setDouble(5, item.getOriginalPrice());
                ps.setDouble(6, item.getDiscount());
                ps.setInt(7, item.getCustomers());
                if (item.getDealStart() != null) {
                    ps.setDate(8, new java.sql.Date(item.getDealStart().getTime()));
                } else {
                    ps.setDate(8, null);
                }
                if (item.getDealEnd() != null) {
                    ps.setDate(9, new java.sql.Date(item.getDealEnd().getTime()));
                } else {
                    ps.setDate(9, null);
                }
                ps.setInt(10, item.getCategoryId());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Add item failed", se);
            throw new SlevnikException("Add item failed", se);
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
        return items;
    }

    @Override
    public boolean removeItem(Item item) throws SlevnikException {
        if (item == null) {
            throw new NullPointerException("Item is null");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("DELETE FROM slevnik.items WHERE id=?");
            ps.setInt(1, item.getId());
            int eu = ps.executeUpdate();
            if (eu == 0) {
                return false;
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding item failed", se);
            throw new SlevnikException("Finding item failed", se);
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

    @Override
    public boolean removeAllItems() throws SlevnikException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("DELETE FROM slevnik.items");
            int eu = ps.executeUpdate();
            if (eu == 0) {
                return false;
            }
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Deleting all items failed", se);
            throw new SlevnikException("Deleting all items failed", se);
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

    @Override
    public Item findItemById(int id) throws SlevnikException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id has to be positive integer");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("SELECT * FROM slevnik.items WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Item item = null;
            if (rs.next()) {
                item = new ItemImpl();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setCity(rs.getString("city"));
                item.setCustomers(rs.getInt("customers"));
                item.setDealEnd(rs.getDate("dealend"));
                item.setDealStart(rs.getDate("dealstart"));
                item.setDiscount(rs.getFloat("discount"));
                item.setOriginalPrice(rs.getFloat("original_price"));
                item.setFinalPrice(rs.getFloat("final_price"));
                item.setUrl(rs.getString("url"));
            }
            return item;
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding item failed", se);
            throw new SlevnikException("Finding item failed", se);
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

    @Override
    public List<Item> findAllItems(String sortingParam, Boolean ascending) throws SlevnikException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            if (ascending) {
                ps = con.prepareStatement("SELECT * FROM slevnik.items ORDER BY " + sortingParam + " ASC");
            } else {
                ps = con.prepareStatement("SELECT * FROM slevnik.items ORDER BY " + sortingParam + " DESC");
            }
            ResultSet rs = ps.executeQuery();
            List<Item> allItems = new ArrayList<Item>();
            while (rs.next()) {
                Item item = new ItemImpl();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setUrl(rs.getString("url"));
                item.setCity(rs.getString("city"));
                item.setFinalPrice(rs.getDouble("final_price"));
                item.setOriginalPrice(rs.getDouble("original_price"));
                item.setDiscount(rs.getDouble("discount"));
                item.setCustomers(rs.getInt("customers"));
                item.setDealStart(rs.getDate("dealstart"));
                item.setDealEnd(rs.getDate("dealend"));
                item.setCategoryId(rs.getInt("category_id"));
                allItems.add(item);
            }
            return Collections.unmodifiableList(allItems);
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding items set failed", se);
            throw new SlevnikException("Finding items set failed", se);
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

    @Override
    public List<Item> findItemsByCity(String city, String sortingParam, Boolean ascending) throws SlevnikException {
        if (city == null) {
            throw new IllegalArgumentException("City can't be null");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            if (ascending) {
                ps = con.prepareStatement("SELECT * FROM slevnik.items WHERE city=? ORDER BY " + sortingParam + " ASC");
            } else {
                ps = con.prepareStatement("SELECT * FROM slevnik.items WHERE city=? ORDER BY " + sortingParam + " DESC");
            }
            ps.setString(1, city);
            System.out.println();
            ResultSet rs = ps.executeQuery();
            List<Item> items = new ArrayList<Item>();
            while (rs.next()) {
                Item item = new ItemImpl();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setUrl(rs.getString("url"));
                item.setCity(rs.getString("city"));
                item.setFinalPrice(rs.getDouble("final_price"));
                item.setOriginalPrice(rs.getDouble("original_price"));
                item.setDiscount(rs.getDouble("discount"));
                item.setCustomers(rs.getInt("customers"));
                item.setDealStart(rs.getDate("dealstart"));
                item.setDealEnd(rs.getDate("dealend"));
                item.setCategoryId(rs.getInt("category_id"));
                items.add(item);
            }
            return Collections.unmodifiableList(items);
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding items failed", se);
            throw new SlevnikException("Finding items failed", se);
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

    @Override
    public List<Item> findItemsByText(String text, String sortingParam, Boolean ascending) throws SlevnikException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            if (ascending) {
                ps = con.prepareStatement("SELECT * FROM slevnik.items WHERE title LIKE ? ORDER BY " + sortingParam + " ASC");
            } else {
                ps = con.prepareStatement("SELECT * FROM slevnik.items WHERE title LIKE ? ORDER BY " + sortingParam + " DESC");
            }
            ps.setString(1, "%" + text + "%");
            System.out.println();
            ResultSet rs = ps.executeQuery();
            List<Item> items = new ArrayList<Item>();
            while (rs.next()) {
                Item item = new ItemImpl();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setUrl(rs.getString("url"));
                item.setCity(rs.getString("city"));
                item.setFinalPrice(rs.getDouble("final_price"));
                item.setOriginalPrice(rs.getDouble("original_price"));
                item.setDiscount(rs.getDouble("discount"));
                item.setCustomers(rs.getInt("customers"));
                item.setDealStart(rs.getDate("dealstart"));
                item.setDealEnd(rs.getDate("dealend"));
                item.setCategoryId(rs.getInt("category_id"));
                items.add(item);
            }
            return Collections.unmodifiableList(items);
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding items failed", se);
            throw new SlevnikException("Finding items failed", se);
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

    @Override
    public List<Item> findItemsByDistanceToCity(String city) throws SlevnikException {
        if (city == null) {
            throw new IllegalArgumentException("City can't be null");
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = pool.getConnection();
            ps = con.prepareStatement("SELECT DISTINCT city FROM slevnik.items");
            System.out.println();
            ResultSet rs = ps.executeQuery();
            List<String> cities = new ArrayList<String>();
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
            ps = con.prepareStatement("SELECT * from slevnik.items");
            rs = ps.executeQuery();
            SortedSet<Item> items = new TreeSet<Item>(new CityDistancesComparator(city, cities));
            while (rs.next()) {
                Item item = new ItemImpl();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setUrl(rs.getString("url"));
                item.setCity(rs.getString("city"));
                item.setFinalPrice(rs.getDouble("final_price"));
                item.setOriginalPrice(rs.getDouble("original_price"));
                item.setDiscount(rs.getDouble("discount"));
                item.setCustomers(rs.getInt("customers"));
                item.setDealStart(rs.getDate("dealstart"));
                item.setDealEnd(rs.getDate("dealend"));
                item.setCategoryId(rs.getInt("category_id"));
                items.add(item);
            }
            List<Item> items2 = new ArrayList();
            for (Item i : items) {
                items2.add(i);
            }
            return Collections.unmodifiableList(items2);
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "Finding items failed", se);
            throw new SlevnikException("Finding items failed", se);
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
}
