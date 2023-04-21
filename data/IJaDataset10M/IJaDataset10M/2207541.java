package br.edu.uncisal.farmacia.controller;

import static br.com.caelum.vraptor.view.Results.page;
import java.util.ArrayList;
import java.util.List;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.edu.uncisal.farmacia.dao.FornecedorDao;
import br.edu.uncisal.farmacia.dao.GrupoDao;
import br.edu.uncisal.farmacia.dao.NotaEntradaDao;
import br.edu.uncisal.farmacia.dao.UfDao;
import br.edu.uncisal.farmacia.model.Endereco;
import br.edu.uncisal.farmacia.model.Fornecedor;
import br.edu.uncisal.farmacia.model.Grupo;
import br.edu.uncisal.farmacia.model.NotaEntrada;
import br.edu.uncisal.farmacia.model.Uf;

@Resource
public class FornecedorController {

    private FornecedorDao dao;

    private NotaEntradaDao notaEntradaDao;

    private GrupoDao grupoDao;

    private UfDao ufDao;

    private Result result;

    private Validator validator;

    public FornecedorController(FornecedorDao dao, GrupoDao grupoDao, UfDao ufDao, NotaEntradaDao notaEntraDao, Result result, Validator validator) {
        super();
        this.dao = dao;
        this.grupoDao = grupoDao;
        this.notaEntradaDao = notaEntraDao;
        this.ufDao = ufDao;
        this.result = result;
        this.validator = validator;
    }

    public void form() {
        result.include("grupos", grupoDao.listAll());
        result.include("ufs", ufDao.listAll());
        Uf uf = ufDao.getById(27L);
        result.include("municipios", uf.getMunicipios());
    }

    @Post
    @Path("/fornecedor")
    public void save(Fornecedor fornecedor, Endereco endereco) {
        fornecedor.setEndereco(endereco);
        if (fornecedor.getNome() == null || fornecedor.getNome().equals("")) {
            validator.add(new ValidationMessage("Um nome para o o fornecedor não foi definido.", "error"));
        }
        if (fornecedor.getCpfCnpj() == null || fornecedor.getCpfCnpj().equals("")) {
            validator.add(new ValidationMessage("Um cpf/cnpj precisa ser definido.", "error"));
        }
        if (fornecedor.getRazaoSocial() == null || fornecedor.getRazaoSocial().equals("")) {
            validator.add(new ValidationMessage("Um razão social não foi definida.", "error"));
        }
        if (fornecedor.getGrupos() == null || fornecedor.getGrupos().size() == 0) {
            validator.add(new ValidationMessage("Pelo menos um grupo deve ser escolhido.", "error"));
        }
        if (validator.hasErrors()) {
            result.include("grupos", grupoDao.listAll());
            result.include("ufs", ufDao.listAll());
            result.include("municipios", ufDao.getById(27L).getMunicipios());
            result.include("fornecedor", fornecedor);
        }
        validator.onErrorUse(page()).of(FornecedorController.class).form();
        if (fornecedor.getId() == null) dao.save(fornecedor); else dao.update(fornecedor);
        result.include("message", "Fornecedor cadastrado com sucesso!");
        result.redirectTo(FornecedorController.class).list();
    }

    public List<Fornecedor> list() {
        return dao.listAll();
    }

    public void buscar(String criteria) {
        result.include("criteria", criteria);
        result.include("fornecedorList", dao.buscar(criteria));
        result.forwardTo("/WEB-INF/jsp/fornecedor/list.jsp");
    }

    @Get
    @Path("/fornecedor/{fornecedor.id}")
    public void get(Fornecedor fornecedor) {
        fornecedor = dao.getById(fornecedor.getId());
        result.include("grupos", grupoDao.listAll());
        result.include("ufs", ufDao.listAll());
        result.include("municipios", fornecedor.getEndereco().getMunicipio().getUf().getMunicipios());
        result.include("fornecedor", fornecedor);
        result.forwardTo("/WEB-INF/jsp/fornecedor/form.jsp");
    }

    @Path("/fornecedor/remove/{fornecedor.id}")
    public void remove(Fornecedor fornecedor) {
        NotaEntrada notaEntrada = new NotaEntrada();
        notaEntrada.setFornecedor(fornecedor);
        if (notaEntradaDao.listByFornecedor(fornecedor).size() > 0) validator.add(new ValidationMessage("O fornecedor não pode ser removido pois existem Notas de Entrada que fazem referência a este.", "error"));
        if (validator.hasErrors()) result.include("fornecedorList", dao.listAll());
        validator.onErrorUse(page()).of(FornecedorController.class).list();
        dao.remove(fornecedor);
        result.include("message", "Fornecedor removido com sucesso");
        result.redirectTo(FornecedorController.class).list();
    }
}
