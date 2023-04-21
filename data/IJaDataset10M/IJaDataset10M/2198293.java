package com.erlang4j.internal.basicIo;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpMbox;
import com.erlang4j.api.exceptions.Erland4jExpectedServerNameOrPidException;
import com.erlang4j.api.exceptions.Erlang4jCannotReceiveException;

public class OtpMBoxBasicMailBox implements IBasicMailBox {

    private final OtpMbox mbox;

    private final String erlangVm;

    public OtpMBoxBasicMailBox(OtpMbox mbox, String erlangVm) {
        this.mbox = mbox;
        this.erlangVm = erlangVm;
    }

    @Override
    public OtpErlangObject receive() {
        try {
            return mbox.receive();
        } catch (Exception e) {
            throw new Erlang4jCannotReceiveException(e);
        }
    }

    @Override
    public OtpErlangObject receive(long timeout) {
        try {
            return mbox.receive(timeout);
        } catch (Exception e) {
            throw new Erlang4jCannotReceiveException(e);
        }
    }

    @Override
    public OtpErlangPid self() {
        return mbox.self();
    }

    @Override
    public void send(Object serverNameOrPid, OtpErlangObject object) {
        if (serverNameOrPid instanceof String) mbox.send((String) serverNameOrPid, erlangVm, object); else if (serverNameOrPid instanceof OtpErlangPid) mbox.send((OtpErlangPid) serverNameOrPid, object); else throw new Erland4jExpectedServerNameOrPidException(serverNameOrPid);
    }
}
