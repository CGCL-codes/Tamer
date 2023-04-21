package org.galab.saveableobject.controller;

import java.awt.*;
import java.io.*;
import java.util.*;
import org.galab.saveableobject.*;
import org.galab.util.*;

public class ControllerComponent extends VisualObject {

    /** Creates new ControllerComponent */
    public ControllerComponent() {
        super();
        inputs = new Vector();
        outputs = new Vector();
    }

    public ControllerComponent(Polygon newShape, Color newColor, V2 newPos, Angle newDir, File newImageFile, SaveableObject newParent) {
        super(newShape, newColor, newPos, newDir, newImageFile, newParent);
        inputs = new Vector();
        outputs = new Vector();
    }

    public ControllerComponent(ControllerComponent component) {
        super(component);
        inputs = new Vector();
        outputs = new Vector();
    }

    public void addInputNode(Node newInput) {
        inputs.add(newInput);
        newInput.setParent(this);
        setupForPaint();
    }

    public void addInputNode(Node newInput, int index) {
        inputs.add(index, newInput);
        newInput.setParent(this);
        setupForPaint();
    }

    public void setInputNode(Node newInput, int index) {
        removeInputNode((Node) inputs.get(index));
        inputs.add(index, newInput);
    }

    public void removeInputNode(Node oldInput) {
        if (oldInput != null) {
            oldInput.removeParent();
            inputs.remove(oldInput);
            setupForPaint();
        }
    }

    public void addOutputNode(Node newOutput) {
        outputs.add(newOutput);
        newOutput.setParent(this);
        setupForPaint();
    }

    public void addOutputNode(Node newOutput, int index) {
        outputs.add(index, newOutput);
        newOutput.setParent(this);
        setupForPaint();
    }

    public void setOutputNode(Node newOutput, int index) {
        removeOutputNode((Node) outputs.get(index));
        outputs.add(index, newOutput);
    }

    public void removeOutputNode(Node oldOutput) {
        if (oldOutput != null) {
            oldOutput.removeParent();
            outputs.remove(oldOutput);
            setupForPaint();
        }
    }

    public int getNumInputs() {
        return inputs.size();
    }

    public int getNumOutputs() {
        return outputs.size();
    }

    public Node getInputNode(int index) {
        return (Node) inputs.get(index);
    }

    public Node getOutputNode(int index) {
        return (Node) outputs.get(index);
    }

    public void process() {
    }

    public void storeActivation() {
        for (int i = 0; i < inputs.size(); i++) {
            ((Node) inputs.get(i)).storeCurrentActivation();
        }
        for (int i = 0; i < outputs.size(); i++) {
            ((Node) outputs.get(i)).storeCurrentActivation();
        }
    }

    public void fullyConnect(ControllerComponent component, String connectionType, ObjectManager objectManager) {
        for (int i = 0; i < getNumOutputs(); i++) {
            Node node1 = getOutputNode(i);
            for (int j = 0; j < component.getNumInputs(); j++) {
                Node node2 = component.getInputNode(j);
                Connection connection = (Connection) objectManager.getInstance(connectionType);
                connection.setParent(getParent());
                connection.connect(node1, node2);
            }
        }
    }

    public static Polygon getStandardInputShape() {
        int[] xpts = new int[3];
        int[] ypts = new int[3];
        xpts[0] = -5;
        ypts[0] = -5;
        xpts[1] = 0;
        ypts[1] = 5;
        xpts[2] = 5;
        ypts[2] = -5;
        return new Polygon(xpts, ypts, 3);
    }

    public static Polygon getStandardOutputShape() {
        int[] xpts = new int[3];
        int[] ypts = new int[3];
        xpts[0] = -5;
        ypts[0] = 5;
        xpts[1] = 0;
        ypts[1] = -5;
        xpts[2] = 5;
        ypts[2] = 5;
        return new Polygon(xpts, ypts, 3);
    }

