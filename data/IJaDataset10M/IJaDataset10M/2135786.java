package sisi.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class LivelliutentiWindow extends Window implements org.zkoss.zk.ui.ext.AfterCompose {

    private Window finestraLivUte;

    private Listbox boxUtenti;

    @SuppressWarnings("unchecked")
    private List tutti_gli_utenti;

    private ListModelList listModelList;

    protected Map<String, Object> args;

    private sisi.users.Users utente;

    @SuppressWarnings("unchecked")
    public void onCreate$finestraLivUte(Event event) {
        CreateEvent ce = (CreateEvent) ((ForwardEvent) event).getOrigin();
        args = (Map<String, Object>) ce.getArg();
    }

    public void afterCompose() {
        Components.wireVariables(this, this);
        finestraLivUte.setTitle("Utenti");
        String nomeUtente = (String) Sessions.getCurrent().getAttribute("User");
        utente = new sisi.users.UsersController().getUserXNom(nomeUtente);
        listModelList = new ListModelList();
        tutti_gli_utenti = new UsersController().getListUtenti(utente.getDatabase());
        listModelList.addAll(tutti_gli_utenti);
        boxUtenti.setModel(listModelList);
        boxUtenti.setItemRenderer(new UtentiItemRenderer());
        Components.addForwards(this, this);
    }

    public void onDoubleClicked(Event event) throws Exception {
        modificaLivello();
    }

    public void onClick$buttonModifica(Event event) throws SuspendNotAllowedException, InterruptedException {
        modificaLivello();
    }

    @SuppressWarnings("unchecked")
    public void modificaLivello() throws SuspendNotAllowedException, InterruptedException {
        if (boxUtenti.getSelectedIndex() >= 0) {
            HashMap map = new HashMap();
            Object ut = boxUtenti.getSelectedItem().getAttribute("rigaUtente");
            ut = new UsersController().refreshUser((Users) ut);
            map.put("nomeUtenteSelezionato", "" + ((Users) ut).getUserName());
            map.put("boxUtenti", boxUtenti);
            Window finestra4 = (Window) Executions.createComponents("/editLivelli.zul", null, map);
            finestra4.doModal();
        } else {
            Messagebox.show("Selezionare una riga da Modificare", "Information", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public class UtentiItemRenderer implements ListitemRenderer {

        public void render(Listitem li, Object data) {
            String nomeUtente = ((Users) data).getUserName();
            Listcell lc;
            lc = new Listcell(nomeUtente);
            if (nomeUtente.equals(utente.getUserName())) {
                lc.setStyle("color:red;");
            }
            lc.setParent(li);
            li.setAttribute("rigaUtente", data);
            new Listcell().setParent(li);
            ComponentsCtrl.applyForward(li, "onDoubleClick=onDoubleClicked");
        }
    }
}
