package org.joy.db;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.SecondaryDatabase;
import com.sleepycat.db.SecondaryKeyCreator;

/**
 *
 * @author 海
 */
public abstract class SecDbKeyCreator implements SecondaryKeyCreator {

    private EntryBinding secKeyBinding;

    public SecDbKeyCreator(EntryBinding secKeyBinding) {
        this.secKeyBinding = secKeyBinding;
    }

    public EntryBinding getSecKeyBinding() {
        return secKeyBinding;
    }
}
