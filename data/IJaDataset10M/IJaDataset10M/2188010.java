package afse;

import massim.af.SimStatus;
import massim.af.WorldModel;
import massim.map.MapViewer;
import com.agentfactory.platform.impl.AbstractPlatformService;

public class MapService extends AbstractPlatformService {

    private WorldModel worldModel;

    private MapViewer viewer;

    private SimStatus simStatus;

    @Override
    public void onStart() {
        viewer = new MapViewer("AFMapper: " + this.getName());
        worldModel = new WorldModel(viewer);
        viewer.setVisible(true);
    }

    public synchronized void processSimStart(SimStatus simStatus) {
        if ((this.simStatus == null) || (!this.simStatus.getSimulationID().equals(simStatus.getSimulationID()))) {
            worldModel.initialize(simStatus.getSizeX(), simStatus.getSizeY());
            int[] c = simStatus.getCorral();
            worldModel.setCorral(c[0], c[1], c[2], c[3]);
            worldModel.setTotalStepsInSimulation(simStatus.getTotalSteps());
            worldModel.setOpponentID(simStatus.getOpponentID());
            worldModel.setSimulationID(simStatus.getSimulationID());
            this.simStatus = simStatus;
        }
    }

    /**
     * Setup the shared world model based on current simulation information
     * @param simStatus
     * @param opponentID
     * @param simulationID
     */
    public synchronized void processSimEnd() {
        if (simStatus != null) {
            worldModel = new WorldModel(viewer);
            simStatus = null;
        }
    }

    public WorldModel getWorldModel() {
        return worldModel;
    }
}
