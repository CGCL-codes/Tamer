package org.dicom4j.dicom.dictionary;

import java.util.Iterator;
import java.util.TreeMap;
import org.dicom4j.dicom.dictionary.item.DictionaryItem;

/**
 * @deprecated
 * List of {@link DictionaryItem}
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class DictionaryElements {

    private TreeMap<String, DictionaryItem> elements;

    public DictionaryElements() {
        super();
        this.elements = new TreeMap<String, DictionaryItem>();
    }

    /**
	 * Add a new element
	 * 
	 * @param aElement
	 * @return
	 */
    public DictionaryItem add(DictionaryItem aElement) {
        this.elements.put(aElement.getKey(), aElement);
        return aElement;
    }

    /**
	 * remove all elements
	 */
    public void clear() {
        this.elements.clear();
    }

    /**
	 * get number of elements
	 */
    public int count() {
        return this.elements.size();
    }

    public DictionaryItem get(String aKey) {
        return this.elements.get(aKey);
    }

    public Iterator<DictionaryItem> getIterator() {
        return this.elements.values().iterator();
    }
}
