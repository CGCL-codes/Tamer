package org.adempierelbr.sped.efd.piscofins.beans;

import org.adempierelbr.sped.RegSped;

/**
 * REGISTRO A001: ABERTURA DO BLOCO A
 * @author Mario Grigioni, mgrigioni
 * @version $Id: RA001.java, 19/01/2011, 11:49:00, mgrigioni
 */
public class RA001 extends RegSped {

    private String IND_MOV;

    /**
	 * Constructor
	 * 
	 * @param IND_MOV
	 */
    public RA001(Boolean hasTransaction) {
        super();
        this.IND_MOV = hasTransaction ? "0" : "1";
    }

    /**
	 * Formata o Bloco A Registro 001
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(IND_MOV).append(PIPE).append(EOL);
        return format.toString();
    }
}
