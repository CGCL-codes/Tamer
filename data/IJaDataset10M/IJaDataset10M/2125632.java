package supernaturalgame;

import java.awt.Graphics2D;
import br.com.ngame.core.GameLevel;
import br.com.ngame.device.InputDevice;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CarLevel extends GameLevel {

    static final int TRUCK_MODE_HIT = 1;

    static final int TRUCK_MODE_CHASE = 0;

    static final int TRUCK_MODE_SLIDE = 2;

    BufferedImage car;

    BufferedImage carFront;

    BufferedImage truck;

    BufferedImage steer;

    BufferedImage shooter;

    int truckX, truckY;

    int truckX_k, truckY_k;

    int truck_mode;

    float truck_s;

    int carX, carY;

    int carX_k, carY_k;

    int crossX, crossY;

    float shotAlpha = 0;

    int steerDir = 0;

    float steerRad = 0;

    Target[] targets;

    Random rnd;

    public void load() throws RuntimeException {
        try {
            getGame().getInputDevice().mapButton(KeyEvent.VK_W, InputDevice.ACTION1);
            getGame().getInputDevice().mapButton(KeyEvent.VK_S, InputDevice.ACTION2);
            getGame().getInputDevice().mapButton(KeyEvent.VK_A, InputDevice.ACTION3);
            getGame().getInputDevice().mapButton(KeyEvent.VK_D, InputDevice.ACTION4);
            getGame().getInputDevice().mapButton(KeyEvent.VK_SPACE, InputDevice.ACTION5);
            getGame().getImageManager().loadImage("img/car.gif");
            getGame().getImageManager().loadImage("img/carFront.gif");
            getGame().getImageManager().loadImage("img/truck.gif");
            getGame().getImageManager().loadImage("img/steeringwheel.gif");
            getGame().getImageManager().loadImage("img/shooter.gif");
            car = getGame().getImageManager().getImage("img/car.gif");
            carFront = getGame().getImageManager().getImage("img/carFront.gif");
            truck = getGame().getImageManager().getImage("img/truck.gif");
            steer = getGame().getImageManager().getImage("img/steeringwheel.gif");
            shooter = getGame().getImageManager().getImage("img/shooter.gif");
            carY = -20;
            rnd = new Random();
            truckX = -300;
            truckY = 110;
            truck_s = 1f;
            truck_mode = TRUCK_MODE_CHASE;
            crossX = 320;
            crossY = 200;
            targets = new Target[6];
            targets[0] = new Target(truckX - 50, truckY - 15);
            targets[1] = new Target(truckX + 50, truckY - 15);
            targets[2] = new Target(truckX - 88, truckY - 15);
            targets[3] = new Target(truckX + 88, truckY - 15);
            targets[4] = new Target(truckX - 113, truckY - 15);
            targets[5] = new Target(truckX + 113, truckY - 15);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unload() {
    }

    public void updateLogic(int currentFrame) {
        if (getGame().getInputDevice().isPressed(InputDevice.EXIT)) {
            getGame().Terminate();
        }
        if (getGame().getInputDevice().isPressed(InputDevice.ACTION1)) {
            crossY -= 3;
        }
        if (getGame().getInputDevice().isPressed(InputDevice.ACTION2)) {
            crossY += 3;
        }
        if (getGame().getInputDevice().isPressed(InputDevice.ACTION3)) {
            crossX -= 3;
        }
        if (getGame().getInputDevice().isPressed(InputDevice.ACTION4)) {
            crossX += 3;
        }
        if (getGame().getInputDevice().isPressed(InputDevice.ACTION5)) {
            shotAlpha = 1f;
            Ellipse2D.Float target = new Ellipse2D.Float();
            for (int i = 0; i < targets.length; i++) {
                target.setFrame(targets[i].x, targets[i].y, 20, 20);
                if (target.contains(crossX, crossY)) {
                    targets[i].dead = true;
                }
            }
        }
        if (shotAlpha > 0) {
            shotAlpha -= 0.2f;
        }
        steerDir = getGame().getInputDevice().getXAxisOffset();
        if (steerDir > 0) {
            if (steerRad < 0.5f) {
                steerRad += 0.1f;
            }
        } else if (steerDir < 0) {
            if (steerRad > -0.5f) {
                steerRad -= 0.1f;
            }
        } else {
            if (steerRad > 0) {
                steerRad -= 0.1f;
            }
            if (steerRad < 0) {
                steerRad += 0.1f;
            }
        }
        if (carY < -15) {
            carY_k = 1 + rnd.nextInt(2);
            carY = -15;
        } else if (carY > 1) {
            carY_k = -1 - rnd.nextInt(2);
            carY = 1;
        }
        carY += carY_k;
        carX += carX_k;
        if (carX > 20) {
            carX = 20;
        } else if (carX < -20) {
            carX = -20;
        }
        if (truck_mode == TRUCK_MODE_CHASE) {
            if (truck_s > 1f) {
                truck_s -= 0.1f;
                carY_k = 10 - rnd.nextInt(30);
                carX_k = 10 - rnd.nextInt(30);
            } else {
                carX = 0;
                carX_k = 0;
                truck_s = 1f;
                truckX_k = 0;
                if (truckX < 150) {
                    truck_mode = TRUCK_MODE_SLIDE;
                } else if (truckX < 315) {
                    truckX_k = 5;
                } else if (truckX > 490) {
                    truck_mode = TRUCK_MODE_SLIDE;
                } else if (truckX > 325) {
                    truckX_k = -5;
                } else {
                    truck_mode = TRUCK_MODE_HIT;
                }
            }
            truckX += truckX_k - steerDir * 10;
        } else if (truck_mode == TRUCK_MODE_SLIDE) {
            if (truckX < 300) {
                truckX_k = 20;
            } else if (truckX > 340) {
                truckX_k = -20;
            } else {
                truck_mode = TRUCK_MODE_CHASE;
            }
            truckX += truckX_k - steerDir * 10;
        } else if (truck_mode == TRUCK_MODE_HIT) {
            if (truck_s < 2.0f) {
                truck_s /= 0.9f;
            } else {
                truck_mode = TRUCK_MODE_CHASE;
                truckX_k = 15;
                if (rnd.nextInt(10) > 5) {
                    truckX_k *= -1;
                }
            }
        }
        targets[0].setPosition(truckX - 50 - 10, truckY - 25);
        targets[1].setPosition(truckX + 50 - 10, truckY - 25);
        targets[2].setPosition(truckX - 88 - 10, truckY - 25);
        targets[3].setPosition(truckX + 88 - 10, truckY - 25);
        targets[4].setPosition(truckX - 113 - 10, truckY - 25);
        targets[5].setPosition(truckX + 113 - 10, truckY - 25);
    }

    public void render(Graphics2D screenBuffer) {
        screenBuffer.setColor(Color.black);
        screenBuffer.fillRect(0, 0, 639, 399);
        screenBuffer.setColor(Color.DARK_GRAY);
        screenBuffer.drawLine(0, 170, 640, 170);
        int tx = truckX - Math.round(((float) truck.getWidth() * truck_s) / 2f);
        int ty = truckY - Math.round(((float) truck.getHeight() * truck_s) / 2f);
        AffineTransform xf = AffineTransform.getScaleInstance(truck_s, truck_s);
        xf.translate(tx / truck_s, ty / truck_s);
        screenBuffer.drawImage(truck, xf, null);
        tx = truckX;
        ty = truckY;
        int dx1 = Math.round(50f * truck_s);
        int dx2 = Math.round(88f * truck_s);
        int dx3 = Math.round(113f * truck_s);
        screenBuffer.setColor(Color.white);
        screenBuffer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
        for (int ii = 30; ii <= 450; ii += 50) {
            int i = Math.round(ii * truck_s);
            if (!targets[0].dead) {
                screenBuffer.fillOval(tx - dx1 - i / 2, ty - 15 - i / 2, i, i);
            }
            if (!targets[1].dead) {
                screenBuffer.fillOval(tx + dx1 - i / 2, ty - 15 - i / 2, i, i);
            }
            if (!targets[2].dead) {
                screenBuffer.fillOval(tx - dx2 - i / 2, ty - 15 - i / 2, i, i);
            }
            if (!targets[3].dead) {
                screenBuffer.fillOval(tx + dx2 - i / 2, ty - 15 - i / 2, i, i);
            }
            if (!targets[4].dead) {
                screenBuffer.fillOval(tx - dx3 - i / 2, ty - 15 - i / 2, i, i);
            }
            if (!targets[5].dead) {
                screenBuffer.fillOval(tx + dx3 - i / 2, ty - 15 - i / 2, i, i);
            }
        }
        if (truck_s == 1f) {
            screenBuffer.setColor(Color.blue);
            for (int i = 0; i < targets.length; i++) {
                if (!targets[i].dead) {
                    screenBuffer.fillOval(targets[i].x, targets[i].y, 20, 20);
                }
            }
        }
        screenBuffer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
        screenBuffer.drawImage(car, carX, carY, null);
        xf = AffineTransform.getRotateInstance(steerRad, carX + 420 + 120, carY + 220 + 135);
        xf.translate(carX + 420, carY + 220);
        screenBuffer.drawImage(steer, xf, null);
        screenBuffer.drawImage(shooter, carX + 50, carY + 100, null);
        screenBuffer.drawImage(carFront, carX, carY + 270, null);
        screenBuffer.setColor(Color.darkGray);
        screenBuffer.fillRect(0, 400, 640, 480);
        screenBuffer.drawLine(crossX - 20, crossY, crossX - 5, crossY);
        screenBuffer.drawLine(crossX + 5, crossY, crossX + 20, crossY);
        screenBuffer.drawLine(crossX, crossY - 20, crossX, crossY - 5);
        screenBuffer.drawLine(crossX, crossY + 5, crossX, crossY + 20);
        if (shotAlpha > 0) {
            screenBuffer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shotAlpha));
            screenBuffer.setColor(Color.red);
            screenBuffer.drawLine(carX + 230, carY + 190, crossX, crossY);
        }
    }
}

class Target {

    int x;

    int y;

    boolean dead = false;

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
