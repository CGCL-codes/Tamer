package org.jnf.persistence;

import java.io.Serializable;

/**
 * Esta clase sirve como base para cualquier petici�n de datos que devuelve, potencialmente, m�s de
 * una p�gina de resultados como por ejemplo un Grid de datos.
 * 
 * @author Pablo Krause powered by GUCOBA Systems S.C.
 * 
 */
public class PagedRequest implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5641921812247056580L;

    public static final String ORDER_ASC = "ASC";

    public static final String ORDER_DESC = "DESC";

    /**
	 * Indice del primer elemento de la lista total de resultados a partir del cual se quiere la
	 * p�gina
	 */
    private int start;

    /** Numero de elementos por pagina */
    private int limit;

    /** El nombre del campo sobre el cual filtrar */
    private String sort;

    /** El tipo de ordenamiento: ascendente o descendente */
    private String dir = ORDER_ASC;

    /** @see #start */
    public int getStart() {
        return start;
    }

    /**
	 * M&eacute;todo depreciado, utilizar el que recibe int.
	 * 
	 * @param firstRecord
	 */
    @Deprecated
    public void setStart(long firstRecord) {
        this.start = (int) firstRecord;
    }

    /** @see #start */
    public void setStart(int firstRecord) {
        this.start = firstRecord;
    }

    /** @see #limit */
    public int getLimit() {
        return limit;
    }

    /** @see #limit */
    public void setLimit(int pageSize) {
        this.limit = pageSize;
    }

    /** @see #sort */
    public String getSort() {
        return sort;
    }

    /** @see #sort */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /** @see #dir */
    public String getDir() {
        return dir;
    }

    /** @see #dir */
    public void setDir(String order) {
        this.dir = order;
    }

    /**
	 * M�todo utilitario que devuelve verdadero si el ordenamiento es acendente. �til en la capa de
	 * persistencia para armar criterios de Hibernate de tipo Order.
	 * Si la propiedad que define el orden es nula o incorrecta, �ste m�todo devuelve verdadero.
	 * 
	 * @return verdadero si la propiedad dir  es 'ASC', nula, o desconocida
	 */
    public boolean isAscendingOrder() {
        return !ORDER_DESC.equals(dir);
    }
}
