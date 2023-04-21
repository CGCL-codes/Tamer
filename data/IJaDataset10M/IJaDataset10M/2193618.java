package com.cosylab.vdct.model.pvdata.xml;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvData.pv.PVArray;
import org.epics.pvData.pv.PVRecord;
import org.epics.pvData.pv.PVScalar;
import org.epics.pvData.pv.PVStructure;
import org.epics.pvData.pv.Type;
import org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener;
import org.epics.pvData.xml.IncludeSubstituteXMLListener;
import org.epics.pvData.xml.XMLToPVDatabaseListener;
import com.cosylab.logging.DebugLogger;
import com.cosylab.vdct.application.TaskEvent;
import com.cosylab.vdct.application.TaskListener;
import com.cosylab.vdct.model.NodeDescriptor;
import com.cosylab.vdct.model.primitive.LinePrimitive;
import com.cosylab.vdct.model.primitive.LabelPrimitive;
import com.cosylab.vdct.model.primitive.OvalPrimitive;
import com.cosylab.vdct.model.primitive.RectanglePrimitive;
import com.cosylab.vdct.model.pvdata.PVEdgeDescriptor;
import com.cosylab.vdct.model.pvdata.PVModel;
import com.cosylab.vdct.model.pvdata.PVModelDescriptor;
import com.cosylab.vdct.model.pvdata.PVModelFactory;
import com.cosylab.vdct.model.pvdata.PVModule;
import com.cosylab.vdct.model.pvdata.PVNode;
import com.cosylab.vdct.model.pvdata.PVNodeDescriptor;
import com.cosylab.vdct.model.pvdata.PVPin;
import com.cosylab.vdct.model.pvdata.PVPinDescriptor;
import java.awt.Color;
import java.awt.Rectangle;

/**
 * PVXMLListener implementation to register with XMLToPVDatabaseFactory
 * @author Janez Golob, jkamenik
 */
public class PVModelXMLListener implements IncludeSubstituteXMLListener, IncludeSubstituteDetailsXMLListener, XMLToPVDatabaseListener {

    Logger debug = DebugLogger.getLogger(PVModule.class.getName(), Level.INFO);

    public static final String ADDPATH = "addPath";

    public static final String DELIM = ", ";

    public static final String HREF = "href";

    public static final String INCLUDE = "include";

    static final String AUXINFO = "auxInfo";

    private static final String ARRAY = "array";

    private static final String IMPORT = "import";

    public static final String NAME = "name";

    private static final String RECORD = "record";

    public static final String REMOVEPATH = "removePath";

    private static final String SCALAR = "scalar";

    private static final String STRUCTURE = "structure";

    public static final String VDCTPOSITIONINFO = "vdctPositionInfo";

    public static final String VDCTVIEWPOINTINFO = "vdctViewPointInfo";

    public static final String VDCTSCALEINFO = "vdctViewScaleInfo";

    public static final String EDGECONTROLPOINTS = "edgeControlPoints";

    public static final String LINE_PRIMITIVE = "linePrimitive";

    public static final String RECTANGLE_PRIMITIVE = "rectanglePrimitive";

    public static final String OVAL_PRIMITIVE = "ovalPrimitive";

    public static final String LABEL_PRIMITIVE = "labelPrimitive";

    private String fileName;

    private Stack<String> sourceFileStack = new Stack<String>();

    private PVModelDescriptor modelDesc;

    private PVModel model;

    private Stack<PVNode> nodeStack = new Stack<PVNode>();

    private Collection<PVNode> nodeCollection = new ArrayList<PVNode>();

    private PVPin tempPin = null;

    private String value = null;

    private Map<String, String> rattributes;

    private Map<String, String> sattributes;

    private Map<String, String> aattributes;

    private TaskListener taskL;

