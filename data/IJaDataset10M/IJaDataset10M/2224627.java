package com.erlang4j.internal.basicIo;

import java.io.IOException;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import com.erlang4j.api.exceptions.Erlang4jCannotConnectToVmException;

/**
 * Produces {@link IBasicMailBox} using OtpNode as the underlying communication object, so it is talking to a
 * local node.
 * 
 * @author Phil Rice
 * 
 */
public class OtpNodeBasicOperationsFactory implements IBasicMailBoxFactory {

    private OtpNode node;

    private final String erlangVm;

    public OtpNodeBasicOperationsFactory(String erlangVm, String cookie, String javaVm) {
        this.erlangVm = erlangVm;
        try {
            this.node = new OtpNode(javaVm, cookie);
            node.ping(erlangVm, 1000);
        } catch (IOException e) {
            throw new Erlang4jCannotConnectToVmException("using mail box", erlangVm, cookie, e);
        }
    }

    @Override
    public IBasicMailBox operations() {
        OtpMbox mBox = node.createMbox();
        return new OtpMBoxBasicMailBox(mBox, erlangVm);
    }
}
