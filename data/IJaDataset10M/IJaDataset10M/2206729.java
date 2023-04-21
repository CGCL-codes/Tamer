package org.avis.net.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.avis.common.Notification;
import org.avis.net.messages.ConfConn;
import org.avis.net.messages.ConnRply;
import org.avis.net.messages.ConnRqst;
import org.avis.net.messages.DisconnRply;
import org.avis.net.messages.DisconnRqst;
import org.avis.net.messages.Nack;
import org.avis.net.messages.NotifyDeliver;
import org.avis.net.messages.NotifyEmit;
import org.avis.net.messages.SecRqst;
import org.avis.net.messages.SubAddRqst;
import org.avis.net.messages.SubDelRqst;
import org.avis.net.messages.SubModRqst;
import org.avis.net.messages.SubRply;
import org.avis.net.messages.TestConn;
import org.avis.net.messages.UNotify;
import org.avis.net.security.Key;
import org.avis.net.security.KeyScheme;
import org.avis.net.security.Keys;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.avis.net.common.ConnectionOptionSet.CONNECTION_OPTION_SET;
import static org.avis.net.security.KeyScheme.SHA1_PRODUCER;
import static org.avis.net.security.Keys.EMPTY_KEYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the router server.
 * 
 * @author Matthew Phillips
 */
public class JUTestServer {

    static final int PORT = 29170;

    private Server server;

    private Random random;

    @Before
    public void setup() {
        random = new Random();
    }

    @After
    public void tearDown() {
        if (server != null) server.close();
    }

    @Test
    public void connect() throws Exception {
        server = new Server(PORT);
        SimpleClient client = new SimpleClient();
        ConnRqst connRqst = new ConnRqst(4, 0);
        client.send(connRqst);
        ConnRply reply = (ConnRply) client.receive();
        assertEquals(connRqst.xid, reply.xid);
        DisconnRqst disconnRqst = new DisconnRqst();
        client.send(disconnRqst);
        DisconnRply disconnRply = (DisconnRply) client.receive();
        assertEquals(disconnRqst.xid, disconnRply.xid);
        client.close();
        server.close();
    }

    /**
   * Test connection options.
   * 
   * @see JUTestConnectionOptions
   */
    @Test
    public void connectionOptions() throws Exception {
        server = new Server(PORT);
        SimpleClient client = new SimpleClient("localhost", PORT);
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("Packet.Max-Length", 1024);
        options.put("Subscription.Max-Count", 16);
        options.put("Subscription.Max-Length", 1024);
        options.put("Attribute.Opaque.Max-Length", 2048 * 1024);
        options.put("TCP.Send-Immediately", 1);
        options.put("Bogus", "not valid");
        ConnRply connReply = client.connect(options);
        assertEquals(Integer.MAX_VALUE, connReply.options.get("Attribute.Opaque.Max-Length"));
        assertEquals(1, connReply.options.get("TCP.Send-Immediately"));
        assertEquals(1024, connReply.options.get("Packet.Max-Length"));
        assertEquals(16, connReply.options.get("Subscription.Max-Count"));
        assertEquals(1024, connReply.options.get("Subscription.Max-Length"));
        assertNull(connReply.options.get("Bogus"));
        SecRqst secRqst = new SecRqst();
        secRqst.addNtfnKeys = new Keys();
        secRqst.addNtfnKeys.add(SHA1_PRODUCER, new Key(new byte[1025]));
        client.send(secRqst);
        Nack nack = (Nack) client.receive();
        assertEquals(secRqst.xid, nack.xid);
        client.closeImmediately();
        client = new SimpleClient("localhost", PORT);
        client.connect(options);
        for (int i = 0; i < 16; i++) client.subscribe("Count == " + i);
        SubAddRqst subAddRqst = new SubAddRqst("Invalid == 1");
        client.send(subAddRqst);
        nack = (Nack) client.receive();
        assertEquals(subAddRqst.xid, nack.xid);
        client.close();
        client = new SimpleClient("localhost", PORT);
        client.connect(options);
        subAddRqst = new SubAddRqst(dummySubscription(2048));
        client.send(subAddRqst);
        nack = (Nack) client.receive();
        assertEquals(subAddRqst.xid, nack.xid);
        client.close();
        int maxConnKeys = CONNECTION_OPTION_SET.getMaxValue("Connection.Max-Keys");
        int maxSubKeys = CONNECTION_OPTION_SET.getMaxValue("Subscription.Max-Keys");
        options = new HashMap<String, Object>();
        options.put("Connection.Max-Keys", maxConnKeys);
        options.put("Subscription.Max-Keys", maxSubKeys);
        client = new SimpleClient("localhost", PORT);
        client.connect(options);
        Keys keys = new Keys();
        for (int i = 0; i < maxConnKeys + 1; i++) keys.add(KeyScheme.SHA1_CONSUMER, new Key(randomBytes(128)));
        secRqst = new SecRqst(keys, EMPTY_KEYS, EMPTY_KEYS, EMPTY_KEYS);
        client.send(secRqst);
        nack = (Nack) client.receive();
        assertEquals(secRqst.xid, nack.xid);
        subAddRqst = new SubAddRqst("n == 1");
        subAddRqst.keys = keys;
        client.send(subAddRqst);
        nack = (Nack) client.receive();
        assertEquals(subAddRqst.xid, nack.xid);
        client.close();
        server.close();
    }

