package org.dinnermate.gui.layout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.dinnermate.gui.layout.controller.EditorController;

public class EditorTest {

    /**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(new EditorController().constructPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
