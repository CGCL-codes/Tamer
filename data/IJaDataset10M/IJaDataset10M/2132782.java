package com.myres.admin.db;

import com.myres.db.ConnectionPool;
import com.myres.model.Order;
import com.myres.model.OrderItem;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.myres.util.TimeTool;
import java.sql.Timestamp;

public class AdminService {

    public List<Order> checkNewOrder() {
        Connection con = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            now.setHours(0);
            now.setMinutes(0);
            now.setSeconds(0);
            PreparedStatement pstmt = con.prepareStatement("select * from res_orders where res_orders._order_time>? and _status=0 and _seen=0 order by res_orders._order_time asc");
            pstmt.setTimestamp(1, now);
            ResultSet rs = pstmt.executeQuery();
            List<Order> orders = Order.populate(rs);
            System.out.println("order size=" + orders.size());
            for (Order order : orders) {
                int id = order.getId();
                order.setItems(getOrderItemOfOrder(id));
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int[] ordersSend(String[] ids) {
        Connection con = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            PreparedStatement pstmt = con.prepareStatement("update res_orders set _seen=1 where res_orders._id=?");
            for (String id : ids) {
                pstmt.setLong(1, Long.parseLong(id));
                pstmt.addBatch();
            }
            return pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int OrderSeen(long oid) {
        Connection con = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            PreparedStatement pstmt = con.prepareStatement("update res_orders set _seen=1 where res_orders._id=?");
            pstmt.setLong(1, oid);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int[] OrdersSeen(List<Order> orders) {
        if (orders.size() == 0) return null;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            pstmt = con.prepareStatement("update res_orders set _seen=1 where res_orders._id=?");
            for (Order order : orders) {
                pstmt.setLong(1, order.getId());
                pstmt.addBatch();
            }
            return pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getOrder(int type) {
        Connection con = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            now.setHours(0);
            now.setMinutes(0);
            now.setSeconds(0);
            PreparedStatement pstmt = con.prepareStatement("select * from res_orders where res_orders._order_time>? and _status=? order by res_orders._order_time asc");
            pstmt.setTimestamp(1, now);
            pstmt.setInt(2, type);
            ResultSet rs = pstmt.executeQuery();
            List<Order> orders = Order.populate(rs);
            System.out.println("order size=" + orders.size());
            for (Order order : orders) {
                int id = order.getId();
                order.setItems(getOrderItemOfOrder(id));
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int setOrderState(int oid, int state) {
        Connection con = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            PreparedStatement pstmt = con.prepareStatement("update res_orders set _status=? where _id=?");
            pstmt.setInt(1, state);
            pstmt.setInt(2, oid);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<OrderItem> getOrderItemOfOrder(int oid) {
        Connection con = null;
        try {
            con = ConnectionPool.getPool().getConnection();
            PreparedStatement pstmt = con.prepareStatement("select * ,(select res_products._name from res_products where res_products._id=_producnt_id) from res_order_items where _order_id=?");
            pstmt.setInt(1, oid);
            ResultSet rs = pstmt.executeQuery();
            return OrderItem.populate(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
