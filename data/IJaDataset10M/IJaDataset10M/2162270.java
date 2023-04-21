package collection.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class TaskWindow extends JPanel {

    JFrame frame;

    JButton add, search, back, subset;

    Collection userCollection = new Collection();

    public TaskWindow() {
        super(new BorderLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel button_pane = new JPanel(new GridLayout(4, 1, 50, 10));
        button_pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("What would you like to do?"), BorderFactory.createEmptyBorder(10, 50, 10, 50)));
        search = new JButton("Search");
        search.setActionCommand("Search");
        search.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("THIS WILL GO TO THE SEARCH WINDOW");
            }
        });
        add = new JButton("Add/Remove");
        add.setActionCommand("Add/Remove");
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("THIS WILL GO TO THE ADD/REMOVE WINDOW");
            }
        });
        subset = new JButton("Subset");
        subset.setActionCommand("Subset");
        subset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("THIS WILL GO TO THE SUBSET WINDOW");
            }
        });
        back = new JButton("Back");
        back.setActionCommand("Back");
        back.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.out.print("back PERFORMED");
            }
        });
        button_pane.add(search);
        button_pane.add(add);
        button_pane.add(subset);
        button_pane.add(back);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(button_pane, BorderLayout.SOUTH);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Tasks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = this;
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TaskWindow taskThread = new TaskWindow();
                taskThread.createAndShowGUI();
            }
        });
    }
}
