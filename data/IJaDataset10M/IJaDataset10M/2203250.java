package panda.transaction.concurrency;

import java.util.HashMap;
import java.util.Map;
import panda.file.Block;

public class ConcurrencyManager {

    private static LockTable lockTable = new LockTable();

    private Map<Block, Boolean> locks = new HashMap<Block, Boolean>();

    public void sharedLock(Block block) {
        if (locks.get(block) == null) {
            lockTable.sharedLock(block);
            locks.put(block, false);
        }
    }

    public void exclusiveLock(Block block) {
        if (locks.get(block) != null) {
            if (locks.get(block).booleanValue()) {
                return;
            }
        }
        sharedLock(block);
        lockTable.exclusiveLock(block);
        locks.put(block, true);
    }

    public void releaseAll() {
        for (Block block : locks.keySet()) lockTable.release(block);
        locks.clear();
    }
}
