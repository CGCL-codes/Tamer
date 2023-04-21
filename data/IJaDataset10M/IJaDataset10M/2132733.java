package quanlydoanra.xoa;

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

public class XoaDoanRa {

    private String maDoan;

    private String error;

    private List<DoanRa> listDoanRa;

    private int i = 0;

    public XoaDoanRa() {
    }

    public XoaDoanRa(String maDoan, String error) {
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

    public void setListDoanRa(List<DoanRa> listDoanRa) {
        this.listDoanRa = listDoanRa;
    }

    public List<DoanRa> getListDoanRa() {
        if (i == 1) this.displayAll(); else this.displayDoanRa();
        return this.listDoanRa;
    }

    /**************************************
	 * Method name		: displayDoanRa
	 * Return type		: String
	 * Description		:
	 * Created date		: Aug 12, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public String displayDoanRa() {
        String result = "timdoanra";
        i = 0;
        this.setError("");
        List<DoanRa> list = new ArrayList<DoanRa>();
        String SQL = "";
        if (this.maDoan != null) {
            SQL = "select * from doanra where TRIM(LOWER(madoan)) LIKE TRIM(LOWER('%" + this.maDoan + "%')) and flagdelete = 0 ORDER BY madoan  DESC ";
        } else SQL = "select * from doanra where flagdelete = 0 ORDER BY madoan DESC";
        System.out.println("SQL " + SQL);
        ArrayList<HashMap<String, Object>> array = ConnectDB.getDataTypeList(SQL);
        if (array.size() > 0) {
            HashMap<String, Object> hash = new HashMap<String, Object>();
            int stt = 0;
            for (int i = 0; i < array.size(); i++) {
                stt = i + 1;
                hash = (HashMap<String, Object>) array.get(i);
                DoanRa doanRa = new DoanRa();
                doanRa.setSTT(String.valueOf(stt));
                doanRa.setMaDoan(hash.get("madoan").toString());
                doanRa.setLoaiDoan(hash.get("loaidoan").toString());
                String strNgayDi = DateUtils.formatDate(hash.get("ngaydi").toString());
                doanRa.setNgayDi(strNgayDi);
                String strNgayVe = DateUtils.formatDate(hash.get("ngayve").toString());
                doanRa.setNgayVe(strNgayVe);
                list.add(doanRa);
            }
        } else this.setError("Không tìm thấy đoàn có mã số đã nhập");
        this.setListDoanRa(list);
        return result;
    }

    /**************************************
	 * Method name		: displayAll
	 * Return type		: String
	 * Description		:
	 * Created date		: Aug 12, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public String displayAll() {
        i = 1;
        this.maDoan = null;
        this.displayDoanRa();
        return "timdoanra";
    }

    /**************************************
	 * Method name		: delete
	 * Return type		: String
	 * Description		:
	 * Created date		: Aug 12, 2008
	 * Author			: Ngoc Diem
	 **************************************/
    public String delete() {
        try {
            HttpServletRequest request = UtilsOnScopes.getRequest();
            String madoan = request.getParameter("maDoan");
            System.out.println("ma doan " + madoan);
            String userName = UtilsOnScopes.getUserName();
            if (!userName.equals("admin")) {
                if (truyXuatDatabase.getDuyet(madoan) == -1) {
                    this.setError("Bạn phải gửi yêu cầu đến người quản trị trước khi xóa đoàn ra này!");
                } else if (truyXuatDatabase.getDuyet(madoan) == 0) {
                    this.setError("Yêu cầu của bạn đang ở trạng thái chờ duyệt");
                } else {
                    if (truyXuatDatabase.xoaDoanRa(madoan)) {
                        this.setError("Xóa thành công đoàn ra!");
                        truyXuatDatabase.insertNhatKyXoaSua(DBUtils.getIDThanhVien() + "", DateUtils.utilDateToSqlDate(new Date()), madoan, "Xóa", "Xóa đoàn ra");
                    } else this.setError("Quá trình xóa đoàn ra bị lỗi!");
                }
            } else {
                if (truyXuatDatabase.xoaDoanRa(madoan)) {
                    this.setError("Xóa thành công đoàn ra!");
                    truyXuatDatabase.insertNhatKyXoaSua(DBUtils.getIDThanhVien() + "", DateUtils.utilDateToSqlDate(new Date()), madoan, "Xóa", "Xóa đoàn ra");
                } else this.setError("Quá trình xóa đoàn ra bị lỗi!");
            }
        } catch (Exception e) {
        }
        return "timdoanra";
    }

    public String guiYeuCau() {
        return "didentrangguiyeucau";
    }
}
