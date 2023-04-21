package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DrawText extends JTextArea {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 16));
        g.setColor(Color.GREEN);
        g.drawString("Core Java Foundation Classes", 20, 80);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Drawing Text");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawText panel = new DrawText();
        f.getContentPane().add(panel);
        f.setSize(250, 200);
        f.setVisible(true);
    }
}
