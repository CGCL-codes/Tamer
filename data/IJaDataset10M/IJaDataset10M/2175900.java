package wood.model.item;

import wood.model.map.Map.Tile;

public class Axe extends EquippableItem {

    private static final long serialVersionUID = 4648105612207536628L;

    public Axe(Tile tile) {
        super(tile, EquipClass.ONE_HANDED);
    }

    @Override
    protected int getBasePhysAttack() {
        return 190;
    }

    @Override
    protected int getBaseMagAttack() {
        return 80;
    }
}
