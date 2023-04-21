package org.imogene.android.common.filter;

import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.imogene.android.util.FormatHelper;

public class IntegerFilter extends NumberFilter<Integer> {

    public static final Creator<IntegerFilter> FILTER_CREATOR = new DefaultCreator<IntegerFilter>() {

        @Override
        protected IntegerFilter newFilter() {
            return new IntegerFilter();
        }

        @Override
        protected IntegerFilter newFilter(ClientFilterCursor c) {
            return new IntegerFilter(c);
        }
    };

    private IntegerFilter() {
        super();
    }

    private IntegerFilter(ClientFilterCursor c) {
        super(c);
    }

    @Override
    protected Integer toType(String value) {
        return FormatHelper.toInteger(value);
    }

    @Override
    protected NumberOperator fromOperator(String str) {
        return NumberOperator.fromOpInt(str);
    }

    @Override
    protected String operator(NumberOperator operator) {
        return operator.operatorInt();
    }
}
