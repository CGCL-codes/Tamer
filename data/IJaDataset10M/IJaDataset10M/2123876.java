package seqSamoa.services.abcast;

import seqSamoa.ProtocolStack;
import seqSamoa.Service;
import seqSamoa.exceptions.AlreadyExistingServiceException;

/**
 * <CODE>DynAbcast</CODE> delivers all messages in the same order on all
 * processes member of the current view. (Total Ordered View Synchrony)
 */
public class DynAbcast extends Service<DynAbcastCallParameters, DynAbcastResponseParameters> {

    public DynAbcast(String name, ProtocolStack stack) throws AlreadyExistingServiceException {
        super(name, stack);
    }
}

;
