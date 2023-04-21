package net.sf.doolin.app.sc.engine.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Locale;
import net.sf.doolin.app.sc.engine.ClientID;
import net.sf.doolin.app.sc.engine.InstanceID;
import net.sf.doolin.app.sc.engine.ManagedClient;
import net.sf.doolin.app.sc.test.XClientResponse;
import net.sf.doolin.app.sc.test.XClientState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestDefaultInstanceClient {

    private ClientID cid;

    private InstanceID iid;

    @Before
    public void before() {
        this.iid = new InstanceID(10);
        this.cid = new ClientID(1, "test", Locale.ENGLISH, this.iid);
    }

    private DefaultInstanceClient<XClientState, XClientResponse> createClient() {
        DefaultInstanceClient<XClientState, XClientResponse> client = new DefaultInstanceClient<XClientState, XClientResponse>(this.cid);
        return client;
    }

    @Test
    public void testDisconnect() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        assertTrue(client.isConnected());
        client.disconnect();
        assertFalse(client.isConnected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDonwloadCheck() {
        DefaultInstanceClient<XClientState, XClientResponse> client = new DefaultInstanceClient<XClientState, XClientResponse>(this.cid);
        ClientID oid = new ClientID(2, "nok", Locale.ENGLISH, this.iid);
        client.setDownload(new XClientState("Game", oid, 0, "Test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDownload_NotReady() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.download();
    }

    @Test
    public void testDownload_OK() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.setDownload(new XClientState("Game", this.cid, 0, "State 0"));
        assertTrue(client.isDownloadReady());
        XClientState t = client.download();
        assertEquals(new XClientState("Game", this.cid, 0, "State 0"), t);
    }

    @Test
    public void testDownloadNOKAfterUpload() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.setDownload(new XClientState("Game", this.cid, 0, "State 0"));
        assertTrue(client.isDownloadReady());
        client.download();
        client.upload(new XClientResponse(this.cid, "Response 0"));
        assertFalse(client.isDownloadReady());
    }

    @Test
    public void testGetClientID() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        ClientID cid = client.getClientID();
        assertEquals(new ClientID(1, "test", Locale.ENGLISH, new InstanceID(10)), cid);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetUpload_NotReady() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.getUpload();
    }

    @Test
    public void testGetUpload_OK() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.setDownload(new XClientState("Game", this.cid, 0, "State 0"));
        client.upload(new XClientResponse(this.cid, "Response 0"));
        XClientResponse upload = client.getUpload();
        assertEquals(new XClientResponse(this.cid, "Response 0"), upload);
    }

    @Test
    public void testManaged_False() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        assertFalse(client.isManaged());
    }

    @Test(expected = IllegalStateException.class)
    public void testManaged_False_NoManagedClient() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.getManagedClient();
    }

    @Test
    public void testManaged_True() {
        @SuppressWarnings("unchecked") ManagedClient<XClientState, XClientResponse> managedClient = Mockito.mock(ManagedClient.class);
        DefaultInstanceClient<XClientState, XClientResponse> client = new DefaultInstanceClient<XClientState, XClientResponse>(managedClient);
        assertTrue(client.isManaged());
        assertNotNull(client.getManagedClient());
    }

    @Test
    public void testSeveralDownloadOKForManaged() {
        @SuppressWarnings("unchecked") ManagedClient<XClientState, XClientResponse> managedClient = mock(ManagedClient.class);
        when(managedClient.getClientID()).thenReturn(this.cid);
        DefaultInstanceClient<XClientState, XClientResponse> client = new DefaultInstanceClient<XClientState, XClientResponse>(managedClient);
        client.setDownload(new XClientState("Game", this.cid, 0, "State 0"));
        assertTrue(client.isDownloadReady());
        client.download();
        assertTrue(client.isDownloadReady());
    }

    @Test
    public void testUpload() {
        DefaultInstanceClient<XClientState, XClientResponse> client = createClient();
        client.setDownload(new XClientState("Game", this.cid, 0, "State 0"));
        client.upload(new XClientResponse(this.cid, "Response 0"));
        assertTrue(client.isUploadReady());
        assertFalse(client.isDownloadReady());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadCheck() {
        DefaultInstanceClient<XClientState, XClientResponse> client = new DefaultInstanceClient<XClientState, XClientResponse>(this.cid);
        ClientID oid = new ClientID(2, "nok", Locale.ENGLISH, this.iid);
        client.upload(new XClientResponse(oid, "Test"));
    }

    @Test
    public void testUploadManaged() {
        @SuppressWarnings("unchecked") ManagedClient<XClientState, XClientResponse> managedClient = mock(ManagedClient.class);
        when(managedClient.getClientID()).thenReturn(this.cid);
        DefaultInstanceClient<XClientState, XClientResponse> client = new DefaultInstanceClient<XClientState, XClientResponse>(managedClient);
        client.setDownload(new XClientState("Game", this.cid, 0, "State 0"));
        client.upload(new XClientResponse(this.cid, "Response 0"));
        assertTrue(client.isUploadReady());
        assertTrue(client.isDownloadReady());
    }
}
