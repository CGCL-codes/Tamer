package org.hmaciel.sisingr.otros.Log;

import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class LogCUso implements ILogCUso {

    @PersistenceContext
    EntityManager em;

    public void registrarLog(Date fecha, String usuario, String actividad) {
        LogBean log = new LogBean();
        log.setFecha(fecha);
        log.setUsuario(usuario);
        log.setActividad(actividad);
        em.persist(log);
    }
}
