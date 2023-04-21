package com.vayoodoot.message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Aug 17, 2007
 * Time: 9:00:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class VDMessageDecoderFactory implements ProtocolCodecFactory {

    private static VDMessageDecoder messageDecoder;

    private static VDMessageEncoder messageEncoder;

    public ProtocolEncoder getEncoder() throws Exception {
        if (messageEncoder == null) {
            messageEncoder = new VDMessageEncoder();
        }
        return messageEncoder;
    }

    public ProtocolDecoder getDecoder() throws Exception {
        if (messageDecoder == null) {
            messageDecoder = new VDMessageDecoder();
        }
        return messageDecoder;
    }
}
