package org.jcvi.tasker.table;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A <code>FieldCell</code> is a simple label with optional text contents.  The cell is drawn
 * with only a bottom border, leaving a visual cue for a hand-filled area in the case the
 * cell is printed without a value.
 *
 * @author jsitz@jcvi.org
 */
public class FieldCell extends JLabel {

    /** The Serial Version UID. */
    private static final long serialVersionUID = -8919679481062883289L;

    /**
     * Constructs a new <code>FieldCell</code>.
     *
     * @param value The text to put in the cell.
     */
    public FieldCell(String value) {
        super(value);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    }
}
