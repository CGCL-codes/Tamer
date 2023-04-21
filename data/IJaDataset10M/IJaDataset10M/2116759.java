package dsb.bar.flowclient;

import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This is a window that displays fullscreen the amount displayedremaining
 * 
 * @author steffan
 * 
 */
public class FlowclientDisplayWindow extends JFrame {

    JLabel label = new JLabel();

    int width;

    int height;

    String message = new String();

    FlowclientModel model;

    Font labelfont;

    /**
	 * Create a Display window
	 * 
	 * @param displayedremaining
	 *            the displayed amount of liters
	 * @param width
	 *            of the screen
	 * @param height
	 *            of the screen
	 */
    public FlowclientDisplayWindow(double displayedremaining, int width, int height, FlowclientModel model) {
        this.width = width;
        this.height = height;
        message = Double.toString(displayedremaining);
        label.setText(message);
        this.model = model;
        this.add(label);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setFontSize();
        this.validateTree();
    }

    /**
	 * Update the window with a new displayedremaining
	 * 
	 * @param displayedremaining
	 */
    public void update(long displayedremaining) {
        Double readabledisplayedremaining = roundOneDecimal((double) displayedremaining / 1000);
        message = Double.toString(readabledisplayedremaining);
        label.setText(message);
        this.setFontSize();
        this.validateTree();
    }

    /**
	 * Set the font size to maximum size possible by the size of the display
	 * device
	 */
    private void setFontSize() {
        Font labelfont = label.getFont();
        int stringwidth = label.getFontMetrics(labelfont).stringWidth(message);
        double widthRatio = (double) width / (double) stringwidth;
        int newFontSize = (int) (labelfont.getSize() * widthRatio);
        int fontsizetouse = Math.min(newFontSize, height);
        label.setFont(new Font(labelfont.getName(), Font.PLAIN, fontsizetouse));
    }

    /**
	 * Round a double to one decimal.
	 * 
	 * @param d
	 * @return
	 */
    private double roundOneDecimal(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
