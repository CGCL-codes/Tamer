package j8086emu.model.progcode.instructions;

import j8086emu.model.exeptions.InvalidArgumentExeption;
import j8086emu.model.interfaces.Performable;
import j8086emu.model.interfaces.Variable;
import j8086emu.model.progcode.VarParser;

/**
 *
 * @author vlad
 */
public class Not extends Performable {

    private final int NUM_OF_ARGS = 1;

    private final VarParser varParser;

    public Not(VarParser varParser) {
        this.varParser = varParser;
    }

    public void perform(String msg) throws InvalidArgumentExeption {
        Variable[] var = varParser.getVar(msg, NUM_OF_ARGS);
        var[0].setData(~var[0].getData() + 1);
    }
}
