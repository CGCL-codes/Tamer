package com.antares.sirius.view.action;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import com.antares.commons.util.Utils;
import com.antares.commons.view.action.BaseAction;
import com.antares.sirius.filter.FinanciadorFilter;
import com.antares.sirius.model.EstadoFinanciador;
import com.antares.sirius.model.Financiador;
import com.antares.sirius.model.TipoFinanciador;
import com.antares.sirius.service.EstadoFinanciadorService;
import com.antares.sirius.service.FinanciadorService;
import com.antares.sirius.service.TipoFinanciadorService;
import com.antares.sirius.view.form.FinanciadorForm;

public class FinanciadorAction extends BaseAction<Financiador, FinanciadorForm, FinanciadorService> {

    private EstadoFinanciadorService estadoFinanciadorService;

    private TipoFinanciadorService tipoFinanciadorService;

    @Override
    public FinanciadorFilter createFilter(FinanciadorForm form) {
        FinanciadorFilter filter = new FinanciadorFilter();
        filter.setNombre(form.getFiltroNombre());
        if (Utils.isNotNullNorEmpty(form.getFiltroIdEstadoFinanciador())) {
            filter.setEstadoFinanciador(estadoFinanciadorService.findById(Utils.parseInteger(form.getFiltroIdEstadoFinanciador())));
        }
        if (Utils.isNotNullNorEmpty(form.getFiltroIdTipoFinanciador())) {
            filter.setTipoFinanciador(tipoFinanciadorService.findById(Utils.parseInteger(form.getFiltroIdTipoFinanciador())));
        }
        return filter;
    }

    @Override
    public void updateEntity(Financiador entity, FinanciadorForm form) {
        entity.setNombre(form.getNombre());
        entity.setCuit(form.getCuit());
        entity.setCbu(form.getCbu());
        entity.setDireccion(form.getDireccion());
        entity.setTelefono(form.getTelefono());
        entity.setContacto(form.getContacto());
        entity.setCelular(form.getCelular());
        entity.setEmail(form.getEmail());
        entity.setObservaciones(form.getObservaciones());
        EstadoFinanciador estadoFinanciador = estadoFinanciadorService.findById(Utils.parseInteger(form.getIdEstadoFinanciador()));
        entity.setEstadoFinanciador(estadoFinanciador);
        TipoFinanciador tipoFinanciador = tipoFinanciadorService.findById(Utils.parseInteger(form.getIdTipoFinanciador()));
        entity.setTipoFinanciador(tipoFinanciador);
    }

    @Override
    protected void loadCollections(FinanciadorForm form) {
        form.setEstadosFinanciador(estadoFinanciadorService.findAll());
        form.setTiposFinanciador(tipoFinanciadorService.findAll());
    }

    @Override
    protected ActionErrors validate(FinanciadorForm form) {
        ActionErrors errors = new ActionErrors();
        if (service.isNombreRepetido(form.getNombre(), form.getId())) {
            errors.add("error", new ActionMessage("errors.unique", Utils.getMessage("sirius.financiador.nombre.label")));
        }
        return errors;
    }

    public EstadoFinanciadorService getEstadoFinanciadorService() {
        return estadoFinanciadorService;
    }

    public void setEstadoFinanciadorService(EstadoFinanciadorService estadoFinanciadorService) {
        this.estadoFinanciadorService = estadoFinanciadorService;
    }

    public TipoFinanciadorService getTipoFinanciadorService() {
        return tipoFinanciadorService;
    }

    public void setTipoFinanciadorService(TipoFinanciadorService tipoFinanciadorService) {
        this.tipoFinanciadorService = tipoFinanciadorService;
    }
}
