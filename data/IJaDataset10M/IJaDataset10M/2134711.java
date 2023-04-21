package HauptKasse;

import java.util.Hashtable;
import Sale.Transaction;
import Sale.SalesPoint;
import Sale.Catalog;
import Sale.Display.*;
import Sale.Currency.*;
import Tools.Debug;

/**
*
* Diese <code>Transaction</code> der Hauptkasse meldet einen Kellner bei der Hauptkasse an.
* Der Kellner wird nach Name und Passwort gefragt und in die Kellnerliste aufgenommen
*
*/
public class TKellneranmelden extends Transaction {

    /**Referenz auf die HauptKasse*/
    CHauptKasse hk;

    /**
*
* Constructor.
*
*@param sp Referenz auf die Hauptkasse
*/
    public TKellneranmelden(SalesPoint sp) {
        super(sp);
        hk = (CHauptKasse) sp;
    }

    /**
*
* f�hrt die KellnerAnmeldung durch.
* fragt nach Name und Passwort. Verhindert, da� ein Kellner sich zweimal anmeldet.
*
*/
    protected void executeTransaction() {
        Debug.proc("TKellneranmelden.executeTransaction");
        while (true) {
            FormSheet fs = getDisplayManager().createFormSheet();
            fs.addItem(new InputLineDescriptor("NAME:"));
            fs.addItem(new PassWDInputLineDescriptor("PASSWORT:"));
            fs.setFlags(FormSheet.CONFIRM_INPUT, true);
            fs.setName("Kellner anmelden");
            getDisplayManager().fillFormSheet(fs);
            String KellnerName = (String) fs.getItem(0).getData();
            String KellnerPasswort = (String) fs.getItem(1).getData();
            if (hk.getKellner(KellnerName) != null) {
                getDisplayManager().doIO(new MsgIO(this, "ACHTUNG", "Es ist bereits ein Kellner unter diesem Namen angemeldet", new String[] { "OK" }));
                return;
            }
            if (hk.getKellnerPasswd(KellnerName) == null) {
                MsgReturn rt = (MsgReturn) getDisplayManager().doIO(new MsgIO(this, "FRAGE", "Sind Sie sicher, ein neuer Kellner zu sein ?", new String[] { "OK", "Abbrechen" }));
                if (rt.answer() == 1) continue; else {
                    fs = getDisplayManager().createFormSheet();
                    fs.addItem(new PassWDInputLineDescriptor("Passwort fuer " + KellnerName + " best�tigen:"));
                    getDisplayManager().fillFormSheet(fs);
                    String PasswortBestaetigt = (String) fs.getItem(0).getData();
                    if (PasswortBestaetigt.equals(KellnerPasswort)) {
                        hk.kellnerAnmelden(KellnerName, KellnerPasswort, gibMoneyBag());
                        break;
                    } else {
                        getDisplayManager().doIO(new MsgIO(this, "PECH", "Falsches Passwort", new String[] { "OK" }));
                        continue;
                    }
                }
            }
            if (KellnerPasswort.equals(hk.getKellnerPasswd(KellnerName))) {
                hk.kellnerAnmelden(KellnerName, gibMoneyBag());
                break;
            } else {
                getDisplayManager().doIO(new MsgIO(this, "PECH", "Falsches Passwort", new String[] { "OK" }));
                continue;
            }
        }
        return;
    }

    /**
 *
 * stellt dem frischgebackenen Kellnermenschen ein wenig Geld zur Verf�gung
 *
 *@return Der prallgef�llte <code>MoneyBag</code>
 */
    private MoneyBag gibMoneyBag() {
        Debug.proc("TKellneranmelden.gibMoneyBag()");
        Currency cMoney = (Currency) Catalog.forName(Currency.DEUTSCHMARK);
        StockEditReturn ser = (StockEditReturn) theDisplayManager.doIO(new StockEditIO(this, "Wechselgeld f�r den Kellner", cMoney, null, new MoneyBag("", cMoney)));
        MoneyBag mb = (MoneyBag) ser.getData();
        return mb;
    }
}
