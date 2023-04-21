package org.apache.fop.fo;

public interface ShorthandParser {

    Property getValueForProperty(String propName, Property.Maker maker, PropertyList propertyList);
}
