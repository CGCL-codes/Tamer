package framework.mentalState;

import java.io.Serializable;

/**
 * Respons�vel por representar as caracter�sticas estruturais e comportamentais
 * de uma condi��o. 
 */
public class Condition implements Serializable {

    /**
     * Tipo da condi��o. 
     */
    private String type = null;

    /**
     * Nome da condi��o.
     */
    private String name = null;

    /**
     * Valor da condi��o.
     */
    private Object value = null;

    /**
     * Construtor da classe respons�vel por atribuir o tipo, nome e o objeto
     * associado � condi��o.
     * @param type
     * Tipo da condi��o.
     * @param name
     * Nome da condi��o.
     * @param value
     * Objeto associado � condi��o.
     */
    public Condition(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    /**
     * Fornece o tipo da condi��o.
     * @return
     * Tipo da condi��o.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Fornece o nome da condi��o.
     * @return
     * Nome da condi��o.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Fornece o objeto associado � condi��o.
     * @return
     * Objeto associado � condi��o.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Atribui um novo tipo � condi��o.
     * @param type
     * Novo tipo da condi��o. 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Atribui um novo nome � condi��o. 
     * @param name
     * Novo nome da condi��o.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Atribui um novo objeto a condi��o.
     * @param value
     * Novo objeto associado � condi��o.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
