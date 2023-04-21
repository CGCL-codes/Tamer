package net.sourceforge.jradiusclient.packets;

import net.sourceforge.jradiusclient.exception.InvalidParameterException;

/**
 * Released under the LGPL<BR>
 * An AccountingRequest to perform an Update Accounting Request
 * @author <a href="mailto:bloihl@users.sourceforge.net">Robert J. Loihl</a>
 * @version $Revision: 1.1 $
 */
public class UpdateAccountingRequest extends AccountingRequest {

    /**
     * Constructs a basic UpdateAccountingRequest
     * @param userName the users name to send with this AccountingRequest
     * @param sessionId the identifier used to correlate accounting requests
     * @exception net.sourceforge.jradiusclient.exception.InvalidParameterException
     */
    public UpdateAccountingRequest(final String userName, final String sessionId) throws InvalidParameterException {
        super(userName, UPDATE_ACCOUNTING_SERVICE_TYPE, sessionId);
    }
}
