package org.sf.cocosc.turtlegraphics;

import java.awt.Graphics;
import javax.swing.JComponent;

public class TurtleCanvas extends JComponent {

    private static final long serialVersionUID = 1L;

    private TurtleConfiguration configuration;

    private IStepSource stepSource;

    public TurtleCanvas(TurtleConfiguration configuration, IStepSource stepSource) {
        super();
        this.configuration = configuration;
        this.stepSource = stepSource;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Turtle turtle = new Turtle(stepSource.getSteps(), configuration);
        turtle.paint(this, g);
    }

    public void setConfiguration(TurtleConfiguration configuration) {
        this.configuration = configuration;
    }
}
