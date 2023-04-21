package collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CollectionUse {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Collection<String> c = new ArrayList<String>();
        c.add("1");
        c.removeAll(Collections.singleton("1"));
    }
}
