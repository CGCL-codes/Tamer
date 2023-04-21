package br.com.sinapp.controller;

import static br.com.sinapp.validation.CustomMatchers.notEmpty;
import static br.com.sinapp.validation.CustomMatchers.positive;
import static org.hamcrest.Matchers.is;
import java.util.List;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;
import br.com.sinapp.dao.PersistenciaEntidadeAvulsoDao;
import br.com.sinapp.dao.PersistenciaEntidadeDao;
import br.com.sinapp.model.EntidadeAvulso;

@Resource
public class EntidadeAvulsoController {

    private Result result;

    private Validator validator;

    private PersistenciaEntidadeAvulsoDao dao;

    private PersistenciaEntidadeDao daoEntidade;

    public EntidadeAvulsoController(Result result, Validator validator, PersistenciaEntidadeAvulsoDao dao, PersistenciaEntidadeDao daoEntidade) {
        this.validator = validator;
        this.result = result;
        this.daoEntidade = daoEntidade;
        this.dao = dao;
    }

    @Path("boleto/{idEntidade}/avulsos")
    public void listar(Long idEntidade) {
        StringBuffer query = new StringBuffer();
        query.append("FROM EntidadeAvulso E ");
        query.append("     WHERE E.id = " + idEntidade);
        List<EntidadeAvulso> lista = dao.list(query.toString());
        result.use(Results.http()).addHeader("Content-Type", "text/html; charset=ISO-8859-1");
        result.use(Results.json()).from(lista.size(), "avulsos").serialize();
    }

    public EntidadeAvulso associar(EntidadeAvulso entidadeAvulso) {
        result.include("entidades", daoEntidade.list("FROM Entidade E ORDER BY E.nomeFantasia"));
        if (entidadeAvulso == null) return null;
        if (entidadeAvulso.getId() > 0) {
            try {
                EntidadeAvulso entidadeAvulsoBanco = dao.load(entidadeAvulso.getId());
                if (entidadeAvulsoBanco.getId() != null) return entidadeAvulsoBanco;
            } catch (Exception e) {
            }
        }
        return entidadeAvulso;
    }

    public void salvar(final EntidadeAvulso entidadeAvulso) {
        validator.checking(new Validations() {

            {
                that(entidadeAvulso.getId(), is(positive()), "entidadeAvulso.entidade", "selecao_obrigatoria", i18n("entidadeAvulso.entidade"));
                that(entidadeAvulso.getGeracaoDebito(), is(notEmpty()), "entidadeAvulso.geracaoDebito", "obrigatorio", i18n("entidadeAvulso.geracaoDebito"));
                that(entidadeAvulso.getGeracaoCodigo(), is(notEmpty()), "entidadeAvulso.geracaoCodigo", "obrigatorio", i18n("entidadeAvulso.geracaoCodigo"));
                that(entidadeAvulso.getGeracaoHistorico(), is(notEmpty()), "entidadeAvulso.geracaoHistorico", "obrigatorio", i18n("entidadeAvulso.geracaoHistorico"));
                that(entidadeAvulso.getBaixaCredito(), is(notEmpty()), "entidadeAvulso.baixaCredito", "obrigatorio", i18n("entidadeAvulso.baixaCredito"));
                that(entidadeAvulso.getBaixaCodigo(), is(notEmpty()), "entidadeAvulso.baixaCodigo", "obrigatorio", i18n("entidadeAvulso.baixaCodigo"));
                that(entidadeAvulso.getBaixaHistorico(), is(notEmpty()), "entidadeAvulso.baixaHistorico", "obrigatorio", i18n("entidadeAvulso.baixaHistorico"));
            }
        });
        if (validator.hasErrors()) {
            result.include("entidades", daoEntidade.list("FROM Entidade E ORDER BY E.nomeFantasia"));
        }
        validator.onErrorUse(Results.page()).of(EntidadeAvulsoController.class).associar(entidadeAvulso);
        try {
            EntidadeAvulso entidadeAvulsoBanco = dao.load(entidadeAvulso.getId());
            if (entidadeAvulsoBanco.getId() != null) {
                dao.getSession().merge(entidadeAvulso);
            }
        } catch (Exception e) {
            dao.save(entidadeAvulso);
        }
        result.use(Results.logic()).redirectTo(EntidadeAvulsoController.class).associar(null);
    }
}
