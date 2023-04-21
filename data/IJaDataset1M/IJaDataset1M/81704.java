package HauptKasse;

import Sale.*;
import Sale.Display.*;
import Sale.Currency.*;
import Log.*;
import java.io.*;
import java.util.*;
import Tools.*;
import Gerichte.*;
import HauptKasse.*;
import Kueche.*;

public class TZukochendeGerichte extends Transaction {

    public TZukochendeGerichte(SalesPoint sp) {
        super(sp);
        if (Debug.Debug) Debug.out("TZukochendeGerichte.TZukochendeGerichte()");
    }

    /**
    nimmt sich aus der Bestelliste alle die Gerichte heraus,<BR>
    welche noch gekocht werden m�ssen und ordnet<BR>
    sie den entsprechenden Tisch und Platz zu. Sollten keine<BR>
    Gerichte mehr zu kochen sein, bekommt man die entsprechende<BR>
    Meldung.<BR>
    */
    public void executeTransaction() {
        if (Debug.Debug) Debug.out("TZukochendeGerichte.executeTransaction()");
        StringBuffer theMessage = new StringBuffer("Folgende Gerichte m�ssen noch gekocht werden: ");
        CBestellListe bliste;
        try {
            bliste = (CBestellListe) Stock.forName(Defines.BESTELLISTE);
        } catch (Sale.NoSuchStockException e) {
            System.err.println("NOSUCHSTOCKEX.. IN TRECHNUNGSBEZAHLEN");
            return;
        }
        Enumeration en = bliste.gibAlleZuKochendenGerichte().elements();
        boolean full = false;
        while (en.hasMoreElements()) {
            full = true;
            CStockGericht gericht = (CStockGericht) en.nextElement();
            theMessage.append("\n");
            theMessage.append(gericht.getVerbal());
            theMessage.append(" f�r Tisch ");
            theMessage.append(gericht.getTisch());
            theMessage.append(", Platz ");
            theMessage.append(gericht.getPlatz());
        }
        String mes = "es m�ssen �berhaupt keine Gerichte mehr gekocht werden.\n���TSCH";
        if (full) mes = theMessage.toString();
        getDisplayManager().doIO(new MsgIO(this, "Zu Kochende Gerichte: ", mes, new String[] { "Alles Rodgerlie" }));
        return;
    }
}
