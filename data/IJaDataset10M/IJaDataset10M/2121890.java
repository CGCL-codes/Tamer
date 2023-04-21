package repast.simphony.demo.schelling;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.VNQuery;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/**
 * The agent for the Schelling model. 
 * 
 * @author Eric Tatara 
 */
public class Agent {

    private int maxAge, currentAge, type;

    private double percentLikeNeighbors;

    private String id;

    private double numLikeMe;

    private double neighborCount;

    private int numberOfAgentTypes = 3;

    public Agent(String id) {
        this.id = id;
        Parameters p = RunEnvironment.getInstance().getParameters();
        int minDeathAge = (Integer) p.getValue("minDeathAge");
        int maxDeathAge = (Integer) p.getValue("maxDeathAge");
        this.type = RandomHelper.nextIntFromTo(0, numberOfAgentTypes - 1);
        this.maxAge = RandomHelper.nextIntFromTo(minDeathAge, maxDeathAge);
        this.percentLikeNeighbors = (Double) p.getValue("percentLikeNeighbors");
    }

    @ScheduledMethod(start = 0, interval = 1)
    public void step() {
        Context<Agent> context = (Context) ContextUtils.getContext(this);
        Grid<Agent> grid = (Grid) context.getProjection("Grid");
        boolean lookingForNewSite = true;
        while (lookingForNewSite) {
            VNQuery<Agent> query = new VNQuery<Agent>(grid, this);
            numLikeMe = 0;
            neighborCount = 0;
            for (Agent agent : query.query()) {
                if (agent.getType() == this.getType()) numLikeMe++;
                neighborCount++;
            }
            if (numLikeMe / neighborCount >= percentLikeNeighbors) {
                lookingForNewSite = false;
            } else {
                int width = grid.getDimensions().getWidth();
                int height = grid.getDimensions().getHeight();
                int x = RandomHelper.nextIntFromTo(1, width - 1);
                int y = RandomHelper.nextIntFromTo(1, height - 1);
                while (grid.getObjectAt(x, y) != null) {
                    x = RandomHelper.nextIntFromTo(1, width - 1);
                    y = RandomHelper.nextIntFromTo(1, height - 1);
                }
                grid.moveTo(this, x, y);
            }
        }
        currentAge++;
        if (currentAge >= maxAge) this.die();
    }

    public void die() {
        Context<Agent> context = (Context) ContextUtils.getContext(this);
        context.remove(this);
        Agent child = new Agent(this.id);
        context.add(child);
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getCurrentAge() {
        return currentAge;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return this.id;
    }

    public double getNumLikeMe() {
        return numLikeMe;
    }

    public double getNeighborCount() {
        return neighborCount;
    }
}
