package games.script;

import java.util.List;
import org.luaj.vm.LuaErrorException;

public abstract class AbstractScript implements Script {

    public boolean callBooleanFunction(String name, Object... params) throws ScriptException {
        try {
            List<Object> retValues = this.callFunction(name, params);
            return (Boolean) retValues.get(0);
        } catch (LuaErrorException e) {
            throw new LuaScriptException(e);
        }
    }

    public String callStringFunction(String name, Object... params) throws ScriptException {
        try {
            List<Object> retValues = this.callFunction(name, params);
            return (String) retValues.get(0);
        } catch (LuaErrorException e) {
            throw new LuaScriptException(e);
        }
    }
}
