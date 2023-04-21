package edu.uwa.aidan.robot.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import org.neuroph.core.NeuralNetwork;
import edu.uwa.aidan.robot.world.AgentWorld;
import edu.uwa.aidan.robot.world.Robot;

/**
 * A simple <code>JPanel</code> which will render the <code>AgentWorld</code>, filling
 * all of the available space, whilst preserving the aspect-ratio.
 * 
 * @author Aidan Morgan
 */
public class WorldPanel extends JPanel {

    private AgentWorld world;

    public WorldPanel(AgentWorld world, NeuralNetwork network) {
        this.world = world;
        Robot r = new Robot(network);
        world.setRobot(r);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        world.paint(g2d, getPixelRatio());
    }

    /**
	 * Determine how many pixels should be used for rendering a unit of the world-coordinate.
	 * @return
	 */
    private double getPixelRatio() {
        double widthRatio = (getWidth()) / world.getWidth();
        double heightRatio = (getHeight()) / world.getHeight();
        return Math.min(widthRatio, heightRatio);
    }
}
