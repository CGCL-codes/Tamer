package com.antares.sirius.service.impl;

import com.antares.commons.service.impl.BusinessEntityServiceImpl;
import com.antares.sirius.dao.ProveedorDAO;
import com.antares.sirius.model.Proveedor;
import com.antares.sirius.service.ProveedorService;

/**
 * Implementacion de la interfaz ProveedorService.
 *
 * @version 1.0.0 Created 23/11/2010 by Julian Martinez
 * @author <a href:mailto:otakon@gmail.com> Julian Martinez </a>
 *
 */
public class ProveedorServiceImpl extends BusinessEntityServiceImpl<Proveedor, ProveedorDAO> implements ProveedorService {

    public boolean isNombreRepetido(String nombre, Integer id) {
        boolean isNombreRepetido = false;
        Proveedor entity = dao.findByNombre(nombre);
        if (entity != null) {
            isNombreRepetido = !entity.getId().equals(id);
        }
        return isNombreRepetido;
    }
}
