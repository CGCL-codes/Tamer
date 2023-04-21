package com.antares.sirius.dao;

import java.util.Collection;
import com.antares.commons.dao.BusinessEntityDAO;
import com.antares.sirius.model.Asignacion;
import com.antares.sirius.model.Persona;
import com.antares.sirius.model.Proyecto;

public interface AsignacionDAO extends BusinessEntityDAO<Asignacion> {

    /**
	 * Devuelve todas las asignaciones de una persona
	 * 
	 * @param persona persona
	 * @return
	 */
    Collection<Asignacion> findAllByPersona(Persona persona);

    /**
	 * Devuelve todas las asignaciones de una persona para un proyecto puntual
	 * 
	 * @param persona persona
	 * @param proyecto proyecto
	 * @return
	 */
    Collection<Asignacion> findAllByPersonaProyecto(Persona persona, Proyecto proyecto);
}
