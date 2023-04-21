package com.pn.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import com.kn.core.dao.BaseDao;
import com.pn.bo.TinhChiPhiSanXuatChungBean;

public class TinhChiPhiSanXuatChungDao extends BaseDao {

    public List<TinhChiPhiSanXuatChungBean> getAllTinhChiPhiSanXuatChung(Long yeuCauId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TinhChiPhiSanXuatChungBean.class);
        criteria.createAlias("yeuCauId", "yeucau");
        criteria.add(Property.forName("yeucau.id").eq(yeuCauId));
        criteria.addOrder(Order.asc("id"));
        List<TinhChiPhiSanXuatChungBean> tinhPSXCList = this.getHibernateTemplate().findByCriteria(criteria);
        return tinhPSXCList;
    }

    public List<TinhChiPhiSanXuatChungBean> getTinhChiPhiSanXuatChungPerNgayHieuLuc(Long thanhPhamId, Date ngayHieuLuc) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TinhChiPhiSanXuatChungBean.class);
        criteria.createAlias("yeuCauId", "yeucau");
        criteria.createAlias("yeucau.thanhPhamId", "thanhpham");
        criteria.add(Property.forName("thanhpham.id").eq(thanhPhamId));
        criteria.add(Property.forName("ngayHieuLuc").le(ngayHieuLuc));
        List<TinhChiPhiSanXuatChungBean> tinhPSXCList = this.getHibernateTemplate().findByCriteria(criteria);
        return tinhPSXCList;
    }

    public void deleteAllChiPhiSXPerYeuCauid(Long yeuCauId) {
        List<TinhChiPhiSanXuatChungBean> cpsxchung = this.getAllTinhChiPhiSanXuatChung(yeuCauId);
        while (cpsxchung != null && cpsxchung.size() > 0) {
            for (TinhChiPhiSanXuatChungBean cpBean : cpsxchung) {
                this.delete(cpBean);
            }
        }
    }

    public void deleteChiPhiSXPerYeuCauid(Long yeuCauId) {
        Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
        String queryString = "delete from TinhChiPhiSanXuatChungBean where yeuCauId = :tyeuCauid";
        Query query = session.createQuery(queryString);
        query.setParameter("tyeuCauid", yeuCauId);
        query.executeUpdate();
    }

    public TinhChiPhiSanXuatChungBean getTinhChiPhiSanXuatChung(Long tinhChiPhiSanXuatChungId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TinhChiPhiSanXuatChungBean.class);
        criteria.add(Property.forName("id").eq(tinhChiPhiSanXuatChungId));
        List<TinhChiPhiSanXuatChungBean> tinhPSXCList = this.getHibernateTemplate().findByCriteria(criteria);
        if (tinhPSXCList != null && tinhPSXCList.size() > 0) {
            return tinhPSXCList.get(0);
        }
        return null;
    }
}
