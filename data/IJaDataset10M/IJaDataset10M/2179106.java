package org.xmlcml.cml.map;

/**
 * catalogable object
 * retrievable from a catalog.
 * still being worked out
 * @author Peter Murray-Rust
 * @version 5.0
 * 
 */
public interface Indexable {

    /** ensure integrity of list and children.
	 * @return class of parent
	 */
    Class<?> getIndexableListClass();

    /** get id.
	 * @return id
	 */
    String getId();

    /** get ref.
	 * @return ref
	 */
    String getRef();
}
