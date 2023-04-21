package uk.ac.lkl.common.util.collections.event;

import java.util.EventObject;
import uk.ac.lkl.common.util.collections.CountMap;

public class CountMapEvent<O> extends EventObject {

    private O object;

    public CountMapEvent(CountMap<O> source, O object) {
        super(source);
        this.object = object;
    }

    public O getObject() {
        return object;
    }
}
