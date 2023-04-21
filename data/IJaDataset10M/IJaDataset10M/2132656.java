package org.dancres.blitz.entry;

import java.io.IOException;
import java.util.Set;

public class Types {

    /**
       Despite appearances, this method doesn't ever need to be called
       within a DiskTxn because it will only ever be invoked as the indirect
       result of a write.  A write forces loading/creation of the appropriate
       EntryRepository and <B>then</B> generates an event which can wake
       up the notify system or blocked match requests.
     */
    public static boolean isSubtype(String aType, String aSubtype) {
        if (aType.equals(aSubtype)) return true;
        EntryRepository myRepos = null;
        try {
            myRepos = EntryRepositoryFactory.get().find(aType);
        } catch (IOException anIOE) {
        }
        if (myRepos != null) {
            return myRepos.getSubtypes().contains(aSubtype);
        }
        return false;
    }
}
