package hla.rti1516e;

import hla.rti1516e.exceptions.CouldNotDecode;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import java.io.Serializable;

/**
 * The factory is used only (outside RTI) to create ObjectClassHandle
 * received as an attribute value or parameter value.
 */
public interface ObjectClassHandleFactory extends Serializable {

    ObjectClassHandle decode(byte[] buffer, int offset) throws CouldNotDecode, FederateNotExecutionMember, NotConnected, RTIinternalError;
}
