package org.megadix.jfcm.samples;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.megadix.jfcm.CognitiveMap;
import org.megadix.jfcm.Concept;
import org.megadix.jfcm.utils.FcmIO;

public class PhotographyExposureTriangle extends JApplet implements ItemListener {

    CognitiveMap map;

    Map<String, JSlider> sliders = new HashMap<String, JSlider>();

    SortedSet<String> calculatedConcepts = new TreeSet<String>();

    public PhotographyExposureTriangle() {
        super();
    }

    @Override
    public void init() {
        try {
            map = FcmIO.loadXml(getClass().getResourceAsStream("PhotographyExposureTriangle.xml")).get(0);
            calculatedConcepts.add("blur");
            calculatedConcepts.add("depth_of_field");
            calculatedConcepts.add("grain");
            resetMap();
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void createGUI() {
        getContentPane().add(createMainPanel());
        getContentPane().add(createButtons(), BorderLayout.SOUTH);
    }

    private JPanel createMainPanel() {
        JLabel lbl;
        GridLayout grid = new GridLayout(8, 2);
        JPanel controls = new JPanel(grid);
        lbl = new JLabel("Concept", SwingConstants.CENTER);
        controls.add(lbl);
        lbl = new JLabel("Value", SwingConstants.CENTER);
        controls.add(lbl);
        addControls("iso", controls);
        addControls("shutter_speed", controls);
        addControls("aperture", controls);
        addControls("blur", controls);
        addControls("depth_of_field", controls);
        addControls("grain", controls);
        return controls;
    }

    private void addControls(String name, JPanel panel) {
        panel.add(new JLabel(name));
        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, -100, 100, 0);
        sliders.put(name, slider);
        panel.add(slider);
    }

    private JPanel createButtons() {
        JButton bt;
        JPanel buttons = new JPanel();
        bt = new JButton("Calculate");
        bt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateMap();
                updateControls();
                repaint();
            }
        });
        buttons.add(bt);
        bt = new JButton("Reset");
        bt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resetMap();
                updateControls();
                repaint();
            }
        });
        buttons.add(bt);
        return buttons;
    }

    void resetMap() {
        Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept c = iter.next();
            c.setFixedOutput(false);
            c.setOutput(0.0);
            c.setPrevOutput(null);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        updateMap();
    }

    private void enableControls(boolean enable) {
        for (JSlider slider : sliders.values()) {
            slider.setEnabled(enable);
        }
    }

    private void updateMap() {
        enableControls(false);
        Iterator<Map.Entry<String, JSlider>> sliderIter = sliders.entrySet().iterator();
        while (sliderIter.hasNext()) {
            Map.Entry<String, JSlider> entry = sliderIter.next();
            if (calculatedConcepts.contains(entry.getKey())) {
                continue;
            }
            Concept c = map.getConcept(entry.getKey());
            c.setOutput(sliders.get(entry.getKey()).getValue() / 100.0);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                map.execute();
                updateControls();
                enableControls(true);
                repaint();
            }
        });
    }

    private void updateControls() {
        logDebug();
        Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept c = iter.next();
            if (!calculatedConcepts.contains(c.getName())) {
                continue;
            }
            setSliderOutput(c.getName());
        }
    }

    private void setSliderOutput(String name) {
        Concept c = map.getConcept(name);
        if (c.isFixedOutput()) return;
        if (c.getOutput() != null) {
            double v = c.getOutput().doubleValue() * 100.0;
            v = Math.min(v, 100.0);
            v = Math.max(v, -100.0);
            JSlider sl = sliders.get(name);
            sl.setValue((int) v);
        }
    }

    private void logDebug() {
        Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept c = iter.next();
            System.out.println(c.getName() + "\t\t" + c.getOutput());
        }
    }
}
