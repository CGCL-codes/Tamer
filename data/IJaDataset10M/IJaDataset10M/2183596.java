package mnemosyne.core;

import mnemosyne.guid.Guid;
import mnemosyne.guid.GuidGenerator;
import mnemosyne.lock.NullLock;

/**
 * @version $Id: DefaultPersistentObjectFactory.java,v 1.2 2004/09/01 14:58:05 charlesblaxland Exp $
 */
public class DefaultPersistentObjectFactory implements PersistentObjectFactory {

    private GuidGenerator guidGenerator;

    private VersionManager versionMgr;

    private Enhancer enhancer;

    public DefaultPersistentObjectFactory(GuidGenerator guidGenerator, VersionManager versionMgr, AopSystem aopSystem) {
        this.guidGenerator = guidGenerator;
        this.versionMgr = versionMgr;
        this.enhancer = new EnhancerImpl(this, aopSystem);
    }

    public PersistentObject createPersistentObject() {
        return createPersistentObject(guidGenerator.generateGUID());
    }

    public PersistentObject createPersistentObject(Guid guid) {
        return new PersistentObjectImpl(guid, versionMgr, enhancer, new NullLock());
    }
}
