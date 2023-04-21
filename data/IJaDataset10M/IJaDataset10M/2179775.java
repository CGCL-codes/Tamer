package net.sourceforge.circuitsmith.objectfactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.File;
import net.sourceforge.circuitsmith.eda.CircuitSmith;
import net.sourceforge.circuitsmith.layers.EdaPcbLayer;
import net.sourceforge.circuitsmith.objects.EdaAttributeText;
import net.sourceforge.circuitsmith.objects.EdaCircle;
import net.sourceforge.circuitsmith.objects.EdaComponentBox;
import net.sourceforge.circuitsmith.objects.EdaDrawing;
import net.sourceforge.circuitsmith.objects.EdaFootprint;
import net.sourceforge.circuitsmith.objects.EdaGrid;
import net.sourceforge.circuitsmith.objects.EdaLine;
import net.sourceforge.circuitsmith.objects.EdaNode;
import net.sourceforge.circuitsmith.objects.EdaObject;
import net.sourceforge.circuitsmith.objects.EdaOrigin;
import net.sourceforge.circuitsmith.objects.EdaPad;
import net.sourceforge.circuitsmith.objects.EdaPolygon;
import net.sourceforge.circuitsmith.objects.EdaRectangle;
import net.sourceforge.circuitsmith.objects.EdaSheet;
import net.sourceforge.circuitsmith.objects.EdaSinglePad;
import net.sourceforge.circuitsmith.objects.EdaSymbol;
import net.sourceforge.circuitsmith.objects.EdaWire;
import net.sourceforge.circuitsmith.objects.EdaAbstractText.Style;
import net.sourceforge.circuitsmith.objects.EdaGrid.GridStyle;
import net.sourceforge.circuitsmith.objects.EdaGrid.Snap;
import net.sourceforge.circuitsmith.objects.EdaAbstractText.XAnchor;
import net.sourceforge.circuitsmith.objects.EdaAbstractText.YAnchor;
import net.sourceforge.circuitsmith.panes.EdaDocumentPane;
import net.sourceforge.circuitsmith.panes.EdaDrawingPane;
import net.sourceforge.circuitsmith.panes.EdaPcbPane;
import net.sourceforge.circuitsmith.panes.EdaSchematicPane;
import net.sourceforge.circuitsmith.panes.EdaSimPane;
import net.sourceforge.circuitsmith.parts.EdaAttribute;
import net.sourceforge.circuitsmith.parts.EdaAttributeList;
import net.sourceforge.circuitsmith.parts.EdaPart;
import net.sourceforge.circuitsmith.parts.EdaPartList;
import net.sourceforge.circuitsmith.projects.EdaLibrary;
import net.sourceforge.circuitsmith.projects.EdaPcb;
import net.sourceforge.circuitsmith.projects.EdaProject;
import net.sourceforge.circuitsmith.projects.EdaSchematic;
import net.sourceforge.circuitsmith.projects.EdaTreeNode;
import net.sourceforge.circuitsmith.sim.EdaSimulation;

/**
 * Defines how circuitsmith objects may be created.
 * <p>
 * This factory methods shall garuantee, that they will create only valid Objects. Even if an Object has a constructor that
 * allows construction of it in an invalid state, implementors of this interface are responsible to return them only in a valid
 * state.
 * <p>
 * Implementors of this interface shall in no circumstances return <code>null</code>.
 * <p>
 * @author holger
 */
public interface EdaSaveableObjectFactory {

    EdaAttribute createEdaAttribute(String name, String value);

    EdaAttribute createEdaDocAttribute(String name, String path);

    EdaAttributeList createEdaAttributeModelList(EdaAttribute[] attributes);

    EdaProject createEdaProject(CircuitSmith eda, File projectFile, EdaTreeNode[] children);

    EdaPart createEdaPart(String sourceDocumentPath, String footprintDocumentPath, String subSheetDocumentPath, EdaPartList partList, long partId, EdaAttribute[] attributes);

