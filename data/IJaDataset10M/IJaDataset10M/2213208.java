package org.griffante.glue.model;

/**
 * <code>DAOFactory</code> e uma interface que permite
 * configurar a maneira que melhor lhe convem para instanciar
 * objetos do tipo <code>DAO</code> utilizados em sua 
 * aplicacao. 
 * <p>
 * Esse pacote ja inclui uma implementacao dessa interface,
 * seu nome e <code>XmlDAOFactory</code>. E uma classe 
 * <i>Singleton</i> e pode ser usada de dentro de suas classes
 * <b>DAOs</b> para carregar objetos do tipo <code>DAO</code>
 * a partir do XML de configuracao.
 * <p>
 * Verifique com atencao a documentacao da classe <code>AbstractDAO</code>,
 * pois ela possui um metodo que ja carrega o <code>DAO</code> do
 * <i>config.xml</i> sem mesmo o desenvolvedor saber, apenas requerindo
 * que o programados registra a qual <code>Service</code> esse <code>DAO</code>
 * e associado no proprio arquivo de configuracao.
 * <p>
 * Se voce nao deseja criar um arquivo XML que contenha a configuracao
 * a ser usada durante a execucao da sua aplicacao, voce pode criar uma 
 * classe que tambem implemente essa iterface e desenvolver a maneira
 * que os objetos <code>DAO</code> serao carregados. Nao ha restricao
 * a maneira que as informacoes serao guardadas permitindo assim carrega-los
 * da memoria, arquivos .txt, JDBC, entre outros.
 * 
 * @author Giuliano Bernardes Griffante
 * @version 1.0
 * @since 0.1.8 - 04/01/2006
 * @see org.griffante.glue.model.XmlDAOFactory
 */
public interface DAOFactory {

    /**
	 * Retorna uma instancia de DAO, que e carregada a partir do
	 * arquivo de configuracao.
	 * @param key Service q qual esse DAO pertence. 
	 * @return Instancia de DAO.
	 * @throws DAOException
	 */
    public DAO getDAO(String key) throws DAOException;
}
