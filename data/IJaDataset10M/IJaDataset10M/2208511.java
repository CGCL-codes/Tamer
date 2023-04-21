package org.adempierelbr.sped.efd.piscofins.beans;

import org.adempierelbr.sped.CounterSped;
import org.adempierelbr.sped.RegSped;

/**
 * REGISTRO 0990: ENCERRAMENTO DO BLOCO 0
 * @author Mario Grigioni, mgrigioni
 * @version $Id: R0990.java, 19/01/2011, 11:47:00, mgrigioni
 */
public class R0990 extends RegSped {

    private String QTD_LIN_0 = "";

    /**
	 * Constructor
	 */
    public R0990() {
        super();
    }

    public String toString() {
        QTD_LIN_0 = "" + CounterSped.getBlockCounter(REG);
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(QTD_LIN_0).append(PIPE).append(EOL);
        return format.toString();
    }
}
