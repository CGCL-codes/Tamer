package org.mobicents.protocols.ss7.isup.impl.message;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.NetworkResourceManagementMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;

/**
 * Start time:00:11:10 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class NetworkResourceManagementMessageImpl extends ISUPMessageImpl implements NetworkResourceManagementMessage {

    /**
	 * 	
	 * @param source
	 * @throws ParameterException
	 */
    public NetworkResourceManagementMessageImpl() {
    }

    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index) throws ParameterException {
        return 0;
    }

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex) throws ParameterException {
    }

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode) throws ParameterException {
    }

    public MessageType getMessageType() {
        return null;
    }

    protected int getNumberOfMandatoryVariableLengthParameters() {
        return 0;
    }

    public boolean hasAllMandatoryParameters() {
        throw new UnsupportedOperationException();
    }

    protected boolean optionalPartIsPossible() {
        throw new UnsupportedOperationException();
    }
}
