package webserg.pazzlers;

/**
 * 
 * @author Sergiy Doroshenko
 * Jul 19, 2008 1:10:22 AM
 * 
 * Class initialization is tricky, and auto-unboxing happens where you
 * least expect it 
 * 
 * Moral: construct instance at end of class initialization
 * to fix: reorganize order of static fields
 */
public class Elvis {

    public static final Elvis ELVIS = new Elvis();

    private Elvis() {
    }

    private static final Boolean LIVING = true;

    private final Boolean alive = LIVING;

    {
        System.out.println(alive);
    }

    static {
        System.out.println(LIVING);
    }

    public final Boolean lives() {
        return alive;
    }

    public static void main(String[] args) {
        System.out.println(ELVIS.lives() ? "Hung dog" : "hotel");
    }
}
