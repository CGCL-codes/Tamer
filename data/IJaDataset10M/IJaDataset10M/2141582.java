package br.com.dotec.controller;

import java.util.List;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.dotec.infra.interceptor.Restrito;
import br.com.dotec.model.Entrega;
import br.com.dotec.model.TipoDeUsuario;
import br.com.dotec.persistence.dao.EntregaCepDAO;

@Resource
public class EntregaController {

    private final EntregaCepDAO entregaCepDAO;

    private final Result result;

    private final Validator validator;

    public EntregaController(EntregaCepDAO entregaCepDAO, Result result, Validator validator) {
        super();
        this.entregaCepDAO = entregaCepDAO;
        this.result = result;
        this.validator = validator;
    }

    @Restrito({ TipoDeUsuario.ADMINISTRADOR })
    @Path(value = { "/admin/entrega/lista" }, priority = 1)
    public List<Entrega> lista(Entrega entrega) {
        List<Entrega> entregas = entregaCepDAO.lista(entrega);
        return entregas;
    }

    @Restrito({ TipoDeUsuario.ADMINISTRADOR })
    @Path(value = { "/admin/entrega/formulario" }, priority = 1)
    public Entrega formulario(Entrega entrega) {
        return entrega;
    }

    @Restrito({ TipoDeUsuario.ADMINISTRADOR })
    @Path(value = { "/admin/entrega/edita" }, priority = 1)
    public void edita(Long id) {
        Entrega entrega = entregaCepDAO.get(id);
        result.forwardTo(this).formulario(entrega);
    }

    @Restrito({ TipoDeUsuario.ADMINISTRADOR })
    @Path(value = { "/admin/entrega/altera" }, priority = 1)
    public void altera(Entrega entrega) {
        validator.validate(entrega);
        validator.onErrorUsePageOf(this).formulario(entrega);
        entregaCepDAO.atualiza(entrega);
        result.redirectTo(this).lista(null);
    }

    @Restrito({ TipoDeUsuario.ADMINISTRADOR })
    @Path(value = { "/admin/entrega/adiciona" }, priority = 1)
    public void adiciona(Entrega entrega) {
        validator.validate(entrega);
        validator.onErrorUsePageOf(this).formulario(entrega);
        entregaCepDAO.salva(entrega);
        result.redirectTo(EntregaController.class).lista(null);
    }

    @Restrito({ TipoDeUsuario.ADMINISTRADOR })
    @Path(value = { "/admin/entrega/remove" }, priority = 1)
    public void remove(Long id) {
        Entrega entrega = entregaCepDAO.get(id);
        entregaCepDAO.remove(entrega);
        result.redirectTo(this).lista(null);
    }
}
