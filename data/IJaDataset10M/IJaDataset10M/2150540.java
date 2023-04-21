package org.openXpertya.process;

import java.math.BigDecimal;
import java.util.logging.Level;
import org.openXpertya.model.MQuarterCategory;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class QuarterCategory_Copy extends SvrProcess {

    /**
     * Constructor de la clase ...
     *
     */
    public QuarterCategory_Copy() {
        super();
    }

    /** Descripción de Campos */
    private int m_C_Quarter_Category_ID = 0;

    /**
     * Descripción de Método
     *
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) {
                ;
            } else if (name.equals("C_Quarter_Category_ID")) {
                m_C_Quarter_Category_ID = ((BigDecimal) para[i].getParameter()).intValue();
            } else {
                log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
            }
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws Exception
     */
    protected String doIt() throws Exception {
        int to_ID = super.getRecord_ID();
        log.info("From C_Quarter_Category_ID=" + m_C_Quarter_Category_ID + ", To=" + to_ID);
        if (to_ID < 1) {
            throw new Exception(MSG_SaveErrorRowNotFound);
        }
        MQuarterCategory to = new MQuarterCategory(getCtx(), to_ID, null);
        String name = to.getName();
        int cat = to.getM_Product_Category_ID();
        MQuarterCategory qcFrom = new MQuarterCategory(getCtx(), m_C_Quarter_Category_ID, null);
        to.copyMQuarterCategoryFrom(qcFrom);
        to.setName(name);
        to.setM_Product_Category_ID(cat);
        to.save();
        return "@Copied@=" + "Ok.";
    }
}
