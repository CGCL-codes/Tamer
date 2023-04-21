package com.google.code.facebookapi;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AttainSessionTest {

    @Test
    public void attainSession() throws Exception {
        JSONObject session = FacebookSessionTestUtils.attainSessionRaw2();
        Assert.assertNotNull(session);
    }
}
