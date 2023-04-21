package CADI.Viewer.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class implements a panel where a line of test (status line) will
 * be displayed.
 * 
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.0 2007-2012/12/09
 */
public class StatusText extends JPanel {

    /**
	 * String to be printed in the status bar.
	 */
    private String text = null;

    /**
	 * Label where the text will be printed.
	 */
    private JLabel label = null;

    /**
	 * Constructor.
	 * 
	 * @param width width of the panel
	 * @param height height of the panel
	 */
    public StatusText(int width, int height) {
        super();
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        if (label == null) {
            label = new JLabel("");
            label.setFont(new Font("SansSerif", Font.PLAIN, 10));
            Dimension dims = new Dimension(width, height);
            label.setSize(dims);
            label.setPreferredSize(dims);
            label.setMinimumSize(dims);
        }
        add(label);
    }

    /**
	 * Prints a single line of text in the status bar.
	 *  
	 * @param text the line of text to be displayed.
	 */
    public void setText(String text) {
        this.text = text;
        label.setText(this.text);
    }
}
