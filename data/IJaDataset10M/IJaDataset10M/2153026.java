package com.unmsm.fisi.clinica.ws.infraestructura.persistencia.hibernate.repositorio.impl;

import java.util.Collection;
import com.unmsm.fisi.clinica.ws.dominio.modelo.VirtualConsultation;
import com.unmsm.fisi.clinica.ws.dominio.modelo.Patient;
import com.unmsm.fisi.clinica.ws.infraestructura.persistencia.hibernate.repositorio.VirtualConsultationRepository;
import com.unmsm.fisi.clinica.ws.infraestructura.persistencia.hibernate.repositorio.HibernateRepository;

public class VirtualConsultationRepositoryHibernate extends HibernateRepository implements VirtualConsultationRepository {

    @Override
    public Collection<VirtualConsultation> getVirtualConsultationsOf(Patient patient) {
        String query = "SELECT patient.virtualConsultations FROM Patient patient " + "WHERE patient.id = " + patient.id();
        Collection<VirtualConsultation> virtualConsultations = getSession().createQuery(query).list();
        return virtualConsultations;
    }

    @Override
    public boolean saveVirtualConsultation(VirtualConsultation virtualConsultation) {
        try {
            getSession().save(virtualConsultation);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeVirtualConsultation(VirtualConsultation virtualConsultation) {
        String query = "DELETE FROM VirtualConsultation consultation" + " WHERE consultation.patient.id = " + virtualConsultation.patient().id() + " AND consultation.description like '" + virtualConsultation.description() + "'" + " AND consultation.doctor.id = " + virtualConsultation.doctor().id();
        try {
            getSession().createQuery(query).executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
