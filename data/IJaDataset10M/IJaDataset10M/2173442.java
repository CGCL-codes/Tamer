package sisi.articoli;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkforge.ckez.CKeditor;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Button;
import org.zkoss.zul.api.Listitem;
import sisi.CfgGlobal;
import sisi.CfgGlobalController;
import sisi.EditVarie;
import sisi.General;
import sisi.articoli.Articoli;
import sisi.articoli.ArticoliController;
import sisi.giacenze.Giacenze;
import sisi.nominativi.Nominativi;
import sisi.nominativi.NominativiController;
import sisi.operazioni.OperazioniController;
import sisi.taglie.Tagliacolore;
import sisi.taglie.TagliacoloreController;
import sisi.tipilistini.Tipilistini;
import sisi.tipilistini.TipilistiniController;
import sisi.listini.Listini;
import sisi.listini.ListiniController;
import sisi.marche.*;
import sisi.aliquote.*;
import sisi.categ.Categ;
import sisi.categ.CategController;
import sisi.codicealt.*;
import sisi.depositi.Depositi;
import sisi.artimg.*;

@SuppressWarnings("serial")
public class EditArticoli extends Window implements org.zkoss.zk.ui.ext.AfterCompose {

    private Window finestra3;

    private Textbox codart, ardes, arunmz, arpos, tbCategoria, descCategoria, tbMarca, descMarca, tbAliquota, descAliquota, meta_descrizione, pre_descrizione, tbFornitore, descFornitore, tbTipologia, tbAssociato;

    private Decimalbox arcostoult, arpeso, dbScortaMinima;

    private Intbox ibReparto, ibPuntiInPiu;

    private Datebox datains;

    private Checkbox arweb, cbMovimentaMagazzino, cbMovimentaPunti;

    private Listhead headEditArtListini, headEditArtCodalt, headEditArtGiacenze;

    private Listbox boxArticoli, boxListini, boxGiacenze, boxCodalt, boxTaglie, boxColori;

    private Button btnSalvaModifiche, btnNuovo, btnUploadImg, btnCancImg;

    private Object ArtItem;

    private boolean lNuovo;

    private ListModelList listModelList;

    protected Map<String, Object> args;

    private Collection<Tagliacolore> tagliacoloreart;

    private Collection<Listini> listiniart;

    private Collection<Giacenze> giacenzeart;

    private Collection<Codalt> codicialterna;

    private int itemSelezionato;

    private Articoli newArt;

    private CKeditor ipertesto;

    private Boolean lGestisceTagliacolore, immagineAttiva, lNumAutoArt;

    private Image immagineArt;

    private Artimg[] artImg;

    private String nomeImmagineArticolo, numAutoArt;

    private String nomeUtente;

    private String versione;

    private Tab tabTagliacolore;

    private String codIvaDefault, taglia_o_variante;

    private Caption capTaglie, capColori;

    private Listheader lhTaglie, lhColori;

    private BigDecimal costoPrecedente;

    public void onCreate$finestra3(Event event) {
        btnUploadImg.setDisabled(immagineAttiva ? true : false);
        btnCancImg.setDisabled(immagineAttiva ? false : true);
    }

