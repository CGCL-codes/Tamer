package core;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import math.CircleFunction;
import math.ConstantFunction;
import math.OneMinusOneFunction;
import math.Point2D;
import particle.Particle2D;
import particle.ParticleFactory;

@SuppressWarnings("serial")
public class Nbody extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private static final int WIDTH = 600;

    private static final int HEIGHT = 600;

    private static final boolean TRACE_PATH = false;

    private static final boolean DELTA_TIME_FIXED = true;

    private static final double DELTA_TIME_AMOUNT = 1E-11;

    ArrayList<Particle2D> particles = new ArrayList<Particle2D>();

    private Image dbImage;

    private Graphics dbg;

    private double estimatedTime = 0;

    private Particle2D CentreParticle;

    private Point2D zoom = new Point2D(4, 4);

    public void init() {
        CentreParticle = new Particle2D(new Point2D((WIDTH * zoom.getX()) / 2, (HEIGHT * zoom.getY()) / 2), 1E35);
        particles.add(CentreParticle);
        ParticleFactory pf = new ParticleFactory();
        pf.setPositionFunction(new CircleFunction(new Point2D(WIDTH * zoom.getX() / 2, HEIGHT * zoom.getY() / 2), new ConstantFunction(WIDTH / 10)));
        pf.setChargeFunction(new Multiply(new OneMinusOneFunction(), pf.getChargeFunction()));
        addParticles(pf.createParticles(100));
        pf.setPositionFunction(new CircleFunction(new Point2D(WIDTH * zoom.getX() / 2, HEIGHT * zoom.getY() / 2), new ConstantFunction(WIDTH / 12)));
        addParticles(pf.createParticles(100));
        pf.setPositionFunction(new CircleFunction(new Point2D(WIDTH * zoom.getX() / 2, HEIGHT * zoom.getY() / 2), new ConstantFunction(WIDTH / 14)));
        addParticles(pf.createParticles(100));
    }

    /**
	 * Add particles to the list
	 * @param particleArray
	 */
    private void addParticles(Particle2D[] particleArray) {
        for (Particle2D p : particleArray) {
            particles.add(p);
        }
    }

    public void start() {
        this.setSize(WIDTH, HEIGHT);
        Thread th = new Thread(this);
        th.start();
    }

    public void stop() {
    }

    public void destroy() {
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while (true) {
            long startTime = System.nanoTime();
            repaint();
            calculateParticles();
            if (DELTA_TIME_FIXED) moveParticles(DELTA_TIME_AMOUNT); else moveParticles(estimatedTime / 1E9);
            try {
                Thread.sleep(0);
            } catch (InterruptedException ex) {
            }
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            estimatedTime = System.nanoTime() - startTime;
        }
    }

    private void calculateParticles() {
        final double G = 6.67300E-11;
        final double Ke = 8.987551787E9;
        for (Particle2D p : particles) {
            Point2D a = new Point2D();
            for (Particle2D other : particles) {
                if (other != p) {
                    Point2D unitTowards = p.getPosition().unitTowards(other.getPosition());
                    double rsqrd = Math.pow(p.getPosition().distanceTo(other.getPosition()), 2);
                    double accel = G * other.getMass() / rsqrd;
                    a = a.add(unitTowards.scale(accel));
                    double accel2 = -Ke * other.getCharge() / (rsqrd * p.getMass());
                    a = a.add(unitTowards.scale(accel2));
                }
            }
            p.setAcceleration(a);
        }
    }

    private void moveParticles(double t) {
        for (Particle2D p : particles) {
            p.move(t);
        }
    }

    /** Update - Method, implements double buffering */
    public void update(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
            dbg.setColor(getBackground());
            dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
        }
        if (!TRACE_PATH) {
            dbg.setColor(getBackground());
            dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
        }
        dbg.setColor(getForeground());
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    /** Draw the scene onto a Graphics g */
    public void paint(Graphics g) {
        g.setColor(Color.red);
        for (Particle2D p : particles) {
            int radius = 1;
            double x = p.getPosition().getX() / zoom.getX(), y = p.getPosition().getY() / zoom.getY();
            g.fillOval((int) x - radius, (int) y - radius, 2 * radius, 2 * radius);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }
}
