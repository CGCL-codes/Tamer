package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

/**
*

 time        [2] OCTET STRING (SIZE(2)), -- HH: MM, BCD coded 

--  Indicates the variable part of the message. Time is BCD encoded. 
--  The most significant hours digit occupies bits 0-3 of the first octet, and the least 
--  significant digit occupies bits 4-7 of the first octet. The most significant minutes digit 
--  occupies bits 0-3 of the second octet, and the least significant digit occupies bits 4-7 
--  of the second octet. 

* 
* @author sergey vetyutnev
* 
*/
public interface VariablePartTime {

    public byte[] getData();

    public int getHour();

    public int getMinute();
}
