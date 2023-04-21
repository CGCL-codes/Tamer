package tm.clc.ast;

import tm.clc.datum.AbstractRefDatum;

public abstract class TyAbstractRef extends TyAbstractPointer {

    public TyAbstractRef() {
        super();
    }

    public TyAbstractRef(TypeNode pointeeTp) {
        super(pointeeTp);
    }

    public int getNumBytes() {
        return AbstractRefDatum.size;
    }
}
