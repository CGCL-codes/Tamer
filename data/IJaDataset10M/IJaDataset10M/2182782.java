package org.cis.jproyinv.comun.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.cis.jproyinv.comun.modelo.Docente;
import org.cis.jproyinv.dao.ServicioDao;

public class DocenteDao extends ServicioDao<Docente, Long> {

    @Override
    public Docente buscar(Long id) {
        return getEm().find(Docente.class, id);
    }

    @Override
    public List<Docente> buscarTodos() {
        return getEm().createQuery("select doc from Docente doc").getResultList();
    }

    public List<Docente> buscarCriterio(String criterio) {
        List<Docente> resultadoBusqueda = new ArrayList();
        try {
            resultadoBusqueda.add((Docente) getEm().createQuery("select doc from Docente doc where doc.numeroDocumentoIdentificacion ='" + criterio + "'").getSingleResult());
        } catch (NoResultException e) {
        }
        resultadoBusqueda.addAll(getEm().createQuery("select doc from Docente doc where doc.nombres like '%" + criterio + "%' or doc.apellidos like '%" + criterio + "%'").getResultList());
        return resultadoBusqueda;
    }
}
