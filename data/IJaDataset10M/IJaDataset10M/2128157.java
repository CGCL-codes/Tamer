package globalwars;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player {

    private Client client;

    private String name;

    private GameEngine engine;

    private Vector2d location;

    private Vector2d fire_point;

    private Vector2d size;

    private BufferedImage gfx;

    private BufferedImage tower;

    private Planet planet;

    private float pos_angle;

    private int tank = 0;

    private Vector2d lastDrawLocation = null;

    private ScaledImage scaled = null;

    private Vector2d tank_scale = null;

    private Vector2d projectionLocation = null;

    private Vector2d scaledLocation = null;

    private boolean lastFire = false;

    public float angle = 0;

    public int burn_time = 0;

    public boolean local;

    public boolean alive = true;

    private int id = -1;

    private Vector2d towerAnchor = new Vector2d(100, 30);

    private int towerLength;

    public Player(String name, Client c, int tank_gfx) {
        tank = tank_gfx;
        client = c;
        client.setPlayer(this);
        this.name = name;
        local = true;
        initGFX(tank_gfx);
    }

    public Player(String name, int tank_gfx, int id) {
        tank = tank_gfx;
        this.name = name;
        client = null;
        local = false;
        initGFX(tank_gfx);
    }

    public void initGFX(int tank_gfx) {
        if (name.equals("n00b")) {
            tank_gfx = 0;
        } else if (name.equals("n00b2")) {
            tank_gfx = 1;
        }
        gfx = GFXHandler.loadXImage("gfx/tank" + tank_gfx + ".png").getImage();
        towerLength = Math.min(gfx.getWidth() - towerAnchor.intX(), gfx.getHeight() - towerAnchor.intY()) / 2;
    }

    public Planet getPlanet() {
        return planet;
    }

    public int getTank() {
        return tank;
    }

    public int getId() {
        return (client != null) ? client.getId() : id;
    }

    public boolean fireLast() {
        return lastFire;
    }

    public void setLastFire(boolean f) {
        lastFire = f;
    }

    public BufferedImage getImage() {
        return gfx;
    }

    public String getNick() {
        return name;
    }

    public void setNick(String n) {
        name = n;
    }

    public Client getClient() {
        return client;
    }

    public void doGravity() {
        int x = (int) Math.ceil(squaremath.cosq(pos_angle) * (planet.size.x / 2d));
        int y = -1 * (int) Math.ceil(squaremath.sinq(pos_angle) * (planet.size.y / 2d));
        Vector2d pos = new Vector2d(x, y);
        Vector2d step = new Vector2d(-Math.cos(pos_angle), Math.sin(pos_angle));
        System.out.println("Searching for soild ground.... " + Math.toDegrees(pos_angle));
        while (!planet.solidPosition(pos)) {
            System.out.println("Testing location: " + pos);
            pos = pos.add(step);
        }
        Vector2d vectorHeight = new Vector2d(-Math.cos(pos_angle), Math.sin(pos_angle));
        location = pos.subtract(vectorHeight.multiply(size).multiply(0.5f));
        location = planet.location.add(location);
        fire_point = location.subtract(vectorHeight.multiply(size).multiply(0.6f));
    }

    /**
     * Returns the corner that is closes to the planet 
     * if the player is located in the specified angle
     * The corner is specified by the relative coordinates
     * from the location of the player (in the middle of the
     * player)
     * @param angle
     * @return
     */
    private Vector2d cornerFromAngle(float angle) {
        angle = (float) (angle % 2 * Math.PI);
        if (angle >= 0 && angle <= Math.PI / 2f) {
            return size.divide(2f).multiply(new Vector2d(-1, 1));
        } else if (angle > Math.PI / 2f && angle <= Math.PI) {
            return size.divide(2f);
        } else if (angle > Math.PI && angle <= 3f * Math.PI / 2f) {
            return size.divide(2f).multiply(new Vector2d(1, -1));
        } else {
            return size.divide(-2f);
        }
    }

    public Vector2d getSize() {
        return size;
    }

    /**
     * Approxiates a radius by taking (x+y/2)/2
     * @return
     */
    public float getRadius() {
        return (size.x + size.y) / 4f;
    }

    public void destroy() {
        alive = false;
    }

    /**
     * Returns upper left corner of the player, and a scaled image
     * 
     * @param res Resolution (screensize/mapsize)
     * @return Location and image scaled to res
     */
    public ScaledImage getScaled(Vector2d res) {
        if (scaled == null || !scaled.resolution.equals(res)) {
            Vector2d real_size = new Vector2d(gfx);
            scaled = ScaledImage.createDualScaledImage(gfx, location.subtract(size.divide(2)), projectionLocation(), real_size, tank_scale, res);
            lastDrawLocation = location.subtract(size.divide(2));
            scaledLocation = GameFrame.gp.getEngine().getMap().getSize().multiply(res).subtract(scaled.location);
        }
        return scaled;
    }

    public Vector2d[] getScaledTower(Vector2d res) {
        Vector2d towerBase = getTowerBase();
        Vector2d towerTop = getTowerTopLocation(towerBase);
        Vector2d[] pos = new Vector2d[2];
        pos[0] = towerBase.multiply(res);
        pos[1] = towerTop.multiply(res);
        return pos;
    }

    public Rectangle getTowerRedrawZone(Vector2d res) {
        Vector2d towerBase = getTowerBase().multiply(res);
        Vector2d scaled_tower_length = tank_scale.multiply(towerLength);
        int xdiff = 0;
        int ydiff = 0;
        double sin = Math.sin(pos_angle - Math.PI / 2);
        double cos = Math.cos(pos_angle - Math.PI / 2);
        if (Math.round(Math.toDegrees(pos_angle)) == 0) {
            xdiff = 0;
            ydiff = -1;
        } else if (Math.round(Math.toDegrees(pos_angle)) == 90) {
            xdiff = -1;
            ydiff = -1;
        } else if (Math.round(Math.toDegrees(pos_angle)) == 180) {
            xdiff = -1;
            ydiff = -1;
        } else if (Math.round(Math.toDegrees(pos_angle)) == 270) {
            xdiff = -1;
            ydiff = 0;
        }
        xdiff *= scaled_tower_length.intX();
        ydiff *= scaled_tower_length.intY();
        return new Rectangle(towerBase.intX() + xdiff, towerBase.intY() + ydiff, scaled_tower_length.intX() + (int) Math.abs(cos) * scaled_tower_length.intX(), scaled_tower_length.intY() + (int) Math.abs(cos) * scaled_tower_length.intY());
    }

    /**
     * Returns affinetransform that rotates and moved the 
     * image to correct position in the world
     * @param scaled ScaledImage to pick scale from
     * @return
     */
    public AffineTransform getAffineTransform(ScaledImage scaled) {
        double angle = (Math.PI / 2) - pos_angle;
        float dcos = (float) (Math.abs(Math.cos(angle)) - Math.cos(angle)) / 2f;
        float dsin = (float) (Math.abs(Math.sin(angle)) - Math.sin(angle)) / 2f;
        float dsin2 = (float) (Math.abs(Math.sin(angle)) + Math.sin(angle)) / 2f;
        float dx = scaled.image.getWidth() * dcos + scaled.image.getHeight() * dsin2;
        float dy = scaled.image.getWidth() * dsin + scaled.image.getHeight() * dcos;
        float[] transform = { (float) Math.cos(angle), (float) Math.sin(angle), -(float) Math.sin(angle), (float) Math.cos(angle), scaled.location.x + dx, scaled.location.y + dy };
        return new AffineTransform(transform);
    }

    private Vector2d projectionLocation() {
        if (projectionLocation == null) {
            Vector2d diff = location.subtract(size.divide(2)).subtract(planet.location);
            projectionLocation = planet.location.add(new Vector2d(0, diff.norm()));
        }
        return projectionLocation;
    }

    private Vector2d getTowerBase() {
        double sin = Math.sin(pos_angle - Math.PI / 2);
        double cos = Math.cos(pos_angle - Math.PI / 2);
        Vector2d modifier = new Vector2d(cos, sin);
        Vector2d pos = location.subtract(size.divide(2f).multiply(modifier));
        pos = pos.add(towerAnchor.multiply(tank_scale).multiply(modifier));
        return pos;
    }

    private Vector2d getTowerTopLocation(Vector2d towerBase) {
        double sin = Math.sin((2 * Math.PI) - pos_angle + angle);
        double cos = Math.cos((2 * Math.PI) - pos_angle + angle);
        Vector2d modifier = new Vector2d(cos, sin);
        Vector2d pos = towerBase.add(modifier.multiply(towerLength).multiply(tank_scale));
        return pos;
    }

    public void setPlanet(Planet p, float angle) {
        planet = p;
        pos_angle = angle;
        size = GameFrame.gp.getEngine().getMap().getTankSize();
        tank_scale = GameFrame.gp.getEngine().getMap().getTankScale(new Vector2d(gfx));
        doGravity();
        System.out.println(name + " @ " + Math.toDegrees(angle));
    }

    public void setEngine(GameEngine e) {
        engine = e;
        if (local) {
            client.engineRunning(e);
            if (!e.isRunning()) {
                System.out.println("Starting game engine...");
                e.start();
                System.out.println("Game running.");
            }
        }
    }

    public Vector2d getLocation() {
        return location;
    }

    public Rocket fire() {
        return fire(true);
    }

    public Rocket fire(boolean send) {
        if (send) {
            client.sendFire(burn_time, angle - pos_angle);
        }
        lastFire = true;
        return new Rocket(fire_point, burn_time, angle - pos_angle, this);
    }
}
