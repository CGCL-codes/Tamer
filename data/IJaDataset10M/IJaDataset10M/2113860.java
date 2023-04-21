package sisi.movimenti;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Button;
import org.zkoss.zul.api.Listitem;
import sisi.CfgGlobal;
import sisi.CfgGlobalController;
import sisi.General;
import sisi.articoli.*;
import sisi.categ.*;
import sisi.codicealt.Codalt;
import sisi.codicealt.CodicialtController;
import sisi.nominativi.Nominativi;
import sisi.nominativi.NominativiController;
import sisi.operazioni.OperazioniController;
import sisi.taglie.Tagliacolore;
import sisi.tipilistini.Tipilistini;
import sisi.tipilistini.TipilistiniController;
import sisi.tipimov.Tipimov;
import sisi.marche.*;
import sisi.aliquote.*;
import sisi.listini.*;

@SuppressWarnings({ "serial", "unused" })
public class EditRigaMovimenti extends Window implements org.zkoss.zk.ui.ext.AfterCompose {

    private Window finestraRigaMovimenti, finestraRigaMovimentiNota, finestraArticoli, finestraAliquote;

    private Textbox ardes, alides, filtroArt, filtroCat, filtroAli, arunmz, arpos, nota, tbTaglia, tbColore, tbLotto, tbUnmisura;

    private Bandbox bd, bdArticolo, bdAliquota;

    private Decimalbox quantita, importounitario, unitarioivato, arcostoult, arpeso, sc1, sc2, sc3, sc4, totalenetto, totaleivato;

    private Datebox datains;

    private Checkbox arweb;

    private Listbox boxCorpo, lbArticoli, lbAliquote;

    private Separator spTagliacolore1, spTagliacolore2;

    private Hbox hbTagliacolore;

    private Button btnTagliacolore;

    private BigDecimal costoImponibile;

    private Movcorpo rigaMov;

    private Tipimov tipoMovimento;

    @SuppressWarnings("unchecked")
    private List tutti_gli_articoli, tutte_le_aliquote;

    private Button btnSalvaModifiche, btnNuovo;

    private Object ArtItem;

    private boolean lNuovo, lGestisceTagliacolore, listinoIvato = false, lTrasferimentoInterno;

    private ListModelList listModelList, listModelListAli;

    private String listino;

    private String codicecliente, trasferimentoInterno;

    protected Map<String, Object> args;

