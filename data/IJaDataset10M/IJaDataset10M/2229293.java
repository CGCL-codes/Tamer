package tomPack;

import java.util.ArrayList;
import java.util.Collection;

public class TomArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 3715085547695599050L;

    public TomArrayList() {
        super();
    }

    public TomArrayList(Collection<? extends E> c) {
        super(c);
    }

    public TomArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public TomArrayList(E[] eArray) {
        super();
        for (int i = 0; i < eArray.length; i++) {
            add(eArray[i]);
        }
    }
}
