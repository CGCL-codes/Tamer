package hokutonorogue.object;

import com.golden.gamedev.util.*;
import hokutonorogue.character.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class Belt extends Cloth {

    public enum Type {

        KEN(50), REI(51), JAGGER(52), SHIN(53), TOKI(54), SOUTHER(55), ZED(56), GOLAN_MAJOR(57), YUDA(58), SHU(59), GOLAN_SOLDIER(142);

        private final int index;

        Type(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

        public static Type random() {
            int i = Utility.getRandom(0, Type.values().length - 1);
            return Type.values()[i];
        }
    }

    public Belt() {
        this.name = "BELT";
    }

    public Belt(int index) {
        this.index = index;
        this.name = "BELT";
    }

    public EquipableHokutoObject getEquipped(CharacterModel character) {
        return character.getWaist().getBelt();
    }

    /**
     * _equip
     *
     * @param character CharacterModel
     * @todo Implement this hokutonorogue.object.Cloth method
     */
    protected void _equipTo(CharacterModel character) {
        if (character.getWaist().getBelt() != null) {
            character.getWaist().getBelt().unequip();
        }
        character.getWaist().setBelt(this);
    }

    /**
     * _remove
     *
     * @param character CharacterModel
     * @todo Implement this hokutonorogue.object.Cloth method
     */
    protected void _unequip() {
        character.getWaist().setBelt(null);
    }

    public int randomType() {
        return Type.random().index();
    }
}
