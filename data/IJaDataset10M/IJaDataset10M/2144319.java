package br.com.sinapp.controller;

import java.util.List;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.sinapp.dao.PersistenciaCidadeDao;
import br.com.sinapp.model.Cidade;

@Resource
public class CidadeController {

    private Result result;

    private PersistenciaCidadeDao dao;

    public CidadeController(PersistenciaCidadeDao dao, Result result) {
        this.dao = dao;
        this.result = result;
    }

    @Get
    @Path("estado/{idEstado}/cidades")
    public void listar(Long idEstado) {
        List<Cidade> cidades = dao.list("FROM Cidade C WHERE C.idEstado = " + idEstado);
        result.use(Results.http()).addHeader("Content-Type", "text/html; charset=ISO-8859-1");
        result.use(Results.json()).from(cidades, "cidades").serialize();
    }
}
