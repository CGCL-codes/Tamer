package view.mxGraph;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.Point;
import java.awt.print.PageFormat;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import model.Element;
import operating.Manager;
import org.w3c.dom.Document;

/**
 *
 * @author William Correa - spm.modeling
 */
public class SpmMxGraphComponent extends mxGraphComponent {

    private static final long serialVersionUID = 2;

    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    public static URL url = null;

    public SpmMxGraphComponent(mxGraph graph) {
        super(graph);
        setPageVisible(true);
        setPageBreakVisible(true);
        setWheelScrollingEnabled(true);
        setGridVisible(true);
        setToolTips(true);
        getConnectionHandler().setCreateTarget(true);
        mxCodec codec = new mxCodec();
        Document doc = mxUtils.loadDocument(getClass().getResource("/view/resources/basic-style.xml").toString());
        codec.decode(doc.getDocumentElement(), graph.getStylesheet());
        PageFormat page = new PageFormat();
        page.setOrientation(0);
        setPageFormat(page);
        getViewport().setOpaque(false);
        setBackground(Color.white);
        setPageBackgroundColor(new Color(200, 200, 200));
    }

    /**
     * Overrides drop behaviour to set the cell style if the target
     * is not a valid drop target and the cells are of the same
     * type (eg. both vertices or both edges). 
     * @param cells 
     * @param dx 
     * @param target
     * @param dy 
     * @param location
     * @return 
     */
    @Override
    public Object[] importCells(Object[] cells, double dx, double dy, Object target, Point location) {
        if (target == null && cells.length == 1 && location != null) {
            target = getCellAt(location.x, location.y);
        }
        return super.importCells(cells, dx, dy, target, location);
    }

    /**
     * A graph that creates new edges from a given template edge.
     */
    public static class CustomGraph extends mxGraph {

        /**
         * Holds the edge to be used as a template for inserting new edges.
         */
        protected Object edgeTemplate;

        /**
         * Custom graph that defines the alternate edge style to be used when
         * the middle control point of edges is double clicked (flipped).
         */
        public CustomGraph() {
            setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
        }

        /**
         * Sets the edge template to be used to inserting edges.
         * @param template 
         */
        public void setEdgeTemplate(Object template) {
            edgeTemplate = template;
        }

        /**
         * Prints out some useful information about the cell in the tooltip.
         * @param cell
         * @return 
         */
        @Override
        public String getToolTipForCell(Object cell) {
            String tip = "<html>";
            mxGeometry geo = getModel().getGeometry(cell);
            mxCellState state = getView().getState(cell);
            if (getModel().isEdge(cell)) {
                tip += "points={";
                if (geo != null) {
                    List points = geo.getPoints();
                    if (points != null) {
                        Iterator it = points.iterator();
                        while (it.hasNext()) {
                            mxPoint point = (mxPoint) it.next();
                            tip += "[x=" + numberFormat.format(point.getX()) + ",y=" + numberFormat.format(point.getY()) + "],";
                        }
                        tip = tip.substring(0, tip.length() - 1);
                    }
                }
                tip += "}<br>";
                tip += "absPoints={";
                if (state != null) {
                    for (int i = 0; i < state.getAbsolutePointCount(); i++) {
                        mxPoint point = state.getAbsolutePoint(i);
                        tip += "[x=" + numberFormat.format(point.getX()) + ",y=" + numberFormat.format(point.getY()) + "],";
                    }
                    tip = tip.substring(0, tip.length() - 1);
                }
                tip += "}";
            } else {
                tip += "geo=[";
                if (geo != null) {
                    tip += "x=" + numberFormat.format(geo.getX()) + ",y=" + numberFormat.format(geo.getY()) + ",width=" + numberFormat.format(geo.getWidth()) + ",height=" + numberFormat.format(geo.getHeight());
                }
                tip += "]<br>";
                tip += "state=[";
                if (state != null) {
                    tip += "x=" + numberFormat.format(state.getX()) + ",y=" + numberFormat.format(state.getY()) + ",width=" + numberFormat.format(state.getWidth()) + ",height=" + numberFormat.format(state.getHeight());
                }
                tip += "]";
            }
            mxPoint trans = getView().getTranslate();
            tip += "<br>scale=" + numberFormat.format(getView().getScale()) + ", translate=[x=" + numberFormat.format(trans.getX()) + ",y=" + numberFormat.format(trans.getY()) + "]";
            tip += "</html>";
            return tip;
        }

        /**
         * Overrides the method to use the currently selected edge template for
         * new edges.
         * 
         * @param parent
         * @param id
         * @param value
         * @param source
         * @param target
         * @param style
         * @return
         */
        @Override
        public Object createEdge(Object parent, String id, Object value, Object source, Object target, String style) {
            if (edgeTemplate != null) {
                mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
                edge.setId(id);
                return edge;
            }
            return super.createEdge(parent, id, value, source, target, style);
        }
    }
}
