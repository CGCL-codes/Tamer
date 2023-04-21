package pokeman;

/**
 * Ethers increase PP for one move.
 * @author Kunal
 */
public class Ether extends Item<Move> {

    private int amountToHeal;

    public Ether(String name) {
        super(name, 1);
        if (name.equals("Ether")) {
            setPrice(3000);
            amountToHeal = 300;
        } else if (name.equals("Max Ether")) {
            setPrice(0);
            amountToHeal = -1;
        }
    }

    @Override
    public int use(Move m) {
        if (amountToHeal >= 0) return m.refilPP(amountToHeal); else return m.refilPP(m.getTotalPP());
    }
}
