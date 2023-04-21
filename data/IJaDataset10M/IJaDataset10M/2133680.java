package threadrace;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ThreadRacePanel extends JPanel {

    public ThreadRacePanel(Model m) {
        myModel_ = m;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(myModel_.getFinishLinePos() + SPACE_RIGHT_OF_FINISH_LINE, Model.NUM_CARS * Model.VERTICAL_DISTANCE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < Model.NUM_CARS; i++) {
            DrawCar(myModel_.getCar(i), g);
        }
        int finishLinePos = myModel_.getFinishLinePos();
        g.setColor(new Color(0, 0, 0));
        g.drawLine(finishLinePos, 0, finishLinePos, Model.VERTICAL_DISTANCE * Model.NUM_CARS);
        if (myModel_.isRaceFinished()) {
            java.util.List<Model.Car> cars = Arrays.asList(myModel_.getCars());
            Collections.sort(cars);
            for (int i = 0; i < Model.NUM_CARS; i++) {
                g.drawString((i + 1) + "ter Platz", myModel_.getFinishLinePos() + OFFSET_RESULT_TEXT, Model.VERTICAL_DISTANCE * myModel_.getCar(i).getNumber() + Model.VERTICAL_DISTANCE / 2);
            }
        }
    }

    public void DrawCar(Model.Car c, Graphics g) {
        g.setColor(c.GetColor());
        int xp = c.GetX();
        int yp = c.GetY() + 10;
        int xs[] = { xp, xp + 5, xp + 10, xp + 25, xp + 30, xp + 37, xp + 37, xp, xp };
        int ys[] = { yp, yp, yp - 5, yp - 5, yp, yp, yp + 7, yp + 7, yp };
        g.fillPolygon(xs, ys, 8);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(xp + 12, yp - 3, 10, 3);
        g.setColor(new Color(0, 0, 0));
        g.fillOval(xp + 6, yp + 2, 7, 7);
        g.fillOval(xp + 23, yp + 2, 7, 7);
    }

    public Model myModel_;

    static int SPACE_RIGHT_OF_FINISH_LINE = 100;

    private static int OFFSET_RESULT_TEXT = 45;

    private static final long serialVersionUID = 1L;
}