    /**
   * Use the simple client to run through a connect, subscribe, emit,
   * change sub, disconnect sequence.
   */
    @Test
    public void subscribe() throws Exception {
        server = new Server(PORT);
        SimpleClient client = new SimpleClient();
        client.connect();
        SubAddRqst subAddRqst = new SubAddRqst("number == 1");
        client.send(subAddRqst);
        SubRply subReply = (SubRply) client.receive();
        assertEquals(subAddRqst.xid, subReply.xid);
        subAddRqst = new SubAddRqst("(1 + 1");
        client.send(subAddRqst);
        Nack nackReply = (Nack) client.receive();
        assertEquals(Nack.PARSE_ERROR, nackReply.error);
        Map<String, Object> ntfn = new HashMap<String, Object>();
        ntfn.put("name", "foobar");
        ntfn.put("number", 1);
        client.send(new NotifyEmit(ntfn));
        NotifyDeliver notifyDeliver = (NotifyDeliver) client.receive();
        assertEquals(0, notifyDeliver.secureMatches.length);
        assertEquals(1, notifyDeliver.insecureMatches.length);
        assertEquals(subReply.subscriptionId, notifyDeliver.insecureMatches[0]);
        assertEquals("foobar", notifyDeliver.attributes.get("name"));
        assertEquals(1, notifyDeliver.attributes.get("number"));
        ntfn = new HashMap<String, Object>();
        ntfn.put("name", "foobar");
        ntfn.put("number", 2);
        client.send(new NotifyEmit(ntfn));
        SubModRqst subModRqst = new SubModRqst(subReply.subscriptionId, "number == 2");
        client.send(subModRqst);
        subReply = (SubRply) client.receive();
        assertEquals(subReply.xid, subModRqst.xid);
        assertEquals(subReply.subscriptionId, subModRqst.subscriptionId);
        SubDelRqst delRqst = new SubDelRqst(subReply.subscriptionId);
        client.send(delRqst);
        subReply = (SubRply) client.receive();
        assertEquals(subReply.subscriptionId, delRqst.subscriptionId);
        delRqst = new SubDelRqst(subReply.subscriptionId);
        client.send(delRqst);
        nackReply = (Nack) client.receive();
        assertEquals(Nack.NO_SUCH_SUB, nackReply.error);
        client.send(new TestConn());
        assertTrue(client.receive() instanceof ConfConn);
        client.close();
        server.close();
    }

