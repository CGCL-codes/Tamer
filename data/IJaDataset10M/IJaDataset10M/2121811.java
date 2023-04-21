package com.trapezium.chisel.reorganizers;

import com.trapezium.chisel.*;
import com.trapezium.vrml.VrmlElement;
import com.trapezium.vrml.node.Node;
import com.trapezium.vrml.fields.Field;
import java.util.Vector;
import java.util.Hashtable;

/**
 *  This chisel joins all Shapes containing a single color in the "diffuseColor"
 *  field.  Shapes with the same parent and same color are joined together into
 *  a single shape.
 */
public class ShapeColorJoiner extends Optimizer {

    /** Internal param class */
    class ColorJoinParam {

        Vector list;

        Node shape;

        ColorJoinParam(Vector list, Node shape) {
            this.list = list;
            this.shape = shape;
        }

        Vector getList() {
            return (list);
        }

        Node getShape() {
            return (shape);
        }
    }

    /** The list of color compatible shapes */
    Hashtable colorCompatibleShapes;

    /** Class constructor */
    public ShapeColorJoiner() {
        super("Shape", "Joining IndexedFaceSet by color...");
        reset();
    }

    /** Reset the Hashtable of color compatible shapes */
    public void reset() {
        colorCompatibleShapes = new Hashtable();
    }

    /** Only color compatible Shapes with IndexedFaceSet geometry within the same
     *  grouping node are joined.
     *
     *  Color compatibility means that the Shape nodes all USE the same appearance
     *  node.  So to use this Chisel effectively, have to split Shapes by color, 
     *  create DEF/USE, then Join Shapes by color.
     */
    public void attemptOptimization(Node n) {
        Field geometry = n.getField("geometry");
        if (geometry == null) {
            return;
        }
        Node geometryNode = geometry.getNodeValue();
        if (geometryNode == null) {
            return;
        }
        if (geometryNode.getBaseName().compareTo("IndexedFaceSet") != 0) {
            return;
        }
        Field appearance = n.getField("appearance");
        if (appearance == null) {
            return;
        }
        Node appearanceNode = appearance.getNodeValue();
        if (appearanceNode == null) {
            return;
        }
        Field material = appearanceNode.getField("material");
        if (material == null) {
            return;
        }
        Node materialNode = material.getNodeValue();
        if (materialNode == null) {
            return;
        }
        VrmlElement shapeParent = n.getParent();
        String listKey = materialNode.toString() + shapeParent.toString();
        Vector colorCompatibleList = (Vector) colorCompatibleShapes.get(listKey);
        boolean firstEntry = false;
        if (colorCompatibleList == null) {
            colorCompatibleList = new Vector();
            colorCompatibleShapes.put(listKey, colorCompatibleList);
            firstEntry = true;
        }
        colorCompatibleList.addElement(geometryNode);
        if (firstEntry) {
            replaceRange(n.getFirstTokenOffset(), n.getLastTokenOffset(), new ColorJoinParam(colorCompatibleList, n));
        } else {
            replaceRange(n.getFirstTokenOffset(), n.getLastTokenOffset(), null);
        }
    }

    /** Replace a range of tokens. 
	 *
	 *  @param tp print destination
	 *  @param param if null, this indicates a Shape to be skipped, and nothing is printed
	 *     Otherwise, this should be a Vector of Shape nodes to be replaced.  The first
	 *     Shape node is written along with its "appearance" field.  The "geometry" field
	 *     is written, with all fields except for "coord" and "coordIndex".  Then "coord"
	 *     is generated from all Shapes in the list, as is "coordIndex".
	 *  @param startTokenOffset first token offset of range being replaced
	 *  @param endTokenOffset last token offset of range being replaced
	 */
    public void optimize(TokenPrinter tp, Object param, int startTokenOffset, int endTokenOffset) {
        if (param instanceof ColorJoinParam) {
            ColorJoinParam cjp = (ColorJoinParam) param;
            Vector v = cjp.getList();
            int count = v.size();
            if (count == 1) {
                tp.printRange(startTokenOffset, endTokenOffset, false);
                return;
            }
            writeFirstShape(tp, cjp.getShape());
            tp.print("coord Coordinate { point [ ");
            int[] coordCounts = new int[count];
            for (int i = 0; i < count; i++) {
                Node n = (Node) v.elementAt(i);
                coordCounts[i] = writeCoord(tp, n);
                if (i > 0) {
                    coordCounts[i] += coordCounts[i - 1];
                }
            }
            tp.print("] }");
            tp.print("coordIndex [");
            for (int i = 0; i < count; i++) {
                Node n = (Node) v.elementAt(i);
                if (i == 0) {
                    writeCoordIndex(tp, n, 0);
                } else {
                    writeCoordIndex(tp, n, coordCounts[i - 1]);
                }
            }
            tp.print("]");
            tp.print("} }");
        }
    }

