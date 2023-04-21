package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.gosh.Gosh;

public class GoshType extends AnyType {

    static final GoshType extent = new GoshType();

    public String getName() {
        return "gosh";
    }

    public CommonType asStored() {
        return CommonType.XANY;
    }

    public Object anyDefault(CommonRow onRow) throws AccessException {
        return super.anyDefault(onRow);
    }

    protected String readConvertString(CommonScope scope, CommonExpr expr) throws AccessException {
        MethodBlock block = (MethodBlock) readConvertAny(scope, expr);
        return block.getFormat();
    }
}
