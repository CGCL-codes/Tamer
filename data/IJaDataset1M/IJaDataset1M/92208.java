package org.opensih.comunes.Controladores;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.opensih.comunes.Modelo.Configuracion;

@Stateless
public class Conf implements IConf {

    @PersistenceContext
    EntityManager em;

    public void agregarValor(String nombre, String valor) {
        Configuracion c = new Configuracion();
        c.setNombre(nombre);
        c.setValor(valor);
        em.persist(c);
    }

    public void modificarValor(String nombre, String valor) {
        Configuracion c = (Configuracion) em.createQuery("SELECT c FROM Configuracion c WHERE c.nombre=:nom").setParameter("nom", nombre).getSingleResult();
        c.setValor(valor);
    }

    public String buscarValor(String nombre) {
        Configuracion c = (Configuracion) em.createQuery("SELECT c FROM Configuracion c WHERE c.nombre=:nom").setParameter("nom", nombre).getSingleResult();
        return c.getValor();
    }

    @SuppressWarnings("unchecked")
    public boolean existeValor(String nombre) {
        List<Configuracion> lista = em.createQuery("SELECT c FROM Configuracion c WHERE c.nombre=:nom").setParameter("nom", nombre).getResultList();
        return !lista.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public List<Configuracion> listarConfiguracion() {
        List<Configuracion> lista = em.createQuery("SELECT c FROM Configuracion c").getResultList();
        return lista;
    }
}
