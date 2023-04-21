package uk.ac.ox.oucs.oxpoints.gaboto.entities;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.ac.ox.oucs.oxpoints.gaboto.entities.Place;

/**
 * Gaboto generated Entity.
 * @see net.sf.gaboto.generation.GabotoGenerator
 */
public class Space extends Place {

    private static Map<String, List<Method>> indirectPropertyLookupTable;

    static {
        indirectPropertyLookupTable = new HashMap<String, List<Method>>();
    }

    @Override
    public String getType() {
        return "http://ns.ox.ac.uk/namespace/oxpoints/2009/02/owl#Space";
    }

    protected List<Method> getIndirectMethodsForProperty(String propertyURI) {
        List<Method> list = super.getIndirectMethodsForProperty(propertyURI);
        if (list == null) return indirectPropertyLookupTable.get(propertyURI); else {
            List<Method> tmp = indirectPropertyLookupTable.get(propertyURI);
            if (tmp != null) list.addAll(tmp);
        }
        return list;
    }
}
