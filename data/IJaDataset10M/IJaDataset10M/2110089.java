package com.medics.dao;

import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.medics.entities.Appointment;

public class AppointmentDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Transactional
    public void save(Appointment appointment) {
        hibernateTemplate.saveOrUpdate(appointment);
    }

    @Transactional
    public void delete(String appointmentname) {
        Appointment appointment = (Appointment) hibernateTemplate.get(Appointment.class, appointmentname);
        hibernateTemplate.delete(appointment);
    }

    @Transactional(readOnly = true)
    public Appointment findById(Integer id) {
        return (Appointment) hibernateTemplate.get(Appointment.class, id);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        return hibernateTemplate.find("from Appointment");
    }
}
