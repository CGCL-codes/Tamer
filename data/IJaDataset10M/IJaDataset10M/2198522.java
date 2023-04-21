package uk.ac.imperial.ma.metric.applets.newSecantExplorationApplet;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import uk.ac.imperial.ma.metric.explorations.calculus.differentiation.NewSecantExploration;

/**
 * @author dan
 *
 */
public class NewSecantExplorationApplet extends JApplet implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1442093597107861581L;

    private JButton jbutton;

    public void init() {
        jbutton = new JButton("");
        jbutton.addActionListener(this);
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(jbutton);
    }

    public void start() {
    }

    public void stop() {
    }

    public void destroy() {
    }

    public void actionPerformed(ActionEvent e) {
        JFrame jframe = new JFrame("Secant Exploration");
        NewSecantExploration exploration = new NewSecantExploration();
        jframe.getContentPane().setLayout(new GridLayout(1, 1));
        jframe.getContentPane().add(exploration);
        jframe.setVisible(false);
        jframe.setSize(800, 600);
        jframe.setVisible(true);
        exploration.init();
    }
}
