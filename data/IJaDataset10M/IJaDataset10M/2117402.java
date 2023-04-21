package org.fcrepo.server.journal.managementmethods;

import org.fcrepo.server.errors.ServerException;
import org.fcrepo.server.journal.entry.JournalEntry;
import org.fcrepo.server.management.ManagementDelegate;

/**
 * Adapter class for Management.purgeRelationship().
 * 
 * @author Jim Blake
 */
public class PurgeRelationshipMethod extends ManagementMethod {

    public PurgeRelationshipMethod(JournalEntry parent) {
        super(parent);
    }

    @Override
    public Object invoke(ManagementDelegate delegate) throws ServerException {
        return delegate.purgeRelationship(parent.getContext(), parent.getStringArgument(ARGUMENT_NAME_PID), parent.getStringArgument(ARGUMENT_NAME_RELATIONSHIP), parent.getStringArgument(ARGUMENT_NAME_OBJECT), parent.getBooleanArgument(ARGUMENT_NAME_IS_LITERAL), parent.getStringArgument(ARGUMENT_NAME_DATATYPE));
    }
}