    @SuppressWarnings("unchecked")
    public void afterCompose() {
        immagineAttiva = false;
        Components.wireVariables(this, this);
        nomeImmagineArticolo = "";
        Map args = Executions.getCurrent().getArg();
        boxArticoli = (Listbox) args.get("boxArticoli");
        if (args.containsKey("lNuovo")) {
            lNuovo = true;
        } else {
            lNuovo = false;
        }
        lGestisceTagliacolore = new CfgGlobalController().getCfgGlobal().getTagliacolore();
        lGestisceTagliacolore = (lGestisceTagliacolore != null ? lGestisceTagliacolore : false);
        tabTagliacolore.setVisible(lGestisceTagliacolore);
        lNumAutoArt = new CfgGlobalController().getCfgGlobal().getArtNumprogressiva();
        numAutoArt = new CfgGlobalController().getCfgGlobal().getArtUltcodice();
        taglia_o_variante = new CfgGlobalController().getCfgGlobal().getVisTagliaVariante();
        if (lGestisceTagliacolore) {
            if (taglia_o_variante.equals("2")) {
                tabTagliacolore.setLabel("Varianti");
                capTaglie.setLabel(" Varianti ");
                capColori.setLabel(" Opzioni Varianti ");
                lhTaglie.setLabel("Varianti");
                lhColori.setLabel("Opzioni Varianti");
            } else {
            }
        }
        if (!lNuovo) {
            ArtItem = boxArticoli.getSelectedItem().getAttribute("rigaArticoli");
            itemSelezionato = boxArticoli.getSelectedIndex();
            Articoli editArt2 = new ArticoliController().refreshArticolo((Articoli) ArtItem);
            ArtItem = editArt2;
            btnNuovo.setVisible(false);
            codart.setValue(((Articoli) ArtItem).getCodart());
            codart.setReadonly(true);
            ardes.setValue(((Articoli) ArtItem).getArdes());
            arunmz.setValue(((Articoli) ArtItem).getArunmz());
            arpeso.setValue(((Articoli) ArtItem).getArpeso());
            arpos.setValue(((Articoli) ArtItem).getArpos());
            ipertesto.setValue(((Articoli) ArtItem).getIpertesto());
            finestra3.setTitle(" - Modifica Articolo - " + ardes.getValue());
            Date dateUtil = (java.util.Date) (((Articoli) ArtItem).getDatains());
            if (dateUtil != null) {
                datains.setValue(dateUtil);
            }
            tbCategoria.setValue(((Articoli) ArtItem).getCodcat());
            modificaCategoria(false);
            tbMarca.setValue(((Articoli) ArtItem).getCodmar());
            modificaMarca(false);
            tbAliquota.setValue(((Articoli) ArtItem).getCodali());
            modificaAliquota(false);
            tbFornitore.setValue(((Articoli) ArtItem).getFornitore());
            modificaFornitore(false);
            arcostoult.setValue(((Articoli) ArtItem).getArcostoult());
            meta_descrizione.setValue(((Articoli) ArtItem).getMeta_descrizione());
            pre_descrizione.setValue(((Articoli) ArtItem).getPre_descrizione());
            tbTipologia.setValue(((Articoli) ArtItem).getTipologia());
            tbAssociato.setValue(((Articoli) ArtItem).getAssociato());
            dbScortaMinima.setValue(((Articoli) ArtItem).getScorta_minima());
            ibReparto.setValue(((Articoli) ArtItem).getReparto());
            String articoloalweb = ((Articoli) ArtItem).getArweb();
            if (articoloalweb != null && articoloalweb.equals("S")) {
                arweb.setChecked(true);
            } else {
                arweb.setChecked(false);
            }
            cbMovimentaMagazzino.setChecked(((Articoli) ArtItem).isMovimentamagazzino());
            cbMovimentaPunti.setChecked(((Articoli) ArtItem).isMovimentapunti());
            ibPuntiInPiu.setValue(((Articoli) ArtItem).getPuntiinpiu());
            ardes.setFocus(true);
            if (lGestisceTagliacolore) {
                tagliacoloreart = ((Articoli) ArtItem).getTagliacoloreart();
                List<String> taglie = new ArrayList<String>();
                List<String> colori = new ArrayList<String>();
                for (Iterator iterator = tagliacoloreart.iterator(); iterator.hasNext(); ) {
                    Tagliacolore tag = (Tagliacolore) iterator.next();
                    if (taglie.isEmpty() || !taglie.contains(tag.getCodtag())) {
                        taglie.add(tag.getCodtag());
                    }
                    if (colori.isEmpty() || !colori.contains(tag.getColore())) {
                        colori.add(tag.getColore());
                    }
                }
                listModelList = new ListModelList();
                listModelList.addAll(taglie);
                boxTaglie.setModel(listModelList);
                boxTaglie.setItemRenderer(new RenderRigheArtTagliacolore(2));
                listModelList = new ListModelList();
                listModelList.addAll(colori);
                boxColori.setModel(listModelList);
                boxColori.setItemRenderer(new RenderRigheArtTagliacolore(2));
            }
            listiniart = ((Articoli) ArtItem).getListiniart();
            listModelList = new ListModelList();
            listModelList.addAll(listiniart);
            boxListini.setModel(listModelList);
            boxListini.setItemRenderer(new RenderRigheArtListini());
            giacenzeart = ((Articoli) ArtItem).getGiacenzeart();
            listModelList = new ListModelList();
            listModelList.addAll(giacenzeart);
            boxGiacenze.setModel(listModelList);
            boxGiacenze.setItemRenderer(new RenderRigheArtGiacenze());
            codicialterna = ((Articoli) ArtItem).getCodicialterna();
            listModelList = new ListModelList();
            listModelList.addAll(codicialterna);
            boxCodalt.setModel(listModelList);
            boxCodalt.setItemRenderer(new RenderRigheCodalt());
            artImg = new ArtimgController().getArtimg(codart.getValue());
            if (artImg != null && artImg.length >= 1) {
                nomeImmagineArticolo = artImg[0].getPercorso();
            } else {
                nomeImmagineArticolo = "";
            }
            if (!nomeImmagineArticolo.isEmpty() && artImg.length >= 1 && artImg[0].getImmagine() != null) {
                AImage img = null;
                try {
                    img = new AImage(nomeImmagineArticolo, artImg[0].getImmagine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                immagineArt.setContent(img);
                int nWidth = img.getWidth();
                int nHeight = img.getHeight();
                int nMax = 600;
                if (nHeight > nMax) {
                    nWidth = nWidth * nMax / nHeight;
                    nHeight = nMax;
                }
                if (nWidth > nMax) {
                    nHeight = nHeight * nMax / nWidth;
                    nWidth = nMax;
                }
                immagineArt.setWidth("" + nWidth + "px");
                immagineArt.setHeight("" + nHeight + "px");
                immagineAttiva = true;
                btnUploadImg.setDisabled(true);
                btnCancImg.setDisabled(false);
            }
        } else {
            finestra3.setTitle(" - Nuovo Articolo - ");
            btnSalvaModifiche.setVisible(false);
            newArt = new Articoli();
            listiniart = ((Articoli) newArt).getListiniart();
            listModelList = new ListModelList();
            boxListini.setModel(listModelList);
            boxListini.setItemRenderer(new RenderRigheArtListini());
            listModelList = new ListModelList();
            boxTaglie.setModel(listModelList);
            boxTaglie.setItemRenderer(new RenderRigheArtTagliacolore(2));
            listModelList = new ListModelList();
            boxColori.setModel(listModelList);
            boxColori.setItemRenderer(new RenderRigheArtTagliacolore(2));
            artImg = new Artimg[0];
            nomeImmagineArticolo = "";
            listModelList = new ListModelList();
            boxCodalt.setModel(listModelList);
            boxCodalt.setItemRenderer(new RenderRigheCodalt());
            codIvaDefault = new CfgGlobalController().getCfgGlobal().getDefaultIva();
            tbAliquota.setValue(codIvaDefault);
            modificaAliquota(false);
            datains.setValue(new Date());
            cbMovimentaMagazzino.setChecked(true);
            cbMovimentaPunti.setChecked(true);
            ibPuntiInPiu.setValue(0);
            if (lNumAutoArt) {
                codart.setValue(calcoloNumAutoArt(numAutoArt.trim()));
            }
        }
        if (lGestisceTagliacolore) {
        }
        Components.addForwards(this, this);
        nomeUtente = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("User");
        sisi.users.Users utente = new sisi.users.UsersController().getUserXNom(nomeUtente);
        versione = utente.getVersione();
        versione = (versione == null ? "" : versione);
        costoPrecedente = arcostoult.getValue();
        new General().utenteImpostoLunghezzaColonne(nomeUtente, "EDITARTICOLI-LISTINI", headEditArtListini);
        new General().utenteImpostoLunghezzaColonne(nomeUtente, "EDITARTICOLI-CODALT", headEditArtCodalt);
        new General().utenteImpostoLunghezzaColonne(nomeUtente, "EDITARTICOLI-GIACENZE", headEditArtGiacenze);
        headEditArtListini.addEventListener("onColSize", new EventListener() {

            public void onEvent(Event event) {
                new General().utenteSalvaLunghezzaColonne((Listbox) headEditArtListini.getListbox(), nomeUtente, "EDITARTICOLI-LISTINI");
            }
        });
        headEditArtCodalt.addEventListener("onColSize", new EventListener() {

            public void onEvent(Event event) {
                new General().utenteSalvaLunghezzaColonne((Listbox) headEditArtCodalt.getListbox(), nomeUtente, "EDITARTICOLI-CODALT");
            }
        });
        headEditArtGiacenze.addEventListener("onColSize", new EventListener() {

            public void onEvent(Event event) {
                new General().utenteSalvaLunghezzaColonne((Listbox) headEditArtGiacenze.getListbox(), nomeUtente, "EDITARTICOLI-GIACENZE");
            }
        });
        arcostoult.addEventListener("onChange", new EventListener() {

            public void onEvent(Event event) {
                InputEvent evt = (InputEvent) event;
                String valore = evt.getValue();
                if (valore == null || valore.isEmpty()) {
                    return;
                }
                valore = valore.replace(",", ".");
                BigDecimal valore2 = new BigDecimal(valore);
                HashMap map = new HashMap();
                map.put("costoPrecedente", costoPrecedente);
                map.put("costoAttuale", valore2);
                map.put("codart", codart.getValue());
                Window finestraRigaNota = (Window) Executions.createComponents("/modListini.zul", null, map);
                try {
                    finestraRigaNota.doModal();
                } catch (SuspendNotAllowedException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object box2 = map.get("boxModListini");
                if (box2 != null) {
                    ListModelList lmlListini = (ListModelList) boxListini.getListModel();
                    Listbox boxModListini = (Listbox) box2;
                    ListModelList lml = (ListModelList) boxModListini.getListModel();
                    int i = lml.getSize();
                    for (int j = 0; j < i; j++) {
                        ListiniDaModificare item = (ListiniDaModificare) lml.getElementAt(j);
                        boolean trovato = false;
                        for (int h = 0; h < lmlListini.getSize(); h++) {
                            Object object = lmlListini.get(h);
                            Listini listini = (Listini) object;
                            if (listini.getCodlis().equalsIgnoreCase(item.getCodlis())) {
                                listini.setLiprezzo(item.getPrezzoProposto());
                                Tipilistini tl = new TipilistiniController().getListinoXCodice(listini.getCodlis(), false);
                                for (int k = 0; k < boxListini.getItemCount(); k++) {
                                    org.zkoss.zul.Listitem itemBox = boxListini.getItemAtIndex(k);
                                    List children = itemBox.getChildren();
                                    String lis = ((Listcell) children.get(0)).getLabel();
                                    if (lis != null && !lis.isEmpty()) {
                                        int posizione = lis.indexOf("- " + tl.getTldes().trim());
                                        posizione = (posizione == -1 ? lis.length() + 1 : posizione);
                                        lis = lis.substring(0, posizione - 1);
                                        lis = lis.trim();
                                    }
                                    if (lis.equalsIgnoreCase(listini.getCodlis().trim())) {
                                        ((Listcell) children.get(0)).setLabel(item.getCodlis() + " - " + tl.getTldes());
                                        ((Listcell) children.get(1)).setLabel(sisi.General.decimalFormat("###,##0.00", item.getPrezzoProposto()));
                                        break;
                                    }
                                }
                                trovato = true;
                                break;
                            }
                        }
                        if (!trovato) {
                            Listini listini = new Listini();
                            listini.setCodlis(item.getCodlis());
                            listini.setLiprezzo(item.getPrezzoProposto());
                            lmlListini.add(listini);
                            boxListini.setSelectedIndex(lmlListini.indexOf(listini));
                        }
                    }
                }
                boxListini.invalidate();
            }
        });
    }

    public void onDoubleClicked2(Event arg0) throws Exception {
        new sisi.General().MsgBox("165", "abc");
    }

    public void onDropMove(Event event) throws Exception {
        ForwardEvent fwe = (ForwardEvent) event;
        DropEvent dropEvent = (DropEvent) fwe.getOrigin();
        Listitem draggedItem = (Listitem) dropEvent.getDragged();
        Listitem droppedItem = (Listitem) dropEvent.getTarget();
        move(draggedItem, droppedItem);
    }

    void move(Listitem dragged, Listitem droppedItem) {
        if (dragged instanceof Listitem) {
            if (dragged.getParent().getId().equals("boxTaglie")) {
                dragged.getParent().insertBefore(dragged, droppedItem.getNextSibling());
            } else if (dragged.getParent().getId().equals("boxColori")) {
                dragged.getParent().insertBefore(dragged, droppedItem.getNextSibling());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void gestioneInsalt() {
        EditVarie editVarie = new EditVarie();
        editVarie.azzeraParametri();
        editVarie.setParLabelGet("Codice:");
        editVarie.setParTitoloFinestra("Genera un codice alternativo:");
        editVarie.setParMaxLenght(128);
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
            Codalt inscodalt = new Codalt();
            inscodalt.setCalcodalt(valore);
            inscodalt.setCodart(codart.getValue());
            Codalt tmpcodalt = new CodicialtController().getCodalt(valore);
            boolean lTrovato = false;
            boxCodalt.renderAll();
            for (int i = 0; i < boxCodalt.getItemCount(); i++) {
                Object oCodalt = boxCodalt.getItemAtIndex(i).getAttribute("rigaArticoliCodalt");
                if (((Codalt) oCodalt).getCalcodalt().trim().equals(valore.trim())) {
                    lTrovato = true;
                    break;
                }
            }
            if (lTrovato) {
                new General().MsgBox("Cod. alternativo esistente in questo articolo", "Avviso ");
            } else if (tmpcodalt.getCalcodalt().isEmpty()) {
                ListModelList lml = (ListModelList) boxCodalt.getListModel();
                if (lml.indexOf(inscodalt) == -1) {
                    lml.add(inscodalt);
                    boxCodalt.setSelectedIndex(lml.indexOf(inscodalt));
                } else {
                    lml.set(lml.indexOf(inscodalt), inscodalt);
                }
            } else {
                new General().MsgBox("Cod. alternativo esistente nell'articolo:" + tmpcodalt.getCodart(), "Avviso ");
            }
        }
    }

    public void onClick$buttonNuovoCodalt() {
        gestioneInsalt();
    }

    public void onClick$buttonCancellaCodalt() {
        cancellaCodalt();
    }

    public void onClick$buttonModificaCodalt() {
        modificaCodalt();
    }

    @SuppressWarnings("unchecked")
    public void modificaCodalt() {
        try {
            int nIndex = boxCodalt.getSelectedIndex();
            if (nIndex == -1) {
                Messagebox.show("Selezionare una riga da Modificare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                lNuovo = false;
                Object oCoda = boxCodalt.getSelectedItem().getAttribute("rigaArticoliCodalt");
                EditVarie editVarie = new EditVarie();
                editVarie.azzeraParametri();
                editVarie.setParLabelGet("Codice:");
                editVarie.setParTitoloFinestra("Modifica codice alternativo:");
                editVarie.setParMaxLenght(128);
                editVarie.setParMaxCols(80);
                editVarie.setParValoreGet(((Codalt) oCoda).getCalcodalt());
                HashMap map = new HashMap();
                map.put("editVarie", editVarie);
                Window finestraRigaNota = (Window) Executions.createComponents("/editVarie.zul", null, map);
                finestraRigaNota.doModal();
                String valore = editVarie.getValoreRitorno();
                if (valore != null && !valore.isEmpty()) {
                    if (!valore.equals(((Codalt) oCoda).getCalcodalt())) {
                        Codalt controllocodalt = new CodicialtController().getCodalt(valore);
                        if (controllocodalt.getCalcodalt().isEmpty()) {
                            Codalt modcodalt = (Codalt) oCoda;
                            modcodalt.setCalcodalt(valore);
                            List children = boxCodalt.getSelectedItem().getChildren();
                            ((Listcell) children.get(1)).setLabel(valore);
                            boxCodalt.getSelectedItem().setValue(modcodalt);
                        } else {
                            new General().MsgBox("Cod. alternativo esistente nell'articolo:" + controllocodalt.getCodart(), "Avviso ");
                        }
                    }
                }
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClick$btnCategoria() {
        selCategoria();
    }

    public void onChange$tbCategoria() {
        modificaCategoria(true);
    }

    @SuppressWarnings("unchecked")
    private void selCategoria() {
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = tbCategoria.getValue();
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        Window finestraCategoria = (Window) Executions.createComponents("/categorie.zul", null, map);
        try {
            finestraCategoria.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceCategSelezionato");
        if (codiceSelezionato != null) {
            tbCategoria.setValue(codiceSelezionato);
        }
        modificaCategoria(true);
    }

    private void modificaCategoria(boolean lEcho) {
        Categ ca = new CategController().getCategXCodice(tbCategoria.getValue(), lEcho);
        if (ca.equals(null) || ca.getCodcat().isEmpty()) {
            tbCategoria.setValue(sisi.General.padLeft("", 5));
            descCategoria.setValue(sisi.General.padLeft("", 30));
            tbCategoria.setFocus(true);
        } else {
            String descrizione = ca.getCades();
            descCategoria.setValue(descrizione);
        }
    }

    public void onClick$btnMarca() {
        selMarca();
    }

    public void onChange$tbMarca() {
        modificaMarca(true);
    }

    @SuppressWarnings("unchecked")
    private void selMarca() {
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = tbMarca.getValue();
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        Window finestraMarca = (Window) Executions.createComponents("/marche.zul", null, map);
        try {
            finestraMarca.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceMarcaSelezionato");
        if (codiceSelezionato != null) {
            tbMarca.setValue(codiceSelezionato);
        }
        modificaMarca(true);
    }

    private void modificaMarca(boolean lEcho) {
        Marche ma = new MarcheController().getMarcaXCodice(tbMarca.getValue(), lEcho);
        if (ma.equals(null) || ma.getCodmar().isEmpty()) {
            tbMarca.setValue(sisi.General.padLeft("", 5));
            descMarca.setValue(sisi.General.padLeft("", 30));
            tbMarca.setFocus(true);
        } else {
            String descrizione = ma.getMades();
            descMarca.setValue(descrizione);
        }
    }

    public void onClick$btnAliquota() {
        selAliquota();
    }

    public void onChange$tbAliquota() {
        modificaAliquota(true);
    }

    @SuppressWarnings("unchecked")
    private void selAliquota() {
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = tbAliquota.getValue();
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        Window finestraAliquota = (Window) Executions.createComponents("/aliquote.zul", null, map);
        try {
            finestraAliquota.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceAliquoteSelezionato");
        if (codiceSelezionato != null) {
            tbAliquota.setValue(codiceSelezionato);
        }
        modificaAliquota(true);
    }

    private void modificaAliquota(boolean lEcho) {
        Aliquote al = new AliquoteController().getAliquoteXCodice(tbAliquota.getValue(), lEcho);
        if (al.equals(null) || al.getCodali().isEmpty()) {
            tbAliquota.setValue(sisi.General.padLeft("", 5));
            descAliquota.setValue(sisi.General.padLeft("", 30));
            tbAliquota.setFocus(true);
        } else {
            String descrizione = al.getAlides();
            descAliquota.setValue(descrizione);
        }
    }

    public void onClick$btnFornitore() {
        selFornitore();
    }

    public void onChange$tbFornitore() {
        modificaFornitore(true);
    }

    @SuppressWarnings("unchecked")
    private void selFornitore() {
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = tbFornitore.getValue();
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        map.put("tipo", "F");
        Window finestraAliquota = (Window) Executions.createComponents("/nominativi.zul", null, map);
        try {
            finestraAliquota.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceNominativoSelezionato");
        if (codiceSelezionato != null) {
            tbFornitore.setValue(codiceSelezionato);
        }
        modificaFornitore(true);
    }

    private void modificaFornitore(boolean lEcho) {
        Nominativi nom = new NominativiController().getNominativoXCodice(tbFornitore.getValue(), lEcho);
        if (nom.equals(null) || nom.getCodnom().isEmpty() || !nom.getNomfor().equalsIgnoreCase("S")) {
            if (!nom.equals(null) && !nom.getCodnom().isEmpty() && !nom.getNomfor().equalsIgnoreCase("S")) {
                new General().MsgBox("Selezionare un Fornitore", "Errore");
            }
            tbFornitore.setValue(sisi.General.padLeft("", 5));
            descFornitore.setValue(sisi.General.padLeft("", 30));
            tbFornitore.setFocus(true);
        } else {
            String descrizione = nom.getNomnom();
            descFornitore.setValue(descrizione);
        }
    }

    public void onClick$buttonModificaLis(Event event) {
        modificaListino();
    }

    public void onClick$buttonNuovoLis(Event event) {
        nuovoListino();
    }

    public void onClick$buttonCancellaLis(Event event) {
        cancellaListino();
    }

    @SuppressWarnings("unchecked")
    public void modificaListino() {
        try {
            int nIndex = boxListini.getSelectedIndex();
            if (nIndex == -1) {
                Messagebox.show("Selezionare una riga da Modificare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                lNuovo = false;
                Object oLis = boxListini.getSelectedItem().getAttribute("rigaArticoliListini");
                oLis = new ListiniController().refreshListini(((Listini) oLis));
                HashMap map = new HashMap();
                map.put("boxListini", boxListini);
                map.put("id", "" + ((Listini) oLis).getLiid());
                map.put("lNuovo", lNuovo);
                Window finestraEditArtLis = (Window) Executions.createComponents("/editArtLis.zul", null, map);
                finestraEditArtLis.doModal();
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void nuovoListino() {
        try {
            String codiceSelezionato = selListino();
            if (codiceSelezionato != null && !codiceSelezionato.isEmpty()) {
                boolean lTrovato = false;
                boxListini.renderAll();
                for (int i = 0; i < boxListini.getItemCount(); i++) {
                    Object oLis = boxListini.getItemAtIndex(i).getAttribute("rigaArticoliListini");
                    if (((Listini) oLis).getCodlis().trim().equals(codiceSelezionato.trim())) {
                        lTrovato = true;
                        break;
                    }
                }
                if (!lTrovato) {
                    lNuovo = true;
                    HashMap map = new HashMap();
                    map.put("boxListini", boxListini);
                    map.put("codicelistino", codiceSelezionato);
                    map.put("lNuovo", lNuovo);
                    Window finestraEditArtLis = (Window) Executions.createComponents("/editArtLis.zul", null, map);
                    finestraEditArtLis.doModal();
                } else {
                    new General().MsgBox("Codice listino gi� inserito", "Errore");
                }
            }
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void cancellaListino() {
        int nIndex = boxListini.getSelectedIndex();
        if (nIndex == -1) {
            try {
                Messagebox.show("Selezionare una riga da Cancellare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        } else {
            ListModelList lml = (ListModelList) boxListini.getListModel();
            lml.remove(nIndex);
            boxListini.setSelectedIndex(nIndex - 1);
        }
    }

    void cancellaCodalt() {
        int nIndex = boxCodalt.getSelectedIndex();
        if (nIndex == -1) {
            try {
                Messagebox.show("Selezionare una riga da Cancellare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        } else {
            ListModelList lml = (ListModelList) boxCodalt.getListModel();
            lml.remove(nIndex);
            boxCodalt.setSelectedIndex(nIndex - 1);
        }
    }

    @SuppressWarnings("unchecked")
    public String selListino() {
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = "    ";
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        Window finestraListini = (Window) Executions.createComponents("/tipilistini.zul", null, map);
        try {
            finestraListini.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceTipilistiniSelezionato");
        return codiceSelezionato;
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonNuovoTag(Event event) throws WrongValueException, InterruptedException {
        String tagliaVariante, insTagliaVariante;
        if (taglia_o_variante.equals("2")) {
            tagliaVariante = "Variante";
            insTagliaVariante = "Inserimento Variante";
        } else {
            tagliaVariante = "Taglia";
            insTagliaVariante = "Inserimento Taglia";
        }
        EditVarie editVarie = new EditVarie();
        editVarie.azzeraParametri();
        editVarie.setParLabelGet(tagliaVariante + ": ");
        editVarie.setParTitoloFinestra(insTagliaVariante);
        editVarie.setParMaxLenght(50);
        HashMap map = new HashMap();
        map.put("editVarie", editVarie);
        Window finestraTaglia = (Window) Executions.createComponents("/editVarie.zul", null, map);
        finestraTaglia.doModal();
        String valore = editVarie.getValoreRitorno();
        if (valore != null && !valore.isEmpty()) {
            ListModelList lml = (ListModelList) boxTaglie.getListModel();
            if (lml.indexOf(valore) == -1) {
                lml.add(valore);
                boxTaglie.setSelectedIndex(lml.indexOf(valore));
            } else {
                new General().MsgBox(tagliaVariante + " Esistente!!!", "Errore");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonModificaTag(Event event) throws WrongValueException, InterruptedException {
        int nIndex = boxTaglie.getSelectedIndex();
        if (nIndex == -1) {
            return;
        }
        String tagliaVariante, insTagliaVariante;
        if (taglia_o_variante.equals("2")) {
            tagliaVariante = "Variante";
            insTagliaVariante = "Modifica Variante";
        } else {
            tagliaVariante = "Taglia";
            insTagliaVariante = "Modifica Taglia";
        }
        Object oTag = boxTaglie.getSelectedItem().getAttribute("rigaArticoliTagliacolore");
        String valorePrecedente = (String) oTag;
        EditVarie editVarie = new EditVarie();
        editVarie.azzeraParametri();
        editVarie.setParLabelGet(tagliaVariante + ":");
        editVarie.setParTitoloFinestra(insTagliaVariante);
        editVarie.setParMaxLenght(50);
        editVarie.setParValoreGet(valorePrecedente);
        HashMap map = new HashMap();
        map.put("editVarie", editVarie);
        Window finestraTaglia = (Window) Executions.createComponents("/editVarie.zul", null, map);
        finestraTaglia.doModal();
        String valore = editVarie.getValoreRitorno();
        if (valore != null && !valore.isEmpty()) {
            ListModelList lml = (ListModelList) boxTaglie.getListModel();
            if (lml.indexOf(valore) == -1) {
                lml.set(lml.indexOf(valorePrecedente), valore);
            } else if (valorePrecedente.equals(valore)) {
            } else {
                new General().MsgBox(tagliaVariante + " Esistente!!!", "Errore");
            }
        }
    }

    public void onClick$buttonCancellaTag(Event event) throws WrongValueException, InterruptedException {
        int nIndex = boxTaglie.getSelectedIndex();
        if (nIndex == -1) {
            try {
                Messagebox.show("Selezionare una riga da Cancellare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        } else {
            ListModelList lml = (ListModelList) boxTaglie.getListModel();
            lml.remove(nIndex);
            boxTaglie.setSelectedIndex(nIndex - 1);
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonNuovoCol(Event event) throws WrongValueException, InterruptedException {
        String coloreVariante, insColoreVariante;
        if (taglia_o_variante.equals("2")) {
            coloreVariante = "Opzione Variante";
            insColoreVariante = "Inserimento Opzione Variante";
        } else {
            coloreVariante = "Colore";
            insColoreVariante = "Inserimento Colore";
        }
        EditVarie editVarie = new EditVarie();
        editVarie.azzeraParametri();
        editVarie.setParLabelGet(coloreVariante + ":");
        editVarie.setParTitoloFinestra(insColoreVariante);
        editVarie.setParMaxLenght(50);
        HashMap map = new HashMap();
        map.put("editVarie", editVarie);
        Window finestraTaglia = (Window) Executions.createComponents("/editVarie.zul", null, map);
        finestraTaglia.doModal();
        String valore = editVarie.getValoreRitorno();
        if (valore != null && !valore.isEmpty()) {
            ListModelList lml = (ListModelList) boxColori.getListModel();
            if (lml.indexOf(valore) == -1) {
                lml.add(valore);
                boxColori.setSelectedIndex(lml.indexOf(valore));
            } else {
                new General().MsgBox(coloreVariante + " Esistente!!!", "Errore");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$buttonModificaCol(Event event) throws WrongValueException, InterruptedException {
        int nIndex = boxColori.getSelectedIndex();
        if (nIndex == -1) {
            return;
        }
        String coloreVariante, insColoreVariante;
        if (taglia_o_variante.equals("2")) {
            coloreVariante = "Opzione Variante";
            insColoreVariante = "Modifica Opzione Variante";
        } else {
            coloreVariante = "Colore";
            insColoreVariante = "Modifica Colore";
        }
        Object oTag = boxColori.getSelectedItem().getAttribute("rigaArticoliTagliacolore");
        String valorePrecedente = (String) oTag;
        EditVarie editVarie = new EditVarie();
        editVarie.azzeraParametri();
        editVarie.setParLabelGet(coloreVariante + ":");
        editVarie.setParTitoloFinestra(insColoreVariante);
        editVarie.setParMaxLenght(50);
        editVarie.setParValoreGet(valorePrecedente);
        HashMap map = new HashMap();
        map.put("editVarie", editVarie);
        Window finestraTaglia = (Window) Executions.createComponents("/editVarie.zul", null, map);
        finestraTaglia.doModal();
        String valore = editVarie.getValoreRitorno();
        if (valore != null && !valore.isEmpty()) {
            ListModelList lml = (ListModelList) boxColori.getListModel();
            if (lml.indexOf(valore) == -1) {
                lml.set(lml.indexOf(valorePrecedente), valore);
            } else if (valorePrecedente.equals(valore)) {
            } else {
                new General().MsgBox(coloreVariante + " Esistente!!!", "Errore");
            }
        }
    }

    public void onClick$buttonCancellaCol(Event event) throws WrongValueException, InterruptedException {
        int nIndex = boxColori.getSelectedIndex();
        if (nIndex == -1) {
            try {
                Messagebox.show("Selezionare una riga da Cancellare", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        } else {
            ListModelList lml = (ListModelList) boxColori.getListModel();
            lml.remove(nIndex);
            boxColori.setSelectedIndex(nIndex - 1);
        }
    }

    public void onClick$btnSalvaModifiche(Event event) throws WrongValueException, InterruptedException {
        update();
    }

    public void onClick$btnNuovo(Event event) throws WrongValueException, InterruptedException {
        add();
    }

    public void onClick$btnAnnulla(Event event) {
        finestra3.detach();
    }

    @SuppressWarnings("unchecked")
    public void onClick$btnCorrelati(Event event) throws Exception {
        HashMap map = new HashMap();
        map.put("codart", codart.getValue());
        Window finestraRigaNota = (Window) Executions.createComponents("/correlati.zul", null, map);
        try {
            finestraRigaNota.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$btnArticoloComposto(Event event) throws Exception {
        HashMap map = new HashMap();
        map.put("codart", codart.getValue());
        Window finestraRigaNota = (Window) Executions.createComponents("/composti.zul", null, map);
        try {
            finestraRigaNota.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$btnArticoloAssociato(Event event) throws Exception {
        HashMap map = new HashMap();
        Sessions.getCurrent().setAttribute("codiceArticoliSelezionato", "");
        map.put("tastoSeleziona", "S");
        map.put("inserimento", "N");
        map.put("modifica", "N");
        map.put("cancellazione", "N");
        Window finestra4 = (Window) Executions.createComponents("/articoli.zul", null, map);
        finestra4.doModal();
        Session session = Sessions.getCurrent();
        String codiceSelezionato = (String) session.getAttribute("codiceArticoliSelezionato");
        if (codiceSelezionato != null && !codiceSelezionato.isEmpty()) {
            tbAssociato.setValue(codiceSelezionato);
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$btnAmbiti(Event event) throws Exception {
        HashMap map = new HashMap();
        map.put("codart", codart.getValue());
        Window finestraRigaNota = (Window) Executions.createComponents("/ambitiart.zul", null, map);
        try {
            finestraRigaNota.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void onClick$btnMulticategorie(Event event) throws Exception {
        HashMap map = new HashMap();
        map.put("codart", codart.getValue());
        Window finestraMulticategorie = (Window) Executions.createComponents("/artcateg.zul", null, map);
        try {
            finestraMulticategorie.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClick$btnUploadImg(Event event) throws Exception {
        Sessions.getCurrent().setAttribute("nomeImmagineCaricata", "");
        Integer limiteImmagine = new CfgGlobalController().getCfgGlobal().getLimiteimg();
        Media mediaImg = sisi.UploadFile.caricaImg2Media(limiteImmagine);
        if (mediaImg != null) {
            String nomeImmagine = (String) mediaImg.getName();
            nomeImmagineArticolo = nomeImmagine;
            AImage img = new AImage(mediaImg.getName(), mediaImg.getByteData());
            String nome = sisi.General.ConvertoNomeFile(mediaImg.getName());
            String tipo = mediaImg.getContentType();
            if (nome == null || nome.isEmpty() || tipo == null || tipo.isEmpty()) {
                return;
            }
            immagineArt.setContent(img);
            int nWidth = img.getWidth();
            int nHeight = img.getHeight();
            int nMax = 600;
            if (nHeight > nMax) {
                nWidth = nWidth * nMax / nHeight;
                nHeight = nMax;
            }
            if (nWidth > nMax) {
                nHeight = nHeight * nMax / nWidth;
                nWidth = nMax;
            }
            immagineArt.setWidth("" + nWidth + "px");
            immagineArt.setHeight("" + nHeight + "px");
            btnUploadImg.setDisabled(true);
            btnCancImg.setDisabled(false);
            if (artImg == null || artImg.length == 0) {
                artImg = new Artimg[1];
            }
            artImg[0] = new Artimg();
            artImg[0].setCodart(codart.getValue());
            artImg[0].setPercorso(sisi.General.ConvertoNomeFile(mediaImg.getName()));
            artImg[0].setImmagine(mediaImg.getByteData());
            artImg[0].setContenttype(mediaImg.getContentType());
        }
    }

    public void onClick$btnCancImg(Event event) throws Exception {
        immagineArt.setSrc("");
        btnUploadImg.setDisabled(false);
        btnCancImg.setDisabled(true);
        artImg = new Artimg[0];
    }

    @SuppressWarnings("unchecked")
    void update() throws WrongValueException, InterruptedException {
        int nOpzione = Messagebox.show("Conferma Modifiche: " + ardes.getValue() + "?", "Modifica Articolo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
        if (nOpzione != 1) {
            return;
        }
        Aliquote ali = new AliquoteController().getAliquoteXCodice(tbAliquota.getValue(), false);
        if (ali.getCodali() == null || ali.getCodali().isEmpty()) {
            new General().MsgBox("Aliquota IVA non valida", "Errore");
            return;
        }
        String associato = tbAssociato.getValue();
        if (!associato.isEmpty()) {
            associato = associato.toUpperCase().trim();
            if (associato.trim().equalsIgnoreCase(codart.getValue().trim())) {
                new General().MsgBox("L'articolo non pu� essere associato a se stesso", "Errore");
                return;
            }
            Articoli[] arts0 = new ArticoliController().getArticoli("WHERE TRIM( BOTH FROM UPPER(c.codart) )='" + associato + "'");
            if (arts0.length < 1) {
                new General().MsgBox("Articolo associato: " + associato + " inesistente.", "Errore");
                return;
            } else {
                for (int i = 0; i < arts0.length; i++) {
                    Articoli art = arts0[i];
                    String ass = art.getAssociato();
                    ass = (ass == null ? "" : ass.trim());
                    if (art.getCodart().trim().equalsIgnoreCase(associato) && !ass.isEmpty()) {
                        new General().MsgBox("L'Articolo : " + art.getCodart().trim() + " � gi� associato con altro articolo.(" + ass.trim() + ")", "Errore");
                        return;
                    }
                }
            }
            Articoli[] arts2 = new ArticoliController().getArticoli("WHERE TRIM( BOTH FROM UPPER(c.associato) )='" + codart.getValue().trim().toUpperCase() + "'");
            for (int i = 0; i < arts2.length; i++) {
                Articoli art = arts2[i];
                new General().MsgBox("Articolo: " + codart.getValue().trim() + " gi� associato con l'articolo " + art.getCodart(), "Errore");
                return;
            }
        }
        ((Articoli) ArtItem).setTagliacoloreart(tagliacoloreart);
        if (datains.getValue() != null) {
            String dt = new java.text.SimpleDateFormat("yyyy-MM-dd").format(datains.getValue());
            ((Articoli) ArtItem).setDatains(java.sql.Date.valueOf(dt));
        } else {
            ((Articoli) ArtItem).setDatains((java.sql.Date) datains.getValue());
        }
        ((Articoli) ArtItem).setCodart(codart.getValue());
        ((Articoli) ArtItem).setArdes(ardes.getValue());
        ((Articoli) ArtItem).setCodcat(tbCategoria.getValue());
        ((Articoli) ArtItem).setCodmar(tbMarca.getValue());
        ((Articoli) ArtItem).setArcostoult(arcostoult.getValue());
        ((Articoli) ArtItem).setArpeso(arpeso.getValue());
        ((Articoli) ArtItem).setArunmz(arunmz.getValue());
        ((Articoli) ArtItem).setArpos(arpos.getValue());
        ((Articoli) ArtItem).setCodali(tbAliquota.getValue().toUpperCase());
        ((Articoli) ArtItem).setMeta_descrizione(meta_descrizione.getValue());
        ((Articoli) ArtItem).setPre_descrizione(pre_descrizione.getValue());
        ((Articoli) ArtItem).setFornitore(tbFornitore.getValue());
        ((Articoli) ArtItem).setTipologia(tbTipologia.getValue());
        ((Articoli) ArtItem).setAssociato(associato);
        ((Articoli) ArtItem).setScorta_minima(dbScortaMinima.getValue());
        ((Articoli) ArtItem).setReparto((ibReparto.getValue() != null ? ibReparto.getValue() : 0));
        if (arweb.isChecked()) {
            ((Articoli) ArtItem).setArweb("S");
        } else {
            ((Articoli) ArtItem).setArweb("N");
        }
        ((Articoli) ArtItem).setIpertesto(ipertesto.getValue());
        ((Articoli) ArtItem).setMovimentamagazzino(cbMovimentaMagazzino.isChecked());
        ((Articoli) ArtItem).setMovimentapunti(cbMovimentaPunti.isChecked());
        ((Articoli) ArtItem).setPuntiinpiu(ibPuntiInPiu.getValue() != null ? ibPuntiInPiu.getValue() : 0);
        Articoli articoloModificato = new ArticoliController().updateArticolo((Articoli) ArtItem);
        Tipilistini tipoListino = null;
        String listinoVisualizzato = new CfgGlobalController().getCfgGlobal().getDefaultListino();
        if (listinoVisualizzato == null || listinoVisualizzato.isEmpty()) {
        } else {
            tipoListino = new sisi.tipilistini.TipilistiniController().getListinoXCodice(listinoVisualizzato, false);
            if (tipoListino.getCodlis() != null && !tipoListino.getCodlis().isEmpty()) {
            }
        }
        Depositi dep = null;
        String giacenzaVisualizzata = new CfgGlobalController().getCfgGlobal().getDefaultDeposito();
        if (giacenzaVisualizzata == null || giacenzaVisualizzata.isEmpty()) {
        } else {
            dep = new sisi.depositi.DepositiController().getDepositoXCodice(giacenzaVisualizzata, false);
            if (dep.getCoddep() != null && !dep.getCoddep().isEmpty()) {
            }
        }
        if (tipoListino != null) {
            Collection<Listini> listiniCollection = ((Articoli) ArtItem).getListiniart();
            String prezzo = "";
            for (Iterator iterator = listiniCollection.iterator(); iterator.hasNext(); ) {
                Listini listini = (Listini) iterator.next();
                if (listini.getCodlis().equalsIgnoreCase(listinoVisualizzato)) {
                    prezzo = sisi.General.decimalFormat("###,##0.00", listini.getLiprezzo());
                    break;
                }
            }
        }
        new OperazioniController().nuovaOperazione("Modifica Articolo: " + ((Articoli) ArtItem).getCodart() + "-" + ((Articoli) ArtItem).getArdes(), "articoli");
        Iterator iterator = listiniart.iterator();
        while (iterator.hasNext()) {
            Listini lisDaCanc = (Listini) iterator.next();
            new ListiniController().removeListini(lisDaCanc);
        }
        boxListini.renderAll();
        for (int i = 0; i < boxListini.getItemCount(); i++) {
            Object oLis = boxListini.getItemAtIndex(i).getAttribute("rigaArticoliListini");
            ((Listini) oLis).setCodart(codart.getValue());
            new ListiniController().addListini((Listini) oLis);
        }
        Iterator iterator2 = codicialterna.iterator();
        while (iterator2.hasNext()) {
            Codalt codaltDaCanc = (Codalt) iterator2.next();
            new CodicialtController().removeCodalt(codaltDaCanc);
        }
        boxCodalt.renderAll();
        for (int i = 0; i < boxCodalt.getItemCount(); i++) {
            Object oLis = boxCodalt.getItemAtIndex(i).getAttribute("rigaArticoliCodalt");
            ((Codalt) oLis).setCodart(codart.getValue());
            new CodicialtController().addCodalt((Codalt) oLis);
        }
        new ArtimgController().removeArtimg(codart.getValue());
        for (int i = 0; i < artImg.length; i++) {
            Artimg artimg2 = artImg[i];
            artimg2.setCodart(codart.getValue());
            new ArtimgController().addArtimg(artimg2);
        }
        if (lGestisceTagliacolore) {
            Iterator iterator3 = tagliacoloreart.iterator();
            while (iterator3.hasNext()) {
                Tagliacolore codDaCanc = (Tagliacolore) iterator3.next();
                new TagliacoloreController().removeTagliacolore(codDaCanc);
            }
            boxTaglie.renderAll();
            boxColori.renderAll();
            if (boxTaglie.getItemCount() == 0 && boxColori.getItemCount() > 0) {
                ListModelList lml = (ListModelList) boxTaglie.getListModel();
                lml.add("UNICA");
                boxTaglie.setSelectedIndex(lml.indexOf("UNICA"));
                boxTaglie.renderAll();
            }
            for (int i = 0; i < boxTaglie.getItemCount(); i++) {
                Object oTag = boxTaglie.getItemAtIndex(i).getAttribute("rigaArticoliTagliacolore");
                if (boxColori.getItemCount() == 0) {
                    Tagliacolore newTagcol = new Tagliacolore();
                    newTagcol.setCodart(((Articoli) ArtItem).getCodart());
                    newTagcol.setCodtag((String) oTag);
                    newTagcol.setColore("UNICO");
                    new TagliacoloreController().addTagliacolore(newTagcol);
                } else {
                    for (int x = 0; x < boxColori.getItemCount(); x++) {
                        Object oCol = boxColori.getItemAtIndex(x).getAttribute("rigaArticoliTagliacolore");
                        Tagliacolore newTagcol = new Tagliacolore();
                        newTagcol.setCodart(((Articoli) ArtItem).getCodart());
                        newTagcol.setCodtag((String) oTag);
                        newTagcol.setColore((String) oCol);
                        new TagliacoloreController().addTagliacolore(newTagcol);
                    }
                }
            }
        }
        ListModelList lml = (ListModelList) boxArticoli.getListModel();
        Object oArtItem = boxArticoli.getSelectedItem().getAttribute("rigaArticoli");
        if (lml.indexOf(oArtItem) == -1) {
        } else {
            lml.set(lml.indexOf(oArtItem), ArtItem);
        }
        finestra3.detach();
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

    void add() {
        if (esisteArticolo(codart.getValue(), true)) {
            if (lNumAutoArt) {
                while (esisteArticolo(numAutoArt, false)) {
                    numAutoArt = calcoloNumAutoArt(numAutoArt);
                }
                codart.setValue(numAutoArt);
                new General().MsgBox("Codice Articolo variato a: " + numAutoArt, "Codice Variato");
            } else {
                return;
            }
        }
        if (versione.equalsIgnoreCase("JOGFREE")) {
            int valore = new sisi.articoli.ArticoliController().getCountArticoli(null);
            if (valore > 5) {
                new sisi.General().MsgBox("Limite di 5 articoli raggiunti per la versione " + versione, "LIMITE RAGGIUNTO");
                return;
            }
        } else if (versione.equalsIgnoreCase("JOGMINI")) {
            int valore = new sisi.articoli.ArticoliController().getCountArticoli(null);
            if (valore > 30) {
                new sisi.General().MsgBox("Limite di 30 articoli raggiunti per la versione " + versione, "LIMITE RAGGIUNTO");
                return;
            }
        }
        Aliquote ali = new AliquoteController().getAliquoteXCodice(tbAliquota.getValue(), false);
        if (ali.getCodali() == null || ali.getCodali().isEmpty()) {
            new General().MsgBox("Aliquota IVA non valida", "Errore");
            return;
        }
        codart.setValue(codart.getValue().trim().toUpperCase());
        String associato = tbAssociato.getValue();
        if (!associato.isEmpty()) {
            associato = associato.toUpperCase().trim();
            if (associato.trim().equalsIgnoreCase(codart.getValue().trim())) {
                new General().MsgBox("L'articolo non pu� essere associato a se stesso", "Errore");
                return;
            }
            Articoli[] arts0 = new ArticoliController().getArticoli("WHERE TRIM( BOTH FROM UPPER(c.codart) )='" + associato + "'");
            if (arts0.length < 1) {
                new General().MsgBox("Articolo associato: " + associato + " inesistente.", "Errore");
                return;
            } else {
                for (int i = 0; i < arts0.length; i++) {
                    Articoli art = arts0[i];
                    String ass = art.getAssociato();
                    ass = (ass == null ? "" : ass.trim());
                    if (art.getCodart().trim().equalsIgnoreCase(associato) && !ass.isEmpty()) {
                        new General().MsgBox("L'Articolo : " + art.getCodart().trim() + " � gi� associato con altro articolo.(" + ass.trim() + ")", "Errore");
                        return;
                    }
                }
            }
            Articoli[] arts2 = new ArticoliController().getArticoli("WHERE TRIM( BOTH FROM UPPER(c.associato) )='" + codart.getValue().trim().toUpperCase() + "'");
            for (int i = 0; i < arts2.length; i++) {
                Articoli art = arts2[i];
                new General().MsgBox("Articolo: " + codart.getValue().trim() + " gi� associato con l'articolo " + art.getCodart(), "Errore");
                return;
            }
        }
        if (lNumAutoArt) {
            CfgGlobal cfg_Global = new CfgGlobalController().getCfgGlobal();
            cfg_Global.setArtUltcodice(codart.getValue());
            new CfgGlobalController().updateCfgGlobal(cfg_Global);
        }
        newArt.setCodart(codart.getValue());
        newArt.setArdes(ardes.getValue());
        newArt.setCodcat(tbCategoria.getValue());
        newArt.setCodmar(tbMarca.getValue());
        newArt.setCodali(tbAliquota.getValue().toUpperCase());
        newArt.setArcostoult(arcostoult.getValue());
        newArt.setArpeso(arpeso.getValue());
        newArt.setArunmz(arunmz.getValue());
        newArt.setArpos(arpos.getValue());
        newArt.setMeta_descrizione(meta_descrizione.getValue());
        newArt.setPre_descrizione(pre_descrizione.getValue());
        newArt.setFornitore(tbFornitore.getValue());
        newArt.setTipologia(tbTipologia.getValue());
        newArt.setAssociato(tbAssociato.getValue());
        newArt.setScorta_minima(dbScortaMinima.getValue());
        newArt.setReparto((ibReparto.getValue() != null ? ibReparto.getValue() : 0));
        if (arweb.isChecked()) {
            newArt.setArweb("S");
        } else {
            newArt.setArweb("N");
        }
        newArt.setIpertesto(ipertesto.getValue());
        newArt.setMovimentamagazzino(cbMovimentaMagazzino.isChecked());
        newArt.setMovimentapunti(cbMovimentaPunti.isChecked());
        newArt.setPuntiinpiu(ibPuntiInPiu.getValue());
        if (datains.getValue() != null) {
            String dt = new java.text.SimpleDateFormat("yyyy-MM-dd").format(datains.getValue());
            newArt.setDatains(java.sql.Date.valueOf(dt));
        } else {
        }
        newArt = new ArticoliController().addArticolo((Articoli) newArt);
        boxListini.renderAll();
        for (int i = 0; i < boxListini.getItemCount(); i++) {
            Object oLis = boxListini.getItemAtIndex(i).getAttribute("rigaArticoliListini");
            ((Listini) oLis).setCodart(newArt.getCodart());
            new ListiniController().addListini((Listini) oLis);
        }
        boxCodalt.renderAll();
        for (int i = 0; i < boxCodalt.getItemCount(); i++) {
            Object oLis = boxCodalt.getItemAtIndex(i).getAttribute("rigaArticoliCodalt");
            ((Codalt) oLis).setCodart(newArt.getCodart());
            new CodicialtController().addCodalt((Codalt) oLis);
        }
        for (int i = 0; i < artImg.length; i++) {
            Artimg artimg2 = artImg[i];
            artimg2.setCodart(codart.getValue());
            new ArtimgController().addArtimg(artimg2);
        }
        if (lGestisceTagliacolore) {
            if (boxTaglie.getItemCount() == 0 && boxColori.getItemCount() > 0) {
                ListModelList lml = (ListModelList) boxTaglie.getListModel();
                lml.add("UNICA");
            }
            if (boxTaglie.getItemCount() != 0 && boxColori.getItemCount() == 0) {
                ListModelList lml = (ListModelList) boxColori.getListModel();
                lml.add("UNICO");
            }
            boxTaglie.renderAll();
            boxColori.renderAll();
            for (int i = 0; i < boxTaglie.getItemCount(); i++) {
                Object oTag = boxTaglie.getItemAtIndex(i).getAttribute("rigaArticoliTagliacolore");
                if (boxColori.getItemCount() == 0) {
                    Tagliacolore newTagcol = new Tagliacolore();
                    newTagcol.setCodart(newArt.getCodart());
                    newTagcol.setCodtag((String) oTag);
                    newTagcol.setColore("");
                    new TagliacoloreController().addTagliacolore(newTagcol);
                } else {
                    for (int x = 0; x < boxColori.getItemCount(); x++) {
                        Object oCol = boxColori.getItemAtIndex(x).getAttribute("rigaArticoliTagliacolore");
                        Tagliacolore newTagcol = new Tagliacolore();
                        newTagcol.setCodart(newArt.getCodart());
                        newTagcol.setCodtag((String) oTag);
                        newTagcol.setColore((String) oCol);
                        new TagliacoloreController().addTagliacolore(newTagcol);
                    }
                }
            }
        }
        ListModelList lml = (ListModelList) boxArticoli.getListModel();
        if (lml.indexOf(newArt) == -1) {
            lml.add(newArt);
            boxArticoli.setSelectedIndex(lml.indexOf(newArt));
        } else {
            lml.set(lml.indexOf(newArt), newArt);
        }
        new OperazioniController().nuovaOperazione("Nuovo Articolo: " + ((Articoli) newArt).getCodart() + "-" + ((Articoli) newArt).getArdes(), "articoli");
        finestra3.detach();
    }

    @SuppressWarnings("unchecked")
    public boolean esisteArticolo(String codArtCerca, boolean lEcho) {
        boolean lEsiste = false;
        List tmpList = new ArticoliController().getListArtXCodice(codArtCerca);
        if (tmpList.size() > 0) {
            lEsiste = true;
            if (lEcho) {
                try {
                    Messagebox.show("Codice Articolo [" + codArtCerca.trim() + "] esistente...", "Errore", Messagebox.OK, Messagebox.EXCLAMATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            lEsiste = false;
        }
        return lEsiste;
    }

    @SuppressWarnings("unchecked")
    public void onClick$btnCodiciABarre() {
        HashMap map = new HashMap();
        map.put("boxTaglie", boxTaglie);
        map.put("boxColori", boxColori);
        map.put("codart", codart);
        map.put("codicialterna", codicialterna);
        map.put("boxCodalt", boxCodalt);
        Window finestraCodbarTG = (Window) Executions.createComponents("/codiciabarretg.zul", null, map);
        try {
            finestraCodbarTG.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String calcoloNumAutoArt(String codiceDaCalcolare) {
        String ultimoNum = codiceDaCalcolare;
        String nuovoCod = "";
        if (ultimoNum.isEmpty()) {
            nuovoCod = "1";
        } else {
            String testo = "", numeri = "", ultimoNumero = "";
            for (int i = ultimoNum.length() - 1; i >= 0; i--) {
                String tmpVal = ultimoNum.substring(i, i + 1);
                if ("0123456789".contains(tmpVal)) {
                    numeri += tmpVal;
                } else {
                    break;
                }
            }
            for (int i = numeri.length() - 1; i >= 0; i--) {
                ultimoNumero += numeri.substring(i, i + 1);
            }
            long dUltNum;
            if (ultimoNumero.isEmpty()) {
                ultimoNumero = "0";
                dUltNum = 1;
                testo = codiceDaCalcolare;
            } else {
                dUltNum = Long.valueOf(ultimoNumero) + 1;
                testo = ultimoNum.substring(0, ultimoNum.indexOf(ultimoNumero));
            }
            nuovoCod = testo + General.paddingString("" + dUltNum, (ultimoNumero.length() > ("" + dUltNum).length() ? ultimoNumero.length() : ("" + dUltNum).length()), '0', true);
        }
        return nuovoCod;
    }
}
