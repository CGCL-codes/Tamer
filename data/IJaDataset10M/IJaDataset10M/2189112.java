package de.tudarmstadt.ukp.wikipedia.timemachine.dump.version;

import de.tudarmstadt.ukp.wikipedia.wikimachine.dump.version.IDumpVersion;
import de.tudarmstadt.ukp.wikipedia.wikimachine.dump.version.IDumpVersionFactory;
import de.tudarmstadt.ukp.wikipedia.wikimachine.hashing.StringHashCodeDisabled;

public class DumpVersionJDKStringKeyFactory implements IDumpVersionFactory {

    @Override
    public IDumpVersion getDumpVersion() {
        IDumpVersion dumpVersion = null;
        try {
            dumpVersion = new DumpVersionJDKGeneric<String, StringHashCodeDisabled>(StringHashCodeDisabled.class);
        } catch (Exception e) {
            dumpVersion = null;
        }
        return dumpVersion;
    }
}
