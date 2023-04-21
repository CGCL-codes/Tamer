package br.ufg.integrate.wrapper.query;

/**
 * @author Rogerio
 * @version 0.1
 *
 * Encapsula uma condi��o de consulta feita por arquivo XML. 
 */
public class QueryCondition {

    /**
	 * Atributo da condi��o corrente.
	 */
    private String attribute;

    /**
	 * Entidade da condi��o corrente.
	 */
    private String entity;

    /**
	 * Operador a ser usado na condi��o entre dois atributos.
	 * Os valores permitidos s�o "equals", "greater" e "smaller".
	 */
    private QueryOperator operator;

    /**
	 * Entidade/atributo referenciados pelo atributo corrente.
	 */
    private QueryRef reference;

    /**
	 * Retorna o atributo da condi��o corrente.
	 * @return Atributo da condi��o
	 */
    public String getAttribute() {
        return attribute;
    }

    /**
	 * Retorna a entidade da condi��o corrente.
	 * @return Entidade da condi��o
	 */
    public String getEntity() {
        return entity;
    }

    /**
	 * Retorna o operador a ser aplicado ao atributo da
	 * condi��o corrente.
	 * @return Operador da condi��o
	 */
    public QueryOperator getOperator() {
        return this.operator;
    }

    /**
	 * Retorna a entidade/atributos referenciados pelo
	 * atributo da condi��o corrente.
	 * @return entidade/atributo referenciado.
	 */
    public QueryRef getReference() {
        return this.reference;
    }

    /**
	 * Define o atributo da condi��o corrente.
	 */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
	 * Define a entidade da condi��o corrente.
	 * @param entity
	 */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
	 * Define o operador a ser aplicado ao atributo
	 * da condi��o corrente.
	 * @param op
	 */
    public void setOperator(QueryOperator op) {
        this.operator = op;
    }

    /**
	 * Define a entidade/atributo referenciada pela condi��o corrente.
	 * @param ref
	 */
    public void setReference(QueryRef ref) {
        this.reference = ref;
    }
}
