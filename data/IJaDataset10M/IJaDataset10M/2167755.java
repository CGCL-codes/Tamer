package br.uff.javaavancado.service;

import br.uff.javaavancado.controletransacao.Transacional;
import br.uff.javaavancado.dao.CategoriaDAO;
import br.uff.javaavancado.dao.controle.FabricaDeDao;
import br.uff.javaavancado.dao.impl.CategoriaDAOImpl;
import br.uff.javaavancado.exception.AplicacaoException;
import br.uff.javaavancado.exception.ObjetoNaoEncontradoException;
import br.uff.javaavancado.modelos.Categoria;
import java.util.List;

public class CategoriaService extends BaseService implements Service, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private CategoriaDAO categoriaDAO;

    public CategoriaService() {
        super(CategoriaService.class);
        try {
            categoriaDAO = FabricaDeDao.getDao(CategoriaDAOImpl.class, Categoria.class);
        } catch (Exception e) {
        }
    }

    @Transacional
    public Categoria salvar(Categoria categoria) {
        return categoriaDAO.inclui(categoria);
    }

    @Transacional
    public void atualizar(Categoria categoria) {
        categoriaDAO.altera(categoria);
    }

    @Transacional
    public void excluir(Categoria categoria) throws AplicacaoException {
        try {
            categoria = categoriaDAO.getPorIdComLock(categoria.getId());
            categoriaDAO.exclui(categoria);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new AplicacaoException("Categoria nao encontrada");
        }
    }

    public List<Categoria> getListaCompleta() {
        return categoriaDAO.getListaCompleta();
    }
}
