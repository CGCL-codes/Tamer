package quanlydoanvao.xoa;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import quanlydoanra.utils.TableNguoiDi;
import quanlydoanra.utils.truyXuatDatabase;
import utils.DateUtils;
import utils.UtilsOnScopes;
import utils.Validate;
import dbutil.ConnectDB;
import dbutil.DBUtils;

public class XoaDoanVao {

    private String maDoan;

    private String error;

    private List<DoanVao> listDoanVao = null;

    private int i = 0;

    public XoaDoanVao() {
    }

    public XoaDoanVao(String maDoan, String error) {
        this.maDoan = maDoan;
        this.error = error;
    }

    public String getMaDoan() {
        return maDoan;
    }

    public void setMaDoan(String maDoan) {
        this.maDoan = maDoan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setListDoanVao(List<DoanVao> listDoanVao) {
        this.listDoanVao = listDoanVao;
    }

    public List<DoanVao> getListDoanVao() {
        if (i == 1) this.displayAll(); else this.displayDoanVao();
        return this.listDoanVao;
    }

    public String displayDoanVao() {
        String result = "timdoanvao";
        i = 0;
        this.setError("");
        List<DoanVao> list = new ArrayList<DoanVao>();
        String SQL = "";
        if (this.maDoan != null) {
            SQL = "select * from doanvao as DV, vanbanden as VBD  where DV.idvbden = VBD.id and madoan = '" + this.maDoan + "' and flagdelete = 0";
        } else SQL = "select * from doanvao as DV, vanbanden as VBD where DV.idvbden = VBD.id and flagdelete = 0 ORDER BY madoan";
        System.out.println("SQL " + SQL);
        ArrayList<HashMap<String, Object>> array = ConnectDB.getDataTypeList(SQL);
        if (array.size() > 0) {
            HashMap<String, Object> hash = new HashMap<String, Object>();
            int stt = 0;
            for (int i = 0; i < array.size(); i++) {
                stt = i + 1;
                hash = (HashMap<String, Object>) array.get(i);
                DoanVao doanVao = new DoanVao();
                doanVao.setStt(String.valueOf(stt));
                doanVao.setMaDoanVao(hash.get("madoan").toString());
                doanVao.setNgayDen(hash.get("ngayden").toString());
                String strNgayDi = hash.get("ngaydi").toString();
                doanVao.setNgayDi(strNgayDi);
                doanVao.setDiaDiemLamViec(hash.get("diadiemlamviec").toString());
                doanVao.setCoQuanDeNghi(hash.get("coquandenghi").toString());
                list.add(doanVao);
            }
        } else this.setError("Không tìm thấy đoàn có mã số đã nhập");
        this.setListDoanVao(list);
        return result;
    }

    public String displayAll() {
        i = 1;
        this.maDoan = null;
        this.displayDoanVao();
        return "timdoanvao";
    }

    public String delete() {
        HttpServletRequest request = UtilsOnScopes.getRequest();
        String madoan = request.getParameter("maDoan");
        System.out.println("ma doan " + madoan);
        if (truyXuatDatabase.getDuyet(madoan) == -1) {
            this.setError("Bạn phải gửi yêu cầu đến người quản trị trước khi sửa đoàn vào này!");
        } else if (truyXuatDatabase.getDuyet(madoan) == 0) {
            this.setError("Yêu cầu của bạn đang ở trạng thái chờ duyệt");
        } else {
            if (truyXuatDatabase.xoaDoanVao(madoan)) {
                this.setError("Xóa thành công đoàn vào!");
            } else this.setError("Quá trình xóa đoàn vào bị lỗi!");
        }
        return "timdoanvao";
    }
}
