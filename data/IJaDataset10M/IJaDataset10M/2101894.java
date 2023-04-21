package org.apache.fop.fo.expr;

import org.apache.fop.fo.Property;

public class FromParentFunction extends FunctionBase {

    public int nbArgs() {
        return 1;
    }

    public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
        String propName = args[0].getString();
        if (propName == null) {
            throw new PropertyException("Incorrect parameter to from-parent function");
        }
        return pInfo.getPropertyList().getFromParent(propName);
    }
}
