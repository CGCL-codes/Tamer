package org.openwar.victory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.openwar.victory.util.Filter;

public class EntityList implements Iterable<Entity> {

    private static EntityList instance;

    public static EntityList get() {
        if (instance == null) {
            instance = new EntityList();
        }
        return instance;
    }

    private List<Entity> list;

    private List<EntityIterator> iterators;

    private EntityList() {
        list = new ArrayList<Entity>();
        iterators = new ArrayList<EntityIterator>();
    }

    @Override
    public EntityIterator iterator() {
        return iterator(null);
    }

    public EntityIterator iterator(final Class<? extends Entity> type) {
        final EntityIterator i = new EntityIterator(type);
        iterators.add(i);
        return i;
    }

    public Entity add(final Entity e) {
        list.add(e);
        e.setState(Entity.State.ACTIVE);
        return e;
    }

    public void remove(final Entity e) {
        e.setState(Entity.State.REMOVED);
        list.remove(e);
    }

    /**
     * @return The list of entities. It's advisable not to use this.
     * Use <code>iterator()</code> instead, or just use
     * <code>for(Entity e : EntityList.get())</code>.
     */
    public List<Entity> getAll() {
        return list;
    }

    /**
     * @return The total number of game objects in the game.
     */
    public int size() {
        return list.size();
    }

    /**
     * The total number of entities of a specific type in the game.
     * @param entityType The type of entity as a <code>Class</code> instance.
     * @return The total number of objects of this type.
     */
    public int size(final Class<? extends Entity> entityType) {
        int count = 0;
        for (Entity e : list) {
            if (e.getState() == Entity.State.ACTIVE && e.getClass() == entityType) {
                count++;
            }
        }
        return count;
    }

    /**
     * Remove all entities, except those that are persistent.
     */
    public void removeAll() {
        for (int i = 0; i < list.size(); i++) {
            final Entity e = list.get(i);
            remove(e);
            i--;
        }
    }

    private void removeIndex(final int index) {
        list.remove(index).setState(Entity.State.REMOVED);
        for (EntityIterator i : iterators) {
            i.indexRemoved(index);
        }
    }

    private void iteratorDone(final EntityIterator i) {
        iterators.remove(i);
    }

    /**
     * Iterator for entities.
     * @author Bart van Heukelom
     */
    public class EntityIterator implements Iterator<Entity>, Iterable<Entity> {

        private int index = -1;

        private Class<? extends Entity> type = null;

        public EntityIterator() {
        }

        public EntityIterator(final Class<? extends Entity> t) {
            type = t;
        }

        @Override
        public boolean hasNext() {
            boolean has = false;
            if (type == null) {
                has = list.size() > index + 1;
            } else {
                int i = index;
                while (!has) {
                    if (list.size() > i + 1) {
                        final Entity entity = list.get(++i);
                        if (type == null || entity.getClass() == type) {
                            has = true;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (!has) {
                iteratorDone(this);
            }
            return has;
        }

        @Override
        public Entity next() {
            while (true) {
                if (hasNext()) {
                    final Entity entity = list.get(++index);
                    if (type == null || entity.getClass() == type) {
                        return entity;
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }
        }

        @Override
        public void remove() {
            removeIndex(index);
        }

        private void indexRemoved(final int r) {
            if (r <= index) {
                index--;
            }
        }

        @Override
        public Iterator<Entity> iterator() {
            return this;
        }
    }

    public void sort() {
        Collections.sort(list);
    }
}
