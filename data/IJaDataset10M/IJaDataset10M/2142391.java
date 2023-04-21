package PredatorPrey;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;

/**
 * The Grass agent.
 * 
 * @author Eric Tatara 
 */
public class Grass extends SimpleAgent {

    private int countdown;

    private boolean alive;

    private static final int ALIVE = 1;

    private static final int DEAD = 0;

    public Grass(Context context, int x, int y) {
        Parameters p = RunEnvironment.getInstance().getParameters();
        int regrowTime = (Integer) p.getValue("grassregrowthtime");
        context.add(this);
        countdown = (int) (Math.random() * regrowTime);
        if (Math.random() <= 0.5) alive = true; else alive = false;
        Grid grid = (Grid) context.getProjection("Simple Grid");
        ContinuousSpace space = (ContinuousSpace) context.getProjection("Continuous Space");
        grid.moveTo(this, x, y);
        space.moveTo(this, x, y, 0);
        GridValueLayer vl = (GridValueLayer) context.getValueLayer("Grass Field");
        if (alive) vl.set(ALIVE, grid.getLocation(this).toIntArray(null)); else vl.set(DEAD, grid.getLocation(this).toIntArray(null));
    }

    @Override
    public void step() {
        if (!alive) {
            if (countdown <= 0) {
                Parameters p = RunEnvironment.getInstance().getParameters();
                int regrowTime = (Integer) p.getValue("grassregrowthtime");
                alive = true;
                updateValueLayer();
                countdown = regrowTime;
            } else countdown--;
        }
    }

    /**
   * Called by sheep when they eay this grass.
   */
    public void consume() {
        alive = false;
        updateValueLayer();
    }

    /**
   * Update the value layer to reflect the status of the grass health.
   */
    private void updateValueLayer() {
        GridValueLayer vl = (GridValueLayer) ContextUtils.getContext(this).getValueLayer("Grass Field");
        Grid grid = (Grid) ContextUtils.getContext(this).getProjection("Simple Grid");
        if (alive) vl.set(ALIVE, grid.getLocation(this).toIntArray(null)); else vl.set(DEAD, grid.getLocation(this).toIntArray(null));
    }

    /**
	 * @return if the sheep is alive.
	 */
    public boolean isAlive() {
        return alive;
    }
}
