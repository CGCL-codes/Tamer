package br.com.linkcom.neo4.util;

import java.io.Serializable;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;

/**
 * Classe utilit�ria para trabalhar com entidades persistentes.
 * 
 * @author giovanejfreitas
 * 
 */
public class EntityUtil {

    /**
	 * Obt�m a inst�ncia de uma determinada entidade que j� foi persistida, ou
	 * <code>null</code> caso o ID n�o perten�a a uma entidade persistida.
	 * 
	 * @param <T> Tipo da classe
	 * @param clazz A classe da entidade
	 * @param id O identificador da inst�ncia (objeto usado como chave prim�ria)
	 * @return Uma inst�ncia de <code>T</code> ou <code>null</code>;
	 */
    public static <T> T getEntity(Class<T> clazz, Serializable id) {
        if (isEntityClass(clazz)) {
            GenericService<T> genericService = Neo.getService(clazz);
            return genericService.loadForEntrada(id);
        } else return null;
    }

    /**
	 * Verifica se a classe representa uma entidade persistida em banco de dados.
	 * <br/>Verifica atrav�s das annotations <code>@Entity</code>
	 * @param clazz
	 * @return
	 */
    public static boolean isEntityClass(Class<?> clazz) {
        return clazz.getAnnotation(javax.persistence.Entity.class) != null || clazz.getAnnotation(org.hibernate.annotations.Entity.class) != null;
    }
}
