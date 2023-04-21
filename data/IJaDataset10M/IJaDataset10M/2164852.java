package sisi.articoli;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;
import sisi.CfgGlobalController;
import sisi.EsportaExcel;
import sisi.General;
import sisi.artimg.Artimg;
import sisi.artimg.ArtimgController;
import sisi.depositi.Depositi;
import sisi.depositi.DepositiController;
import sisi.giacenze.Giacenze;
import sisi.giacenze.GiacenzeController;
import sisi.listini.Listini;
import sisi.operazioni.*;
import sisi.tipilistini.Tipilistini;
import sisi.tipilistini.TipilistiniController;
import sisi.users.CfgUtentiController;

@SuppressWarnings("serial")
public class ArticoliWindow extends Window implements org.zkoss.zk.ui.ext.AfterCompose {

    private Window finestraArticoli;

    private Textbox cercaArt;

    private Listbox boxArticoli;

    @SuppressWarnings("unchecked")
    public List tutti_gli_articoli;

    private Object ArtItem;

    private boolean lNuovo;

    private ListModelList listModelList;

    private final String PAG_ARTICOLI = "pagArticoli";

    private int quantitaArticoli = 0;

    private Toolbarbutton buttonSeleziona, buttonNuovo, buttonModifica, buttonCancella, buttonStampa, buttonStorico, buttonExcel;

    private String nomeUtente;

    private Listheader lhListino, lhGiacenza;

    private Listhead headArticoli;

    private String listinoVisualizzato, giacenzaVisualizzata, cerca = "";

    private boolean lDalMenu;

    private String theme = "";

    private List<?> dirittiUtente;

