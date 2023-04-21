package org.hmaciel.descop.ejb.controladores;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hmaciel.descop.ejb.role.RoleBean;

@Stateless
public class AltaMedicoCU implements IAltaMedicoCU {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public boolean existeMed(String cedula) {
        List existeCi = em.createQuery("select med from EntityBean med join med.id iis_med where iis_med.extension=:ii").setParameter("ii", cedula).getResultList();
        return (existeCi.isEmpty()) ? false : true;
    }

    public void guardarMedico(RoleBean med) {
        em.persist(med);
    }
}
