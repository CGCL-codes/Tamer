package org.adempierelbr.sped.efd.beans;

import java.math.BigDecimal;
import org.adempierelbr.sped.RegSped;
import org.adempierelbr.util.TextUtil;
import org.compiere.util.Env;

/**
 * REGISTRO C172: OPERAÇÕES COM ISSQN (CÓDIGO 01)
 * @author Mario Grigioni, mgrigioni
 * @version $Id: RC172.java, 11/02/2011, 14:45:00, mgrigioni
 */
public class RC172 extends RegSped {

    private RC170 rc170;

    private BigDecimal VL_SERV_NT;

    private BigDecimal VL_BC_ISSQN;

    private BigDecimal ALIQ_ISSQN;

    private BigDecimal VL_ISSQN;

    private BigDecimal VL_BC_IRRF;

    private BigDecimal VL_IRRF;

    private BigDecimal VL_BC_PREV;

    private BigDecimal VL_PREV;

    /**
	 * Constructor
	 * @param rc170
	 * @param VL_SERV_NT
	 * @param VL_BC_ISSQN
	 * @param ALIQ_ISSQN
	 * @param VL_ISSQN
	 * @param VL_BC_IRRF
	 * @param VL_IRRF
	 * @param VL_BC_PREV
	 * @param VL_PREV
	 */
    public RC172(RC170 rc170, BigDecimal VL_SERV_NT, BigDecimal VL_BC_ISSQN, BigDecimal ALIQ_ISSQN, BigDecimal VL_ISSQN, BigDecimal VL_BC_IRRF, BigDecimal VL_IRRF, BigDecimal VL_BC_PREV, BigDecimal VL_PREV) {
        super();
        this.rc170 = rc170;
        this.VL_SERV_NT = VL_SERV_NT;
        this.VL_BC_ISSQN = VL_BC_ISSQN;
        this.ALIQ_ISSQN = ALIQ_ISSQN;
        this.VL_ISSQN = VL_ISSQN;
        this.VL_BC_IRRF = VL_BC_IRRF;
        this.VL_IRRF = VL_IRRF;
        this.VL_BC_PREV = VL_BC_PREV;
        this.VL_PREV = VL_PREV;
    }

    public RC170 getRC170() {
        return this.rc170;
    }

    public BigDecimal getVL_SERV_NT() {
        return VL_SERV_NT == null ? Env.ZERO : VL_SERV_NT;
    }

    public BigDecimal getVL_BC_ISSQN() {
        return VL_BC_ISSQN == null ? Env.ZERO : VL_BC_ISSQN;
    }

    public BigDecimal getVL_ISSQN() {
        return VL_ISSQN == null ? Env.ZERO : VL_ISSQN;
    }

    public BigDecimal getVL_BC_IRRF() {
        return VL_BC_IRRF == null ? Env.ZERO : VL_BC_IRRF;
    }

    public BigDecimal getVL_IRRF() {
        return VL_IRRF == null ? Env.ZERO : VL_IRRF;
    }

    public BigDecimal getVL_BC_PREV() {
        return VL_BC_PREV == null ? Env.ZERO : VL_BC_PREV;
    }

    public BigDecimal getVL_PREV() {
        return VL_PREV == null ? Env.ZERO : VL_PREV;
    }

    /**
	 * Formata o Bloco C Registro 172
	 * 
	 * @return
	 */
    public String toString() {
        StringBuilder format = new StringBuilder(PIPE).append(REG).append(PIPE).append(TextUtil.toNumeric(VL_BC_ISSQN)).append(PIPE).append(TextUtil.toNumeric(ALIQ_ISSQN)).append(PIPE).append(TextUtil.toNumeric(VL_ISSQN)).append(PIPE);
        return (TextUtil.removeEOL(format).append(EOL)).toString();
    }
}
