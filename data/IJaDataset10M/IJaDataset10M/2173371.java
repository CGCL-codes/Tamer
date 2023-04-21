package vizz3d.datagraph;

import grail.interfaces.EdgeInterface;
import grail.interfaces.NodeInterface;
import grail.properties.GraphProperties;
import grail.properties.TypeValues;
import java.awt.Color;
import java.util.HashMap;
import visgraph.VisualProperties;
import visgraph.gl.visTypes.GLBox;
import visgraph.gl.visTypes.GLCone;
import visgraph.gl.visTypes.GLCylinder;
import visgraph.gl.visTypes.GLLine;
import visgraph.gl.visTypes.GLSphere;
import visgraph.visualgraph.interfaces.MetaphorTypeInterface;
import vizz3d.common.gui.MainFrame;
import vizz3d.jogl.visengine.Interactions.JOGLScenePropInterface;

public class Graphinit {

    private MainFrame mainFrame = null;

    private HashMap typeColor = new HashMap();

    private HashMap typeShape = new HashMap();

    private HashMap typeLine = new HashMap();

    public Graphinit(MainFrame mf) {
        mainFrame = mf;
    }

    public Color getColorForType(TypeValues tv) {
        return (Color) typeColor.get(tv);
    }

    public MetaphorTypeInterface getShapeForType(TypeValues tv) {
        return (MetaphorTypeInterface) typeShape.get(tv);
    }

    public Integer getShapeForLine(TypeValues tv) {
        return (Integer) typeLine.get(tv);
    }

    public void setGraph(String name) {
        if (name.equals("PACKAGE_GRAPH_SPHERE")) {
            TypeValues tv = new TypeValues("PACKAGE_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(255, 0, 0), new GLSphere(), new Integer(0));
            tv = new TypeValues("CLASS_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 255, 0), new GLSphere(), new Integer(0));
            tv = new TypeValues("INTERFACE_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 0, 255), new GLSphere(), new Integer(0));
            tv = new TypeValues("EXTENDS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(0, 255, 255), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_SOLID));
            tv = new TypeValues("IMPLEMENTS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 255, 0), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DASH));
            tv = new TypeValues("CONTAINS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 0, 255), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DOT));
        }
        if (name.equals("PACKAGE_GRAPH_MIXED")) {
            TypeValues tv = new TypeValues("PACKAGE_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(255, 0, 0), new GLSphere(), new Integer(0));
            tv = new TypeValues("CLASS_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 255, 0), new GLBox(), new Integer(0));
            tv = new TypeValues("INTERFACE_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 0, 255), new GLCone(), new Integer(0));
            tv = new TypeValues("EXTENDS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(0, 255, 255), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_SOLID));
            tv = new TypeValues("IMPLEMENTS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 255, 0), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DASH));
            tv = new TypeValues("CONTAINS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 0, 255), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DOT));
        }
        if (name.equals("CALL_GRAPH_SPHERE")) {
            TypeValues tv = new TypeValues("FIELD_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(255, 0, 0), new GLSphere(), new Integer(0));
            tv = new TypeValues("CONSTRUCTOR_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 255, 0), new GLSphere(), new Integer(0));
            tv = new TypeValues("INITIALIZATION_BLOCK_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 0, 255), new GLSphere(), new Integer(0));
            tv = new TypeValues("METHOD_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 255, 255), new GLSphere(), new Integer(0));
            tv = new TypeValues("FIELD_REFERENCE_NODE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 255, 0), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_SOLID));
            tv = new TypeValues("CONSTRUCTOR_REFERENCE_NODE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 0, 255), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DASH));
            tv = new TypeValues("CONTAINS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(100, 0, 100), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DOT));
            tv = new TypeValues("METHOD_REFERENCE_NODE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(100, 100, 100), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DASH));
        }
        if (name.equals("CALL_GRAPH_MIXED")) {
            TypeValues tv = new TypeValues("FIELD_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(255, 0, 0), new GLSphere(), new Integer(0));
            tv = new TypeValues("CONSTRUCTOR_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 255, 0), new GLBox(), new Integer(0));
            tv = new TypeValues("INITIALIZATION_BLOCK_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 0, 255), new GLCone(), new Integer(0));
            tv = new TypeValues("METHOD_NODE", TypeValues.TYPE_NODE);
            setGraph(tv, new Color(0, 255, 255), new GLCylinder(), new Integer(0));
            tv = new TypeValues("FIELD_REFERENCE_NODE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 255, 0), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_SOLID));
            tv = new TypeValues("CONSTRUCTOR_REFERENCE_NODE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(255, 0, 255), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DASH));
            tv = new TypeValues("CONTAINS_EDGE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(100, 0, 100), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DOT));
            tv = new TypeValues("METHOD_REFERENCE_NODE", TypeValues.TYPE_EDGE);
            setGraph(tv, new Color(100, 100, 100), new GLLine(), new Integer(JOGLScenePropInterface.PATTERN_DASH));
        }
    }

    public void setPropertyEdge(EdgeInterface edge, String name, TypeValues type) {
        edge.setProperty(GraphProperties.LABEL, name);
        edge.setProperty(GraphProperties.TYPE, type);
        Color col = getColorForType(type);
        MetaphorTypeInterface shape = getShapeForType(type);
        Integer line = getShapeForLine(type);
        edge.setProperty(GraphProperties.EDGE_COLOR, col);
        edge.setProperty(GraphProperties.EDGE_SHAPE, line);
        edge.setProperty(VisualProperties.SCENE_EDGE_SHAPE_GL, shape);
    }

    public void setPropertyNode(NodeInterface node, String name, TypeValues type) {
        node.setProperty(GraphProperties.LABEL, name);
        node.setProperty(GraphProperties.TYPE, type);
        Color col = getColorForType(type);
        MetaphorTypeInterface shape = getShapeForType(type);
        node.setProperty(GraphProperties.NODE_COLOR, col);
        node.setProperty(VisualProperties.SCENE_NODE_SHAPE_GL, shape);
    }

    public void setGraph(TypeValues type, Color col, MetaphorTypeInterface shape, Integer line) {
        if (type.isNodeType()) {
            if (!mainFrame.containsNode(mainFrame.getComboNodes(), type)) {
                mainFrame.getComboNodes().addItem(type);
                mainFrame.getComboNodes().setSelectedItem(type);
            }
        }
        if (type.isEdgeType()) {
            if (!mainFrame.containsNode(mainFrame.getComboEdges(), type)) {
                mainFrame.getComboEdges().addItem(type);
                mainFrame.getComboEdges().setSelectedItem(type);
            }
        }
        typeColor.put(type, col);
        typeShape.put(type, shape);
        typeLine.put(type, line);
    }
}
