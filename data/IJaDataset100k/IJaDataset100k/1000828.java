package net.sf.gilead.adapter4appengine.client;

import java.util.Arrays;
import net.sf.gilead.adapter4appengine.server.domain.TestEntity2;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Link;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Test emulation througt GWT serialization process.
 * @author bruno.marchesson
 *
 */
public class GwtEmulationTest extends GWTTestCase {

    /**
	 * Get module name
	 */
    public String getModuleName() {
        return "net.sf.gilead.adapter4appengine.Test";
    }

    /**
	 * Test clone user and messages in stateful mode
	 */
    public void testEmulationSupport() {
        final byte[] contents = new byte[128];
        Arrays.fill(contents, (byte) 15);
        final String linkContent = "test clone user and messages in stateful mode";
        final TestEntity2 entity = new TestEntity2();
        entity.setTextBytes(new Blob(contents));
        entity.setLink(new Link(linkContent));
        entity.setKey(null);
        Timer timer = new Timer() {

            public void run() {
                TransfertServiceAsync remoteService = (TransfertServiceAsync) GWT.create(TransfertService.class);
                ((ServiceDefTarget) remoteService).setServiceEntryPoint(GWT.getModuleBaseURL() + "/TransfertService");
                remoteService.sendAndReceiveNew(entity, new AsyncCallback<TestEntity2>() {

                    public void onFailure(Throwable caught) {
                        assertFalse(caught.toString(), false);
                        finishTest();
                    }

                    public void onSuccess(TestEntity2 result) {
                        assertNotNull(result);
                        assertNotNull(result.getTextBytes());
                        assertNotNull(result.getTextThumb());
                        assertNotNull(result.getLink());
                        assertEquals(result.getLink().getValue(), linkContent);
                        finishTest();
                    }
                });
            }
        };
        delayTestFinish(100000);
        timer.schedule(100);
    }
}
