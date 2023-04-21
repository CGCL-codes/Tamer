package br.eb.ime.orivalde.algo;

import java.util.ArrayList;
import br.eb.ime.orivalde.matrix.ShortestPathMatrix;
import br.eb.ime.orivalde.model.Client;
import br.eb.ime.orivalde.model.Depot;
import br.eb.ime.orivalde.model.RouteGroup;
import br.eb.ime.orivalde.model.local.Local;

/**
 *
 * @author Orivalde
 */
public abstract class VRPSolver {

    /**List of shops to visit*/
    public ArrayList<Client> client4Today;

    /**Solution of the problem*/
    public RouteGroup bestSolution;

    /**The depot ^_^ */
    public Depot depot;

    /**Data of VRPdata*/
    double speed;

    double costPerKm;

    double downloadTime;

    double vehicleCapacity;

    double maximumWorkTime;

    public ShortestPathMatrix shortestPathMatrix;

    public VRPSolver(MDVRPdata input, Depot depot) {
        this.client4Today = depot.getClient4Today();
        ArrayList<Integer> localListID = (ArrayList<Integer>) new ArrayList<Integer>();
        for (int i = 0; i < client4Today.size(); i++) {
            Integer I = Integer.valueOf(client4Today.get(i).getLocalID());
            localListID.add(I);
        }
        this.bestSolution = new RouteGroup();
        this.depot = depot;
        this.speed = input.getSpeed();
        this.vehicleCapacity = input.getVehicleCapacity();
        this.maximumWorkTime = input.getMaximumWorkTime();
        this.shortestPathMatrix = input.getShortPathMatrix();
        this.costPerKm = input.getCostPerKm();
        this.downloadTime = input.getDownloadTime();
    }

    public abstract void searchRoutes(final int thread);
}
