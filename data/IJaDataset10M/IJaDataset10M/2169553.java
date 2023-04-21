package es.wtestgen.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import es.wtestgen.bean.profesor.RespuestaForm;
import es.wtestgen.domain.Asignatura;
import es.wtestgen.domain.Pregunta;
import es.wtestgen.domain.Respuesta;
import es.wtestgen.util.HibernateUtil;

public class RespuestaDAO {

    private static final Log log = LogFactory.getLog(RespuestaDAO.class);

    public RespuestaDAO() {
    }

    public boolean eliminar(int codResp) {
        Transaction tx = null;
        Session session = HibernateUtil.currentSession();
        boolean eliminado = false;
        try {
            tx = session.beginTransaction();
            Respuesta respuesta = (Respuesta) session.get(Respuesta.class, codResp);
            Pregunta pregunta = (Pregunta) session.get(Pregunta.class, respuesta.getPregunta().getCodPreg());
            pregunta.getRespuestas().remove(respuesta);
            session.delete(respuesta);
            tx.commit();
            eliminado = true;
        } catch (Exception e) {
            try {
                tx.rollback();
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            HibernateUtil.closeSession();
        }
        return eliminado;
    }

    public List findAll() {
        List lista = new ArrayList();
        Criteria criteria = null;
        try {
            Session session = HibernateUtil.currentSession();
            criteria = session.createCriteria(Respuesta.class);
            lista = criteria.list();
        } catch (HibernateException e) {
            log.error("_____Error al obtener la todos los registros de la clase Respuesta", e);
            throw new HibernateException(e);
        }
        return lista;
    }

    public List findById(String cod) {
        List lista = new ArrayList();
        Criteria criteria = null;
        int id = Integer.parseInt(cod);
        try {
            Session session = HibernateUtil.currentSession();
            criteria = session.createCriteria(Respuesta.class);
            criteria.add(Restrictions.eq("codResp", id));
            lista = criteria.list();
        } catch (HibernateException e) {
            log.error("_____Error al obtener el registro de la clase Respuesta con id: " + cod, e);
            throw new HibernateException(e);
        }
        return lista;
    }

    public boolean guardarRespuestaEnPregunta(Respuesta resp, int codPreg) {
        Transaction tx = null;
        Session session = HibernateUtil.currentSession();
        boolean guardado = false;
        try {
            tx = session.beginTransaction();
            session.clear();
            Pregunta pregunta = (Pregunta) session.get(Pregunta.class, codPreg);
            if (resp.getCodResp() != -1) {
                Respuesta respuesta = (Respuesta) session.get(Respuesta.class, resp.getCodResp());
                respuesta.setPregunta(pregunta);
                respuesta.setRespuestaResp(resp.getRespuestaResp());
                respuesta.setEsCorrecta(resp.isEsCorrecta());
                session.saveOrUpdate(pregunta);
                pregunta.getRespuestas().add(respuesta);
            } else {
                resp.setPregunta(pregunta);
                session.saveOrUpdate(resp);
                pregunta.getRespuestas().add(resp);
            }
            session.saveOrUpdate(pregunta);
            tx.commit();
            guardado = true;
        } catch (Exception e) {
            try {
                tx.rollback();
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            HibernateUtil.closeSession();
        }
        return guardado;
    }

    public List findRespuestasPregunta(Pregunta pregunta) {
        List respuestas = new ArrayList();
        Criteria criteria = null;
        try {
            Session session = HibernateUtil.currentSession();
            criteria = session.createCriteria(Respuesta.class);
            criteria.add(Restrictions.eq("pregunta", pregunta));
            Pregunta preg = (Pregunta) session.get(Pregunta.class, pregunta.getCodPreg());
            respuestas = criteria.list();
        } catch (HibernateException e) {
            log.error("_____Error al obtener las respuestas de la pregunta: " + pregunta.getEnunciadoPreg(), e);
            throw new HibernateException(e);
        }
        return respuestas;
    }

    public List findRespuestasAsignatura(Asignatura asignatura) {
        List respuestas = new ArrayList();
        List totalRespuestas = new ArrayList();
        List preguntas = new ArrayList();
        Criteria criteria = null;
        try {
            Session session = HibernateUtil.currentSession();
            Asignatura asig = (Asignatura) session.get(Asignatura.class, asignatura.getCodAsig());
            criteria = session.createCriteria(Pregunta.class);
            criteria.add(Restrictions.eq("asignatura", asig));
            preguntas = criteria.list();
            for (Iterator iterator = preguntas.iterator(); iterator.hasNext(); ) {
                Pregunta pregunta = (Pregunta) iterator.next();
                criteria = session.createCriteria(Respuesta.class);
                criteria.add(Restrictions.eq("pregunta", pregunta));
                respuestas = criteria.list();
                for (Iterator iterator2 = respuestas.iterator(); iterator2.hasNext(); ) {
                    Respuesta respuesta = (Respuesta) iterator2.next();
                    totalRespuestas.add(respuesta);
                }
            }
        } catch (HibernateException e) {
            log.error("_____Error al obtener las respuestas de la asignatura: " + asignatura.getNombreAsig(), e);
            throw new HibernateException(e);
        }
        return totalRespuestas;
    }

    public List findByParameters(RespuestaForm respuestaForm) {
        List lista = new ArrayList();
        Criteria criteria = null;
        try {
            Session session = HibernateUtil.currentSession();
            criteria = session.createCriteria(Respuesta.class);
            if (respuestaForm.getRespuestaResp() != null && !"".equals(respuestaForm.getRespuestaResp())) {
                criteria.add(Restrictions.like("respuestaResp", "%" + respuestaForm.getRespuestaResp() + "%"));
            }
            if (respuestaForm.isEsCorrecta()) {
                criteria.add(Restrictions.eq("esCorrecta", respuestaForm.isEsCorrecta()));
            } else {
                criteria.add(Restrictions.eq("esCorrecta", respuestaForm.isEsCorrecta()));
            }
            lista = criteria.list();
        } catch (HibernateException e) {
            log.error("_____Error al obtener los registros de la clase Respuesta para los parametros de busquedas.", e);
            throw new HibernateException(e);
        }
        return lista;
    }
}
