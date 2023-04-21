package org.adempierelbr.sped.efd.piscofins.beans;

import java.math.BigDecimal;
import org.adempierelbr.sped.RegSped;
import org.adempierelbr.util.RemoverAcentos;
import org.adempierelbr.util.TextUtil;
import org.compiere.util.Env;

/**
 * REGISTRO A170: COMPLEMENTO DO DOCUMENTO - ITENS DO DOCUMENTO
 * @author Mario Grigioni, mgrigioni
 * @version $Id: RA170.java, 19/01/2011, 17:14:00, mgrigioni
 */
public class RA170 extends RegSped implements Comparable<Object> {

    private Integer NUM_ITEM;

    private String COD_ITEM;

    private String DESCR_COMPL;

    private String NAT_BC_CRED;

    private String IND_ORIG_CRED;

    private String CST_PIS;

    private String CST_COFINS;

    private String COD_CTA;

    private String COD_CCUS;

    private String CFOP;

    private BigDecimal VL_ITEM;

    private BigDecimal VL_DESC;

    private BigDecimal VL_BC_PIS;

    private BigDecimal ALIQ_PIS;

    private BigDecimal VL_PIS;

    private BigDecimal VL_BC_COFINS;

    private BigDecimal ALIQ_COFINS;

    private BigDecimal VL_COFINS;

    private BigDecimal VL_OPR;

    /**
	 * Constructor
	 * @param NUM_ITEM
	 * @param COD_ITEM
	 * @param DESCR_COMPL
	 * @param VL_ITEM
	 * @param VL_DESC
	 * @param NAT_BC_CRED
	 * @param IND_ORIG_CRED
	 * @param CST_PIS
	 * @param VL_BC_PIS
	 * @param ALIQ_PIS
	 * @param VL_PIS
	 * @param CST_COFINS
	 * @param VL_BC_COFINS
	 * @param ALIQ_COFINS
	 * @param VL_COFINS
	 * @param COD_CTA
	 * @param COD_CCUS
	 * @param CFOP
	 * @param VL_OPR
	 */
    public RA170(int NUM_ITEM, String COD_ITEM, String DESCR_COMPL, BigDecimal VL_ITEM, BigDecimal VL_DESC, String NAT_BC_CRED, String IND_ORIG_CRED, String CST_PIS, BigDecimal VL_BC_PIS, BigDecimal ALIQ_PIS, BigDecimal VL_PIS, String CST_COFINS, BigDecimal VL_BC_COFINS, BigDecimal ALIQ_COFINS, BigDecimal VL_COFINS, String COD_CTA, String COD_CCUS, String CFOP, BigDecimal VL_OPR) {
        super();
        this.NUM_ITEM = NUM_ITEM;
        this.COD_ITEM = COD_ITEM;
        this.DESCR_COMPL = DESCR_COMPL;
        this.NAT_BC_CRED = NAT_BC_CRED;
        this.IND_ORIG_CRED = IND_ORIG_CRED;
        this.CST_PIS = CST_PIS;
        this.CST_COFINS = CST_COFINS;
        this.COD_CTA = COD_CTA;
        this.COD_CCUS = COD_CCUS;
        this.VL_ITEM = VL_ITEM;
        this.VL_DESC = VL_DESC;
        this.VL_BC_PIS = VL_BC_PIS;
        this.ALIQ_PIS = ALIQ_PIS;
        this.VL_PIS = VL_PIS;
        this.VL_BC_COFINS = VL_BC_COFINS;
        this.ALIQ_COFINS = ALIQ_COFINS;
        this.VL_COFINS = VL_COFINS;
        this.CFOP = CFOP;
        this.VL_OPR = VL_OPR;
    }

    public BigDecimal getVL_PIS() {
        return VL_PIS == null ? Env.ZERO : VL_PIS;
    }

    public BigDecimal getVL_COFINS() {
        return VL_COFINS == null ? Env.ZERO : VL_COFINS;
    }

    public BigDecimal getVL_OPR() {
        return VL_OPR == null ? Env.ZERO : VL_OPR;
    }

    public String getCFOP() {
        return CFOP;
    }

    /**
	 * Formata o Bloco A Registro 170
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(TextUtil.lPad(NUM_ITEM, 3)).append(PIPE).append(COD_ITEM).append(PIPE).append(TextUtil.checkSize(RemoverAcentos.remover(DESCR_COMPL), 255)).append(PIPE).append(TextUtil.toNumeric(VL_ITEM, 2)).append(PIPE).append(TextUtil.toNumeric(VL_DESC, 2)).append(PIPE).append(TextUtil.checkSize(NAT_BC_CRED, 2)).append(PIPE).append(TextUtil.checkSize(IND_ORIG_CRED, 1)).append(PIPE).append(TextUtil.checkSize(CST_PIS, 2)).append(PIPE).append(TextUtil.toNumeric(VL_BC_PIS, 2)).append(PIPE).append(TextUtil.toNumeric(ALIQ_PIS, 2)).append(PIPE).append(TextUtil.toNumeric(VL_PIS, 2)).append(PIPE).append(TextUtil.checkSize(CST_COFINS, 2)).append(PIPE).append(TextUtil.toNumeric(VL_BC_COFINS, 2)).append(PIPE).append(TextUtil.toNumeric(ALIQ_COFINS, 2)).append(PIPE).append(TextUtil.toNumeric(VL_COFINS, 2)).append(PIPE).append(TextUtil.checkSize(COD_CTA, 60)).append(PIPE).append(TextUtil.checkSize(COD_CCUS, 60)).append(PIPE);
        return (TextUtil.removeEOL(format).append(EOL)).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((NUM_ITEM == null) ? 0 : NUM_ITEM.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        RA170 other = (RA170) obj;
        if (NUM_ITEM == null) {
            if (other.NUM_ITEM != null) return false;
        } else if (!NUM_ITEM.equals(other.NUM_ITEM)) return false;
        return true;
    }

    /**
	 * 	Comparador para Collection
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    public int compare(Object o1, Object o2) {
        if (o1 == null) return 1; else if (o2 == null) return -1; else if (o1 instanceof RA170 && o2 instanceof RA170) {
            RA170 e1 = (RA170) o1;
            RA170 e2 = (RA170) o2;
            if (e1.NUM_ITEM == null) return 1; else if (e2.NUM_ITEM == null) return -1; else return e1.NUM_ITEM.compareTo(e2.NUM_ITEM);
        } else return 0;
    }

    /**
	 * 	Comparador para Collection
	 */
    public int compareTo(Object o) {
        return compare(this, o);
    }
}
