package com.arykow.applications.ugabe.standalone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdvancedAudioPropertiesDialog implements ComponentListener, ChangeListener, ActionListener {

    JFrame owner;

    AudioDriver ad;

    JDialog dialog;

    public AdvancedAudioPropertiesDialog(JFrame o) {
        if (!(false)) throw new Error("Assertion failed: " + "false");
    }

    ArrayList<Integer> samplingRates = new ArrayList<Integer>();

    ArrayList<Mixer.Info> mixers = new ArrayList<Mixer.Info>();

    private JComboBox outputDevices;

    private JCheckBox useStereo;

    private JSlider samplingRateSlider;

    private JSpinner samplingRateSpinner;

    private JSlider bufferSizeSlider;

    private JSpinner bufferSizeSpinner;

    private JSlider outputIntervalSlider;

    private JSpinner outputIntervalSpinner;

    GUI gui;

    public AdvancedAudioPropertiesDialog(JFrame o, AudioDriver ad, GUI gui) {
        owner = o;
        this.ad = ad;
        this.gui = gui;
        dialog = new JDialog(owner, "Advanced Audio Properties", true);
        dialog.setResizable(false);
        if (ad != null) {
            outputDevices = new JComboBox();
            ad.stop();
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (int i = 0; i < mixers.length; i++) {
                Mixer mixer = AudioSystem.getMixer(mixers[i]);
                Line.Info[] info = mixer.getSourceLineInfo();
                boolean b = false;
                for (int j = 0; j < info.length; ++j) {
                    if (info[j].toString().indexOf("buffer") >= 0) b = true;
                }
                if (b) {
                    this.mixers.add(mixers[i]);
                    outputDevices.addItem(mixers[i].getName());
                }
            }
            ad.start();
            for (int i = 0; i < outputDevices.getItemCount(); ++i) {
                if (((Mixer.Info) (this.mixers.get(i))).equals(ad.getMixerInfo())) outputDevices.setSelectedIndex(i);
            }
            samplingRates.add(new Integer(8000));
            samplingRates.add(new Integer(11025));
            samplingRates.add(new Integer(22050));
            samplingRates.add(new Integer(32000));
            samplingRates.add(new Integer(44056));
            samplingRates.add(new Integer(44100));
            samplingRates.add(new Integer(47250));
            samplingRates.add(new Integer(48000));
            useStereo = new JCheckBox("Stereo output");
            useStereo.setSelected(ad.getChannels() == 2);
            samplingRateSlider = new JSlider(0, samplingRates.size() - 1);
            samplingRateSlider.setPaintTicks(true);
            samplingRateSlider.setSnapToTicks(true);
            samplingRateSlider.setPaintTrack(true);
            samplingRateSlider.setPaintLabels(true);
            samplingRateSlider.updateUI();
            ArrayList<Integer> sr = new ArrayList<Integer>();
            for (int i = 0; i < samplingRates.size(); ++i) sr.add(samplingRates.get(i));
            samplingRateSpinner = new JSpinner(new SpinnerListModel(sr));
            bufferSizeSlider = new JSlider(1 << 5, 1 << 16);
            bufferSizeSpinner = new JSpinner(new SpinnerNumberModel(1 << 5, 1, 1 << 16, 1));
            bufferSizeSlider.setValue(ad.getBufferSize());
            bufferSizeSpinner.setValue(new Integer(ad.getBufferSize()));
            outputIntervalSlider = new JSlider(0, bufferSizeSlider.getMaximum());
            outputIntervalSpinner = new JSpinner(new SpinnerNumberModel(ad.getOutputInterval(), 1, ad.getBufferSize(), ad.getFrameSize()));
            outputIntervalSlider.setValue(ad.getOutputInterval());
            outputIntervalSpinner.setValue(new Integer(ad.getOutputInterval()));
            samplingRateSlider.setValue(samplingRates.indexOf(Integer.valueOf(ad.getSampleRate())));
            samplingRateSpinner.setValue(Integer.valueOf(ad.getSampleRate()));
            bufferSizeSlider.setValue(ad.getBufferSize());
            bufferSizeSpinner.setValue(Integer.valueOf(ad.getBufferSize()));
            outputIntervalSlider.setValue(ad.getOutputInterval());
            outputIntervalSpinner.setValue(Integer.valueOf(ad.getOutputInterval()));
            useStereo.addChangeListener(this);
            outputDevices.addActionListener(this);
            bufferSizeSlider.addChangeListener(this);
            bufferSizeSpinner.addChangeListener(this);
            samplingRateSlider.addChangeListener(this);
            samplingRateSpinner.addChangeListener(this);
            outputIntervalSlider.addChangeListener(this);
            outputIntervalSpinner.addChangeListener(this);
            BorderLayout bl = new BorderLayout();
            dialog.setLayout(bl);
            JPanel container = new JPanel();
            container.setLayout(new GridLayout(8, 2));
            container.add(new JLabel("Output device:"));
            container.add(new JPanel());
            container.add(outputDevices);
            container.add(useStereo);
            container.add(new JLabel("Sampling rate:"));
            container.add(new JPanel());
            container.add(samplingRateSlider);
            container.add(samplingRateSpinner);
            container.add(new JLabel("Buffer size:"));
            container.add(new JPanel());
            container.add(bufferSizeSlider);
            container.add(bufferSizeSpinner);
            container.add(new JLabel("Output interval:"));
            container.add(new JPanel());
            container.add(outputIntervalSlider);
            container.add(outputIntervalSpinner);
            dialog.add(new JPanel(), BorderLayout.NORTH);
            dialog.add(new JPanel(), BorderLayout.SOUTH);
            dialog.add(new JPanel(), BorderLayout.WEST);
            dialog.add(new JPanel(), BorderLayout.EAST);
            dialog.add(container, BorderLayout.CENTER);
        }
        dialog.addComponentListener(this);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        Dimension d = owner.getSize();
        Point p = new Point();
        p.setLocation((owner.getLocation().getX() + (d.getWidth() / 2)) - (dialog.getWidth() / 2), (owner.getLocation().getY() + d.getHeight() / 2) - (dialog.getHeight() / 2));
        dialog.setLocation(p);
    }

    public void showWindow() {
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(outputDevices)) {
            ad.setMixerInfo((Mixer.Info) mixers.get(outputDevices.getSelectedIndex()));
            gui.saveConfig();
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(samplingRateSlider)) {
            samplingRateSpinner.setValue((Integer) samplingRates.get(samplingRateSlider.getValue()));
            ad.setSampleRate(((Integer) samplingRates.get(samplingRateSlider.getValue())).intValue());
            gui.saveConfig();
        } else if (e.getSource().equals(samplingRateSpinner)) {
            for (int i = 0; i < samplingRates.size(); ++i) {
                if (samplingRateSpinner.getValue() == samplingRates.get(i)) samplingRateSlider.setValue(i);
            }
            ad.setSampleRate(((Integer) samplingRates.get(samplingRateSlider.getValue())).intValue());
            gui.saveConfig();
        } else if (e.getSource().equals(bufferSizeSlider)) {
            ad.setBufferSize(bufferSizeSlider.getValue());
            bufferSizeSlider.setValue(ad.getBufferSize());
            bufferSizeSpinner.setValue(new Integer(ad.getBufferSize()));
            outputIntervalSlider.setMaximum(ad.getBufferSize());
            outputIntervalSlider.setValue(ad.getOutputInterval());
            outputIntervalSpinner.setModel(new SpinnerNumberModel(ad.getOutputInterval(), 1, ad.getBufferSize(), ad.getFrameSize()));
            gui.saveConfig();
        } else if (e.getSource().equals(bufferSizeSpinner)) {
            ad.setBufferSize(((Integer) bufferSizeSpinner.getValue()).intValue());
            bufferSizeSlider.setValue(ad.getBufferSize());
            bufferSizeSpinner.setValue(new Integer(ad.getBufferSize()));
            outputIntervalSlider.setMaximum(ad.getBufferSize());
            outputIntervalSlider.setValue(ad.getOutputInterval());
            outputIntervalSpinner.setModel(new SpinnerNumberModel(ad.getOutputInterval(), 1, ad.getBufferSize(), ad.getFrameSize()));
            gui.saveConfig();
        } else if (e.getSource().equals(outputIntervalSlider)) {
            ad.setOutputInterval(outputIntervalSlider.getValue());
            outputIntervalSpinner.setValue(Integer.valueOf(ad.getOutputInterval()));
            outputIntervalSlider.setValue(ad.getOutputInterval());
            gui.saveConfig();
        } else if (e.getSource().equals(outputIntervalSpinner)) {
            ad.setOutputInterval(((Integer) outputIntervalSpinner.getValue()).intValue());
            outputIntervalSpinner.setValue(Integer.valueOf(ad.getOutputInterval()));
            outputIntervalSlider.setValue(ad.getOutputInterval());
            gui.saveConfig();
        } else if (e.getSource().equals(useStereo)) {
            if (useStereo.getModel().isSelected()) ad.setChannels(2); else ad.setChannels(1);
            gui.saveConfig();
        }
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
    }
}
