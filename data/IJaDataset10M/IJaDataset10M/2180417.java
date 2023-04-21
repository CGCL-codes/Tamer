package influx.dtc.collection.impl;

import influx.dtc.collection.IEntityKeyListDTC;
import influx.dtc.collection.IKeyListCompositeDTC;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Search composite Data Transfer Container implementation used for storing multiple key list DTCs
 * 
 * @author whoover
 * @param <KEY>
 *            the key type
 */
public class KeyListCompositeDTC<KEY> extends EntityKeyListDTC<KEY, IEntityKeyListDTC<?, ?>> implements IKeyListCompositeDTC<KEY> {

    protected static final Logger LOG = LoggerFactory.getLogger(KeyListCompositeDTC.class);

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public KeyListCompositeDTC() {
    }

    /**
	 * Distinct key constructor
	 * 
	 * @param distinctListKey
	 *            the distinctListKey
	 */
    public KeyListCompositeDTC(final String distinctListKey) {
        setDistinctListKey(distinctListKey);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setCollection(final List<IEntityKeyListDTC<?, ?>> listKeyDTCs) {
        if (listKeyDTCs != null) {
            String[] ids = new String[listKeyDTCs.size()];
            for (IEntityKeyListDTC<?, ?> listKeyDTC : listKeyDTCs) {
                for (String id : ids) {
                    if (id != null && id.equalsIgnoreCase(listKeyDTC.getDistinctListKey())) {
                        LOG.error("Search DTC IDs must be unique... " + "Unable to set the specified list!");
                        return;
                    }
                }
                ids[ids.length] = listKeyDTC.getDistinctListKey();
            }
        }
        super.setCollection(listKeyDTCs);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<IEntityKeyListDTC<?, ?>> getCollection() {
        return super.getCollection();
    }

    /**
	 * {@inheritDoc}
	 */
    public <K, ITEMS> void addKeyListDTC(final IEntityKeyListDTC<K, ITEMS> item) {
        getCollection().add(item);
    }

    /**
	 * {@inheritDoc}
	 */
    public final <K, ITEMS> List<IEntityKeyListDTC<K, ITEMS>> getKeyListDTCsByClassTypes(final Class<? super K> keyClass, final Class<? super ITEMS> itemsClass) throws IndexOutOfBoundsException {
        return getKeyListDTCForClassTypes(keyClass, itemsClass, true);
    }

    /**
	 * {@inheritDoc}
	 */
    public final <K, ITEMS> IEntityKeyListDTC<K, ITEMS> getKeyListDTCByClassTypes(final Class<? super K> searchKeyClass, final Class<? super ITEMS> itemsClass) throws NullPointerException {
        List<IEntityKeyListDTC<K, ITEMS>> list = getKeyListDTCForClassTypes(searchKeyClass, itemsClass, false);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
	 * Gets the composite search DTC within the list that has the specified item class. The specified key class and search by class are present for generic consistency, but cannot be null. If more
	 * than one search DTCs exist only the first occurrence will be returned when return null is false. If there are not any items found a null entry will be returned. When return all is true all of
	 * the results with the describe class specification will be returned.
	 * 
	 * @param <K>
	 *            key
	 * @param <ITEMS>
	 *            the annotated {@link influx.model.Entity} items type
	 * @param keyClass
	 *            the keyClass
	 * @param itemClass
	 *            the itemClass
	 * @param returnAll
	 *            boolean whether it returns all or not
	 * @return the list of class matched items
	 * @throws NullPointerException
	 *             NullPointerException
	 */
    @SuppressWarnings("unchecked")
    private final <K, ITEMS> List<IEntityKeyListDTC<K, ITEMS>> getKeyListDTCForClassTypes(final Class<? super K> keyClass, final Class<? super ITEMS> itemClass, final boolean returnAll) throws NullPointerException {
        List<IEntityKeyListDTC<K, ITEMS>> list = new ArrayList<IEntityKeyListDTC<K, ITEMS>>();
        for (IEntityKeyListDTC listItem : getCollection()) {
            if (isAssignableFrom(listItem, keyClass, itemClass)) {
                list.add((IEntityKeyListDTC<K, ITEMS>) listItem);
                if (!returnAll) {
                    list.add((IEntityKeyListDTC<K, ITEMS>) listItem);
                    return list;
                }
            }
        }
        return list;
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public final <K, ITEMS> IEntityKeyListDTC<K, ITEMS> getKeyListDTCByName(final String name) {
        if (getCollection() == null || name == null || name.length() == 0) {
            return null;
        }
        for (IEntityKeyListDTC<?, ?> keyListDTC : getCollection()) {
            if (keyListDTC.getName() != null && name.equalsIgnoreCase(keyListDTC.getName())) {
                return (IEntityKeyListDTC<K, ITEMS>) keyListDTC;
            }
        }
        return null;
    }

    /**
	 * Validates that the specified search DTCs key, items, and application user ID are assignable from the corresponding classes provided.
	 * 
	 * @param <ITEMS>
	 *            the annotated {@link influx.model.Entity} items type
	 * @param keyListDTC
	 *            the keyListDTC
	 * @param keyClass
	 *            the keyClass
	 * @param itemClass
	 *            the itemClass
	 * @return is the key list DTC assignable from the specified classes
	 */
    protected static final <ITEMS> boolean isAssignableFrom(final IEntityKeyListDTC<?, ITEMS> keyListDTC, final Class<?> keyClass, final Class<? super ITEMS> itemClass) {
        if (keyListDTC == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("key list DTC cannot be null");
            }
            return false;
        }
        if (keyClass == null || keyListDTC.getKey() == null || !keyClass.isAssignableFrom(keyListDTC.getKey().getClass())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No match for key class: " + keyClass);
            }
            return false;
        }
        if (itemClass == null || keyListDTC.getCollection() == null || keyListDTC.getCollection().isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Item class and key list DTC list " + "of items cannot be null");
            }
            return false;
        }
        for (final Object searchItem : keyListDTC.getCollection()) {
            if (!itemClass.isAssignableFrom(searchItem.getClass())) {
                return false;
            }
        }
        return true;
    }
}
