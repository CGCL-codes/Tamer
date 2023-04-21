package framework.agentRole;

import java.io.Serializable;
import java.util.*;

/**
 * Respons�vel por representar as caracter�sticas estruturais e comportamentais
 * de um dever. 
 */
public class Duty implements Serializable {

    /**
     * Conjunto de a��es que s�o associadas ao dever. Estas a��es s�o realizadas para 
     * concretizar planos, e assim, tentar alcan�ar objetivos.
     */
    private Collection actions = new Vector();

    /**
     * Construtor da classe que recebe uma a��o que � associada a um dever. 
     * @param newAction
     * Nova a��o associada ao dever.
     */
    public Duty(String newAction) {
        this.actions.add(newAction);
    }

    /**
	* Fornece o conjunto de a��es associadas ao dever.
	* @return
	* Todas as a��es associadas ao dever.
	*/
    public Collection getActions() {
        return this.actions;
    }

    /**
	* Atribui uma nova a��o ao conjunto de a��es associado ao dever. 
	* @param newAction
	* Nova a��o associada ao dever.
	*/
    public void setAction(String newAction) {
        this.actions.add(newAction);
    }
}
