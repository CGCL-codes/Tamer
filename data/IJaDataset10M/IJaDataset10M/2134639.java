package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBManager {

    private String dburl = "";

    private String dbuser = "";

    private String dbpass = "";

    public void setDbUrl(String url) {
        dburl = url;
    }

    public String getDbUrl() {
        return dburl;
    }

    public void setDbUser(String user) {
        dbuser = user;
    }

    public String getDbUser() {
        return dbuser;
    }

    public void setDbPass(String pass) {
        dbpass = pass;
    }

    public String getDbPass() {
        return dbpass;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getDbUrl(), getDbUser(), getDbPass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Restaurant> getRestaurantList(String keyword) {
        ArrayList<Restaurant> list = new ArrayList<Restaurant>();
        Connection conn = getConnection();
        if (conn != null) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                String sqlQuery = "";
                sqlQuery += "SELECT r.sn, r.name, r.lati, r.longi, r.phone ";
                sqlQuery += "FROM restaurant r JOIN food f ON r.sn = f.fk_restaurant ";
                sqlQuery += "		LEFT JOIN review v ON r.sn = v.fk_restaurant ";
                sqlQuery += "WHERE 1=1" + keyword + " ";
                sqlQuery += "GROUP BY r.name ";
                sqlQuery += "ORDER BY AVG(v.rate) DESC, SUM(v.rate) DESC";
                System.out.println("getRestaurantList : " + sqlQuery);
                ps = conn.prepareStatement(sqlQuery);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setSn(Integer.parseInt(rs.getString("sn")));
                    restaurant.setName(rs.getString("name"));
                    restaurant.setPhone(rs.getString("phone"));
                    restaurant.setLati(Double.parseDouble(rs.getString("Lati")));
                    restaurant.setLongi(Double.parseDouble(rs.getString("Longi")));
                    restaurant.setFoodList(getFoodList(rs.getString("sn"), keyword));
                    restaurant.setReviewList(getReviewList(rs.getString("sn")));
                    list.add(restaurant);
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                closeConnection(conn);
            }
        }
        return list;
    }

    public ArrayList<Food> getFoodList(String sn, String keyword) {
        ArrayList<Food> list = new ArrayList<Food>();
        Connection conn = getConnection();
        if (conn != null) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                String sqlQuery = "";
                sqlQuery = "";
                sqlQuery += "SELECT DISTINCT f.sn, f.name, f.price, f.deliverable ";
                sqlQuery += "FROM restaurant r JOIN food f ON r.sn = f.fk_restaurant ";
                sqlQuery += "WHERE r.sn=" + sn + keyword;
                System.out.println("getFoodList : " + sqlQuery);
                ps = conn.prepareStatement(sqlQuery);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Food food = new Food();
                    food.setSn(Integer.parseInt(rs.getString("sn")));
                    food.setName(rs.getString("name"));
                    food.setPrice(rs.getString("price"));
                    food.setDeliverable(rs.getString("deliverable"));
                    list.add(food);
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                closeConnection(conn);
            }
        }
        return list;
    }

    public ArrayList<Review> getReviewList(String sn) {
        ArrayList<Review> list = new ArrayList<Review>();
        Connection conn = getConnection();
        if (conn != null) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                String sqlQuery = "";
                sqlQuery += "SELECT DISTINCT v.sn, v.fk_restaurant, v.reviewer, v.content, v.rate, v.reg_date ";
                sqlQuery += "FROM restaurant r JOIN review v ON r.sn = v.fk_restaurant ";
                sqlQuery += "WHERE r.sn=" + sn;
                System.out.println("getReviewList : " + sqlQuery);
                ps = conn.prepareStatement(sqlQuery);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Review review = new Review();
                    review.setSn(Integer.parseInt(rs.getString("sn")));
                    review.setFk_restaurant(Integer.parseInt(rs.getString("fk_restaurant")));
                    review.setReviewer(rs.getString("reviewer"));
                    review.setContent(rs.getString("content"));
                    review.setRate(Integer.parseInt(rs.getString("rate")));
                    review.setRegdate(rs.getString("reg_date"));
                    list.add(review);
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                closeConnection(conn);
            }
        }
        return list;
    }

    public void addReview(String fk_restaurant, String reviewer, String content, String rate) {
        Connection conn = getConnection();
        if (conn != null) {
            PreparedStatement ps = null;
            try {
                String sqlQuery = "";
                sqlQuery += "INSERT INTO `review` (`fk_restaurant`, `reviewer`, `content`, `rate`) VALUES";
                sqlQuery += "(" + fk_restaurant + ",";
                sqlQuery += " '" + reviewer + "',";
                sqlQuery += " '" + content + "',";
                sqlQuery += " " + rate + ")";
                System.out.println("addReview : " + sqlQuery);
                ps = conn.prepareStatement(sqlQuery);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                closeConnection(conn);
            }
        }
    }
}
