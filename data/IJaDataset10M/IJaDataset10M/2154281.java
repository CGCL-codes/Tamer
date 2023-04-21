package org.adempierelbr.sped.ecd.beans;

import java.math.BigDecimal;
import org.adempierelbr.sped.RegSped;
import org.adempierelbr.util.RemoverAcentos;
import org.adempierelbr.util.TextUtil;

/**
 * Identificação do Arquivo
 * 
 * Bloco J Registro J100 - BALANÇO PATRIMONIAL
 * 
 * Ocorrência - 1:N Os registros devem ser gerados na mesma ordem em que devem
 * ser visualizados. Campo 02 – COD_AGL. Devem ser informados códigos para todas
 * as linhas nas quais exista valor. Campo 05 – DESCR_COD_AGL. A definição da
 * descrição, função e funcionamento do código de aglutinação são prerrogativa e
 * responsabilidade do empresário ou sociedade empresária.
 * 
 * 
 * @author Priscila Pinheiro (Kenos, www.kenos.com.br)
 * @author Mario Grigioni, mgrigioni
 * @version $Id: RJ100.java, 18/11/2010, 11:36:00, mgrigioni
 */
public class RJ100 extends RegSped {

    private String COD_AGL;

    private BigDecimal NIVEL_AGL;

    private String IND_GPR_BAL;

    private String DESCR_COD_AGL;

    private BigDecimal VL_CTA;

    private String IND_DC_BAL;

    /**
	 * Constructor
	 */
    public RJ100(String COD_AGL, BigDecimal NIVEL_AGL, String IND_GPR_BAL, String DESCR_COD_AGL, BigDecimal VL_CTA, String IND_DC_BAL) {
        super();
        this.COD_AGL = COD_AGL;
        this.NIVEL_AGL = NIVEL_AGL;
        this.DESCR_COD_AGL = DESCR_COD_AGL;
        this.VL_CTA = VL_CTA;
        this.IND_DC_BAL = IND_DC_BAL;
    }

    /**
	 * Formata o Bloco J Registro 100
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(TextUtil.checkSize(COD_AGL, 255)).append(PIPE).append(TextUtil.toNumeric(NIVEL_AGL, 0, 255)).append(PIPE).append(TextUtil.checkSize(IND_GPR_BAL, 1)).append(PIPE).append(TextUtil.checkSize(RemoverAcentos.remover(DESCR_COD_AGL), 255)).append(PIPE).append(TextUtil.toNumeric(VL_CTA, 0, 255)).append(PIPE).append(TextUtil.checkSize(IND_DC_BAL, 1)).append(PIPE);
        return (TextUtil.removeEOL(format).append(EOL)).toString();
    }
}
