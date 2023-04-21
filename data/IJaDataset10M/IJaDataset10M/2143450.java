package HauptKasse;

import java.io.*;
import java.util.*;
import Sale.Display.*;
import Sale.Transaction;
import Sale.SalesPoint;
import Sale.Catalog;
import Sale.*;
import Sale.Currency.*;
import Gerichte.CSpeiseKarte;
import Kellner.CKellner;
import Tools.Debug;
import Tools.Defines;

/**unsere Transaction f�rs Speichern
in dieser Transaction werden alle Speicheraktionen erledigt,
d.h. Dateiname erfragen, Speicherungstyp erfrage, entsprechende Daten sichern*/
public class TSpeichern extends Transaction {

    /**Referenz zur Hauptkasse*/
    CHauptKasse HauptKasse;

    /**event. vorbelegter Dateiname*/
    String filename = null;

    /**event. vorbelegter Dateityp*/
    int type = 0;

    /**der Konstruktor
    die Referenz zum Salespoint wird sich als Hauptkasse gemerkt*/
    public TSpeichern(SalesPoint s) {
        super(s);
        HauptKasse = (CHauptKasse) s;
    }

    public TSpeichern(SalesPoint s, String Standard, int Type) {
        this(s);
        filename = Standard;
        type = Type;
    }

    /**hier passiert das Speichern
    @param DateiTyp Art der Datei in Defines festgelegt
    @see Tools.Defines
    @param Dateiname der Dateiname der Datei*/
    private void RealySaving(int DateiTyp, String Dateiname) {
        Debug.out("File to open: " + Dateiname);
        ObjectOutputStream Datei;
        try {
            Datei = new ObjectOutputStream(new FileOutputStream(Dateiname));
        } catch (IOException e) {
            Debug.err("Unable to open file:" + Dateiname);
            getDisplayManager().doIO(new MsgIO(this, "ACHTUNG", "... es ist ein Fehler beim �ffnen der Datei aufgetreten\nDatei: '" + Dateiname + "'", new String[] { "Ok" }));
            HauptKasse.alive = false;
            return;
        }
        try {
            Datei.writeObject(Defines.FILEDESCR[DateiTyp]);
        } catch (Exception e) {
            Debug.err("Filedescription error : " + e);
            getDisplayManager().doIO(new MsgIO(this, "ACHTUNG", "... es ist ein Fehler beim Schreiben der Datei aufgetreten\nDatei: '" + Dateiname + "'", new String[] { "Ok" }));
            HauptKasse.alive = false;
            return;
        }
        Debug.out("Filedescription: " + Defines.FILEDESCR[DateiTyp]);
        Debug.out("save Catalog / Stock listen ...");
        try {
            Catalog.storeGlobalCatalogs(Datei);
            Stock.storeGlobalStocks(Datei);
            Debug.out("save Zuliefererliste ...");
            Datei.writeObject(HauptKasse.getZulieferer());
            Debug.out("save KellnerTable");
            Datei.writeObject(HauptKasse.getKellnerTable());
            Debug.out("save HauptKassenMoneyBag");
            Datei.writeObject(HauptKasse.getMoneyBag());
            Debug.out("save KellnerVector");
            Hashtable ht = new Hashtable();
            Vector kv = HauptKasse.getKellnerVector();
            Enumeration kellner = kv.elements();
            CKellner aktKellner;
            MoneyBag aktMoney;
            while (kellner.hasMoreElements()) {
                aktKellner = (CKellner) kellner.nextElement();
                aktMoney = aktKellner.getMoneyBag();
                Debug.out("aktKellner: " + aktKellner.getName() + " MB: " + aktMoney);
                ht.put(aktKellner.getName(), aktMoney);
            }
            Datei.writeObject(ht);
            Debug.out("save Kellnertable");
            Datei.writeObject(HauptKasse.getKellnerTable());
        } catch (IOException e) {
            Debug.out("Catalog Error IOException: save Catalog / Stock listen ...");
            HauptKasse.alive = false;
            return;
        }
        HauptKasse.alive = false;
    }

    /**hier wird die Transaction gestartet, d.h. (event.) nach Dateiname /Typ gefragt und danach Realysaving aufgerufen*/
    protected void executeTransaction() {
        Debug.proc("TSpeichern.executeTransaction");
        if ((filename == null) && (type == 0)) {
            FormSheet fs = getDisplayManager().createFormSheet();
            fs.setName("Datei speichern");
            fs.addItem(new ChoiceDescriptor("was soll gespeichert werden ?", Defines.FILEDESCR));
            fs.addItem(new InputLineDescriptor("Datei:"));
            fs.setFlags(FormSheet.CONFIRM_INPUT, true);
            getDisplayManager().fillFormSheet(fs);
            type = ((Integer) fs.getItem(0).getData()).intValue();
            filename = (String) fs.getItem(1).getData();
        }
        RealySaving(type, filename);
    }
}
