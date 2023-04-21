package org.adempierelbr.sped.ecd.beans;

import org.adempierelbr.sped.RegSped;
import org.adempierelbr.util.RemoverAcentos;
import org.adempierelbr.util.TextUtil;

/**
 * Identificação do Arquivo
 * 
 * Bloco J Registro J930 - IDENTIFICAÇÃO DOS SIGNATÁRIOS DA ESCRITURAÇÃO
 * 
 * Ocorrência - 1:N Chave: [IDENT_CPF]+[COD_ASSIN]
 * 
 * @author Priscila Pinheiro (Kenos, www.kenos.com.br)
 * @author Mario Grigioni, mgrigioni
 * @version $Id: RJ930.java, 18/11/2010, 14:24:00, mgrigioni
 */
public class RJ930 extends RegSped {

    private String IDENT_NOM;

    private String IDENT_CPF;

    private String IDENT_QUALIF;

    private String COD_ASSIM;

    private String IND_CRC;

    /**
	 * Constructor
	 */
    public RJ930(String IDENT_NOM, String IDENT_CPF, String IDENT_QUALIF, String COD_ASSIM, String IND_CRC) {
        super();
        this.IDENT_NOM = IDENT_NOM;
        this.IDENT_CPF = IDENT_CPF;
        this.IDENT_QUALIF = IDENT_QUALIF;
        this.COD_ASSIM = COD_ASSIM;
        this.IND_CRC = IND_CRC;
    }

    /**
	 * Formata o Bloco J Registro 930
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(TextUtil.checkSize(RemoverAcentos.remover(IDENT_NOM), 255)).append(PIPE).append(TextUtil.toNumeric(IDENT_CPF)).append(PIPE).append(TextUtil.checkSize(RemoverAcentos.remover(IDENT_QUALIF), 255)).append(PIPE).append(TextUtil.checkSize(COD_ASSIM, 3)).append(PIPE).append(TextUtil.checkSize(IND_CRC, 11)).append(PIPE);
        return (TextUtil.removeEOL(format).append(EOL)).toString();
    }
}
