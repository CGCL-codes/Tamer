package model.dao.admin;

import java.util.ArrayList;
import java.util.List;
import model.myutil.myConnector;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ADMIN
 */
public class DAOUser {

    /**
     * Đăng Nhập
     * @param 
     * @return 
     */
    public static ArrayList DangNhap(String ID, String Pass) {
        ArrayList ketQua = new ArrayList();
        try {
            Session session = myConnector.getSession();
            Transaction tr = (Transaction) session.beginTransaction();
            Query qr = session.getNamedQuery("sp_dangnhap").setString(0, ID).setString(1, Pass);
            ketQua = (ArrayList) qr.list();
            tr.commit();
        } catch (Exception ex) {
            DAOLogDB.write("DAOUser.DangNhap " + ex.getMessage());
        } finally {
            return ketQua;
        }
    }

    /**
     * Lấy Tất Cả User Bao Gồm Stastus = 0
     * @param 
     * @return 
     */
    public static ArrayList getAllUser() {
        ArrayList ketQua = new ArrayList();
        try {
            Session session = myConnector.getSession();
            Transaction tr = (Transaction) session.beginTransaction();
            Query qr = session.getNamedQuery("get_user_notnull");
            ketQua = (ArrayList) qr.list();
            tr.commit();
        } catch (Exception ex) {
            DAOLogDB.write("DAOUser.getAllUser " + ex.getMessage());
        } finally {
            return ketQua;
        }
    }

    /**
     * Lấy User Với Mã ID Xác Định
     * @param 
     * @return 
     */
    public static ArrayList getUserId(int id_user) {
        ArrayList ketQua = new ArrayList();
        try {
            Session session = myConnector.getSession();
            Transaction tr = (Transaction) session.beginTransaction();
            Query qr = session.getNamedQuery("get_user_id").setInteger(0, id_user);
            ketQua = (ArrayList) qr.list();
            tr.commit();
        } catch (Exception ex) {
            DAOLogDB.write("DAOUser.getUserId " + ex.getMessage());
        } finally {
            return ketQua;
        }
    }

    /**
     * Xóa User Theo User_ID
     * @param 
     * @return 
     */
    public static void delUser(int id_user) {
        try {
            Session session = myConnector.getSession();
            Transaction tr = (Transaction) session.beginTransaction();
            Query qr = session.getNamedQuery("del_user").setInteger(0, id_user);
            List li = qr.list();
            tr.commit();
        } catch (Exception ex) {
            DAOLogDB.write("DAOUser.delUser " + ex.getMessage());
        }
    }

    /**
     * Thay Đổi Mật Khẩu 
     * @param 
     * @return 
     */
    public static void updUserPass(int id_user, String pass1, String pass2) {
        try {
            Session session = myConnector.getSession();
            Transaction tr = (Transaction) session.beginTransaction();
            Query qr = session.getNamedQuery("upd_user_pass").setInteger(0, id_user).setString(1, pass1).setString(2, pass2);
            List li = qr.list();
            tr.commit();
        } catch (Exception ex) {
            DAOLogDB.write("DAOUser.updUserPass " + ex.getMessage());
        }
    }

    /**
     * Thêm User
     * @param 
     * @return 
     */
    public static void insUser(int id_user, String n1, String n2, String n3, String n4, String n5, String n6) {
        try {
            Session session = myConnector.getSession();
            Transaction tr = (Transaction) session.beginTransaction();
            Query qr = session.getNamedQuery("ins_user").setInteger(0, id_user).setString(1, n1).setString(2, n2).setString(3, n3).setString(4, n4).setString(5, n5).setString(6, n6);
            List li = qr.list();
            tr.commit();
        } catch (Exception ex) {
            DAOLogDB.write("DAOUser.updUserPass " + ex.getMessage());
        }
    }
}
