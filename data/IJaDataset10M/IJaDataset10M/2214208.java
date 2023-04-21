package mapgen;

import objects.Planet;
import objects.Position;
import objects.Race;
import objects.geom.MapGeometry;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

/**
 * Author: serhiy
 * Created on Sep 12, 2007, 3:06:18 AM
 */
public class SpiralMapGenerator extends StandardMapGenerator {

    protected static final Random random = new Random();

    protected int branchCount;

    protected double step;

    protected double branchWidth;

    private double tmax;

    private double radius;

    private double angleStep;

    private double homeDistance;

    @Override
    public void extractData(Properties props, MapGeometry geometry) {
        super.extractData(props, geometry);
        branchCount = Integer.parseInt(props.getProperty("Branches.Count", "1"));
        branchWidth = Double.parseDouble(props.getProperty("Branches.Width", "0"));
        step = Double.parseDouble(props.getProperty("Branches.Distance"));
    }

    @Override
    protected void init(Race[] races) {
        super.init(races);
        homeDistance = homePlanetTypes.get(0).minDistance;
        angleStep = 2.0 * Math.PI / (double) branchCount;
        radius = step / angleStep;
    }

    @Override
    protected void setSize(double size) {
        homeDistance *= size / geometry.getSize();
        super.setSize(size);
    }

    @Override
    protected void createPlanets(Race[] races) throws TooManyAttempts {
        logger.finer("Creating planets");
        double k1 = 2.0 * homeDistance / radius;
        tmax = Math.sqrt(k1 * (double) ((races.length + branchCount - 1) / branchCount));
        geometry.setSize(4.0 * radius * Math.hypot(1.0, tmax));
        Position[] homes = new Position[races.length];
        for (int i = 0; i < races.length; ++i) {
            int j = i / branchCount;
            int k = i % branchCount;
            double t = Math.sqrt(k1 * ((double) j + 0.5));
            double angle = t + angleStep * (double) k;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double r = radius * t;
            double x = radius * cos + r * sin;
            double y = radius * sin - r * cos;
            homes[i] = Position.round00(new Position(x, y));
        }
        Collections.shuffle(Arrays.asList(homes));
        createInhabitedPlanets(races, homes);
        createUninhabitedPlanets();
        double hsize = 0.0;
        for (Planet planet : planets) {
            Position pos = planet.getPosition();
            hsize = Math.max(hsize, Math.abs(pos.getX()));
            hsize = Math.max(hsize, Math.abs(pos.getY()));
        }
        hsize = Math.ceil(hsize);
        geometry.setSize(4.0 * hsize);
    }

    @Override
    protected Position randomPosition() {
        double t = tmax * Math.sqrt(random.nextDouble());
        double angle = t + angleStep * (double) random.nextInt(branchCount);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double r = radius * t + branchWidth * (random.nextDouble() - 0.5);
        double x = radius * cos + r * sin;
        double y = radius * sin - r * cos;
        return new Position(x, y);
    }
}
