package thread.immutability;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class ImmutableSet {

    Random r = new Random();

    private final Set pins;

    public ImmutableSet(Set s) {
        pins = Collections.emptySet();
        pins.addAll(s);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Set<String> s = new TreeSet<String>();
        s.add("b");
        s.add("a");
        s.add("c");
        System.out.println(s);
        ImmutableSet ims = new ImmutableSet(s);
        System.out.println(ims.getPins());
    }

    Set getPins() {
        return Collections.unmodifiableSet(pins);
    }
}
