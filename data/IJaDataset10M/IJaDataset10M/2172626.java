package com.proyecto.tropero.core.bd.DAO.model.Interface;

import com.proyecto.tropero.core.bd.DAO.Arquitectura.Interface.IGenericDAO;
import com.proyecto.tropero.core.domain.Raza;

public interface IRazaDAO extends IGenericDAO {

    /**
	 * Busca la raza con la descripcion pasada por parametro
	 * @param descripcion
	 * @return raza
	 */
    public Raza getRazaByDescripcion(String descripcion);
}
