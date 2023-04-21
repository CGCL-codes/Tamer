package quanlyhochieu.taomoi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.jfree.date.DateUtilities;
import com.sun.corba.se.pept.transport.Connection;
import utils.DateUtils;
import dbutil.ConnectDB;

public class utils {

    public utils() {
    }

    /**************************************
	 * Method name		: insertData
	 * Return type		: int
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public static int insertData(String SQL) {
        ConnectDB cont = new ConnectDB();
        cont.connectDB();
        int numberRowsUpdated = -1;
        try {
            numberRowsUpdated = cont.getStatement().executeUpdate(SQL);
        } catch (SQLException se) {
            System.out.println(" SQL is invalid");
            se.printStackTrace();
        }
        return numberRowsUpdated;
    }

    /**************************************
	 * Method name		: deleteVitriLuuTru
	 * Return type		: boolean
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public static boolean deleteVitriLuuTru(int id) {
        boolean result = false;
        String sql = "delete from vitriluutru where id = " + id + "";
        ConnectDB cont = new ConnectDB();
        cont.connectDB();
        try {
            int i = cont.getStatement().executeUpdate(sql);
            if (i > 0) result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    /**************************************
	 * Method name		: updateViTriTu
	 * Return type		: boolean
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public static boolean updateViTriTu(String viTriTu, String nganTrongTu, String moTa, int id) {
        boolean result = false;
        String sql = "update vitriluutru set vitritu = '" + viTriTu + "'," + " ngantrongtu = '" + nganTrongTu + "'," + " mota = '" + moTa + "' where id = " + id;
        System.out.println("SQL UPDATE " + sql);
        ConnectDB cont = new ConnectDB();
        cont.connectDB();
        try {
            int i = cont.getStatement().executeUpdate(sql);
            if (i > 0) result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    /**************************************
	 * Method name		: checkHCTrongDoanRa
	 * Return type		: boolean
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public static boolean checkHCTrongDoanRa(String soHC) {
        boolean result = false;
        String sql = "select * from dsnguoidi as DS, doanra as DR where DS.sohc = '" + soHC + "' and DS.iddoanra = DR.madoan and DR.ngayve >  '" + DateUtils.utilDateToSqlDate(new java.util.Date()) + "'";
        ArrayList<HashMap<String, Object>> array = ConnectDB.getDataTypeList(sql);
        if (array.size() > 0) {
            result = false;
        } else result = true;
        return result;
    }

    /**************************************
	 * Method name		: checkSoHCVaCMND
	 * Return type		: boolean
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public static boolean checkSoHCVaCMND(String soHC) {
        boolean result = false;
        String sql = "select cmnd from hochieu where sohc = '" + soHC + "'";
        ArrayList<HashMap<String, Object>> array = ConnectDB.getDataTypeList(sql);
        if (array.size() > 0) {
            HashMap<String, Object> hash = new HashMap<String, Object>();
            hash = (HashMap<String, Object>) array.get(0);
            String sqlCMND = "select * from hochieu where cmnd = '" + hash.get("cmnd").toString() + "'";
            ArrayList<HashMap<String, Object>> arrayCmnd = ConnectDB.getDataTypeList(sqlCMND);
            if (arrayCmnd.size() > 1) result = false; else result = true;
        }
        return result;
    }

    /**************************************
	 * Method name		: getCMND
	 * Return type		: String
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public static String getCMND(String soHC) {
        String cmnd = null;
        String sql = "select cmnd from hochieu where sohc = '" + soHC + "'";
        ArrayList<HashMap<String, Object>> array = ConnectDB.getDataTypeList(sql);
        if (array.size() > 0) {
            HashMap<String, Object> hash = new HashMap<String, Object>();
            hash = (HashMap<String, Object>) array.get(0);
            cmnd = hash.get("cmnd").toString();
        }
        return cmnd;
    }

    /**************************************
	 * Method name		: xoaHoChieu
	 * Return type		: boolean
	 * Description		:
	 * Created date		: Aug 20, 2008
	 * Author			: Ngoc DIem
	 **************************************/
    public static boolean xoaHoChieu(String soHC) {
        boolean result = false;
        String sqlHC = "delete from hochieu where sohc = '" + soHC + "'";
        String sqlViTriLuuTru = "delete from vitriluutru where sohc = '" + soHC + "'";
        ConnectDB cont = new ConnectDB();
        cont.connectDB();
        try {
            if (checkSoHCVaCMND(soHC) == false) {
                int i = cont.getStatement().executeUpdate(sqlHC);
                int j = cont.getStatement().executeUpdate(sqlViTriLuuTru);
                if (i > 0 && j > 0) result = true; else {
                    cont.getConnect().rollback();
                    result = false;
                }
            } else {
                String cmnd = getCMND(soHC);
                String sqlCaNhan = "delete from canhan where cmnd = '" + cmnd + "'";
                int i = cont.getStatement().executeUpdate(sqlHC);
                int j = cont.getStatement().executeUpdate(sqlViTriLuuTru);
                int k = cont.getStatement().executeUpdate(sqlCaNhan);
                if (i > 0 && j > 0 && k > 0) result = true; else {
                    cont.getConnect().rollback();
                    result = false;
                }
            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public static boolean capNhatData(String sql) {
        boolean result = false;
        ConnectDB cont = new ConnectDB();
        cont.connectDB();
        try {
            int i = cont.getStatement().executeUpdate(sql);
            if (i > 0) result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public static int insertCaNhan(String cmnd, String ten, String coQuan, String chucVu, boolean dangVien, String namSinh, boolean gioiTinh, UploadedFile anh) {
        int result = 0;
        ConnectDB.connectDB();
        String SQL = "INSERT INTO canhan(cmnd,ten,namsinh,coquan,gioitinh,dangvien,chucvu,anh) " + " VALUES(?,?,?,?,?,?,?,?)";
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(anh.getInputStream());
        } catch (FileNotFoundException exFile) {
            exFile.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement pre = ConnectDB.connect.prepareStatement(SQL);
            pre.setString(1, cmnd);
            pre.setString(2, ten);
            pre.setString(3, namSinh);
            pre.setString(4, coQuan);
            pre.setBoolean(5, gioiTinh);
            pre.setBoolean(6, dangVien);
            pre.setString(7, chucVu);
            pre.setBinaryStream(8, bis, (int) anh.getSize());
            result = pre.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(" SQL is invalid");
        }
        return result;
    }

    public static boolean capNhatCaNhan(String cmnd, String ten, String coQuan, String chucVu, boolean dangVien, String namSinh, String gioiTinh, UploadedFile fileAnh) {
        int result = 0;
        if (fileAnh != null) {
            String sqlUpdateCaNhan = "update canhan set ten = ?, " + " namsinh = ?," + " gioitinh = ?, " + " coquan = ?," + " anh = ?, " + " dangvien =?, " + " chucvu  = ? where cmnd = ?";
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(fileAnh.getInputStream());
            } catch (FileNotFoundException exFile) {
                exFile.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement pre = ConnectDB.connect.prepareStatement(sqlUpdateCaNhan);
                pre.setString(1, ten);
                pre.setString(2, namSinh);
                boolean boolGioiTinh = gioiTinh.equals("Nam") ? true : false;
                pre.setBoolean(3, boolGioiTinh);
                pre.setString(4, coQuan);
                pre.setBinaryStream(5, bis, (int) fileAnh.getSize());
                pre.setBoolean(6, dangVien);
                pre.setString(7, chucVu);
                pre.setString(8, cmnd);
                result = pre.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sqlUpdateCaNhan = "update canhan set ten = ?, " + " namsinh = ?," + " gioitinh = ?, " + " coquan = ?," + " dangvien =?, " + " chucvu  = ? where cmnd = ?";
            try {
                PreparedStatement pre = ConnectDB.connect.prepareStatement(sqlUpdateCaNhan);
                pre.setString(1, ten);
                pre.setString(2, namSinh);
                boolean boolGioiTinh = gioiTinh.equals("Nam") ? true : false;
                pre.setBoolean(3, boolGioiTinh);
                pre.setString(4, coQuan);
                pre.setBoolean(5, dangVien);
                pre.setString(6, chucVu);
                pre.setString(7, cmnd);
                result = pre.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result == 1 ? true : false;
    }
}
