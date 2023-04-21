package CashDesk;

import Kino;
import Sale.*;
import Sale.StdForms.*;

/** auff�llen der Kasse */
public class FillCashDesk extends Transaction {

    private Currency cMoney;

    private FormSheet fs;

    public FillCashDesk() {
        super("Geld einzahlen");
        cMoney = new Currency();
        fs = new EditStockForm("Geld einzahlen", "Wieviel Geld wollen Sie einzahlen ?", cMoney, new MoneyBag("Einzahlung", cMoney));
    }

    public void executeTransaction() {
        getOwner().setFormSheet(fs);
        Kino.getCashDesk().addMoney(((EditStockForm) fs).getDestStock().sumStock() / 100);
    }
}
