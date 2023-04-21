package orm;

import org.orm.*;

public class Tcc_asistenciaeventoSetCollection extends org.orm.util.ORMSet {

    public Tcc_asistenciaeventoSetCollection(Object owner, org.orm.util.ORMAdapter adapter, int ownerKey, int targetKey, int collType) {
        super(owner, adapter, ownerKey, targetKey, true, collType);
    }

    public Tcc_asistenciaeventoSetCollection(Object owner, org.orm.util.ORMAdapter adapter, int ownerKey, int collType) {
        super(owner, adapter, ownerKey, -1, false, collType);
    }

    /**
	* Return an iterator over the persistent objects
	* @return The persisten objects iterator
	*/
    public java.util.Iterator getIterator() {
        return super.getIterator(_ownerAdapter);
    }

    /**
	 * Add the specified persistent object to ORMSet
	 * @param value the persistent object
	 */
    public void add(Tcc_asistenciaevento value) {
        if (value != null) {
            super.add(value, value._ormAdapter);
        }
    }

    /**
	 * Remove the specified persistent object from ORMSet
	 * @param value the persistent object
	 */
    public void remove(Tcc_asistenciaevento value) {
        super.remove(value, value._ormAdapter);
    }

    /**
	 * Return true if ORMSet contains the specified persistent object
	 * @param value the persistent object
	 * @return True if this contains the specified persistent object
	 */
    public boolean contains(Tcc_asistenciaevento value) {
        return super.contains(value);
    }

    /**
	 * Return an array containing all of the persistent objects in ORMSet
	 * @return The persistent objects array
	 */
    public Tcc_asistenciaevento[] toArray() {
        return (Tcc_asistenciaevento[]) super.toArray(new Tcc_asistenciaevento[size()]);
    }

    /**
	 * Return an sorted array containing all of the persistent objects in ORMSet
	 * @param propertyName Name of the property for sorting:<ul>
	 * <li>ae_id</li>
	 * </ul>
	 * @return The persistent objects sorted array
	 */
    public Tcc_asistenciaevento[] toArray(String propertyName) {
        return toArray(propertyName, true);
    }

    /**
	 * Return an sorted array containing all of the persistent objects in ORMSet
	 * @param propertyName Name of the property for sorting:<ul>
	 * <li>ae_id</li>
	 * </ul>
	 * @param ascending true for ascending, false for descending
	 * @return The persistent objects sorted array
	 */
    public Tcc_asistenciaevento[] toArray(String propertyName, boolean ascending) {
        return (Tcc_asistenciaevento[]) super.toArray(new Tcc_asistenciaevento[size()], propertyName, ascending);
    }

    protected PersistentManager getPersistentManager() throws PersistentException {
        return orm.ConanControlPersistentManager.instance();
    }
}
