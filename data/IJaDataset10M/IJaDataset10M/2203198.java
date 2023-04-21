package com.antares.sirius.service.impl;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.Collection;
import com.antares.commons.service.impl.BusinessEntityServiceImpl;
import com.antares.sirius.dao.ProyectoDAO;
import com.antares.sirius.model.EstadoProyecto;
import com.antares.sirius.model.Proyecto;
import com.antares.sirius.service.EstadoProyectoService;
import com.antares.sirius.service.ParametroService;
import com.antares.sirius.service.ProyectoService;

/**
 * Implementacion de la interfaz ProyectoService.
 *
 * @version 1.0.0 Created 23/11/2010 by Julian Martinez
 * @author <a href:mailto:otakon@gmail.com> Julian Martinez </a>
 *
 */
public class ProyectoServiceImpl extends BusinessEntityServiceImpl<Proyecto, ProyectoDAO> implements ProyectoService {

    private ParametroService parametroService;

    private EstadoProyectoService estadoProyectoService;

    public Collection<Proyecto> findAllNoFinalizados() {
        EstadoProyecto estadoFinalizado = estadoProyectoService.findById(parametroService.findIdEstadoProyectoFinalizado());
        return dao.findAllExceptEstados(estadoFinalizado);
    }

    public Collection<Proyecto> findAllNoFinalizadosNiCierre() {
        EstadoProyecto estadoFinalizado = estadoProyectoService.findById(parametroService.findIdEstadoProyectoFinalizado());
        EstadoProyecto estadoCierre = estadoProyectoService.findById(parametroService.findIdEstadoProyectoCierre());
        return dao.findAllExceptEstados(estadoFinalizado, estadoCierre);
    }

    public boolean isFinalizado(Proyecto proyecto) {
        Integer idEstadoFinalizado = parametroService.findIdEstadoProyectoFinalizado();
        return proyecto.getEstadoProyecto().getId().equals(idEstadoFinalizado);
    }

    public boolean isCierre(Proyecto proyecto) {
        Integer idEstadoCierre = parametroService.findIdEstadoProyectoCierre();
        return proyecto.getEstadoProyecto().getId().equals(idEstadoCierre);
    }

    public boolean isNombreRepetido(String nombre, Integer id) {
        boolean isNombreRepetido = false;
        Proyecto entity = dao.findByNombre(nombre);
        if (entity != null) {
            isNombreRepetido = !entity.getId().equals(id);
        }
        return isNombreRepetido;
    }

    public boolean isTransicionValida(Proyecto proyecto, Integer idEstado) {
        boolean isTransicionValida = false;
        for (EstadoProyecto estado : proyecto.getEstadoProyecto().getProximosEstadosPosibles()) {
            if (estado.getId().equals(idEstado)) {
                isTransicionValida = true;
            }
        }
        return isTransicionValida;
    }

    public void saveCambioEstado(Proyecto proyecto, Integer idEstado) {
        EstadoProyecto nuevoEstado = estadoProyectoService.findById(idEstado);
        proyecto.setEstadoProyecto(nuevoEstado);
        dao.save(proyecto);
    }

    public boolean isIndividual(Proyecto proyecto) {
        boolean isIndividual = TRUE;
        isIndividual = proyecto.getTipoAgrupamiento().getId().equals(parametroService.findIdTipoAgrupamientoIndividual());
        return isIndividual;
    }

    public boolean isAgrupado(Proyecto proyecto) {
        boolean isAgrupado = FALSE;
        isAgrupado = proyecto.getTipoAgrupamiento().getId().equals(parametroService.findIdTipoAgrupamientoAgrupado());
        return isAgrupado;
    }

    public void setParametroService(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    public void setEstadoProyectoService(EstadoProyectoService estadoProyectoService) {
        this.estadoProyectoService = estadoProyectoService;
    }
}
