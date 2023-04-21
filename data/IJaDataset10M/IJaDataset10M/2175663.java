package scamsoft.squadleader.server;

import java.util.*;
import java.io.Serializable;

/**
 * A Key used by clients to verify themselves to the server.
 * User: Andreas Mross
 * Date: 29-Jul-2003
 * Time: 08:43:20
 */
public class Key implements Serializable {

    private int id;

    public Key() {
        id = new Random().nextInt(Integer.MAX_VALUE);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        final Key key = (Key) o;
        if (id != key.id) return false;
        return true;
    }

    public int hashCode() {
        return id;
    }

    static final long serialVersionUID = -6282008990304763742L;
}
