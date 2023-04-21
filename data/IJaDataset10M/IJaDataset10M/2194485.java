package com.volantis.shared.net.http;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of HTTPMessageEntities that provides more interesting
 * that minimum specified.
 */
public class SimpleHTTPMessageEntities implements HTTPMessageEntities {

    /**
     * The underlying container for the entities.
     */
    private Collection entities = new ArrayList();

    public HTTPMessageEntity[] put(HTTPMessageEntity entity) {
        HTTPMessageEntity replaced[] = remove(entity.getIdentity());
        entities.add(entity);
        return replaced;
    }

    public Iterator iterator() {
        return entities.iterator();
    }

    public Iterator namesIterator() {
        return new Iterator() {

            private Iterator iterator = entities.iterator();

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Object next() {
                HTTPMessageEntity next = (HTTPMessageEntity) iterator.next();
                return next.getName();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public HTTPMessageEntity[] remove(HTTPMessageEntityIdentity identity) {
        List removeList = retrieveIdentifiedEntities(identity);
        if (removeList != null) {
            for (int i = 0; i < removeList.size(); i++) {
                entities.remove(removeList.get(i));
            }
        }
        return listToTypedArray(removeList);
    }

    public void clear() {
        entities.clear();
    }

    public HTTPMessageEntity[] retrieve(HTTPMessageEntityIdentity identity) {
        List retrieved = retrieveIdentifiedEntities(identity);
        return listToTypedArray(retrieved);
    }

    public boolean containsIdentity(HTTPMessageEntityIdentity identity) {
        Iterator iterator = entities.iterator();
        boolean found = false;
        HTTPMessageEntity entity = null;
        while (iterator.hasNext() && !found) {
            entity = (HTTPMessageEntity) iterator.next();
            found = entity.getIdentity().identityEquals(identity);
        }
        return found;
    }

    public void add(HTTPMessageEntity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
    }

    public HTTPMessageEntity[] put(HTTPMessageEntity[] newEntities) {
        List replacedList = null;
        for (int i = 0; i < newEntities.length; i++) {
            HTTPMessageEntity entity = newEntities[i];
            List retrieved = retrieveIdentifiedEntities(entity.getIdentity());
            if (retrieved != null) {
                if (replacedList == null) {
                    replacedList = retrieved;
                } else {
                    replacedList.addAll(retrieved);
                }
                remove(entity.getIdentity());
            }
        }
        for (int i = 0; i < newEntities.length; i++) {
            entities.add(newEntities[i]);
        }
        return listToTypedArray(replacedList);
    }

    public Iterator valuesIterator(final HTTPMessageEntityIdentity identity) {
        return new Iterator() {

            private Iterator iterator = null;

            private Iterator getIterator() {
                if (iterator == null) {
                    List entities = retrieveIdentifiedEntities(identity);
                    if (entities == null) {
                        iterator = Collections.EMPTY_LIST.iterator();
                    } else {
                        iterator = entities.iterator();
                    }
                }
                return iterator;
            }

            public boolean hasNext() {
                return getIterator().hasNext();
            }

            public Object next() {
                HTTPMessageEntity next = (HTTPMessageEntity) getIterator().next();
                return next.getValue();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int size() {
        return entities.size();
    }

    /**
     * Find all the entities with a given name. Put them into a Collection
     * and return this Collection.
     * @param identity The name of the entities to retrieve.
     * @return A Collection containing all the entities within this
     * HTTPMessageEntities that have the name specified.
     */
    protected List retrieveIdentifiedEntities(HTTPMessageEntityIdentity identity) {
        ArrayList retrieved = null;
        Iterator iterator = entities.iterator();
        boolean found = false;
        HTTPMessageEntity entity = null;
        while (iterator.hasNext() && !found) {
            entity = (HTTPMessageEntity) iterator.next();
            if (entity.getIdentity().identityEquals(identity)) {
                if (retrieved == null) {
                    retrieved = new ArrayList();
                }
                retrieved.add(entity);
            }
        }
        return retrieved;
    }

    /**
     * Given a list return an array of the same type as the class of the first
     * element in list that contains all the elements in list. It is assumed
     * that all the elements in list are of the same class.
     * @param list The list.
     * @return An array of the type of the list elements or null if list is
     * null. If there are no elements in list an HTTPMessageEntity array of
     * 0 length is returned.
     */
    private HTTPMessageEntity[] listToTypedArray(List list) {
        HTTPMessageEntity array[] = null;
        if (list != null) {
            Object entity = list.get(0);
            if (entity != null) {
                Class arrayClass = entity.getClass();
                Object arrayType[] = (Object[]) Array.newInstance(arrayClass, 0);
                array = (HTTPMessageEntity[]) list.toArray(arrayType);
            } else {
                array = new HTTPMessageEntity[0];
            }
        }
        return array;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleHTTPMessageEntities)) return false;
        final SimpleHTTPMessageEntities entities1 = (SimpleHTTPMessageEntities) o;
        if (!entities.equals(entities1.entities)) return false;
        return true;
    }

    public int hashCode() {
        return entities.hashCode();
    }
}
