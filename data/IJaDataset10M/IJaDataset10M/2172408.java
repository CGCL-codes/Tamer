package org.openXpertya.wf;

import java.util.logging.Level;
import org.openXpertya.model.MUser;
import org.openXpertya.process.ProcessInfoParameter;
import org.openXpertya.process.StateEngine;
import org.openXpertya.process.SvrProcess;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class WFActivityManage extends SvrProcess {

    /** Descripción de Campos */
    private boolean p_IsAbort = false;

    /** Descripción de Campos */
    private int p_AD_User_ID = 0;

    /** Descripción de Campos */
    private int p_AD_WF_Responsible_ID = 0;

    /** Descripción de Campos */
    private int p_AD_WF_Activity_ID = 0;

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
            } else if (name.equals("IsAbort")) {
                p_IsAbort = "Y".equals(para[i].getParameter());
            } else if (name.equals("AD_User_ID")) {
                p_AD_User_ID = para[i].getParameterAsInt();
            } else if (name.equals("AD_WF_Responsible_ID")) {
                p_AD_WF_Responsible_ID = para[i].getParameterAsInt();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
        p_AD_WF_Activity_ID = getRecord_ID();
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
        MWFActivity activity = new MWFActivity(getCtx(), p_AD_WF_Activity_ID, get_TrxName());
        log.info("doIt - " + activity);
        MUser user = MUser.get(getCtx(), getAD_User_ID());
        if (p_IsAbort) {
            String msg = user.getName() + ": Abort";
            activity.setTextMsg(msg);
            activity.setAD_User_ID(getAD_User_ID());
            activity.setWFState(StateEngine.STATE_Aborted);
            return msg;
        }
        String msg = null;
        if ((p_AD_User_ID != 0) && (activity.getAD_User_ID() != p_AD_User_ID)) {
            MUser from = MUser.get(getCtx(), activity.getAD_User_ID());
            MUser to = MUser.get(getCtx(), p_AD_User_ID);
            msg = user.getName() + ": " + from.getName() + " -> " + to.getName();
            activity.setTextMsg(msg);
            activity.setAD_User_ID(p_AD_User_ID);
        }
        if ((p_AD_WF_Responsible_ID != 0) && (activity.getAD_WF_Responsible_ID() != p_AD_WF_Responsible_ID)) {
            MWFResponsible from = MWFResponsible.get(getCtx(), activity.getAD_WF_Responsible_ID());
            MWFResponsible to = MWFResponsible.get(getCtx(), p_AD_WF_Responsible_ID);
            String msg1 = user.getName() + ": " + from.getName() + " -> " + to.getName();
            activity.setTextMsg(msg1);
            activity.setAD_WF_Responsible_ID(p_AD_WF_Responsible_ID);
            if (msg == null) {
                msg = msg1;
            } else {
                msg += " - " + msg1;
            }
        }
        activity.save();
        return msg;
    }
}
