package org.gvsig.graph;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.gvsig.exceptions.BaseException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleLineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.v02.FConstant;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.GraphicLayer;
import com.iver.cit.gvsig.fmap.rendering.FGraphic;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.topology.TopologyBuilder;
import com.iver.cit.gvsig.topology.triangulation.DelaunayTriangulation;
import com.iver.cit.gvsig.topology.triangulation.Pnt;
import com.iver.cit.gvsig.topology.triangulation.Simplex;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.planargraph.Node;
import com.vividsolutions.jts.planargraph.NodeMap;

/**
 * @author fjp Primera prueba acerca de la creaci�n de pol�gonos a partir de una
 *         capa de l�neas
 *
 */
public class TopologyExtension extends Extension {

    private class MyNode extends Node {

        public MyNode(Coordinate pt) {
            super(pt);
            occurrences = 1;
        }

        int occurrences;

        public int getOccurrences() {
            return occurrences;
        }

        public void setOccurrences(int occurrences) {
            this.occurrences = occurrences;
        }
    }

    /**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
    public void initialize() {
    }

    /**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
    public void execute(String s) {
        View v = (View) PluginServices.getMDIManager().getActiveWindow();
        MapControl mc = v.getMapControl();
        FLayer[] actives = mc.getMapContext().getLayers().getActives();
        try {
            for (int i = 0; i < actives.length; i++) {
                if (actives[i] instanceof FLyrVect) {
                    FLyrVect lv = (FLyrVect) actives[i];
                    if (s.compareTo("CLEAN") == 0) doClean(lv);
                    if (s.compareTo("TRIANGULATION") == 0) doTriangulation(lv);
                    if (s.compareTo("SHOW_ERRORS") == 0) doShowNodeErrors(lv);
                }
            }
        } catch (BaseException e) {
            e.printStackTrace();
            NotificationManager.addError(e);
        }
    }

    private void doTriangulation(FLyrVect lv) throws BaseException {
        View v = (View) PluginServices.getMDIManager().getActiveWindow();
        MapControl mc = v.getMapControl();
        FBitSet bitSet = lv.getRecordset().getSelection();
        Rectangle2D fullExtent = lv.getFullExtent();
        Simplex initialTriangle = new Simplex(new Pnt[] { new Pnt(fullExtent.getX(), fullExtent.getY()), new Pnt(fullExtent.getMaxX(), fullExtent.getY()), new Pnt(fullExtent.getCenterX(), fullExtent.getMaxY()) });
        DelaunayTriangulation triangulator = new DelaunayTriangulation(initialTriangle);
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            IGeometry g = lv.getSource().getShape(i);
            Geometry jtsG = g.toJTSGeometry();
            Coordinate[] coords = jtsG.getCoordinates();
            if (jtsG.isEmpty()) continue;
            Coordinate[] linePts = CoordinateArrays.removeRepeatedPoints(coords);
            Coordinate startPt = linePts[0];
            Coordinate endPt = linePts[linePts.length - 1];
            triangulator.delaunayPlace(startPt);
            triangulator.delaunayPlace(endPt);
        }
        GraphicLayer graphicLayer = mc.getMapContext().getGraphicsLayer();
        SimpleMarkerSymbol simNodeError = new SimpleMarkerSymbol();
        simNodeError.setSize(10);
        simNodeError.setStyle(FConstant.SYMBOL_STYLE_MARKER_SQUARE);
        simNodeError.setOutlined(true);
        simNodeError.setOutlineColor(Color.RED);
        int idSymbolPoint = graphicLayer.addSymbol(simNodeError);
        Iterator it = triangulator.iterator();
        while (it.hasNext()) {
            Simplex triangle = (Simplex) it.next();
            for (Iterator otherIt = triangle.facets().iterator(); otherIt.hasNext(); ) {
                Set facet = (Set) otherIt.next();
                Pnt[] endpoint = (Pnt[]) facet.toArray(new Pnt[2]);
                GeneralPathX gp = new GeneralPathX();
                System.err.println(endpoint[0].toString());
                gp.moveTo(endpoint[0].coord(0), endpoint[0].coord(1));
                gp.lineTo(endpoint[1].coord(0), endpoint[1].coord(1));
                IGeometry gAux = ShapeFactory.createPolyline2D(gp);
                FGraphic graphic = new FGraphic(gAux, idSymbolPoint);
                graphicLayer.addGraphic(graphic);
            }
        }
        mc.drawGraphics();
    }

    /**
	 * @param lv
	 *            FLayerVect de l�neas para convertir a pol�gonos.
	 */
    private void doClean(FLyrVect lv) {
        TopologyBuilder topologyBuilder = new TopologyBuilder(0, 0);
        topologyBuilder.setInputLayer(lv);
        View v = (View) PluginServices.getMDIManager().getActiveWindow();
        MapControl mc = v.getMapControl();
        try {
            FBitSet bitSet = lv.getRecordset().getSelection();
            Collection polygons = topologyBuilder.buildPolygons();
            Iterator it = polygons.iterator();
            GraphicLayer graphicLayer = mc.getMapContext().getGraphicsLayer();
            int idSymbolPol = graphicLayer.addSymbol(new SimpleFillSymbol());
            int idSymbolCutEdge = graphicLayer.addSymbol(new SimpleLineSymbol());
            int idSymbolDangle = graphicLayer.addSymbol(new SimpleLineSymbol());
            while (it.hasNext()) {
                Polygon pol = (Polygon) it.next();
                IGeometry gAux = FConverter.jts_to_igeometry(pol);
                FGraphic graphic = new FGraphic(gAux, idSymbolPol);
                graphicLayer.addGraphic(graphic);
            }
            Collection cutEdges = topologyBuilder.getCutEdges();
            it = cutEdges.iterator();
            while (it.hasNext()) {
                LineString lin = (LineString) it.next();
                IGeometry gAux = FConverter.jts_to_igeometry(lin);
                FGraphic graphic = new FGraphic(gAux, idSymbolCutEdge);
                graphicLayer.addGraphic(graphic);
            }
            Collection dangles = topologyBuilder.getDangles();
            it = dangles.iterator();
            while (it.hasNext()) {
                LineString lin = (LineString) it.next();
                IGeometry gAux = FConverter.jts_to_igeometry(lin);
                FGraphic graphic = new FGraphic(gAux, idSymbolDangle);
                graphicLayer.addGraphic(graphic);
            }
            mc.drawGraphics();
        } catch (BaseException e) {
            e.printStackTrace();
            NotificationManager.addError(e);
        }
    }