    @SuppressWarnings("unchecked")
    public void afterCompose() {
        theme = General.getTheme();
        Map args = Executions.getCurrent().getArg();
        String tastoSeleziona = (String) args.get("tastoSeleziona");
        tastoSeleziona = (tastoSeleziona == null || tastoSeleziona.isEmpty() ? "N" : tastoSeleziona);
        boolean lTastoSeleziona = tastoSeleziona.equals("S");
        String inserimento = (String) args.get("inserimento");
        boolean lInserimento = (inserimento == null);
        String modifica = (String) args.get("modifica");
        boolean lModifica = (modifica == null);
        String cancellazione = (String) args.get("cancellazione");
        boolean lCancellazione = (cancellazione == null);
        String dalmenu = (String) args.get("dalmenu");
        lDalMenu = (dalmenu == null || !dalmenu.equalsIgnoreCase("S") ? false : true);
        Components.wireVariables(this, this);
        if (!lTastoSeleziona) {
            buttonSeleziona.setVisible(false);
        }
        buttonNuovo.setDisabled(!lInserimento);
        buttonModifica.setDisabled(!lModifica);
        buttonCancella.setDisabled(!lCancellazione);
        finestraArticoli.setTitle("Tabella Articoli");
        listModelList = new ListModelList();
        quantitaArticoli = new ArticoliController().getListCountArticoli();
        listinoVisualizzato = new CfgGlobalController().getCfgGlobal().getDefaultListino();
        if (listinoVisualizzato == null || listinoVisualizzato.isEmpty()) {
            lhListino.setVisible(false);
        } else {
            lhListino.setVisible(true);
            Tipilistini tipoListino = new sisi.tipilistini.TipilistiniController().getListinoXCodice(listinoVisualizzato, false);
            if (tipoListino.getCodlis() != null && !tipoListino.getCodlis().isEmpty()) {
                lhListino.setLabel("Listino: " + tipoListino.getTldes());
            }
        }
        giacenzaVisualizzata = new CfgGlobalController().getCfgGlobal().getDefaultDeposito();
        if (giacenzaVisualizzata == null || giacenzaVisualizzata.isEmpty()) {
            lhGiacenza.setVisible(false);
        } else {
            lhGiacenza.setVisible(true);
            Depositi dep = new sisi.depositi.DepositiController().getDepositoXCodice(giacenzaVisualizzata, false);
            if (dep.getCoddep() != null && !dep.getCoddep().isEmpty()) {
                lhGiacenza.setLabel("Giacenza: " + dep.getDpdes());
            }
        }
        Paging pag = (Paging) getFellow(PAG_ARTICOLI);
        pag.setTotalSize(quantitaArticoli);
        Components.addForwards(this, this);
        headArticoli.addEventListener("onColSize", new EventListener() {

            public void onEvent(Event event) {
                new General().utenteSalvaLunghezzaColonne((Listbox) headArticoli.getListbox(), nomeUtente, "LB-ARTICOLI");
            }
        });
        pag.addEventListener("onPaging", new EventListener() {

            public void onEvent(Event event) {
                PagingEvent pe = (PagingEvent) event;
                Paging pag = (Paging) getFellow(PAG_ARTICOLI);
                final int PAGE_SIZE = pag.getPageSize();
                int pgno = pe.getActivePage();
                int ofs = pgno * PAGE_SIZE;
                redraw(ofs, PAGE_SIZE);
            }
        });
        finestraArticoli.addEventListener("onClientInfo", new EventListener() {

            public void onEvent(Event event) {
                int heightFin = Integer.valueOf(finestraArticoli.getHeight().replace("px", "")).intValue();
                int pageSize;
                if (theme.equalsIgnoreCase("classicblue")) {
                    pageSize = Math.max((heightFin - 160) / 17, 10);
                } else {
                    pageSize = Math.max((heightFin - 160) / 28, 10);
                }
                boxArticoli.setRows(pageSize);
                Paging pag = (Paging) getFellow(PAG_ARTICOLI);
                pag.setPageSize(pageSize);
                int pgno = pag.getActivePage();
                int ofs = pgno * pageSize;
                redraw(ofs, pageSize);
            }
        });
        finestraArticoli.addEventListener("onSize", new EventListener() {

            public void onEvent(Event event) {
                int heightFin = Integer.valueOf(finestraArticoli.getHeight().replace("px", "")).intValue();
                int pageSize;
                if (theme.equalsIgnoreCase("classicblue")) {
                    pageSize = Math.max((heightFin - 160) / 17, 10);
                } else {
                    pageSize = Math.max((heightFin - 160) / 28, 10);
                }
                boxArticoli.setRows(pageSize);
                Paging pag = (Paging) getFellow(PAG_ARTICOLI);
                pag.setPageSize(pageSize);
                int pgno = pag.getActivePage();
                int ofs = pgno * pageSize;
                redraw(ofs, pageSize);
            }
        });
        finestraArticoli.addEventListener("onMaximize", new EventListener() {

            public void onEvent(Event event) {
                int heightFin = Integer.valueOf(finestraArticoli.getHeight().replace("px", "")).intValue();
                int pageSize;
                if (theme.equalsIgnoreCase("classicblue")) {
                    pageSize = Math.max((heightFin - 160) / 17, 10);
                } else {
                    pageSize = Math.max((heightFin - 160) / 28, 10);
                }
                boxArticoli.setRows(pageSize);
                Paging pag = (Paging) getFellow(PAG_ARTICOLI);
                pag.setPageSize(pageSize);
                int pgno = pag.getActivePage();
                int ofs = pgno * pageSize;
                redraw(ofs, pageSize);
            }
        });
        if (quantitaArticoli < 1000) {
            cercaArt.addEventListener("onChanging", new EventListener() {

                public void onEvent(Event event) {
                    final InputEvent evt = (InputEvent) event;
                    String valore = evt.getValue();
                    cerca = valore;
                    cercaArt(cerca);
                    cercaArt.setFocus(true);
                }
            });
        } else {
            cercaArt.addEventListener("onOK", new EventListener() {

                public void onEvent(Event event) {
                    String valore = cercaArt.getValue();
                    cerca = valore.trim();
                    cercaArt(cerca);
                    cercaArt.setFocus(true);
                }
            });
        }
        if (lDalMenu) {
            finestraArticoli.setMaximized(true);
        }
        nomeUtente = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("User");
        dirittiUtente = new CfgUtentiController().getListDirittiUtente(nomeUtente);
        if (dirittiUtente.contains("010201")) {
            buttonNuovo.setDisabled(true);
        }
        if (dirittiUtente.contains("010202")) {
            buttonModifica.setDisabled(true);
        }
        if (dirittiUtente.contains("010203")) {
            buttonCancella.setDisabled(true);
        }
        if (dirittiUtente.contains("010204")) {
            buttonStampa.setDisabled(true);
        }
        if (dirittiUtente.contains("010205")) {
            buttonStorico.setDisabled(true);
        }
        if (dirittiUtente.contains("010206")) {
            buttonExcel.setDisabled(true);
        }
        new General().utenteImpostoLunghezzaColonne(nomeUtente, "LB-ARTICOLI", headArticoli);
    }

