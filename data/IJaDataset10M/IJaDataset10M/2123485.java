package com.crypticbit.ipa.ui.swing.map;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import com.crypticbit.ipa.ui.swing.Mediator;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Style;

public class KmlHandler {

    public interface WaypointProvider {

        public Collection<? extends KmlWaypoint> getWaypoints();
    }

    private Mediator mediator;

    private WaypointProvider waypointProvider;

    public KmlHandler(Mediator mediator, WaypointProvider waypointProvider) {
        this.mediator = mediator;
        this.waypointProvider = waypointProvider;
    }

    public JButton createExport() {
        JButton button = new JButton(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/export.png"), "Export to KML"));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(mediator.getMainFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        outputToKml(file);
                    } catch (FileNotFoundException e1) {
                        mediator.displayWarningDialog("Unable to export to KML", e1);
                    }
                }
            }
        });
        return button;
    }

    public JButton createOpen() {
        JButton button = new JButton(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/open-map.png"), "Open Map"));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File temp;
                try {
                    temp = new File(com.crypticbit.ipa.io.util.IoUtils.createTempDir(".deviceRoot"), "open.kml");
                    outputToKml(temp);
                    Desktop.getDesktop().open(temp);
                } catch (IOException e1) {
                    mediator.displayWarningDialog("Unable to open KML map", e1);
                }
            }
        });
        return button;
    }

    public void outputToKml(File file) throws FileNotFoundException {
        final Kml kml = KmlFactory.createKml();
        final Document document = kml.createAndSetDocument().withName("iPhoneAnalzyer-export.kml").withOpen(true);
        Set<Color> seen = new HashSet<Color>();
        for (KmlWaypoint waypoint : waypointProvider.getWaypoints()) {
            Color color = waypoint.getColor();
            if (color != null) {
                if (!seen.contains(color)) {
                    seen.add(color);
                    Style style = document.createAndAddStyle();
                    style.withId("style_" + color.hashCode()).createAndSetIconStyle().withColor("FF" + asHex(color.getBlue()) + asHex(color.getGreen()) + asHex(color.getRed())).withScale(1.0);
                }
            }
            final Placemark placemark = document.createAndAddPlacemark().withName(waypoint.getName()).withDescription(waypoint.getDescription());
            if (color != null) placemark.withStyleUrl("#style_" + color.hashCode());
            final Point point = placemark.createAndSetPoint();
            List<Coordinate> coord = point.createAndSetCoordinates();
            coord.add(new Coordinate(waypoint.getLongitude(), waypoint.getLatitude(), 0));
        }
        kml.marshal(file);
    }

    private String asHex(int num) {
        String temp = Integer.toHexString(num);
        if (temp.length() < 2) {
            temp = "0" + temp;
        }
        return temp;
    }

    public interface KmlWaypoint {

        public double getLatitude();

        public double getLongitude();

        public String getName();

        public Color getColor();

        public String getDescription();
    }
}
