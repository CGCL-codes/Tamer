package whiteoak.lang.support;

import java.util.ArrayList;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import whiteoak.tools.openjdk.compiler.code.WhiteoakTypes;

public class MixinTag implements OperatorTag {

    private final OperatorTag inner = new UnionTag() {

        @Override
        protected void resolveConflict(WhiteoakTypes types, ArrayList<MethodSymbol> methods, int i, MethodSymbol a, int j, MethodSymbol b) {
            int canelledPosition = i;
            if (WhiteoakTypes.hasAttribute(a, types.syms.hasBehaviorAnnotationType) && !WhiteoakTypes.hasAttribute(b, types.syms.hasBehaviorAnnotationType)) canelledPosition = j;
            methods.set(canelledPosition, null);
        }
    };

    public Features evaluate(Features lms, Features rms, WhiteoakTypes types) {
        return inner.evaluate(lms, rms, types);
    }
}
