package org.hardtokenmgmt.admin.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.toedter.calendar.JDateChooser;

public class TestCalendar {

    private JFrame frame;

    /**
	 * Launch the application
	 * @param args
	 */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    TestCalendar window = new TestCalendar();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the application
	 */
    public TestCalendar() {
        createContents();
    }

    /**
	 * Initialize the contents of the frame
	 */
    private void createContents() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 375);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JDateChooser jcal = new JDateChooser();
        JButton button = new JButton("select");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                System.out.println("Date: " + jcal.getDate());
            }
        });
        JPanel panel = new JPanel();
        panel.add(jcal);
        panel.add(button);
        frame.getContentPane().add(panel);
    }
}
