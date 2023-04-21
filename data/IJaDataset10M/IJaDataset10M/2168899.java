package collections.sets;

import collections.sets.B;
import collections.sets.Boo;
import collections.sets.Boo2;
import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedHashSetUsing {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Set<Boo> l = new LinkedHashSet<Boo>();
        Boo f = new Boo("qwerty");
        Boo f2 = new Boo2();
        f2.test(new B());
    }
}
