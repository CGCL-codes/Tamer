package quanlyhochieu.soluongcapmoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import qldoanra.baocao.theothoigian.TableDisplay;
import quanlydoanra.utils.Utils;
import quanlydoanvao.xoa.DoanVao;
import utils.DateUtils;
import dbutil.ConnectDB;
import exportPDF.ExporToPDF;

public class HoChieuMoi {

    private String error;

    private java.util.Date ngayBatDau;

    private java.util.Date ngayKetThuc;

    private List<HoChieu> listHC;

    private boolean hienThiListHC = true;

    private int tongSoLuong;

    private String pathToListHC = "/qlhochieu/hochieucapmoi/empty.xhtml";

    public HoChieuMoi() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public java.util.Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(java.util.Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public java.util.Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(java.util.Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public boolean checkValue() {
        boolean result = false;
        String str;
        if (this.ngayBatDau == null || this.ngayBatDau.equals("") || this.ngayKetThuc == null || this.ngayKetThuc.equals("")) {
            result = false;
            this.setError("Nhập đầy đủ thông tin có dấu (*) phía trước");
        } else if (Utils.kiemTraNgay(this.ngayBatDau, this.ngayKetThuc) == false) {
            this.setError("Ngày bắt đầu phải trước ngày kết thúc! ");
            result = false;
        } else {
            this.setError("");
            result = true;
        }
        return result;
    }

    public List<HoChieu> chooseListHC() {
        List<HoChieu> list = new ArrayList<HoChieu>();
        if (this.checkValue()) {
            String SQL = "select * from hochieu as HC, canhan as CN where HC.cmnd = CN.cmnd ";
            SQL += "and ngaycap > '" + DateUtils.utilDateToSqlDate(this.ngayBatDau) + "' ";
            SQL += "and ngaycap < '" + DateUtils.utilDateToSqlDate(this.ngayKetThuc) + "'";
            System.out.println("SQL " + SQL);
            ArrayList<HashMap<String, Object>> array = ConnectDB.getDataTypeList(SQL);
            this.setTongSoLuong(array.size());
            if (array.size() > 0) {
                HashMap<String, Object> hash = new HashMap<String, Object>();
                int stt = 0;
                for (int i = 0; i < array.size(); i++) {
                    stt = i + 1;
                    hash = (HashMap<String, Object>) array.get(i);
                    HoChieu hoChieu = new HoChieu();
                    hoChieu.setStt(stt);
                    hoChieu.setCmnd(hash.get("cmnd").toString());
                    hoChieu.setCoQuan(hash.get("coquan").toString());
                    boolean boolGioiTinh = Boolean.parseBoolean(hash.get("gioitinh").toString());
                    String strGioiTinh = "";
                    if (boolGioiTinh) strGioiTinh = "Nam"; else strGioiTinh = "Nữ";
                    hoChieu.setGioiTinh(strGioiTinh);
                    hoChieu.setNamSinh(hash.get("namsinh").toString());
                    hoChieu.setSoHC(hash.get("sohc").toString());
                    hoChieu.setTen(hash.get("ten").toString());
                    list.add(hoChieu);
                    this.setError("");
                }
            }
        }
        return list;
    }

    public List<HoChieu> getListHC() {
        this.setListHC(this.chooseListHC());
        return listHC;
    }

    public void setListHC(List<HoChieu> listHC) {
        this.listHC = listHC;
    }

    public boolean isHienThiListHC() {
        return hienThiListHC;
    }

    public void setHienThiListHC(boolean hienThiListHC) {
        this.hienThiListHC = hienThiListHC;
    }

    public int getTongSoLuong() {
        return tongSoLuong;
    }

    public void setTongSoLuong(int tongSoLuong) {
        this.tongSoLuong = tongSoLuong;
    }

    public String reset() {
        this.ngayBatDau = null;
        this.ngayKetThuc = null;
        this.tongSoLuong = 0;
        this.listHC = null;
        this.setError("");
        return "thongkesoluonghc";
    }

    public String getPathToListHC() {
        if (this.hienThiListHC == false) this.pathToListHC = "/qlhochieu/hochieucapmoi/empty.xhtml"; else this.pathToListHC = "/qlhochieu/hochieucapmoi/listHC.xhtml";
        return pathToListHC;
    }

    public void setPathToListHC(String pathToListHC) {
        this.pathToListHC = pathToListHC;
    }

    public String thongKeSoLuongHC() {
        this.setListHC(this.chooseListHC());
        return "thongkesoluonghc";
    }

    public String XuatReport() {
        return "thongkesoluonghc";
    }
}