    /**
   * Test multiple clients sending messages between each other.
   */
    @Test
    public void multiClient() throws Exception {
        server = new Server(PORT);
        SimpleClient client1 = new SimpleClient();
        client1.connect();
        SubAddRqst subAddRqst1 = new SubAddRqst("client == 1 || all == 1");
        client1.send(subAddRqst1);
        SubRply subReply1 = (SubRply) client1.receive();
        assertEquals(subAddRqst1.xid, subReply1.xid);
        SimpleClient client2 = new SimpleClient();
        client2.connect();
        SubAddRqst subAddRqst2 = new SubAddRqst("client == 2 || all == 1");
        client2.send(subAddRqst2);
        SubRply subReply2 = (SubRply) client2.receive();
        assertEquals(subAddRqst2.xid, subReply2.xid);
        Map<String, Object> ntfn = new HashMap<String, Object>();
        ntfn.put("client", 2);
        ntfn.put("payload", "hello from client 1");
        client1.send(new NotifyEmit(ntfn));
        NotifyDeliver client2Notify = (NotifyDeliver) client2.receive();
        assertEquals("hello from client 1", client2Notify.attributes.get("payload"));
        ntfn = new HashMap<String, Object>();
        ntfn.put("client", 1);
        ntfn.put("payload", "hello from client 2");
        client2.send(new NotifyEmit(ntfn));
        NotifyDeliver client1Notify = (NotifyDeliver) client1.receive();
        assertEquals("hello from client 2", client1Notify.attributes.get("payload"));
        ntfn = new HashMap<String, Object>();
        ntfn.put("all", 1);
        ntfn.put("payload", "hello all");
        client1.send(new NotifyEmit(ntfn));
        client2Notify = (NotifyDeliver) client2.receive();
        assertEquals("hello all", client2Notify.attributes.get("payload"));
        client1Notify = (NotifyDeliver) client1.receive();
        assertEquals("hello all", client1Notify.attributes.get("payload"));
        client1.close();
        client2.close();
        server.close();
    }

    /**
   * Test secure messaging using the producer key scheme. Other
   * schemes should really be tested, but the key matching logic for
   * all the schemes supported in the server is done by the security
   * tests, so not bothering for now.
   */
    @Test
    public void security() throws Exception {
        server = new Server(PORT);
        SimpleClient alice = new SimpleClient("alice");
        SimpleClient bob = new SimpleClient("bob");
        SimpleClient eve = new SimpleClient("eve");
        alice.connect();
        bob.connect();
        eve.connect();
        Key alicePrivate = new Key("alice private");
        Key alicePublic = alicePrivate.publicKeyFor(SHA1_PRODUCER);
        Keys aliceNtfnKeys = new Keys();
        aliceNtfnKeys.add(SHA1_PRODUCER, alicePrivate);
        Keys bobSubKeys = new Keys();
        bobSubKeys.add(SHA1_PRODUCER, alicePublic);
        Keys eveSubKeys = new Keys();
        eveSubKeys.add(SHA1_PRODUCER, new Key("Not alice's key").publicKeyFor(SHA1_PRODUCER));
        bob.subscribe("require (From-Alice)", bobSubKeys);
        eve.subscribe("require (From-Alice)", eveSubKeys);
        Notification ntfn = new Notification();
        ntfn.put("From-Alice", 1);
        alice.sendNotify(ntfn, aliceNtfnKeys);
        NotifyDeliver bobNtfn = (NotifyDeliver) bob.receive();
        assertEquals(1, bobNtfn.attributes.get("From-Alice"));
        try {
            NotifyDeliver eveNtfn = (NotifyDeliver) eve.receive(2000);
            assertEquals(1, eveNtfn.attributes.get("From-Alice"));
            fail("Eve foiled our super secret scheme");
        } catch (MessageTimeoutException ex) {
        }
        alice.close();
        bob.close();
        eve.close();
    }

