package com.wpjr.simulator.system;

import com.wpjr.simulator.entity.enums.NodeState;
import com.wpjr.simulator.entity.enums.SimulationType;
import com.wpjr.simulator.entity.node.Node;

public class TestNodeSystemEquidistant extends TestNodeSystemAbstract {

    public void testCreateNodes() throws SimulationException {
        int numberOfNodes = 50;
        system.reset();
        assertTrue(system.getNodeList().isEmpty());
        String[] args = { "-non", "50", "-gs", "500" };
        system.getConfig().setSimulationType(SimulationType.EQUIDISTANT);
        system.getConfig().setAverageDistance(20);
        system.init(args);
        assertEquals(numberOfNodes, system.getConfig().getNumberOfNodes());
        assertEquals(500, system.getConfig().getGridSize());
        assertEquals(SimulationType.EQUIDISTANT, system.getConfig().getSimulationType());
        assertTrue(system.getNodeList().isEmpty());
        system.startUp();
        assertEquals(SimulationType.EQUIDISTANT, system.getConfig().getSimulationType());
        assertEquals(numberOfNodes, system.getConfig().getNodesInfo().size());
        assertEquals(numberOfNodes, system.getNodeList().size());
        for (Node node : system.getNodeList()) {
            assertEquals(NodeState.ONLINE, node.getState());
        }
    }
}
