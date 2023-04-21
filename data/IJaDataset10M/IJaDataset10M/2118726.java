package PredatorPrey;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * The Sheep agent.
 * 
 * @author Eric Tatara 
 */
public class Sheep extends SimpleAgent {

    public Sheep(double energy) {
        this.setEnergy(energy);
        this.setHeading(Math.random() * 360);
    }

    public Sheep() {
        Parameters p = RunEnvironment.getInstance().getParameters();
        double gain = (Double) p.getValue("sheepgainfromfood");
        this.setEnergy(Math.random() * 2 * gain);
        this.setHeading(Math.random() * 360);
    }

    @Override
    public void step() {
        Context context = ContextUtils.getContext(this);
        move();
        this.setEnergy(this.getEnergy() - 1);
        Grid patch = (Grid) context.getProjection("Simple Grid");
        GridPoint point = patch.getLocation(this);
        int x = point.getX();
        int y = point.getY();
        Parameters p = RunEnvironment.getInstance().getParameters();
        double gain = (Double) p.getValue("sheepgainfromfood");
        Grass grass = null;
        for (Object o : patch.getObjectsAt(x, y)) {
            if (o instanceof Grass) grass = (Grass) o;
        }
        if (grass != null && grass.isAlive()) {
            grass.consume();
            this.setEnergy(this.getEnergy() + gain);
        }
        double rate = (Double) p.getValue("sheepreproduce");
        if (100 * Math.random() < rate) {
            this.setEnergy(this.getEnergy() / 2);
            Sheep sheep = new Sheep(this.getEnergy());
            context.add(sheep);
        }
        if (this.getEnergy() < 0) die();
    }

    @Override
    public int isSheep() {
        return 1;
    }
}
