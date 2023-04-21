package si.mk.k3.kbrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.media.j3d.Node;
import javax.media.j3d.NodeComponent;
import javax.media.j3d.Shape3D;

/**
 * This is the base class for all X3D nodes. It automatically handles node 
 * hierarchy and contains default implementation of methods, some of which should
 * be overridden in derived node classes.
 * 
 * @author markok
 *
 */
public abstract class X3DNodeImpl implements X3DNode {

    protected String m_nodeName;

    protected List<X3DNode> m_subnodes;

    private List<XTouchSensor> m_touchSensors;

    protected X3DNodeImpl() {
        this(null);
    }

    protected X3DNodeImpl(String nodeName) {
        m_nodeName = nodeName;
    }

    /**
     * Adds subnode to this node. Not intended to be overridden, but if it is,
     * don't forget to call this method. 
     */
    public void add(X3DNode node) {
        if (m_subnodes == null) {
            m_subnodes = new ArrayList<X3DNode>();
        }
        m_subnodes.add(node);
    }

    /**
     * Should be overridden in all node classes, which contain fields. This means 
     * almost all classes, but currently this feature is not widely supported, 
     * so nodes can not be changed after class creation.
     *  
     * TODO implement method getField() in all subclasses of X3DNodeImpl with fields.
     */
    public X3DField getField(String name) {
        return null;
    }

    /**
     * Methods getJ3DNode() and getJ3DNodeComponent() are defined to overcome
     * difference between X3D and Java3D. Some fields are Nodes in X3D, but 
     * NodeComponents in Java3D, for example Appearance.
     *  
     * At least one of them should be overridden.
     * 
     * Override this method, when j3d class is subclass of j3d.Node.
     * 
     * @return j3d node
     */
    public Node getJ3DNode() {
        return null;
    }

    /**
     * Override this method, when j3d class is subclass of j3d.NodeComponent.
     * 
     * @see #getJ3DNode()
     */
    public NodeComponent getJ3DNodeComponent() {
        return null;
    }

    /**
     * Adapter method.
     */
    public void getShape3Ds(List<Shape3D> shapes) {
    }

    void addTouchSensor(XTouchSensor touchSensor) {
        if (m_touchSensors == null) {
            m_touchSensors = new ArrayList<XTouchSensor>();
        }
        m_touchSensors.add(touchSensor);
    }

    /**
    * X3D spec says, that all shapes below the group node, which contains 
    * TouchSensor, must send events to the Touch sensor.
    */
    public void collectTouchSensors(Map<Shape3D, List<XTouchSensor>> touchSensorsMap) {
        if (m_touchSensors != null && m_touchSensors.size() > 0) {
            List<Shape3D> shapes = new ArrayList<Shape3D>();
            getShape3Ds(shapes);
            for (Shape3D shape : shapes) {
                List<XTouchSensor> sensorList = touchSensorsMap.get(shape);
                if (sensorList == null) {
                    sensorList = new ArrayList<XTouchSensor>();
                    touchSensorsMap.put(shape, sensorList);
                }
                sensorList.addAll(m_touchSensors);
            }
        }
        if (m_subnodes != null) {
            for (X3DNode node : m_subnodes) {
                node.collectTouchSensors(touchSensorsMap);
            }
        }
    }

    public String toString() {
        return "X3DNodeImpl: " + m_nodeName;
    }
}
