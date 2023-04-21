package br.com.prossys.dao;

/**
 * Interface para a classe de persistência de dados relacionados à pessoa.
 * @author Victor Gutmann
 * @version 1.0
 */
public interface InterfacePessoaDAO {

    /**
     * Verifica se há um username igual já cadastrado.
     * @param username Username a ser verificado
     * @return Booleano dizendo se há ou não um username igual ao passado pelo argumento
     * @throws br.com.prossys.dao.PersistenciaException
     */
    public boolean usernameEmUso(String username) throws PersistenciaException;
}
