package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojo.Sanpham;
import util.MyHibernateUtil;

/**
 *
 * @author TT
 */
public class SanPhamDAO {

    public static ArrayList<Sanpham> layDanhSachSanPham() {
        ArrayList<Sanpham> list = null;
        SessionFactory sf = MyHibernateUtil.getSessionFactory();
        Session ss = sf.openSession();
        try {
            ss.beginTransaction();
            String hql = "from pojo.Sanpham";
            list = (ArrayList<Sanpham>) ss.createQuery(hql).list();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public static ArrayList<Sanpham> layDanhSachSanPham(int maLoaiSanPham) {
        ArrayList<Sanpham> dsKQ = null;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            String hql = "from Sanpham where trangThai = 1 AND loaisanpham = :loaiSP";
            Query query = ss.createQuery(hql);
            query.setParameter("loaiSP", LoaiSanPhamDAO.layLoaiSPDuaVaoMa(maLoaiSanPham));
            dsKQ = (ArrayList<Sanpham>) query.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        return dsKQ;
    }

    public static ArrayList<Sanpham> layDanhSachSanPhamHot(int soSanPham) {
        ArrayList<Sanpham> dsKQ = new ArrayList<Sanpham>();
        String hql = "select sp " + "from Sanpham sp join sp.chitietdaugias ct " + "where sp.trangThai = 1 " + "group by sp.maSanPham " + "order by count(ct.maChiTietDauGia) desc";
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            Query query = ss.createQuery(hql);
            query.setFirstResult(0);
            query.setMaxResults(soSanPham);
            dsKQ = (ArrayList<Sanpham>) query.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        return dsKQ;
    }

    public static ArrayList<Sanpham> layDanhSachSanPhamMoiDang(int soSanPham) {
        ArrayList<Sanpham> dsKQ = new ArrayList<Sanpham>();
        String hql = "select sp " + "from Sanpham sp " + "where sp.trangThai = 1 " + "order by sp.ngayDang desc";
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            Query query = ss.createQuery(hql);
            query.setFirstResult(0);
            query.setMaxResults(soSanPham);
            dsKQ = (ArrayList<Sanpham>) query.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        return dsKQ;
    }

    public static Sanpham laySanPham(int maSanPham) {
        Sanpham tv = null;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            String hql = "from Sanpham where maSanPham = :maSanPham";
            Query query = ss.createQuery(hql);
            query.setInteger("maSanPham", maSanPham);
            tv = (Sanpham) query.uniqueResult();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        return tv;
    }

    /**
     * Lay danh sach san pham co phan trang
     * @param maLoaiSanPham
     * @param trang
     * @param soSPTrenTrang
     * @return ArrayList<Sanpham>
     */
    public static ArrayList<Sanpham> layDanhSachSanPham(int maLoaiSanPham, int trang, int soSPTrenTrang) {
        ArrayList<Sanpham> dsKQ = null;
        int a = (trang - 1) * soSPTrenTrang;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            String hql = "from Sanpham where loaisanpham = :loaiSP AND trangThai = 1";
            Query query = ss.createQuery(hql);
            query.setParameter("loaiSP", LoaiSanPhamDAO.layLoaiSPDuaVaoMa(maLoaiSanPham));
            if (soSPTrenTrang != -1) {
                query.setFirstResult(a);
                query.setMaxResults(soSPTrenTrang);
            }
            dsKQ = (ArrayList<Sanpham>) query.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        return dsKQ;
    }

    /**
     * Tim kiem sp co phan trang
     * @param maLoaiSanPham
     * @param tenSanPham
     * @param giaMin
     * @param giaMax
     * @param trang
     * @param soSPTrenTrang
     * @return 
     */
    public static ArrayList<Sanpham> timKiem(int maLoaiSanPham, String tenSanPham, float giaMin, float giaMax, int trang, int soSPTrenTrang) {
        ArrayList<Sanpham> dsKQ = new ArrayList<Sanpham>();
        int a = (trang - 1) * soSPTrenTrang;
        String hql = "from Sanpham where trangThai = 1 ";
        if (!tenSanPham.isEmpty()) {
            hql += "AND tenSanPham LIKE :tenSanPham ";
        }
        if (maLoaiSanPham != -1) {
            hql += "AND loaisanpham = :loaiSP ";
        }
        if (giaMin != -1) {
            hql += "AND giaHienTai >= :giaMin ";
        }
        if (giaMax != -1) {
            hql += "AND giaHienTai <= :giaMax ";
        }
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            Query query = ss.createQuery(hql);
            if (!tenSanPham.isEmpty()) {
                query.setString("tenSanPham", "%" + tenSanPham + "%");
            }
            if (maLoaiSanPham != -1) {
                query.setParameter("loaiSP", LoaiSanPhamDAO.layLoaiSPDuaVaoMa(maLoaiSanPham));
            }
            if (giaMin != -1) {
                query.setFloat("giaMin", giaMin);
            }
            if (giaMax != -1) {
                query.setFloat("giaMax", giaMax);
            }
            if (soSPTrenTrang != -1) {
                query.setFirstResult(a);
                query.setMaxResults(soSPTrenTrang);
            }
            dsKQ = (ArrayList<Sanpham>) query.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        return dsKQ;
    }

    /**
     * Lay so trang tim kiem duoc
     * @param maLoaiSanPham
     * @param tenSanPham
     * @param giaMin
     * @param giaMax
     * @param soSPTrenTrang
     * @return 
     */
    public static int laySoTrangTimKiemDuoc(int maLoaiSanPham, String tenSanPham, float giaMin, float giaMax, int soSPTrenTrang) {
        int kq = 0;
        String hql = "SELECT COUNT(*) FROM Sanpham WHERE trangThai = 1 ";
        if (!tenSanPham.isEmpty()) {
            hql += "AND tenSanPham LIKE '%" + tenSanPham + "%' ";
        }
        if (maLoaiSanPham != -1) {
            hql += "AND loaisanpham = :loaiSP ";
        }
        if (giaMin != -1) {
            hql += "AND giaHienTai >= " + giaMin + " ";
        }
        if (giaMax != -1) {
            hql += "AND giaHienTai <= " + giaMax + " ";
        }
        long temp = 0;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            Query query = ss.createQuery(hql);
            if (maLoaiSanPham != -1) {
                query.setParameter("loaiSP", LoaiSanPhamDAO.layLoaiSPDuaVaoMa(maLoaiSanPham));
            }
            temp = (Long) query.uniqueResult();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        if (soSPTrenTrang != -1) {
            kq = (int) temp / soSPTrenTrang;
            if (temp % soSPTrenTrang != 0) {
                kq++;
            }
        } else {
            kq = 1;
        }
        return kq;
    }

    /**
     * Xoa san pham
     * @param maSanPham
     * @return 
     */
    public static boolean xoaSanPham(int maSanPham) {
        boolean kq = true;
        Sanpham sp = laySanPham(maSanPham);
        sp.setTrangThai(false);
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            ss.update(sp);
            t.commit();
        } catch (Exception e) {
            t.rollback();
            kq = false;
            System.out.println(e);
        } finally {
            ss.close();
        }
        return kq;
    }

    public static boolean capNhatSanPham(Sanpham sp) {
        boolean kq = true;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            ss.update(sp);
            t.commit();
            ss.flush();
        } catch (Exception e) {
            t.rollback();
            kq = false;
            System.out.println(e);
        } finally {
            ss.close();
        }
        return kq;
    }

