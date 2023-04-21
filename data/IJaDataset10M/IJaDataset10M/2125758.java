package net.sf.graphiti.ui.figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.graphiti.model.Edge;
import net.sf.graphiti.model.ObjectType;
import net.sf.graphiti.ui.figure.shapes.IShape;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * This class provides a figure for vertices.
 * 
 * @author Samuel Beaussier
 * @author Nicolas Isch
 * @author Matthieu Wipliez
 */
public class VertexFigure extends Figure {

    /**
	 * This class is here because it is necessary to circumvent a missing
	 * feature in GEF.
	 * <p>
	 * Most objects in Draw2D are compared by reference. Granted, this is a lot
	 * faster, but in some cases unwanted. For some reason
	 * {@link VertexFigure#getSourceAnchor(Edge, Connection)} is called several
	 * times per connection. We want to set bendpoints on a connection only
	 * once, because otherwise Draw2D messes up if the same bendpoints occur
	 * several times.
	 * </p>
	 * 
	 * <p>
	 * So basically this class holds all the information we need (we want
	 * bendpoint after the start or before the end and not both) and it has an
	 * {@link Object#equals(Object)} method which allows us to check whether a
	 * connection constraint list contains this. We have to store the
	 * association connection -> list of concrete bendpoints in a map, namely
	 * the {@link VertexFigure#bendpoints} field.
	 * </p>
	 * 
	 * <p>
	 * TODO: I suppose that if guys use the editor for a long time adding and
	 * removing a lot of connections, there might be a problem with this
	 * bendpoints thing...
	 * </p>
	 * 
	 * @author Matthieu Wipliez
	 */
    private class ConcreteBendpoint {

        public boolean end;

        public int offset;

        public ConcreteBendpoint(boolean end, int offset) {
            this.end = end;
            this.offset = offset;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ConcreteBendpoint) {
                ConcreteBendpoint cbp = (ConcreteBendpoint) obj;
                return end == cbp.end && offset == cbp.offset;
            } else {
                return false;
            }
        }

