package org.dbe.composer.wfengine.bpel.impl.lock;

import java.util.Set;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

/**
 * Noop variable locker, does nothing and is used when no serializable scopes
 * are present.
 */
public class SdlNoopVariableLocker implements ISdlVariableLocker {

    private static final Logger logger = Logger.getLogger(SdlNoopVariableLocker.class.getName());

    public boolean addExclusiveLock(Set aSetOfVariablePaths, String aOwnerXPath, ISdlVariableLockCallback aCallback) {
        logger.debug("addExclusiveLock() ownerXPath=" + aOwnerXPath);
        return true;
    }

    public boolean addSharedLock(Set aSetOfVariablePaths, String aOwnerXPath, ISdlVariableLockCallback aCallback) {
        logger.debug("addSharedLock() ownerXPath=" + aOwnerXPath);
        return true;
    }

    public void releaseLocks(String aOwner) throws SdlBusinessProcessException {
        logger.debug("releaseLocks() owner=" + aOwner);
    }

    public DocumentFragment getLockerData(ISdlVariableLockCallback aProcess) throws SdlBusinessProcessException {
        return null;
    }

    public void setLockerData(Node aNode, ISdlVariableLockCallback aProcess) throws SdlBusinessProcessException {
    }
}
