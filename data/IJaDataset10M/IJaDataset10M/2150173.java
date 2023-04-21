package lelouet.datacenter.thermal.impacts.impactGenerators;

import gridlib.api.Grid;
import gridlib.api.Machine;
import java.rmi.RemoteException;
import lelouet.datacenter.thermal.impacts.ImpactGenerator;
import lelouet.datacenter.thermal.impacts.ImpactMap;

/**
 * for each server, add an impact on each other server depending on the distance
 * the air has to go by to come from one to another server<br />
 * the model is : impact(from, to) += impact(from, from)*(10/(10+distance(from,
 * to)))
 */
public class WithDistanceImpact implements ImpactGenerator {

    protected double rackHeight;

    /**
	 * @param rackHeight
	 *            the height, in cm, of the racks
	 */
    public WithDistanceImpact(double rackHeight) {
        this.rackHeight = rackHeight;
    }

    @Override
    public void apply(Grid grid, ImpactMap storage) {
        try {
            for (Machine from : grid.getAllMachines()) {
                double selfImpact = storage.getImpact(from, from);
                for (Machine to : grid.getAllMachines()) {
                    double presentImpact = storage.getImpact(from, to);
                    presentImpact += selfImpact * 10 / (10 + getDistance(from, to));
                    storage.setImpact(from, to, presentImpact);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace(System.err);
        }
    }

    protected double getDistance(Machine from, Machine to) {
        try {
            double fromRackDist = rackHeight - from.getLevel().getNumber() * lelouet.datacenter.Utils.U_HEIGHT_CM;
            if (fromRackDist < 0) {
                fromRackDist = 0;
            }
            double toRackDist = rackHeight - to.getLevel().getNumber() * lelouet.datacenter.Utils.U_HEIGHT_CM;
            if (toRackDist < 0) {
                toRackDist = 0;
            }
            return toRackDist + fromRackDist + horizontaldistance(from, to);
        } catch (RemoteException e) {
            e.printStackTrace(System.err);
            return Double.POSITIVE_INFINITY;
        }
    }

    protected static double horizontaldistance(Machine from, Machine to) {
        double distX = 0;
        try {
            distX = from.getLevel().getRack().getPosX() - to.getLevel().getRack().getPosX();
        } catch (RemoteException e) {
            e.printStackTrace(System.err);
        }
        double distY = 0;
        try {
            distY = from.getLevel().getRack().getPosY() - to.getLevel().getRack().getPosY();
        } catch (RemoteException e) {
            e.printStackTrace(System.err);
        }
        return Math.sqrt(distX * distX + distY * distY);
    }
}
