package praktikumid.k11.p23b;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

    private Graph g;

    public Frame() {
        setLocation(100, 100);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel toolbar = new JPanel();
        toolbar.setBackground(Color.BLACK);
        add(toolbar, BorderLayout.EAST);
        GraphPanel gp = new GraphPanel(this);
        add(gp, BorderLayout.CENTER);
        Graph g = new Graph();
        g.add(new Point(0, 34, gp));
        g.add(new Point(20, 4, gp));
        g.add(new Point(40, 64, gp));
        g.add(new Point(70, 74, gp));
        g.add(new Point(100, 22, gp));
        this.g = g;
    }

    public Graph getGraph() {
        return g;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setVisible(true);
    }
}
