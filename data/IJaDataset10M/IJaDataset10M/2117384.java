package org.geonetwork.domain.csw202.discovery;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author heikki doeleman
 *
 */
public class ElementSetName {

    private ElementSet elementSet;

    private List<String> typeNames;

    public ElementSet getElementSet() {
        return elementSet;
    }

    public void setElementSet(ElementSet elementSet) {
        this.elementSet = elementSet;
    }

    public List<String> getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(List<String> typeNames) {
        this.typeNames = typeNames;
    }

    /**
	 * For Jixb binding.
	 *
	 * @return
	 */
    @SuppressWarnings("unused")
    private static List<String> typeNamesFactory() {
        return new ArrayList<String>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((typeNames == null) ? 0 : typeNames.hashCode());
        result = prime * result + ((elementSet == null) ? 0 : elementSet.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ElementSetName)) {
            return false;
        }
        ElementSetName other = (ElementSetName) obj;
        if (typeNames == null) {
            if (other.typeNames != null) return false;
        } else if (!typeNames.equals(other.typeNames)) return false;
        if (elementSet == null) {
            if (other.elementSet != null) return false;
        } else if (!elementSet.equals(other.elementSet)) return false;
        return true;
    }
}

class ElementSetNameConversion {

    public static List<String> deserializeTypeNames(String text) {
        if (text == null) {
            return null;
        } else {
            String[] split = text.split(",");
            List<String> typeNames = new ArrayList<String>();
            for (String s : split) {
                typeNames.add(s);
            }
            return typeNames;
        }
    }

    public static String serializeTypeNames(List<String> typeNames) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < typeNames.size(); i++) {
            if (i > 0) {
                buff.append(',');
            }
            buff.append(typeNames.get(i));
        }
        return buff.toString();
    }
}
