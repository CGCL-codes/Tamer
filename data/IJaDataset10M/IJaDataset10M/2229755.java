package org.yjchun.hanghe.layer;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yjchun.hanghe.Global;
import org.yjchun.hanghe.projection.LonLatPoint;
import org.yjchun.hanghe.projection.WorldProjection;
import org.yjchun.hanghe.ui.ChartView;
import org.yjchun.hanghe.ui.PropertyAction;
import org.yjchun.hanghe.util.event.EventListener;
import org.yjchun.hanghe.util.event.EventObject;

public abstract class Layer extends JComponent {

    static final Logger log = LoggerFactory.getLogger(Layer.class);

    public static int DEFAULT_LAYER_DEPTH_RASTER_CHART = 1;

    public static int DEFAULT_LAYER_DEPTH_VECTOR_CHART = 1;

    public static int DEFAULT_LAYER_DEPTH_FEATURE = 10;

    public static int DEFAULT_LAYER_DEPTH_MOVABLE = 100;

    /**
	 * Depth level for this layer. 
	 */
    protected int depthLevel = -1;

    protected WorldProjection projection = null;

    protected boolean isLocationLonLatFixed = false;

    protected LonLatPoint location = new LonLatPoint();

    protected Action enableAction;

    protected boolean isLoaded = false;

    protected Layer() {
        super();
        setOpaque(false);
        setEnabled(false);
        setRequestFocusEnabled(false);
        setFocusable(false);
        super.setVisible(false);
    }

    public boolean load() {
        if (depthLevel == -1 || getName() == null) throw new IllegalStateException("Layer is not initialized");
        enableAction = new PropertyAction(getName()) {

            public void actionPerformed(ActionEvent e) {
                setVisible((Boolean) getValue(SELECTED_KEY));
            }
        };
        enableAction.putValue(PropertyAction.SELECTED_KEY, true);
        isLoaded = true;
        setVisible(isLoaded);
        return isLoaded;
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public boolean isVisible() {
        if (!isLoaded) return false;
        return super.isVisible();
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public Action[] getActions() {
        return null;
    }

    public Action getEnableAction() {
        enableAction.putValue(AbstractAction.SELECTED_KEY, isVisible());
        if (!isLoaded) enableAction.setEnabled(false);
        return enableAction;
    }

    protected ChartView getChartView() {
        Container c = getParent();
        if (c == null || !(c instanceof ChartView)) throw new IllegalStateException("Parent ChartView is not set");
        return (ChartView) c;
    }

    /**
	 * Set location of this component lon/lat based.
	 * Once this method is called, it keep the location based on lon/lat even when
	 * the projection is changed. Call to either another setLocationLonLatFixed()
	 * or setLocationFixed() to cancel this behavior.
	 * 
	 * @param lon
	 * @param lat
	 */
    public void setCenterFixedLonLat(double lon, double lat) {
        location.setLonLat(lon, lat);
        isLocationLonLatFixed = true;
        Point newLocation = new Point();
        projection.forward(location, newLocation);
        newLocation.x -= getWidth() / 2;
        newLocation.y -= getHeight() / 2;
        setLocation(newLocation);
    }

    public void setLocationFixed(int x, int y) {
        isLocationLonLatFixed = false;
        setLocation(x, y);
    }

    public void projectionChanged(WorldProjection proj) {
        if (!isLoaded) return;
        projection = proj;
        if (isLocationLonLatFixed) {
            Point newLocation = new Point();
            proj.forward(location, newLocation);
            newLocation.x -= getWidth() / 2;
            newLocation.y -= getHeight() / 2;
            setLocation(newLocation);
        }
        onProjectionChanged(proj);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (projection == null || !isLoaded) return;
        render((Graphics2D) g, projection);
    }

    /**
	 * Called when projection is changed. Usually setSize() and repaint() is required
	 * in most layers.
	 */
    protected abstract void onProjectionChanged(WorldProjection proj);

    public void render(Graphics2D g, WorldProjection proj) {
    }
}
