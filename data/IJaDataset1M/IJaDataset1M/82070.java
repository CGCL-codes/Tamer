package ch.iserver.ace.test.jupiter;

import junit.framework.TestCase;
import org.easymock.MockControl;
import ch.iserver.ace.Operation;
import ch.iserver.ace.algorithm.Algorithm;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.algorithm.TransformationException;
import ch.iserver.ace.test.AlgorithmTestFactory;
import ch.iserver.ace.test.ReceptionNode;
import ch.iserver.ace.test.RelayNode;
import ch.iserver.ace.test.StartNode;

/**
 * Test case for the <code>NWayExecuteVisitor</code> class.
 */
public class NWayExecuteVisitorTest extends TestCase {

    /**
	 * Tests the process of visiting a start node.
	 */
    public void testVisitStartNode() {
        MockControl control = MockControl.createControl(AlgorithmTestFactory.class);
        MockControl algoCtrl1 = MockControl.createControl(Algorithm.class);
        MockControl algoCtrl2 = MockControl.createControl(Algorithm.class);
        AlgorithmTestFactory factory = (AlgorithmTestFactory) control.getMock();
        Algorithm algo1 = (Algorithm) algoCtrl1.getMock();
        Algorithm algo2 = (Algorithm) algoCtrl2.getMock();
        StartNode s = new StartNode("0", "abc", 1);
        factory.createAlgorithm(0, Boolean.FALSE);
        control.setReturnValue(algo1);
        factory.createAlgorithm(0, Boolean.TRUE);
        control.setReturnValue(algo2);
        NWayExecuteVisitor visitor = new NWayExecuteVisitor(factory);
        control.replay();
        algoCtrl1.replay();
        algoCtrl2.replay();
        s.accept(visitor);
        control.verify();
        algoCtrl1.verify();
        algoCtrl2.verify();
        assertTrue(visitor.getAlgorithm("0") == algo1);
        assertTrue(visitor.getServerAlgorithm("0") == algo2);
    }

    /**
	 * Tests if the execute visitor visits a relay node correctly. This
	 * includes calling receiveRequest on the algorithm of the incoming
	 * site, calling generateRequest on all other server algorithms
	 * and calling setRequest on all remote successors.
	 * @throws TransformationException 
	 */
    public void testVisitRelayNode() throws TransformationException {
        MockControl[] algoCtrl = new MockControl[] { MockControl.createControl(Algorithm.class), MockControl.createControl(Algorithm.class), MockControl.createControl(Algorithm.class) };
        Algorithm[] algo = new Algorithm[] { (Algorithm) algoCtrl[0].getMock(), (Algorithm) algoCtrl[1].getMock(), (Algorithm) algoCtrl[2].getMock() };
        MockControl[] nodeCtrl = new MockControl[] { MockControl.createControl(ReceptionNode.class), MockControl.createControl(ReceptionNode.class) };
        ReceptionNode[] node = new ReceptionNode[] { (ReceptionNode) nodeCtrl[0].getMock(), (ReceptionNode) nodeCtrl[1].getMock() };
        RelayNode relay = new RelayNode("server", "1", "10");
        Request req = new TestRequest(0, null);
        relay.setRequest("0", req);
        relay.addRemoteSuccessor(node[0]);
        relay.addRemoteSuccessor(node[1]);
        algo[0].receiveRequest(req);
        algoCtrl[0].setReturnValue(null);
        algo[1].generateRequest(null);
        algoCtrl[1].setReturnValue(null);
        algo[2].generateRequest(null);
        algoCtrl[2].setReturnValue(null);
        node[0].getParticipantId();
        nodeCtrl[0].setReturnValue("1");
        node[0].setRequest("0", null);
        node[1].getParticipantId();
        nodeCtrl[1].setReturnValue("2");
        node[1].setRequest("0", null);
        NWayExecuteVisitor visitor = new NWayExecuteVisitor(null);
        visitor.setServerAlgorithm("0", algo[0]);
        visitor.setServerAlgorithm("1", algo[1]);
        visitor.setServerAlgorithm("2", algo[2]);
        algoCtrl[0].replay();
        algoCtrl[1].replay();
        algoCtrl[2].replay();
        nodeCtrl[0].replay();
        nodeCtrl[1].replay();
        relay.accept(visitor);
        algoCtrl[0].verify();
        algoCtrl[1].verify();
        algoCtrl[2].verify();
        nodeCtrl[0].verify();
        nodeCtrl[1].verify();
    }

    /**
	 * Private helper class for requests.
	 */
    private static class TestRequest implements Request {

        private int siteId;

        private Operation operation;

        public TestRequest(int siteId, Operation operation) {
            this.siteId = siteId;
            this.operation = operation;
        }

        public Operation getOperation() {
            return operation;
        }

        public int getSiteId() {
            return siteId;
        }

        public Timestamp getTimestamp() {
            return null;
        }
    }
}
