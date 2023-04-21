package gui.app;

import gui.gauges.AirspeedIndicator;
import gui.gauges.Altimeter;
import gui.gauges.AttitudeIndicator;
import gui.gauges.Clock;
import gui.gauges.GenericRPM;
import gui.gauges.HeadingIndicator;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author David Escobar Sanabria
 */
public class PanelGauges extends JPanel {

    private Altimeter altimeterIndicator;

    private AirspeedIndicator airSpeedIndicator;

    private AttitudeIndicator attitudIndicator;

    private HeadingIndicator headingIndicator;

    private Clock clockIndicator;

    private JLabel labelAltimeter;

    private JLabel labelAirSpeed;

    private JLabel labelAttitud;

    private JLabel labelHeading;

    private JLabel labelClock;

    private JLabel labelGPS;

    private JLabel labelModeManual;

    private JLabel labelModeAHD;

    private JLabel labelModeIHD;

    private JLabel labelModeWPN;

    private JPanel panelGPS;

    private JPanel panelModeManual;

    private JPanel panelModeAHD;

    private JPanel panelModeIHD;

    private JPanel panelModeWPN;

    private boolean GPS = false;

    private double altitud = 0;

    private double airSpeed = 0;

    private double airSpeedRef = 0;

    private double pitch = 0;

    private double heading = 0;

    private double time = 0;

    private double bank = 0;

    private int mode = 0;

    private Color color1 = new Color(255, 204, 51);

    private Color color2 = new Color(173, 0, 35);

    /**
     *
     */
    public PanelGauges() {
        initComponents();
        setMode(0);
    }

    private void initComponents() {
        Color color1 = new Color(173, 0, 35);
        setBackground(color1);
        setLayout(null);
        altimeterIndicator = new Altimeter();
        altimeterIndicator.setBounds(10, 10, 150, 150);
        add(altimeterIndicator);
        altimeterIndicator.init("Altitude");
        labelAltimeter = new JLabel("0.0");
        labelAltimeter.setHorizontalAlignment(JLabel.CENTER);
        labelAltimeter.setForeground(new java.awt.Color(255, 204, 51));
        labelAltimeter.setBounds(10, 170, 150, 20);
        add(labelAltimeter);
        airSpeedIndicator = new AirspeedIndicator();
        airSpeedIndicator.setBounds(10, 200, 150, 150);
        add(airSpeedIndicator);
        airSpeedIndicator.init("Air speed");
        labelAirSpeed = new JLabel("0.0 , 0.0");
        labelAirSpeed.setHorizontalAlignment(JLabel.CENTER);
        labelAirSpeed.setForeground(new java.awt.Color(255, 204, 51));
        labelAirSpeed.setBounds(10, 360, 150, 20);
        add(labelAirSpeed);
        attitudIndicator = new AttitudeIndicator();
        attitudIndicator.setBounds(170, 10, 310, 310);
        add(attitudIndicator);
        attitudIndicator.init("Attitude");
        labelAttitud = new JLabel("0.0 , 0.0");
        labelAttitud.setHorizontalAlignment(JLabel.CENTER);
        labelAttitud.setForeground(new java.awt.Color(255, 204, 51));
        labelAttitud.setBounds(170, 360, 310, 20);
        add(labelAttitud);
        headingIndicator = new HeadingIndicator();
        headingIndicator.setBounds(500, 10, 150, 150);
        add(headingIndicator);
        headingIndicator.init("Heading Indicator");
        labelHeading = new JLabel("0.0");
        labelHeading.setHorizontalAlignment(JLabel.CENTER);
        labelHeading.setForeground(new java.awt.Color(255, 204, 51));
        labelHeading.setBounds(500, 170, 150, 20);
        add(labelHeading);
        clockIndicator = new Clock();
        clockIndicator.setBounds(500, 200, 150, 150);
        add(clockIndicator);
        clockIndicator.init("Clock");
        labelClock = new JLabel("0.0");
        labelClock.setHorizontalAlignment(JLabel.CENTER);
        labelClock.setForeground(new java.awt.Color(255, 204, 51));
        labelClock.setBounds(500, 360, 150, 20);
        add(labelClock);
        labelGPS = new JLabel("GPS off");
        labelGPS.setHorizontalAlignment(JLabel.CENTER);
        labelGPS.setVerticalAlignment(JLabel.CENTER);
        panelGPS = new JPanel();
        panelGPS.setBackground(Color.red);
        panelGPS.add(labelGPS);
        panelGPS.setBounds(10, 390, 150, 30);
        add(panelGPS);
        labelModeManual = new JLabel("MANUAL");
        labelModeManual.setHorizontalAlignment(JLabel.CENTER);
        labelModeManual.setVerticalAlignment(JLabel.CENTER);
        panelModeManual = new JPanel();
        panelModeManual.setBackground(color1);
        panelModeManual.add(labelModeManual);
        panelModeManual.setBounds(170, 390, 112, 30);
        add(panelModeManual);
        labelModeAHD = new JLabel("AHD");
        labelModeAHD.setHorizontalAlignment(JLabel.CENTER);
        labelModeAHD.setVerticalAlignment(JLabel.CENTER);
        panelModeAHD = new JPanel();
        panelModeAHD.setBackground(color1);
        panelModeAHD.add(labelModeAHD);
        panelModeAHD.setBounds(292, 390, 112, 30);
        add(panelModeAHD);
        labelModeIHD = new JLabel("IHD");
        labelModeIHD.setHorizontalAlignment(JLabel.CENTER);
        labelModeIHD.setVerticalAlignment(JLabel.CENTER);
        panelModeIHD = new JPanel();
        panelModeIHD.setBackground(color1);
        panelModeIHD.add(labelModeIHD);
        panelModeIHD.setBounds(414, 390, 112, 30);
        add(panelModeIHD);
        labelModeWPN = new JLabel("WPN");
        labelModeWPN.setHorizontalAlignment(JLabel.CENTER);
        labelModeWPN.setVerticalAlignment(JLabel.CENTER);
        panelModeWPN = new JPanel();
        panelModeWPN.setBackground(color1);
        panelModeWPN.add(labelModeWPN);
        panelModeWPN.setBounds(537, 390, 112, 30);
        add(panelModeWPN);
    }

