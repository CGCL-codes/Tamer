package com.bulksms;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.bulksms.BulksmsSmsClient.RoutingGroup;
import com.googlecode.sms4j.SmsClient;
import com.googlecode.sms4j.SmsException;

public class BulksmsSmsClientTest {

    @Test
    public void testWrongCredentials() throws IOException, SmsException {
        final SmsClient client = new BulksmsSmsClient("_test_username_", "_test_password_", BULKSMS_ROUTING_GROUP);
        try {
            client.send("test", "+972540000000", DEST_NUMBER, "hello from bulksms! (testWrongCredentials)");
            fail("expected exception");
        } catch (SmsException e) {
        }
    }

    @Test
    public void testSms() throws IOException, SmsException {
        final SmsClient client = new BulksmsSmsClient(BULKSMS_USERNAME, BULKSMS_PASSWORD, BULKSMS_ROUTING_GROUP);
        final String id = client.send(null, "+972540000000", DEST_NUMBER, "hello from bulksms! (testSms)");
        assertNotNull(id);
    }

    private static final String BULKSMS_USERNAME = "";

    private static final String BULKSMS_PASSWORD = "";

    private static final RoutingGroup BULKSMS_ROUTING_GROUP = RoutingGroup.ECONOMY;

    private static final String DEST_NUMBER = "";
}
