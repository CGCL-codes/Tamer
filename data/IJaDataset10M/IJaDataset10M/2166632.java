package de.juwimm.cms.model;

import org.apache.log4j.Logger;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.util.SequenceHelper;

/**
 * @see de.juwimm.cms.model.LockHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: LockHbmDaoImpl.java 42 2009-02-27 23:14:16Z skulawik $
 */
public class LockHbmDaoImpl extends LockHbmDaoBase {

    private static Logger log = Logger.getLogger(LockHbmDaoImpl.class);

    @Override
    public LockHbm create(LockHbm lockHbm) {
        if (lockHbm.getLockId() == null || lockHbm.getLockId().intValue() == 0) {
            try {
                Integer id = SequenceHelper.getSequenceSession().getNextSequenceNumber("lock.lock_id");
                lockHbm.setLockId(id);
            } catch (Exception e) {
                log.error("Error creating primary key", e);
            }
        }
        if (lockHbm.getOwner() == null) {
            lockHbm.setOwner(getUserHbmDao().load(AuthenticationHelper.getUserName()));
        }
        return super.create(lockHbm);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findAll(final int transform) {
        return this.findAll(transform, "from de.juwimm.cms.model.LockHbm as lockHbm");
    }
}
