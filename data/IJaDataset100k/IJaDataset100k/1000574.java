package com.google.code.timetrail.gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.UIManager;
import com.google.code.timetrail.backend.*;

public class GameFrame extends JFrame {

    /**
	 * acts as the gui for the game
	 * @author Apurv 
	 * @version 1.01
	 * added inventory instantiation to the frame
	 */
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    protected int addButtonCount;

    private Control gameControl;

    /**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Unable to use native look and feel");
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    GameFrame frame = new GameFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the frame.
	 */
    public GameFrame() {
        gameControl = new Control();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new CardLayout(0, 0));
        contentPane.add(new TitleFrame(gameControl, contentPane), "name_1317839433132283000");
        pack();
    }
}