        /**
		 * Returns a {@link RelativeBendpoint} with the same characteristics as
		 * this bendpoint.
		 * 
		 * @param conn
		 *            The {@link Connection} to attach the relative bendpoint
		 *            to.
		 * @return A {@link RelativeBendpoint}
		 */
        public RelativeBendpoint getBendpoint(Connection conn) {
            RelativeBendpoint bp = new RelativeBendpoint(conn);
            if (end) {
                bp.setWeight(1.0f);
                bp.setRelativeDimensions(new Dimension(0, 0), new Dimension(offset, 0));
            } else {
                bp.setWeight(0.0f);
                bp.setRelativeDimensions(new Dimension(offset, 0), new Dimension(0, 0));
            }
            return bp;
        }
    }

    /**
	 * The reason for this field is listed in the documentation for the inner
	 * class {@link ConcreteBendpoint}.
	 * 
	 * @see ConcreteBendpoint
	 */
    private Map<Connection, List<ConcreteBendpoint>> bendpoints;

    private Map<String, Label> inputPorts;

    private Label labelId;

    private Label labelToolTip;

    private Map<String, Label> outputPorts;

    private IShape shape;

    /**
	 * Creates a new VertexFigure using the given vertex model.
	 * 
	 * @param dimension
	 * @param color
	 * @param shape
	 */
    public VertexFigure(Font font, Dimension dimension, Color color, IShape shape) {
        bendpoints = new HashMap<Connection, List<ConcreteBendpoint>>();
        inputPorts = new LinkedHashMap<String, Label>();
        outputPorts = new LinkedHashMap<String, Label>();
        setFont(font);
        setLayoutManager(new XYLayout());
        initLabels();
        initShape(shape, color, dimension);
    }

    /**
	 * Adds an input port with the given name.
	 * 
	 * @param portName
	 *            The port name.
	 */
    public void addInputPort(String portName) {
        if (portName != null && !portName.isEmpty()) {
            inputPorts.put(portName, null);
        }
    }

    /**
	 * Adds an output port with the given name.
	 * 
	 * @param portName
	 *            The port name.
	 */
    public void addOutputPort(String portName) {
        if (portName != null && !portName.isEmpty()) {
            outputPorts.put(portName, null);
        }
    }

    /**
	 * Adds a bendpoint to the bendpoint list that is the routing constraint of
	 * the <code>conn</code> connection. For additional information, see
	 * {@link ConcreteBendpoint}.
	 * 
	 * @param conn
	 *            The connection.
	 * @param end
	 *            True for the end anchor, false for the start anchor.
	 * @param offset
	 *            The offset.
	 */
    @SuppressWarnings("unchecked")
    public void addToList(Connection conn, boolean end, int offset) {
        List<ConcreteBendpoint> list = bendpoints.get(conn);
        if (list == null) {
            list = new ArrayList<ConcreteBendpoint>();
            bendpoints.put(conn, list);
        }
        ConcreteBendpoint cbp = new ConcreteBendpoint(end, offset);
        if (!list.contains(cbp)) {
            List<RelativeBendpoint> cstList = (List<RelativeBendpoint>) conn.getRoutingConstraint();
            if (cstList == null) {
                cstList = new ArrayList<RelativeBendpoint>();
                conn.setRoutingConstraint(cstList);
            }
            list.add(cbp);
            cstList.add(cbp.getBendpoint(conn));
        }
    }

    /**
	 * Adjusts the size of this figure according to its id and ports.
	 */
    @SuppressWarnings("unchecked")
    public void adjustSize() {
        List<IFigure> children = new ArrayList<IFigure>(shape.getChildren());
        children.remove(labelId);
        for (IFigure child : children) {
            shape.remove(child);
        }
        updatePorts();
        if (shape instanceof Polyline) {
            shape.setDimension(shape.getPreferredSize());
        }
        setSize(shape.getPreferredSize());
    }

    /**
	 * Returns the label for the input port whose name is given.
	 * 
	 * @param portName
	 *            The input port name.
	 * @return Its label.
	 */
    public Label getInputPortLabel(String portName) {
        return inputPorts.get(portName);
    }

    /**
	 * Returns the id label object.
	 * 
	 * @return A {@link Label}.
	 */
    public Label getLabelId() {
        return labelId;
    }

    /**
	 * Returns the label for the output port whose name is given.
	 * 
	 * @param portName
	 *            The output port name.
	 * @return Its label.
	 */
    public Label getOutputPortLabel(String portName) {
        return outputPorts.get(portName);
    }

    /**
	 * Returns the size of this VertexFigure.
	 * 
	 * @return The size of this VertexFigure.
	 */
    public Dimension getPreferredSize(int w, int h) {
        return getBounds().getSize();
    }

    /**
	 * Returns the shape of this figure.
	 * 
	 * @return The shape of this figure.
	 */
    public IShape getShape() {
        return shape;
    }

    /**
	 * Returns the connection source anchor, i.e. where connections start.
	 * 
	 * @return The {@link ConnectionAnchor} of the underlying shape.
	 */
    public ConnectionAnchor getSourceAnchor() {
        return shape.getConnectionAnchor(this, null, true);
    }

    /**
	 * Returns the connection source anchor, i.e. where connections start.
	 * 
	 * @param edge
	 *            The edge model of the connection. Allows the figure to
	 *            retrieve the source port.
	 * @param conn
	 *            The connection figure.
	 * @return The {@link ConnectionAnchor} of the underlying shape.
	 */
    public ConnectionAnchor getSourceAnchor(Edge edge, Connection conn) {
        String portName = (String) edge.getValue(ObjectType.PARAMETER_SOURCE_PORT);
        ConnectionAnchor anchor = shape.getConnectionAnchor(this, portName, true);
        addToList(conn, false, 20);
        addToList(conn, true, -20);
        return anchor;
    }

    /**
	 * Returns the connection target anchor, i.e. where connections end.
	 * 
	 * @return The {@link ConnectionAnchor} of the underlying shape.
	 */
    public ConnectionAnchor getTargetAnchor() {
        return shape.getConnectionAnchor(this, null, false);
    }

    /**
	 * Returns the connection target anchor, i.e. where connections end.
	 * 
	 * @param edge
	 *            The edge model of the connection. Allows the figure to
	 *            retrieve the target port.
	 * @param conn
	 *            The connection figure.
	 * @return The {@link ConnectionAnchor} of the underlying shape.
	 */
    public ConnectionAnchor getTargetAnchor(Edge edge, Connection conn) {
        String portName = (String) edge.getValue(ObjectType.PARAMETER_TARGET_PORT);
        ConnectionAnchor anchor = shape.getConnectionAnchor(this, portName, false);
        return anchor;
    }

    /**
	 * Initializes the labels.
	 */
    private void initLabels() {
        labelId = new Label();
        labelId.setForegroundColor(ColorConstants.black);
        labelToolTip = new Label();
        labelToolTip.setForegroundColor(ColorConstants.black);
        setToolTip(labelToolTip);
    }

    /**
	 * Initializes the shape and adds it to this figure.
	 * 
	 * @param shape
	 *            An {@link IShape}.
	 * @param color
	 *            Its {@link Color}.
	 * @param dimension
	 *            Its {@link Dimension}.
	 */
    private void initShape(IShape shape, Color color, Dimension dimension) {
        this.shape = shape;
        shape.setBackgroundColor(color);
        shape.setDimension(new Dimension(dimension.width, dimension.height));
        add(shape, new Rectangle(0, 0, -1, -1));
        GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        data.horizontalSpan = 2;
        shape.add(labelId, data);
    }

    /**
	 * Resets the ports sets.
	 */
    public void resetPorts() {
        inputPorts.clear();
        outputPorts.clear();
    }

    /**
	 * The name to set
	 * 
	 * @param text
	 */
    public void setId(String text) {
        labelId.setText(text);
        labelToolTip.setText(text);
    }

    /**
	 * Adds a label for the given entry. The entry key is a port name used in
	 * the label, and the entry value is updated to the newly-created label.
	 * 
	 * @param entry
	 *            An entry of {@link #inputPorts} or {@link #outputPorts}.
	 * @param horizontalAlignment
	 *            The horizontal alignment: {@link SWT#BEGINNING},
	 *            {@link SWT#CENTER} or {@link SWT#END}.
	 * @param horizontalSpan
	 *            The horizontal span, <code>1</code> or <code>2</code>.
	 */
    private void updatePortLabel(Entry<String, Label> entry, int horizontalAlignment, int horizontalSpan) {
        Label label = new Label(entry.getKey());
        entry.setValue(label);
        GridData data = new GridData(horizontalAlignment, SWT.CENTER, false, false);
        data.horizontalSpan = horizontalSpan;
        shape.add(label, data);
    }

    /**
	 * Adds label for all ports of this figure in the grid layout.
	 */
    private void updatePorts() {
        List<Entry<String, Label>> inputPortList = new ArrayList<Entry<String, Label>>(inputPorts.entrySet());
        List<Entry<String, Label>> outputPortList = new ArrayList<Entry<String, Label>>(outputPorts.entrySet());
        List<Entry<String, Label>> portList = new ArrayList<Entry<String, Label>>();
        for (Entry<String, Label> inputPort : inputPorts.entrySet()) {
            if (outputPorts.containsKey(inputPort.getKey())) {
                portList.add(inputPort);
                outputPortList.remove(inputPort);
                inputPortList.remove(inputPort);
            }
        }
        updatePortsFromLists(inputPortList, outputPortList, portList);
    }

    /**
	 * Adds labels from the ports of the given lists.
	 * 
	 * @param inputPortList
	 *            The list of input ports.
	 * @param outputPortList
	 *            The list of output ports.
	 * @param portList
	 *            The list of undirected ports/ports having the same name.
	 */
    private void updatePortsFromLists(List<Entry<String, Label>> inputPortList, List<Entry<String, Label>> outputPortList, List<Entry<String, Label>> portList) {
        Iterator<Entry<String, Label>> itInput = inputPortList.iterator();
        Iterator<Entry<String, Label>> itOutput = outputPortList.iterator();
        while (itInput.hasNext()) {
            if (itOutput.hasNext()) {
                updatePortLabel(itInput.next(), SWT.BEGINNING, 1);
                updatePortLabel(itOutput.next(), SWT.END, 1);
            } else {
                updatePortLabel(itInput.next(), SWT.BEGINNING, 2);
            }
        }
        while (itOutput.hasNext()) {
            updatePortLabel(itOutput.next(), SWT.END, 2);
        }
        for (Entry<String, Label> entry : portList) {
            updatePortLabel(entry, SWT.CENTER, 2);
            String portName = entry.getKey();
            Label label = entry.getValue();
            inputPorts.put(portName, label);
            outputPorts.put(portName, label);
        }
    }

    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }
}
