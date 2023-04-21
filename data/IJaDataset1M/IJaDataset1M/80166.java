package game.swing.app;

import game.swing.rushhour.area.AreasWindow;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Rules how to play dmLite Rush Hour.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-03-21
 */
public class Rules extends JDialog implements ActionListener {

    private JTextArea textArea = new JTextArea();

    private JButton button = new JButton("OK");

    private JPanel northPanel = new JPanel();

    private JPanel southPanel = new JPanel();

    public Rules(AreasWindow aParent) {
        super(aParent);
        try {
            init();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        pack();
    }

    private void init() throws Exception {
        this.setTitle("Modelibra Rush Hour Rules");
        this.setResizable(false);
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(northPanel, "North");
        cp.add(southPanel, "South");
        textArea.setEditable(false);
        textArea.append(this.prepareText());
        northPanel.add(textArea);
        southPanel.setLayout(new FlowLayout());
        southPanel.add(button);
        button.addActionListener(this);
    }

    private String prepareText() {
        String t = new String();
        t = t + "Modelibra Rush Hour is based on Rush Hour by ThinkFun.             \n";
        t = t + "You can visit ThinkFun at http://www.puzzles.com .                 \n";
        t = t + "                                                                   \n";
        t = t + "The objective:  To exit the Red Car in the third row to the right. \n";
        t = t + "                                                                   \n";
        t = t + "To play:  Select a car by clicking on it.                          \n";
        t = t + "The selected car gets black spots.                                 \n";
        t = t + "Then, click in front of or behind the selected car                 \n";
        t = t + "to move the car up or down, left or right,                         \n";
        t = t + "until the path is cleared to exit the Red Car.                     \n";
        return t;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            this.dispose();
        }
    }
}
