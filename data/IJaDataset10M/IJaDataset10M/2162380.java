package jse;

/**
 *
 * @author Swang
 */
public class ReflectTest {

    public static void main(String[] args) throws ClassNotFoundException {
        Class c = Class.forName("jse.FilePath");
        System.out.println(c.getCanonicalName());
    }
}
