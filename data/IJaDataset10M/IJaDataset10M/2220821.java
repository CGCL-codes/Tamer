package pokeman;

/**
 * catches pokemon, but as of now it does nothing
 * @author Kunal
 */
public class Pokeball extends Item<Pokemon> {

    private double multiplier;

    public Pokeball(String name) {
        super(name, 1);
        if (name.equals("Poke Ball")) {
            setPrice(200);
            multiplier = 1;
        } else if (name.equals("Great Ball")) {
            setPrice(600);
            multiplier = 1.5;
        } else if (name.equals("Ultra Ball")) {
            setPrice(1200);
            multiplier = 2;
        } else if (name.equals("Master Ball")) {
            setPrice(0);
            multiplier = 255;
        }
    }

    /**
     * returns the number of shakes. If the number of shakes == 4, it is captured
     * if it equals -1, then i dunno
     * @param other
     * @return
     */
    @Override
    public int use(Pokemon other) {
        int catchrate = other.getCatchRate();
        int max = other.getMaxHP();
        int curr = other.getCurrentHP();
        double status = other.getStatus().getCatchMultipler();
        int a = (int) ((((3 * max - 2 * curr) * catchrate * multiplier) / (3 * max)) * status);
        if (a >= 255) {
            return 4;
        } else {
            int b = (int) (1048560 / Math.sqrt(Math.sqrt(16711680.0 / a)));
            int ran1 = (int) (Math.random() * 65535);
            int ran2 = (int) (Math.random() * 65535);
            int ran3 = (int) (Math.random() * 65535);
            int ran4 = (int) (Math.random() * 65535);
            int counter = 0;
            if (ran1 <= b) counter++;
            if (ran2 <= b) counter++;
            if (ran3 <= b) counter++;
            if (ran4 <= b) counter++;
            return counter;
        }
    }
}
