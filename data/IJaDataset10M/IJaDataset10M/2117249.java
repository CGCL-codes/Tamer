package com.antares.sirius.service.impl;

import com.antares.commons.service.impl.BaseServiceImpl;
import com.antares.sirius.dao.TipoPresupuestoDAO;
import com.antares.sirius.model.TipoPresupuesto;
import com.antares.sirius.service.ParametroService;
import com.antares.sirius.service.TipoPresupuestoService;

/**
 * Implementacion de la interfaz TipoPresupuestoService.
 *
 * @version 1.0.0 Created 23/11/2010 by Julian Martinez
 * @author <a href:mailto:otakon@gmail.com> Julian Martinez </a>
 *
 */
public class TipoPresupuestoServiceImpl extends BaseServiceImpl<TipoPresupuesto, TipoPresupuestoDAO> implements TipoPresupuestoService {

    private ParametroService parametroService;

    public TipoPresupuesto findTipoPresupuestoActividad() {
        return dao.findById(parametroService.findIdTipoPresupuestoActividad());
    }

    public TipoPresupuesto findTipoPresupuestoProyecto() {
        return dao.findById(parametroService.findIdTipoPresupuestoProyecto());
    }

    public void setParametroService(ParametroService parametroService) {
        this.parametroService = parametroService;
    }
}
