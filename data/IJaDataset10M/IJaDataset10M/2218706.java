package drk.menu;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class MidMenu extends JFrame {

    public static JFrame middle;

    public static ImageIcon electric;

    @SuppressWarnings("serial")
    public static void middleMenu() {
        middle = new JFrame();
        middle.setUndecorated(true);
        middle.setSize(800, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        middle.setBounds((screenSize.width - 800) / 2, (screenSize.height - 600) / 2, 800, 600);
        electric = new ImageIcon("Electrical.jpg");
        Color transparent = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        JTextArea middleStory = new JTextArea("Congratulations you made it out of the floor. You will now " + "\nmove up to the next floor.\n\n\n\n\n\n\n\n" + "\tPress SpaceBar to continue...");
        JPanel middlePanel = new JPanel() {

            protected void paintComponent(Graphics g) {
                g.drawImage(electric.getImage(), 0, 0, null);
                super.paintComponent(g);
            }
        };
        middlePanel.setOpaque(false);
        middleStory.setEnabled(false);
        middleStory.setLineWrap(false);
        middleStory.setMargin(new Insets(130, 0, 0, 0));
        middleStory.setForeground(Color.white);
        middleStory.setBackground(transparent);
        Font font = new Font("Arial", Font.BOLD, 28);
        middleStory.setFont(font);
        middleStory.setEditable(false);
        middlePanel.add(middleStory, BorderLayout.CENTER);
        middle.add(middlePanel);
        middle.setVisible(true);
        middle.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_SPACE) middle.dispose();
            }
        });
        middle.requestFocus();
    }

    public static void main(String args[]) {
        middleMenu();
    }
}
