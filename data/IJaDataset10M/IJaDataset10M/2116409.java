package br.com.webcrm.dao;

import java.util.List;
import br.com.webcrm.model.Entidade;

public interface EntidadeDAO extends BaseDAO<Entidade, Integer> {

    public List<Entidade> consultarPorNome(String nomeEntidade, Integer situacao);

    public List<Entidade> consultarPorCpfCnpj(Integer valor, Integer situacao);

    public List<Entidade> consultarPorNomeFantasia(String nomeFantasia, Integer situacao);

    public List<Entidade> consultarPorIdEntidade(Integer idEntidade);

    public List<Entidade> consultarTodos();
}
