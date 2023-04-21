package net.sourceforge.hlm.impl.library.formulae;

import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.generic.exceptions.*;
import net.sourceforge.hlm.impl.generic.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.util.storage.*;

public abstract class CaseListImpl<T> extends FixedListImpl<CaseList.Case<T>> implements CaseList<T> {

    public CaseListImpl(StoredObject parent, int startIndex) {
        super(parent, startIndex);
        if (this.isEmpty()) {
            try {
                this.add(null);
            } catch (DependencyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected short getTypeID() {
        return Id.CASE;
    }

    @Override
    public void move(CaseList.Case<T> item, CaseList.Case<T> before) throws DependencyException {
        super.move(item, before);
        this.cleanUp();
    }

    @Override
    public void remove(CaseList.Case<T> item) throws DependencyException {
        if (this.getCount() <= 1) {
            throw new IllegalArgumentException();
        }
        super.remove(item);
        this.cleanUp();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    protected void cleanUp() {
        CaseList.Case<T> lastItem = this.getLastItem();
        if (lastItem != null) {
            ((CaseImpl<T>) lastItem).cleanUp();
        }
    }
}
