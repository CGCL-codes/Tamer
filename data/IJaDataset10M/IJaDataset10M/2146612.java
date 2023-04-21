package org.openXpertya.process;

import java.math.BigDecimal;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.openXpertya.model.MInvoice;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CopyFromInvoice extends SvrProcess {

    /** Descripción de Campos */
    private int m_C_Invoice_ID = 0;

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
            } else if (name.equals("C_Invoice_ID")) {
                m_C_Invoice_ID = ((BigDecimal) para[i].getParameter()).intValue();
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
        int To_C_Invoice_ID = getRecord_ID();
        log.info("From C_Invoice_ID=" + m_C_Invoice_ID + " to " + To_C_Invoice_ID);
        if (To_C_Invoice_ID == 0) {
            throw new IllegalArgumentException("Target C_Invoice_ID == 0");
        }
        if (m_C_Invoice_ID == 0) {
            throw new IllegalArgumentException("Source C_Invoice_ID == 0");
        }
        MInvoice from = new MInvoice(getCtx(), m_C_Invoice_ID, null);
        MInvoice to = new MInvoice(getCtx(), To_C_Invoice_ID, null);
        int no = to.copyLinesFrom(from, false, false);
        return "@Copied@=" + no;
    }
}
