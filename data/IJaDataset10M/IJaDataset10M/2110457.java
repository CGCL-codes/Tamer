package jflowmap.tests_manual;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import jflowmap.FlowMapColorSchemes;
import jflowmap.geo.MapProjections;
import jflowmap.models.map.GeoMap;
import jflowmap.views.ColorCodes;
import jflowmap.views.flowmap.ColorSchemeAware;
import jflowmap.views.map.PGeoMap;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;

/**
 * @author Ilya Boyandin
 */
public class VisualAreaMapInCameraTest extends PFrame {

    private final ColorSchemeAware mapColorScheme = new ColorSchemeAware() {

        @Override
        public Color getColor(ColorCodes code) {
            return FlowMapColorSchemes.INVERTED.get(code);
        }
    };

    private final PGeoMap map;

    private final PPath redRect;

    private final PPath blueRect;

    public VisualAreaMapInCameraTest() throws IOException {
        setSize(800, 600);
        PCanvas canvas = getCanvas();
        PCamera camera = canvas.getCamera();
        map = new PGeoMap(mapColorScheme, GeoMap.load("data/refugees/countries-areas-ll.xml.gz"), MapProjections.MERCATOR);
        camera.addChild(map);
        PBounds mapFB = map.getFullBounds();
        redRect = new PPath(new Rectangle2D.Double(mapFB.x, mapFB.y, mapFB.width, mapFB.height));
        redRect.setStrokePaint(Color.red);
        redRect.setStroke(new PFixedWidthStroke(1.0f));
        camera.addChild(redRect);
        blueRect = new PPath(new Rectangle2D.Double(0, 0, 1, 1));
        blueRect.setStrokePaint(Color.blue);
        blueRect.setStroke(new PFixedWidthStroke(1.0f));
        camera.addChild(blueRect);
        camera.addPropertyChangeListener(new CameraListener());
        fitInView();
    }

    private class CameraListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            final String prop = evt.getPropertyName();
            if (prop == PCamera.PROPERTY_VIEW_TRANSFORM) {
            } else if (prop == PCamera.PROPERTY_BOUNDS) {
                fitInView();
            }
        }
    }

    private void fitInView() {
        PCamera camera = getCanvas().getCamera();
        PBounds cameraBounds = camera.getBounds();
        PBounds mapFullBounds = map.getUnionOfChildrenBounds(null);
        double scale = Math.min(camera.getWidth() / mapFullBounds.width / 3, camera.getHeight() / mapFullBounds.height);
        if (scale <= 0) {
            scale = 1.0;
        }
        map.setScale(scale);
        map.setOffset(cameraBounds.getMaxX() - mapFullBounds.getMaxX() * scale, (cameraBounds.getMaxY() + mapFullBounds.getMaxY() * scale) / 2);
        blueRect.setBounds(camera.getBounds().getBounds2D());
        redRect.setBounds(map.getFullBounds().getBounds2D());
        repaint();
    }

    private Rectangle2D.Double getActualMapBounds(PBounds mapFullBounds) {
        Rectangle2D.Double actualMapBounds = new Rectangle2D.Double();
        map.getTransform().inverseTransform(mapFullBounds.getBounds2D(), actualMapBounds);
        return actualMapBounds;
    }

    public static void main(String[] args) {
        try {
            new VisualAreaMapInCameraTest().setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
