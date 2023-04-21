package org.openaion.gameserver.model.gameobjects.stats.id;

import org.openaion.gameserver.model.gameobjects.stats.StatEffectType;

/**
 * @author Antivirus
 *
 */
public class ItemSetStatEffectId extends StatEffectId {

    private int setpart;

    private ItemSetStatEffectId(int id, int setpart) {
        super(id, StatEffectType.ITEM_SET_EFFECT);
        this.setpart = setpart;
    }

    public static ItemSetStatEffectId getInstance(int id, int setpart) {
        return new ItemSetStatEffectId(id, setpart);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = super.equals(o);
        result = (result) && (o != null);
        result = (result) && (o instanceof ItemSetStatEffectId);
        result = (result) && (((ItemSetStatEffectId) o).setpart == setpart);
        return result;
    }

    @Override
    public int compareTo(StatEffectId o) {
        int result = super.compareTo(o);
        if (result == 0) {
            if (o instanceof ItemSetStatEffectId) {
                result = setpart - ((ItemSetStatEffectId) o).setpart;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final String str = super.toString() + ",parts:" + setpart;
        return str;
    }
}
