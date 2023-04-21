package com.gorillalogic.dal.common.table.store.scriptbase;

import com.gorillalogic.dal.InternalException;
import com.gorillalogic.gosh.Script;
import java.io.*;

/**
 * <code>NullScriptStore</code> does no-ops on save/load.
 *
 * @author <a href="mailto:Brendan@Gosh"></a>
 * @version 1.0
 */
class NullScriptStore extends BaseScriptStore {

    public String getName() {
        return "<<null-store>>";
    }

    public long getTimeStamp() {
        return 0;
    }

    public Script in() throws ScriptStore.CheckpointException {
        return null;
    }

    public PrintWriter out(boolean append) throws ScriptStore.CheckpointException {
        return null;
    }

    BaseScriptStore delete() throws ScriptStore.CheckpointException {
        return null;
    }

    public void wipeClean() {
        ;
    }
}
