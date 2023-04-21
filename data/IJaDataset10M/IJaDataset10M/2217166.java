package org.adempierelbr.process;

import java.util.logging.Level;
import org.adempierelbr.model.MLBRBoleto;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

/**
 *	ProcCancelBoleto
 *
 *	Process to Cancel Boleto
 *	
 *	@author Mario Grigioni (Kenos, www.kenos.com.br)
 *	@version $Id: ProcCancelBoleto.java, 12/03/2008 15:14:00 mgrigioni
 */
public class ProcCancelBoleto extends SvrProcess {

    /** Invoice               */
    private static int p_C_Invoice_ID = 0;

    /**
	 *  Prepare - e.g., get Parameters.
	 */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) ; else log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
        }
    }

    /**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
    protected String doIt() throws Exception {
        log.info("ProcCancelBoleto Process " + "Fatura: " + p_C_Invoice_ID);
        p_C_Invoice_ID = getRecord_ID();
        if (p_C_Invoice_ID != 0) {
            MLBRBoleto.cancelBoleto(getCtx(), p_C_Invoice_ID, get_TrxName());
        }
        return "ProcCancelBoleto Process Completed " + "Fatura: " + p_C_Invoice_ID;
    }
}
