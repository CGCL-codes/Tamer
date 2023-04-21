package jake2.client;

import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CircleListener implements ActionListener {

    Robot robot;

    public CircleListener(Robot robot) {
        this.robot = robot;
    }

    public void actionPerformed(ActionEvent evt) {
        int originx = (int) GhostMouse.size.getWidth() / 2;
        int originy = (int) GhostMouse.size.getHeight() / 2;
        double pi = 3.1457;
        for (double theta = 0; theta < 4 * pi; theta = theta + 0.1) {
            double radius = theta * 20;
            double x = Math.cos(theta) * radius + originx;
            double y = Math.sin(theta) * radius + originy;
            robot.mouseMove((int) x, (int) y);
            try {
                Thread.sleep(25);
            } catch (Exception ex) {
            }
        }
    }
}
