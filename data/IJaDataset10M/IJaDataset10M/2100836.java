package whereisnow.ui.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class WinErrorWindow extends JFrame {

    public WinErrorWindow(String errorMessage) {
        Container container = getContentPane();
        BoxLayout bl = new BoxLayout(container, BoxLayout.Y_AXIS);
        setLayout(bl);
        WinErrorPanel errorPanel = new WinErrorPanel(errorMessage);
        WinLogoPanel logo = new WinLogoPanel();
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/link.png"));
        setIconImage(image);
        container.add(logo);
        container.add(errorPanel, CENTER_ALIGNMENT);
        setContentPane(container);
        setResizable(false);
        setTitle("Desk.Now - Error");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(350, 180));
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int xPos = (screenWidth / 2) - (frameWidth / 2);
        int yPos = (screenHeight / 2) - (frameHeight / 2);
        setBounds(xPos, yPos, frameWidth, frameHeight);
        setVisible(true);
    }
}
