package main.controladores;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import main.rim.entity.OrganizationBean;

@Stateless
public class IngresoServicioController implements IIngresoServicioController {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public boolean existeServ(String nombre) {
        List existenombre = em.createQuery("select org from OrganizationBean org where org.name=:nom").setParameter("nom", nombre).getResultList();
        return (existenombre.isEmpty()) ? false : true;
    }

    public void guardarServicio(OrganizationBean org) {
        em.persist(org);
    }
}
