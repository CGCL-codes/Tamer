package csel.controller.commands;

import java.util.HashMap;
import csel.controller.commandutil.Parameterizable;
import csel.controller.enums.ActionEnum;

public abstract class NBCommand implements Parameterizable {

    private HashMap<ActionEnum, Integer> slotNumberHash;

    public NBCommand() {
        this.slotNumberHash = new HashMap<ActionEnum, Integer>();
        slotNumberHash.put(ActionEnum.SLOT_1, 0);
        slotNumberHash.put(ActionEnum.SLOT_2, 1);
        slotNumberHash.put(ActionEnum.SLOT_3, 2);
        slotNumberHash.put(ActionEnum.SLOT_4, 3);
        slotNumberHash.put(ActionEnum.SLOT_5, 4);
        slotNumberHash.put(ActionEnum.SLOT_6, 5);
        slotNumberHash.put(ActionEnum.SLOT_7, 6);
        slotNumberHash.put(ActionEnum.SLOT_8, 7);
        slotNumberHash.put(ActionEnum.SLOT_9, 8);
        slotNumberHash.put(ActionEnum.SLOT_0, 9);
    }

    public Integer getInteger(ActionEnum actionEnum) {
        return slotNumberHash.get(actionEnum);
    }

    public NBCommand clone() {
        try {
            return (NBCommand) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
}