    public static int themSanPham(Sanpham sp) {
        int kq = 0;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        sp.setTrangThai(true);
        try {
            t.begin();
            kq = (Integer) ss.save(sp);
            t.commit();
        } catch (Exception e) {
            t.rollback();
            System.out.println(e);
        } finally {
            ss.close();
        }
        return kq;
    }

    public static int soTrang(int maLoaiSanPham, int soSPTrenTrang) {
        long temp = 0;
        String hql = "SELECT count(*) FROM Sanpham WHERE loaisanpham = :loaiSP AND trangThai = 1";
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        Transaction t = ss.getTransaction();
        try {
            t.begin();
            Query query = ss.createQuery(hql);
            query.setParameter("loaiSP", LoaiSanPhamDAO.layLoaiSPDuaVaoMa(maLoaiSanPham));
            temp = (Long) query.uniqueResult();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ss.close();
        }
        int soTrang;
        if (soSPTrenTrang != -1) {
            soTrang = (int) temp / soSPTrenTrang;
            if (temp % soSPTrenTrang != 0) {
                soTrang++;
            }
        } else {
            soTrang = 1;
        }
        return soTrang;
    }

    public static List<Sanpham> layDanhSachSPAd(int maLoaiSanPham) {
        List<Sanpham> list = null;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        String hql = "select sp from Sanpham sp where sp.loaisanpham.maLoaiSanPham=:maLoaiSanPham";
        try {
            ss.beginTransaction();
            list = ss.createQuery(hql).setParameter("maLoaiSanPham", maLoaiSanPham).list();
            ss.close();
        } catch (Exception ex) {
            list = null;
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public static List<Sanpham> layDanhSachSPAd() {
        List<Sanpham> list = null;
        Session ss = MyHibernateUtil.getSessionFactory().openSession();
        try {
            ss.beginTransaction();
            list = ss.createQuery("from pojo.Sanpham").list();
            ss.close();
        } catch (Exception ex) {
            list = null;
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
