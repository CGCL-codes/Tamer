package com.miden2ena.mogeci.action;

import com.miden2ena.mogeci.actionForm.DettagliForm;
import com.miden2ena.mogeci.facade.Facade;
import com.miden2ena.mogeci.exceptions.DatabaseException;
import com.miden2ena.mogeci.report.CreateReport;
import com.miden2ena.mogeci.util.CvDettagli;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;

/**
 * Classe che implementa il metodo che cerca nel database tutti i dati legati ad un CV dato il DettagliForm
 * @author Miden2ena
 * @version
 */
public class DettagliResponsabileAction extends Action {

    /**
     * variabile ritornata se sono stati trovati i dettagli del CV
     */
    private static final String SUCCESS = "success";

    /**
     * variabile ritornata se avviene qualche errore
     */
    private static final String ERRORE = "errore";

    private static final String ECCEZIONE = "eccezione";

    /**
     * Metodo che cerca i dettagli di un CV in base ai parametri specificati nel DettagliForm
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro: DettagliForm
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws DatabaseException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws DatabaseException {
        DettagliForm candidato = (DettagliForm) form;
        Facade f = new Facade();
        CvDettagli cv = new CvDettagli();
        try {
            cv = f.ricercaCvById(candidato.getIdCv());
        } catch (HibernateException ex) {
            ex.printStackTrace();
            if (ex.getMessage().equals("RicercaCvById,Cv non certificato")) {
                ActionMessages errors = new ActionMessages();
                errors.add("facade", new ActionMessage("curriculum non certificato "));
                saveErrors(request, errors);
                return mapping.findForward(ECCEZIONE);
            }
            ActionMessages errors = new ActionMessages();
            errors.add("mysql", new ActionMessage("mysql non e' connesso "));
            saveErrors(request, errors);
            return mapping.findForward(ERRORE);
        }
        HttpSession session = request.getSession();
        session.setAttribute("candidato", cv);
        return mapping.findForward(SUCCESS);
    }
}
