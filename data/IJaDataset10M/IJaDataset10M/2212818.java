package au.edu.qut.yawl.editor.net;

import junit.framework.*;
import au.edu.qut.yawl.editor.elements.model.YAWLPort;
import au.edu.qut.yawl.editor.elements.model.YAWLVertex;
import au.edu.qut.yawl.editor.elements.model.VertexContainer;
import au.edu.qut.yawl.editor.elements.model.InputCondition;
import au.edu.qut.yawl.editor.elements.model.OutputCondition;
import au.edu.qut.yawl.editor.elements.model.AtomicTask;
import au.edu.qut.yawl.editor.elements.model.MultipleAtomicTask;
import au.edu.qut.yawl.editor.elements.model.CompositeTask;
import au.edu.qut.yawl.editor.elements.model.MultipleCompositeTask;
import au.edu.qut.yawl.editor.elements.model.Decorator;
import java.awt.Point;

public class TestNetElementSummary extends TestCase {

    NetGraphModel netModel = null;

    NetElementSummary summary = null;

    public TestNetElementSummary(String pName) {
        super(pName);
    }

    public static Test suite() {
        return new TestSuite(TestNetElementSummary.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        NetGraph net = new NetGraph();
        net.buildNewGraphContent();
        netModel = net.getNetModel();
    }

    public void testGetModel() {
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getModel(), netModel);
    }

    public void testInputCondition() {
        assertEquals(2, netModel.getRootCount());
        summary = new NetElementSummary(netModel);
        InputCondition inputCondition = null;
        for (int i = 0; i < netModel.getRootCount(); i++) {
            if (netModel.getRootAt(i) instanceof InputCondition) {
                inputCondition = (InputCondition) netModel.getRootAt(i);
            }
        }
        assertNotNull(summary.getInputCondition());
        assertEquals(summary.getInputCondition(), inputCondition);
    }

    public void testOutputCondition() {
        assertEquals(2, netModel.getRootCount());
        summary = new NetElementSummary(netModel);
        OutputCondition outputCondition = null;
        for (int i = 0; i < netModel.getRootCount(); i++) {
            if (netModel.getRootAt(i) instanceof OutputCondition) {
                outputCondition = (OutputCondition) netModel.getRootAt(i);
            }
        }
        assertNotNull(summary.getInputCondition());
        assertEquals(summary.getOutputCondition(), outputCondition);
    }