    /**
     *
     * @return
     */
    public boolean isGPS() {
        return GPS;
    }

    /**
     *
     * @param GPS
     */
    public void setGPS(boolean GPS) {
        this.GPS = GPS;
        if (GPS) {
            panelGPS.setBackground(color1);
            labelGPS.setText("GPS on");
        } else {
            panelGPS.setBackground(Color.red);
            labelGPS.setText("GPS off");
        }
    }

    /**
     *
     * @return
     */
    public double getAirSpeed() {
        return airSpeed;
    }

    /**
     *
     * @param airSpeed
     */
    public void setAirSpeed(double airSpeed) {
        this.airSpeed = airSpeed;
        double[] data = new double[1];
        data[0] = this.airSpeed;
        airSpeedIndicator.setData(data);
        String st = "" + airSpeed;
        if (st.length() > 5) {
            st = st.substring(0, 6);
        }
        labelAirSpeed.setText(st);
    }

    /**
     *
     * @return
     */
    public double getAltitud() {
        return altitud;
    }

    /**
     *
     * @param altitud
     */
    public void setAltitud(double altitud) {
        this.altitud = altitud;
        double[] data = new double[1];
        data[0] = altitud;
        altimeterIndicator.setData(data);
        String st = "" + altitud;
        if (st.length() > 5) {
            st = st.substring(0, 6);
        }
        labelAltimeter.setText(st);
    }

    /**
     *
     * @return
     */
    public double getHeading() {
        return heading;
    }

    /**
     *
     * @param heading
     */
    public void setHeading(double heading) {
        this.heading = heading;
        double[] data = new double[1];
        data[0] = heading;
        headingIndicator.setData(data);
        String st = "" + heading;
        if (st.length() > 5) {
            st = st.substring(0, 6);
        }
        labelHeading.setText(st);
    }

    /**
     *
     * @return
     */
    public double getPitch() {
        return pitch;
    }

    /**
     *
     * @param pitch
     * @param bank
     */
    public void setPitchAndBank(double pitch, double bank) {
        this.pitch = pitch;
        this.bank = bank;
        double[] data = new double[2];
        data[0] = pitch;
        data[1] = bank;
        attitudIndicator.setData(data);
        String st = "" + pitch;
        if (st.length() > 5) {
            st = st.substring(0, 6);
        }
        String st2 = "" + bank;
        if (st2.length() > 5) {
            st2 = st2.substring(0, 6);
        }
        labelAttitud.setText(st + " , " + st2);
    }

    /**
     *
     * @return
     */
    public double getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(double time) {
        this.time = time;
        double[] data = new double[3];
        data[0] = (double) ((int) (time / 60));
        data[1] = (double) ((int) (time / 1));
        data[2] = (double) ((int) time);
        clockIndicator.setData(data);
        String st = "" + time;
        if (st.length() > 5) {
            st = st.substring(0, 6);
        }
        labelClock.setText(st);
    }

    /**
     *
     * @return
     */
    public int getMode() {
        return mode;
    }

    /**
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
        setAHDMode((mode & 0x02) / 2);
        setIHDMode((mode & 0x04) / 4);
        setWPNMode((mode & 0x08) / 8);
        setManualMode((mode & 0x10) / 16);
    }

    void setAirSpeed(double iAS, double ias_ref) {
        this.airSpeed = iAS;
        airSpeedRef = ias_ref;
        double[] data = new double[2];
        data[0] = this.airSpeed;
        data[1] = this.airSpeedRef;
        airSpeedIndicator.setData(data);
        String st = "" + airSpeed + " , " + airSpeedRef;
        if (st.length() > 14) {
            st = st.substring(0, 15);
        }
        labelAirSpeed.setText(st);
    }

    public void setManualMode(int b) {
        if (b == 0) {
            panelModeManual.setBackground(Color.red);
        } else {
            if (b == 1) {
                panelModeManual.setBackground(color1);
            }
        }
    }

    public void setAHDMode(int b) {
        if (b == 0) {
            panelModeAHD.setBackground(Color.red);
        } else {
            if (b == 1) {
                panelModeAHD.setBackground(color1);
            }
        }
    }

    public void setIHDMode(int b) {
        if (b == 0) {
            panelModeIHD.setBackground(Color.red);
        } else {
            if (b == 1) {
                panelModeIHD.setBackground(color1);
            }
        }
    }

    public void setWPNMode(int b) {
        if (b == 0) {
            panelModeWPN.setBackground(Color.red);
        } else {
            if (b == 1) {
                panelModeWPN.setBackground(color1);
            }
        }
    }
}
