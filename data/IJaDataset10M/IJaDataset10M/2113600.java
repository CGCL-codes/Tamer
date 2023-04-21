package ognl;

import java.util.*;

/**
 * Implementation of ElementsAccessor that returns an iterator over the map's values.
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
public class MapElementsAccessor implements ElementsAccessor {

    public Enumeration getElements(Object target) {
        return new IteratorEnumeration(((Map) target).values().iterator());
    }
}
