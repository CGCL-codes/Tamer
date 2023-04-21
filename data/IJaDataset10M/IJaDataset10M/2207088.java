package javadevices;

import java.awt.*;
import devices.*;

class MotorImp extends Canvas implements Motor, Runnable {

    final int up = 1;

    final int down = -1;

    final int idle = 0;

    protected final int width = 2;

    protected final int height = 45;

    protected final int posx;

    protected final int id;

    protected final int nFloors;

    protected final int decelerationSteps;

    protected final int sensorPos[][];

    protected final int timeStep = 30;

    protected LiftSensorObserver observer;

    protected Thread thread = new Thread(this);

    protected int posy;

    protected int destination = 9;

    protected int floor = 0;

    protected int direction = idle;

    public MotorImp(int i, int x, int nf, int s, int pos[]) {
        id = i;
        nFloors = nf;
        sensorPos = new int[3][nFloors];
        decelerationSteps = s;
        for (int j = 0; j < nf; j++) sensorPos[0][j] = pos[j] - s;
        for (int j = 0; j < nf; j++) sensorPos[1][j] = pos[j];
        for (int j = 0; j < nf; j++) sensorPos[2][j] = pos[j] + s;
        setLocation(x, 0);
        setSize(width, pos[0] + height);
        setBackground(Color.white);
        posy = pos[0];
        posx = x;
    }

    public synchronized void setObserver(LiftSensorObserver o) {
        if (observer == null) {
            observer = o;
            thread.start();
        }
    }

    public void setDestination(int floor) {
        destination = floor;
    }

    public synchronized void raise() {
        if (direction == idle) {
            direction = up;
            notify();
        }
    }

    public synchronized void lower() {
        if (direction == idle) {
            direction = down;
            notify();
        }
    }

    public void run() {
        int time = timeStep * timeStep * (decelerationSteps + 1);
        while (true) {
            observer.update(floor);
            synchronized (this) {
                direction = idle;
                while (direction == idle) try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            floor += direction;
            observer.update(floor);
            boolean arriving = false;
            for (int i = 1; i < decelerationSteps + 1; i++) {
                try {
                    thread.sleep((long) Math.sqrt(time / i));
                } catch (InterruptedException e) {
                }
                posy -= direction;
                repaint();
            }
            do {
                try {
                    thread.sleep(timeStep);
                } catch (InterruptedException e) {
                }
                posy -= direction;
                repaint();
                if (approach(floor)) if (floor == destination) arriving = true; else {
                    floor += direction;
                    observer.update(floor);
                }
            } while (!arriving);
            for (int i = decelerationSteps; i > 0; i--) {
                try {
                    thread.sleep((long) Math.sqrt(time / i));
                } catch (InterruptedException e) {
                }
                posy -= direction;
                repaint();
            }
        }
    }

    protected boolean approach(int floor) {
        return posy * direction <= sensorPos[1 + direction][floor] * direction;
    }

    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, posy, width, height);
    }
}
