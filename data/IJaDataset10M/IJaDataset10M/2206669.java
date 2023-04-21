package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.InformationMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;

/**
 * Start time:23:59:59 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class InformationMessageImpl extends ISUPMessageImpl implements InformationMessage {

    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);

    static final int _INDEX_F_MessageType = 0;

    static final int _INDEX_F_InformationIndicators = 1;

    static final int _INDEX_O_CallingPartyCategory = 0;

    static final int _INDEX_O_CallingPartyNumber = 1;

    static final int _INDEX_O_CallReference = 2;

    static final int _INDEX_O_ConnectionRequest = 3;

    static final int _INDEX_O_ParameterCompatibilityInformation = 4;

    static final int _INDEX_O_NetworkSpecificFacility = 5;

    static final int _INDEX_O_EndOfOptionalParameters = 6;

    /**
	 * 
	 * @param source
	 * @throws ParameterException
	 */
    public InformationMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);
        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    public InformationIndicators getInformationIndicators() {
        return (InformationIndicators) super.f_Parameters.get(_INDEX_F_InformationIndicators);
    }

    public void setInformationIndicators(InformationIndicators v) {
        super.f_Parameters.put(_INDEX_F_InformationIndicators, v);
    }

    public CallingPartyCategory getCallingPartyCategory() {
        return (CallingPartyCategory) super.o_Parameters.get(_INDEX_O_CallingPartyCategory);
    }

    public void setCallingPartyCategory(CallingPartyCategory v) {
        super.o_Parameters.put(_INDEX_O_CallingPartyCategory, v);
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return (CallingPartyNumber) super.o_Parameters.get(_INDEX_O_CallingPartyNumber);
    }

    public void setCallingPartyNumber(CallingPartyNumber v) {
        super.o_Parameters.put(_INDEX_O_CallingPartyNumber, v);
    }

    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
    }

    public void setCallReference(CallReference v) {
        super.o_Parameters.put(_INDEX_O_CallReference, v);
    }

    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
    }

    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation v) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, v);
    }

    public ConnectionRequest getConnectionRequest() {
        return (ConnectionRequest) super.o_Parameters.get(_INDEX_O_ConnectionRequest);
    }

    public void setConnectionRequest(ConnectionRequest v) {
        super.o_Parameters.put(_INDEX_O_ConnectionRequest, v);
    }

    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);
    }

    public void setNetworkSpecificFacility(NetworkSpecificFacility v) {
        super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, v);
    }

    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index) throws ParameterException {
        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);
        if (b.length - index > 1) {
            try {
                byte[] informationInd = new byte[2];
                informationInd[0] = b[index++];
                informationInd[1] = b[index++];
                InformationIndicators bci = parameterFactory.createInformationIndicators();
                ((AbstractISUPParameter) bci).decode(informationInd);
                this.setInformationIndicators(bci);
            } catch (Exception e) {
                throw new ParameterException("Failed to parse BackwardCallIndicators due to: ", e);
            }
            return index - localIndex;
        } else {
            throw new IllegalArgumentException("byte[] must have atleast 2 octets");
        }
    }

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex) throws ParameterException {
        throw new UnsupportedOperationException();
    }

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode) throws ParameterException {
        switch((int) parameterCode) {
            case CallingPartyCategory._PARAMETER_CODE:
                CallingPartyCategory RS = parameterFactory.createCallingPartyCategory();
                ((AbstractISUPParameter) RS).decode(parameterBody);
                this.setCallingPartyCategory(RS);
                break;
            case CallingPartyNumber._PARAMETER_CODE:
                CallingPartyNumber x = parameterFactory.createCallingPartyNumber();
                ((AbstractISUPParameter) x).decode(parameterBody);
                this.setCallingPartyNumber(x);
                break;
            case ConnectionRequest._PARAMETER_CODE:
                ConnectionRequest z = parameterFactory.createConnectionRequest();
                ((AbstractISUPParameter) z).decode(parameterBody);
                this.setConnectionRequest(z);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation cc = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) cc).decode(parameterBody);
                this.setParameterCompatibilityInformation(cc);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility v = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) v).decode(parameterBody);
                this.setNetworkSpecificFacility(v);
                break;
            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }
    }

    public MessageType getMessageType() {
        return _MESSAGE_TYPE;
    }

    protected int getNumberOfMandatoryVariableLengthParameters() {
        return 0;
    }

    public boolean hasAllMandatoryParameters() {
        return getInformationIndicators() != null;
    }

    protected boolean optionalPartIsPossible() {
        return true;
    }
}
