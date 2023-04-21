package org.adempierelbr.sped.efd.beans;

import java.sql.Timestamp;
import org.adempierelbr.sped.RegSped;
import org.adempierelbr.util.TextUtil;

/**
 * REGISTRO E100: PERÍODO DA APURAÇÃO DO ICMS.
 * @author Mario Grigioni, mgrigioni
 * @version $Id: RE100.java, 08/02/2011, 12:13:00, mgrigioni
 */
public class RE100 extends RegSped {

    private Timestamp DT_INI;

    private Timestamp DT_FIN;

    /**
	 * Constructor
	 * @param DT_INI
	 * @param DT_FIN
	 */
    public RE100(Timestamp DT_INI, Timestamp DT_FIN) {
        super();
        this.DT_INI = DT_INI;
        this.DT_FIN = DT_FIN;
    }

    /**
	 * Formata o Bloco E Registro 100
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(TextUtil.timeToString(DT_INI, "ddMMyyyy")).append(PIPE).append(TextUtil.timeToString(DT_FIN, "ddMMyyyy")).append(PIPE);
        return (TextUtil.removeEOL(format).append(EOL)).toString();
    }
}
