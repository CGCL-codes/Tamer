package ai;

import comm.Messenger;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;
import world.Base;
import world.Flag;
import world.Friend;
import world.Obstacle;
import world.Point;
import world.World;
import world.WorldConstant;

/**
 *
 * @author dheath
 */
public class Reactive implements Intelligence {

    private int botId;

    private World world;

    private Messenger msgr;

    private long lastTime;

    private long lastPlot;

    private boolean guard;

    public Reactive(int botId) {
        this.botId = botId;
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader stdin = new BufferedReader(isr);
            System.out.println("Will this tank guard?");
            String ans = stdin.readLine();
            if (ans.equalsIgnoreCase("y")) {
                this.guard = true;
            } else {
                this.guard = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWorld(World world) {
        this.world = world;
        lastTime = 0;
    }

    public void setMessenger(Messenger msgr) {
        this.msgr = msgr;
    }

    public void NextMove() {
        Date now = new Date();
        if (now.getTime() - lastTime > 100) {
            double worldsize = 0;
            Friend me = world.getFriends().get(botId);
            worldsize = Double.parseDouble(world.getConstants().get("worldsize"));
            Date now2 = new Date();
            if (now2.getTime() - lastPlot > 10000) {
                try {
                    FileWriter testFile = new FileWriter(now.getTime() + "_gnu.txt");
                    PrintWriter out = new PrintWriter(testFile);
                    out.println("set xrange [-" + (worldsize / 2) + ": " + (worldsize / 2) + "]");
                    out.println("set yrange [-" + (worldsize / 2) + ": " + (worldsize / 2) + "]");
                    out.println("unset key");
                    out.println("set size square");
                    out.println();
                    out.println("unset arrow");
                    for (Obstacle ob : world.getObstacles()) {
                        out.println("set arrow from " + ob.getBLPoint().getX() + ", " + ob.getBLPoint().getY() + " to " + ob.getBRPoint().getX() + ", " + ob.getBRPoint().getY() + " nohead lt 3");
                        out.println("set arrow from " + ob.getBRPoint().getX() + ", " + ob.getBRPoint().getY() + " to " + ob.getTRPoint().getX() + ", " + ob.getTRPoint().getY() + " nohead lt 3");
                        out.println("set arrow from " + ob.getTRPoint().getX() + ", " + ob.getTRPoint().getY() + " to " + ob.getTLPoint().getX() + ", " + ob.getTLPoint().getY() + " nohead lt 3");
                        out.println("set arrow from " + ob.getTLPoint().getX() + ", " + ob.getTLPoint().getY() + " to " + ob.getBLPoint().getX() + ", " + ob.getBLPoint().getY() + " nohead lt 3");
                    }
                    for (Base ob : world.getBases()) {
                        out.println("set arrow from " + ob.getBLPoint().getX() + ", " + ob.getBLPoint().getY() + " to " + ob.getBRPoint().getX() + ", " + ob.getBRPoint().getY() + " nohead lt 3");
                        out.println("set arrow from " + ob.getBRPoint().getX() + ", " + ob.getBRPoint().getY() + " to " + ob.getTRPoint().getX() + ", " + ob.getTRPoint().getY() + " nohead lt 3");
                        out.println("set arrow from " + ob.getTRPoint().getX() + ", " + ob.getTRPoint().getY() + " to " + ob.getTLPoint().getX() + ", " + ob.getTLPoint().getY() + " nohead lt 3");
                        out.println("set arrow from " + ob.getTLPoint().getX() + ", " + ob.getTLPoint().getY() + " to " + ob.getBLPoint().getX() + ", " + ob.getBLPoint().getY() + " nohead lt 3");
                    }
                    for (Flag f : world.getFlags()) {
                        out.println("set arrow from " + f.getLocation().getX() + ", " + f.getLocation().getY() + " to " + f.getLocation().getX() + 4 + ", " + f.getLocation().getY() + 4 + " nohead lt 3");
                    }
                    out.println();
                    out.println("plot '-' with vectors head");
                    for (double x = -1.0 * worldsize / 2.0 + 10.0; x < worldsize / 2.0; x += 10.0) {
                        for (double y = -1.0 * worldsize / 2.0 + 10.0; y < worldsize / 2.0; y += 10.0) {
                            Point here = new Point(x, y);
                            Point deltaXY = new Point(0, 0);
                            int count = 0;
                            Point r = calculateRandomField();
                            deltaXY.setX(r.getX() + deltaXY.getX());
                            deltaXY.setY(r.getY() + deltaXY.getY());
                            count++;
                            deltaXY.setX(deltaXY.getX() / ((double) count));
                            deltaXY.setY(deltaXY.getY() / ((double) count));
                            out.println(here.getX() + " " + here.getY() + " " + (deltaXY.getX()) + " " + (deltaXY.getY()));
                        }
                    }
                    out.println("e");
                    out.close();
                    lastPlot = now2.getTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Point deltaXY = new Point(0, 0);
            int count = 0;
            for (Obstacle ob : world.getObstacles()) {
                double radius = Math.abs(ob.getLocation().getX() - ob.getTLPoint().getX() + 2);
                Point p = calulateRepulsiveField(me.getLocation(), ob.getLocation(), radius, 10);
                deltaXY.setX(p.getX() + deltaXY.getX());
                deltaXY.setY(p.getY() + deltaXY.getY());
                count++;
            }
            double stop = 1.0;
            if (!guard) {
                if (me.getFlag().equalsIgnoreCase("none")) {
                    for (Flag flag : world.getFlags()) {
                        if (!flag.getOwnerColor().equalsIgnoreCase(me.getColor())) {
                            double radius = 0;
                            Point p = calulateAttractiveField(me.getLocation(), flag.getLocation(), radius, worldsize * 1.5);
                            deltaXY.setX(p.getX() + deltaXY.getX());
                            deltaXY.setY(p.getY() + deltaXY.getY());
                            count++;
                            break;
                        }
                    }
                } else {
                    for (Base base : world.getBases()) {
                        if (base.getTeamColor().equalsIgnoreCase(me.getColor())) {
                            double radius = 0;
                            Point p = calulateAttractiveField(me.getLocation(), base.getLocation(), radius, worldsize * 1.5);
                            deltaXY.setX(p.getX() + deltaXY.getX());
                            deltaXY.setY(p.getY() + deltaXY.getY());
                            count++;
                        }
                    }
                }
            } else {
                for (Flag flag : world.getFlags()) {
                    if (flag.getOwnerColor().equalsIgnoreCase(me.getColor())) {
                        double radius = 0;
                        double dist = Math.sqrt(Math.pow(flag.getLocation().getX() - me.getLocation().getX(), 2) + Math.pow(flag.getLocation().getY() - me.getLocation().getY(), 2));
                        Point p = null;
                        if (dist > 60) {
                            p = calulateAttractiveField(me.getLocation(), flag.getLocation(), radius, worldsize * 1.5);
                            deltaXY.setX(p.getX() + deltaXY.getX());
                            deltaXY.setY(p.getY() + deltaXY.getY());
                            count++;
                        } else {
                            if (dist < 20) {
                                p = calulateRepulsiveField(me.getLocation(), flag.getLocation(), radius, worldsize * 1.5);
                                deltaXY.setX(p.getX() + deltaXY.getX());
                                deltaXY.setY(p.getY() + deltaXY.getY());
                                count++;
                            } else {
                                p = calulateAttractiveField(me.getLocation(), flag.getLocation(), radius, worldsize * 1.5);
                                deltaXY.setX(p.getX() + deltaXY.getX());
                                deltaXY.setY(p.getY() + deltaXY.getY());
                                count++;
                                stop = 0.0;
                                shoot();
                            }
                        }
                    }
                }
            }
            Point r = calculateRandomField();
            deltaXY.setX(r.getX() + deltaXY.getX());
            deltaXY.setY(r.getY() + deltaXY.getY());
            count++;
            deltaXY.setX(deltaXY.getX() / ((double) count));
            deltaXY.setY(deltaXY.getY() / ((double) count));
            double targetAngle = Math.atan2(deltaXY.getY(), deltaXY.getX());
            double diff = targetAngle - me.getAngle();
            double dist = Math.sqrt(deltaXY.getX() * deltaXY.getX() + deltaXY.getY() * deltaXY.getY());
            if (diff < 0) {
                diff += Math.PI * 2;
            }
            if (Math.abs(diff) <= .1) {
                dist = 10;
            }
            double angvel = 0.0;
            double speed = 0.0;
            if (diff <= Math.PI) {
                angvel = diff / 2;
                speed = (1 - (diff / 2)) * (dist / 10);
            } else {
                angvel = -1 * (((Math.PI * 2) - diff) / 2);
                speed = (1 - (((Math.PI * 2) - diff) / 2)) * (dist / 10);
            }
            if (speed < .1) {
                speed = .1;
            }
            speed(speed * stop);
            if (stop < .5) {
                angvel(angvel / 2);
            } else {
                angvel(angvel);
            }
        }
    }

    public Point calculateRandomField() {
        Random rand = new Random();
        return new Point(rand.nextDouble() * 4.0, rand.nextDouble() * 4.0);
    }

    public Point calulateAttractiveField(Point tank, Point obj, double radius, double s) {
        double alpha = .35;
        double distance = Math.sqrt(Math.pow(tank.getX() - obj.getX(), 2) + Math.pow(tank.getY() - obj.getY(), 2));
        double angle = Math.atan2((obj.getY() - tank.getY()), (obj.getX() - tank.getX()));
        if (distance < radius) {
            return new Point(0, 0);
        } else {
            if (distance >= radius && distance < s + radius) {
                return new Point((alpha * (distance - radius) * Math.cos(angle)), (alpha * (distance - radius) * Math.sin(angle)));
            } else {
                return new Point((alpha * Math.cos(angle)), (alpha * Math.sin(angle)));
            }
        }
    }

    public Point calulateRepulsiveField(Point tank, Point obj, double radius, double s) {
        double beta = 1.75;
        double distance = Math.sqrt(Math.pow(tank.getX() - obj.getX(), 2) + Math.pow(tank.getY() - obj.getY(), 2));
        double angle = Math.atan2((obj.getY() - tank.getY()), (obj.getX() - tank.getX()));
        if (distance < radius) {
            return new Point(-1 * Math.cos(angle) * Double.POSITIVE_INFINITY, -1 * Math.sin(angle) * Double.POSITIVE_INFINITY);
        } else {
            if (distance >= radius && distance < s + radius) {
                return new Point((-1 * beta * (s + radius - distance) * Math.cos(angle)), (-1 * beta * (s + radius - distance) * Math.sin(angle)));
            } else {
                return new Point(0, 0);
            }
        }
    }

    public Point calulateTangentField(Point tank, Point obj, double radius, double s) {
        double beta = .5;
        double distance = Math.sqrt(Math.pow(tank.getX() - obj.getX(), 2) + Math.pow(tank.getY() - obj.getY(), 2));
        double angle = Math.atan2((obj.getY() - tank.getY()), (obj.getX() - tank.getX()));
        angle += Math.PI / 2;
        if (distance < radius) {
            return new Point(-1 * Math.cos(angle) * Double.POSITIVE_INFINITY, -1 * Math.sin(angle) * Double.POSITIVE_INFINITY);
        } else {
            if (distance >= radius && distance < s + radius) {
                return new Point((-1 * beta * (s + radius - distance) * Math.cos(angle)), (-1 * beta * (s + radius - distance) * Math.sin(angle)));
            } else {
                return new Point(0, 0);
            }
        }
    }

    private void shoot() {
        msgr.sendCommand("shoot " + botId);
    }

    private void speed(double v) {
        msgr.sendCommand("speed " + botId + " " + v);
    }

    private void angvel(double v) {
        msgr.sendCommand("angvel " + botId + " " + v);
    }
}
