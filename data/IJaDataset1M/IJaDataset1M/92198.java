package org.yjchun.hanghe.layer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.yjchun.hanghe.Global;
import org.yjchun.hanghe.device.DeviceManager;
import org.yjchun.hanghe.device.GPSDevice;
import org.yjchun.hanghe.projection.LonLatPoint;
import org.yjchun.hanghe.projection.WorldProjection;
import org.yjchun.hanghe.ui.ChartView;
import org.yjchun.hanghe.ui.PropertyAction;
import org.yjchun.hanghe.util.event.EventListener;
import org.yjchun.hanghe.util.event.EventObject;

/**
 * Properties:
 *  layer.current-vehicle.auto-center.enabled=true
 * @author yjchun
 *
 */
public class CurrentVehicleLayer extends Layer {

    boolean autoCenter = false;

    LonLatPoint shipLocation = new LonLatPoint();

    int width = 11;

    int height = 11;

    Action[] actions;

    /**
	 * @param parent
	 */
    public CurrentVehicleLayer() {
        super();
        depthLevel = DEFAULT_LAYER_DEPTH_MOVABLE;
        setName("current-vehicle");
        DeviceManager.eventManager.addListener("GPSLocationChanged", new EventListener() {

            public void onEvent(EventObject arg) {
                GPSDevice gps = (GPSDevice) arg.getArgument();
                gpsSignalReceived(gps);
            }
        }, this);
        setSize(21, 21);
        actions = new Action[1];
        actions[0] = new PropertyAction("autoCenter") {

            public void actionPerformed(ActionEvent e) {
                setAutoCenterEnabled((Boolean) getValue(SELECTED_KEY));
            }
        };
        actions[0].putValue(PropertyAction.SELECTED_KEY, autoCenter);
    }

    public Action[] getActions() {
        return actions;
    }

    /**
	 * @param gps
	 */
    protected void gpsSignalReceived(GPSDevice gps) {
        setCurrentLocation(gps.longitude, gps.latitude);
    }

    public void setCurrentLocation(double lon, double lat) {
        shipLocation.setLocation(lon, lat);
        setCenterFixedLonLat(shipLocation.longitude, shipLocation.latitude);
        setCenterIfNotInBound();
    }

    /**
	 * 
	 */
    private void setCenterIfNotInBound() {
        if (autoCenter) {
            ChartView chartView = getChartView();
            Point p = new Point();
            projection.forward(shipLocation, p);
            if (p.x < projection.getWidth() / 4 || p.x > projection.getWidth() / 4 * 3 || p.y < projection.getHeight() / 4 || p.y > projection.getHeight() / 4 * 3) {
                chartView.setCenter(shipLocation.longitude, shipLocation.latitude);
            }
        }
    }

    @Override
    public void render(Graphics2D g, WorldProjection proj) {
        g.setPaint(Color.magenta);
        g.setStroke(new BasicStroke(5));
        g.drawLine(0, 10, 20, 10);
        g.drawLine(10, 0, 10, 20);
    }

    /**
	 * @param autoCenterSelected
	 */
    public void setAutoCenterEnabled(boolean b) {
        autoCenter = b;
        if (autoCenter && isVisible()) {
            getChartView().setCenter(shipLocation.longitude, shipLocation.latitude);
        }
    }

    protected void onProjectionChanged(WorldProjection proj) {
        setCenterIfNotInBound();
    }
}
