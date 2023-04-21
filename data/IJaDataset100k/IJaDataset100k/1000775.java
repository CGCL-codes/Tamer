package com.medics.dao;

import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.medics.entities.MedicHistory;

public class MedicHistoryDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Transactional
    public void save(MedicHistory medicHistory) {
        hibernateTemplate.saveOrUpdate(medicHistory);
    }

    @Transactional
    public void delete(String medicHistoryname) {
        MedicHistory medicHistory = (MedicHistory) hibernateTemplate.get(MedicHistory.class, medicHistoryname);
        hibernateTemplate.delete(medicHistory);
    }

    @Transactional(readOnly = true)
    public MedicHistory findById(Integer id) {
        return (MedicHistory) hibernateTemplate.get(MedicHistory.class, id);
    }

    @Transactional(readOnly = true)
    public List<MedicHistory> findAll() {
        return hibernateTemplate.find("from MedicHistory");
    }
}