    EdaPartList createEdaPartList(CircuitSmith eda, String name);

    EdaLibrary createEdaLibrary(CircuitSmith eda, EdaProject sourceProject);

    EdaAttributeText createEdaAttributeText(float locationX, float locationY, float angle, String layerName, Color color, boolean isFlipped, boolean isShown, String text, Font font, XAnchor xAnchor, YAnchor yAnchor, Style style, String name);

    EdaLine createEdaLine(float locationX, float locationY, float width, float height, float lineWidth, float angle, String layerName, Color color, boolean isFlipped, boolean isShown);

    EdaOrigin createEdaOrigin(float locationX, float locationY, String layerName, Color color);

    EdaDrawing createEdaDrawing(EdaObject[] groupedObjects, EdaAttributeList attributeList);

    EdaGrid createEdaGrid(String layerName, Color color, Point2D.Float gridSpacing, EdaGrid.AutoGrid autoGrid, Snap snap, GridStyle style);

    EdaSymbol createEdaSymbol(float locationX, float locationY, float angle, String layerName, Color color, boolean isFlipped, boolean isShown, EdaObject[] groupedObjects, String name, long partId, EdaPartList partList);

    EdaFootprint createEdaFootprint(float locationX, float locationY, float angle, String layerName, Color color, boolean isFlipped, boolean isShown, EdaObject[] groupedObjects, String name, long partId, EdaPartList partList, EdaPcbLayer.PcbSide pcbSide);

    EdaSchematicPane createEdaSchematicPane(CircuitSmith eda, EdaDrawing drawing);

    EdaPcbPane createEdaPcbPane(CircuitSmith eda, EdaDrawing drawing);

    EdaSchematic createEdaSchematic(CircuitSmith eda, String name, EdaTreeNode[] children, EdaDrawingPane pane);

    EdaPcb createEdaPcb(CircuitSmith eda, String name, EdaTreeNode[] children, EdaDrawingPane pane);

    EdaSimPane createEdaSimPane(CircuitSmith eda);

    EdaSimulation createEdaSimulation(CircuitSmith eda, String name, EdaTreeNode[] children, EdaDocumentPane pane);

    EdaComponentBox createEdaComponentBox(float locationX, float locationY, float width, float height, float lineWidth, float angle, String layerName, Color color, boolean isFlipped, boolean isShown, String name);

    EdaCircle createEdaCircle(float locationX, float locationY, float width, float height, float lineWidth, float angle, String layerName, Color color, boolean isFlipped, boolean isShown);

    EdaPad createEdaPad(float locationX, float locationY, float width, float height, float angle, String layerName, boolean isFlipped, float diameter, EdaPad.PadType padType, EdaSinglePad.PadShape padShape, String padNumber);

    EdaPad createEdaVia(float locationX, float locationY, float width, float height, float angle, String layerName, boolean isFlipped, float diameter, EdaSinglePad.PadShape padShape);

    EdaRectangle createEdaRectangle(float locationX, float locationY, float width, float height, float lineWidth, float angle, String layerName, Color color, boolean isFlipped, boolean isShown);

    EdaSheet createEdaSheet(float locationX, float locationY, float width, float height, String layerName, Color color, String sheetSize, String sheetOrientation);

    EdaWire createEdaWire(float locationX, float locationY, float width, float height, float lineWidth, float angle, String layerName, Color color, boolean isFlipped, boolean isShown);

    EdaNode createEdaNode(float locationX, float locationY, String layerName, Color color, boolean isShown);

    EdaObject createEdaArc(float x, float y, float x2, float y2, float lineWidth, float angle, float drawnAngle, float startAngle, String layerName, Color color, boolean flipped, boolean isShown);

    public EdaPolygon createEdaPolygon(float x, float y, float angle, String layerName, boolean flipped, boolean isShown);

    public EdaPolygon createPcbPolygon(float x, float y, float angle, String netName, String layerName, boolean flipped, boolean isShown);
}
