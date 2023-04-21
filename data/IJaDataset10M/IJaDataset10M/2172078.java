package MapsGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author elena
 *
 */
public class ShowMapImage extends JPanel {

    MapImage mapImage;

    public ShowMapImage() {
        super(new GridBagLayout());
        GridBagLayout gridbag = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(1, 1, 1, 1);
        mapImage = new MapImage();
        gridbag.setConstraints(mapImage, c);
        add(mapImage);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("My Map Image");
        frame.setIconImage(new ImageIcon("images/world_thumb.gif").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent showMapImage = new ShowMapImage();
        showMapImage.setOpaque(true);
        frame.setContentPane(showMapImage);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
