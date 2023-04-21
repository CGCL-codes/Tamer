package org.jfree.beans.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A GUI for editing paint instances.  INCOMPLETE.
 */
public class PaintEditorGUI extends JPanel implements ChangeListener {

    JColorChooser chooser;

    /**
     * Default constructor.
     */
    public PaintEditorGUI() {
        setLayout(new BorderLayout());
        this.chooser = new JColorChooser();
        this.chooser.getSelectionModel().addChangeListener(this);
        add(this.chooser);
    }

    /**
     * Returns the paint.
     * 
     * @return The paint.
     */
    public Paint getPaint() {
        return this.chooser.getColor();
    }

    /**
     * Sets the paint.
     * 
     * @param paint  the paint.
     */
    public void setPaint(Paint paint) {
        if (paint instanceof Color) {
            this.chooser.getSelectionModel().setSelectedColor((Color) paint);
        }
    }

    public void stateChanged(ChangeEvent e) {
        firePropertyChange("paint", null, this.chooser.getColor());
    }
}
