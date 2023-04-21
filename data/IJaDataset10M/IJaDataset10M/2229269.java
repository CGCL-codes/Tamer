package server;

import objects.ShipGroup;
import objects.TechBlock;
import util.AccurateNumber;

public class UnmountGroupDismantleCAP extends UnmountGroupsCommand {

    static void unloadCargoToMAT(ShipGroup group, int count) {
        group.source.getCargo("MAT").changeValue(group.owner, group.source, new AccurateNumber((double) count).mul(group.getLoadedCargo()));
    }

    @Override
    void unmount(ShipGroup group, int count) {
        unloadCargoToMAT(group, count);
        double cargo = group.owner.getTechBlock().getTech(TechBlock.CARGO).doubleValue();
        double percent = (cargo - 1.0) / cargo;
        double fullMass = group.getType().mass().doubleValue() * count;
        double capMass = fullMass * percent;
        double matMass = fullMass - capMass;
        group.source.getCargo("CAP").changeValue(group.owner, group.source, new AccurateNumber(capMass));
        group.source.getCargo("MAT").changeValue(group.owner, group.source, new AccurateNumber(matMass));
    }
}
