package objectDraw.tool;

import java.awt.*;
import javax.swing.*;
import objectDraw.canvas.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;

/**
 * Class that allows the user to change the color of figures on the canvas.
 *
 */
public class ColorMenu extends JColorChooser {

    private static final long serialVersionUID = 968215056554136661L;

    private CanvasLook canvas;

    public ColorMenu(CanvasLookImpl c) {
        super(Color.BLACK);
        canvas = c;
        this.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] panel = this.getChooserPanels();
        this.removeChooserPanel(panel[1]);
        this.removeChooserPanel(panel[2]);
        final ColorSelectionModel csm = this.getSelectionModel();
        csm.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                ((AbstractCanvas) canvas.getCanvas()).setColor(csm.getSelectedColor());
                if (canvas.getCanvas().getSelection() != null) {
                    canvas.getCanvas().getSelection().setColor(csm.getSelectedColor());
                }
            }
        });
        this.setBackground(Color.DARK_GRAY);
    }
}
