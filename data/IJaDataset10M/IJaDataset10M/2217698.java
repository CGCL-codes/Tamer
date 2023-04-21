package org.openXpertya.process;

import java.util.logging.Level;
import org.openXpertya.model.MRequest;
import org.openXpertya.util.ErrorUsuarioOXP;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class RequestReOpen extends SvrProcess {

    /** Descripción de Campos */
    private int p_R_Request_ID = 0;

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
            } else if (name.equals("R_Request_ID")) {
                p_R_Request_ID = para[i].getParameterAsInt();
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
        MRequest request = new MRequest(getCtx(), p_R_Request_ID, get_TrxName());
        log.info(request.toString());
        if (request.getID() == 0) {
            throw new ErrorUsuarioOXP("@NotFound@ @R_Request_ID@ " + p_R_Request_ID);
        }
        request.setR_Status_ID();
        request.setProcessed(false);
        if (request.save() && !request.isProcessed()) {
            return "@OK@";
        }
        return "@Error@";
    }
}