    @SuppressWarnings("unchecked")
    private void redraw(int firstResult, int maxResults) {
        Listbox lst = boxArticoli;
        lst.getItems().clear();
        int quanti = Math.min(quantitaArticoli, maxResults);
        List<Articoli> tutti_gli_articoli = new ArticoliController().getListArticoli2(cerca, firstResult, quanti);
        listModelList = new ListModelList();
        listModelList.addAll(tutti_gli_articoli);
        boxArticoli.setModel(listModelList);
        boxArticoli.setItemRenderer(new RenderBrowseArticoli());
    }

    public void onClick$buttonModifica(Event event) {
        modificaArt();
    }

    @SuppressWarnings("unchecked")
    public void modificaArt() {
        if (dirittiUtente.contains("010202")) {
            return;
        }
        try {
            int nIndex = boxArticoli.getSelectedIndex();
            if (nIndex == -1) {
                Messagebox.show("Selezionare una riga da Modificare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                lNuovo = false;
                HashMap map = new HashMap();
                map.put("boxArticoli", boxArticoli);
                Window finestra3 = (Window) Executions.createComponents("/editArticoli.zul", null, map);
                finestra3.doModal();
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void onClick$buttonAggiungiArticoli(Event event) {
        Clients.showBusy("Attendere, inserimento articoli...", true);
        int quanti = 100000;
        int i2 = new ArticoliController().getListCountArticoli();
        String ipertesto = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." + " Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley" + " of type and scrambled it to make a type specimen book.\n" + " It has survived not only five centuries, but also the leap into electronic typesetting, " + "remaining essentially unchanged. " + "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages," + " and more recently with desktop publishing software like Aldus PageMaker " + "including versions of Lorem Ipsum.\n" + "It is a long established fact that a reader will be distracted by the readable content " + "of a page when looking at its layout. The point of using Lorem Ipsum is that it" + " has a more-or-less normal distribution of letters, as opposed to using \'Content here, content here\'," + " making it look like readable English. Many desktop publishing packages and web page editors now use" + " Lorem Ipsum as their default model text, and a search for \'lorem ipsum\' will uncover many web sites" + " still in their infancy. Various versions have evolved over the years, sometimes by accident," + " sometimes on purpose (injected humour and the like). \n";
        ipertesto = ipertesto + ipertesto + ipertesto;
        for (int i = 1; i < quanti; i++) {
            if (((i + i2) % 1000) == 0) {
                System.out.println("i+i2: " + (i + i2));
            }
            Articoli art = new sisi.articoli.Articoli();
            art.setCodart("Art." + (i2 + i));
            art.setArdes("Articolo di prova inserito in automatico..." + (i2 + i));
            art.setCodali("IV20");
            art.setIpertesto(ipertesto);
            new ArticoliController().addArticolo((Articoli) art);
        }
        Clients.showBusy(null, false);
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonNuovo(Event event) {
        try {
            lNuovo = true;
            HashMap map = new HashMap();
            map.put("lNuovo", lNuovo);
            map.put("boxArticoli", boxArticoli);
            Window finestra3 = (Window) Executions.createComponents("/editArticoli.zul", null, map);
            finestra3.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClick$buttonCancella(Event event) {
        delete();
    }

    void delete() {
        int nIndex = boxArticoli.getSelectedIndex();
        if (nIndex == -1) {
            try {
                Messagebox.show("Selezionare una riga da Cancellare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        } else {
            ArtItem = boxArticoli.getSelectedItem().getAttribute("rigaArticoli");
            int nOpzione = 0;
            try {
                nOpzione = Messagebox.show("Conferma Cancellazione Articolo: " + ((Articoli) ArtItem).getArdes() + "?", "Cancella Articolo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (nOpzione != 1) {
                return;
            }
            int numeroMovimentiArticoli = new ArticoliController().getArticoliStoricoCount(((Articoli) ArtItem).getCodart());
            if (numeroMovimentiArticoli > 0) {
                new General().MsgBox("Articolo Movimentato", "Errore Cancellazione");
                return;
            }
            new ArticoliController().removeArticolo((Articoli) ArtItem);
            nIndex = boxArticoli.getSelectedIndex();
            ListModelList lml = (ListModelList) boxArticoli.getListModel();
            lml.remove(nIndex);
            boxArticoli.setSelectedIndex(nIndex - 1);
            new OperazioniController().nuovaOperazione("Cancella Articolo: " + ((Articoli) ArtItem).getCodart() + "-" + ((Articoli) ArtItem).getArdes(), "articoli");
        }
    }

    public void onClick$buttonCercaArt(Event event) {
        cerca = cercaArt.getValue();
        cercaArt(cercaArt.getValue());
    }

    void cercaArt(String cerca) {
        if (cerca.length() != 0 && cerca.length() < 3) {
            return;
        }
        if (cerca.isEmpty()) {
            finestraArticoli.setTitle("Tabella Articoli");
        } else {
            finestraArticoli.setTitle("Tabella Articoli - filtro ricerca: " + cerca);
        }
        this.boxArticoli.clearSelection();
        quantitaArticoli = new ArticoliController().getCountArticoli(cerca);
        Paging pag = (Paging) getFellow(PAG_ARTICOLI);
        pag.setTotalSize(quantitaArticoli);
        final int PAGE_SIZE = pag.getPageSize();
        redraw(0, PAGE_SIZE);
    }

    public void onDoubleClicked(Event event) throws Exception {
        if (buttonSeleziona.isVisible()) {
            selezionaArt();
        } else {
            modificaArt();
        }
    }

    public void onClick$buttonSeleziona(Event event) {
        selezionaArt();
    }

    public void selezionaArt() {
        if (boxArticoli.getSelectedIndex() >= 0 && buttonSeleziona.isVisible()) {
            Object articoli = boxArticoli.getSelectedItem().getAttribute("rigaArticoli");
            String codiceSelezionato = ((sisi.articoli.Articoli) articoli).getCodart();
            Sessions.getCurrent().setAttribute("codiceArticoliSelezionato", codiceSelezionato);
            finestraArticoli.detach();
        }
    }

    public void onClick$buttonStampa() {
        Articoli[] ar = new ArticoliController().getArticoli();
        sisi.articoli.ArticoliDS ds = new sisi.articoli.ArticoliDS(ar);
        (Sessions.getCurrent()).setAttribute("nomefile", "reports/articoli.jasper");
        (Sessions.getCurrent()).setAttribute("datasource", ds);
        (Sessions.getCurrent()).setAttribute("tipostampa", "pdf");
        Executions.getCurrent().sendRedirect("/stampaReport.zul", "_blank");
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonStorico(Event event) {
        try {
            int nIndex = boxArticoli.getSelectedIndex();
            if (nIndex == -1) {
                Messagebox.show("Selezionare un Articolo da visualizzare lo Storico", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                ArtItem = boxArticoli.getSelectedItem().getAttribute("rigaArticoli");
                HashMap map = new HashMap();
                map.put("codart", ((Articoli) ArtItem).getCodart());
                Window finestra3 = (Window) Executions.createComponents("/articoli-storico.zul", null, map);
                finestra3.doModal();
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonExcel(Event event) throws IOException {
        String listinoDefault = new CfgGlobalController().getCfgGlobal().getDefaultListino();
        String magazzinoDefault = new CfgGlobalController().getCfgGlobal().getDefaultDeposito();
        Tipilistini listino = new TipilistiniController().getListinoXCodice(listinoDefault, false);
        Depositi dep = new DepositiController().getDepositoXCodice(magazzinoDefault, false);
        listino.setTldes(listino.getCodlis() != null ? listino.getTldes() : "");
        dep.setDpdes(dep.getCoddep() != null && dep.getCoddep().equals(magazzinoDefault) ? dep.getDpdes() : "");
        EsportaExcel oExcel = new sisi.EsportaExcel();
        oExcel.create("SISI");
        String[] intestazioni = { "Codice", "Descrizione", "Categoria", "Costo Ultimo", "Listino: " + listino.getTldes(), "Giacenza: " + dep.getDpdes(), "Scorta Minima" };
        int[] lunghezzaColonne = { 3000, 12000, 12000, 3000, 3000, 3000, 3000 };
        oExcel.intestazione(intestazioni);
        int quanti = quantitaArticoli;
        int quantiXPagina = 20;
        int pagine = (quanti / quantiXPagina) + 1;
        for (int i = 0; i < pagine; i++) {
            quantiXPagina = Math.min(quantiXPagina, quantitaArticoli);
            int inizio = i * quantiXPagina;
            int fine = quantiXPagina;
            if (i == pagine - 1) {
                int ultimaPagina = quantitaArticoli - ((pagine - 1) * quantiXPagina);
                quantiXPagina = Math.min(quantiXPagina, ultimaPagina);
                fine = quantiXPagina;
            }
            List lista = new ArticoliController().getListArticoli2(cercaArt.getValue(), inizio, fine);
            for (Object object : lista) {
                Articoli art = (Articoli) object;
                String cat = (art.getCodcat() == null || art.getCodcat().isEmpty() || art.getCateg() == null ? "" : art.getCodcat() + " - " + art.getCateg().getCades());
                BigDecimal costoUltimo = art.getArcostoult();
                costoUltimo = (costoUltimo == null ? BigDecimal.ZERO : costoUltimo);
                Collection<Listini> listiniCollection = art.getListiniart();
                BigDecimal prezzo = BigDecimal.ZERO;
                for (Iterator iterator = listiniCollection.iterator(); iterator.hasNext(); ) {
                    Listini listini = (Listini) iterator.next();
                    if (listini.getCodlis().equalsIgnoreCase(listinoVisualizzato)) {
                        prezzo = listini.getLiprezzo();
                        break;
                    }
                }
                Giacenze gia = new GiacenzeController().getGiacenzeValore(art.getCodart(), dep.getCoddep());
                BigDecimal scorta = (art.getScorta_minima() == null ? BigDecimal.ZERO : art.getScorta_minima());
                BigDecimal giacInMag = (gia.getGiacenza() == null ? BigDecimal.ZERO : gia.getGiacenza());
                Object[] dettaglio = { art.getCodart(), art.getArdes(), cat, costoUltimo, prezzo, giacInMag, scorta };
                oExcel.dettaglio(dettaglio);
            }
        }
        oExcel.lenghtCols(lunghezzaColonne);
        String nomeFile = new sisi.General().percorsoFincati() + "/" + "articoli.xls";
        oExcel.saveFile(nomeFile);
    }

    public class RenderBrowseArticoli extends GenericForwardComposer implements ListitemRenderer {

        /**
 * 
 */
        private static final long serialVersionUID = 1L;

        public RenderBrowseArticoli() {
        }

        public String CategoriaString(Articoli art) {
            String cString = "";
            try {
                if (art.getCateg() != null) {
                    cString = art.getCodcat() + " - " + art.getCateg().getCades();
                }
            } finally {
            }
            return cString;
        }

        @SuppressWarnings("unchecked")
        public void render(org.zkoss.zul.Listitem listItem, Object data) throws Exception {
            Articoli art = (Articoli) data;
            Listcell ca = new Listcell();
            ca.setLabel(art.getCodart());
            ca.setParent(listItem);
            Listcell des = new Listcell();
            des.setLabel(art.getArdes());
            des.setParent(listItem);
            new Listcell(CategoriaString(art)).setParent(listItem);
            if (lhListino.isVisible()) {
                Collection<Listini> listiniCollection = art.getListiniart();
                String prezzo = "";
                for (Iterator iterator = listiniCollection.iterator(); iterator.hasNext(); ) {
                    Listini listini = (Listini) iterator.next();
                    if (listini.getCodlis().equalsIgnoreCase(listinoVisualizzato)) {
                        prezzo = sisi.General.decimalFormat("###,##0.00", listini.getLiprezzo());
                        break;
                    }
                }
                new Listcell(prezzo).setParent(listItem);
            }
            if (lhGiacenza.isVisible()) {
                String qta = "";
                Collection<Giacenze> giacenzeCollection = art.getGiacenzeart();
                for (Iterator iterator = giacenzeCollection.iterator(); iterator.hasNext(); ) {
                    Giacenze giacenze = (Giacenze) iterator.next();
                    if (giacenze.getCoddep().equalsIgnoreCase(giacenzaVisualizzata)) {
                        qta = sisi.General.decimalFormat("###,##0.00", giacenze.getGiacenza());
                        break;
                    }
                }
                new Listcell(qta).setParent(listItem);
            }
            listItem.setAttribute("rigaArticoli", data);
            boolean visualizzaTooltipImg = false;
            if (visualizzaTooltipImg) {
                Artimg[] artImg;
                String nomeImmagineArticolo = "";
                AImage img = null;
                boolean immagineAttiva = false;
                artImg = new ArtimgController().getArtimg(art.getCodart());
                if (artImg != null && artImg.length >= 1) {
                    nomeImmagineArticolo = artImg[0].getPercorso();
                } else {
                    nomeImmagineArticolo = "";
                }
                if (!nomeImmagineArticolo.isEmpty() && artImg[0].getImmagine() != null) {
                    try {
                        img = new AImage(nomeImmagineArticolo, artImg[0].getImmagine());
                        immagineAttiva = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (immagineAttiva) {
                    Popup popup = new Popup();
                    Vbox vbox = new Vbox();
                    Label lbl = new Label(art.getCodart().trim());
                    lbl.setStyle("font-weight:bold;");
                    Label lbl2 = new Label(art.getArdes());
                    Image imgTT = new Image();
                    imgTT.setContent(img);
                    int nWidth = img.getWidth();
                    int nHeight = img.getHeight();
                    int nMax = 300;
                    if (nHeight > nMax) {
                        nWidth = nWidth * nMax / nHeight;
                        nHeight = nMax;
                    }
                    if (nWidth > nMax) {
                        nHeight = nHeight * nMax / nWidth;
                        nWidth = nMax;
                    }
                    imgTT.setWidth("" + nWidth + "px");
                    imgTT.setHeight("" + nHeight + "px");
                    vbox.appendChild(lbl);
                    vbox.appendChild(lbl2);
                    vbox.appendChild(imgTT);
                    popup.appendChild(vbox);
                    ca.setTooltip(popup);
                    popup.setParent(ca);
                }
            }
            ComponentsCtrl.applyForward(listItem, "onDoubleClick=onDoubleClicked");
        }
    }
}