    @SuppressWarnings("unchecked")
    public void onCreate$finestraRigaMovimenti(Event event) {
        CreateEvent ce = (CreateEvent) ((ForwardEvent) event).getOrigin();
        args = (Map<String, Object>) ce.getArg();
        boxCorpo = (Listbox) args.get("boxCorpo");
        tipoMovimento = (Tipimov) args.get("tipoMovimento");
        if (args.containsKey("lNuovo")) {
            lNuovo = true;
        } else {
            lNuovo = false;
        }
        listino = (String) args.get("listino");
        codicecliente = (String) args.get("codicecliente");
        trasferimentoInterno = (String) args.get("trasferimento");
        lTrasferimentoInterno = (trasferimentoInterno != null && trasferimentoInterno.equalsIgnoreCase("S") ? true : false);
        listinoIvato = false;
        if (listino != null && !listino.isEmpty()) {
            Tipilistini tl = new TipilistiniController().getListinoXCodice(listino, false);
            if (!tl.getCodlis().isEmpty()) {
                listinoIvato = tl.getTlconiva().equalsIgnoreCase("S");
            }
        }
        lGestisceTagliacolore = new CfgGlobalController().getCfgGlobal().getTagliacolore();
        if (!lNuovo) {
            Object rigaMov2 = boxCorpo.getSelectedItem().getAttribute("rigaCorpoMovimento");
            rigaMov = (Movcorpo) rigaMov2;
            tbTaglia.setValue(rigaMov.getCodtag());
            tbColore.setValue(rigaMov.getColore());
            bdArticolo.setValue(rigaMov.getCodart());
            ardes.setValue(rigaMov.getArdes());
            bdAliquota.setValue(rigaMov.getCodali());
            Aliquote tmpali = new sisi.aliquote.AliquoteController().getAliquoteXCodice(rigaMov.getCodali(), false);
            String tmpDes;
            tmpDes = (tmpali != null ? tmpali.getAlides() : "");
            alides.setValue(tmpDes);
            quantita.setValue(rigaMov.getQuantita());
            importounitario.setValue(rigaMov.getUnitsi());
            if (listinoIvato) {
                unitarioivato.setValue(rigaMov.getUnitci());
            }
            sc1.setValue(rigaMov.getSc1());
            sc2.setValue(rigaMov.getSc2());
            sc3.setValue(rigaMov.getSc3());
            sc4.setValue(rigaMov.getSc4());
            costoImponibile = rigaMov.getCostoimponibile();
            tbLotto.setValue(rigaMov.getLotto());
            tbUnmisura.setValue(rigaMov.getUnmisura());
            finestraRigaMovimenti.setTitle(" - Modifica Riga - ");
            btnNuovo.setVisible(false);
            bdArticolo.setFocus(true);
            if (listinoIvato) {
                modificoPrezzoUnitario();
            }
            aggiornaTotali();
        } else {
            quantita.setValue(new CfgGlobalController().getCfgGlobal().getDefaultQuantita());
            rigaMov = new Movcorpo();
            finestraRigaMovimenti.setTitle(" - Nuova Riga - ");
            btnSalvaModifiche.setVisible(false);
            costoImponibile = BigDecimal.ZERO;
            bdArticolo.setFocus(true);
            if (codicecliente != null && !lTrasferimentoInterno) {
                Nominativi cliente = new NominativiController().getNominativoXCodice(codicecliente);
                sc1.setValue(cliente.getNomsco1());
                sc2.setValue(cliente.getNomsco2());
                sc3.setValue(cliente.getNomsco3());
                sc4.setValue(cliente.getNomscog());
                if (cliente.getCodali() != null && cliente.getCodali().isEmpty()) {
                    bdAliquota.setValue(cliente.getCodali());
                }
            }
        }
        if (!lGestisceTagliacolore) {
            spTagliacolore1.setVisible(false);
            spTagliacolore2.setVisible(false);
            hbTagliacolore.setVisible(false);
            btnTagliacolore.setVisible(false);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$finestraRigaMovimentiNota(Event event) {
        CreateEvent ce = (CreateEvent) ((ForwardEvent) event).getOrigin();
        args = (Map<String, Object>) ce.getArg();
        boxCorpo = (Listbox) args.get("boxCorpo");
        if (args.containsKey("lNuovo")) {
            lNuovo = true;
        } else {
            lNuovo = false;
        }
        listino = (String) args.get("listino");
        codicecliente = (String) args.get("codicecliente");
        trasferimentoInterno = (String) args.get("trasferimento");
        lTrasferimentoInterno = (trasferimentoInterno != null && trasferimentoInterno.equalsIgnoreCase("S") ? true : false);
        if (!lNuovo) {
        } else {
            rigaMov = new Movcorpo();
            finestraRigaMovimentiNota.setTitle(" - Nuova Riga di Nota - ");
            nota.setFocus(true);
        }
    }

    public void afterCompose() {
        Components.wireVariables(this, this);
        if (quantita == null) {
            Components.addForwards(this, this);
            return;
        }
        quantita.addEventListener(Events.ON_CHANGE, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
            }
        });
        quantita.addEventListener(Events.ON_OK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
            }
        });
        importounitario.addEventListener(Events.ON_CHANGE, new EventListener() {

            @SuppressWarnings("unchecked")
            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
                if (tipoMovimento.getTmcontro().equals("3") && tipoMovimento.isModificalistini()) {
                    if (bdArticolo.getValue().isEmpty()) {
                        return;
                    }
                    Articoli art = new ArticoliController().getArticoliXCodice(bdArticolo.getValue(), false);
                    if (art.getCodart().isEmpty()) {
                        return;
                    }
                    BigDecimal tmpCostoUltimo = new BigDecimal("0");
                    tmpCostoUltimo = (art.getArcostoult() == null ? tmpCostoUltimo : art.getArcostoult());
                    if (tmpCostoUltimo.compareTo(importounitario.getValue()) != 0) {
                        HashMap map = new HashMap();
                        map.put("costoPrecedente", tmpCostoUltimo);
                        map.put("costoAttuale", importounitario.getValue());
                        map.put("codart", art.getCodart());
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
                            Listbox boxModListini = (Listbox) box2;
                            ListModelList lml = (ListModelList) boxModListini.getListModel();
                            int i = lml.getSize();
                            art.setArcostoult(importounitario.getValue());
                            try {
                                art = new ArticoliController().updateArticolo(art);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            for (int j = 0; j < i; j++) {
                                ListiniDaModificare item = (ListiniDaModificare) lml.getElementAt(j);
                                boolean trovato = false;
                                Listini listinoDaVariare = new ListiniController().getListiniXCodice(art.getCodart(), "", item.getCodlis());
                                listinoDaVariare.setLiprezzo(item.getPrezzoProposto());
                                if (listinoDaVariare.getLiid() != null) {
                                    new ListiniController().updateListini(listinoDaVariare);
                                    new OperazioniController().nuovaOperazione("Listino Variato: " + art.getCodart() + "-" + item.getCodlis(), "listini");
                                } else {
                                    listinoDaVariare.setCodart(art.getCodart());
                                    listinoDaVariare.setCodlis(item.getCodlis());
                                    new ListiniController().addListini(listinoDaVariare);
                                    new OperazioniController().nuovaOperazione("Nuovo Listino: " + art.getCodart() + "-" + item.getCodlis(), "listini");
                                }
                            }
                        }
                    }
                }
            }
        });
        unitarioivato.addEventListener(Events.ON_CHANGE, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                modificoPrezzoUnitario();
                aggiornaTotali();
            }
        });
        sc1.addEventListener(Events.ON_CHANGE, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
            }
        });
        sc2.addEventListener(Events.ON_CHANGE, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
            }
        });
        sc3.addEventListener(Events.ON_CHANGE, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
            }
        });
        sc4.addEventListener(Events.ON_CHANGE, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                aggiornaTotali();
            }
        });
        Components.addForwards(this, this);
    }

    public void onChange$bdArticolo() {
        modificaRigaArticolo();
    }

    public void modificaRigaArticolo() {
        String valore = bdArticolo.getValue();
        if (valore != null && valore.equalsIgnoreCase("NOTA")) {
            if (codicecliente != null && !codicecliente.isEmpty() && !lTrasferimentoInterno) {
                Nominativi cliente = new NominativiController().getNominativoXCodice(codicecliente);
                if (cliente.getCodali() != null && !cliente.getCodali().isEmpty()) {
                    bdAliquota.setValue(cliente.getCodali());
                } else {
                    bdAliquota.setValue(new CfgGlobalController().getCfgGlobal().getDefaultIva());
                }
            } else {
                bdAliquota.setValue(new CfgGlobalController().getCfgGlobal().getDefaultIva());
            }
            alides.setValue(new sisi.aliquote.AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false).getAlides());
            importounitario.setValue(BigDecimal.ZERO);
            quantita.setValue(BigDecimal.ZERO);
            costoImponibile = BigDecimal.ZERO;
        } else {
            Articoli articoloTrovato = new sisi.articoli.ArticoliController().getArticoliXCodalt(valore, false);
            if (articoloTrovato.getCodart() != null && !articoloTrovato.getCodart().isEmpty()) {
                if (lGestisceTagliacolore) {
                    Codalt codalttrovato = new CodicialtController().getCodalt(valore);
                    if (!codalttrovato.getCalcodalt().isEmpty()) {
                        if (codalttrovato.getCodtag() != null && !codalttrovato.getCodtag().isEmpty()) {
                            tbTaglia.setValue(codalttrovato.getCodtag());
                        }
                        if (codalttrovato.getColore() != null && !codalttrovato.getColore().isEmpty()) {
                            tbColore.setValue(codalttrovato.getColore());
                        }
                    }
                }
                bdArticolo.setValue(articoloTrovato.getCodart());
                ardes.setValue(articoloTrovato.getArdes());
                tbUnmisura.setValue(articoloTrovato.getArunmz());
                if (articoloTrovato.getCodali() != null && !articoloTrovato.getCodali().isEmpty()) {
                    boolean isCliente = true;
                    if (codicecliente != null && !codicecliente.isEmpty() && !lTrasferimentoInterno) {
                        Nominativi cliente = new NominativiController().getNominativoXCodice(codicecliente);
                        isCliente = cliente.getNomcli().equalsIgnoreCase("S");
                        if (cliente.getCodali() != null && !cliente.getCodali().isEmpty()) {
                            bdAliquota.setValue(cliente.getCodali());
                            alides.setValue(new sisi.aliquote.AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false).getAlides());
                        } else {
                            bdAliquota.setValue(articoloTrovato.getCodali());
                            alides.setValue(new sisi.aliquote.AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false).getAlides());
                        }
                    } else {
                        bdAliquota.setValue(articoloTrovato.getCodali());
                        alides.setValue(new sisi.aliquote.AliquoteController().getAliquoteXCodice(articoloTrovato.getCodali(), false).getAlides());
                    }
                    costoImponibile = articoloTrovato.getArcostoult();
                    if (isCliente) {
                        BigDecimal prezzo = new sisi.listini.ListiniController().getListiniXCodice(articoloTrovato.getCodart(), "", listino).getLiprezzo();
                        if (listinoIvato) {
                            unitarioivato.setValue(prezzo);
                            modificoPrezzoUnitario();
                        } else {
                            importounitario.setValue(prezzo);
                        }
                    } else {
                        importounitario.setValue(costoImponibile);
                    }
                }
            }
        }
        aggiornaTotali();
    }

    public void onOpen$bdArticolo() {
        bdArticolo.invalidate();
        selArticolo();
    }

    @SuppressWarnings("unchecked")
    public void selArticolo() {
        if (finestraArticoli != null) {
            return;
        }
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = bdArticolo.getValue();
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        finestraArticoli = (Window) Executions.createComponents("/articoli.zul", null, map);
        try {
            finestraArticoli.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceArticoliSelezionato");
        if (codiceSelezionato != null) {
            bdArticolo.setValue(codiceSelezionato);
        }
        modificaRigaArticolo();
        finestraArticoli = null;
    }

    public void onOpen$bdAliquota() {
        selAliquota();
    }

    public void onChange$bdAliquota() {
        modificaRigaAliquote(true);
    }

    public void modificaRigaAliquote(boolean lEcho) {
        String valore = bdAliquota.getValue();
        Aliquote tmpAli = new AliquoteController().getAliquoteXCodice(valore, lEcho);
        if (tmpAli.getCodali() != null && !tmpAli.getCodali().isEmpty()) {
            alides.setValue(tmpAli.getAlides());
        }
        aggiornaTotali();
    }

    @SuppressWarnings("unchecked")
    public void selAliquota() {
        if (finestraAliquote != null) {
            return;
        }
        HashMap map = new HashMap();
        String codiceSelezionato;
        codiceSelezionato = bdAliquota.getValue();
        map.put("codiceSelezionato", codiceSelezionato);
        map.put("tastoSeleziona", "S");
        finestraAliquote = (Window) Executions.createComponents("/aliquote.zul", null, map);
        try {
            finestraAliquote.doModal();
        } catch (SuspendNotAllowedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Session session = Sessions.getCurrent();
        codiceSelezionato = (String) session.getAttribute("codiceAliquoteSelezionato");
        if (codiceSelezionato != null) {
            bdAliquota.setValue(codiceSelezionato);
        }
        modificaRigaAliquote(true);
        finestraAliquote = null;
    }

    private void onChange$quantita(Event event) {
        aggiornaTotali();
    }

    private void onChange$importounitario(Event event) {
        aggiornaTotali();
    }

    private void onChange$unitarioivato(Event event) {
        modificoPrezzoUnitario();
        aggiornaTotali();
    }

    public void onClick$btnSalvaModifiche(Event event) throws WrongValueException, InterruptedException {
        add_update();
    }

    public void onClick$btnNuovo(Event event) throws WrongValueException, InterruptedException {
        add_update();
    }

    public void onClick$btnAnnulla(Event event) {
        finestraRigaMovimenti.detach();
    }

    public void onClick$btnAnnullaNote(Event event) {
        finestraRigaMovimentiNota.detach();
    }

    public void onClick$btnTagliacolore(Event event) throws SuspendNotAllowedException, InterruptedException {
        selezionaTagliaColore();
    }

    public void onClick$btnSalvaNota(Event event) {
        add_nota(null, null, false, null);
    }

    public void add_nota(Object oTestoNota, Object oCodiceCliente, boolean trasferimentoInterno, Object oBoxCorpo) {
        int nLineas = 0;
        String testo;
        String cRiga = "";
        int nCol = 0;
        if (oTestoNota != null) {
            testo = ((String) oTestoNota).trim();
            codicecliente = (String) oCodiceCliente;
            lTrasferimentoInterno = trasferimentoInterno;
            boxCorpo = (Listbox) oBoxCorpo;
            Movcorpo rigaMov = new Movcorpo();
        } else {
            testo = nota.getValue().trim();
        }
        BigDecimal percAliquota = new AliquoteController().getAliquoteXCodice(new CfgGlobalController().getCfgGlobal().getDefaultIva(), false).getPerc();
        String codAliquota = new CfgGlobalController().getCfgGlobal().getDefaultIva();
        if (codicecliente != null && !codicecliente.isEmpty() && !lTrasferimentoInterno) {
            Nominativi cliente = new NominativiController().getNominativoXCodice(codicecliente, false);
            if (cliente.getCodali() != null && !cliente.getCodali().isEmpty()) {
                codAliquota = cliente.getCodali();
                percAliquota = new AliquoteController().getAliquoteXCodice(codAliquota, false).getPerc();
            }
        } else {
            percAliquota = new AliquoteController().getAliquoteXCodice(new CfgGlobalController().getCfgGlobal().getDefaultIva(), false).getPerc();
            codAliquota = new CfgGlobalController().getCfgGlobal().getDefaultIva();
        }
        for (int i = 0; i < testo.length(); i++) {
            char valore = testo.charAt(i);
            if (valore == '\n') {
                nCol = 0;
            } else if (i == testo.length() - 1) {
                cRiga += valore;
                nCol = 0;
            } else {
                cRiga += valore;
                nCol++;
            }
            if (nCol >= 60) {
                if (valore != ' ') {
                    int j = 0;
                    int x = 60;
                    while (valore != ' ') {
                        valore = testo.charAt(i - j);
                        if (valore == ' ') {
                            i = i - j;
                            x = x - j;
                        } else {
                            j++;
                        }
                        if (j >= 60) {
                            valore = ' ';
                        }
                    }
                    cRiga = cRiga.substring(0, x - 1);
                }
                nCol = 0;
            }
            if (nCol == 0) {
                rigaMov = new Movcorpo();
                rigaMov.setCodart("NOTA");
                rigaMov.setArdes(cRiga);
                cRiga = "";
                rigaMov.setCodali(codAliquota);
                rigaMov.setQuantita(BigDecimal.ZERO);
                rigaMov.setUnitsi(BigDecimal.ZERO);
                rigaMov.setUnitci(BigDecimal.ZERO);
                rigaMov.setAliiva(percAliquota);
                rigaMov.setSc1(BigDecimal.ZERO);
                rigaMov.setSc2(BigDecimal.ZERO);
                rigaMov.setSc3(BigDecimal.ZERO);
                rigaMov.setSc4(BigDecimal.ZERO);
                rigaMov.setTotivato(BigDecimal.ZERO);
                rigaMov.setTotnetto(BigDecimal.ZERO);
                rigaMov.setCostoimponibile(BigDecimal.ZERO);
                ListModelList lml = (ListModelList) boxCorpo.getListModel();
                if (lml.indexOf(rigaMov) == -1) {
                    lml.add(rigaMov);
                    boxCorpo.setSelectedIndex(lml.indexOf(rigaMov));
                } else {
                    lml.set(lml.indexOf(rigaMov), rigaMov);
                }
            }
        }
        if (oTestoNota == null) {
            finestraRigaMovimentiNota.detach();
        }
    }

    void add_update() {
        if (!validazioneDati()) {
            return;
        }
        if (bdArticolo.getValue().isEmpty()) {
            return;
        }
        if (bdAliquota.getValue().isEmpty()) {
            return;
        }
        if (lNuovo) {
            rigaMov = new Movcorpo();
        }
        rigaMov.setCodart(bdArticolo.getValue());
        rigaMov.setArdes(ardes.getValue());
        rigaMov.setCodali(bdAliquota.getValue());
        rigaMov.setQuantita(quantita.getValue());
        rigaMov.setUnitsi(importounitario.getValue());
        rigaMov.setUnitci(unitarioivato.getValue());
        rigaMov.setAliiva(new AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false).getPerc());
        rigaMov.setSc1(sc1.getValue());
        rigaMov.setSc2(sc2.getValue());
        rigaMov.setSc3(sc3.getValue());
        rigaMov.setSc4(sc4.getValue());
        rigaMov.setLotto(tbLotto.getValue());
        rigaMov.setTotivato(totaleivato.getValue());
        rigaMov.setTotnetto(totalenetto.getValue());
        rigaMov.setCodtag(tbTaglia.getValue());
        rigaMov.setColore(tbColore.getValue());
        rigaMov.setCostoimponibile(costoImponibile);
        rigaMov.setUnmisura(tbUnmisura.getValue());
        if (lNuovo) {
            ListModelList lml = (ListModelList) boxCorpo.getListModel();
            if (lml.indexOf(rigaMov) == -1) {
                lml.add(rigaMov);
                boxCorpo.setSelectedIndex(lml.indexOf(rigaMov));
            } else {
                lml.set(lml.indexOf(rigaMov), rigaMov);
            }
        } else {
            ListModelList lml = (ListModelList) boxCorpo.getListModel();
            Object oRigaCorpo = boxCorpo.getSelectedItem().getAttribute("rigaCorpoMovimento");
            if (lml.indexOf(oRigaCorpo) == -1) {
            } else {
                lml.set(lml.indexOf(oRigaCorpo), rigaMov);
            }
        }
        finestraRigaMovimenti.detach();
    }

    @SuppressWarnings("unchecked")
    public boolean esisteArticolo(String codArtCerca) {
        boolean lEsiste = false;
        List tmpList = new ArticoliController().getListArtXCodice(codArtCerca);
        if (tmpList.size() > 0) {
            lEsiste = true;
            try {
                Messagebox.show("Codice Articolo [" + codArtCerca.trim() + "] esistente...", "Errore", Messagebox.OK, Messagebox.EXCLAMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            lEsiste = false;
        }
        return lEsiste;
    }

    private boolean validazioneDati() {
        boolean lOk = true;
        if (quantita.getValue() == null) {
            new General().MsgBox("Inserire una quantit�", "Errore");
            lOk = false;
        }
        if (importounitario.getValue() == null) {
            new General().MsgBox("Inserire importo unitario", "Errore");
            lOk = false;
        }
        Aliquote ali = new AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false);
        if (ali.getCodali() == null || ali.getCodali().isEmpty()) {
            new General().MsgBox("Aliquota IVA non valida", "Errore");
            lOk = false;
        }
        if (!bdArticolo.getValue().trim().equalsIgnoreCase("NOTA")) {
            Articoli art = new ArticoliController().getArticoliXCodice(bdArticolo.getValue(), false);
            if (art.getCodart().isEmpty()) {
                new General().MsgBox("Articolo non trovato", "Errore");
                lOk = false;
            }
            if (lGestisceTagliacolore && tbTaglia.getValue().isEmpty() && tbColore.getValue().isEmpty()) {
                if (art.getTagliacoloreart().size() > 0) {
                    new General().MsgBox("Selezionare taglia e colore", "Errore");
                    lOk = false;
                }
            }
        }
        return lOk;
    }

    private void aggiornaTotali() {
        BigDecimal centoBD = new BigDecimal(100);
        BigDecimal tmpTotaleNetto = new BigDecimal("0");
        Aliquote ali = new AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false);
        BigDecimal percAli = ali.getPerc();
        if (importounitario.getValue() != null && quantita.getValue() != null) {
            if (importounitario.getValue().compareTo(BigDecimal.ZERO) != 0 && quantita.getValue().compareTo(BigDecimal.ZERO) != 0) {
                tmpTotaleNetto = importounitario.getValue().multiply(quantita.getValue());
            } else {
            }
        }
        if (sc1.getValue() != null && sc1.getValue().compareTo(BigDecimal.ZERO) != 0) {
            tmpTotaleNetto = tmpTotaleNetto.subtract(tmpTotaleNetto.multiply(sc1.getValue()).divide(centoBD));
        }
        if (sc2.getValue() != null && sc2.getValue().compareTo(BigDecimal.ZERO) != 0) {
            tmpTotaleNetto = tmpTotaleNetto.subtract(tmpTotaleNetto.multiply(sc2.getValue()).divide(centoBD));
        }
        if (sc3.getValue() != null && sc3.getValue().compareTo(BigDecimal.ZERO) != 0) {
            tmpTotaleNetto = tmpTotaleNetto.subtract(tmpTotaleNetto.multiply(sc3.getValue()).divide(centoBD));
        }
        if (sc4.getValue() != null && sc4.getValue().compareTo(BigDecimal.ZERO) != 0) {
            tmpTotaleNetto = tmpTotaleNetto.subtract(tmpTotaleNetto.multiply(sc4.getValue()).divide(centoBD));
        }
        totalenetto.setValue(tmpTotaleNetto);
        if (percAli == null || percAli.compareTo(BigDecimal.ZERO) == 0 || importounitario.getValue() == null) {
            totaleivato.setValue(totalenetto.getValue());
            unitarioivato.setValue(importounitario.getValue());
        } else {
            totaleivato.setValue(totalenetto.getValue().multiply(percAli).divide(centoBD).add(totalenetto.getValue()));
            unitarioivato.setValue(importounitario.getValue().add(importounitario.getValue().multiply(percAli).divide(centoBD)));
        }
    }

    @SuppressWarnings("unchecked")
    private void selezionaTagliaColore() throws SuspendNotAllowedException, InterruptedException {
        if (bdArticolo.getValue().isEmpty()) {
            return;
        }
        Articoli art = new ArticoliController().getArticoliXCodice(bdArticolo.getValue(), false);
        if (art.getCodart().isEmpty()) {
            return;
        }
        Collection<Tagliacolore> tagliacolore = art.getTagliacoloreart();
        if (tagliacolore.size() == 0) {
            return;
        }
        HashMap map = new HashMap();
        map.put("codart", bdArticolo.getValue());
        Window finestraSelTagCol = (Window) Executions.createComponents("/selezionaTagCol.zul", null, map);
        finestraSelTagCol.doModal();
        String taglia = (String) Sessions.getCurrent().getAttribute("codiceTagliaSelezionato");
        String colore = (String) Sessions.getCurrent().getAttribute("codiceColoreSelezionato");
        if (!taglia.isEmpty() && !colore.isEmpty()) {
            tbTaglia.setValue(taglia);
            tbColore.setValue(colore);
        } else {
            tbTaglia.setValue("");
            tbColore.setValue("");
        }
    }

    void modificoPrezzoUnitario() {
        BigDecimal centoBD = new BigDecimal(100);
        Aliquote ali = new AliquoteController().getAliquoteXCodice(bdAliquota.getValue(), false);
        BigDecimal percAli = ali.getPerc();
        MathContext mcNO = new MathContext(2, RoundingMode.UNNECESSARY);
        MathContext mc2 = new MathContext(2, RoundingMode.HALF_EVEN);
        MathContext mc10 = new MathContext(10, RoundingMode.HALF_EVEN);
        BigDecimal impuni = importounitario.getValue();
        BigDecimal impuniivato = unitarioivato.getValue();
        if (impuniivato == null || impuniivato.compareTo(BigDecimal.ZERO) == 0) {
            importounitario.setValue(BigDecimal.ZERO);
            return;
        }
        if (percAli.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal val1 = percAli.add(centoBD, mc10);
            impuni = impuniivato.multiply(centoBD, mc10);
            impuni = impuni.divide(val1, mc10);
            importounitario.setValue(impuni);
        } else {
            importounitario.setValue(impuniivato);
        }
    }
}
