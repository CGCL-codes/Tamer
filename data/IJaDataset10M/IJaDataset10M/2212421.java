package de.bioutils.gff3.attribute;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>
 * Non-final Collection of {@code Attribute}s.
 * </p>
 * @author Alexander Kerner
 * @lastVist 2009-11-05
 * @see Attribute
 *
 */
public interface AttributeGroup extends Serializable, Iterable<Attribute> {

    List<Attribute> asList();

    List<String> getAsStringList();

    void add(Attribute attribute);

    void addAll(AttributeGroup group);

    void remove(Attribute attribute);

    boolean isEmpty();

    int getSize();

    boolean hasKey(String key);

    boolean hasKeyIgnoreCase(String key);

    boolean hasValue(String value);

    boolean hasValueIgnoreCase(String value);

    boolean contains(Attribute attribute);

    /**
	 * 
	 * @param key
	 * @return first occurrence of {@code Attribute} with Attribute.key.equals(key);
	 * @throws NoSuchElementException
	 */
    Attribute getAttributeByKey(String key);

    Attribute getAttributeByKeyIgnoreCase(String key);

    /**
	 * 
	 * @param value
	 * @return first occurrence of {@code Attribute} with Attribute.value.equals(value);
	 * @throws NoSuchElementException
	 */
    Attribute getAttributeByValue(String value);

    Attribute getAttributeByValueIgnoreCase(String value);
}
