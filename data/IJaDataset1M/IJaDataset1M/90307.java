package samples.privateandfinal;

/**
 * A class used to test the functionality to mock private methods that are also
 * final.
 * 
 * @author Johan Haleby
 */
public class StupidPrivateFinal {

    public String say(String name) {
        return sayIt(name);
    }

    private final String sayIt(String name) {
        return "Hello " + name;
    }
}
