package org.adempierelbr.process;

import java.util.logging.Level;
import org.adempierelbr.model.MLBRNotaFiscal;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;

/**
 *	ProcReactivatenF
 *
 *  Process to reactivate Nota Fiscal
 *  
 *  [ 1986687 ] Criação de um Processo para Reativar Nota Fiscal
 *	 
 *	@author Mario Grigioni
 *	@version $Id: ProcReactivate.java, 06/06/2008 14:16:00 mgrigioni
 */
public class ProcReactivateNF extends SvrProcess {

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(ProcReactivateNF.class);

    private int p_LBR_NotaFiscal_ID = 0;

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
        p_LBR_NotaFiscal_ID = getRecord_ID();
        if (p_LBR_NotaFiscal_ID == 0) throw new IllegalArgumentException("Nota Fiscal == 0");
        log.info("ProcReactivateNF Process Nota Fiscal " + p_LBR_NotaFiscal_ID);
        MLBRNotaFiscal nf = new MLBRNotaFiscal(getCtx(), p_LBR_NotaFiscal_ID, get_TrxName());
        if (nf.isCancelled()) return "Não é permitido reativar uma Nota Fiscal Cancelada";
        nf.setProcessed(false);
        nf.save(get_TrxName());
        return "ProcReactivateNF Process Completed";
    }
}
