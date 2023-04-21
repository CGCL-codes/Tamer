package sisi.gruppi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import sisi.EditVarie;
import sisi.operazioni.*;
import sisi.users.CfgUtentiController;

@SuppressWarnings("serial")
public class GruppiWindow extends Window implements org.zkoss.zk.ui.ext.AfterCompose {

    private Window finestraGruppi;

    private Textbox cercaGruppo;

    private Listbox boxGruppi;

    @SuppressWarnings("unchecked")
    private List tutti_i_gruppi;

    private Toolbarbutton buttonSeleziona, buttonNuovo, buttonModifica, buttonCancella, buttonStampa, buttonExcel;

    private String nomeUtente;

    private List<?> dirittiUtente;

    private ListModelList listModelList;

    private String cerca = "";

    protected Map<String, Object> args;

    @SuppressWarnings("unchecked")
    public void onCreate$finestraGruppi(Event event) {
        CreateEvent ce = (CreateEvent) ((ForwardEvent) event).getOrigin();
        args = (Map<String, Object>) ce.getArg();
        String tastoSeleziona = (String) args.get("tastoSeleziona");
        tastoSeleziona = (tastoSeleziona == null || tastoSeleziona.isEmpty() ? "N" : tastoSeleziona);
        boolean lTastoSeleziona = tastoSeleziona.equals("S");
        if (!lTastoSeleziona) {
            buttonSeleziona.setVisible(false);
        }
        nomeUtente = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("User");
        dirittiUtente = new CfgUtentiController().getListDirittiUtente(nomeUtente);
        if (dirittiUtente.contains("021201")) {
            buttonNuovo.setDisabled(true);
        }
        if (dirittiUtente.contains("021202")) {
            buttonModifica.setDisabled(true);
        }
        if (dirittiUtente.contains("021203")) {
            buttonCancella.setDisabled(true);
        }
        if (dirittiUtente.contains("021204")) {
            buttonStampa.setDisabled(true);
        }
        if (dirittiUtente.contains("021205")) {
            buttonExcel.setDisabled(true);
        }
    }

    public void afterCompose() {
        Components.wireVariables(this, this);
        finestraGruppi.setTitle(Labels.getLabel("GRUPPI_TITOLO_FINESTRA"));
        listModelList = new ListModelList();
        tutti_i_gruppi = new GruppiController().getListGruppi("");
        listModelList.addAll(tutti_i_gruppi);
        boxGruppi.setModel(listModelList);
        boxGruppi.setItemRenderer(new GruppiItemRenderer());
        Components.addForwards(this, this);
        cercaGruppo.addEventListener("onChanging", new EventListener() {

            public void onEvent(Event event) {
                final InputEvent evt = (InputEvent) event;
                String valore = evt.getValue();
                cerca = valore;
                cercaGruppo(cerca);
                cercaGruppo.setFocus(true);
            }
        });
    }

    public void onDoubleClicked(Event event) throws Exception {
        if (buttonSeleziona.isVisible()) {
            goSeleziona();
        } else {
            modificaGruppo();
        }
    }

    public void onClick$buttonModifica(Event event) {
        modificaGruppo();
    }

