package playground.gregor.sim2d_v2.simulation.floor;

import java.util.List;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import playground.gregor.sim2d_v2.controller.Sim2DConfig;
import playground.gregor.sim2d_v2.scenario.Scenario2DImpl;
import playground.gregor.sim2d_v2.simulation.Agent2D;
import playground.gregor.sim2d_v2.simulation.PhantomManager;

/**
 * @author laemmel
 * 
 */
public class PhantomForceModule extends AgentInteractionModule implements DynamicForceModule {

    private final PhantomManager phantomMgr;

    /**
	 * @param floor
	 * @param scenario
	 * @param phantomMgr2
	 */
    public PhantomForceModule(Floor floor, Scenario2DImpl scenario, PhantomManager phantomMgr2) {
        super(floor, scenario);
        this.phantomMgr = phantomMgr2;
    }

    @Override
    public void run(Agent2D agent) {
        List<Coordinate> neighbors = this.neighbors.get(agent);
        updateForces(agent, neighbors);
    }

    @Override
    protected void updateAgentQuadtree() {
        this.coordsQuad = new Quadtree();
        for (Coordinate c : this.phantomMgr.getPhatomsList()) {
            Envelope e = new Envelope(c);
            this.coordsQuad.insert(e, c);
        }
        this.neighbors.clear();
        for (Agent2D agent : this.floor.getAgents()) {
            double minX = agent.getPosition().x - Sim2DConfig.PNeighborhoddRange;
            double maxX = agent.getPosition().x + Sim2DConfig.PNeighborhoddRange;
            double minY = agent.getPosition().y - Sim2DConfig.PNeighborhoddRange;
            double maxY = agent.getPosition().y + Sim2DConfig.PNeighborhoddRange;
            Envelope e = new Envelope(minX, maxX, minY, maxY);
            List l = this.coordsQuad.query(e);
            this.neighbors.put(agent, l);
        }
    }

    @Override
    public void forceUpdate() {
    }
}
