package client.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MyTextField extends JTextField {

    public MyTextField(String text) {
        super(text);
        init();
    }

    public MyTextField() {
        super();
        init();
    }

    Font real = new Font("Arial", 3, 15);

    Font dum = new Font("Arial", 0, 15);

    public void init() {
        this.setFont(dum);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon icon;
        Image i;
        icon = new ImageIcon("images/background_2.png");
        i = icon.getImage();
        g.drawImage(i, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
        g.setFont(real);
        String text = this.getText();
        int index = text.length() - 13;
        if (index > 0) {
            text = text.substring(index);
        }
        if (text != null) {
            int div_x = 2;
            int div_y = (this.getBounds().height) / 2 + 4;
            g.setColor(new Color(5, 5, 5));
            g.drawString(text, div_x + 2, div_y + 2);
            g.setColor(new Color(0, 0, 0));
            g.drawString(text, div_x - 1, div_y + 1);
            g.drawString(text, div_x - 1, div_y - 1);
            g.drawString(text, div_x + 1, div_y - 1);
            g.drawString(text, div_x + 1, div_y + 1);
            g.setColor(Color.white);
            g.drawString(text, div_x, div_y);
            g.setFont(dum);
        }
        g.setColor(Color.blue);
    }
}
