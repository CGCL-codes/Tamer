package elsimulatorjava;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author plitter
 */
public class FloorButton extends JComponent {

    private static Elevator[] elevators;

    private static short elevatorIndex;

    private boolean pressed;

    private static BufferedImage[] blankButton = new BufferedImage[2];

    private String buttonLabel;

    short buttonNumber;

    public FloorButton(String bL) throws IOException {
        buttonLabel = bL;
        blankButton[0] = ImageIO.read(new File("images/Blank_Button.png"));
        blankButton[1] = ImageIO.read(new File("images/Blank_Button_Press.png"));
        setPreferredSize(new Dimension(blankButton[0].getWidth(), blankButton[0].getHeight()));
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent me) {
                try {
                    pressed();
                } catch (IOException ex) {
                }
            }
        });
        String numString = new String();
        for (byte i = 0; i < buttonLabel.length(); i++) {
            if (buttonLabel.charAt(i) != ' ') {
                numString += buttonLabel.charAt(i);
            }
        }
        this.buttonNumber = Short.valueOf(numString);
        repaint();
    }

    public void pressed() throws IOException {
        if (!pressed) {
            elevators[elevatorIndex].addPerson(new Person(this.buttonNumber));
            pressed = true;
            repaint();
        }
    }

    protected void setButton(boolean p) {
        pressed = p;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (elevators[elevatorIndex].isDestFloor(buttonNumber)) {
            pressed = true;
        } else {
            pressed = false;
        }
        if (!pressed) {
            g.drawImage(blankButton[0], 0, 0, null);
        } else {
            g.drawImage(blankButton[1], 0, 0, null);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Monospaced Plain", Font.PLAIN, 38);
        AttributedString as1 = new AttributedString(buttonLabel);
        as1.addAttribute(TextAttribute.FONT, font);
        g2d.drawString(as1.getIterator(), 1, 40);
    }

    protected static void setElevatorIndex(short eIdx) {
        elevatorIndex = eIdx;
    }

    protected static void setElevators(Elevator[] e) {
        elevators = e;
    }
}
