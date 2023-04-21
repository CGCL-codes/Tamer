package blomo.script.scriplet;

import java.util.Map;
import blomo.script.Scriplet;
import blomo.script.ScriptParser;

/**
 * @author Malte Schulze
 *
 */
public class Var extends Scriplet {

    @Override
    public Object execute(Object vars[], Map<Object, Object> parameter) {
        Map<Object, Object> namespace = ScriptParser.getNamespace((String) vars[0]);
        if (namespace == null) return null;
        return namespace.get(vars[1]);
    }
}
