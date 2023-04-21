package com.pn.services;

import java.util.List;
import com.kn.core.services.BaseServices;
import com.pn.bo.PhiSanXuatChungBean;
import com.pn.dao.PhiSanXuatChungDao;

public class PhiSanXuatChungServices extends BaseServices {

    private PhiSanXuatChungDao phiSanXuatChungDao;

    public void addPhiSanXuatChung(String maChiPhi, String tenChiPhi, String ghiChu) throws Exception {
        if (isExistedPhiSanXuatChung(maChiPhi)) {
            throw new Exception("Ma Chi Phi da ton tai");
        }
        PhiSanXuatChungBean phiSanXuatChungBean = new PhiSanXuatChungBean();
        phiSanXuatChungBean.setMaChiPhi(maChiPhi);
        phiSanXuatChungBean.setTenChiPhi(tenChiPhi);
        phiSanXuatChungBean.setGhiChu(ghiChu);
        phiSanXuatChungDao.save(phiSanXuatChungBean);
    }

    public void deletePhiSanXuatChung(Long phiSanXuatChungId) throws Exception {
        if (!isExistedPhiSanXuatChung(phiSanXuatChungId)) {
            throw new Exception("Ma Chi Phi khong ton tai");
        }
        PhiSanXuatChungBean phiSanXuatChungBean = phiSanXuatChungDao.getPhiSanXuatChung(phiSanXuatChungId);
        phiSanXuatChungDao.delete(phiSanXuatChungBean);
    }

    public List<PhiSanXuatChungBean> getAllPhiSanXuatChung() throws Exception {
        return phiSanXuatChungDao.getAllPhiSanXuatChung();
    }

    public String getMaTuDongMoi() {
        return phiSanXuatChungDao.getMaTuDongMoi();
    }

    public PhiSanXuatChungBean getPhiSanXuatChung(String maChiPhi) throws Exception {
        return phiSanXuatChungDao.getPhiSanXuatChung(maChiPhi);
    }

    public PhiSanXuatChungBean getPhiSanXuatChung(Long phiSanXuatChungId) throws Exception {
        return phiSanXuatChungDao.getPhiSanXuatChung(phiSanXuatChungId);
    }

    public void updatePhiSanXuatChung(Long phiSanXuatChungId, String maChiPhi, String tenChiPhi, String ghiChu) throws Exception {
        PhiSanXuatChungBean phiSanXuatChungBean = phiSanXuatChungDao.getPhiSanXuatChung(phiSanXuatChungId);
        if (phiSanXuatChungBean != null) {
            phiSanXuatChungBean.setMaChiPhi(maChiPhi);
            phiSanXuatChungBean.setTenChiPhi(tenChiPhi);
            phiSanXuatChungBean.setGhiChu(ghiChu);
            phiSanXuatChungDao.saveOrUpdate(phiSanXuatChungBean);
        }
    }

    public boolean isExistedPhiSanXuatChung(String maChiPhi) throws Exception {
        PhiSanXuatChungBean phiSanXuatChungBean = getPhiSanXuatChung(maChiPhi);
        if (phiSanXuatChungBean != null) {
            return true;
        }
        return false;
    }

    public boolean isExistedPhiSanXuatChung(Long phiSanXuatChungId) throws Exception {
        PhiSanXuatChungBean phiSanXuatChungBean = getPhiSanXuatChung(phiSanXuatChungId);
        if (phiSanXuatChungBean != null) {
            return true;
        }
        return false;
    }

    public void setPhiSanXuatChungDao(PhiSanXuatChungDao phiSanXuatChungDao) {
        this.phiSanXuatChungDao = phiSanXuatChungDao;
    }

    public PhiSanXuatChungDao getPhiSanXuatChungDao() {
        return phiSanXuatChungDao;
    }
}
