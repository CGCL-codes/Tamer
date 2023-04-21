package com.iver.cit.gvsig.fmap.drivers.dgn;

/**
 * Clase utilizada para guardar un elemento de tipo TagValue.
 *
 * @author Vicente Caballero Navarro
 */
public class DGNElemTagValue extends DGNElemCore {

    public int tagType;

    public int tagSet;

    public int tagIndex;

    public int tagLength;

    public tagValueUnion tagValue = new tagValueUnion();
}
