package wrm.saferJava.oval.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sebastian Thomschke
 */
public interface CollectionFactory {

    /**
	 * Instantiate an ArrayList like list object
	 * @return a new list
	 */
    <ValueType> List<ValueType> createList();

    /**
	 * Instantiate an ArrayList like list object
	 * @return a new list
	 */
    <ValueType> List<ValueType> createList(int initialCapacity);

    /**
	 * Instantiate a HashMap like map object
	 * @return a new map
	 */
    <KeyType, ValueType> Map<KeyType, ValueType> createMap();

    /**
	 * Instantiate a HashMap like map object
	 * @return a new map
	 */
    <KeyType, ValueType> Map<KeyType, ValueType> createMap(int initialCapacity);

    /**
	 * Instantiate a HashSet like set object
	 * @return a new set
	 */
    <ValueType> Set<ValueType> createSet();

    /**
	 * Instantiate a HashSet like set object
	 * @return a new set
	 */
    <ValueType> Set<ValueType> createSet(int initialCapacity);
}
