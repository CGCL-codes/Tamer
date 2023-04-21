package br.com.linkcom.neo.authorization;

/**
 * Representa a autorizacao de um usuario.
 * A classe que implementar essa interface deve 
 * ter m�todos do tipo canRead canWrite que retorna 
 * algum valor do tipo boolean indicando se o usu�rio 
 * pode ou n�o fazer algo no sistema para determinado 
 * control
 * @author rogelgarcia
 */
public interface Authorization {

    /**
     * Constante que define o nome do atributo da autorizacao
     * Sempre que o usu�rio pedir algum recurso � salvo, por um filtro, um objeto Autorizacao no request
     */
    public static final String AUTHORIZATION_ATTRIBUTE = "authorization";
}
