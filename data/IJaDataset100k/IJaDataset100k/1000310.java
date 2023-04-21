package org.mersys.sagi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mersys.sagi.modelo.Area;
import org.mersys.sagi.modelo.Permiso;
import org.mersys.sagi.modelo.Usuario;
import org.mersys.utilidades.Iniciador;

/**
 *
 * @author Oficina
 */
public class DaoUsuario {

    private static final Logger log = Logger.getLogger(DaoUsuario.class.getName());

    private Session ss;

    private Transaction tx;

    public DaoUsuario() {
    }

    /** Permite obtener la Session actual o la crea si no existe alguna, tambien
     *  crea una Transaccion.
     *
     * @throws org.hibernate.HibernateException
     */
    private void Iniciar() throws HibernateException {
        ss = Iniciador.getSessionFactory().getCurrentSession();
        tx = ss.beginTransaction();
    }

    /** Realiza un rollback en caso de que ocurra una execpcion
     * en la capa de accedo a datos o JDBC.
     *
     */
    private void admExcepcion(HibernateException ex) {
        if (tx != null) {
            tx.rollback();
        }
        log.info("HAP: Error de Hibernate, detalle:" + ex);
    }

    /**Devuelve un Objeto tipo Usuario deacuerdo al Login pasado como parametro,
     * si no existe o hay mas de uno con el mismo Login devuelve un valor NULL.*/
    public Usuario getUsuario(String usu) {
        log.info("HAP: Iniciando getUsuario(" + usu + ")");
        Usuario resultado = null;
        try {
            Iniciar();
            String hql = "from Usuario where usuario=:USU";
            Query q = ss.createQuery(hql);
            q.setParameter("USU", usu);
            resultado = (Usuario) q.uniqueResult();
            if (resultado == null) {
                log.info("HAP: Usuario es NULL");
            } else {
                log.info("HAP: Cargando Usuario:" + resultado.getUsuario());
            }
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return resultado;
    }

    public List<Permiso> getPermisos(Usuario usu) {
        log.info("HAP: Iniciando getPermisos(usu)");
        List<Permiso> lstper = new ArrayList<Permiso>();
        try {
            Iniciar();
            String hql = "from Permiso p where p.usuario=:USU";
            Query q = ss.createQuery(hql);
            q.setEntity("USU", usu);
            lstper = q.list();
            log.info("HAP: Obteniendo la lista de Permisos, lst.size:" + lstper.size());
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return lstper;
    }

    public Permiso getPermiso(Area area, Usuario usu) {
        log.info("HAP: Iniciando getPermisos(usu)");
        Permiso per = null;
        if (area == null) {
            log.info("HAP: Area es NULL");
            return per;
        }
        if (usu == null) {
            log.info("HAP: Usuario es NULL");
            return per;
        }
        try {
            Iniciar();
            String hql = "from Permiso where usuario=:USU and area=:ARE";
            Query q = ss.createQuery(hql);
            q.setEntity("USU", usu);
            q.setEntity("ARE", area);
            per = (Permiso) q.uniqueResult();
            log.info("HAP: Obteniendo el Permiso correspondiente a USUARIO:" + usu.getUsuario() + ", AREA:" + area.getNombre());
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return per;
    }

    public Area getArea(long id) {
        log.info("HAP: Iniciando getArea(" + id + ")");
        Area area = null;
        try {
            Iniciar();
            area = (Area) ss.get(Area.class, id);
            log.info("HAP: Obteniendo el Area");
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return area;
    }
}