    public void setupForPaint() {
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        int minXInput = 0, maxXInput = 0, minYInput = 0, maxYInput = 0;
        int minXOutput = 0, maxXOutput = 0, minYOutput = 0, maxYOutput = 0;
        int numNodes = inputs.size();
        double increment = 20;
        for (int i = 0; i < numNodes; i++) {
            Node node = (Node) inputs.get(i);
            node.setPos(new V2((i - (0.5 * (numNodes - 1))) * increment, 0));
            Polygon nodeShape = transformPolygon(node.getShape(), node);
            for (int j = 0; j < nodeShape.npoints; j++) {
                if ((i == 0 && j == 0) || nodeShape.xpoints[j] < minX) {
                    minX = (int) (nodeShape.xpoints[j]);
                }
                if ((i == 0 && j == 0) || nodeShape.xpoints[j] > maxX) {
                    maxX = (int) (nodeShape.xpoints[j]);
                }
                if ((i == 0 && j == 0) || nodeShape.ypoints[j] < minY) {
                    minY = (int) (nodeShape.ypoints[j]);
                }
                if ((i == 0 && j == 0) || nodeShape.ypoints[j] > maxY) {
                    maxY = (int) (nodeShape.ypoints[j]);
                }
            }
            if (outputs.size() != 0) {
                nodeShape = node.getShape();
                for (int j = 0; j < nodeShape.npoints; j++) {
                    if ((i == 0 && j == 0) || nodeShape.xpoints[j] < minXInput) {
                        minXInput = (int) (nodeShape.xpoints[j]);
                    }
                    if ((i == 0 && j == 0) || nodeShape.xpoints[j] > maxXInput) {
                        maxXInput = (int) (nodeShape.xpoints[j]);
                    }
                    if ((i == 0 && j == 0) || nodeShape.ypoints[j] < minYInput) {
                        minYInput = (int) (nodeShape.ypoints[j]);
                    }
                    if ((i == 0 && j == 0) || nodeShape.ypoints[j] > maxYInput) {
                        maxYInput = (int) (nodeShape.ypoints[j]);
                    }
                }
            }
        }
        numNodes = outputs.size();
        increment = 20;
        for (int i = 0; i < numNodes; i++) {
            Node node = (Node) outputs.get(i);
            node.setPos(new V2((i - (0.5 * (numNodes - 1))) * increment, 0));
            Polygon nodeShape = transformPolygon(node.getShape(), node);
            for (int j = 0; j < nodeShape.npoints; j++) {
                if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.xpoints[j] < minX) {
                    minX = (int) (nodeShape.xpoints[j]);
                }
                if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.xpoints[j] > maxX) {
                    maxX = (int) (nodeShape.xpoints[j]);
                }
                if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.ypoints[j] < minY) {
                    minY = (int) (nodeShape.ypoints[j]);
                }
                if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.ypoints[j] > maxY) {
                    maxY = (int) (nodeShape.ypoints[j]);
                }
            }
            if (inputs.size() != 0) {
                nodeShape = node.getShape();
                for (int j = 0; j < nodeShape.npoints; j++) {
                    if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.xpoints[j] < minXOutput) {
                        minXOutput = (int) (nodeShape.xpoints[j]);
                    }
                    if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.xpoints[j] > maxXOutput) {
                        maxXOutput = (int) (nodeShape.xpoints[j]);
                    }
                    if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.ypoints[j] < minYOutput) {
                        minYOutput = (int) (nodeShape.ypoints[j]);
                    }
                    if ((inputs.size() == 0 && i == 0 && j == 0) || nodeShape.ypoints[j] > maxYOutput) {
                        maxYOutput = (int) (nodeShape.ypoints[j]);
                    }
                }
            }
        }
        int inputDiff = (maxYInput - minYInput) / 2;
        int outputDiff = (maxYOutput - minYOutput) / 2;
        for (int i = 0; i < inputs.size(); i++) {
            Node node = (Node) inputs.get(i);
            node.augmentPos(new V2(0, inputDiff));
        }
        for (int i = 0; i < outputs.size(); i++) {
            Node node = (Node) outputs.get(i);
            node.augmentPos(new V2(0, -outputDiff));
        }
        int xpoints[] = new int[4];
        int ypoints[] = new int[4];
        minY -= outputDiff;
        maxY += inputDiff;
        xpoints[0] = minX;
        ypoints[0] = minY;
        xpoints[1] = minX;
        ypoints[1] = maxY;
        xpoints[2] = maxX;
        ypoints[2] = maxY;
        xpoints[3] = maxX;
        ypoints[3] = minY;
        setShape(new Polygon(xpoints, ypoints, 4));
        setColor(Color.black);
        setFillState(false);
        calculateRotatedShape();
    }

    public void resetActivity() {
        for (int i = 0; i < getNumInputs(); i++) {
            Node node = getInputNode(i);
            node.setActivation(0);
        }
        for (int i = 0; i < getNumOutputs(); i++) {
            Node node = getOutputNode(i);
            node.setActivation(0);
        }
    }

    public void setColorFromActivation() {
        for (int i = 0; i < inputs.size(); i++) {
            ((Node) inputs.get(i)).setColorFromActivation();
        }
        for (int i = 0; i < outputs.size(); i++) {
            ((Node) outputs.get(i)).setColorFromActivation();
        }
    }

    public Vector inputs;

    public static final double inputs_MIN = 0;

    public static final double inputs_MAX = 1;

    public int inputs_FIXED = SET_AND_FIXED;

    public Vector outputs;

    public static final double outputs_MIN = 0;

    public static final double outputs_MAX = 1;

    public int outputs_FIXED = SET_AND_FIXED;
}
