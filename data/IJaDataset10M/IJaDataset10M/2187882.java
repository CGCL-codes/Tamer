package base.value.userobject;

import base.function.CObjectFunction;

public class CParent extends CObjectFunction {

    @Override
    public CUserObject ToObject() {
        return Param[0].ToObject().Parent;
    }
}
