package base.function.operator;

import base.function.CBooleanFunction;
import base.value.CValue;
import base.value.CValue.CType;

public class CEqual extends CBooleanFunction {

    @Override
    public void Init() {
        AssertParam(2);
    }

    @Override
    public boolean ToBoolean() {
        CValue Value0 = Param[0].GetValue();
        CValue Value1 = Param[1].GetValue();
        CType Type0 = Value0.GetType();
        CType Type1 = Value1.GetType();
        if (Type0 == CType.UserObject || Type1 == CType.UserObject) {
            if (Type0 != CType.UserObject || Type1 != CType.UserObject) Error("Объект пользователя можно сравнивать только с объектом пользователя");
            if (Value0.ToObject() == Value1.ToObject()) return true; else return false;
        } else if (Type0 == CType.Boolean || Type1 == CType.Boolean) {
            if (Type0 != CType.Boolean || Type1 != CType.Boolean) Error("Логическое значение можно сравнивать только с логическим значением");
            if (Value0.ToBoolean() == Value1.ToBoolean()) return true; else return false;
        } else if (Type0 == CType.String || Type1 == CType.String) {
            if (Value0.ToString().equals(Value1.ToString())) return true; else return false;
        } else if (Type0 == CType.Float || Type1 == CType.Float) {
            if (Value0.ToFloat() == Value1.ToFloat()) return true; else return false;
        } else {
            if (Value0.ToInt() == Value1.ToInt()) return true; else return false;
        }
    }
}
