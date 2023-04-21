package jmri.jmrix.rps.swing.soundset;

import jmri.jmrix.rps.*;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Frame for control of the sound speed for the RPS system
 *
 * @author	   Bob Jacobsen   Copyright (C) 2008
 * @version   $Revision: 1.7 $
 */
public class SoundSetPane extends JPanel implements ReadingListener, MeasurementListener, PropertyChangeListener {

    public SoundSetPane() {
        super();
    }

    public void dispose() {
        Distributor.instance().removeReadingListener(this);
        Distributor.instance().removeMeasurementListener(this);
        Engine.instance().removePropertyChangeListener(this);
    }

    JTextField vsound;

    JTextField newval;

    JTextField dist;

    JTextField id;

    JTextField rcvr;

    JTextField speed;

    JCheckBox auto;

    JTextField gain;

    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (e.getPropertyName().equals("vSound")) {
            vsound.setText(nf.format(e.getNewValue()));
        }
    }

    public void initComponents() {
        nf = java.text.NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(5);
        nf.setGroupingUsed(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Current sound velocity: "));
        vsound = new JTextField(5);
        vsound.setEnabled(false);
        vsound.setText(nf.format(Engine.instance().getVSound()));
        p.add(vsound);
        this.add(p);
        this.add(new JSeparator());
        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("New sound velocity: "));
        newval = new JTextField(8);
        p.add(newval);
        JButton b = new JButton("Set");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                setPushed();
            }
        });
        p.add(b);
        this.add(p);
        this.add(new JSeparator());
        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Known Distance: "));
        dist = new JTextField(5);
        p.add(dist);
        p.add(new JLabel("Transmitter ID: "));
        id = new JTextField(5);
        p.add(id);
        p.add(new JLabel("Receiver Number: "));
        rcvr = new JTextField(3);
        p.add(rcvr);
        this.add(p);
        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Measured Speed: "));
        speed = new JTextField(8);
        speed.setEnabled(false);
        p.add(speed);
        auto = new JCheckBox("Auto Set");
        p.add(auto);
        p.add(new JLabel("Damping: "));
        gain = new JTextField(3);
        gain.setText("10.");
        p.add(gain);
        this.add(p);
        Distributor.instance().addReadingListener(this);
        Distributor.instance().addMeasurementListener(this);
        Engine.instance().addPropertyChangeListener(this);
    }

    void setPushed() {
        double val = Double.parseDouble(newval.getText());
        Engine.instance().setVSound(val);
    }

    java.text.NumberFormat nf;

    public void notify(Reading r) {
        try {
            if (!r.getID().equals(id.getText())) return;
            int i = Integer.parseInt(rcvr.getText());
            if (i < 1 || i > r.getNValues()) {
                log.warn("resetting receiver number");
                rcvr.setText("");
            }
            log.debug("Rcvr " + i + " saw " + r.getValue(i));
            double val = r.getValue(i);
            if (val < 100) {
                log.warn("time too small to use: " + val);
                return;
            }
            double newspeed = Double.parseDouble(dist.getText()) / val;
            speed.setText(nf.format(newspeed));
            if (auto.isSelected()) {
                double g = Double.parseDouble(gain.getText());
                if (g < 1) {
                    log.warn("resetting gain from " + gain.getText());
                    gain.setText("10.");
                    return;
                }
                double updatedspeed = (newspeed + g * Engine.instance().getVSound()) / (g + 1);
                Engine.instance().setVSound(updatedspeed);
            }
        } catch (Exception e) {
            log.debug("Error calculating speed: " + e);
            speed.setText("");
        }
    }

    public void notify(Measurement m) {
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SoundSetPane.class.getName());
}
