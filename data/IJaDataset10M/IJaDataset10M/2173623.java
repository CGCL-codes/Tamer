package org.equanda.test.dm.server;

import org.equanda.persistence.Uoid;
import org.equanda.persistence.EquandaConstraintViolation;
import org.equanda.persistence.EquandaPersistenceException;
import org.equanda.test.dm.server.pm.DMRemove2Bean;
import org.equanda.test.dm.server.pm.DMRemove2Selector;
import static org.equanda.test.TestExceptionCodes.ECV_REMOVE2_NOT_ALOWED_STRING_TO_ADD;

/**
 * Description!!!
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
public class LinkableMediator extends LinkableMediatorBase {

    public static final String REMOVE2_NOT_ALOWED_STRING_TO_ADD = "REMOVE2_NOT_ALOWED_STRING_TO_ADD";

    public static final String REMOVE2_NOT_ALOWED_STRING_TO_REMOVE = "REMOVE2_NOT_ALOWED_STRING_TO_REMOVE";

    public void setEquandaLinkSingleBidirectional(Uoid value) throws EquandaPersistenceException {
        checkAdd(value);
        super.setEquandaLinkSingleBidirectional(value);
    }

    public void setEquandaLinkOtherSideCallFacade(Uoid value) throws EquandaPersistenceException {
        checkAdd(value);
        super.setEquandaLinkOtherSideCallFacade(value);
    }

    public void addEquandaLinkOtherSideCallFacadeMultiple(Uoid value) throws EquandaPersistenceException {
        checkAdd(value);
        super.addEquandaLinkOtherSideCallFacadeMultiple(value);
    }

    public void removeEquandaLinkOtherSideCallFacadeMultiple(Uoid value) throws EquandaPersistenceException {
        checkRemove(value);
        super.removeEquandaLinkOtherSideCallFacadeMultiple(value);
    }

    private void checkAdd(Uoid value) throws EquandaConstraintViolation {
        if (value != null) {
            DMRemove2Bean obj = DMRemove2Selector.selectId(em, value);
            if (REMOVE2_NOT_ALOWED_STRING_TO_ADD.equals(obj.getTheString())) {
                throw new EquandaConstraintViolation(ECV_REMOVE2_NOT_ALOWED_STRING_TO_ADD, REMOVE2_NOT_ALOWED_STRING_TO_ADD);
            }
        }
    }

    private void checkRemove(Uoid value) throws EquandaConstraintViolation {
        if (value != null) {
            DMRemove2Bean obj = DMRemove2Selector.selectId(em, value);
            if (REMOVE2_NOT_ALOWED_STRING_TO_REMOVE.equals(obj.getTheString())) {
                throw new EquandaConstraintViolation(ECV_REMOVE2_NOT_ALOWED_STRING_TO_ADD, REMOVE2_NOT_ALOWED_STRING_TO_ADD);
            }
        }
    }
}
