package perun.common.xml;

import java.util.Vector;

/**
 * This class is used to represent the XML file "pointer" while processing
 * triggers/tasks parameters. Using this class are parameters stored into
 * appropriate HashMap (HashTable) - perun.trigger.parameters.<name>
 */
public class XMLPath extends Vector<String> {

    public static final String EMPTY_STRING = "";

    /**
	 * Adds path element to the end of the path
	 *
	 * @param element is the element
	 */
    public void addPathElement(String element) {
        add(element);
    }

    /**
	 * Removes the last element from the path
	 */
    public void removeLastPathElement() {
        if (elementCount > 0) elementData[--elementCount] = null;
    }

    /**
	 * converts path to String
	 *
	 * @return XML path including the last element.
	 */
    public String toString() {
        StringBuilder output = new StringBuilder();
        if (elementCount == 0) return EMPTY_STRING;
        for (int i = 0; i < elementCount; i++) {
            output.append(elementData[i].toString()).append('.');
        }
        return output.toString();
    }

    /**
	 * Gets number of stored elements
	 *
	 * @return How many elements is stored in path
	 */
    public int getDepth() {
        return elementCount;
    }
}
