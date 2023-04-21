package Absyn;

import java.io.Serializable;
import Symbol.Symbol;

/**
 * @author MaYunlei
 *
 */
public class CompBoolExp extends BoolExp implements Serializable {

    public Value v1;

    public Symbol comp;

    public Value v2;

    public CompBoolExp(Value v1, Symbol cop, Value v2) {
        this.v1 = v1;
        this.comp = cop;
        this.v2 = v2;
    }
}
