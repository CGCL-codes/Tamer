package com.abiquo.framework.messages;

import com.abiquo.framework.domain.types.DataType;
import com.abiquo.framework.xml.AbsXMLMessage;
import com.abiquo.framework.xml.MessageConstants;
import com.abiquo.framework.xml.events.messages.TransferResponseXMLEvent;

/**
 * <!-- Carry the Data represented on the InstanceName at DataTranserRequest --> <xs:complexType name =
 * "DataTransferResponse"> <xs:sequence> <xs:element name="Data" type="Data" minOccurs="1" maxOccurs="1"/>
 * </xs:sequence> </xs:complexType>
 * */
public class TransferResponse implements IGridMessage {

    private DataType data;

    public TransferResponse(DataType d) {
        data = d;
        data.setStream();
    }

    public DataType getData() {
        return data;
    }

    public int getType() {
        return MessageConstants.ET_TRANSFER_RESPONSE;
    }

    public AbsXMLMessage toXML() {
        return new TransferResponseXMLEvent(this);
    }
}
