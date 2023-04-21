package j8086emu.model.progcode.instructions;

import j8086emu.model.exeptions.InvalidArgumentExeption;
import j8086emu.model.interfaces.Performable;
import j8086emu.model.interfaces.Variable;
import j8086emu.model.progcode.VarParser;
import j8086emu.model.progcode.types.VarTypes;

/**
 * @author sone
 */
public class Mov extends Performable {

    private final int NUM_OF_ARGS = 2;

    private VarParser varParser;

    private Variable[] var;

    public Mov(VarParser varParser) {
        this.varParser = varParser;
    }

    public void perform(String msg) throws InvalidArgumentExeption {
        var = varParser.getVar(msg, NUM_OF_ARGS);
        if (isValidTypes(var)) {
            var[0].setData(var[1].getData());
        } else {
            System.out.println(var[0].getType().toString() + " " + var[1].getType().toString());
            throw new InvalidArgumentExeption("");
        }
    }

    private boolean isType0(VarTypes type) {
        return var[0].getType() == type;
    }

    private boolean isType1(VarTypes type) {
        return var[1].getType() == type;
    }

    private boolean isValidTypes(Variable[] var) {
        if (isType0(VarTypes.reg16) && isType1(VarTypes.db)) return true;
        if (isType0(VarTypes.reg16) && isType1(VarTypes.reg16)) return true;
        if (isType0(VarTypes.mem) && isType1(VarTypes.reg16)) return true;
        if (isType0(VarTypes.reg16) && isType1(VarTypes.mem)) return true;
        if (isType0(VarTypes.reg16) && isType1(VarTypes.dw)) return true;
        if (isType0(VarTypes.reg8) && isType1(VarTypes.db)) return true;
        if (isType0(VarTypes.mem) && isType1(VarTypes.dw)) return true;
        if (isType0(VarTypes.mem) && isType1(VarTypes.db)) return true;
        if (isType0(VarTypes.accum) && isType1(VarTypes.mem)) return true;
        if (isType0(VarTypes.segreg) && isType1(VarTypes.reg16)) return true;
        if (isType0(VarTypes.segreg) && isType1(VarTypes.mem)) return true;
        if (isType0(VarTypes.reg16) && isType1(VarTypes.segreg)) return true;
        if (isType0(VarTypes.mem) && isType1(VarTypes.segreg)) return true;
        return false;
    }
}
