package hotel.service.impl;

import hotel.model.Room;
import hotel.service.RoomService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 类说明
 * 
 * @author lipeiying
 * @version 创建时间：2011-12-31 下午03:50:32
 */
public class RoomServiceImpl implements RoomService {

    @Override
    public boolean createRoom(Room room) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "insert into room (roomid,price,status,beds,styles) values (?,?,?,?,?)";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoomid());
            ps.setFloat(2, room.getPrice());
            ps.setInt(3, room.getStatus());
            ps.setInt(4, room.getBeds());
            ps.setInt(5, room.getStyles());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return true;
    }

    @Override
    public boolean deleteRoom(String roomid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "delete from room where roomid = ?";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, roomid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return true;
    }

    @Override
    public List<Room> listRoom() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Room> list = new ArrayList<Room>();
        String sql = "select * from room";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String roomid = rs.getString("roomid");
                float price = rs.getFloat("price");
                int beds = rs.getInt("beds");
                int styles = rs.getInt("styles");
                int stauts = rs.getInt("status");
                Room r = new Room(roomid, price, beds, styles, stauts);
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return list;
    }

    @Override
    public boolean updateRoom(String pid, Room room) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "update room set roomid = ?,price=?,styles=?,beds=? where roomid =?";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoomid());
            ps.setFloat(2, room.getPrice());
            ps.setInt(3, room.getStyles());
            ps.setInt(4, room.getBeds());
            ps.setString(5, pid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return true;
    }

    @Override
    public Room getRoom(String roomid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Room room = null;
        String sql = "select * from room where roomid = ?";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, roomid);
            rs = ps.executeQuery();
            if (rs.next()) {
                float price = rs.getFloat("price");
                int beds = rs.getInt("beds");
                int styles = rs.getInt("styles");
                int stauts = rs.getInt("status");
                room = new Room(roomid, price, beds, styles, stauts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return room;
    }

    @Override
    public boolean bookRoom(String roomid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "update room set status=? where roomid =?";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Room.RESERVE);
            ps.setString(2, roomid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return true;
    }

    @Override
    public boolean checkInRoom(String roomid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "update room set status=? where roomid =?";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Room.CHECKIN);
            ps.setString(2, roomid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return true;
    }

    @Override
    public boolean clearRoom(String roomid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "update room set status=? where roomid =?";
        try {
            conn = JdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Room.AVAILABLE);
            ps.setString(2, roomid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            JdbcUtil.free(rs, ps, conn);
        }
        return true;
    }

    @Override
    public boolean isAvailable(String roomid) {
        Room room = getRoom(roomid);
        if (null == room) {
            return false;
        } else {
            return Room.AVAILABLE == room.getStatus() ? true : false;
        }
    }
}
