package org.ofbiz.shark.mapping;

import org.enhydra.shark.api.internal.working.CallbackUtilities;
import org.enhydra.shark.api.internal.scriptmappersistence.ScriptMappingManager;
import org.enhydra.shark.api.RootException;
import org.enhydra.shark.api.ScriptMappingTransaction;
import org.enhydra.shark.api.TransactionException;

/**
 * Shark Script Mappings Implementation
 */
public class EntityScriptMappingMgr implements ScriptMappingManager {

    public static final String module = EntityScriptMappingMgr.class.getName();

    protected CallbackUtilities callBack = null;

    public void configure(CallbackUtilities callbackUtilities) throws RootException {
        this.callBack = callbackUtilities;
    }

    public ScriptMappingTransaction getScriptMappingTransaction() throws TransactionException {
        return null;
    }
}
