package abc.weaving.residues;

import polyglot.util.InternalCompilerError;
import soot.*;
import soot.jimple.*;
import abc.soot.util.LocalGeneratorEx;
import abc.weaving.weaver.Weaver;
import abc.weaving.weaver.*;

/** A context value that comes directly from a 
 *  jimple value already in the current method
 *  @author Ganesh Sittampalam
 *  @author Ondrej Lhotak
 *  @date 30-Apr-04
 */
public class JimpleValue extends ContextValue {

    private Immediate value;

    public ContextValue inline(ConstructorInliningMap cim) {
        if (value instanceof Local) return new JimpleValue(cim.map((Local) value));
        if (value instanceof Constant) return new JimpleValue(value);
        throw new InternalCompilerError("Unhandled Immediate: " + value);
    }

    public JimpleValue(Immediate value) {
        if (value == null) throw new InternalCompilerError("JimpleValue constructed with null argument");
        this.value = value;
    }

    public String toString() {
        return "jimplevalue(" + value + ")";
    }

    public Type getSootType() {
        return value.getType();
    }

    public Value getSootValue() {
        Value v = (Value) abc.main.Main.v().getAbcExtension().getWeaver().getUnitBindings().get(value);
        if (v != null) return v; else return value;
    }
}
