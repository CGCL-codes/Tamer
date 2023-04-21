package POJO;

/**
 *
 * @author Vannes
 */
public class pojoNguoiDung {

    public String TenDangNhap;

    public String MatKhau;

    public String HoTen;

    public String DienThoai;

    public String DiaChi;

    public String Email;

    public pojoPhanQuyen PhanQuyen;

    public pojoTinhTrangNguoiDung TinhTrang;

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public void setDienThoai(String DienThoai) {
        this.DienThoai = DienThoai;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.MatKhau = MatKhau;
    }

    public pojoPhanQuyen getPhanQuyen() {
        return PhanQuyen;
    }

    public void setPhanQuyen(pojoPhanQuyen PhanQuyen) {
        this.PhanQuyen = PhanQuyen;
    }

    public String getTenDangNhap() {
        return TenDangNhap;
    }

    public void setTenDangNhap(String TenDangNhap) {
        this.TenDangNhap = TenDangNhap;
    }

    public pojoTinhTrangNguoiDung getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(pojoTinhTrangNguoiDung TinhTrang) {
        this.TinhTrang = TinhTrang;
    }
}
