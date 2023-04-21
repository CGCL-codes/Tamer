package tron.core;

import java.awt.Color;
import javax.swing.JOptionPane;
import tron.control.Controller;
import tron.properties.TronProperties;

/**
 *
 * @author User
 */
public class Player {

    private static int idCounter = 1;

    private int id;

    private int x;

    private int y;

    private int direction;

    private Color color;

    private boolean firstTurn;

    private boolean lost;

    private int place;

    private Controller controller;

    public Player(int x, int y, Color color, int direction, Controller controller) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.controller = controller;
        this.direction = direction;
        firstTurn = true;
        lost = false;
        id = idCounter++;
    }

    public String getName() {
        return TronProperties.getSingleton().getProperty("p" + id + ".control");
    }

    public boolean computeMove() {
        long startTime, endTime;
        int stepTime = Integer.parseInt(TronProperties.getSingleton().getProperty("step.time"));
        int newDir = 0;
        try {
            startTime = System.nanoTime();
            newDir = controller.newDirection(id);
            endTime = System.nanoTime() - startTime;
            endTime /= 1000000;
            if (endTime > stepTime) {
                System.out.println(getName() + " took " + endTime + "ms exceeding maximum step time of " + stepTime + "ms.");
                JOptionPane.showMessageDialog(null, getName() + " took " + endTime + "ms exceeding maximum step time of " + stepTime + "ms.");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, getName() + " threw " + e);
            return false;
        }
        setDirection(newDir);
        return true;
    }

    public void step() {
        switch(direction) {
            case Game.NORTH:
                y++;
                break;
            case Game.SOUTH:
                y--;
                break;
            case Game.EAST:
                x++;
                break;
            case Game.WEST:
                x--;
                break;
        }
    }

    public void gameOver(int place) {
        lost = true;
        this.place = place;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        if (this.direction % 2 != direction % 2 || firstTurn) {
            firstTurn = false;
            this.direction = direction;
        }
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasLost() {
        return lost;
    }

    public int getPosition() {
        if (hasLost()) {
            return place;
        }
        return 0;
    }
}
