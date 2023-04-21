package com.siemens.ct.exi.datatype.decoder;

import java.io.IOException;
import com.siemens.ct.exi.datatype.Datatype;
import com.siemens.ct.exi.io.channel.DecoderChannel;

/**
 * TODO Description
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20080718
 */
public class BinaryDatatypeDecoder extends AbstractDatatypeDecoder {

    public String decodeValue(TypeDecoder decoder, Datatype datatype, DecoderChannel dc, String namespaceURI, String localName) throws IOException {
        return dc.decodeBinaryAsString();
    }
}
