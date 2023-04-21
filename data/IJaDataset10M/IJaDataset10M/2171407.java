package base.function;

import base.module.CModule;
import base.value.CValue;
import base.value.CValue.CType;
import parser.CParser;

public abstract class CFunction extends CModule {

    @Override
    protected void AssertParam(int ParamsQuantity) {
        if (Param == null) {
            if (ParamsQuantity != 0) Error("Функция \"" + CParser.ModuleNames.get(this.getClass()) + "\" не должна иметь параметров");
        } else {
            if (Param.length != ParamsQuantity) Error("Функция \"" + CParser.ModuleNames.get(this.getClass()) + "\" " + ParamString(ParamsQuantity, "на"));
        }
    }

    @Override
    protected void AssertParam(int[] Array) {
        for (int ParamsQuantity : Array) if (Param.length == ParamsQuantity) return;
        ParamArrayError(Array, true);
    }

    public CType NumericType(CValue Value) {
        CType Type = Value.GetType();
        if (Type == CType.String) if (Value.ToString().contains(".")) return CType.Float; else return CType.Int;
        if (Type == CType.Boolean) return CType.Int;
        return Type;
    }
}
