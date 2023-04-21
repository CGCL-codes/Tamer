package org.nextframework.authorization;

/**
 * O ModuloAutorizacao representa um tipo de autorizacao no sistema.
 * O ModuloAutorizacao define como � determinada autorizacao 
 * (quais ItemAutorizacao tem). E verifica se o usu�rio pode efetuar determinada 
 * acao no sistema. O Modulo pode utilizar um autorizador para verificar a autorizacao.
 * O Modulo tamb�m cria a Autorizacao de determinado usu�rio baseado em suas permissoes.
 * 
 * O ModuloAutorizacao deve especificar um pequeno contrato contendo as seguintes informa��es:
 * 
 * Autorizacao: classe que representa uma autorizacao daquele modulo
 * Itens Autorizacao: quais items de autorizacao esse modulo possui
 * 
 * @author rogelgarcia
 */
public interface AuthorizationModule {

    /**
     * Cria uma autorizacao baseado nas permissoes. A classe da Autoricao criada � 
     * definida pelo contrato do Modulo
     */
    public Authorization createAuthorization(Permission[] permissoes);

    /**
     * Verifica se o usu�rio � autorizado a efetuar determinada acao baseado nas permissoes que ele tem 
     * para determinado Control. � importante que s� sejam fornecidas permissoes para o Control que o 
     * usu�rio estiver usando.
     */
    public boolean isAuthorized(String acao, Permission[] permissoes);

    /**
     * Verifica se o usu�rio � autorizado a efetuar determinada acao baseado na Autorizacao. 
     * � importante que essa autorizacao seje forjada com permissoes que  
     * sejam para o Control que o usu�rio estiver usando.
     */
    public boolean isAuthorized(String acao, Authorization autorizacao);

    /**
     * Retorna a lista de AuthorizationItem para esse Modulo
     */
    public AuthorizationItem[] getAuthorizationItens();

    /**
     * Retorna o nome do grupot desse authorization module. Authorizations module
     * com o mesmo Group Name ser�o agrupados no mesmo bloco na tela de autorizacao
     * @return
     */
    public String getAuthorizationGroupName();
}
