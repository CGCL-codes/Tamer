package containers;

import java.util.*;
import net.mindview.util.*;
import static net.mindview.util.Print.*;

public class CollectionMethods {

    public static void main(String[] args) {
        Collection<String> c = new ArrayList<String>();
        c.addAll(Countries.names(6));
        c.add("ten");
        c.add("eleven");
        print(c);
        Object[] array = c.toArray();
        String[] str = c.toArray(new String[0]);
        print("Collections.max(c) = " + Collections.max(c));
        print("Collections.min(c) = " + Collections.min(c));
        Collection<String> c2 = new ArrayList<String>();
        c2.addAll(Countries.names(6));
        c.addAll(c2);
        print(c);
        c.remove(Countries.DATA[0][0]);
        print(c);
        c.remove(Countries.DATA[1][0]);
        print(c);
        c.removeAll(c2);
        print(c);
        c.addAll(c2);
        print(c);
        String val = Countries.DATA[3][0];
        print("c.contains(" + val + ") = " + c.contains(val));
        print("c.containsAll(c2) = " + c.containsAll(c2));
        Collection<String> c3 = ((List<String>) c).subList(3, 5);
        c2.retainAll(c3);
        print(c2);
        c2.removeAll(c3);
        print("c2.isEmpty() = " + c2.isEmpty());
        c = new ArrayList<String>();
        c.addAll(Countries.names(6));
        print(c);
        c.clear();
        print("after c.clear():" + c);
    }
}
