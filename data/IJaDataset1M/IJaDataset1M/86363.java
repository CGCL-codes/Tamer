package gnu.trove.impl.sync;

import gnu.trove.iterator.*;
import gnu.trove.procedure.*;
import gnu.trove.set.*;
import gnu.trove.list.*;
import gnu.trove.function.*;
import gnu.trove.map.*;
import gnu.trove.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Random;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class TSynchronizedIntSet extends TSynchronizedIntCollection implements TIntSet {

    private static final long serialVersionUID = 487447009682186044L;

    public TSynchronizedIntSet(TIntSet s) {
        super(s);
    }

    public TSynchronizedIntSet(TIntSet s, Object mutex) {
        super(s, mutex);
    }

    public boolean equals(Object o) {
        synchronized (mutex) {
            return c.equals(o);
        }
    }

    public int hashCode() {
        synchronized (mutex) {
            return c.hashCode();
        }
    }
}
