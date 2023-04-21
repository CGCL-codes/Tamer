package br.com.guaraba.wally.core.dominio.servicos;

import java.util.List;
import br.com.guaraba.commons.service.impl.ServiceException;
import br.com.guaraba.wally.core.dominio.entidades.Fornecedor;

public interface IFornecedorService {

    Fornecedor carregarPorLoginSenha(String login, String senha) throws ServiceException;

    Fornecedor salvar(Fornecedor fornecedor) throws ServiceException;

    void excluir(Fornecedor fornecedor) throws ServiceException;

    List<Fornecedor> listarTodos() throws ServiceException;

    Fornecedor cadastrar(Fornecedor fornecedor) throws ServiceException;
}
