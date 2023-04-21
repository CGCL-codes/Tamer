package Versandhausagentur.Transaction;

import java.util.Enumeration;
import Sale.SalesPoint;
import Sale.Transaction;
import Sale.Catalog;
import Sale.CatalogItem;
import Sale.DictionaryCatalogItem;
import Sale.Stock;
import Sale.CountingStock;
import Sale.StockItem;
import Sale.NoSuchElementException;
import Sale.DuplicateKeyException;
import Sale.Display.MenuSheet;
import Sale.Display.SubmenuSheet;
import Sale.Display.ButtonAction;
import Sale.Display.IOEvent;
import Sale.Display.IOReturn;
import Sale.Display.MsgIO;
import Sale.Display.MsgReturn;
import Sale.Display.DisplayCatalog;
import Sale.Display.StockEditIO;
import Sale.Display.StockEditReturn;
import Sale.Display.NoSuchFormSheetItemException;
import Versandhausagentur.Agentur;
import Versandhausagentur.Log.AgenturLog;
import Versandhausagentur.Versandhaus;
import Versandhausagentur.Versandhauskatalog;
import Versandhausagentur.Artikel;
import Versandhausagentur.Preis;
import Versandhausagentur.Display.ZeileIO;
import Versandhausagentur.Display.ZeileReturn;
import Versandhausagentur.Display.ArtikelIO;
import Versandhausagentur.Display.ArtikelReturn;
import Versandhausagentur.Display.ArtikelwahlIO;
import Versandhausagentur.Display.ArtikelwahlReturn;
import Versandhausagentur.Transaction.AgenturTransaction;

/**
 * Der Artikel als Partner der Versandhausagentur wird in einer gesonderten Datenstruktur, dem
 * Versandhauskatalog, verwaltet. Diese Verwaltung wird durch die VersandhauskatalogTransaction gesteuert.
 *
 * @version     1.0
 * @author      Maik Boden, Rayko Enz
 * @see         Versandhausagentur.AgenturTransaction
**/
public class VersandhausTransaction extends AgenturTransaction {

    /**
     * Erzeugt eine neue VersandhauskatalogTransaction die der Verwaltung des Versandhauskataloges der
     * Versandhausagentur dient und alle hierzu notwendigen Methoden bereitstellt.
    **/
    public VersandhausTransaction(SalesPoint theSalesPoint) {
        super(theSalesPoint);
    }

    /**
     * F�hrt die Verwaltung des Versandhauses durch.
     * <p>
     * Hierzu gehoeren die Pflege des Versandhauskataloges, die Vergabe des Rabattsatzes
     * und die Festlegung der Zahlungsfrist.
     *
     * @exception   NoSuchFormSheetItemException Wird ausgel�st, wenn ... siehe Tutorial zum Salespoint-Framework
     * @see         Versandhausagentur.Transaction.AgenturTransaction
    **/
    public void executeTransaction() throws NoSuchFormSheetItemException {
        infiniteLoop();
    }

    /**
     * �bergibt das Untermen� mit allen Funktionen zur Pflege des Versandhauskataloges
     *
     * @return      Zur�ckgegeben wird das Men�, in Form eines AWTMenuSheet.
    **/
    public MenuSheet getDefaultMenuSheet() {
        MenuSheet ms = theDisplayManager.createMenuSheet();
        ms.setName("Versandhaus verwalten");
        ms.addButton("Versandhauskatalog pflegen", new ButtonAction() {

            public void perform() {
                katalog();
            }
        });
        ms.addButton("Rabattsatz festlegen", new ButtonAction() {

            public void perform() {
                editRabatt();
            }
        });
        ms.addButton("Zahlungsfrist festlegen", new ButtonAction() {

            public void perform() {
                editZahlungsfrist();
            }
        });
        ms.addButton("Zum Hauptmen� zur�ckkehren", new ButtonAction() {

            public void perform() {
                Transaction.this.stop();
            }
        });
        return ms;
    }

    /**
     * Pflegt den Versandhauskatalog.
    **/
    protected void katalog() {
        boolean goloop = true;
        while (goloop) {
            ArtikelwahlReturn awrt = (ArtikelwahlReturn) theDisplayManager.doIO(new ArtikelwahlIO(this, "Versandhauskatalog pflegen", Versandhauskatalog.getKatalog(Agentur.VERSANDHAUSKATALOG), new String[] { "Neuen Artikel aufnehmen", "Artikel bearbeiten", "Artikel entfernen", "Zur�ck zur Versandhausverwaltung" }));
            switch(awrt.answer) {
                case 0:
                    addArtikel();
                    break;
                case 1:
                    editArtikel(awrt.markedArtikel);
                    break;
                case 2:
                    deleteArtikel(awrt.markedArtikel);
                    break;
                default:
                    goloop = false;
                    return;
            }
        }
    }

