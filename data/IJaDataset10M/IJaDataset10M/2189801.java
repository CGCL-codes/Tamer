package com.pn.bo;

import java.util.Date;
import java.util.Set;

public class DinhMucVatTuBean extends BaseBean {

    private Set<DinhMucVatTuRowBean> dinhMucVatTuRowList;

    private Double thanhTienTongCong;

    private Long lanHieuChinh;

    private Date ngayHieuChinh;

    private String userUpdate;

    private YeuCauTinhGiaThanhBean yeuCauId;

    public void setDinhMucVatTuRowList(Set<DinhMucVatTuRowBean> dinhMucVatTuRowList) {
        this.dinhMucVatTuRowList = dinhMucVatTuRowList;
    }

    public Set<DinhMucVatTuRowBean> getDinhMucVatTuRowList() {
        return dinhMucVatTuRowList;
    }

    public void setThanhTienTongCong(Double thanhTienTongCong) {
        this.thanhTienTongCong = thanhTienTongCong;
    }

    public Double getThanhTienTongCong() {
        return thanhTienTongCong;
    }

    public void setLanHieuChinh(Long lanHieuChinh) {
        this.lanHieuChinh = lanHieuChinh;
    }

    public Long getLanHieuChinh() {
        return lanHieuChinh;
    }

    public void setNgayHieuChinh(Date ngayHieuChinh) {
        this.ngayHieuChinh = ngayHieuChinh;
    }

    public Date getNgayHieuChinh() {
        return ngayHieuChinh;
    }

    public void setYeuCauId(YeuCauTinhGiaThanhBean yeuCauId) {
        this.yeuCauId = yeuCauId;
    }

    public YeuCauTinhGiaThanhBean getYeuCauId() {
        return yeuCauId;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public String getUserUpdate() {
        return userUpdate;
    }
}
