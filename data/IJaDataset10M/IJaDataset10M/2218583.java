package org.opencarto.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vividsolutions.jts.awt.PointTransformation;
import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The default style.
 * 
 * @author julien Gaffuri
 *
 */
public class DefaultStyle implements Style {

    private static final Logger logger = Logger.getLogger(DefaultStyle.class.getName());

    private DefaultStyle() {
    }

    private static DefaultStyle instance;

    /**
	 * @return The unique class instance.
	 */
    public static DefaultStyle getInstance() {
        if (instance == null) {
            instance = new DefaultStyle();
        }
        return instance;
    }

    private BasicStroke stroke = null;

    private BasicStroke getStroke() {
        if (this.stroke == null) this.stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        return this.stroke;
    }

    private BasicStroke lineIntStroke = null;

    private BasicStroke getLineIntStroke() {
        if (this.lineIntStroke == null) this.lineIntStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        return this.lineIntStroke;
    }

    private BasicStroke lineExtStroke = null;

    private BasicStroke getLineExtStroke() {
        if (this.lineExtStroke == null) this.lineExtStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        return this.lineExtStroke;
    }

    @Override
    public void draw(Stylable st, ShapeWriter sw, PointTransformation pt, Graphics2D gr) {
        Geometry geom = st.getGeom();
        if (geom instanceof Point) draw((Point) geom, pt, gr); else if (geom instanceof LineString) draw((LineString) geom, sw, gr); else if (geom instanceof Polygon) draw((Polygon) geom, sw, gr); else if (geom instanceof GeometryCollection) draw((GeometryCollection) geom, sw, pt, gr); else {
            logger.log(Level.WARNING, "Method not implemented yet for geometry type: " + geom.getClass().getSimpleName());
        }
    }

    private void draw(Point geom, PointTransformation pt, Graphics2D gr) {
        Coordinate dp = geom.getCoordinate();
        gr.setStroke(this.getStroke());
        gr.setColor(Color.GRAY);
        int width = 2;
        Point2D p = new java.awt.Point();
        pt.transform(dp, p);
        gr.fillOval((int) p.getX() - width, (int) p.getY() - width, 2 * width, 2 * width);
        gr.setColor(Color.BLACK);
        gr.drawOval((int) p.getX() - width, (int) p.getY() - width, 2 * width, 2 * width);
    }

    private void draw(LineString geom, ShapeWriter sw, Graphics2D gr) {
        gr.setColor(Color.BLACK);
        gr.setStroke(this.getLineExtStroke());
        gr.draw(sw.toShape(geom));
        gr.setColor(Color.GRAY);
        gr.setStroke(this.getLineIntStroke());
        gr.draw(sw.toShape(geom));
    }

    private static void draw(Polygon geom, ShapeWriter sw, Graphics2D gr) {
        gr.setColor(Color.GRAY);
        gr.fill(sw.toShape(geom));
        gr.setColor(Color.BLACK);
        gr.draw(sw.toShape(geom));
    }

    private void draw(GeometryCollection gc, ShapeWriter sw, PointTransformation pt, Graphics2D gr) {
        for (int i = 0; i < gc.getNumGeometries(); i++) {
            Geometry geom = gc.getGeometryN(i);
            if (geom instanceof Point) draw((Point) geom, pt, gr); else if (geom instanceof LineString) draw((LineString) geom, sw, gr); else if (geom instanceof Polygon) draw((Polygon) geom, sw, gr); else if (geom instanceof GeometryCollection) draw((GeometryCollection) geom, sw, pt, gr);
        }
    }

    @Override
    public Geometry footprint(Stylable st) {
        logger.warning("Method not implemented yet.");
        return null;
    }
}
