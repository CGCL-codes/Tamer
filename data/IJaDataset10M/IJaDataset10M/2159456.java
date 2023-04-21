package com.miden2ena.mogeci.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 * Classe che implementa il metodo per autenticare il responsabile e intervistatore
 * @author Miden2ena
 * @version
 */
public class EntraAction extends DispatchAction {

    /**
     * variabile ritornata se � stato autenticato l'intervistatore
     */
    private static final String INTERVISTATORE = "intervistatore";

    /**
     * variabile ritornata se � stato autenticato il responsabile
     */
    private static final String RESPONSABILE = "responsabile";

    /**
     * Metodo che autentica l'accesso del responsabile
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws Exception
     */
    public ActionForward responsabile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(RESPONSABILE);
    }

    /**
     * Metodo che autentica l'accesso dell'intervistatore
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws Exception
     */
    public ActionForward intervistatore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(INTERVISTATORE);
    }
}
