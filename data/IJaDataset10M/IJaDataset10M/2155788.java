package units;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import timer.Timer;
import player.DirPad;
import player.DirPad.Direction;
import sushiwar.Constants;
import sushiwar.Screen;
import timer.TimerListener;

public abstract class Agent implements KeyListener, TimerListener, Constants {

    protected double x = 0;

    protected double y = 0;

    protected int width = 1;

    protected int height = 1;

    protected double radius = 1;

    protected int ux = 0;

    protected int uy = 0;

    protected Rectangle box = null;

    protected Rectangle collisionBox = null;

    protected Point collisionBoxPosition = null;

    protected boolean respondControl = false;

    protected DirPad controlPad = null;

    protected Timer moveTimer = null;

    protected Screen screen = null;

    protected static ArrayList<Agent> instances = new ArrayList<Agent>();

    public Agent(double x, double y, int width, int height, Screen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = width / 2;
        this.collisionBox = new Rectangle((int) x, (int) y, width, height);
        this.collisionBoxPosition = new Point(0, 0);
        this.box = new Rectangle((int) x, (int) y, width, height);
        this.screen = screen;
        this.controlPad = new DirPad();
        this.moveTimer = new Timer(this, MOVE_TIMER_PERIOD);
        instances.add(this);
    }

    public Agent(double x, double y, int width, int height, Screen screen, boolean respondControl) {
        this(x, y, width, height, screen);
        this.respondControl = respondControl;
    }

    /**
	 * Desloca o objeto. Não permite que saia da tela.
	 * @param dx Deslocamento em x.
	 * @param dy Deslocamento em y.
	 * @return Um valor inteiro. O primeiro bit será 1 se o movimento em x for
	 * feito totalmente, 0 caso contrário. Idem para o segundo bit para a dire-
	 * ção y.
	 */
    public int move(double dx, double dy) {
        double newX = x + dx;
        double newY = y + dy;
        int result = 0;
        setPosition(newX, newY);
        result = screen.isBoxInScreen(box);
        return result;
    }

    /**
	 * Define a posição do objeto.
	 * @param x Posição x.
	 * @param y Posição y.
	 * @return Verdadeiro se a nova posição estiver na tela.
	 */
    public boolean setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.updateBox();
        return true;
    }

    public void setPositionX(double newX) {
        this.x = newX;
        this.updateBox();
    }

    public void setPositionY(double newY) {
        this.y = newY;
        this.updateBox();
    }

    public void setCollisionBox(int x, int y, int width, int height) {
        collisionBox.setSize(width, height);
        collisionBoxPosition.setLocation(x, y);
        collisionBox.setLocation(box.x + collisionBoxPosition.x, box.y + collisionBoxPosition.y);
    }

    public void setCollisionBoxCenter(int width, int height) {
        setCollisionBox((this.width - width) / 2, (this.height - height) / 2, width, height);
    }

    protected void updateBox() {
        this.box.setLocation((int) x - width / 2, (int) y - height / 2);
        this.updateCollisionBox();
    }

    protected void updateCollisionBox() {
        collisionBox.setLocation(box.x + collisionBoxPosition.x, box.y + collisionBoxPosition.y);
    }

    public void toggle(boolean yes) {
        respondControl = yes;
    }

    public void startTimer() {
        moveTimer.start();
    }

    /**
	 * (Chamado pelo timer)
	 * Move o agente caso responda a controles e tenha
	 * alguma tecla direcional pressionada.
	 */
    @Override
    public int update() {
        if (ux != 0 || uy != 0) this.move(ux, uy);
        return 0;
    }

    public double getPositionX() {
        return x;
    }

    public double getPositionY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRadius() {
        return radius;
    }

    public Rectangle getBox() {
        return (Rectangle) box.clone();
    }

    public Rectangle getCollisionBox() {
        return (Rectangle) collisionBox.clone();
    }

    public boolean isMoving() {
        return ux != 0;
    }

    public boolean isControllable() {
        return respondControl;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (respondControl) {
            Direction pressed = DirPad.KeyEvent2Direction(e);
            if (!controlPad.isDirectionPressed(pressed)) keyPressedOnce(e);
            controlPad.setDirection(pressed, true);
        }
    }

    public void keyPressedOnce(KeyEvent e) {
        Direction pressed = DirPad.KeyEvent2Direction(e);
        ux += DirPad.Direction2X(pressed);
        uy += DirPad.Direction2Y(pressed);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (respondControl) {
            Direction pressed = DirPad.KeyEvent2Direction(e);
            if (pressed == null || controlPad.isDirectionPressed(pressed)) keyReleasedOnce(e);
            controlPad.setDirection(pressed, false);
        }
    }

    public void keyReleasedOnce(KeyEvent e) {
        Direction pressed = DirPad.KeyEvent2Direction(e);
        ux -= DirPad.Direction2X(pressed);
        uy -= DirPad.Direction2Y(pressed);
    }
}
