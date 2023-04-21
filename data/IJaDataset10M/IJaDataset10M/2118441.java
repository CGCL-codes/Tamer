package org.nextframework.authorization;

import java.util.Map;

/**
 * Interface que representa o acesso a dados de autorizacao do framework. 
 * Todos os dados persistentes que o framework necessitar s�o 
 * buscados atrav�s de uma classe que implementa essa interface 
 * @author rogelgarcia
 * @since 22/01/2006
 * @version 1.1
 */
public interface AuthorizationDAO {

    /**
	 * Retorna um usu�rio com determinado login
	 * @param login
	 * @return
	 */
    public User findUserByLogin(String login);

    /**
     * Procura todos os papeis que determinado usu�rio faz parte
     * @param user
     * @return
     */
    public Role[] findUserRoles(User user);

    /**
     * Acha uma determinada permissao de determinado papel em determinado controle
     * @param role
     * @param controlName
     * @return
     */
    public Permission findPermission(Role role, String controlName);

    /**
     * Salva ou atualiza uma permissao no banco para determinado controle, papel e permissoes
     * Retorna a permissao que foi salva no banco de dados
     * @param controlName
     * @param role
     * @param permissionMap
     * @return
     */
    public Permission savePermission(String controlName, Role role, Map<String, String> permissionMap);

    /**
     * Retorna todos os pap�is que existem no sistema
     * @return
     */
    public Role[] findAllRoles();
}
