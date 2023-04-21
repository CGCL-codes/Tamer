package fr.crnan.videso3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import fr.crnan.videso3d.graphics.SurfacePolygonAnnotation;
import fr.crnan.videso3d.graphics.VPolygon;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AirspaceLayer;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.airspaces.AirspaceAttributes;
import gov.nasa.worldwindx.examples.util.HighlightController;

/**
 * A very simple GLCanvas to displays one object at a time. <br/>
 * No mouse interaction allowed.
 * @author Bruno Spyckerelle
 * @version 0.1.1
 */
public class SimpleGLCanvasFactory {

    public static WorldWindowGLCanvas SimpleGLCanvasPolygon(AirspaceAttributes attrs, AirspaceAttributes attrsHighlight) {
        WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
        applyModel(wwd);
        ArrayList<LatLon> positions = new ArrayList<LatLon>();
        positions.add(Position.fromDegrees(46.4627, 1.2399));
        positions.add(Position.fromDegrees(46.4650, 5.3068));
        positions.add(Position.fromDegrees(49.2736, 5.4163));
        positions.add(Position.fromDegrees(49.2712, 1.1234));
        VPolygon polygon = new VPolygon(positions);
        polygon.setNormalAttributes(attrs);
        polygon.setHighlightAttributes(attrsHighlight);
        polygon.setAltitudes(0, 250e3);
        AirspaceLayer layer = new AirspaceLayer();
        layer.addAirspace(polygon);
        wwd.getModel().getLayers().addIfAbsent(layer);
        wwd.getView().setEyePosition(Position.fromDegrees(49, 4, 1050e3));
        wwd.getView().setPitch(Angle.fromDegrees(70));
        wwd.getView().setHeading(Angle.fromDegrees(36.3));
        return wwd;
    }

    public static WorldWindowGLCanvas SimpleGLCanvasPolygonShape(ShapeAttributes attrsNormal, ShapeAttributes attrsHighlight) {
        WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
        applyModel(wwd);
        ArrayList<LatLon> positions = new ArrayList<LatLon>();
        positions.add(Position.fromDegrees(46.4627, 1.2399));
        positions.add(Position.fromDegrees(46.4650, 5.3068));
        positions.add(Position.fromDegrees(49.2736, 5.4163));
        positions.add(Position.fromDegrees(49.2712, 1.1234));
        SurfacePolygonAnnotation surface = new SurfacePolygonAnnotation(positions);
        surface.setAttributes(attrsNormal);
        surface.setHighlightAttributes(attrsHighlight);
        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(surface);
        wwd.getModel().getLayers().addIfAbsent(layer);
        wwd.getView().setEyePosition(Position.fromDegrees(48, 3, 850e3));
        return wwd;
    }

    private static void applyModel(WorldWindowGLCanvas wwd) {
        wwd.setModel(new BasicModel());
        wwd.addSelectListener(new HighlightController(wwd, SelectEvent.ROLLOVER));
        wwd.getInputHandler().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();
            }
        });
        wwd.getInputHandler().addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                e.consume();
            }
        });
        wwd.getInputHandler().addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                e.consume();
            }
        });
        for (Layer l : wwd.getModel().getLayers()) {
            if (l instanceof CompassLayer) wwd.getModel().getLayers().remove(l);
            if (l instanceof ScalebarLayer) wwd.getModel().getLayers().remove(l);
            if (l instanceof WorldMapLayer) wwd.getModel().getLayers().remove(l);
        }
    }
}
