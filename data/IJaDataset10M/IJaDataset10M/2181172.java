package com.pn.bo;

public class FactDeXuatGiaBanBO extends BaseBO {

    private DimThanhPhamBO id_thanhpham;

    private DimNgayBO id_ngay_hieuchinh;

    private DimNgayBO id_ngay_phathanh;

    private DimNgayBO id_ngay_pheduyet;

    private KhuVucBanHangBo id_khu_vuc_ban_hang;

    private Double f_giaban_plus_v;

    private Double f_phantram_tieuthu;

    private Double f_giaban;

    private String state;

    private Integer lanhieuchinh;

    public DimThanhPhamBO getId_thanhpham() {
        return id_thanhpham;
    }

    public void setId_thanhpham(DimThanhPhamBO idThanhpham) {
        id_thanhpham = idThanhpham;
    }

    public KhuVucBanHangBo getId_khu_vuc_ban_hang() {
        return id_khu_vuc_ban_hang;
    }

    public void setId_khu_vuc_ban_hang(KhuVucBanHangBo idKhuVucBanHang) {
        id_khu_vuc_ban_hang = idKhuVucBanHang;
    }

    public Double getF_giaban_plus_v() {
        return f_giaban_plus_v;
    }

    public void setF_giaban_plus_v(Double fGiabanPlusV) {
        f_giaban_plus_v = fGiabanPlusV;
    }

    public Double getF_phantram_tieuthu() {
        return f_phantram_tieuthu;
    }

    public void setF_phantram_tieuthu(Double fPhantramTieuthu) {
        f_phantram_tieuthu = fPhantramTieuthu;
    }

    public Double getF_giaban() {
        return f_giaban;
    }

    public void setF_giaban(Double fGiaban) {
        f_giaban = fGiaban;
    }

    public DimNgayBO getId_ngay_hieuchinh() {
        return id_ngay_hieuchinh;
    }

    public void setId_ngay_hieuchinh(DimNgayBO idNgayHieuchinh) {
        id_ngay_hieuchinh = idNgayHieuchinh;
    }

    public DimNgayBO getId_ngay_phathanh() {
        return id_ngay_phathanh;
    }

    public void setId_ngay_phathanh(DimNgayBO idNgayPhathanh) {
        id_ngay_phathanh = idNgayPhathanh;
    }

    public DimNgayBO getId_ngay_pheduyet() {
        return id_ngay_pheduyet;
    }

    public void setId_ngay_pheduyet(DimNgayBO idNgayPheduyet) {
        id_ngay_pheduyet = idNgayPheduyet;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getLanhieuchinh() {
        return lanhieuchinh;
    }

    public void setLanhieuchinh(Integer lanhieuchinh) {
        this.lanhieuchinh = lanhieuchinh;
    }
}
