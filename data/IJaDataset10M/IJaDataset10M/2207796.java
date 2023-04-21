package paulo;

import java.awt.Rectangle;

public class Entity {

    protected long uId;

    protected String spriteSheet;

    protected String name;

    protected int mapId;

    protected int x, y, tX, tY;

    protected int speed;

    protected int range;

    protected Rectangle rect;

    protected Stats stats = new Stats();

    public Entity(long uId, String spriteSheet, String name, int speed, int mapId, int x, int y, int range) {
        this.uId = uId;
        this.spriteSheet = spriteSheet;
        this.name = name;
        this.speed = speed;
        this.mapId = mapId;
        tX = x;
        tY = y;
        this.x = tX;
        this.y = tY;
        this.range = range;
        rect = new Rectangle(x, y, 32, 64);
    }

    public void move() {
        if (tX > Game.getInstance().maps.get(mapId).image.getWidth()) {
            tX = x;
        }
        if (tY > Game.getInstance().maps.get(mapId).image.getHeight()) {
            tY = y;
        }
        if (Math.abs(tX - x) < speed) {
            x = tX;
        }
        if (Math.abs(tY - y) < speed) {
            y = tY;
        }
        if (x != tX || y != tY) {
            double angle = (6.28 / 360) * Math.atan2(tX - x, tY - y) * 180 / Math.PI;
            x = (int) (x + speed * Math.sin(angle));
            y = (int) (y + speed * Math.cos(angle));
        }
        rect.setLocation(x, y);
    }
}
