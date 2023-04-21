package com.orange.erp.bean;

import java.util.Date;
import java.util.Set;

public class KhachHangBean extends BaseBean {

    private String ma;

    private String ten;

    private String diaChi;

    private String deskPhone;

    private String cellPhone;

    private String fax;

    private String email;

    private String nguoiLienHe;

    private String chucVu;

    private String loai;

    private String bankAccountNumber;

    private String bankName;

    private String tinhTrang;

    private String moTa;

    private Boolean isDeleted;

    private Date dateLastUpdated;

    private String userLastUpdated;

    /** For display only, not for save **/
    private Set<TinDungBean> tinDungs;

    private LoaiSanPhamBean loaiSanPham;

    public KhachHangBean() {
    }

    public KhachHangBean(Long id, String ma, String ten, String diaChi, String deskPhone, String cellPhone, String fax, String email, String nguoiLienHe, String chucVu, String loai, String bankAccountNumber, String bankName, String tinhTrang, String moTa, Boolean isDeleted, Date dateLastUpdated, String userLastUpdated) {
        setId(id);
        this.ma = ma;
        this.ten = ten;
        this.diaChi = diaChi;
        this.deskPhone = deskPhone;
        this.cellPhone = cellPhone;
        this.fax = fax;
        this.email = email;
        this.nguoiLienHe = nguoiLienHe;
        this.chucVu = chucVu;
        this.loai = loai;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.tinhTrang = tinhTrang;
        this.moTa = moTa;
        this.isDeleted = isDeleted;
        this.dateLastUpdated = dateLastUpdated;
        this.userLastUpdated = userLastUpdated;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDeskPhone() {
        return deskPhone;
    }

    public void setDeskPhone(String deskPhone) {
        this.deskPhone = deskPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNguoiLienHe() {
        return nguoiLienHe;
    }

    public void setNguoiLienHe(String nguoiLienHe) {
        this.nguoiLienHe = nguoiLienHe;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getUserLastUpdated() {
        return userLastUpdated;
    }

    public void setUserLastUpdated(String userLastUpdated) {
        this.userLastUpdated = userLastUpdated;
    }

    public Set<TinDungBean> getTinDungs() {
        return tinDungs;
    }

    public void setTinDungs(Set<TinDungBean> tinDungs) {
        this.tinDungs = tinDungs;
    }

    public LoaiSanPhamBean getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPhamBean loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}
