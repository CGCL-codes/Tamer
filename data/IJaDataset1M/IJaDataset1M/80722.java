package base.function.operator;

import base.function.CBooleanFunction;

public class COr extends CBooleanFunction {

    @Override
    public void Init() {
        AssertParam(2);
    }

    @Override
    public boolean ToBoolean() {
        return Param[0].ToBoolean() || Param[1].ToBoolean();
    }
}
