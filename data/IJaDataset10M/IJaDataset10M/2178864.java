package net.sourceforge.aidungeon.common.map.mapObject.item;

import net.sourceforge.aidungeon.common.map.mapObject.MapObject;

public interface ItemMapObject extends MapObject {

    public int getWeight();

    public boolean canPickUp();
}
