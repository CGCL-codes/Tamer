package net.sourceforge.hlm.impl.library.contexts;

import net.sourceforge.hlm.impl.generic.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.util.storage.*;

public abstract class ContextListImpl<T> extends SelectableListImpl<T> implements ContextList<T> {

    public ContextListImpl(StoredObject parent, int startIndex) {
        super(parent, startIndex);
    }
}
