package com.thoughtworks.jinterface.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpPeer;
import com.ericsson.otp.erlang.OtpSelf;

public class ClientNode {

    @Test
    public void add() throws Exception {
        OtpSelf cNode = new OtpSelf("clientnode", "cookie");
        OtpPeer sNode = new OtpPeer("servernode@byrned.corporate.thoughtworks.com");
        OtpConnection connection = cNode.connect(sNode);
        OtpErlangObject[] args = new OtpErlangObject[] { new OtpErlangLong(1), new OtpErlangLong(2) };
        connection.sendRPC("mathserver", "add", args);
        OtpErlangLong sum = (OtpErlangLong) connection.receiveRPC();
        assertEquals(3, sum.intValue());
    }
}
