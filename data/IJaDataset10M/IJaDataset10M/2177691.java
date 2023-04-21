package org.gunncs.ui;

import java.awt.Graphics;
import javax.swing.JComponent;
import org.gunncs.RobotModel;

/**
 *
 * @author anand
 */
public class DashBoardComponent extends JComponent {

    RobotModel robot;

    public DashBoardComponent() {
        robot = new RobotModel();
    }

    public DashBoardComponent(RobotModel r) {
        robot = r;
    }

    public void setRobotModel(RobotModel r) {
        robot = r;
    }

    public RobotModel getRobotModel() {
        return robot;
    }

    public void paintComponent(Graphics g) {
        robot.draw(g);
    }
}
