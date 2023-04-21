package com.miden2ena.mogeci.action;

import com.miden2ena.mogeci.actionForm.ShowAttivitaForm;
import com.miden2ena.mogeci.dao.CompetenzaDao;
import com.miden2ena.mogeci.facade.Facade;
import com.miden2ena.mogeci.exceptions.DatabaseException;
import com.miden2ena.mogeci.exceptions.WFEConnectionException;
import com.miden2ena.mogeci.exceptions.WFEWorklistException;
import com.miden2ena.mogeci.wfe.WFEController;
import org.hibernate.HibernateException;
import com.miden2ena.mogeci.util.ConfigUtil;
import com.miden2ena.mogeci.util.FlowHeader;
import com.miden2ena.mogeci.util.Subject;
import com.miden2ena.mogeci.wfe.WFEReader;
import fr.improve.struts.taglib.layout.menu.MenuComponent;
import fr.improve.struts.taglib.layout.util.LayoutUtils;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import openwfe.org.worklist.WorkListException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * La classe MenuResponsabileAction restituisce il forward che indirizza alla sezione selezionata nel men�
 * @author Miden2ena
 * @version
 */
public class MenuResponsabileAction extends DispatchAction {

    /**
     * variabile ritornata se si sceoglie di tornare alla home
     */
    private static final String HOME = "home";

    /**
     * variabile ritornata se si sceglie inserire un CV arrivato da mail
     */
    private static final String INSCVMAIL = "insCVMail";

    /**
     * variabile ritornata se si sceglie di inserire un CV
     */
    private static final String INSCV = "insCV";

    /**
     * variabile ritornata se si vuole ricercare un CV
     */
    private static final String RICERCA = "ricerca";

    /**
     * variabile ritornata se si vuole vedere la pagina delle attivit�
     */
    private static final String ATTIVITA = "attivita";

    /**
     * variabile ritornata se si incontra qualche errore
     */
    private static final String ERRORE = "errore";

    /**
     * Metodo che torna il forward alla home
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws Exception
     *
     */
    public ActionForward home(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(HOME);
    }

    /**
     * Metodo che torna il forward alla pagina di inserimento CV ricevuto da mail
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws Exception
     *
     */
    public ActionForward insCVMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(INSCVMAIL);
    }

    /**
     * Metodo che torna il forward alla pagina di inserimento CV manuale
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws Exception
     *
     */
    public ActionForward insCV(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        return mapping.findForward(INSCV);
    }

    /**
     * Metodo che torna il forward alla pagina di ricerca
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws DatabaseException
     *
     */
    public ActionForward ricerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws DatabaseException {
        HttpSession session = request.getSession();
        Facade f = new Facade();
        try {
            ArrayList aList = f.findAllLingue();
            ArrayList bList = f.findAllCompetenzaSenzaLingue();
            session.setAttribute("listaLingue", aList);
            session.setAttribute("listaCompetenze", bList);
        } catch (HibernateException ex) {
            ex.printStackTrace();
            ActionMessages errors = new ActionMessages();
            errors.add("mysql", new ActionMessage("mysql non e' connesso "));
            saveErrors(request, errors);
            return mapping.findForward(ERRORE);
        }
        return mapping.findForward(RICERCA);
    }

    /**
     * Metodo che torna il forward alla pagina di visione delle attivit�
     * @param mapping la mappatura dell'Action e ActionForm
     * @param form Actionform passato come parametro
     * @param request contenente la richiesta del client
     * @param response contenente la risposta del server
     * @return mapping contiene il forward
     * @throws Exception
     *
     */
    public ActionForward attivita(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NotBoundException, RemoteException, MalformedURLException, WFEConnectionException, WFEWorklistException, WorkListException, Exception {
        HttpSession session = request.getSession();
        WFEController ctrl = WFEController.getInstance();
        java.util.Map map = null;
        try {
            map = ctrl.getStoreMap("responsabile", "mogeci", ConfigUtil.WORK_SESSION_SERVER_URL);
        } catch (WFEWorklistException ex) {
            ex.printStackTrace();
            ActionMessages errors = new ActionMessages();
            errors.add("wfe", new ActionMessage("il wfe non e' connesso "));
            saveErrors(request, errors);
            return mapping.findForward(ERRORE);
        } catch (WFEConnectionException ex) {
            ex.printStackTrace();
            ActionMessages errors = new ActionMessages();
            errors.add("wfe", new ActionMessage("il wfe non e' connesso "));
            saveErrors(request, errors);
            return mapping.findForward(ERRORE);
        }
        MenuComponent lc_menu = new MenuComponent();
        lc_menu.setName("store");
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry lc_entry = (Map.Entry) it.next();
            String lc_header = (String) lc_entry.getKey();
            MenuComponent lc_subject = createChildMenu(lc_menu, lc_header.replaceAll("_", "-"), lc_header.replaceAll("_", "-"));
            Iterator lc_it2 = ((List) lc_entry.getValue()).iterator();
            while (lc_it2.hasNext()) {
                FlowHeader tmp = (FlowHeader) lc_it2.next();
                String indMail = null;
                if (((tmp.getMapAttributi()).get(ConfigUtil.FLOW_FIELD_INDMAIL)) != null) indMail = ((tmp.getMapAttributi()).get(ConfigUtil.FLOW_FIELD_INDMAIL)).toString();
                MenuComponent lc_newsItem = createChildMenu(lc_subject, tmp.getCognomeNomeCandidato() + "||mail:" + indMail + " ||FEId: " + tmp.getFlowExpressionId() + " || date: " + tmp.getDataCreazione(), tmp.getFlowExpressionId());
                lc_newsItem.setLocation("menuResponsabile.do?reqCode=attivita&storeName=" + tmp.getStore() + "&flowExpressionId=" + tmp.getFlowExpressionId());
            }
        }
        LayoutUtils.addMenuIntoSession(request, lc_menu);
        String lc_headerId = request.getParameter("flowExpressionId");
        Subject element = new Subject();
        if (lc_headerId != null) {
            FlowHeader selez = ctrl.getWorkItemHeader("responsabile", "mogeci", com.miden2ena.mogeci.util.ConfigUtil.WORK_SESSION_SERVER_URL, lc_headerId);
            element.setFlowExpressionId(selez.getFlowExpressionId());
            element.setIdCV(selez.getIdCV());
            element.setDataCreazione(selez.getDataCreazione());
            element.setDataModifica(selez.getDataModifica());
            element.setParticipant(selez.getParticipant());
            if (selez.getCognomeNomeCandidato() != null) element.setCognomeNomeCandidato(selez.getCognomeNomeCandidato());
            if (((selez.getMapAttributi()).get(ConfigUtil.FLOW_FIELD_INDMAIL)) != null) element.setMail(((selez.getMapAttributi()).get(ConfigUtil.FLOW_FIELD_INDMAIL)).toString());
            request.setAttribute("subjects", element);
        }
        return mapping.findForward(ATTIVITA);
    }

    /**
     * Metodo che crea il menu per la visualizzazione delle attivit�
     * @param in_parent cotiene il men� precedente
     * @param in_title variabile che contiene il titolo
     * @param in_id contiene l'identificativo
     * @return lc_child contiene il MenuComponent costruito
     *
     */
    private MenuComponent createChildMenu(MenuComponent in_parent, String in_title, String in_id) {
        MenuComponent lc_child = new MenuComponent(in_id);
        in_parent.addMenuComponent(lc_child);
        lc_child.setTitle(in_title);
        return lc_child;
    }
}
