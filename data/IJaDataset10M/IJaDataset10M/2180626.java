package br.ufmg.dcc.modelo.gerenciamento.deteccaointruso;

import br.ufmg.dcc.modelo.gerenciamento.deteccaointruso.sinalradio.MaliciousNode;
import br.ufmg.dcc.modelo.gerenciamento.deteccaointruso.sinalradio.MaliciousProgram;
import com.wpjr.simulator.entity.Coordinates;
import com.wpjr.simulator.entity.Message;
import com.wpjr.simulator.entity.enums.MessageCode;
import com.wpjr.simulator.entity.enums.NodeEvent;
import com.wpjr.simulator.entity.enums.NodeState;
import com.wpjr.simulator.entity.enums.NodeSystemEvent;
import com.wpjr.simulator.entity.enums.NodeType;
import com.wpjr.simulator.entity.enums.ProgramType;
import com.wpjr.simulator.entity.node.Node;
import com.wpjr.simulator.system.SimulationException;
import com.wpjr.simulator.system.TestNodeSystemAbstract;

public class TestMaliciousNode extends TestNodeSystemAbstract {

    private MaliciousNode maliciousNode;

    private MaliciousProgram maliciousProgram;

    private Coordinates c3 = new Coordinates(2, 2);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        maliciousNode = new MaliciousNode(c3, NodeType.MALICIOUS);
        system.getConfig().setTurnOnMaliciousNodeOnStartup(true);
        system.addNode(maliciousNode);
        assertEquals(system.getNodeList().size(), Node.getNextId());
    }

    public void testIsMalicious() throws SimulationException {
        assertEquals(3, system.getNodeList().size());
        system.startUp(true, false);
        assertEquals(6, scheduler.getActions().size());
        assertTrue(system.getNodeList().contains(maliciousNode));
        assertTrue(maliciousNode.isMalicious());
        assertTrue(maliciousNode.getContainsProgram(ProgramType.MALICIOUS));
        scheduler.runAll();
        assertEquals(0, scheduler.getActions().size());
        assertEquals(2, node.getNeighbors().size());
        assertEquals(2, node2.getNeighbors().size());
        assertEquals(2, maliciousNode.getNeighbors().size());
    }

    public void testTurnOn() throws SimulationException {
        assertTrue(system.getNodeList().contains(maliciousNode));
        system.startUp(true, true);
        assertEquals(0, scheduler.getActions().size());
        assertEquals(NodeState.ONLINE, maliciousNode.getState());
        assertEquals(2, maliciousNode.getNeighbors().size());
    }

    public void testSendMessage() throws SimulationException {
        system.startUp(true, true);
        Message messageMalicious = null;
        long messagesSent = system.getStatistics().getValue(NodeSystemEvent.SYSTEM_MESSAGE_SENT);
        long messagesSentNode = maliciousNode.getNodeStatisticsValue(NodeEvent.NODE_MESSAGE_SENT);
        assertTrue(maliciousNode.getNeighbors().size() > 0);
        for (Integer dest : maliciousNode.getNeighbors()) {
            messageMalicious = new Message(maliciousNode, dest, MessageCode.GENERIC_DATA_MESSAGE);
            maliciousNode.getProgram().send(messageMalicious);
        }
        scheduler.runAll();
        assertEquals(messagesSent + 2, system.getStatistics().getValue(NodeSystemEvent.SYSTEM_MESSAGE_SENT));
        assertEquals(messagesSentNode + 2, maliciousNode.getNodeStatisticsValue(NodeEvent.NODE_MESSAGE_SENT));
    }
}