    public void testConditions() {
        netModel.getGraph().addCondition(new Point(10, 10));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getConditions().size(), 1);
        netModel.getGraph().addCondition(new Point(20, 20));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getConditions().size(), 2);
        netModel.getGraph().addCondition(new Point(30, 30));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getConditions().size(), 3);
    }

    public void testAtomicTasks() {
        netModel.getGraph().addAtomicTask(new Point(10, 10));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getAtomicTasks().size(), 1);
        netModel.getGraph().addMultipleAtomicTask(new Point(20, 20));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getAtomicTasks().size(), 2);
        netModel.getGraph().addAtomicTask(new Point(30, 30));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getAtomicTasks().size(), 3);
        netModel.getGraph().addMultipleAtomicTask(new Point(40, 40));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getAtomicTasks().size(), 4);
        AtomicTask decoratedAtomicTask = netModel.getGraph().addAtomicTask(new Point(50, 50));
        netModel.setSplitDecorator(decoratedAtomicTask, Decorator.AND_TYPE, Decorator.LEFT);
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getAtomicTasks().size(), 5);
        int atomicTasks = 0;
        int multipleAtomicTasks = 0;
        for (int i = 0; i < netModel.getRootCount(); i++) {
            if (netModel.getRootAt(i) instanceof VertexContainer) {
                assertEquals(decoratedAtomicTask, ((VertexContainer) netModel.getRootAt(i)).getVertex());
            }
            if (netModel.getRootAt(i) instanceof AtomicTask) {
                atomicTasks++;
            }
            if (netModel.getRootAt(i) instanceof MultipleAtomicTask) {
                multipleAtomicTasks++;
            }
        }
        assertEquals(2, atomicTasks);
        assertEquals(2, multipleAtomicTasks);
    }

    public void testCompositeTasks() {
        netModel.getGraph().addCompositeTask(new Point(10, 10));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getCompositeTasks().size(), 1);
        netModel.getGraph().addMultipleCompositeTask(new Point(20, 20));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getCompositeTasks().size(), 2);
        netModel.getGraph().addCompositeTask(new Point(30, 30));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getCompositeTasks().size(), 3);
        netModel.getGraph().addMultipleCompositeTask(new Point(40, 40));
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getCompositeTasks().size(), 4);
        CompositeTask decoratedCompositeTask = netModel.getGraph().addCompositeTask(new Point(50, 50));
        netModel.setSplitDecorator(decoratedCompositeTask, Decorator.AND_TYPE, Decorator.LEFT);
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getCompositeTasks().size(), 5);
        int compositeTasks = 0;
        int multipleCompositeTasks = 0;
        for (int i = 0; i < netModel.getRootCount(); i++) {
            if (netModel.getRootAt(i) instanceof VertexContainer) {
                assertEquals(decoratedCompositeTask, ((VertexContainer) netModel.getRootAt(i)).getVertex());
            }
            if (netModel.getRootAt(i) instanceof CompositeTask) {
                compositeTasks++;
            }
            if (netModel.getRootAt(i) instanceof MultipleCompositeTask) {
                multipleCompositeTasks++;
            }
        }
        assertEquals(2, compositeTasks);
        assertEquals(2, multipleCompositeTasks);
    }

    public void testFlows() {
        AtomicTask task = netModel.getGraph().addAtomicTask(new Point(10, 10));
        InputCondition inputCondition = null;
        OutputCondition outputCondition = null;
        assertEquals(3, netModel.getRootCount());
        for (int i = 0; i < netModel.getRootCount(); i++) {
            if (netModel.getRootAt(i) instanceof InputCondition) {
                inputCondition = (InputCondition) netModel.getRootAt(i);
                continue;
            }
            if (netModel.getRootAt(i) instanceof OutputCondition) {
                outputCondition = (OutputCondition) netModel.getRootAt(i);
                continue;
            }
            if (netModel.getRootAt(i) instanceof AtomicTask) {
                assertEquals(task, netModel.getRootAt(i));
                continue;
            }
        }
        assertNotNull(inputCondition);
        assertNotNull(outputCondition);
        YAWLPort inputConditionPort = inputCondition.getPortAt(YAWLVertex.RIGHT);
        YAWLPort taskIncommingPort = task.getPortAt(YAWLVertex.LEFT);
        YAWLPort taskOutgoingPort = task.getPortAt(YAWLVertex.RIGHT);
        YAWLPort outputConditionPort = outputCondition.getPortAt(YAWLVertex.LEFT);
        netModel.getGraph().connect(inputConditionPort, taskIncommingPort);
        netModel.getGraph().connect(taskOutgoingPort, outputConditionPort);
        summary = new NetElementSummary(netModel);
        assertEquals(summary.getFlows().size(), 2);
    }

    public void testCancellationSets() {
        AtomicTask atomicTask = netModel.getGraph().addAtomicTask(new Point(10, 10));
        CompositeTask compositeTask = netModel.getGraph().addCompositeTask(new Point(20, 20));
        InputCondition inputCondition = null;
        OutputCondition outputCondition = null;
        for (int i = 0; i < netModel.getRootCount(); i++) {
            if (netModel.getRootAt(i) instanceof InputCondition) {
                inputCondition = (InputCondition) netModel.getRootAt(i);
                continue;
            }
            if (netModel.getRootAt(i) instanceof OutputCondition) {
                outputCondition = (OutputCondition) netModel.getRootAt(i);
                continue;
            }
        }
        netModel.getGraph().connect(inputCondition.getPortAt(YAWLVertex.RIGHT), atomicTask.getPortAt(YAWLVertex.LEFT));
        netModel.getGraph().connect(atomicTask.getPortAt(YAWLVertex.RIGHT), compositeTask.getPortAt(YAWLVertex.LEFT));
        netModel.getGraph().connect(compositeTask.getPortAt(YAWLVertex.RIGHT), outputCondition.getPortAt(YAWLVertex.LEFT));
        summary = new NetElementSummary(netModel);
        assertEquals(0, summary.getTasksWithCancellationSets().size());
        netModel.getGraph().getSelectionModel().setSelectionCell(compositeTask);
        netModel.getGraph().changeCancellationSet(atomicTask);
        netModel.getGraph().addSelectedCellsToVisibleCancellationSet();
        summary = new NetElementSummary(netModel);
        assertEquals(1, summary.getTasksWithCancellationSets().size());
        netModel.getGraph().getSelectionModel().setSelectionCell(atomicTask);
        netModel.getGraph().changeCancellationSet(compositeTask);
        netModel.getGraph().addSelectedCellsToVisibleCancellationSet();
        summary = new NetElementSummary(netModel);
        assertEquals(2, summary.getTasksWithCancellationSets().size());
    }
}