    /**
     * Nimmt einen neuen Artikel in den Versandhauskatalog der Versandhausagentur auf.
    **/
    protected void addArtikel() {
        Artikel artikel = new Artikel(new String(""));
        boolean go = true;
        while (go) {
            go = false;
            ArtikelReturn art = (ArtikelReturn) getDisplayManager().doIO((IOEvent) new ArtikelIO(this, "Neuen Artikel aufnehmen", artikel, new String[] { "Artikel aufnehmen", "Abbruch" }));
            switch(art.answer) {
                case 1:
                    break;
                default:
                    artikel = (Artikel) art.artikel;
                    if (artikel.getKey().equals("")) {
                        go = true;
                        getDisplayManager().doIO(new MsgIO(this, "KEINE Artikelnummer", "F�r die eindeutige Vergabe der Artikelnummern sind Sie selbst zust�ndig.", new String[] { "OK" }));
                    } else {
                        try {
                            Versandhauskatalog.getKatalog(Agentur.VERSANDHAUSKATALOG).addItem((DictionaryCatalogItem) artikel);
                        } catch (DuplicateKeyException e) {
                            getDisplayManager().doIO(new MsgIO(this, "DOPPELTE ArtikelNUMMER", "Ein Artikel mit dieser Artikelnummer ist schon im Versandhauskatalog vorhanden.", new String[] { "OK" }));
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Bearbeitet die Daten eines Artikel im Versandhauskatalog
     *
     * @param       artikel Der zu bearbeitende Artikel
    **/
    protected void editArtikel(Artikel artikel) {
        if (artikel != null) {
            ArtikelReturn art = (ArtikelReturn) getDisplayManager().doIO((IOEvent) new ArtikelIO(this, "Artikeldaten bearbeiten", (Artikel) artikel, new String[] { "�nderungen �bernehmen", "Abbruch" }));
            switch(art.answer) {
                case 1:
                    break;
                default:
                    artikel.setAttribute("Bezeichnung", art.artikel.getAttribute("Bezeichnung"));
                    artikel.setAttribute("Beschreibung", art.artikel.getAttribute("Beschreibung"));
                    artikel.setAttribute("Preis", art.artikel.getAttribute("Preis"));
                    break;
            }
        }
    }

    /**
     * Entfernt einen Artikel aus dem Versandhauskatalog
     *
     * @param       artikel Der zu entfernende Artikel
    **/
    protected void deleteArtikel(Artikel artikel) {
        if (artikel != null) {
            ArtikelReturn art = (ArtikelReturn) getDisplayManager().doIO((IOEvent) new ArtikelIO(this, "Artikel entfernen", (Artikel) artikel, new String[] { "Artikel entfernen", "Abbruch" }));
            switch(art.answer) {
                case 1:
                    break;
                default:
                    try {
                        ((Catalog) Versandhauskatalog.getKatalog(Agentur.VERSANDHAUSKATALOG)).deleteItem(((DictionaryCatalogItem) art.artikel).getKey());
                    } catch (NoSuchElementException e) {
                        getDisplayManager().doIO(new MsgIO(this, "UNBEKANNTE ArtikelNUMMER", "Ein Artikel mit der angegebenen Artikelnummer existiert nicht im Versandhauskatalog.", new String[] { "OK" }));
                    }
                    break;
            }
        }
    }

    /**
     * Bearbeitet den aktuellen Rabattsatz beim Versandhaus.
    **/
    protected void editRabatt() {
        boolean go = true;
        while (go) {
            go = false;
            Float rabatt = Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).getRabatt(new Preis());
            ZeileReturn zrt = (ZeileReturn) getDisplayManager().doIO(new ZeileIO(this, "Rabattsatz festlegen", "aktueller Rabattsatz : ", rabatt.toString(), new String[] { "OK", "Abbruch" }));
            switch(zrt.answer) {
                case 0:
                    Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).removeAlleRabatte();
                    try {
                        Float rb = new Float(zrt.inputLine);
                        if (rb.floatValue() < 1) {
                            rb = new Float(1);
                            go = true;
                        }
                        if (rb.floatValue() > 99) {
                            rb = new Float(99);
                            go = true;
                        }
                        Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).addRabatt(rb, new Preis());
                    } catch (NumberFormatException e) {
                        go = true;
                        Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).addRabatt(rabatt, new Preis());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Bearbeitet die aktuelle Zahlungsfrist beim Versandhaus.
    **/
    protected void editZahlungsfrist() {
        boolean go = true;
        while (go) {
            go = false;
            Integer rechnungslegung = Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).getRechnungslegung();
            ZeileReturn zrt = (ZeileReturn) getDisplayManager().doIO(new ZeileIO(this, "Zahlungsfrist festlegen", "Tage bis zur Bezahlung : ", rechnungslegung.toString(), new String[] { "OK", "Abbruch" }));
            switch(zrt.answer) {
                case 0:
                    try {
                        Integer rl = new Integer(zrt.inputLine);
                        if (rl.intValue() < 1) {
                            rl = new Integer(1);
                            go = true;
                        }
                        if (rl.intValue() > 60) {
                            rl = new Integer(60);
                            go = true;
                        }
                        Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).setRechnungslegung(rl);
                    } catch (NumberFormatException e) {
                        go = true;
                        Versandhaus.getVersandhaus(Agentur.VERSANDHAUS).setRechnungslegung(rechnungslegung);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
