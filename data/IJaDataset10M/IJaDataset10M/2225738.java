package lang2.parser;

import java.util.Collection;
import java.util.Map;
import lang2.parser.Factory;
import lang2.parser.L2Factory;
import lang2.parser.WrapFactory;
import lang2.vm.Value;
import lang2.vm.Wrapper;

/**
 * @author gocha
 */
public class VarRefFactory extends WrapFactory {

    public VarRefFactory(Factory factory) {
        super(factory);
    }

    protected Map<String, Object> getMem() {
        Object o = this;
        while (true) {
            if (o instanceof L2Factory) {
                return ((L2Factory) o).getMemory();
            }
            if (o instanceof Wrapper) {
                o = ((Wrapper) o).unwrap();
                if (o == null) return null;
            } else {
                return null;
            }
        }
    }

    @Override
    public Value Function(Collection<String> args, Value body) {
        VRFunctionLog fun = new VRFunctionLog();
        fun.setMemory(getMem());
        fun.setParameterNames(args.toArray(new String[] {}));
        fun.setBody(body);
        return fun;
    }
}
