package org.progeeks.mapview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.event.*;
import org.progeeks.util.log.*;
import org.progeeks.mapview.wms.*;

/**
 *  A MapViewer wrapper that decorates a MapViewer with common
 *  overlay components and setup.
 *
 *  @version   $Revision: 3966 $
 *  @author    Paul Speed
 */
public class MapViewerKit extends JPanel {

    static Log log = Log.getLog();

    private MapViewer map;

    private JProgressBar progressBar;

    private JSlider scaleSlider;

    private JPanel bottom;

    public MapViewerKit() {
        super(new BorderLayout());
        buildComponents();
    }

    protected void buildComponents() {
        this.map = new MapViewer();
        map.setLayout(new GridBagLayout());
        add(map, "Center");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        JPanel left = new JPanel();
        left.setOpaque(false);
        map.add(left, gbc);
        scaleSlider = new JSlider(JSlider.VERTICAL);
        scaleSlider.setModel(map.getScaleRangeModel());
        scaleSlider.setOpaque(false);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setMinorTickSpacing(1);
        scaleSlider.setSnapToTicks(true);
        left.add(scaleSlider);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel center = new JPanel();
        center.setOpaque(false);
        map.add(center, gbc);
        progressBar = new JProgressBar();
        center.add(progressBar);
        map.getTaskManager().addChangeListener(new ProgressObserver());
    }

    public MapViewer getMapViewer() {
        return map;
    }

    private class ProgressObserver implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            boolean busy = ((LayerTaskManager) e.getSource()).isBusy();
            progressBar.setIndeterminate(busy);
            progressBar.setVisible(busy);
        }
    }

    public static void main(String[] args) {
        Log.initialize();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.warn("Error setting system look and feel.", e);
        }
        MapViewerKit kit = new MapViewerKit();
        final MapViewer map = kit.getMapViewer();
        WmsService wms1 = new WmsService("http://localhost:8080/geoserver/wms?server=WMS");
        WmsLayer earth = new WmsLayer(wms1, "bluemarble");
        earth.setZoomSmoothing(WmsLayer.ZoomSmoothing.BOTH);
        map.addLayer(earth);
        map.addLayer(new WmsLayer(wms1, "ERV:TM_WORLD_BORDERS-0"));
        map.addLayer(new WmsLayer(wms1, "ERV:virginia_highway"));
        map.addLayer(new WmsLayer(wms1, "ERV:iraq_highway"));
        map.addLayer(new WmsLayer(wms1, "ERV:citiesx020"));
        MarkerLayer markers = new MarkerLayer();
        map.addLayer(markers);
        markers.addMarker(new Marker(new GeoPoint(-77.10057831, 38.87137604)));
        markers.addMarker(new Marker(new GeoPoint(-77.34068298, 38.96873093), MarkerPainter.GMARKER_WHITE.recolor(255, 255, 0)));
        markers.addMarker(new Marker(new GeoPoint(-77.03636932, 38.89511108), MarkerPainter.GMARKER, MarkerPainter.DIAMOND_BLACK));
        markers.addMarker(new Marker(new GeoPoint(-77.15276337, 39.08399582), MarkerPainter.GMARKER, MarkerPainter.SQUARE_BLACK));
        CompositeMapLayer group = new CompositeMapLayer();
        group.addLayer(new GridLayer());
        group.addLayer(new ScaleLegendLayer());
        map.addLayer(group);
        map.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("clicked:" + e.getX() + ", " + e.getY());
                System.out.println("point:" + map.screenToWorld(e.getX(), e.getY()));
                System.out.println("Hits:" + map.getHits(e.getX(), e.getY()));
            }
        });
        JFrame frame = new JFrame("Testing");
        frame.getContentPane().add(kit, "Center");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
