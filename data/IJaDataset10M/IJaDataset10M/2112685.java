package org.gvsig.gui.beans.comboboxconfigurablelookup.programmertests;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * <p>Sample of personalized combo box cell renderer.</p>
 * 
 * @version 08/02/2008
 * @author Pablo Piqueras Bartolom� (pablo.piqueras@iver.es) 
 */
public class SampleBasicComboBoxRenderer extends BasicComboBoxRenderer {

    private static final long serialVersionUID = -9044759678425798655L;

    /**
	 * <p>Creates a new instance of the <code>SampleBasicComboBoxRenderer</code> class.</p>
	 */
    public SampleBasicComboBoxRenderer() {
        super();
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) component.setBackground(Color.GRAY); else component.setBackground(new Color(((17 * index) % 256), ((31 * index) % 256), ((7 * index) % 256)));
        return component;
    }
}
