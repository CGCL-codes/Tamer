package org.dinopolis.gpstool.plugin.dufourmap;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * SliderFrame for setAlphaChannel on Dufourmaps
 * 
 * @author Samuel Benz
 * @version $Revision: 788 $
 */
public class SliderFrame extends JPanel implements ActionListener, WindowListener, ChangeListener {

    private static final long serialVersionUID = -6302133054563154581L;

    DufourmapLayer maplayer;

    public SliderFrame(DufourmapLayer layer_) {
        maplayer = layer_;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel sliderLabel = new JLabel("Map opaqueness", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        float initSlide = maplayer.getAlphaValue() * 100;
        JSlider alphaChannel = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) initSlide);
        alphaChannel.addChangeListener(this);
        alphaChannel.setMajorTickSpacing(50);
        alphaChannel.setMinorTickSpacing(5);
        alphaChannel.setPaintTicks(true);
        alphaChannel.setPaintLabels(true);
        alphaChannel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(sliderLabel);
        add(alphaChannel);
    }

    /** Add a listener for window events. */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            float alphaValue = (float) source.getValue();
            maplayer.setAlphaValue(alphaValue / 100);
        }
    }
}
