package org.myrobotlab.control;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.myrobotlab.service.GUIService;
import org.myrobotlab.service.OpenCV.FilterWrapper;

public class OpenCVFilterCannyGUI extends OpenCVFilterGUI {

    JSlider2 lowThreshold = new JSlider2(JSlider.HORIZONTAL, 0, 256, 0);

    JSlider2 highThreshold = new JSlider2(JSlider.HORIZONTAL, 0, 256, 256);

    JSlider2 apertureSize = new JSlider2(JSlider.HORIZONTAL, 1, 3, 1);

    public class JSlider2 extends JSlider {

        private static final long serialVersionUID = 1L;

        JLabel value = new JLabel();

        public JSlider2(int vertical, int i, int j, int k) {
            super(vertical, i, j, k);
            value.setText("" + k);
        }
    }

    public class AdjustSlider implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider2 slider = (JSlider2) e.getSource();
            Object[] params = new Object[3];
            params[0] = name;
            params[1] = slider.getName();
            params[2] = slider.getValue();
            if (slider.getName().compareTo("apertureSize") == 0) {
                params[2] = slider.getValue() * 2 + 1;
            }
            myGUI.send(boundServiceName, "setFilterCFG", params);
            slider.value.setText("" + slider.getValue());
        }
    }

    AdjustSlider change = new AdjustSlider();

    public OpenCVFilterCannyGUI(String boundFilterName, String boundServiceName, GUIService myService) {
        super(boundFilterName, boundServiceName, myService);
        lowThreshold.setName("lowThreshold");
        highThreshold.setName("highThreshold");
        apertureSize.setName("apertureSize");
        lowThreshold.addChangeListener(change);
        highThreshold.addChangeListener(change);
        apertureSize.addChangeListener(change);
        GridBagConstraints gc2 = new GridBagConstraints();
        TitledBorder title;
        JPanel j = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder("threshold");
        j.setBorder(title);
        gc.gridx = 0;
        gc.gridy = 0;
        j.add(new JLabel("low"), gc);
        ++gc.gridx;
        j.add(lowThreshold, gc);
        ++gc.gridx;
        j.add(lowThreshold.value, gc);
        ++gc.gridy;
        gc.gridx = 0;
        j.add(new JLabel("high"), gc);
        ++gc.gridx;
        j.add(highThreshold, gc);
        ++gc.gridx;
        j.add(highThreshold.value, gc);
        display.add(j, gc2);
        gc2.gridy = 1;
        gc2.gridx = 0;
        j = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder("apertureSize");
        j.setBorder(title);
        j.add(apertureSize);
        j.add(apertureSize.value);
        display.add(j, gc2);
    }

    @Override
    public void apply() {
    }

    @Override
    public JPanel getDisplay() {
        return display;
    }

    @Override
    public void setFilterData(FilterWrapper filter) {
    }
}
