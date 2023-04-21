package com.antares.sirius.view.action;

import static com.antares.commons.enums.ActionEnum.CREATE;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import com.antares.commons.util.Utils;
import com.antares.commons.validation.CustomValidationRoutines;
import com.antares.commons.view.action.BaseAction;
import com.antares.sirius.filter.MetaFilter;
import com.antares.sirius.model.Meta;
import com.antares.sirius.model.ObjetivoEspecifico;
import com.antares.sirius.service.GastoService;
import com.antares.sirius.service.MetaService;
import com.antares.sirius.service.ObjetivoEspecificoService;
import com.antares.sirius.service.ProyectoService;
import com.antares.sirius.view.form.MetaForm;

public class MetaAction extends BaseAction<Meta, MetaForm, MetaService> {

    private ProyectoService proyectoService;

    private ObjetivoEspecificoService objetivoEspecificoService;

    private GastoService gastoService;

    @Override
    public MetaFilter createFilter(MetaForm form) {
        MetaFilter filter = new MetaFilter();
        filter.setNombre(form.getFiltroNombre());
        if (Utils.isNotNullNorEmpty(form.getFiltroIdObjetivoEspecifico())) {
            filter.setObjetivoEspecifico(objetivoEspecificoService.findById(Utils.parseInteger(form.getFiltroIdObjetivoEspecifico())));
        }
        return filter;
    }

    @Override
    public void updateEntity(Meta entity, MetaForm form) {
        entity.setNombre(form.getNombre());
        entity.setDescripcion(form.getDescripcion());
        entity.setPonderacion(Utils.parseInteger(form.getPonderacion()));
        if (Utils.isNotNullNorEmpty(form.getIdObjetivoEspecifico())) {
            entity.setObjetivoEspecifico(objetivoEspecificoService.findById(Utils.parseInteger(form.getIdObjetivoEspecifico())));
        }
    }

    @Override
    public ActionForward initUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward;
        Integer id = new Integer(request.getParameter("id"));
        Meta entity = service.findById(id);
        if (entity != null && entity.isActivo() && proyectoService.isFinalizado(entity.getProyecto())) {
            forward = sendMessage(request, mapping, "errors.proyectoFinalizado", "/meta/meta-query.do?method=lastQuery");
        } else {
            forward = super.initUpdate(mapping, form, request, response);
        }
        return forward;
    }

    @Override
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward;
        Integer id = new Integer(request.getParameter("id"));
        Meta entity = service.findById(id);
        if (entity != null) {
            if (proyectoService.isFinalizado(entity.getProyecto())) {
                forward = sendMessage(request, mapping, "errors.proyectoFinalizado", "/meta/meta-query.do?method=lastQuery");
            } else if (gastoService.existenGastosMeta(entity)) {
                forward = sendMessage(request, mapping, "errors.existenGastos", "/meta/meta-query.do?method=lastQuery");
            } else {
                service.delete(entity);
                forward = query(mapping, form, request, response);
            }
        } else {
            forward = mapping.findForward("restrictedAccess");
        }
        return forward;
    }

    @Override
    protected void loadCollections(MetaForm form) {
        if (CREATE.equals(form.getAction())) {
            form.setObjetivosEspecificos(objetivoEspecificoService.findAllNoFinalizadosNiCierre());
        } else {
            form.setObjetivosEspecificos(objetivoEspecificoService.findAll());
        }
    }

    @Override
    protected ActionErrors validate(MetaForm form) {
        ActionErrors errors = new ActionErrors();
        ObjetivoEspecifico objetivoEspecifico = objetivoEspecificoService.findById(Utils.parseInteger(form.getIdObjetivoEspecifico()));
        Integer ponderacionTotal = objetivoEspecifico.ponderacionTotal(form.getId());
        Integer nuevaPonderacion = Utils.parseInteger(form.getPonderacion());
        CustomValidationRoutines.validatePonderacion(ponderacionTotal, nuevaPonderacion, errors, "sirius.meta.objetivoEspecifico.label");
        if (service.isNombreRepetido(form.getNombre(), form.getId())) {
            errors.add("error", new ActionMessage("errors.unique", Utils.getMessage("sirius.meta.nombre.label")));
        }
        if (proyectoService.isFinalizado(objetivoEspecifico.getProyecto())) {
            errors.add("error", new ActionMessage("errors.proyectoFinalizado"));
        }
        if (CREATE.equals(form.getAction())) {
            if (proyectoService.isCierre(objetivoEspecifico.getProyecto())) {
                errors.add("error", new ActionMessage("errors.proyectoCierre"));
            }
        }
        return errors;
    }

    public void setObjetivoEspecificoService(ObjetivoEspecificoService objetivoEspecificoService) {
        this.objetivoEspecificoService = objetivoEspecificoService;
    }

    public void setGastoService(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    public void setProyectoService(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }
}
