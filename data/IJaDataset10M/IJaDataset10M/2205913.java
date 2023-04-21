package seqSamoa.services.gms;

import seqSamoa.ProtocolStack;
import seqSamoa.Service;
import seqSamoa.exceptions.AlreadyExistingServiceException;

/**
 * <CODE>ManageView</CODE> allows to manage a view, i.e. to add or remove
 * process to a view.
 */
public class ManageView extends Service<ManageViewCallParameters, ManageViewResponseParameters> {

    public ManageView(String name, ProtocolStack stack) throws AlreadyExistingServiceException {
        super(name, stack, false, true);
    }
}

;
