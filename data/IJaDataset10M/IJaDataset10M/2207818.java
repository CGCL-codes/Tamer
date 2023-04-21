package com.orange.erp.bean;

import java.util.Date;

public class ChinhSachGiaBean extends BaseBean {

    private Long sanPhamId;

    private Double giaBanNoVAT;

    private Double giaGiamBm;

    private Date ngayHieuLuc;

    /** For display only, not for save**/
    private SanPhamBean sanPham;

    public ChinhSachGiaBean() {
    }

    public ChinhSachGiaBean(Long id, Long sanPhamId, Double giaBanNoVAT, Double giaGiamBm, Date ngayHieuLuc) {
        setId(id);
        this.sanPhamId = sanPhamId;
        this.giaBanNoVAT = giaBanNoVAT;
        this.giaGiamBm = giaGiamBm;
        this.ngayHieuLuc = ngayHieuLuc;
    }

    public Long getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(Long sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public Double getGiaBanNoVAT() {
        return giaBanNoVAT;
    }

    public void setGiaBanNoVAT(Double giaBanNoVAT) {
        this.giaBanNoVAT = giaBanNoVAT;
    }

    public Double getGiaGiamBm() {
        return giaGiamBm;
    }

    public void setGiaGiamBm(Double giaGiamBm) {
        this.giaGiamBm = giaGiamBm;
    }

    public Date getNgayHieuLuc() {
        return ngayHieuLuc;
    }

    public void setNgayHieuLuc(Date ngayHieuLuc) {
        this.ngayHieuLuc = ngayHieuLuc;
    }

    public SanPhamBean getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPhamBean sanPham) {
        this.sanPham = sanPham;
    }
}