    /**
   * Test changing subscription security settings on the fly.
   */
    @Test
    public void securitySubModify() throws Exception {
        server = new Server(PORT);
        SimpleClient alice = new SimpleClient("alice");
        SimpleClient bob = new SimpleClient("bob");
        alice.connect();
        bob.connect();
        Key alicePrivate = new Key("alice private");
        Key alicePublic = alicePrivate.publicKeyFor(SHA1_PRODUCER);
        Keys aliceNtfnKeys = new Keys();
        aliceNtfnKeys.add(SHA1_PRODUCER, alicePrivate);
        Keys bobSubKeys = new Keys();
        bobSubKeys.add(SHA1_PRODUCER, alicePublic);
        SubAddRqst subAddRqst = new SubAddRqst("require (From-Alice)", bobSubKeys);
        subAddRqst.acceptInsecure = true;
        bob.send(subAddRqst);
        SubRply subRply = (SubRply) bob.receive(SubRply.class);
        Notification ntfn = new Notification();
        ntfn.put("From-Alice", 1);
        alice.sendNotify(ntfn, aliceNtfnKeys);
        NotifyDeliver bobNtfn = (NotifyDeliver) bob.receive();
        assertEquals(1, bobNtfn.secureMatches.length);
        assertEquals(0, bobNtfn.insecureMatches.length);
        assertEquals(subRply.subscriptionId, bobNtfn.secureMatches[0]);
        alice.sendNotify(ntfn);
        bobNtfn = (NotifyDeliver) bob.receive();
        assertEquals(0, bobNtfn.secureMatches.length);
        assertEquals(1, bobNtfn.insecureMatches.length);
        assertEquals(subRply.subscriptionId, bobNtfn.insecureMatches[0]);
        SubModRqst subModRqst = new SubModRqst(subRply.subscriptionId, "");
        subModRqst.acceptInsecure = false;
        bob.send(subModRqst);
        bob.receive(SubRply.class);
        alice.sendNotify(ntfn);
        try {
            bobNtfn = (NotifyDeliver) bob.receive(2000);
            fail("Server delivered message insecurely");
        } catch (MessageTimeoutException ex) {
        }
        subModRqst = new SubModRqst(subRply.subscriptionId, "");
        subModRqst.delKeys = new Keys();
        subModRqst.delKeys.add(SHA1_PRODUCER, alicePublic);
        bob.send(subModRqst);
        bob.receive(SubRply.class);
        alice.sendNotify(ntfn, aliceNtfnKeys);
        try {
            bobNtfn = (NotifyDeliver) bob.receive(2000);
            fail("Server delivered message insecurely");
        } catch (MessageTimeoutException ex) {
        }
        alice.close();
        bob.close();
    }

    @Test
    public void unotify() throws Exception {
        server = new Server(PORT);
        SimpleClient client1 = new SimpleClient();
        SimpleClient client2 = new SimpleClient();
        client2.connect();
        client2.subscribe("number == 1");
        Notification ntfn = new Notification();
        ntfn.put("number", 1);
        ntfn.put("client", "client 1");
        client1.send(new UNotify(4, 0, ntfn));
        client1.close();
        NotifyDeliver reply = (NotifyDeliver) client2.receive();
        assertEquals("client 1", reply.attributes.get("client"));
        client2.close();
    }

    /**
   * Test handling of client that does Bad Things.
   */
    @Test
    public void badClient() throws Exception {
        server = new Server(PORT);
        SimpleClient client = new SimpleClient();
        SimpleClient badClient = new SimpleClient();
        client.connect();
        client.subscribe("number == 1");
        Map<String, Object> ntfn = new HashMap<String, Object>();
        ntfn.put("name", "foobar");
        ntfn.put("number", 1);
        badClient.send(new NotifyEmit(ntfn));
        try {
            client.receive(2000);
            fail("Server allowed client with no connection to notify");
        } catch (MessageTimeoutException ex) {
        }
        badClient.close();
        badClient = new SimpleClient();
        SecRqst secRqst = new SecRqst();
        badClient.send(secRqst);
        Nack nack = (Nack) badClient.receive();
        badClient.close();
        badClient = new SimpleClient();
        SubAddRqst subAddRqst = new SubAddRqst("require (hello)", EMPTY_KEYS);
        badClient.send(subAddRqst);
        nack = (Nack) badClient.receive();
        assertEquals(subAddRqst.xid, nack.xid);
        badClient.close();
        badClient = new SimpleClient();
        badClient.connect();
        ConnRqst connRqst = new ConnRqst(4, 0);
        badClient.send(connRqst);
        nack = (Nack) badClient.receive();
        assertEquals(connRqst.xid, nack.xid);
        badClient.closeImmediately();
        badClient = new SimpleClient();
        badClient.connect();
        SubModRqst subModRqst = new SubModRqst(123456, "");
        badClient.send(subModRqst);
        nack = (Nack) badClient.receive();
        assertEquals(subModRqst.xid, nack.xid);
        badClient.close();
        client.close();
    }

    private static String dummySubscription(int length) {
        StringBuilder str = new StringBuilder("i == -1");
        for (int i = 0; str.length() + 15 < length; i++) str.append(" && i == " + i);
        return str.toString();
    }

    private byte[] randomBytes(int length) {
        byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }
}
