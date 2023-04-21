package aerith.swing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.jdesktop.swingx.JXImagePanel;
import aerith.components.AerithSplashScreen;

public class AerithSplashScreenTest {

    private JXImagePanel regularContentPanel;

    private AerithSplashScreen layeredMyGlassPane;

    private JFrame frame;

    private final int HEIGHT = 600, WIDTH = 600;

    public AerithSplashScreenTest() throws Exception {
        initializeInterface();
    }

    private void initializeInterface() throws Exception {
        layeredMyGlassPane = new AerithSplashScreen();
        frame = new JFrame("Reflection Demo");
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initContentPanel();
        frame.setGlassPane(layeredMyGlassPane);
        layeredMyGlassPane.setVisible(true);
        frame.setContentPane(regularContentPanel);
        frame.pack();
        frame.setVisible(true);
        regularContentPanel.setVisible(false);
    }

    private void initContentPanel() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image coverArt = tk.getImage(getClass().getResource("/DesertLandscape.png"));
        regularContentPanel = new JXImagePanel();
        regularContentPanel.setLayout(new GridLayout());
        regularContentPanel.setImage(coverArt);
    }

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        new AerithSplashScreenTest();
    }
}
