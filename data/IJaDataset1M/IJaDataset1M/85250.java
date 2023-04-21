package de.spindler.yahtzee.common.net.service;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ServiceResponseDecoder implements ProtocolDecoder {

    @Override
    public void decode(IoSession arg0, IoBuffer arg1, ProtocolDecoderOutput arg2) throws Exception {
    }

    @Override
    public void dispose(IoSession arg0) throws Exception {
    }

    @Override
    public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
    }
}