    /** Write out coordIndex values, offset by a specific amount.
	 *
	 *  @param tp print destination
	 *  @param n the IndexedFaceSet with the coordIndex field to modify and write
	 *  @param offset modify all non-negative-one coordIndex values by adding this amount
	 */
    void writeCoordIndex(TokenPrinter tp, Node n, int offset) {
        Field coordIndex = n.getField("coordIndex");
        if (coordIndex != null) {
            int scanner = coordIndex.getFirstTokenOffset();
            int end = coordIndex.getLastTokenOffset();
            dataSource.setState(scanner);
            while ((scanner != -1) && (scanner < end)) {
                scanner = dataSource.skipToNumber(0);
                if ((scanner != -1) && (scanner < end)) {
                    int val = dataSource.getIntValue(scanner);
                    if (val == -1) {
                        tp.print(-1);
                    } else {
                        tp.print(val + offset);
                    }
                    scanner = dataSource.getNextToken();
                }
            }
        }
    }

    /** Write out coordinate values, keep track of how many coordinates per node.
	 *
	 *  @param tp print destination
	 *  @param n the IndexedFaceSet to get coordinates from
	 *  
	 *  @return the number of coordinates written out
	 */
    int writeCoord(TokenPrinter tp, Node n) {
        Field coord = n.getField("coord");
        int coordCount = 0;
        if (coord != null) {
            Node coordNode = coord.getNodeValue();
            if (coordNode != null) {
                Field point = coordNode.getField("point");
                if (point != null) {
                    int scanner = point.getFirstTokenOffset();
                    int end = point.getLastTokenOffset();
                    dataSource.setState(scanner);
                    while ((scanner != -1) && (scanner < end)) {
                        scanner = dataSource.skipToNumber(0);
                        int n1 = scanner;
                        scanner = dataSource.getNextToken();
                        scanner = dataSource.skipToNumber(0);
                        int n2 = scanner;
                        scanner = dataSource.getNextToken();
                        scanner = dataSource.skipToNumber(0);
                        int n3 = scanner;
                        scanner = dataSource.getNextToken();
                        if ((scanner != -1) && (scanner <= end)) {
                            coordCount++;
                            tp.print(dataSource, n1);
                            tp.print(dataSource, n2);
                            tp.print(dataSource, n3);
                        }
                    }
                }
            }
        }
        return (coordCount);
    }

    /** Write out the first shape info, which means the entire appearance field */
    void writeFirstShape(TokenPrinter tp, Node shape) {
        tp.print("Shape { ");
        Field appearance = shape.getField("appearance");
        if (appearance != null) {
            tp.printRange(appearance.getFirstTokenOffset(), appearance.getLastTokenOffset(), false);
        }
        Field geometry = shape.getField("geometry");
        if (geometry != null) {
            Node geometryNode = geometry.getNodeValue();
            if (geometryNode != null) {
                tp.printRange(geometry.getFirstTokenOffset(), geometryNode.getFirstTokenOffset(), false);
                tp.print("{");
                printField(tp, geometryNode, "ccw");
                printField(tp, geometryNode, "convex");
                printField(tp, geometryNode, "creaseAngle");
                printField(tp, geometryNode, "solid");
            }
        }
    }

    /** print a field if it exists */
    void printField(TokenPrinter tp, Node n, String fieldName) {
        Field f = n.getField(fieldName);
        if (f != null) {
            tp.printRange(f.getFirstTokenOffset(), f.getLastTokenOffset(), false);
        }
    }
}
