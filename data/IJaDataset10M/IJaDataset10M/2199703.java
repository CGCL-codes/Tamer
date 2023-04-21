package com.cosylab.vdct.model;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 *
 * @author Janez Golob
 */
public abstract class AbstractDynamicModelTests {

    public Model model;

    public ModelTestSupplier supplier;

    public AbstractDynamicModelTests(ModelTestSupplier supplier) throws Exception {
        model = supplier.createFilledModel();
        this.supplier = supplier;
    }

    @Test
    public void addNodeTest() throws Exception {
        Node newNode = supplier.createNewNode(model);
        assertNotNull(newNode);
        model.addNode(newNode);
        assertNotNull(model.findNode(newNode.getUniqueName()));
        supplier.checkModelConsistency(model);
    }

    @Test
    public void removeNodeTest() throws Exception {
        Collection<Node> nodes = model.getNodes();
        if (nodes.isEmpty()) return;
        for (Node node : nodes) {
            model.removeNode(node);
            assertNull(model.findNode(node.getUniqueName()));
        }
        supplier.checkModelConsistency(model);
    }

    @Test
    public void addPinTest() throws Exception {
        Collection<Node> nodes = model.getNodes();
        if (nodes.isEmpty()) return;
        for (Node node : nodes) {
            Pin pin = supplier.createNewPin(node);
            assertNotNull(pin);
            node.addPin(pin);
            assertNotNull(model.findPin(pin.getUniqueName()));
        }
        supplier.checkModelConsistency(model);
    }

    @Test
    public void removePinTest() throws Exception {
        Collection<Node> nodes = model.getNodes();
        if (nodes.isEmpty()) return;
        int i = 0;
        for (Node node : nodes) {
            for (Pin pin : node.getPins()) {
                if (pin.getUniqueName().equals(node.getName() + ".SLNK")) {
                    try {
                        node.removePin(pin);
                    } catch (RuntimeException e) {
                        assertNotNull(model.findPin(pin.getUniqueName()));
                    }
                } else {
                    node.removePin(pin);
                    assertNull(model.findPin(pin.getUniqueName()));
                }
            }
            if (i++ > 2) break;
        }
        supplier.checkModelConsistency(model);
    }

    @Test
    public void addNodesTest() throws Exception {
        Node newNode = supplier.createNewNode(model);
        assertNotNull(newNode);
        Collection<Node> nodes = new ArrayList<Node>();
        nodes.add(newNode);
        model.addNodes(nodes);
        assertNotNull(model.findNode(newNode.getUniqueName()));
        supplier.checkModelConsistency(model);
    }

    @Test
    public void testRenameNodeAndPins() throws Exception {
        Collection<Node> nodes = model.getNodes();
        if (nodes.isEmpty()) return;
        int i = 0;
        for (Node node : nodes) {
            String newName = "rename" + (i++);
            node.setName(newName);
            assertNotNull(model.findNode(newName));
            int j = 0;
            for (Pin pin : node.getPins()) {
                String pinNewName = "rename" + (j++);
                pin.setName(pinNewName);
            }
            if (i >= 3) break;
        }
        for (Node node : nodes) {
            try {
                String newName = "rename";
                node.setName(newName);
            } catch (IllegalArgumentException e) {
                System.out.println("catch exeption, thats OK");
            }
            _checkUnique(node);
        }
        supplier.checkModelConsistency(model);
    }

    @Test
    public void removeEdgeTest() {
        Collection<Edge> edges = model.getEdges();
        if (edges.isEmpty()) return;
        Edge edge = edges.iterator().next();
        model.removeEdge(edge);
        assertFalse(model.getEdges().contains(edge));
    }

    private void _checkUnique(Node node) {
        String uniqueName = node.getUniqueName();
        for (Node nod : node.getModel().getNodes()) {
            if (node.equals(nod)) continue;
            assertNotSame(uniqueName, nod.getUniqueName());
        }
    }

    @Test
    public void testClone() throws Exception {
        Model cloneModel = supplier.createEmptyModel();
        for (Node node : model.getNodes()) {
            Node clone = node.clone(cloneModel);
            if (clone == null) continue;
            assertNotSame(clone, node);
            assertEquals(clone.getPins().size(), node.getPins().size());
            assertEquals(clone.getUniqueName(), node.getUniqueName());
            assertEquals(clone.getPosition(), node.getPosition());
        }
    }
}