    public PVModelXMLListener(TaskListener taskL, PVModel model, String fileName) {
        this.taskL = taskL;
        this.model = model;
        this.modelDesc = (PVModelDescriptor) model.getModelDescriptor();
        this.fileName = fileName;
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#startElementBeforeSubstitution(
         * java.lang.String, java.util.Map)
         */
    public void startElementBeforeSubstitution(String name, Map<String, String> attributes) {
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#elementBeforeSubstitution(
         * java.lang.String)
         */
    public void elementBeforeSubstitution(String content) {
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#newSourceFile(
         * java.lang.String)
         */
    public void newSourceFile(String fileName) {
        if (!sourceFileStack.empty() && sourceFileStack.peek().equals(this.fileName)) {
            String justFileName = "";
            if (fileName.lastIndexOf("/") == -1) {
                justFileName = fileName.substring(0, fileName.length());
            } else {
                justFileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
            }
            modelDesc.addElement(INCLUDE + "," + HREF + "," + justFileName);
        }
        sourceFileStack.push(fileName);
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#endSourceFile()
         */
    public void endSourceFile() {
        sourceFileStack.pop();
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#addPath(java.lang.String)
         */
    public void addPath(String pathName) {
        if (!sourceFileStack.empty() && sourceFileStack.peek().equals(this.fileName)) {
            modelDesc.addElement(INCLUDE + DELIM + ADDPATH + DELIM + pathName);
        }
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#removePath(java.lang.String)
         */
    public void removePath(String pathName) {
        if (!sourceFileStack.empty() && sourceFileStack.peek().equals(this.fileName)) {
            modelDesc.addElement(INCLUDE + DELIM + REMOVEPATH + DELIM + pathName);
        }
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#substitute(
         * java.lang.String, java.lang.String)
         */
    public void substitute(String from, String to) {
        if (sourceFileStack.peek().equals(fileName)) {
            modelDesc.addElement("substitute" + DELIM + "from" + DELIM + from + DELIM + "to" + DELIM + to);
        }
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteDetailsXMLListener#removeSubstitute(
         * java.lang.String)
         */
    public void removeSubstitute(String from) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteXMLListener#endDocument()
         */
    public void endDocument() {
        int i = 0;
        for (PVNode node : nodeCollection) {
            i++;
            taskL.taskChanged(new TaskEvent("Opening pv model [adding records]", false, i, nodeCollection.size()));
            model.addNode(node);
        }
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteXMLListener#startElement(
         * java.lang.String, java.util.Map)
         */
    public void startElement(String type, Map<String, String> attributes) {
        debug.fine("startElement " + type + " " + attributes.toString());
        PVNode node = null;
        if (type.equals(RECORD) || type.equals(STRUCTURE)) {
            if (rattributes != null) {
                throw new IllegalStateException("attributes are being overriden");
            }
            this.rattributes = attributes;
        } else if (type.equals(ARRAY) || type.equals(SCALAR)) {
            if (sattributes != null) {
                throw new IllegalStateException("attributes are being overriden");
            }
            this.sattributes = attributes;
        } else if (type.equals(AUXINFO)) {
            if (aattributes != null) {
                throw new IllegalStateException("attributes are being overriden");
            }
            this.aattributes = attributes;
        } else if ((type.equals(INCLUDE) || type.equals(IMPORT)) && sourceFileStack.peek().equals(fileName)) {
            StringBuilder builder = new StringBuilder();
            builder.append(type + DELIM);
            for (String attr : attributes.keySet()) {
                builder.append(attr + DELIM + attributes.get(attr));
            }
            modelDesc.addElement(builder.toString());
        }
    }

    private void insertAuxInfo() {
        debug.fine("auxInfo: " + aattributes.toString());
        if (tempPin != null) {
            ((PVPinDescriptor) tempPin.getPinDescriptor()).addAuxInfo(aattributes, value);
            value = null;
            aattributes = null;
        } else if (nodeStack.empty() == false) {
            ((PVNodeDescriptor) nodeStack.peek().getNodeDescriptor()).addAuxInfo(aattributes, value);
            if (aattributes.get(NAME).equals(VDCTPOSITIONINFO) && value != null) {
                StringTokenizer st = new StringTokenizer(value, DELIM);
                if (st.countTokens() == 2) {
                    int x = Integer.decode(st.nextToken());
                    int y = Integer.decode(st.nextToken());
                    Point pos = new Point(x, y);
                    nodeStack.peek().setPosition(pos);
                }
            } else if (aattributes.get(NAME).equals(VDCTVIEWPOINTINFO)) {
                StringTokenizer st = new StringTokenizer(value, DELIM);
                if (st.countTokens() == 2) {
                    int x = Integer.decode(st.nextToken());
                    int y = Integer.decode(st.nextToken());
                    Point pos = new Point(x, y);
                    nodeStack.peek().setViewportPosition(pos);
                }
            }
            value = null;
            aattributes = null;
        } else {
            if (aattributes.get(NAME).equals(VDCTVIEWPOINTINFO)) {
                StringTokenizer st = new StringTokenizer(value, DELIM);
                if (st.countTokens() == 2) {
                    int x = Integer.decode(st.nextToken());
                    int y = Integer.decode(st.nextToken());
                    Point pos = new Point(x, y);
                    model.setViewportPosition(pos);
                }
            } else if (aattributes.get(NAME).equals(VDCTSCALEINFO)) {
                model.setZoomFactor(Double.parseDouble(value));
            } else if (aattributes.get(NAME).equals(EDGECONTROLPOINTS)) {
                ((PVModelDescriptor) model.getModelDescriptor()).addEdgePositionHint(value);
            } else if (aattributes.get(NAME).equals(LINE_PRIMITIVE)) {
                createLinePrimitive(value);
            } else if (aattributes.get(NAME).equals(RECTANGLE_PRIMITIVE)) {
                createRectanglePrimitive(value);
            } else if (aattributes.get(NAME).equals(OVAL_PRIMITIVE)) {
                createOvalPrimitive(value);
            } else if (aattributes.get(NAME).equals(LABEL_PRIMITIVE)) {
                createLabelPrimitive(value);
            }
            value = null;
            aattributes = null;
        }
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteXMLListener#element(
         * java.lang.String)
         */
    public void element(String content) {
        value = content;
        debug.fine("elementContent = " + value);
    }

    /**
         * @see org.epics.pvData.xml.IncludeSubstituteXMLListener#endElement(java.lang.String)
         */
    public void endElement(String type) {
        debug.finest("endElement " + type);
        if (type.equals(AUXINFO)) {
            insertAuxInfo();
        }
    }

    public void startStructure(PVStructure pvStructure) {
        debug.finest("startStructure " + pvStructure.getFullName());
        PVNode node;
        String type = pvStructure.getExtendsStructureName();
        if (type == null) {
            type = PVModel.EMPTYSTRUCTURE;
        }
        if (nodeStack.empty()) {
            node = PVModelFactory.createNode(model, type, rattributes);
        } else {
            node = PVModelFactory.createNode(nodeStack.peek(), type, rattributes);
        }
        PVNodeDescriptor ndesc = (PVNodeDescriptor) node.getNodeDescriptor();
        if (nodeStack.empty()) {
            node.setNodeLocation(sourceFileStack.peek());
            if (sourceFileStack.peek().equals(fileName)) {
                ndesc.setEditable(true);
                ndesc.setVisibility(NodeDescriptor.Visibility.FULLY_VISIBLE);
            } else {
                ndesc.setEditable(false);
                ndesc.setVisibility(NodeDescriptor.Visibility.BLURED);
            }
        } else {
            ndesc.setEditable(nodeStack.peek().isEditable());
            ndesc.setVisibility(nodeStack.peek().getNodeDescriptor().getVisibility());
        }
        nodeStack.push(node);
        rattributes = null;
    }

    public void endStructure() {
        debug.finest("endStructure");
        if (!nodeStack.empty()) {
            PVNode node = nodeStack.pop();
            if (nodeStack.empty()) {
                nodeCollection.add(node);
            } else {
                nodeStack.peek().addNode(node);
            }
        }
    }

    public void startRecord(PVRecord pvRecord) {
        PVNode node;
        String type = pvRecord.getExtendsStructureName();
        if (type == null) {
            type = PVModel.EMPTYSTRUCTURE;
        }
        if (nodeStack.empty()) {
            node = PVModelFactory.createNode(model, type, rattributes);
        } else {
            node = PVModelFactory.createNode(nodeStack.peek(), type, rattributes);
        }
        PVNodeDescriptor ndesc = (PVNodeDescriptor) node.getNodeDescriptor();
        if (nodeStack.empty()) {
            node.setNodeLocation(sourceFileStack.peek());
            if (sourceFileStack.peek().equals(fileName)) {
                ndesc.setEditable(true);
                ndesc.setVisibility(NodeDescriptor.Visibility.FULLY_VISIBLE);
            } else {
                ndesc.setEditable(false);
                ndesc.setVisibility(NodeDescriptor.Visibility.BLURED);
            }
        } else {
            ndesc.setEditable(nodeStack.peek().isEditable());
            ndesc.setVisibility(nodeStack.peek().getNodeDescriptor().getVisibility());
        }
        nodeStack.push(node);
        rattributes = null;
    }

    public void endRecord() {
        if (!nodeStack.empty()) {
            PVNode node = nodeStack.pop();
            if (nodeStack.empty()) {
                nodeCollection.add(node);
            } else {
                throw new IllegalStateException("Nested records not allowed.");
            }
        }
    }

    public void newStructureField(PVStructure pvStructure) {
        debug.finest("startStructureField " + pvStructure.getFullName());
        PVNode node;
        String type = pvStructure.getExtendsStructureName();
        if (type == null) {
            type = PVModel.EMPTYSTRUCTURE;
        }
        if (nodeStack.empty()) {
            node = PVModelFactory.createNode(model, type, rattributes);
        } else {
            node = PVModelFactory.createNode(nodeStack.peek(), type, rattributes);
        }
        PVNodeDescriptor ndesc = (PVNodeDescriptor) node.getNodeDescriptor();
        if (nodeStack.empty()) {
            node.setNodeLocation(sourceFileStack.peek());
            if (sourceFileStack.peek().equals(fileName)) {
                ndesc.setEditable(true);
                ndesc.setVisibility(NodeDescriptor.Visibility.FULLY_VISIBLE);
            } else {
                ndesc.setEditable(false);
                ndesc.setVisibility(NodeDescriptor.Visibility.INVISIBLE);
            }
        } else {
            ndesc.setEditable(nodeStack.peek().isEditable());
            ndesc.setVisibility(nodeStack.peek().getNodeDescriptor().getVisibility());
        }
        nodeStack.push(node);
        rattributes = null;
    }

    public void endStructureField() {
        debug.finest("endStructureField");
        if (!nodeStack.empty()) {
            PVNode node = nodeStack.pop();
            if (nodeStack.empty()) {
            } else {
                nodeStack.peek().addNode(node);
            }
        }
    }

    public void startArray(PVArray pvArray) {
        boolean settable = sourceFileStack.peek().equals(fileName);
        if (nodeStack.empty()) {
            throw new IllegalStateException("node stack empty");
        }
        tempPin = PVModelFactory.createPin(nodeStack.peek(), Type.scalarArray, PVModelFactory.getTypeName(pvArray.getArray().getElementType()), sattributes, "", settable);
        sattributes = null;
    }

    public void endArray() {
        ((PVPinDescriptor) tempPin.getPinDescriptor()).setValue(value);
        nodeStack.peek().addPin(tempPin);
        tempPin = null;
        value = null;
    }

    public void startScalar(PVScalar pvScalar) {
        boolean settable = sourceFileStack.peek().equals(fileName);
        if (nodeStack.empty()) {
            throw new IllegalStateException("node stack empty when starting " + pvScalar.getFullFieldName());
        }
        tempPin = PVModelFactory.createPin(nodeStack.peek(), Type.scalar, PVModelFactory.getTypeName(pvScalar.getScalar().getScalarType()), sattributes, "", settable);
        sattributes = null;
    }

    public void endScalar() {
        debug.fine(tempPin.getName() + " = " + value);
        ((PVPinDescriptor) tempPin.getPinDescriptor()).setValue(value);
        nodeStack.peek().addPin(tempPin);
        tempPin = null;
        value = null;
    }

    public void startAuxInfo(String name, Map<String, String> attributes) {
    }

    public void endAuxInfo() {
    }

    private void createLinePrimitive(String value) {
        String[] split = value.split("\\s+");
        String string;
        ArrayList<Point> points = new ArrayList<Point>();
        if (split == null || split.length != 6) {
            throw new RuntimeException("wrong line primitive format: " + value);
        }
        for (int i = 0; i <= 1; i++) {
            string = split[i];
            String[] cp = string.split(",");
            points.add(new Point(Integer.decode(cp[0]), Integer.decode(cp[1])));
        }
        Color color = new Color(Integer.decode(split[2]));
        boolean dashed = Boolean.parseBoolean(split[3]);
        boolean sourceAnchor = Boolean.parseBoolean(split[4]);
        boolean targetAnchor = Boolean.parseBoolean(split[5]);
        LinePrimitive l = new LinePrimitive(sourceFileStack.peek().equals(fileName) ? true : false);
        l.setDashed(dashed);
        l.setForegroundColor(color);
        l.setControlPoints(points);
        l.setDisplaySourceAnchor(sourceAnchor);
        l.setDisplayTargetAnchor(targetAnchor);
        if (nodeStack.empty()) {
            model.addPrimitive(l);
        } else {
            nodeStack.peek().addPrimitive(l);
        }
    }

    private void createLabelPrimitive(String value) {
        String[] split = value.split("\\s+");
        String string;
        ArrayList<Point> points = new ArrayList<Point>();
        if (split == null || split.length < 6) {
            throw new RuntimeException("wrong label primitive format: " + value);
        }
        for (int i = 0; i <= 1; i++) {
            string = split[i];
            String[] cp = string.split(",");
            points.add(new Point(Integer.decode(cp[0]), Integer.decode(cp[1])));
        }
        Color color = new Color(Integer.decode(split[2]));
        boolean bordered = Boolean.parseBoolean(split[4]);
        boolean dashed = Boolean.parseBoolean(split[5]);
        String text = split.length > 3 ? value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\"") - 1) : "";
        LabelPrimitive l = new LabelPrimitive(sourceFileStack.peek().equals(fileName) ? true : false);
        l.setForegroundColor(color);
        l.setControlPoints(points);
        l.setLabel(text);
        l.setBorder(bordered);
        l.setBorderDashed(dashed);
        if (nodeStack.empty()) {
            model.addPrimitive(l);
        } else {
            nodeStack.peek().addPrimitive(l);
        }
    }

    private void createRectanglePrimitive(String value) {
        String[] split = value.split("\\s+");
        String string;
        ArrayList<Point> points = new ArrayList<Point>();
        if (split == null || split.length != 6) {
            throw new RuntimeException("wrong rectangle primitive format: " + value);
        }
        for (int i = 0; i <= 1; i++) {
            string = split[i];
            String[] cp = string.split(",");
            points.add(new Point(Integer.decode(cp[0]), Integer.decode(cp[1])));
        }
        Color color = new Color(Integer.decode(split[2]));
        boolean dashed = Boolean.parseBoolean(split[3]);
        boolean filled = Boolean.parseBoolean(split[4]);
        Color background = new Color(Integer.decode(split[5]));
        RectanglePrimitive r = new RectanglePrimitive(sourceFileStack.peek().equals(fileName) ? true : false);
        r.setDashed(dashed);
        r.setForegroundColor(color);
        r.setControlPoints(points);
        r.setFilled(filled);
        r.setBackgroundColor(background);
        if (nodeStack.empty()) {
            model.addPrimitive(r);
        } else {
            nodeStack.peek().addPrimitive(r);
        }
    }

    private void createOvalPrimitive(String value) {
        String[] split = value.split("\\s+");
        String string;
        ArrayList<Point> points = new ArrayList<Point>();
        if (split == null || split.length != 6) {
            throw new RuntimeException("wrong rectangle primitive format: " + value);
        }
        for (int i = 0; i <= 1; i++) {
            string = split[i];
            String[] cp = string.split(",");
            points.add(new Point(Integer.decode(cp[0]), Integer.decode(cp[1])));
        }
        Color color = new Color(Integer.decode(split[2]));
        boolean dashed = Boolean.parseBoolean(split[3]);
        boolean filled = Boolean.parseBoolean(split[4]);
        Color background = new Color(Integer.decode(split[5]));
        OvalPrimitive o = new OvalPrimitive(sourceFileStack.peek().equals(fileName) ? true : false);
        o.setDashed(dashed);
        o.setForegroundColor(color);
        o.setControlPoints(points);
        o.setFilled(filled);
        o.setBackgroundColor(background);
        if (nodeStack.empty()) {
            model.addPrimitive(o);
        } else {
            nodeStack.peek().addPrimitive(o);
        }
    }
}
