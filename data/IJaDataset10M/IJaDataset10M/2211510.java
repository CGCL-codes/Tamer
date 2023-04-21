package edu.ucla.stat.SOCR.core;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

/**
 * this class used to let the user change value
 * <p>
 * It compose of two parts, a JSlider and a TextField.
 * The TextField used to allow user type value directly.
 *
 * When value changed, it will notify any registered Observers.
 * The Observers can get the value from getValue() or from getValueAsInt().
 * It also allow to set a new value by setValue().
 *
 * It has setTitle(), setRange(), setValue() methods to provide similar
 * functions as Original Scrooler
 *
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class IntValueSetter extends JPanel {

    private int type;

    private Observable observable = new Observable() {

        public void notifyObservers() {
            setChanged();
            super.notifyObservers(IntValueSetter.this);
        }
    };

    /**
     *
     * @uml.property name="value"
     */
    private int value;

    private int minv;

    private int maxv;

    /**
     *
     * @uml.property name="slider"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JSlider slider;

    /**
     *
     * @uml.property name="valueText"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JTextField valueText = new JTextField(5);

    private boolean enterTyped = false;

    /**
     *  create a Continuous type valueSetter, the real value will be multiply by
     * the scale
     * @param title
     * @param min
     * @param max
     */
    public IntValueSetter(String title, int min, int max) {
        this(title, min, max, (min + max) / 2);
    }

    /**
     * create a discrete type valueSetter,
     */
    public IntValueSetter(String title, int min, int max, int initial) {
        constructing(title, min, max, initial);
    }

    private void constructing(String title, int min, int max, int initial) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeSlider(min, max, initial);
        add(slider);
        add(Box.createVerticalStrut(8));
        add(valueText);
        TitledBorder tb = new TitledBorder(new EtchedBorder(), title);
        setBorder(tb);
        valueText.setMaximumSize(valueText.getPreferredSize());
        valueText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                textAreaPeformAction();
            }
        });
    }

    private void initializeSlider(int min, int max, int initial) {
        value = initial;
        this.minv = min;
        this.maxv = max;
        Hashtable labels = new Hashtable();
        labels.put(new Integer(min), new JLabel(String.valueOf(min)));
        labels.put(new Integer(max), new JLabel(String.valueOf(max)));
        slider = new JSlider(min, max, initial);
        slider.setMajorTickSpacing(Math.round((max - min) / 10.0f));
        slider.setMinorTickSpacing(Math.round((max - min) / 20.0f));
        slider.setPreferredSize(new Dimension(70, 50));
        slider.setPaintTicks(true);
        slider.setSnapToTicks(false);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (enterTyped) return;
                value = slider.getValue();
                setTextAreaValue();
            }
        });
        setTextAreaValue();
    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    /**
     *
     * @uml.property name="value"
     */
    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        String nv = String.valueOf(v);
        valueText.setText(nv);
        textAreaPeformAction();
    }

    public void setRange(int min, int max) {
        setRange(min, max, (min + max) / 2);
    }

    public void setRange(int min, int max, int v) {
        if (min >= max) return;
        if (v < min) v = min; else if (v > max) v = max;
        remove(slider);
        initializeSlider(min, max, v);
        add(slider, 0);
        revalidate();
        repaint();
    }

    public void setTitle(String title) {
        ((TitledBorder) getBorder()).setTitle(title);
    }

    public void setEnabled(boolean b) {
        slider.setEnabled(b);
        valueText.setEnabled(b);
        super.setEnabled(b);
    }

    private void setTextAreaValue() {
        String ov = valueText.getText();
        String nv = String.valueOf(value);
        if (ov.equals(nv)) return;
        valueText.setText(nv);
        observable.notifyObservers();
    }

    private void setSliderValue() {
        slider.setValue(value);
    }

    /**
     *  do what needed after the text area hit a return
     */
    private void textAreaPeformAction() {
        try {
            value = Integer.parseInt(valueText.getText());
            if (value < minv && minv >= 0) {
                valueText.selectAll();
                return;
            }
            if (value < minv) setRange(value, maxv, value); else if (value > maxv) setRange(minv, value, value);
            enterTyped = true;
            setSliderValue();
            observable.notifyObservers();
            enterTyped = false;
        } catch (Exception e) {
            e.printStackTrace();
            valueText.selectAll();
        }
    }
}
