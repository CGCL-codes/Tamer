package com.pn.services;

import java.util.List;
import com.kn.core.services.BaseServices;
import com.pn.bo.DonViSanXuatBean;
import com.pn.dao.DonViSanXuatDao;

public class DonViSanXuatServices extends BaseServices {

    private DonViSanXuatDao donViDao;

    public void addDonViSanXuat(String maDonVi, String tenDonVi, String ghiChu) throws Exception {
        if (isExistedDonViSanXuat(maDonVi)) {
            throw new Exception("Ma Danh Muc da ton tai");
        }
        DonViSanXuatBean donViBean = new DonViSanXuatBean();
        donViBean.setMaDonVi(maDonVi);
        donViBean.setTenDonVi(tenDonVi);
        donViBean.setGhiChu(ghiChu);
        donViDao.save(donViBean);
    }

    public void deleteDonViSanXuat(Long donViId) throws Exception {
        if (!isExistedDonViSanXuat(donViId)) {
            throw new Exception("Ma Danh Muc khong ton tai");
        }
        DonViSanXuatBean donViBean = donViDao.getDonViSanXuat(donViId);
        donViDao.delete(donViBean);
    }

    public List<DonViSanXuatBean> getAllDonViSanXuat() throws Exception {
        return donViDao.getAllDonViSanXuat();
    }

    public String getMaTuDongMoi() {
        return donViDao.getMaTuDongMoi();
    }

    public DonViSanXuatBean getDonViSanXuat(String maDonVi) throws Exception {
        return donViDao.getDonViSanXuat(maDonVi);
    }

    public DonViSanXuatBean getDonViSanXuat(Long donViId) throws Exception {
        return donViDao.getDonViSanXuat(donViId);
    }

    public void updateDonViSanXuat(Long donViId, String maDonVi, String tenDonVi, String ghiChu) throws Exception {
        DonViSanXuatBean donViBean = donViDao.getDonViSanXuat(donViId);
        if (donViBean != null) {
            donViBean.setMaDonVi(maDonVi);
            donViBean.setTenDonVi(tenDonVi);
            donViBean.setGhiChu(ghiChu);
            donViDao.saveOrUpdate(donViBean);
        }
    }

    public boolean isExistedDonViSanXuat(String maDonVi) throws Exception {
        DonViSanXuatBean donViBean = getDonViSanXuat(maDonVi);
        if (donViBean != null) {
            return true;
        }
        return false;
    }

    public boolean isExistedDonViSanXuat(Long donViId) throws Exception {
        DonViSanXuatBean donViBean = getDonViSanXuat(donViId);
        if (donViBean != null) {
            return true;
        }
        return false;
    }

    public void setDonViDao(DonViSanXuatDao donViDao) {
        this.donViDao = donViDao;
    }

    public DonViSanXuatDao getDonViDao() {
        return donViDao;
    }
}
