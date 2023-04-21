package modell;

import views.*;

/**
 * Bank osztaly
 * @author NKode
 */
public class Bank extends Element {

    private int money;

    /**
     * konstruktor
     * @param g jatekoazonosito
     */
    public Bank(Game g) {
        super(g);
        this.setMyview(new views.BankView(this));
    }

    /**
     * A bankban levo penz csokkentese
     * @param amount enniyvel csokkenti a bank penzkeszletet
     */
    public void DecreaseMoney(int amount) {
        money = money - amount;
    }

    /**
     * Bankrablas tortenik (a rablo a bankhoz ert)
     * @param robber a rablo
     */
    @Override
    public void ReachedBy(Robber robber) {
        if (money > 0) {
            robber.setIsrobbing(true);
            robber.IncLoot(1);
            this.DecreaseMoney(1);
        }
    }

    /**
     * A bankban levo penzkeszlet beallitasa
     * @param money ennyire allitjuk a penzkeszletet
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Bankban levo penzkeszlet lekerdezese
     * @return a penzkeszlet erteke
     */
    public int getMoney() {
        return this.money;
    }

    @Override
    public void ReachedBy(Civilian civilian) {
    }

    @Override
    public void ReachedBy(Cop cop) {
    }
}
