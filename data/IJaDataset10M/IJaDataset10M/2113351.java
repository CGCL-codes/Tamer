package org.jz.jpaxs.frames;

import java.util.List;

/**
 *
 * @author GZSVM
 */
public class ConstantUserFilter extends UserFilter {

    private String fFieldName;

    private int fComparisionType;

    private Object fValue;

    private boolean fIsNegative;

    public ConstantUserFilter(String _FieldName, int _ComparisionType, Object _Value, boolean _IsNegative) {
        fFieldName = _FieldName;
        fComparisionType = _ComparisionType;
        fValue = _Value;
        fIsNegative = _IsNegative;
    }

    @Override
    public void chargeInstanceList(List _InstanceList) {
    }

    @Override
    public void bind() {
    }
}
