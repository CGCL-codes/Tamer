package com.caimao.netflow.decoder.packetdecoder;

import com.caimao.netflow.packetdecoder.field.decoder.FieldTypeConstants;

public class HeaderDecoder4V5Packet extends HeadDecoderCommon {

    private static int[] headerFieldsDefinition = new int[] { FieldTypeConstants.VERSION, FieldTypeConstants.COUNT, FieldTypeConstants.SYSUPTIME, FieldTypeConstants.UNIX_SECS, FieldTypeConstants.UNIX_NSECS, FieldTypeConstants.FLOW_SEQUENCE, FieldTypeConstants.ENGINE_TYPE, FieldTypeConstants.ENGINE_ID, FieldTypeConstants.RESERVED };

    @Override
    public int[] getHeaderDefinition() {
        return headerFieldsDefinition;
    }

    private static HeaderDecoder4V5Packet instance = new HeaderDecoder4V5Packet();

    public static HeaderDecoder4V5Packet getInstance() {
        return instance;
    }
}
