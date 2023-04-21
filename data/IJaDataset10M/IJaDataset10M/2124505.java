package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.geom.coords.MGRSCoord;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.util.Logging;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

/**
 * Panel that allows the user to input different coordinates and displays the corresponding latitude and longitude in
 * decimal degrees.
 * <p>
 * Supported format are:
 * <ul>
 * <li>MGRS strings with or without separating spaces.</li>
 * <li>Decimal degrees with sign prefix or N, S, E, W suffix.</li>
 * <li>Degrees, minutes and seconds with sign prefix or N, S, E, W suffix.</li>
 * </ul>
 * The separator between lat/lon pairs can be ',', ', ' or any number of spaces.
 * </p>
 * <p>
 * Examples:<pre>
 * 11sku528111
 * 11S KU 528 111
 *
 * 45N 123W
 * +45.1234, -123.12
 * 45.1234N 123.12W
 *
 * 45 30' 00"N, 50 30'W
 * 4530' -50 30'
 * 45 30 N 50 30 W
 * </pre>
 * </p>
 *
 * @author Patrick Murris
 * @version $Id: GoToCoordinatePanel.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class GoToCoordinatePanel extends JPanel {

    private WorldWindow wwd;

    private JTextField coordInput;

    private JLabel resultLabel;

    public GoToCoordinatePanel(WorldWindow wwd) {
        super(new GridLayout(0, 1, 0, 0));
        this.wwd = wwd;
        this.makePanel();
    }

    private JPanel makePanel() {
        JPanel controlPanel = this;
        JPanel coordPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        coordPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.coordInput = new JTextField(10);
        this.coordInput.setToolTipText("Type coordinates and press Enter");
        this.coordInput.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                LatLon latLon = computeLatLonFromString(coordInput.getText(), wwd.getModel().getGlobe());
                updateResult(latLon);
            }
        });
        coordPanel.add(this.coordInput);
        JPanel resultPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.resultLabel = new JLabel();
        resultPanel.add(this.resultLabel);
        JPanel gotoPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        gotoPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JButton gotoButton = new JButton("Go to location");
        gotoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                LatLon latLon = computeLatLonFromString(coordInput.getText(), wwd.getModel().getGlobe());
                updateResult(latLon);
                if (latLon != null) {
                    View view = wwd.getView();
                    double distance = view.getCenterPoint().distanceTo3(view.getEyePoint());
                    view.goTo(new Position(latLon, 0), distance);
                }
            }
        });
        gotoPanel.add(gotoButton);
        controlPanel.add(coordPanel);
        controlPanel.add(resultPanel);
        controlPanel.add(gotoPanel);
        controlPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Go to")));
        return controlPanel;
    }

    private void updateResult(LatLon latLon) {
        if (latLon != null) {
            coordInput.setText(coordInput.getText().toUpperCase());
            resultLabel.setText(String.format("Lat %7.4f° Lon %7.4f°", latLon.getLatitude().degrees, latLon.getLongitude().degrees));
        } else resultLabel.setText("Invalid coordinates");
    }

    /**
     * Tries to extract a latitude and a longitude from the given text string.
     *
     * @param coordString the input string.
     * @param globe the current <code>Globe</code>.
     * @return the corresponding <code>LatLon</code> or <code>null</code>.
     */
    private static LatLon computeLatLonFromString(String coordString, Globe globe) {
        if (coordString == null) {
            String msg = Logging.getMessage("nullValue.StringIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        Angle lat = null;
        Angle lon = null;
        coordString = coordString.trim();
        String regex;
        String separators = "(\\s*|,|,\\s*)";
        Pattern pattern;
        Matcher matcher;
        regex = "\\d{1,2}[A-Za-z]\\s*[A-Za-z]{2}\\s*\\d{1,5}\\s*\\d{1,5}";
        if (coordString.matches(regex)) {
            try {
                MGRSCoord MGRS = MGRSCoord.fromString(coordString, globe);
                if (MGRS.getLatitude().degrees != 0 || MGRS.getLatitude().degrees != 0) {
                    lat = MGRS.getLatitude();
                    lon = MGRS.getLongitude();
                } else return null;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        if (lat == null || lon == null) {
            regex = "([-|\\+]?\\d+?(\\.\\d+?)??\\s*[N|n|S|s]??)";
            regex += separators;
            regex += "([-|\\+]?\\d+?(\\.\\d+?)??\\s*[E|e|W|w]??)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(coordString);
            if (matcher.matches()) {
                String sLat = matcher.group(1).trim();
                int signLat = 1;
                char suffix = sLat.toUpperCase().charAt(sLat.length() - 1);
                if (!Character.isDigit(suffix)) {
                    signLat = suffix == 'N' ? 1 : -1;
                    sLat = sLat.substring(0, sLat.length() - 1);
                    sLat = sLat.trim();
                }
                String sLon = matcher.group(4).trim();
                int signLon = 1;
                suffix = sLon.toUpperCase().charAt(sLon.length() - 1);
                if (!Character.isDigit(suffix)) {
                    signLon = suffix == 'E' ? 1 : -1;
                    sLon = sLon.substring(0, sLon.length() - 1);
                    sLon = sLon.trim();
                }
                lat = Angle.fromDegrees(Double.parseDouble(sLat) * signLat);
                lon = Angle.fromDegrees(Double.parseDouble(sLon) * signLon);
            }
        }
        if (lat == null || lon == null) {
            regex = "([-|\\+]?\\d{1,3}[d|D|°|\\s](\\s*\\d{1,2}['|’|\\s])?(\\s*\\d{1,2}[\"|”])?\\s*[N|n|S|s]?)";
            regex += separators;
            regex += "([-|\\+]?\\d{1,3}[d|D|°|\\s](\\s*\\d{1,2}['|’|\\s])?(\\s*\\d{1,2}[\"|”])?\\s*[E|e|W|w]?)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(coordString);
            if (matcher.matches()) {
                lat = parseDMSString(matcher.group(1));
                lon = parseDMSString(matcher.group(5));
            }
        }
        if (lat == null || lon == null) return null;
        if (lat.degrees >= -90 && lat.degrees <= 90 && lon.degrees >= -180 && lon.degrees <= 180) return new LatLon(lat, lon);
        return null;
    }

    /**
     * Parse a Degrees, Minute, Second coordinate string.
     * 
     * @param dmsString the string to parse.
     * @return the corresponding <code>Angle</code> or null.
     */
    private static Angle parseDMSString(String dmsString) {
        dmsString = dmsString.replaceAll("[D|d|°|'|’|\"|”]", " ");
        dmsString = dmsString.replaceAll("\\s+", " ");
        dmsString = dmsString.trim();
        int sign = 1;
        char suffix = dmsString.toUpperCase().charAt(dmsString.length() - 1);
        if (!Character.isDigit(suffix)) {
            sign = (suffix == 'N' || suffix == 'E') ? 1 : -1;
            dmsString = dmsString.substring(0, dmsString.length() - 1);
            dmsString = dmsString.trim();
        }
        char prefix = dmsString.charAt(0);
        if (!Character.isDigit(prefix)) {
            sign *= (prefix == '-') ? -1 : 1;
            dmsString = dmsString.substring(1, dmsString.length());
        }
        String[] DMS = dmsString.split(" ");
        double d = Integer.parseInt(DMS[0]);
        double m = DMS.length > 1 ? Integer.parseInt(DMS[1]) : 0;
        double s = DMS.length > 2 ? Integer.parseInt(DMS[2]) : 0;
        if (m >= 0 && m <= 60 && s >= 0 && s <= 60) return Angle.fromDegrees(d * sign + m / 60 * sign + s / 3600 * sign);
        return null;
    }
}
