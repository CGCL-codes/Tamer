package net.sf.molae.pipe.comod;

import java.util.ConcurrentModificationException;

/**
 * Counter implementation of ModificationMark.
 * @version 1.0
 * @author <a href="mailto:ralph.wagner@web.de">Ralph Wagner</a>
 */
public final class ModificationCounter implements ModificationMark {

    private int counter = 0;

    private final ModificationCounter mother;

    private ModificationCounter(ModificationCounter mother) {
        this.mother = mother;
    }

    private ModificationCounter() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * @return a new instance of this class.
     */
    public static ModificationCounter getInstance() {
        return new ModificationCounter();
    }

    public ModificationMark createChild() {
        return new ModificationCounter(this);
    }

    public void checkForComodification() {
        if ((mother != null) && (counter != mother.counter)) {
            throw new ConcurrentModificationException();
        }
    }

    public void markModification() {
        if (mother != null) {
            counter = mother.counter;
        } else {
            counter++;
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        buf.append(getClass());
        buf.append(" mother:" + mother);
        buf.append(" counter:" + counter);
        buf.append(")");
        return buf.toString();
    }
}
