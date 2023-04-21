package org.marcont.mcpi.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Michal Wozniak
 */
public class ListManagement {

    public static List merge(List a, List b) {
        List c = new ArrayList();
        for (Iterator it = a.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (!c.contains(o)) c.add(o);
        }
        for (Iterator it = b.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (!c.contains(o)) c.add(o);
        }
        return c;
    }
}
