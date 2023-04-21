package org.adempierelbr.sped.ecd.beans;

import org.adempierelbr.sped.CounterSped;
import org.adempierelbr.sped.RegSped;

/**
 * Identificação do Arquivo Bloco 9 Registro 9990 - ENCERRAMENTO DO BLOCO 9
 * Ocorrência - um por Arquivo
 * 
 * @author Priscila Pinheiro (Kenos, www.kenos.com.br)
 * @author Mario Grigioni, mgrigioni
 * @version $Id: R9990.java, 16/11/2010, 15:49:00, mgrigioni
 */
public class R9990 extends RegSped {

    private String QTD_LIN_9 = "";

    /**
	 * Constructor
	 * 
	 */
    public R9990() {
        super();
    }

    public String toString() {
        QTD_LIN_9 = "" + CounterSped.getBlockCounter(REG);
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(QTD_LIN_9).append(PIPE).append(EOL);
        return format.toString();
    }
}
