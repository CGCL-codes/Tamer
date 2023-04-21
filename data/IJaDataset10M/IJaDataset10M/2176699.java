package br.ufal.npd.fox.indextype.primitive;

import br.ufal.npd.fox.indextype.FoxIndexTypeIF;

public class FoxIndexTypeFullText extends FoxIndexTypePrimitive {

    public FoxIndexTypeFullText() {
        super();
    }

    @Override
    public byte getCode() {
        return FoxIndexTypeIF.BYTE_INDEX_TYPE_FULL_TEXT;
    }
}
