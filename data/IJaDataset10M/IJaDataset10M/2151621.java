package org.adempierelbr.sped.efd.piscofins.beans;

import org.adempierelbr.sped.RegSped;

/**
 * REGISTRO 9001: ABERTURA DO BLOCO 9
 * @author Mario Grigioni, mgrigioni
 * @version $Id: R9001.java, 07/02/2011, 14:42:00, mgrigioni
 */
public class R9001 extends RegSped {

    private String IND_MOV;

    /**
	 * Constructor
	 * 
	 * @param IND_MOV
	 */
    public R9001(Boolean hasTransaction) {
        super();
        this.IND_MOV = hasTransaction ? "0" : "1";
    }

    /**
	 * Formata o Bloco 9 Registro 001
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(IND_MOV).append(PIPE).append(EOL);
        return format.toString();
    }
}
