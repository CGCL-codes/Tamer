package com.springrts.ai.crans.building;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.clb.Command;
import com.springrts.ai.oo.clb.Unit;
import com.springrts.ai.oo.clb.UnitDef;

class UpgradeTask implements Task {

    final Unit oldBuilding;

    final AIFloat3 oldBuildingPos;

    final UnitDef newBuildingDef;

    boolean waitingForBuildSiteToClear;

    final Set<Unit> builders = new HashSet<Unit>();

    UpgradeTask(final Unit oldBuilding, final AIFloat3 oldBuildingPos, final UnitDef buildingDef) {
        this.oldBuilding = oldBuilding;
        this.oldBuildingPos = new AIFloat3(oldBuildingPos);
        this.newBuildingDef = buildingDef;
        this.waitingForBuildSiteToClear = false;
    }

    /**
     * Verify the task and the units current orders match. For a upgrade task, this means the unit must have a reclaim
     * order.
     */
    public boolean verify(final Unit builder) {
        final List<Command> currentCommands = builder.getCurrentCommands();
        if (currentCommands.isEmpty()) {
            return waitingForBuildSiteToClear;
        }
        final Command currentCommand = currentCommands.get(0);
        final int commandId = currentCommand.getId();
        return commandId == OldCmdHelper.CMD_RECLAIM.value;
    }
}
