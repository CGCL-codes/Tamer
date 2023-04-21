package org.humboldt.cassia.core;

import java.util.Collection;
import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.humboldt.cassia.core.jdo.EstadoCron;

/**
 * Clase encargada de las operaciones de actualizaci�n de la informaci�n de estandares
 * @author Benjamin A. Rodriguez R. benjamin.a.rodriguez@gmail.com
 *
 */
public class FacadeOperacionesEstadoWebDavCassiaCore {

    private PersistenceManagerFactory factory;

    public PersistenceManagerFactory getFactory() {
        return factory;
    }

    public void setFactory(PersistenceManagerFactory factory) {
        this.factory = factory;
    }

    /**
     * Registra el estandar en la Base de datos
     * @param estandar
     * @param atributoPrincipal
     * @param niveles
     * @param usuario
     */
    public void crearEstadoWebDav(EstadoCron estadowebdav) {
        PersistenceManager pm = factory.getPersistenceManager();
        Transaction tx = null;
        try {
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(estadowebdav);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            pm.close();
        }
    }

    public void actualizarEstadoWebDav(EstadoCron estadowebdav) {
        PersistenceManager pm = factory.getPersistenceManager();
        Transaction tx = null;
        try {
            tx = pm.currentTransaction();
            tx.begin();
            EstadoCron estadowebdavAnterior = new EstadoCron();
            Extent e = pm.getExtent(EstadoCron.class, true);
            Query q = pm.newQuery(e, "IDOBJETO == " + estadowebdav.getIDOBJETO());
            Collection cllestadowebdav = (Collection) q.execute();
            estadowebdavAnterior = (EstadoCron) cllestadowebdav.iterator().next();
            estadowebdavAnterior.setIDTIPOESTADO(estadowebdav.getIDTIPOESTADO());
            estadowebdavAnterior.setIDOBJETO(estadowebdav.getIDOBJETO());
            estadowebdavAnterior.setIDTIPOBJETO(estadowebdav.getIDTIPOBJETO());
            estadowebdavAnterior.setOBJETO(estadowebdav.getOBJETO());
            estadowebdavAnterior.setWebdav(estadowebdav.getWebdav());
            pm.makePersistent(estadowebdavAnterior);
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en facade actualizar estado webdav:" + e.getMessage());
            tx.rollback();
        } finally {
            pm.close();
        }
    }
}
