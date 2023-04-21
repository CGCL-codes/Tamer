package spc.gaius.actalis.systems;

import java.util.BitSet;
import org.apache.openjpa.kernel.FetchConfiguration;
import org.apache.openjpa.kernel.OpenJPAStateManager;

public class LdapLazyStore extends LdapStoreManager {

    public static int loadcalls;

    public LdapLazyStore() {
    }

    public boolean load(OpenJPAStateManager sm, BitSet fields, FetchConfiguration fetch, int lockLevel, Object context) {
        loadcalls++;
        return super.load(sm, fields, fetch, lockLevel, context);
    }
}
