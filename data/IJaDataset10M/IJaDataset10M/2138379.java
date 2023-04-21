package org.nakedobjects.nof.reflect.remote;

import java.util.Vector;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.reflect.NakedObjectAction;
import org.nakedobjects.noa.reflect.NakedObjectAction.Target;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.reflect.peer.ActionPeer;
import org.nakedobjects.nof.reflect.remote.data.Data;
import org.nakedobjects.nof.reflect.remote.data.Distribution;
import org.nakedobjects.nof.reflect.remote.data.DummyNullValue;
import org.nakedobjects.nof.reflect.remote.data.DummyReferenceData;
import org.nakedobjects.nof.reflect.remote.data.DummyResultData;
import org.nakedobjects.nof.reflect.remote.data.KnownObjects;
import org.nakedobjects.nof.reflect.remote.data.ObjectData;
import org.nakedobjects.nof.reflect.remote.data.ObjectEncoder;
import org.nakedobjects.nof.reflect.remote.data.ReferenceData;
import org.nakedobjects.nof.reflect.remote.data.ServerActionResultData;
import org.nakedobjects.nof.testsystem.TestPojo;
import org.nakedobjects.nof.testsystem.TestProxySystem;
import test.org.nakedobjects.object.reflect.DummyIdentifier;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ProxyAction_ExecuteLocallyTest extends TestCase {

    private ProxyAction proxy;

    private ActionPeer actionPeer;

    private ObjectEncoder encoder;

    private Distribution remoteInterface;

    private NakedObject target;

    private NakedReference param1;

    private TestProxySystem system;

    private DummyIdentifier identifier;

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.OFF);
        system = new TestProxySystem();
        system.init();
        actionPeer = createMock(ActionPeer.class);
        encoder = createMock(ObjectEncoder.class);
        remoteInterface = createMock(Distribution.class);
        proxy = new ProxyAction(actionPeer, remoteInterface, encoder);
        actionPeer.getIdentifier();
        identifier = new DummyIdentifier();
        expectLastCall().andStubReturn(identifier);
        target = system.createTransientTestObject();
        actionPeer.execute(eq(target), aryEq(new Naked[] { param1, param1 }));
        expectLastCall().andReturn(null);
    }

    public void testOnTransientExecutionIsPassedToDelegate() throws Exception {
        actionPeer.getTarget();
        expectLastCall().andStubReturn(null);
        replay(actionPeer, encoder, remoteInterface);
        proxy.execute(target, new Naked[] { param1, param1 });
        verify(actionPeer, encoder, remoteInterface);
    }

    public void testOnPersistentAnnotatedAsLocalIsPassedToDelegate() throws Exception {
        actionPeer.getTarget();
        expectLastCall().andStubReturn(NakedObjectAction.LOCAL);
        replay(actionPeer, encoder, remoteInterface);
        proxy.execute(target, new Naked[] { param1, param1 });
        verify(actionPeer, encoder, remoteInterface);
    }
}
