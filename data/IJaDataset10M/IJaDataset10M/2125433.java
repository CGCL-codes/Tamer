package com.cell.rpg.quest.formula;

import com.cell.rpg.formula.ObjectProperty;
import com.cell.rpg.quest.TriggerUnitType;
import com.g2d.annotation.Property;

@Property("单位-属性")
public class TriggerUnitProperty extends ObjectProperty {

    @Property("单位类型")
    public TriggerUnitType trigger_unit_type = TriggerUnitType.PLAYER;

    @Override
    public String toString() {
        return trigger_unit_type + "." + super.toString();
    }
}
