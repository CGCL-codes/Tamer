package com.thoughtworks.hangar19;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

public class RegistrationTest {

    @Test
    public void registered() throws IOException {
        OtpNode b = new OtpNode("b", Constants.COOKIE);
        OtpMbox mail = b.createMbox();
        mail.registerName("foo");
        Assert.assertEquals("foo", mail.getName());
        Assert.assertEquals(1, mail.getNames().length);
        Assert.assertEquals("foo", mail.getNames()[0]);
        mail.close();
    }
}
