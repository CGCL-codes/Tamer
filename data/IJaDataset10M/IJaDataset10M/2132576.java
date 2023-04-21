package middleend.items;

/**
 * @author ivan__g__s
 */
public final class ArmorMetal extends Armor {

    public ArmorMetal(int x, int y, int strength) {
        super(x, y, strength);
    }

    public ArmorMetal(ArmorMetal other) {
        this(other.x, other.y, other.strength);
    }

    public ArmorMetal(int x, int y) {
        this(MapItemsDefaults.getArmorMetal(x, y));
    }

    public ArmorMetal() {
        this(MapItemsDefaults.getArmorMetal(0, 0));
    }
}
