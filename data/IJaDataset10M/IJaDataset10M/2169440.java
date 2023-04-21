package view;

import java.awt.image.BufferedImage;
import javax.swing.JApplet;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 * 
 * @author spock
 */
class ScreenManager extends JApplet {

    private BufferedImage imageBuffer;

    private Overview overview;

    private CommandQueueOverview commandQueueOverview;

    private MainScreen mainScreen;

    private boolean showOverview = false;

    private boolean showCommandQueueOverview = false;

    private static final int APPWIDTH = 800;

    private static final int APPHEIGHT = 600;

    public static final long serialVersionUID = 1L;

    Graphics2D graphix;

    private static KeyListener listen;

    private static JFrame frame;

    public void init() {
        getContentPane().setLayout(new BorderLayout());
        frame = new JFrame("Random Terrain Septic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(APPWIDTH, APPHEIGHT));
        imageBuffer = new BufferedImage(APPWIDTH, APPHEIGHT, BufferedImage.TYPE_INT_ARGB);
        mainScreen = new MainScreen();
        mainScreen.refreshImage();
        overview = new Overview();
        overview.refreshImage();
        commandQueueOverview = new CommandQueueOverview();
        commandQueueOverview.refreshImage();
        graphix = (Graphics2D) imageBuffer.getGraphics();
        graphix.drawImage(mainScreen.getImage(), 0, 0, this);
        if (showOverview) {
            graphix.drawImage(overview.getImage(), 480, 0, this);
            if (showCommandQueueOverview) {
                graphix.drawImage(commandQueueOverview.getImage(), 230, 40, this);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                displayBufferedImage(imageBuffer);
            }
        });
    }

    JFrame getFrame() {
        return frame;
    }

    MainScreen getMainScreen() {
        return mainScreen;
    }

    Overview getOverview() {
        return overview;
    }

    CommandQueueOverview getCommandQueueOverview() {
        return commandQueueOverview;
    }

    void refreshMe() {
        imageBuffer = new BufferedImage(APPWIDTH, APPHEIGHT, BufferedImage.TYPE_INT_ARGB);
        graphix = (Graphics2D) imageBuffer.getGraphics();
        graphix.drawImage(mainScreen.getImage(), 0, 0, null);
        if (showOverview) {
            graphix.drawImage(overview.getImage(), 480, 20, null);
            if (showCommandQueueOverview) {
                graphix.drawImage(commandQueueOverview.getImage(), 230, 40, null);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                displayBufferedImage(imageBuffer);
            }
        });
    }

    private void displayBufferedImage(BufferedImage image) {
        this.setContentPane(new JLabel(new ImageIcon(image)));
        this.validate();
    }

    void hideOverview() {
        showOverview = false;
    }

    void showOverview() {
        showOverview = true;
    }

    void hideCommandQueueOverview() {
        showCommandQueueOverview = false;
    }

    void showCommandQueueOverview(String[] commands) {
        if (showOverview) {
            showCommandQueueOverview = true;
            commandQueueOverview.setCommands(commands);
        }
    }

    BufferedImage getImage() {
        return null;
    }
}
