package tristero.lookup;

import java.util.*;
import tristero.util.*;
import discordia.*;

public class TransientLookup extends AbstractLookup {

    Map pub = new Hashtable();

    Map priv = new Hashtable();

    public void associate(String id, List addresses, boolean opaque) {
        if (opaque) priv.put(id, addresses); else pub.put(id, addresses);
    }

    public void forget(String id) {
        pub.remove(id);
        priv.remove(id);
    }

    public List lookup(String id) {
        List l = (List) pub.get(id);
        return l;
    }

    public String connect(String id, int strategy) {
        return null;
    }
}
