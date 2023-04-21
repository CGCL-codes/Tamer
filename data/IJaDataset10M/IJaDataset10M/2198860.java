package com.kenstevens.stratdom.site.action.exploding;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.model.Data;
import com.kenstevens.stratdom.model.Unit;
import com.kenstevens.stratdom.move.Movement;
import com.kenstevens.stratdom.site.action.ActionFactory;
import com.kenstevens.stratdom.ui.SectorCoords;

@Scope("prototype")
@Component
public class MoveUnitExplodingAction extends ExplodingAction {

    @Autowired
    ActionFactory actionFactory;

    @Autowired
    Data db;

    private final Unit unit;

    private final SectorCoords target;

    public MoveUnitExplodingAction(Unit unit, SectorCoords target) {
        this.unit = unit;
        this.target = target;
    }

    @Override
    public void explode() {
        actionFactory.unitSelected(unit, false);
        Movement movement = new Movement(unit, db.getWorld());
        final List<SectorCoords> hops = movement.getHops(unit, target);
        for (SectorCoords hop : hops) {
            actionFactory.moveHop(unit, hop);
        }
    }

    @Override
    public String getDescription() {
        return "Move " + unit.getType() + " to " + target.x + "," + target.y;
    }

    public Unit getUnit() {
        return unit;
    }
}