    /**
	 * We search for origin-endpoints in LineString. Each one will generate a
	 * Node. We also fill a map Node-numOccurrences. Dangle and Fuzzy nodes will
	 * be those that have an occurrence count = 1. (Node with degree cero in
	 * graph's language)
	 *
	 * @param lyr
	 * @throws DriverException
	 * @throws DriverIOException
	 */
    private void doShowNodeErrors(FLyrVect lv) throws BaseException {
        View v = (View) PluginServices.getMDIManager().getActiveWindow();
        MapControl mc = v.getMapControl();
        NodeMap nodeMap = new NodeMap();
        FBitSet bitSet = lv.getRecordset().getSelection();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            IGeometry g = lv.getSource().getShape(i);
            Geometry jtsG = g.toJTSGeometry();
            Coordinate[] coords = jtsG.getCoordinates();
            if (jtsG.isEmpty()) continue;
            Coordinate[] linePts = CoordinateArrays.removeRepeatedPoints(coords);
            Coordinate startPt = linePts[0];
            Coordinate endPt = linePts[linePts.length - 1];
            MyNode nStart = (MyNode) nodeMap.find(startPt);
            MyNode nEnd = (MyNode) nodeMap.find(endPt);
            if (nStart == null) {
                nStart = new MyNode(startPt);
                nodeMap.add(nStart);
            } else nStart.setOccurrences(nStart.getOccurrences() + 1);
            if (nEnd == null) {
                nEnd = new MyNode(endPt);
                nodeMap.add(nEnd);
            } else nEnd.setOccurrences(nEnd.getOccurrences() + 1);
        }
        GraphicLayer graphicLayer = mc.getMapContext().getGraphicsLayer();
        SimpleMarkerSymbol simNodeError = new SimpleMarkerSymbol();
        simNodeError.setSize(10);
        simNodeError.setStyle(FConstant.SYMBOL_STYLE_MARKER_SQUARE);
        simNodeError.setOutlined(true);
        simNodeError.setOutlineColor(Color.RED);
        int idSymbolPoint = graphicLayer.addSymbol(simNodeError);
        Iterator it = nodeMap.iterator();
        while (it.hasNext()) {
            MyNode node = (MyNode) it.next();
            if (node.getOccurrences() == 1) {
                FPoint2D p = FConverter.coordinate2FPoint2D(node.getCoordinate());
                IGeometry gAux = ShapeFactory.createPoint2D(p);
                FGraphic graphic = new FGraphic(gAux, idSymbolPoint);
                graphicLayer.addGraphic(graphic);
            }
        }
        mc.drawGraphics();
    }

    /**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
    public boolean isEnabled() {
        View v = (View) PluginServices.getMDIManager().getActiveWindow();
        MapControl mc = v.getMapControl();
        if (mc.getMapContext().getLayers().getActives().length > 0) return true;
        return false;
    }

    /**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
    public boolean isVisible() {
        return false;
    }
}
