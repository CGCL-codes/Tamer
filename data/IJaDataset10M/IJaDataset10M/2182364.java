package org.jcvi.platetools.swing;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/**
 * 
 *
 * @author jsitz@jcvi.org
 */
public class ThemedJPanel extends JPanel {

    /** The Serial Version UID */
    private static final long serialVersionUID = 6531573790129118412L;

    /**
     * Creates a new <code>ThemedJPanel</code>.
     */
    public ThemedJPanel() {
        super(true);
        this.setLayout(new MigLayout());
    }

    public JLabel buildHeaderLabel(String text) {
        return this.buildLabel(text, SwingConstants.RIGHT, this.getHeaderFont());
    }

    public JLabel buildGroupHeaderLabel(String text) {
        return this.buildLabel(text, SwingConstants.CENTER, this.getGroupHeaderFont());
    }

    public JLabel buildLabel(String text, int alignment, Font font) {
        final JLabel label = new JLabel(text, alignment);
        label.setFont(font);
        return label;
    }

    public Font getHeaderFont() {
        return GlobalTheme.frameTheme().getBaseFont().deriveFont(Font.BOLD, 10);
    }

    public Font getGroupHeaderFont() {
        return this.getHeaderFont();
    }
}
