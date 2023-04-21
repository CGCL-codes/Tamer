package server.order;

import objects.GalaxyException;
import objects.Race;
import objects.ShipGroup;
import server.BadCommandFormatException;
import java.io.PrintWriter;

public class UnmountGroups extends AbstractOrderCommand {

    @Override
    public final void exec(Race race, String[] cmd, PrintWriter out) throws GalaxyException {
        if (cmd.length <= 1) throw new BadCommandFormatException();
        ShipGroup group = parseShipGroup(cmd[1], race);
        if (!group.canSeePlanet()) throw new GalaxyException("Ships must be in orbit");
        int count = parseCount(2, cmd, group);
        unmount(group, count);
        if (group.remove(count)) race.updateVisibility(group.getSource());
    }

    void unmount(ShipGroup group, int count) {
        group.getSource().addMaterials(count * group.getType().mass());
    }
}
