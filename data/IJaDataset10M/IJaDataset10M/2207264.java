package br.com.sigep.dao;

import java.util.List;
import br.com.sigep.bean.Ocorrencia;
import br.com.sigep.bean.OutrosVinculos;

public interface OcorrenciaDaoInterface {

    public void atualizar(Ocorrencia transientObject) throws DaoException;

    public Ocorrencia carregar(long id) throws DaoException;

    public List<Ocorrencia> listar() throws DaoException;

    public List<Ocorrencia> listarOcorrenciaFiltro(String descricao) throws DaoException;

    public void remover(Ocorrencia persistentObject) throws DaoException;

    public void remover(long id) throws DaoException;

    public void salvar(Ocorrencia newInstance) throws DaoException;
}
