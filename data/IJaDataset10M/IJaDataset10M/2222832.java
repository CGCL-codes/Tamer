package com.opendicom.miniRIS;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.opendicom.miniRIS.entidades.Procedimiento;
import com.opendicom.miniRIS.interfaces.ABM_ProcedimientoCU;

@Stateless
public class ABM_ProcedimientoCUBean implements ABM_ProcedimientoCU {

    @PersistenceContext
    private EntityManager em;

    public void guardarProcedimiento(Procedimiento p) {
        em.persist(p);
    }

    @SuppressWarnings("unchecked")
    public boolean existeProcedimiento(String codigo) {
        List existe = em.createQuery("select p from Procedimiento p where p.id=:codigo").setParameter("codigo", codigo).getResultList();
        return (existe.isEmpty()) ? false : true;
    }

    @SuppressWarnings("unchecked")
    public List<Procedimiento> listarProcedimientos(String modalidad) {
        List<Procedimiento> result = new LinkedList<Procedimiento>();
        if (modalidad.compareTo("todas") == 0) {
            result = em.createQuery("select p from Procedimiento p").getResultList();
        } else {
            result = em.createQuery("select p from Procedimiento p where p.modalidad=:modalidad").setParameter("modalidad", modalidad).getResultList();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Procedimiento buscarProcPorDefecto(String modalidad) {
        Procedimiento result = null;
        List<Procedimiento> aux = new LinkedList<Procedimiento>();
        if (modalidad.compareTo("CT") == 0) {
            aux = em.createQuery("select p from Procedimiento p where p.id='Tomografia'").getResultList();
        } else if (modalidad.compareTo("US") == 0) {
            aux = em.createQuery("select p from Procedimiento p where p.id='Ecografia'").getResultList();
        } else {
            aux = em.createQuery("select p from Procedimiento p where p.id='Rayos'").getResultList();
        }
        if (!aux.isEmpty()) {
            result = aux.get(0);
        }
        return result;
    }
}
