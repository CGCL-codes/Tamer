package fmpp.dataloaders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import bsh.Interpreter;
import fmpp.Engine;
import fmpp.tdd.DataLoader;

/**
 * Evaluates a BeanShell expression (looks like as Java).
 * The scrip has access to the <code>Engine</code> object by the
 * <code>engine</code> variable.
 */
public class EvalDataLoader implements DataLoader {

    public Object load(Engine e, List args) throws Exception {
        int ln = args.size();
        if (ln < 1 || ln > 2) {
            throw new IllegalArgumentException("eval(script[, vars]) needs 1 or 2 arguments.");
        }
        String script;
        Object o = args.get(0);
        if (!(o instanceof String)) {
            throw new IllegalArgumentException("The 1st parameter to eval(script[, vars])" + "must be a string, but it was a " + fmpp.tdd.Interpreter.getTypeName(o) + ".");
        }
        script = (String) o;
        Interpreter intp = new Interpreter();
        intp.set("engine", e);
        if (ln > 1) {
            o = args.get(1);
            if (!(o instanceof Map)) {
                throw new IllegalArgumentException("The 2nd parameter to eval(script[, vars])" + "must be a hash, but it was a " + fmpp.tdd.Interpreter.getTypeName(o) + ".");
            }
            Map vars = (Map) o;
            Iterator it = vars.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry ent = (Map.Entry) it.next();
                intp.set((String) ent.getKey(), ent.getValue());
            }
        }
        return intp.eval(script);
    }
}
