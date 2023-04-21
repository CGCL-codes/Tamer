package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.utils.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.common.table.indexing.IndexStrategy;
import com.gorillalogic.dal.model.*;

class EnumType extends IntType {

    private static int counter = 1;

    private String name = "Enum" + counter++;

    private com.gorillalogic.dal.model.Enum.Row metaRow;

    private CommonTable range;

    EnumType(com.gorillalogic.dal.model.Enum.Row metaRow, CommonTable range) {
        this.metaRow = metaRow;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected CommonType doCoerce(CommonType type, boolean subtract) throws AccessException {
        if (type == CommonType.XSTRING) return this;
        return super.doCoerce(type, subtract);
    }

    public CommonTable commonRange() {
        return range;
    }

    public Entity.Row metaRow() {
        return metaRow;
    }

    public boolean isValueType() {
        return range.isValueType();
    }

    String quoteForGCL(String s, boolean forceQuote) {
        return StringTransforms.quote(s, forceQuote);
    }

    public int convertToIntegral(CommonRow row, CommonExpr expr) throws AccessException {
        return convertToInt(expr.computeString(row));
    }

    int convertToInt(String key) throws ConversionException {
        try {
            CommonItr itr = range.commonLoopLock();
            while (itr.next()) {
                String next = itr.getString(0);
                if (key.equals(next)) {
                    return (int) itr.getRowId();
                }
            }
        } catch (ConversionException e) {
            throw e;
        } catch (AccessException e) {
            throw new InternalException(e);
        }
        return -1;
    }

    String convertToString(int ord) throws ConversionException {
        try {
            return range.commonRow(ord).getString(0);
        } catch (AccessException e) {
            throw new InternalException(e);
        }
    }

    protected String readConvertString(CommonScope scope, CommonExpr expr) throws AccessException {
        int ord = readConvertInt(scope, expr);
        return convertToString(ord);
    }

    protected void writeConvertString(CommonScope scope, CommonExpr expr, String value) throws AccessException {
        int ord = convertToInt(value);
        writeConvertInt(scope, expr, ord);
    }
}
