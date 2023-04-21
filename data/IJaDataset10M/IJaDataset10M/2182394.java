package org.adempierelbr.sacred.beans;

import org.adempierelbr.sacred.CounterSacred;
import org.adempierelbr.sacred.RegSacred;
import org.adempierelbr.util.TextUtil;

/**
 * REGISTRO 5990: ENCERRAMENTO DO BLOCO 5
 * 
 * @author Mario Grigioni
 * @version $Id: B5R5990.java, 14/04/2010, 14:30, mgrigioni
 */
public class B5R5990 implements RegSacred {

    private final String BLOCO = "B5";

    private final String REG = "5990";

    private String QTD_LIN_0 = "";

    /**
	 * Constructor
	 */
    public B5R5990() {
        addCounter();
    }

    public String toString() {
        QTD_LIN_0 = "" + CounterSacred.getBlockCounter(BLOCO);
        String format = REG + PIPE + QTD_LIN_0;
        return TextUtil.removeEOL(format) + EOL;
    }

    public void addCounter() {
        CounterSacred.register(BLOCO + REG);
    }
}
