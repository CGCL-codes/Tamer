package net.sf.l2j.util;

import java.util.Iterator;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;

/**
 * This class ...
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */
public abstract class L2ObjectSet<T extends L2Object> implements Iterable<T> {

    public static L2ObjectSet<L2Object> createL2ObjectSet() {
        switch(Config.SET_TYPE) {
            case WorldObjectSet:
                return new WorldObjectSet<L2Object>();
            default:
                return new L2ObjectHashSet<L2Object>();
        }
    }

    public static L2ObjectSet<L2PlayableInstance> createL2PlayerSet() {
        switch(Config.SET_TYPE) {
            case WorldObjectSet:
                return new WorldObjectSet<L2PlayableInstance>();
            default:
                return new L2ObjectHashSet<L2PlayableInstance>();
        }
    }

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract void clear();

    public abstract void put(T obj);

    public abstract void remove(T obj);

    public abstract boolean contains(T obj);

    public abstract Iterator<T> iterator();
}
