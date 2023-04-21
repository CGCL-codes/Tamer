package org.adempierelbr.sped.efd.piscofins.beans;

import org.adempierelbr.sped.RegSped;
import org.adempierelbr.util.RemoverAcentos;
import org.adempierelbr.util.TextUtil;

/**
 * REGISTRO 0190: IDENTIFICAÇÃO DAS UNIDADES DE MEDIDA
 * @author Mario Grigioni, mgrigioni
 * @version $Id: R0190.java, 04/02/2011, 09:28:00, mgrigioni
 */
public class R0190 extends RegSped {

    private String UNID;

    private String DESCR;

    /**
	 * Constructor
	 * @param UNID
	 * @param DESCR
	 */
    public R0190(String UNID, String DESCR) {
        super();
        setUNID(UNID, DESCR);
    }

    public String getUNID() {
        return this.UNID;
    }

    private void setUNID(String UNID, String DESCR) {
        if (UNID == null || UNID.isEmpty()) {
            this.UNID = "UN";
            this.DESCR = "UNIDADE";
        } else {
            this.UNID = TextUtil.checkSize(RemoverAcentos.remover(UNID), 6).toUpperCase();
            if (DESCR == null || DESCR.trim().isEmpty()) DESCR = "SEM DESCRICAO";
            this.DESCR = DESCR;
        }
    }

    /**
	 * Formata o Bloco 0 Registro 190
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(UNID).append(PIPE).append(TextUtil.checkSize(RemoverAcentos.remover(DESCR), 255)).append(PIPE);
        return (TextUtil.removeEOL(format).append(EOL)).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((UNID == null) ? 0 : UNID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        R0190 other = (R0190) obj;
        if (UNID == null) {
            if (other.UNID != null) return false;
        } else if (!UNID.equals(other.UNID)) return false;
        return true;
    }
}
