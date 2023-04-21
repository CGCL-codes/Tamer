package edu.kds.gui.regrets;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
import edu.kds.circuit.*;
import edu.kds.circuit.geom.*;
import edu.kds.circuit.visitors.ComponentEdgeJointCollector;
import edu.kds.event.StateChangeEvent;
import edu.kds.event.StateChangeListener;
import edu.kds.gui.regrets.events.*;

public class RegretManager implements KeyListener {

    private final Stack<Regretable> undoStack;

    private final Stack<Regretable> redoStack;

    private final HistoryFrame historyFrame;

    private final CircuitListener circuitListener;

    private final Circuit circuit;

    public RegretManager(Circuit c) {
        historyFrame = new HistoryFrame();
        undoStack = new Stack<Regretable>();
        redoStack = new Stack<Regretable>();
        circuitListener = new CircuitListener();
        circuit = c;
        circuit.addChangeListener(circuitListener);
        ComponentEdgeJointCollector collector = new ComponentEdgeJointCollector();
        circuit.accept(collector);
        for (CComponent comp : collector.getComponents()) comp.addChangeListener(circuitListener);
        for (WireJoint joint : collector.getJoints()) joint.addChangeListener(circuitListener);
        for (WireEdge edge : collector.getEdges()) edge.addChangeListener(circuitListener);
    }

    public void undo() {
        if (undoStack.isEmpty()) return;
        Regretable event = undoStack.pop();
        circuitListener.setListeningEnabled(false);
        event.undo();
        circuitListener.setListeningEnabled(true);
        historyFrame.updateHistory(undoStack);
        if (event instanceof CreateWireEvent) {
            redoStack.removeAllElements();
            return;
        }
        redoStack.push(event);
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Regretable event = redoStack.pop();
            circuitListener.setListeningEnabled(false);
            event.redo();
            circuitListener.setListeningEnabled(true);
            undoStack.push(event);
        }
        historyFrame.updateHistory(undoStack);
    }

    private void addEvent(Regretable r) {
        Regretable pushThis = r;
        if (!undoStack.isEmpty()) {
            Regretable top = undoStack.peek();
            if (top.joinable(r)) {
                pushThis = top.join(r);
                undoStack.pop();
            }
        }
        undoStack.push(pushThis);
        historyFrame.updateHistory(undoStack);
    }

    @SuppressWarnings("unused")
    private void printStacks() {
        System.out.print("UndoStack: |");
        for (Regretable r : undoStack) {
            System.out.print(r.getShortString() + "|");
        }
        System.out.print("\nRedoStack: |");
        for (Regretable r : redoStack) {
            System.out.print(r.getShortString() + "|");
        }
        System.out.println();
    }

    public void keyPressed(KeyEvent evt) {
        if (evt.getModifiers() == KeyEvent.CTRL_MASK) {
            if (evt.getKeyCode() == KeyEvent.VK_Z) {
                undo();
            }
            if (evt.getKeyCode() == KeyEvent.VK_Y) {
                redo();
            }
            if (evt.getKeyCode() == KeyEvent.VK_R) {
                redo();
            }
        }
    }

    public void keyTyped(KeyEvent evt) {
    }

    public void keyReleased(KeyEvent evt) {
    }

    private class CircuitListener implements StateChangeListener {

        private boolean listeningEnabled = true;

        void setListeningEnabled(boolean b) {
            listeningEnabled = b;
        }

        public void stateChanged(StateChangeEvent evt) {
            if (!listeningEnabled) return;
            if (evt.getChangeType() == Circuit.ADD_COMPONENT_EVENT) {
                ((CComponent) evt.getAttachment()).removeChangeListener(this);
                ((CComponent) evt.getAttachment()).addChangeListener(this);
                addEvent(new CreateComponentEvent((CComponent) evt.getAttachment()));
            } else if (evt.getChangeType() == Circuit.REMOVE_COMPONENT_EVENT) {
                addEvent(new DeleteComponentEvent((CComponent) evt.getAttachment()));
            } else if (evt.getChangeType() == CComponent.USER_MOVE_EVENT) {
                addEvent(new MoveComponentEvent((CComponent) evt.getSource(), (InchVector) evt.getAttachment()));
            } else if (evt.getChangeType() == CComponent.RESIZE_EVENT) {
                addEvent(new ResizeComponentEvent((CComponent) evt.getSource(), (InchDimension[]) evt.getAttachment()));
            } else if (evt.getChangeType() == CComponent.ROTATE_EVENT) {
                addEvent(new RotateEvent((CComponent) evt.getSource()));
            } else if (evt.getChangeType() == CComponent.X_MIRROR_EVENT) {
                addEvent(new MirrorEvent((CComponent) evt.getSource(), 0));
            } else if (evt.getChangeType() == CComponent.Y_MIRROR_EVENT) {
                addEvent(new MirrorEvent((CComponent) evt.getSource(), 1));
            } else if (evt.getChangeType() == CComponent.PROPERTY_EVENT) {
                addEvent(new ChangeParameterEvent((CComponent) evt.getSource(), (CComponent.Properties[]) evt.getAttachment()));
            } else if (evt.getChangeType() == Circuit.ADD_JOINT_EVENT) {
                ((WireJoint) evt.getAttachment()).removeChangeListener(this);
                ((WireJoint) evt.getAttachment()).addChangeListener(this);
            } else if (evt.getChangeType() == WireJoint.USER_MOVE_EVENT) {
                addEvent(new MoveJointEvent((WireJoint) evt.getSource(), (InchVector) evt.getAttachment()));
            } else if (evt.getChangeType() == Circuit.ADD_EDGE_EVENT) {
                ((WireEdge) evt.getAttachment()).removeChangeListener(this);
                ((WireEdge) evt.getAttachment()).addChangeListener(this);
            } else if (evt.getChangeType() == WireEdge.MOVE_EVENT) {
                addEvent(new MoveEdgeEvent((WireEdge) evt.getSource(), (InchVector) evt.getAttachment()));
            } else if (evt.getChangeType() == Circuit.ADD_WIRE_EVENT) {
                addEvent(new CreateWireEvent((WireEdge) evt.getAttachment()));
            }
        }
    }
}
