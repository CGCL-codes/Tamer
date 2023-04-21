package com.gcapmedia.dab.epg.binary.decode;

import com.gcapmedia.dab.epg.binary.Element;

/**
 * Interface to implement in order to deconstruct an element into its
 * specific API context. 
 * 
 * Deconstructors are used in order to understand the generic layer built
 * after reading the binary data into Elements, Attributes and CData objects.
 * 
 * They should nest from each other as per the API.
 */
public interface Deconstructor<E> {

    /**
	 * Deconstruct an element into its specific API context
	 * @param element Element to deconstruct
	 * @return Deconstructor API object
	 */
    public E deconstruct(Element element);
}
