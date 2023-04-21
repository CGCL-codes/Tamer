package squirrel_util;

import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (03.02.01 23:32:29)
 * @author: 
 */
public class ExpiryEvent extends EventObject {

    public static final int EXPIRED = 1;

    public static final int UNREGISTERED = 2;

    private final int id;

    /**
 * ExpiryEvent constructor comment.
 * @param source java.lang.Object
 */
    public ExpiryEvent(Expirable source, int id) {
        super(source);
        this.id = id;
    }

    public final Expirable getExpirable() {
        return (Expirable) getSource();
    }

    /**
 * Insert the method's description here.
 * Creation date: (03.02.01 23:49:35)
 * @return int
 */
    public final int getId() {
        return id;
    }
}
