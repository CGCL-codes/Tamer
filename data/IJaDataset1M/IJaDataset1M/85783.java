package net.kano.joscar.rvcmd.addins;

import net.kano.joscar.rvcmd.AbstractRejectRvCmd;
import net.kano.joscar.snaccmd.CapabilityBlock;
import net.kano.joscar.snaccmd.icbm.RecvRvIcbm;

/**
 * A rendezvous command used to indicate that the user has denied an
 * {@linkplain AddinsReqRvCmd add-in invitation}.
 */
public class AddinsRejectRvCmd extends AbstractRejectRvCmd {

    /**
     * Creates a new add-in invitation rejection RV command from the given
     * incoming add-in rejection RV ICBM.
     *
     * @param icbm an incoming add-in invitation rejection RV command
     */
    public AddinsRejectRvCmd(RecvRvIcbm icbm) {
        super(icbm);
    }

    /**
     * Creates a new outgoing add-in invitation rejection RV command with the
     * given rejection code.
     *
     * @param rejectionCode a "rejection code," like {@link
     *        #REJECTCODE_CANCELLED}
     */
    public AddinsRejectRvCmd(int rejectionCode) {
        super(CapabilityBlock.BLOCK_ADDINS, rejectionCode);
    }
}
