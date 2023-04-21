package ch.gmtech.lab.swing;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SwingSpike extends JFrame {

    private static final long serialVersionUID = 8263789753684186667L;

    public SwingSpike() {
    }

    private JPanel emptyVerticalArea(int size) {
        JPanel emptyVerticalArea = new JPanel();
        emptyVerticalArea.setLayout(new BoxLayout(emptyVerticalArea, BoxLayout.PAGE_AXIS));
        emptyVerticalArea.add(Box.createVerticalStrut(size));
        return emptyVerticalArea;
    }

    private JPanel row(JLabel label, JTextField field) {
        JPanel row1 = new JPanel(new BorderLayout());
        row1.add(label, BorderLayout.WEST);
        row1.add(field, BorderLayout.EAST);
        return row1;
    }

    public static void main(String[] args) {
        SwingSpike createCourseWindow = new SwingSpike();
        createCourseWindow.setVisible(true);
    }
}
