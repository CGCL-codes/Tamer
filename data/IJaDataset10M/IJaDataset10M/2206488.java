package com.siemens.ct.exi.datatype.encoder;

import java.io.IOException;
import com.siemens.ct.exi.datatype.Datatype;
import com.siemens.ct.exi.exceptions.XMLParsingException;
import com.siemens.ct.exi.io.channel.EncoderChannel;
import com.siemens.ct.exi.util.datatype.XSDFloat;

/**
 * TODO Description
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20080718
 */
public class FloatDatatypeEncoder extends AbstractDatatypeEncoder implements DatatypeEncoder {

    private XSDFloat lastValidFloat = XSDFloat.newInstance();

    public FloatDatatypeEncoder(TypeEncoder typeEncoder) {
        super(typeEncoder);
    }

    public boolean isValid(Datatype datatype, String value) {
        try {
            lastValidFloat.parse(value);
            return true;
        } catch (XMLParsingException e) {
            return false;
        }
    }

    public void writeValue(EncoderChannel valueChannel, String uri, String localName) throws IOException {
        valueChannel.encodeFloat(lastValidFloat);
    }
}
