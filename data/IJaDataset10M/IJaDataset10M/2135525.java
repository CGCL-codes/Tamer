package basicgui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextAreaWithButtons {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Text Box with Buttons");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel primary = new TextAreaWithButtonsPanel();
        frame.getContentPane().add(primary);
        frame.pack();
        frame.setVisible(true);
    }
}
