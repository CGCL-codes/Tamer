package ar.edu.unlp.info.diseyappweb.data.dao;

import java.util.List;
import ar.edu.unlp.info.diseyappweb.data.GenericHibernateDAO;
import ar.edu.unlp.info.diseyappweb.model.LlamadoLicitacion;

/**
 * La clase LlamadoLicitacionDAO representa el DAO para los llamados a licitación.
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public class LlamadoLicitacionDAO extends GenericHibernateDAO<LlamadoLicitacion, Integer> {

    /**
	 * Busca todas los llamados a licitación.
	 * 
	 * @return colección de llamados a licitación.
	 */
    public List<LlamadoLicitacion> traerLlamadosLicitacion() {
        return this.findAll();
    }

    /**
	 * Busca la materia prima correspondiente al identificador.
	 * 
	 * @param id identificador de la materia prima.
	 * @return materia prima encontrada o <code>null</code>.
	 */
    public LlamadoLicitacion traerLlamadoLicitacionPorId(Integer id) {
        return this.findById(id, false);
    }
}
