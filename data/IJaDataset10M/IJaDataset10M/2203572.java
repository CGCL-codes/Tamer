package org.sablecc.sablecc;

import java.util.*;

public class ListCast implements Cast {

    public static final ListCast instance = new ListCast();

    private ListCast() {
    }

    public Object cast(Object o) {
        return (List) o;
    }
}
