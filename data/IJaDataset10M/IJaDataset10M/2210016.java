package org.indi.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.indi.client.IndiClient;
import org.indi.clientmessages.GetProperties;
import org.indi.objects.BlobVector;
import org.indi.objects.NumberVector;
import org.indi.objects.Permission;
import org.indi.objects.Standard;
import org.indi.objects.State;
import org.indi.objects.Switch;
import org.indi.objects.SwitchRule;
import org.indi.objects.SwitchVector;
import org.indi.objects.TextVector;
import org.indi.objects.Vector;
import org.indi.reactor.Reactor;
import org.indi.server.BasicDevice;
import org.indi.server.CallbackKey;
import org.indi.server.Device;
import org.indi.server.DeviceConnection;
import org.indi.server.IndiServer;
import org.indi.server.VectorCallback;
import org.junit.After;

class CallbackDevice extends BasicDevice {

    public boolean onNewCalled = false;

    public boolean onNewCalledByInnerClass = false;

    /**
     * The Vector hosting the connect /disconnect switch
     */
    private final SwitchVector powerswitch;

    /**
     * the switch to connect/disconnect the device
     */
    private Switch connectswitch = null;

    /**
     * the switch to connect/disconnect the device
     */
    private Switch disconnectswitch = null;

    /**
     * the name of the device
     */
    private final String name = "Simple Device";

    private CallbackKey key;

    public CallbackDevice() {
        super();
        this.powerswitch = new SwitchVector(this.name, Standard.Vector.CONNECTION, "Main Control", State.Idle, Permission.ReadWrite, SwitchRule.OneOfMany, 0);
        this.connectswitch = new Switch(Standard.Property.CONNECT, Switch.State.Off);
        this.powerswitch.add(this.connectswitch);
        this.disconnectswitch = new Switch(Standard.Property.DISCONNECT, Switch.State.On);
        this.powerswitch.add(disconnectswitch);
        key = register(powerswitch, new VectorCallback() {

            public void onNew(Vector vector) {
                onNewCalledByInnerClass = true;
            }
        });
    }

    public void onNew(SwitchVector vector) {
        onNewCalled = true;
    }

    public void onNew(TextVector vector) {
    }

    public void onNew(NumberVector vector) {
    }

    public void onNew(BlobVector vector) {
    }

    public void onGetProperties(GetProperties o) {
    }

    public void onTimer() {
    }

    public void unregister() {
        unregister(key);
    }

    public void registerRegistrator() {
        register(powerswitch, new VectorCallback() {

            public void onNew(Vector vector) {
                CallbackKey k = register(powerswitch, new VectorCallback() {

                    public void onNew(Vector vector) {
                    }
                });
                unregister(key);
            }
        });
    }
}

public class TestCallback extends TestCase {

    Reactor r;

    IndiClient c;

    PatchedIndiServer s;

    CallbackDevice d;

    public void setUp() throws Exception {
        r = new Reactor();
        s = new PatchedIndiServer(r);
        d = new CallbackDevice();
        s.addDevice(d);
        int i;
        for (i = 0; i < 10; i++) {
            r.handleEvents(10);
        }
        c = new IndiClient(r);
        for (i = 0; i < 10; i++) {
            r.handleEvents(10);
        }
    }

    public void testCallback1() throws Exception {
        c.sendString("<newSwitchVector\n" + "  device='Simple Device'\n" + "  name='CONNECTION'>\n" + "  <oneSwitch\n" + "    name='CONNECT'>\n" + "      On\n" + "  </oneSwitch>\n" + "</newSwitchVector>\n");
        for (int i = 0; i < 10; i++) {
            r.handleEvents(10);
        }
        if (!d.onNewCalled) {
            throw new RuntimeException();
        }
        if (!d.onNewCalledByInnerClass) {
            throw new RuntimeException();
        }
        d.unregister();
        d.onNewCalledByInnerClass = false;
        c.sendString("<newSwitchVector\n" + "  device='Simple Device'\n" + "  name='CONNECTION'>\n" + "  <oneSwitch\n" + "    name='CONNECT'>\n" + "      On\n" + "  </oneSwitch>\n" + "</newSwitchVector>\n");
        for (int i = 0; i < 10; i++) {
            r.handleEvents(10);
        }
        if (d.onNewCalledByInnerClass) {
            throw new RuntimeException();
        }
        if (!s.getDevices().contains(d)) {
            throw new RuntimeException();
        }
        ;
        d.registerRegistrator();
        c.sendString("<newSwitchVector\n" + "  device='Simple Device'\n" + "  name='CONNECTION'>\n" + "  <oneSwitch\n" + "    name='CONNECT'>\n" + "      On\n" + "  </oneSwitch>\n" + "</newSwitchVector>\n");
        for (int i = 0; i < 10; i++) {
            r.handleEvents(10);
        }
        if (!s.getDevices().contains(d)) {
            throw new RuntimeException();
        }
        ;
    }

    @After
    public void tearDown() throws Exception {
        s.shutDown();
        c.shutDown();
    }
}
