package gc;

/**
 * GC tests have no dependecy on class library.
 *
 * @keyword gc
 */
public class Force {

    public static void main(String[] args) {
        System.out.println("forcing gc...");
        System.gc();
        System.out.println("PASS");
    }
}
