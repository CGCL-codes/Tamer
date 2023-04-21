package Model;

import Model.DAO.MySQL;
import Model.DAO.MySqlDataAccessHelper;
import POJO.pojoNguoiDung;
import POJO.pojoSanPham;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class modelNguoiDung {

    public static pojoNguoiDung loadNguoiDung(ResultSet rs) throws SQLException {
        pojoNguoiDung nd = null;
        if (rs.next()) {
            nd = new pojoNguoiDung();
            nd.TenDangNhap = rs.getString("TenDangNhap");
            nd.HoTen = rs.getString("HoTen");
            nd.DienThoai = rs.getString("DienThoai");
            nd.DiaChi = rs.getString("DiaChi");
            nd.Email = rs.getString("Email");
            nd.PhanQuyen = modelPhanQuyen.getPhanQuyen(rs.getInt("PhanQuyen"));
            nd.TinhTrang = modelTinhTrangNguoiDung.getTinhTrangND(rs.getInt("TinhTrang"));
        }
        return nd;
    }

    public static pojoNguoiDung getNguoiDung(String user, String pass) throws SQLException {
        String sql = String.format("SELECT * " + "FROM NguoiDung " + "WHERE TenDangNhap='%s' AND MatKhau='%s' AND TinhTrang = 1", user, pass);
        if (MySQL.executeQuery(sql)) {
            return loadNguoiDung(MySQL.getResult());
        }
        return null;
    }

    public static boolean ktTonTai(String user) throws SQLException {
        String sql = String.format("SELECT * FROM NguoiDung WHERE TenDangNhap='%s'", user);
        if (MySQL.executeQuery(sql)) {
            return MySQL.getResult().next();
        }
        return false;
    }

    public static boolean themNguoiDung(pojoNguoiDung nd) throws SQLException {
        String sql = String.format("INSERT INTO NguoiDung(TenDangNhap, MatKhau" + ", HoTen, DienThoai, DiaChi," + " Email) VALUES('%s','%s','%s','%s','%s','%s')", nd.TenDangNhap, nd.MatKhau, nd.HoTen, nd.DienThoai, nd.DiaChi, nd.Email);
        if (MySQL.executeNonQuery(sql)) {
            return true;
        }
        return false;
    }

    public static boolean KTAdmin(String Username) {
        try {
            MySqlDataAccessHelper helper = new MySqlDataAccessHelper();
            helper.Open();
            String sql = String.format("Select * From NguoiDung Where TenDangNhap = '%s'", Username);
            ResultSet rs = helper.ExecuteQuery(sql);
            if (rs.next()) {
                int maPQ = rs.getInt("PhanQuyen");
                if (modelPhanQuyen.getPhanQuyen(maPQ).getTenPhanQuyen().equalsIgnoreCase("admin")) {
                    return true;
                }
            }
            helper.Close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return false;
    }

    public static ArrayList<pojoNguoiDung> ShowDanhSachNguoiDung() {
        ArrayList<pojoNguoiDung> ds = new ArrayList<pojoNguoiDung>();
        try {
            MySqlDataAccessHelper helper = new MySqlDataAccessHelper();
            helper.Open();
            String sql = "Select * From NguoiDung Where TinhTrang = 1 and PhanQuyen <> 2";
            ResultSet rs = helper.ExecuteQuery(sql);
            while (rs.next()) {
                pojoNguoiDung nd = new pojoNguoiDung();
                nd.TenDangNhap = rs.getString("TenDangNhap");
                nd.HoTen = rs.getString("HoTen");
                nd.DienThoai = rs.getString("DienThoai");
                nd.DiaChi = rs.getString("DiaChi");
                nd.Email = rs.getString("Email");
                ds.add(nd);
            }
            helper.Close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return ds;
    }

    public static void BanNick(String TenDangNhap) {
        try {
            MySqlDataAccessHelper helper = new MySqlDataAccessHelper();
            helper.Open();
            String sql = String.format("Update NguoiDung Set TinhTrang = 0 Where TenDangNhap = '%s'", TenDangNhap);
            helper.ExecuteUpdate(sql);
            helper.Close();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public static void updateThongTin(pojoNguoiDung ndOld, pojoNguoiDung ndUpt) throws SQLException {
        boolean hasMore = false;
        String AND = ", ";
        String update = "UPDATE NguoiDung";
        String set = "SET ";
        String where = String.format("WHERE TenDangNhap = '%s'", ndOld.TenDangNhap);
        if (!ndUpt.HoTen.equals("") && !ndUpt.HoTen.equals(ndOld.HoTen)) {
            set += String.format("HoTen = '%s'", ndUpt.HoTen);
            hasMore = true;
        }
        if (!ndUpt.Email.equals("") && !ndUpt.Email.equals(ndOld.Email)) {
            if (hasMore) {
                set += AND;
            }
            set += String.format("Email = '%s'", ndUpt.Email);
            hasMore = true;
        }
        if (!ndUpt.DiaChi.equals("") && !ndUpt.DiaChi.equals(ndOld.DiaChi)) {
            if (hasMore) {
                set += AND;
            }
            set += String.format("DiaChi = '%s'", ndUpt.DiaChi);
            hasMore = true;
        }
        if (!ndUpt.DienThoai.equals("") && !ndUpt.DienThoai.equals(ndOld.DienThoai)) {
            if (hasMore) {
                set += AND;
            }
            set += String.format("DienThoai = '%s'", ndUpt.DienThoai);
            hasMore = true;
        }
        if (hasMore) {
            if (MySQL.startConnection()) {
                String sql = String.format("%s %s %s", update, set, where);
                MySQL.executeNonQuery(sql);
                MySQL.closeConnection();
            }
        }
    }

    public static void updateMatKhau(String oldPass, String newPass) throws SQLException {
        if (MySQL.startConnection()) {
            String sql = String.format("UPDATE NguoiDung " + "SET MatKhau='%s' " + "WHERE MatKhau='%s'", newPass, oldPass);
            MySQL.executeNonQuery(sql);
            MySQL.closeConnection();
        }
    }

    public static boolean ktSoHuuEmail(String user, String email) throws SQLException {
        if (MySQL.startConnection()) {
            String sql = "SELECT *" + " FROM NguoiDung" + " WHERE TenDangNhap='%s' AND Email='%s'";
            sql = String.format(sql, user, email);
            if (MySQL.executeQuery(sql)) {
                if (MySQL.getResult().next()) {
                    return true;
                }
            }
            MySQL.closeConnection();
        }
        return false;
    }

    public static String getHoTen(String user) throws SQLException {
        if (MySQL.startConnection()) {
            String sql = "SELECT * FROM NguoiDung WHERE TenDangNhap = '" + user + "'";
            if (MySQL.executeQuery(sql)) {
                if (MySQL.getResult().next()) {
                    return MySQL.getResult().getString("HoTen");
                }
            }
            MySQL.closeConnection();
        }
        return null;
    }

    public static String getMatKhau(String user) throws SQLException {
        if (MySQL.startConnection()) {
            String sql = "SELECT * FROM NguoiDung WHERE TenDangNhap = '" + user + "'";
            if (MySQL.executeQuery(sql)) {
                if (MySQL.getResult().next()) {
                    return MySQL.getResult().getString("MatKhau");
                }
            }
            MySQL.closeConnection();
        }
        return null;
    }
}
