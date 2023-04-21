package com.thoughtworks.ex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import junit.framework.TestCase;

public class WhereisTest extends TestCase {

    public void testWhereis() throws Exception {
        OtpNode node = new OtpNode("node");
        OtpMbox first = node.createMbox("first");
        assert "first".equals(first.getName());
        OtpMbox second = node.createMbox();
        assert first.self() == second.whereis("first");
        first.close();
        second.close();
        node.close();
    }

    public void testRegistered() throws Exception {
        OtpNode node = new OtpNode("node");
        node.createMbox("a");
        node.createMbox("b");
        List names = Arrays.asList(node.getNames());
        assert names.contains("a");
        assert names.contains("b");
        node.close();
    }
}
