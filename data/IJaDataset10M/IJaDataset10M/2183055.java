package br.ufg.integrate.wrapper.query;

/**
 * @author Rogerio
 * @version 0.1
 *
 * Encapsula uma refer�ncia entre entidades em uma consulta feita em
 * arquivo XML.
 */
public class QueryRef {

    /**
	 * Entidade referenciada.
	 */
    private String entity;

    /**
	 * Atributo referenciado
	 */
    private String attributeRef;

    /**
	 * Retorna o atributo referenciado.
	 */
    public String getAttributeRef() {
        return this.attributeRef;
    }

    /**
	 * Retorna a entidade referenciada.
	 * @return Entidade referenciada.
	 */
    public String getEntity() {
        return entity;
    }

    /**
	 * Define o atributo referenciado.
	 */
    public void setAttributeRef(String att) {
        this.attributeRef = att;
    }

    /**
	 * Define a entidade referenciada.
	 * @param entity
	 */
    public void setEntity(String entity) {
        this.entity = entity;
    }
}
