package net.sf.jgamelibrary.physics.test;

import java.util.*;
import net.sf.jgamelibrary.geom.*;
import net.sf.jgamelibrary.physics.*;

public class DefaultEngine extends RotatingEngine<DefaultEntity> {

    private Wall w1, w2;

    DefaultEntity e1, e2, e3, e4, e5, big;

    private DefaultEntity wall;

    private ArrayList<DefaultEntity> entities = new ArrayList<DefaultEntity>(3);

    public DefaultEngine() {
        super(new MovingCollisionHandler<DefaultEntity>());
        w1 = new Wall(new Vector2D(100, 100), new Vector2D(200, 100));
        w2 = new Wall(new Vector2D(100, 200), new Vector2D(200, 200));
        e1 = new MobileEntity(Polygon2D.regularPolygon(4, 10.0, Math.PI / 2.0));
        wall = new DefaultEntity(Polygon2D.regularPolygon(20, 400, 0), false);
        wall.setPosition(new Vector2D(400, 400));
        entities.add(wall);
        e1.setPosition(new Vector2D(100, 400));
        e1.setVelocity(new Vector2D(40, 80));
        e2 = new Square(300, 500);
        e2.setVelocity(new Vector2D(100, 100));
        entities.add(e2);
        e3 = new Square(300, 200);
        e3.setVelocity(new Vector2D(-100, 20));
        entities.add(e3);
        e4 = new Square(100, 100);
        e4.setVelocity(new Vector2D(150, -100));
        entities.add(e4);
        e5 = new Square(400, 300);
        e5.setVelocity(new Vector2D(100, 0));
        e5.setMass(2);
        entities.add(e5);
        for (int i = 0; i < 10; i++) {
            DefaultEntity e = new DefaultEntity(Polygon2D.regularPolygon(3, 10, i), 1);
            e.getPosition().setCartesian(150 + ((i + 1) % 3) * 20 * i, 150 + (i % 3) * 20 * i);
            e.getVelocity().setCartesian(50 * i, -50 * i);
            entities.add(e);
        }
        big = new DefaultEntity(Polygon2D.regularPolygon(5, 100, 0), 5);
        big.getPosition().setLocation(400, 400);
        big.getVelocity().setCartesian(0, 0);
        entities.add(big);
        super.setEntities(entities);
    }

    @Override
    public DefaultEngine update(double ticks) {
        super.rotate(big, ticks);
        for (DefaultEntity e : super.getEntities()) {
            super.rotate(e, ticks);
        }
        super.update(ticks);
        return this;
    }

    private static class Square extends MobileEntity {

        public Square() {
            super(Polygon2D.regularPolygon(4, 10, Math.PI / 4));
        }

        public Square(double x, double y) {
            this();
            super.getPosition().setCartesian(x, y);
        }
    }
}
