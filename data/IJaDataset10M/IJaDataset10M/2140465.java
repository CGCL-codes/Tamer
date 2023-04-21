package ca.gc.drdc_rddc.atlantic.hla;

import hla.rti13.java1.MemoryExhausted;
import hla.rti13.java1.RTIinternalError;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jmock.Mock;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.StubSequence;
import ca.gc.drdc_rddc.atlantic.application.ModelFederate;
import ca.gc.drdc_rddc.atlantic.dmso.RTInterface;

public class MockModelFederate extends ModelFederate {

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(MockModelFederate.class);

    /** Mock RTI object for testing. */
    public Mock mockRti;

    public MockModelFederate() {
        super();
        mockRti = new Mock(RTInterface.class);
        logger.debug("mockRti = new Mock(RTInterface.class);");
        logger.debug("Setting up mock stubs...");
        int maxHandles = 500;
        List objClsHandles = new ArrayList(maxHandles);
        List intrClsHandles = new ArrayList(maxHandles);
        List attrHandles = new ArrayList(maxHandles);
        List parmHandles = new ArrayList(maxHandles);
        for (int i = 0; i < maxHandles; i++) {
            objClsHandles.add(new ReturnStub(new Integer(i + 1000)));
            intrClsHandles.add(new ReturnStub(new Integer(i + 2000)));
            attrHandles.add(new ReturnStub(new Integer(i + 3000)));
            parmHandles.add(new ReturnStub(new Integer(i + 4000)));
        }
        mockRti.stubs().method("getObjectClassHandle").withAnyArguments().will(new StubSequence(objClsHandles));
        mockRti.stubs().method("getInteractionClassHandle").withAnyArguments().will(new StubSequence(intrClsHandles));
        mockRti.stubs().method("getAttributeHandle").withAnyArguments().will(new StubSequence(attrHandles));
        mockRti.stubs().method("getParameterHandle").withAnyArguments().will(new StubSequence(parmHandles));
        mockRti.stubs().method("tick").withAnyArguments();
        mockRti.stubs().method("createFederationExecution").withAnyArguments();
        mockRti.stubs().method("joinFederationExecution").withAnyArguments().will(new ReturnStub(new Integer(1)));
        mockRti.stubs().method("resignFederationExecution").withAnyArguments();
        mockRti.stubs().method("destroyFederationExecution").withAnyArguments();
        mockRti.stubs().method("subscribeObjectClassAttributes").withAnyArguments();
        mockRti.stubs().method("publishObjectClass").withAnyArguments();
        mockRti.stubs().method("publishInteractionClass").withAnyArguments();
        mockRti.stubs().method("subscribeInteractionClass").withAnyArguments();
        mockRti.stubs().method("enableTimeConstrained").withAnyArguments();
        mockRti.stubs().method("enableTimeRegulation").withAnyArguments();
        logger.debug("Mock stubs set.");
    }

    public void buildDummyModel() {
        HLAObjectClass oCls;
        HLAObject obj;
        oCls = model.lookupObjectClass("CompositeEntity.Air");
        oCls.startRegisteration();
        mockRti.stubs().method("requestObjectAttributeValueUpdate").withAnyArguments();
        obj = oCls.discoverObject(1234, "aircraft1");
        ValueMap vm;
        vm = new ValueHashMap();
        vm.set("X", 1.2);
        vm.set("Y", 2.3);
        vm.set("Z", 3.4);
        obj.set("Position", vm);
        oCls = model.lookupObjectClass("CompositeEntity.SeaSurface");
        oCls.startRegisteration();
        obj = oCls.discoverObject(1235, "ship1");
    }

    /**
	 * Entry to the federate startup.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        ModelFederate federate = new MockModelFederate();
        federate.launch(args);
    }
}