    @SuppressWarnings("unchecked")
    public void modificaGruppo() {
        if (dirittiUtente.contains("021202")) {
            return;
        }
        try {
            int nIndex = boxGruppi.getSelectedIndex();
            if (nIndex == -1) {
                Messagebox.show(Labels.getLabel("VOCI_SELEZIONARE_RIGA_DA_MODIFICARE"), Labels.getLabel("VOCI_INFORMAZIONE"), Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Object gruppoItem = boxGruppi.getSelectedItem().getAttribute("rigaGruppo");
                Gruppi modGruppo = new GruppiController().refreshGruppo((Gruppi) gruppoItem);
                gruppoItem = modGruppo;
                EditVarie editVarie = new EditVarie();
                editVarie.azzeraParametri();
                editVarie.setParLabelGet(Labels.getLabel("VOCI_DESCRIZIONE"));
                editVarie.setParTitoloFinestra(Labels.getLabel("GRUPPI_MODIFICA_GRUPPO"));
                editVarie.setParMaxLenght(100);
                editVarie.setParMaxCols(80);
                editVarie.setParValoreGet(((Gruppi) gruppoItem).getDescrizione());
                HashMap map = new HashMap();
                map.put("editVarie", editVarie);
                Window finestraRigaNota = (Window) Executions.createComponents("/editVarie.zul", null, map);
                try {
                    finestraRigaNota.doModal();
                } catch (SuspendNotAllowedException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String valore = editVarie.getValoreRitorno();
                if (valore != null && !valore.isEmpty()) {
                    Gruppi gruppox = new Gruppi();
                    gruppox.setDescrizione(valore);
                    gruppox.setId(((Gruppi) gruppoItem).getId());
                    new GruppiController().updateGruppo((Gruppi) gruppox);
                    new OperazioniController().nuovaOperazione(Labels.getLabel("GRUPPI_MODIFICA_GRUPPO") + ": " + ((Gruppi) gruppox).getId() + "-" + ((Gruppi) gruppox).getDescrizione(), "gruppi");
                    List children = boxGruppi.getSelectedItem().getChildren();
                    ((Listcell) children.get(0)).setLabel("" + gruppox.getId());
                    ((Listcell) children.get(1)).setLabel(gruppox.getDescrizione());
                }
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClick$buttonExcel(Event event) throws IOException {
        esportaExcel();
    }

    public void onClick$buttonSeleziona(Event event) {
        goSeleziona();
    }

    void goSeleziona() {
        if (boxGruppi.getSelectedIndex() >= 0) {
            Object gruppo = boxGruppi.getSelectedItem().getAttribute("rigaGruppo");
            int codiceSelezionato = ((Gruppi) gruppo).getId();
            Sessions.getCurrent().setAttribute("codiceGruppoSelezionato", codiceSelezionato);
            finestraGruppi.detach();
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonNuovo(Event event) {
        try {
            EditVarie editVarie = new EditVarie();
            editVarie.azzeraParametri();
            editVarie.setParLabelGet(Labels.getLabel("VOCI_DESCRIZIONE"));
            editVarie.setParTitoloFinestra(Labels.getLabel("GRUPPI_NUOVO_GRUPPO"));
            editVarie.setParMaxLenght(100);
            editVarie.setParMaxCols(80);
            HashMap map = new HashMap();
            map.put("editVarie", editVarie);
            Window finestraRigaNota = (Window) Executions.createComponents("/editVarie.zul", null, map);
            try {
                finestraRigaNota.doModal();
            } catch (SuspendNotAllowedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String valore = editVarie.getValoreRitorno();
            if (valore != null && !valore.isEmpty()) {
                Gruppi gruppox = new Gruppi();
                gruppox.setDescrizione(valore);
                gruppox = new GruppiController().addGruppo(gruppox);
                new OperazioniController().nuovaOperazione(Labels.getLabel("GRUPPI_NUOVO_GRUPPO") + ": " + ((Gruppi) gruppox).getId() + "-" + ((Gruppi) gruppox).getDescrizione(), "gruppi");
                ListModelList lml = (ListModelList) boxGruppi.getListModel();
                if (lml.indexOf(gruppox) == -1) {
                    lml.add(gruppox);
                    boxGruppi.setSelectedIndex(lml.indexOf(gruppox));
                } else {
                    lml.set(lml.indexOf(gruppox), gruppox);
                }
                boxGruppi.invalidate();
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        }
    }

    public void onClick$buttonCancella(Event event) throws InterruptedException {
        delete();
    }

    void delete() throws InterruptedException {
        int nIndex = boxGruppi.getSelectedIndex();
        if (nIndex == -1) {
            try {
                Messagebox.show(Labels.getLabel("VOCI_SELEZIONARE_RIGA_DA_CANCELLARE"), Labels.getLabel("VOCI_INFORMAZIONE"), Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        } else {
            Object gruppo = boxGruppi.getSelectedItem().getAttribute("rigaGruppo");
            if (gruppo == null) {
                return;
            }
            if (Messagebox.show(Labels.getLabel("GRUPPI_CONFERMA_CANCELLAZIONE") + ": " + ((Gruppi) gruppo).getDescrizione().trim() + "?", Labels.getLabel("GRUPPI_CANCELLA_GRUPPO"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES) {
                Gruppi gruppox = new Gruppi();
                gruppox.setId(((Gruppi) gruppo).getId());
                new GruppiController().removeGruppo((Gruppi) gruppox);
                new OperazioniController().nuovaOperazione(Labels.getLabel("GRUPPI_CANCELLA_GRUPPO") + ": " + ((Gruppi) gruppo).getId() + "-" + ((Gruppi) gruppo).getDescrizione(), "gruppi");
                ListModelList lml = (ListModelList) boxGruppi.getListModel();
                lml.remove(nIndex);
                boxGruppi.setSelectedIndex(nIndex - 1);
            }
        }
    }

    public void onClick$buttonCercaGruppo(Event event) {
        cercaGruppo(cercaGruppo.getValue());
    }

    void cercaGruppo(String cerca) {
        if (cerca.isEmpty()) {
            finestraGruppi.setTitle(Labels.getLabel("GRUPPI_TITOLO_FINESTRA"));
        } else {
            finestraGruppi.setTitle(Labels.getLabel("GRUPPI_TITOLO_FINESTRA") + " - " + Labels.getLabel("VOCI_FILTRO_RICERCA") + ": " + cerca);
        }
        this.boxGruppi.clearSelection();
        this.tutti_i_gruppi = new GruppiController().getListGruppi(cerca);
        this.listModelList.clear();
        this.listModelList.addAll(this.tutti_i_gruppi);
        this.boxGruppi.renderAll();
    }

    public void onClick$buttonStampa() {
    }

    void esportaExcel() throws IOException {
        new sisi.EsportaExcel().EsportaExcel2(boxGruppi, "gruppi.xls");
    }

    public class GruppiItemRenderer implements ListitemRenderer {

        public void render(Listitem li, Object data) {
            new Listcell("" + ((Gruppi) data).getId()).setParent(li);
            new Listcell(((Gruppi) data).getDescrizione()).setParent(li);
            li.setAttribute("rigaGruppo", data);
            ComponentsCtrl.applyForward(li, "onDoubleClick=onDoubleClicked");
        }
    }
}
