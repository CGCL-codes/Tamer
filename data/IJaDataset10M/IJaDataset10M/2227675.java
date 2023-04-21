package wood.model.item;

import wood.model.map.Map.Tile;

public class CrystalBow extends EquippableItem {

    private static final long serialVersionUID = -8999964131957625509L;

    public CrystalBow(Tile tile) {
        super(tile, EquipClass.GENERIC);
    }
}
