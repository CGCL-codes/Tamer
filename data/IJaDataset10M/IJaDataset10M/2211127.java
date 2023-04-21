package org.gvsig.gui.beans.colorchooser;

import java.awt.Dimension;
import java.util.EventObject;
import org.gvsig.gui.beans.TestUI;

/**
* Test del ColorChooser
*
* @version 08/05/2007
* @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
*/
public class TestColorChooser implements ColorChooserListener {

    private TestUI jFrame = new TestUI("TestColorChooser");

    private ColorChooser colorChooser = null;

    public TestColorChooser() {
        initialize();
    }

    private void initialize() {
        colorChooser = new ColorChooser();
        colorChooser.addValueChangedListener(this);
        colorChooser.setEnabled(true);
        jFrame.setSize(new Dimension(258, 167));
        jFrame.setContentPane(colorChooser);
        jFrame.setVisible(true);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new TestColorChooser();
    }

    public void actionValueChanged(EventObject e) {
        System.out.println("Changed: " + colorChooser.getColor() + ", " + colorChooser.getColor().getAlpha());
    }

    public void actionValueDragged(EventObject e) {
        System.out.println("Dragged: " + colorChooser.getColor() + ", " + colorChooser.getColor().getAlpha());
    }
}
