package org.fcrepo.server.journal.managementmethods;

import org.fcrepo.common.Constants;
import org.fcrepo.server.errors.ServerException;
import org.fcrepo.server.journal.entry.JournalEntry;
import org.fcrepo.server.management.ManagementDelegate;

/**
 * Adapter class for Management.getNextPID()
 * 
 * @author Jim Blake
 */
public class GetNextPidMethod extends ManagementMethod {

    public GetNextPidMethod(JournalEntry parent) {
        super(parent);
    }

    @Override
    public Object invoke(ManagementDelegate delegate) throws ServerException {
        String[] pidList = delegate.getNextPID(parent.getContext(), parent.getIntegerArgument(ARGUMENT_NAME_NUM_PIDS), parent.getStringArgument(ARGUMENT_NAME_NAMESPACE));
        parent.setRecoveryValues(Constants.RECOVERY.PID_LIST.uri, pidList);
        return pidList;
    }
}
